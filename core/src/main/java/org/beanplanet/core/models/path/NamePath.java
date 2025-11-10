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
