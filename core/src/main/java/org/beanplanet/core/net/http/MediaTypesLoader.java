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

package org.beanplanet.core.net.http;

import org.beanplanet.core.io.IoException;
import org.beanplanet.core.io.resource.Resource;
import org.beanplanet.core.io.resource.UrlResource;
import org.beanplanet.core.logging.Logger;
import org.beanplanet.core.models.RegistryLoader;
import org.beanplanet.core.util.EnumerationUtil;
import org.beanplanet.core.util.StringUtil;

import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.*;

import static org.beanplanet.core.util.StringUtil.isNotBlank;

public class MediaTypesLoader implements RegistryLoader<MediaTypeRegistry>, Logger {
    @Override
    public void loadIntoRegistry(MediaTypeRegistry registry) {
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

                    List<String> fileExtensions = isNotBlank(mediaTypeInfoDetails.get(1)) ? StringUtil.asCsvList(mediaTypeInfoDetails.get(1)) : Collections.emptyList();
                    registry.addToRegistry(mediaTypeName, new MediaType(mediaTypeName), fileExtensions);
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
