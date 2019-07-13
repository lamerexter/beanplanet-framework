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

import org.beanplanet.core.models.Described;
import org.beanplanet.core.models.Named;

import java.util.List;

/**
 * <a href="https://www.iana.org/assignments/media-types/media-types.xhtml">Media Type</a>, as defined by the <a href="https://www.iana.org">Internet Assigned Numbers Authority (IANA)</a>. Represents
 * most widely-known types of content such as text (text/plain), HTML (text/html), images (image/png amongst others) and more.
 */
public interface MediaType extends Named, Described {
    /**
     * Gets the base of the media type name. For the JPEG image content type, <b>image</b>/jpeg, the base name is
     * <b>image</b>.
     *
     * @return the base name part of the media type name.
     */
    String getBaseType();

    /**
     * Gets the subtype name of the media type name. For the JPEG image content type, image/<b>jpeg</b>, the base
     * name is <b>jpeg</b>.
     *
     * @return the subtype name part of the media type name.
     */
    String getSubType();

    /**
     * Gets the known file extensions (not including the leading dot) for the content type. For text documents, for
     * example, these might be <b>txt</b> and <b>text</b>.
     *
     * @return the known content types of the media type, in preference order with first preference first, which
     * may be empty but never null.
     */
    List<String> getFileExtensions();
}
