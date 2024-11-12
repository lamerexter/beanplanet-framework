/*
 *  MIT Licence:
 *
 *  Copyright (C) 2019 Beanplanet Ltd
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

package org.beanplanet.core.net.http.converter;

import org.beanplanet.core.io.IoException;
import org.beanplanet.core.io.IoUtil;
import org.beanplanet.core.lang.FilteringPackageClassScanner;
import org.beanplanet.core.lang.PackageResourceScanner;
import org.beanplanet.core.lang.TypeUtil;
import org.beanplanet.core.lang.conversion.TypeConversionException;
import org.beanplanet.core.logging.Logger;
import org.beanplanet.core.models.Registry;
import org.beanplanet.core.models.RegistryLoader;
import org.beanplanet.core.net.http.MediaType;
import org.beanplanet.core.net.http.converter.annotations.HttpMessageBodyConverter;
import org.beanplanet.core.util.EnumerationUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.beanplanet.core.util.CollectionUtil.nullSafe;
import static org.beanplanet.core.util.StringUtil.asCsvList;

public class HttpMessageBodyHandlerLoader implements RegistryLoader<Registry<MediaType, org.beanplanet.core.net.http.converter.HttpMessageBodyConverter<?>>>, Logger {
    public static final String HANDLER_PACKAGES_RESOURCE = "META-INF/services/org/beanplanet/net/http/message-body-converter-packages.txt";

    protected static final Predicate<Class<?>> HANDLER_ANNOTATED_TYPE_FILTER = type -> type.isAnnotationPresent(HttpMessageBodyConverter.class);

    protected PackageResourceScanner<Class<?>> packageScanner = new FilteringPackageClassScanner(HANDLER_ANNOTATED_TYPE_FILTER);

    @SuppressWarnings("unchecked")
    @Override
    public void loadIntoRegistry(Registry<MediaType, org.beanplanet.core.net.http.converter.HttpMessageBodyConverter<?>> registry) {
        Set<Class<?>> discoveredHandlerClasses = messageBodyHandlerPackageResources();

        List<org.beanplanet.core.net.http.converter.HttpMessageBodyConverter> handlers = discoveredHandlerClasses.stream()
                                                                                                                 .map(handlerClazz -> {
                                                                                      try {
                                                                                          Object o = TypeUtil.instantiateClass(handlerClazz);
                                                                                          if (!(o instanceof org.beanplanet.core.net.http.converter.HttpMessageBodyConverter)) {
                                                                                              warning("Ignoring apparent configured HTTP message body I/O handler [{0}]: as it is not an instance of one! ", handlerClazz);
                                                                                              return null;
                                                                                          }

                                                                                          return (org.beanplanet.core.net.http.converter.HttpMessageBodyConverter)o;
                                                                                      } catch (Exception ex) {
                                                                                          warning("Unable to instantiate HTTP message body I/O handler [{0}]: does it exist and have a no-arg constructor? Further information: ", handlerClazz, ex.getMessage());
                                                                                          return null;
                                                                                      }
                                                                                  })
                                                                                                                 .filter(Objects::nonNull)
                                                                                                                 .map(org.beanplanet.core.net.http.converter.HttpMessageBodyConverter.class::cast)
                                                                                                                 .collect(Collectors.toList());
        handlers.forEach(h -> h.getSupportedMediaTypes().forEach(m -> registry.addToRegistry((MediaType) m, h)));
    }

    private Set<Class<?>> messageBodyHandlerPackageResources() {
        debug("Looking for HTTP message body I/O handlers [{0}] ...", HANDLER_PACKAGES_RESOURCE);
        Set<Class<?>> resourceSet = new LinkedHashSet<>();
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader classLoader = getClass().getClassLoader();
        if (contextClassLoader != null) {
            findAndAddResources(resourceSet, contextClassLoader);
        }
        if (contextClassLoader != classLoader) {
            findAndAddResources(resourceSet, classLoader);
        }
        debug("Discovered total of {0} HTTP message body I/O handlers in packages specified in {1}", resourceSet.size(), HANDLER_PACKAGES_RESOURCE);
        return resourceSet;
    }

    private void findAndAddResources(Set<Class<?>> resourceSet, ClassLoader classLoader) {
        try {
            Set<String> packagesInResource = new LinkedHashSet<>();
            for (URL handlerPackagesResource : EnumerationUtil.toList(classLoader.getResources(HANDLER_PACKAGES_RESOURCE))) {
                debug("Discovered HTTP message body I/O handler package resource [{0}]", handlerPackagesResource);

                findAndAddPackages(packagesInResource, classLoader);
            }

            for(String handlerPkg : packagesInResource) {
                resourceSet.addAll(packageScanner.findResourcesInPackage(handlerPkg, classLoader));
            }
        } catch (IOException e) {
            throw new IoException("Unable to load HTTP message body handlers: ", e);
        }
    }

    protected void findAndAddPackages(Set<String> packageNames, ClassLoader cl) {
        if (isDebugEnabled()) {
            debug("Searching for annotation-configured HTTP message I/O handler packages [classLoader=" + cl.getClass() + ", resource(s)="
                    + HANDLER_PACKAGES_RESOURCE + "] ...");
        }

        BufferedReader reader = null;
        try {
            for (String resourcePath : nullSafe(asCsvList(HANDLER_PACKAGES_RESOURCE))) {
                for (Enumeration<URL> resources = cl.getResources(resourcePath); resources.hasMoreElements();) {
                    URL resourceURL = resources.nextElement();
                    if (isDebugEnabled()) {
                        debug("Reading HTTP message I/O handler packages from resource [" + resourceURL.toExternalForm() + "] ...");
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
            throw new TypeConversionException("Unable to HTTP message I/O handler packages names ["
                    + HANDLER_PACKAGES_RESOURCE + "] through classloader [" + cl.getClass().getName()
                    + "]: ",
                    ioEx);
        } finally {
            IoUtil.closeIgnoringErrors(reader);
        }
    }

}
