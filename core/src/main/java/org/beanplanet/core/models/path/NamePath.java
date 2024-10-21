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

import org.beanplanet.core.util.StringUtil;

import static java.util.Collections.singletonList;

public interface NamePath extends Path<String> {
    NamePath EMPTY_NAME_PATH = new DelimitedNamePath("", "");

    /**
     * Joins the name path into a single path string, with path elements delimited by the given delimiter.
     * @param delimiter used to delim path elements.
     * @return a path string of all elements, delimited by the given delimiter.
     */
    default String join(String delimiter) {
        return StringUtil.asDelimitedString(getElements(), delimiter);
    }

    /**
     * <p>
     * Joins the given path to a singleton element.
     * </p>
     *
     * @param element the singleton element to be joined to this path.
     * @return a new Namepath, consisting of all the elememts of this path plus the singleton element specified as the last element.
     * @see #singletonPath(String)
     */
    default NamePath joinSingleton(String element) {
        return new SimpleNamePath(getElements()).join(singletonPath(element));
    }

    static NamePath singletonPath(String element) {
        return new SimpleNamePath(singletonList(element));
    }
}
