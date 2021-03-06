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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

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
}
