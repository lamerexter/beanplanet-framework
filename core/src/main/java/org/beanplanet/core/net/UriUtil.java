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
