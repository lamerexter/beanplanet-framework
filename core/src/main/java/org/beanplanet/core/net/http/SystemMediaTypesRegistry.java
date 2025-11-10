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

import org.beanplanet.core.UncheckedException;
import org.beanplanet.core.models.AbstractRegistry;
import org.beanplanet.core.models.Registry;
import org.beanplanet.core.models.RegistryLoader;
import org.beanplanet.core.util.MultiValueListMap;
import org.beanplanet.core.util.MultiValueListMapImpl;

import java.util.Collections;
import java.util.List;

public class SystemMediaTypesRegistry extends AbstractRegistry<String, MediaType> implements MediaTypeRegistry {
    private static final MediaTypeRegistry instance;
    private final MultiValueListMap<String, MediaType> fileExtensionToMediaType = new MultiValueListMapImpl<>();

    static {
        SystemMediaTypesRegistry systemMediaTypesRegistry = new SystemMediaTypesRegistry();
        instance = systemMediaTypesRegistry;
        try {
            systemMediaTypesRegistry.startup();
        } catch (Exception e) {
            throw new UncheckedException("Unable to load media type: ", e);
        }

    }
    @SuppressWarnings("unchecked,rawtypes")
    private SystemMediaTypesRegistry() {
        super((RegistryLoader)new MediaTypesLoader());
    }

    public static MediaTypeRegistry getInstance() {
        return instance;
    }

    @Override
    public List<MediaType> findMediaTypesForFileExtension(String extension) {
        checkLoaded();

        return fileExtensionToMediaType.getOrDefault(extension.toLowerCase(), Collections.emptyList());
    }

    @Override
    public List<String> findFileExtensionsForMediaType(String name) {
        checkLoaded();

        MediaType found = lookup(name);
        throw new UnsupportedOperationException();
//        return found != null ? found.getFileExtensions() : Collections.emptyList();
    }

    @Override
    public boolean addToRegistry(String key, MediaType mediaType) {
        return super.addToRegistry(key, mediaType);
    }

    @Override
    public boolean addToRegistry(String key, MediaType mediaType, List<String> fileExtensions) {
        boolean added = addToRegistry(key, mediaType);
        fileExtensions.stream().map(String::toLowerCase).forEach(ext -> fileExtensionToMediaType.addValue(ext, mediaType));
        return added;
    }

    @Override
    public boolean removeFromRegistry(String key) {
        final MediaType found = lookup(key);
        boolean removed = super.removeFromRegistry(key);
        throw new UnsupportedOperationException();
//        if (found != null) {
//            found.getFileExtensions().forEach(ext -> fileExtensionToMediaType.removeValue(ext, found));
//        }
//        return removed;
    }

    public static void main(String ... args) throws Exception {
        new SystemMediaTypesRegistry().startup();
    }
}
