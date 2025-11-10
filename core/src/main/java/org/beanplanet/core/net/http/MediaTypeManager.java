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

import java.util.List;
import java.util.Optional;

public interface MediaTypeManager {
    /**
     * Finds the media type for the given media type name.
     *
     * @param mediaTypeName the media type name of the media type to be found.
     * @return optionally, the media type found.
     */
    Optional<MediaType> findMediaType(String mediaTypeName);

    /**
     * Returns the known file extensions (not including the leading dot) for the given media type. For plain/text documents, for
     * example, these might be <b>txt</b> and <b>text</b>.
     *
     * @return the known content types of the content type, in preference order with first preference first.
     */
    List<MediaType> findMediaTypesOfFileExtensions(MediaType mediaType);

    /**
     * Returns the known media type names that are commonly-known aliases of the given media type. For JPEG images, for example, these might be
     * <code>image/jpeg</code> and <code>image/jpg</code>.
     *
     * @param aliasType the media type for which all other alias media types are to be determined.
     * @return all of the media types that are known alises of the given media type, including the one specified, in order of precedence.
     */
    List<MediaType> findAllMediaTypes(MediaType aliasType);
}
