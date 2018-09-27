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

import org.beanplanet.core.models.Named;

import java.util.List;

/**
 * Definition of a content type (holderf of MIME type information).
 *
 * @author Gary Watson
 *
 */
public interface MediaType extends Named {
    /**
     * Returns the base of the content type name. For the JPEG image content type, <b>image</b>/jpeg, the base name is
     * <b>image</b>.
     *
     * @return the base name part of the content type name.
     */
    String getBaseType();

    /**
     * Returns the subtype name of the content type name. For the JPEG image content type, image/<b>jpeg</b>, the base
     * name is <b>jpeg</b>.
     *
     * @return the subtype name part of the content type name.
     */
    String getSubType();

    /**
     * Returns the canonical name of the media type. Although a media type may have more than one known type name, this
     * is the most well-known name for the type.
     */
    default String getName() {
        return getBaseType()+"/"+getSubType();
    }
}

