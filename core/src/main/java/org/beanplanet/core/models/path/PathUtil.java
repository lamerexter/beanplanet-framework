/*
 *  MIT Licence:
 *
 *  Copyright (C) 2020 Beanplanet Ltd
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

package org.beanplanet.core.models.path;

import static org.beanplanet.core.models.path.NamePath.EMPTY_NAME_PATH;
import static org.beanplanet.core.models.path.Path.EMPTY_PATH;

/**
 * Path based utility class.
 */
public class PathUtil {
    /**
     * Returns the empty path.
     *
     * @return an empty path.
     */
    @SuppressWarnings("unchecked")
    public static <T> Path<T> emptyPath() {
        return (Path<T>)EMPTY_PATH;
    }

    /**
     * Returns the empty name path.
     *
     * @return an empty name path.
     */
    @SuppressWarnings("unchecked")
    public static NamePath emptyNamePath() {
        return (NamePath)EMPTY_NAME_PATH;
    }

}
