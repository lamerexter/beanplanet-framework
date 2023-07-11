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

package org.beanplanet.core.mediatypes;

import org.beanplanet.core.UncheckedException;
import org.beanplanet.core.models.AbstractRegistry;
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
    private SystemMediaTypesRegistry() {
        super(new MediaTypesLoader());
    }

    public static MediaTypeRegistry getInstance() {
        return instance;
    }

    @Override
    public List<MediaType> findMediaTypesForFileExtension(String extension) {
        checkLoaded();

        return fileExtensionToMediaType.getOrDefault(extension, Collections.emptyList());
    }

    @Override
    public List<String> findFileExtensionsForMediaType(String name) {
        checkLoaded();

        MediaType found = lookup(name);
        return found != null ? found.getFileExtensions() : Collections.emptyList();
    }

    @Override
    public boolean addToRegistry(String key, MediaType mediaType) {
        boolean added = super.addToRegistry(key, mediaType);
        mediaType.getFileExtensions().forEach(ext ->fileExtensionToMediaType.addValue(ext, mediaType));
        return added;
    }

    @Override
    public boolean removeFromRegistry(String key) {
        final MediaType found = lookup(key);
        boolean removed = super.removeFromRegistry(key);
        if (found != null) {
            found.getFileExtensions().forEach(ext -> fileExtensionToMediaType.removeValue(ext, found));
        }
        return removed;
    }

    public static void main(String ... args) throws Exception {
        new SystemMediaTypesRegistry().startup();
    }
}
