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

import org.beanplanet.core.io.FileUtil;
import org.beanplanet.core.io.resource.Resource;
import org.beanplanet.core.models.Registry;
import org.beanplanet.core.net.http.converter.HttpMessageBodyConverter;

import java.util.List;
import java.util.Optional;

import static org.beanplanet.core.util.StringUtil.isNotBlank;

/**
 * <a href="https://www.iana.org/assignments/media-types/media-types.xhtml">Media Type</a>, as defined by the <a href="https://www.iana.org">Internet Assigned Numbers Authority (IANA)</a>. Represents
 * most widely-known types of content such as text (text/plain), HTML (text/html), images (image/png amongst others) and more.
 */
public interface MediaTypeRegistry extends Registry<String, MediaType> {
    boolean addToRegistry(String key, MediaType mediaType, List<String> fileExtensions);

    List<MediaType> findMediaTypesForFileExtension(String extension);

    default Optional<MediaType> findMediaTypeForFileExtension(String extension) {
        return findMediaTypesForFileExtension(extension).stream().findFirst();
    }

    default Optional<MediaType> findMediaTypeOfFile(String filename) {
        return FileUtil.hasFilenameExtension(filename) ? findMediaTypesForFileExtension(FileUtil.getFilenameSuffix(filename)).stream().findFirst() : Optional.empty();
    }

    default Optional<MediaType> findMediaType(Resource resource) {
        return isNotBlank(resource.getName()) ? findMediaTypeOfFile(resource.getName()) : Optional.empty();
    }

    List<String> findFileExtensionsForMediaType(String name);

    default Optional<String> findFileExtensionForMediaType(String name) {
        return findFileExtensionsForMediaType(name).stream().findFirst();
    }
}
