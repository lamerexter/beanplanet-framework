/*
 *  MIT Licence:
 *
 *  Copyright (C) 2018 Beanplanet Ltd
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without restriction
 *  including without limitation the rights to use, copy, modify, merge,
 *  publish, distribute, sublicense, and/or sell copies of the Software,
 *  and to permit persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 *  KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 *  PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 *  CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  DEALINGS IN THE SOFTWARE.
 */
package org.beanplanet.core.lang.conversion;

import org.beanplanet.core.io.IoUtil;
import org.beanplanet.core.lang.FilteringPackageClassScanner;
import org.beanplanet.core.lang.PackageResourceScanner;
import org.beanplanet.core.lang.TypeUtil;
import org.beanplanet.core.logging.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;

import static org.beanplanet.core.util.CollectionUtil.nullSafe;
import static org.beanplanet.core.util.StringUtil.asCsvList;
import static org.beanplanet.core.util.StringUtil.asDelimitedString;

public class PackageScanTypeConverterLoader implements TypeConverterLoader, Logger {
   public static final String TYPE_CONVERTER_PACKAGES_RESOURCE = "META-INF/services/org/beanplanet/converters/type-converter-packages.txt";

   protected String typeConverterPackagesResource = TYPE_CONVERTER_PACKAGES_RESOURCE;

//   protected SeverityLogger logger = BeanPlanetLogFactory.getSeverityLogger(PackageScanTypeConverterLoader.class);
//
   protected static final Predicate<Class<?>> ANNOTATED_CLASS_FILTER = new Predicate<Class<?>>() {
      private static final long serialVersionUID = 1L;

      public boolean test(Class<?> clazz) {
         return clazz.isAnnotationPresent(org.beanplanet.core.lang.conversion.annotations.TypeConverter.class);
      }

      public String toString() {
         return "AnnotatedClassPredicate[annotation="+TypeConverter.class.getName()+"]";
      }
   };
   protected static final Predicate<Class<?>> TYPE_CONVERTER_CLASS_FILTER = new Predicate<Class<?>>() {
      private static final long serialVersionUID = 1L;

      public boolean test(Class<?> clazz) {
         return TypeConverter.class.isAssignableFrom(clazz);
      }

      public String toString() {
         return "TypeConverterClassPredicate[typeConverterClass="+TypeConverter.class.getName()+"]";
      }
   };
   protected static final Predicate<Class<?>> PACKAGE_SCAN_CLASS_FILTER = TYPE_CONVERTER_CLASS_FILTER.or(ANNOTATED_CLASS_FILTER);

   protected PackageResourceScanner<Class<?>> packageScanner = new FilteringPackageClassScanner(PACKAGE_SCAN_CLASS_FILTER);

   private static final Predicate<Method> ANNOTATED_METHOD_FILTER = new Predicate<Method>() {
      private static final long serialVersionUID = 1L;

      public boolean test(Method method) {
         Class<?> paramTypes[] = method.getParameterTypes();
         return Modifier.isPublic(method.getModifiers())
                && !Modifier.isAbstract(method.getModifiers())
                && method.isAnnotationPresent(org.beanplanet.core.lang.conversion.annotations.TypeConverter.class)
                && paramTypes != null
                && paramTypes.length <= 2
                && paramTypes[0] != Void.class
                && paramTypes[0] != void.class
                && (paramTypes.length == 1 || (paramTypes[1] == Class.class));
      }
   };
//
//   public PackageScanTypeConverterLoader() {
//   }
//
//   public PackageScanTypeConverterLoader(String typeConverterPackagesResource) {
//      setTypeConverterPackagesResource(typeConverterPackagesResource);
//   }
//
   /**
    * @return the typeConverterPackagesResource
    */
   public String getTypeConverterPackagesResource() {
      return typeConverterPackagesResource;
   }

   /**
    * @param typeConverterPackagesResource the typeConverterPackagesResource to set
    */
   public void setTypeConverterPackagesResource(String typeConverterPackagesResource) {
      this.typeConverterPackagesResource = typeConverterPackagesResource;
   }

