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
