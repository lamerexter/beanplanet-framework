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

package org.beanplanet.core.mediatypes;

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