   public void load(TypeConverterRegistry registry) {
      // ------------------------------------------------------------------------
      // Add context class loader packages first, if available
      // ------------------------------------------------------------------------
      String packageNames[] = findPackageNames();
      if ( packageNames.length > 0 ) {
         if (isDebugEnabled()) {
            debug("Discovered type converter "
                  + (packageNames.length == 1 ? "package" : "packages")
                  + ": \n" + asDelimitedString(packageNames, ",\n"));
         }

         Set<Class<?>> classes = packageScanner.findResourcesInPackages(packageNames);
         loadTypeConverters(registry, classes);

         if (isDebugEnabled()) {
            debug("Loaded " + registry.size() + " type converters from "
                         + classes.size() + (classes.size() == 1 ? " class in " : " classes in ")
                         + packageNames.length + (packageNames.length == 1 ? " package " : " packages ")
                         + "into registry");
         }
      }
   }
//
//   /**
//    * @return the packageScanner
//    */
//   public PackageResourceScanner<Class<?>> getPackageScanner() {
//      return packageScanner;
//   }
//
//   /**
//    * @param packageScanner the packageScanner to set
//    */
//   public void setPackageScanner(PackageResourceScanner<Class<?>> packageScanner) {
//      this.packageScanner = packageScanner;
//   }
//
   protected String[] findPackageNames() {
      Set<String> packageNamesSet = new LinkedHashSet<String>();
      ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
      ClassLoader classLoader = getClass().getClassLoader();
      if (contextClassLoader != null) {
         findAndAddPackages(packageNamesSet, contextClassLoader);
      }
      if (contextClassLoader != classLoader) {
         findAndAddPackages(packageNamesSet, classLoader);
      }
      return packageNamesSet.toArray(new String[packageNamesSet.size()]);
   }

   protected void findAndAddPackages(Set<String> packageNames, ClassLoader cl) {
      if (isDebugEnabled()) {
         debug("Searching for annotation-configured type converters [classLoader=" + cl + ", resource(s)="
                      + getTypeConverterPackagesResource() + "] ...");
      }

      BufferedReader reader = null;
      try {
         for (String resourcePath : nullSafe(asCsvList(getTypeConverterPackagesResource()))) {
            for (Enumeration<URL> resources = cl.getResources(resourcePath); resources.hasMoreElements();) {
               URL resourceURL = resources.nextElement();
               if (isDebugEnabled()) {
                  debug("Reading type converter packages from resource [" + resourceURL.toExternalForm() + "] ...");
               }
               reader = new BufferedReader(new InputStreamReader(resourceURL.openStream()));
               for (String line = null; (line = reader.readLine()) != null;) {
                  line = line.trim();
                  if (line.length() == 0 || line.startsWith("#")) {
                     continue;
                  }
                  List<String> linePackageNames = asCsvList(line);
                  if (linePackageNames != null) {
                     packageNames.addAll(linePackageNames);
                  }
               }
            }
         }
      } catch (IOException ioEx) {
         throw new TypeConversionException("Unable to load type converter package names ["
                                                 + getTypeConverterPackagesResource() + "] through classloader [" + cl
                                                 + "]: ",
                                           ioEx);
      } finally {
         IoUtil.closeIgnoringErrors(reader);
      }
   }

   protected void loadTypeConverters(TypeConverterRegistry registry, Set<Class<?>> classes) {
      Set<Class<?>> visitedClasses = new HashSet<>();
      for (Class<?> clazz : classes) {
         loadTypeConverters(visitedClasses, registry, clazz);
      }
   }

   protected void loadTypeConverters(Set<Class<?>> visitedClasses, TypeConverterRegistry registry, Class<?> clazz) {
      // Determine whether the class is itself a type converter, has type converter annotations or both
      if (TYPE_CONVERTER_CLASS_FILTER.test(clazz)) {
         try {
            registry.addConverter((TypeConverter) clazz.newInstance());
         } catch (Exception ex) {
            warning("Ignoring " + "problematic" + " type converter [class=" + clazz.getName()
                           + "] which could not be instantiated: ", ex);
         }
      }

      if (ANNOTATED_CLASS_FILTER.test(clazz)) {
         loadAnnotatedTypeConverters(visitedClasses, registry, clazz);
      }
   }

   protected void loadAnnotatedTypeConverters(Set<Class<?>> visitedClasses,
                                              TypeConverterRegistry registry,
                                              Class<?> clazz) {
      if (visitedClasses.contains(clazz)) {
         return;
      }

      TypeUtil.getMethodsInClassHierarchy(clazz).filter(ANNOTATED_METHOD_FILTER).forEach(m -> {
         try {
            registry.addConverter(m.getParameterTypes()[0],
                                  m.getReturnType(),
                                  Modifier.isStatic(m.getModifiers()) ?
                                  new StaticMethodTypeConverter(m) :
                                  new InstanceMethodTypeConverter(m.getDeclaringClass().newInstance(), m));
         } catch (InstantiationException | IllegalAccessException ex) {
            error("Ignoring problematic type converter candidate method [" + m + "] which could not be instantiated: ", ex);
         }
      });

      visitedClasses.add(clazz);
   }
}
