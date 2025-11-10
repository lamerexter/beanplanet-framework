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

/**
 * The path component of a URI, following the specification of URIs outlined in
 * <a href="https://www.rfc-editor.org/rfc/rfc3986">URI: General Syntax</a>
 */
public class UriPath extends DelimitedNamePath {
    final boolean isAbsolute;

    public UriPath(String path) {
        super(StringUtil.lTrim(path, "/"), "/");
        this.isAbsolute = path != null && path.startsWith("/");
    }

    /**
     * Whether this path is empty.
     *
     * @return true if this path is absolute or contains path elments, false otherwise.
     */
    public boolean isEmpty() {
        return !isAbsolute && super.isEmpty();
    }

    @Override
    public boolean isAbsolute() {
        return isAbsolute;
    }
}
