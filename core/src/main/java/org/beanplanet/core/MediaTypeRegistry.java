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

package org.beanplanet.core;

import org.beanplanet.core.models.Registry;

import java.util.List;
import java.util.Optional;

/**
 * <a href="https://www.iana.org/assignments/media-types/media-types.xhtml">Media Type</a>, as defined by the <a href="https://www.iana.org">Internet Assigned Numbers Authority (IANA)</a>. Represents
 * most widely-known types of content such as text (text/plain), HTML (text/html), images (image/png amongst others) and more.
 */
public interface MediaTypeRegistry extends Registry<String, MediaType> {
    List<MediaType> findMediaTypesForFileExtension(String extension);

    default Optional<MediaType> findMediaTypeForFileExtension(String extension) {
        return findMediaTypesForFileExtension(extension).stream().findFirst();
    }

    List<String> findFileExtensionsForMediaType(String name);

    default Optional<String> findFileExtensionForMediaType(String name) {
        return findFileExtensionsForMediaType(name).stream().findFirst();
    }
}
