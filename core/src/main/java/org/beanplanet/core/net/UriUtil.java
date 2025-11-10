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

package org.beanplanet.core.net;

import org.beanplanet.core.models.path.Path;
import org.beanplanet.core.models.path.UriPath;
import org.beanplanet.core.util.StringUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * A static utility containing URI-related functionality.
 */
public class UriUtil {
    public static String encode(final String decoded) {
        return encode(decoded, "UTF-8");
    }

    public static String encode(final String decoded, final String encoding) {
        try {
            return decoded == null ? null : URLEncoder.encode(decoded, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new NetworkException(format("An error occurred URL encoding [%s]: ", decoded), e);
        }
    }

    public static String decode(final String encoded) {
        return decode(encoded, "UTF-8");
    }

    public static String decode(final String encoded, final String encoding) {
        try {
            return encoded == null ? null : URLDecoder.decode(encoded, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new NetworkException(format("An error occurred URL decoding [%s]: ", encoded), e);
        }
    }

    /**
     * <p>
     * Merges the path elements of URIs to form a combined hierarchical path, following
     * the specification of URIs outlined in <a href="https://www.rfc-editor.org/rfc/rfc3986">URI:
     * General Syntax</a>
     * </p>
     * <p>
     * Examples:
     * <pre>
     * null + null = null
     * "" + null = ""
     * "" + "" = ""
     * / + null = /
     * / + / = /
     * /a + / = /          // Second is absolute, overriding first
     * /a + /b = /b
     * /a + b = /a/b
     * </pre>
     * </p>
     * @param path1 the first path, which may be null, an absolute or relative URI path.
     * @param path2 the second path, which may be null, an absolute or relative URI path.
     * @return
     */
    public static String mergePaths(final String path1, final String path2) {
        if (path1 == null && path2 == null) return null;
        if (path1 != null && StringUtil.isBlank(path2)) return path1;

        UriPath uriPath1 = new UriPath(path1);
        UriPath uriPath2 = new UriPath(path2);
        Path<String> resolved = uriPath1.resolve(uriPath2);
        if ( resolved == null ) return null;

        return (uriPath1.isAbsolute() || uriPath2.isAbsolute() ? "/" : "")+ String.join("/", resolved.getElements());
    }
}
