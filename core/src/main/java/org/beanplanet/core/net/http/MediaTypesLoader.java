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

package org.beanplanet.core.net.http;

import org.beanplanet.core.io.IoException;
import org.beanplanet.core.io.resource.Resource;
import org.beanplanet.core.io.resource.UrlResource;
import org.beanplanet.core.logging.Logger;
import org.beanplanet.core.models.Registry;
import org.beanplanet.core.models.RegistryLoader;
import org.beanplanet.core.util.EnumerationUtil;
import org.beanplanet.core.util.StringUtil;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.*;

public class MediaTypesLoader implements RegistryLoader<Registry<String, MediaType>>, Logger {
    @Override
    public void loadIntoRegistry(Registry<String, MediaType> registry) {
        for (Resource mediaTypeResource : mediaTypeResources()) {
            int numberOfMediaTypesLoaded = 0;
            Properties mediaTypeProperties = new Properties();
            try (Reader propertiesReader = mediaTypeResource.getReader()) {
                mediaTypeProperties.load(propertiesReader);

                for (Map.Entry<Object, Object> mediaTypeEntry : mediaTypeProperties.entrySet()) {
                    String mediaTypeName = (String) mediaTypeEntry.getKey();
                    String mediaTypeInfo = (String) mediaTypeEntry.getValue();
                    List<String> mediaTypeInfoDetails = StringUtil.asDsvList(mediaTypeInfo, "|");
                    if ( mediaTypeInfoDetails.size() != 2) {
                        warning("Ignoring invalid Media Type information for entry [{0}={1}] in {2}: expected 2 delimted (|) columns and found {3}", mediaTypeName, mediaTypeInfo, mediaTypeResource, mediaTypeInfoDetails.size());
                        continue;
                    }

                    // TODO: Add back description and known file extensions here.
                    registry.addToRegistry(mediaTypeName, new MediaType(mediaTypeName));
                    numberOfMediaTypesLoaded++;
                }
            } catch (IOException ioEx) {
                throw new IoException(ioEx);
            }
            info("Loaded {0} media types from resource {1}", numberOfMediaTypesLoaded, mediaTypeResource);
        }
    }

    private Iterable<Resource> mediaTypeResources() {
        Set<Resource> resourceSet = new LinkedHashSet<>();
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader classLoader = getClass().getClassLoader();
        if (contextClassLoader != null) {
            findAndAddResources(resourceSet, contextClassLoader);
        }
        if (contextClassLoader != classLoader) {
            findAndAddResources(resourceSet, classLoader);
        }
        return resourceSet;
    }

    private void findAndAddResources(Set<Resource> resourceSet, ClassLoader classLoader) {
        try {
            for (URL mediaTypesResourceUrl : EnumerationUtil.toList(classLoader.getResources("META-INF/services/org/beanplanet/mediatypes/media-types.properties"))) {
                resourceSet.add(new UrlResource(mediaTypesResourceUrl));
            }
        } catch (IOException e) {
            throw new IoException(e);
        }
    }
}
