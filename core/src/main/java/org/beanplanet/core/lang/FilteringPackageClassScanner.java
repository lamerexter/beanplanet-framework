/*
 * Copyright (c) 2001-present the original author or authors (see NOTICE herein).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.beanplanet.core.lang;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.lang.ClassNotFoundException;

import org.beanplanet.core.io.FileUtil;
import org.beanplanet.core.io.IoUtil;
import org.beanplanet.core.logging.Logger;
import org.beanplanet.core.util.StringUtil;

import static org.beanplanet.core.Predicates.truePredicate;


public class FilteringPackageClassScanner implements PackageResourceScanner<Class<?>>, Logger {
   protected Predicate<Class<?>> filter = truePredicate();
   /** The class loaders to be used to scan the packages. */
   protected Set<ClassLoader> classLoaders;
   
   
   public FilteringPackageClassScanner() {
   }

   public FilteringPackageClassScanner(Predicate<Class<?>> filter) {
      this();
      this.filter = filter;
   }

   /**
    * Returns the filter that will accept resources.
    * 
    * @return the filter configured to accept or reject package resources.
    */
   public Predicate<Class<?>> getFilter() {
      return filter;
   }

   /**
    * Sets the filter that will accept resources.
    * 
    * @param filter the filter configured to accept or reject package resources.
    */
   public void setFilter(Predicate<Class<?>> filter) {
      this.filter = filter;
   }

   /**
    * @return the classLoaders
    */
   public Set<ClassLoader> getClassLoaders() {
      return classLoaders;
   }
   
   public void addClassLoader(ClassLoader classLoader) {
      Assert.notNull(classLoaders, "The class loaders set may not be null when adding a new class loader");
      classLoaders.add(classLoader);
   }

   /**
    * @param classLoaders the classLoaders to set
    */
   public void setClassLoaders(Set<ClassLoader> classLoaders) {
      this.classLoaders = classLoaders;
   }

   /**
    * Scans the packages, whose names are determined by the given strategy, for resources of the required type according to the
    * filter provided.
    *
    * @param packageSourceStrategy the strategy used to determine the package names
    * @return a set of the resources found in the given packages, or the empty set if no resources were found.
    */
   public Set<Class<?>> findResourcesInPackages(PackageSourceStrategy packageSourceStrategy) {
      return findResourcesInPackages(packageSourceStrategy.findPackages());
   }


   /**
    * Scans the specified named packages for resources of the required type according to the
    * filter provided.
    * 
    * @param packageNames the names of the packages to be scanned
    * @return a set of the resources found in the given packages, or the empty set if no resources were found.
    */
   public Set<Class<?>> findResourcesInPackages(String... packageNames) {
      if ( packageNames == null || packageNames.length == 0 ) {
         return Collections.emptySet();
      }
      if ( isDebugEnabled() ) {
         debug("Scanning package for classes [packages="+StringUtil.asDelimitedString(packageNames, ",")+", filter="+getFilter()+"] ...");
      }
      
      
      Set<Class<?>> resources = new LinkedHashSet<Class<?>>();
      for (String packageName : packageNames) {
         packageName = StringUtil.replaceAllRegex(packageName, "\\.", "/");
         filterResources(resources, filter, packageName);
      }
      
      if ( isDebugEnabled() ) {
         debug("Package scan found "+resources.size()
                      +(resources.size() == 1 ? " class in " : " classes in ")
                      +packageNames.length+(packageNames.length == 1 ? " package: \n" : " packages: \n")
                      +StringUtil.asDelimitedString(resources, ",\n"));
      }

      return resources;
   }

   @Override
   public Set<Class<?>> findResourcesInPackage(String packageName, ClassLoader classLoader) {
      Set<Class<?>> resources = new LinkedHashSet<Class<?>>();
      packageName = StringUtil.replaceAllRegex(packageName, "\\.", "/");
      filterResources(resources, filter, packageName, classLoader);

      return resources;
   }

   protected void filterResources(Set<Class<?>> resources, Predicate<Class<?>> filter, String packageName) {
      Set<ClassLoader> classLoaders = (this.classLoaders != null ? this.classLoaders : getDefaultClassLoaders());
      
      for (ClassLoader classLoader : classLoaders) {
         filterResources(resources, filter, packageName, classLoader);
      }
   }

   protected void filterResources(Set<Class<?>> resources, Predicate<Class<?>> filter, String packageName, ClassLoader classLoader) {
      try {
         for (Enumeration<URL> resourceURLs = getClassLoaderResources(classLoader, packageName); resourceURLs.hasMoreElements(); ) {
            URL resourceURL = resourceURLs.nextElement();

            String resourcePath = resourceURL.getPath(); //NetworkUtil.URLDecode(resourceURL.getPath(), "UTF-8");
            if ( "file".equals(resourceURL.getProtocol()) ) {
               // Decode resource path using the URI class - it provides better decoding that URLDecoder. For example, the
               // plus (+) character is decoded by URLDecoder but not by URI.getPath(); + is a valid character in
               // some file-systems such as MacOS.
               try {
                  resourcePath = new URI(resourcePath).getPath();
               }
               catch (URISyntaxException ignoredEx) {
                  // Handled below using the original resourcePath above.
               }

               File packageFile = new File(resourcePath);
               if ( !packageFile.isDirectory() ) {
                  warning("Skipping package file [file="+packageFile.getAbsolutePath()+", URL="+resourceURL+"] which does not exist, could not be read or is not a directory.");
                  continue;
               }

               if ( isDebugEnabled() ) {
                  debug("Looking for classes in package directory ["+packageFile.getAbsolutePath()+"] recursively ...");
               }
               loadResourcesInDirectory(resources, filter, packageName, classLoader, packageFile);
            }
            else {
               // Assume it's a JAR or Zip file
               if ( resourcePath.indexOf('!') > 0 ) {
                  resourcePath = resourcePath.substring(0, resourcePath.indexOf('!'));
               }
               if ( isDebugEnabled() ) {
                  debug("Looking for classes in archive [archive="+resourcePath+"] recursively ...");
               }
               URL archiveURL = new URL(resourcePath);
               loadResourcesInArchive(resources, filter, packageName, classLoader, archiveURL.openStream());
            }
         }
      } catch (IOException ioEx) {
         warning("Error scanning package [package="+packageName+", classLoader="+classLoader+"] for resources: ", ioEx);
      }
   }
   
   protected Enumeration<URL> getClassLoaderResources(ClassLoader classLoader, String packageName) throws IOException {
      // Some class loaders, such as URLClassLoader, require a trailing slash when requesting resources.
      packageName = StringUtil.ensureHasSuffix(packageName, "/");
      
      return classLoader.getResources(packageName);
   }
   
   protected void loadResourcesInDirectory(Set<Class<?>> resources, Predicate<Class<?>> filter, String packageName, ClassLoader classLoader, File packageDirectory) {
      StringBuilder s = new StringBuilder();
      File files[] = packageDirectory.listFiles();
      
      for (File file : files) {
         s.setLength(0);
         String filename = file.getName();
         if ( filename != null ) {
            filename = filename.trim();
            s.append(packageName).append("/").append(filename);
            String packageOrClass = (packageName == null ? filename : s.toString());
            
            if ( file.isDirectory()) {
               loadResourcesInDirectory(resources, filter, packageOrClass, classLoader, file);
            }
            else if ( "class".equalsIgnoreCase(FileUtil.getFilenameSuffix(file)) ) {
               addIfResourceMatches(resources, filter, packageOrClass, classLoader);
            }
         }
      }
   }
   
   protected void loadResourcesInArchive(Set<Class<?>> resources, Predicate<Class<?>> filter, String packageName, ClassLoader classLoader, InputStream archiveIS) throws IOException {
      JarInputStream jis = null;
      
      try {
         jis = new JarInputStream(archiveIS);
         JarEntry entry = null;
         while ( (entry = jis.getNextJarEntry()) != null ) {
            String filename = entry.getName();
            if ( filename != null ) {
               filename = filename.trim();
               if ( !entry.isDirectory() && filename.startsWith(packageName) && "class".equalsIgnoreCase(FileUtil.getFilenameSuffix(filename)) ) {
                  addIfResourceMatches(resources, filter, filename, classLoader);
               }
            }
         }
      }
      finally {
         IoUtil.closeIgnoringErrors(jis);
      }
   }
   
   protected void addIfResourceMatches(Set<Class<?>> resources, Predicate<Class<?>> filter, String packageAndClassName, ClassLoader classLoader) {
      packageAndClassName = StringUtil.rTrim(packageAndClassName, ".class");
      packageAndClassName = StringUtil.replaceAllRegex(packageAndClassName, "/", ".");
      try {
         Class<?> clazz = classLoader.loadClass(packageAndClassName);
         
         if ( filter.test(clazz) && !resources.contains(clazz) ) {
            resources.add(clazz);
            if ( isDebugEnabled() ) {
               debug("Found and added class [class="+clazz.getName()+", classLoader="+classLoader.getClass().getName()+"] matching filter");
            }
         }
      }
      catch (ClassNotFoundException cnfEx) {
         if ( isDebugEnabled() ) {
            debug("Ignoring class which could not be loaded through classloader [class="+packageAndClassName+", classLoader="+classLoader.getClass().getName()+"]: ", cnfEx);
         }
      }
      catch (NoClassDefFoundError ncdEx) {
         if ( isDebugEnabled() ) {
            debug("Ignoring class which could not be loaded through classloader [class="+packageAndClassName+", classLoader="+classLoader.getClass().getName()+"]: ", ncdEx);
         }
      }
   }

   protected Set<ClassLoader> getDefaultClassLoaders() {
      Set<ClassLoader> classLoaders = new LinkedHashSet<ClassLoader>();
      ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
      ClassLoader classLoader = getClass().getClassLoader();
      if ( contextClassLoader != null ) {
         classLoaders.add(contextClassLoader);
      }
      if ( contextClassLoader != classLoader ) {
         classLoaders.add(classLoader);
      }
      return classLoaders;
   }
}
