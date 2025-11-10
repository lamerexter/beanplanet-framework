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

package org.beanplanet.core.net.http;

import org.beanplanet.core.lang.Assert;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.beanplanet.core.codec.Hex.hexDigit;

/**
 * MIME (Multipurpose Internet Mail Extensions) encoding and decoding functionality, as specified
 * by multiple RFCs:
 * <dl>
 * <dt>RFC 2045</dt><dd>Part One: Format of Internet Message Bodies</dd>
 * <dt>RFC 2047</dt><dd>Part Three: Message Header Extensions for Non-ASCII Text</dd>
 * </dl>
 */
public class MimeCodec {
    /**
     * A set of the Quoted-Printable characters (characters that may be transferred as-is, as determined by RFC 2045, Section 6.7.
     */
    private static final BitSet Q_PRINTABLE = new BitSet(256);


    static {
        // RFC 2045, Section 6.7, and RFC 2047, Section 4.2
        Q_PRINTABLE.set(33, 127);
        Q_PRINTABLE.clear(61); // = as used in Q spec, RFC2045, Section 4.2 (1)
        Q_PRINTABLE.clear(63); // ? excluded in Q spec, RFC2045, Section 4.2 (3)
        Q_PRINTABLE.clear(95); // _ excluded in Q spec, RFC2045, Section 4.2 (2,3)
    }

    /**
     * Encode the given text according to the Quoted-Printable method described in RFC 2045, Section 6.7 and
     * RFC 2047, Section 4.2.
     *
     * @param text    the text to be encoded, which may not be null.
     * @param charset the charset to be applied to the encoding and specified in the encoding itself, which may not be null.
     * @return the Quoted-Printable encoded representation of the text.
     * @see <a href="https://tools.ietf.org/html/rfc2047">RFC 2047</a>, Sections 1 and 2
     */
    public static String encodeQuotedPrintable(String text, Charset charset) {
        Assert.notNull(text, "The text to Quoted-Printable encode may not be null");
        Assert.notNull(charset, "The character set to apply in Quoted-Printable encoding may not be null");

        byte[] textBytes = text.getBytes(charset);
        StringBuilder sb = new StringBuilder(textBytes.length << 1);
        sb.append("=?").append(charset.name()).append("?Q?");
        for (byte b : textBytes) {
            if (b == 32) { // RFC 2047, Section 4.2(2)
                sb.append('_');
            } else if (isQuotePrintable(b)) {
                sb.append((char) b);
            } else {
                sb.append('=');
                char ch1 = hexDigit(b >> 4);
                char ch2 = hexDigit(b);
                sb.append(ch1);
                sb.append(ch2);
            }
        }
        sb.append("?=");
        return sb.toString();
    }

    private static boolean isQuotePrintable(byte b) {
        // Use unsigned octet byte range 0-255, converting -1 .. -128 to positive range 255 .. 128, if necessary
        return Q_PRINTABLE.get(b < 0 ? b + 256 : b);
    }

    /**
     * Escapes special characters in Quoted-Printable text. Normally non US-ASCII based characters should be encoded
     * via {@link #encodeQuotedPrintable(String, Charset)}, but this method may be used where the character set is
     * either US-ASCII or unknown (assumed to be compatible).
     *
     * @param text the text whose special characters are to be escaped.
     * @return the Quoted-Printable version of the given text.
     */
    public static String encodeQuotedPrintableCharacters(String text) {
        if (text.indexOf('"') == -1 && text.indexOf('\\') == -1) {
            return text;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '"' || c == '\\') {
                sb.append('\\');
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * Encodes the text of HTTP header parameter values according to the guidelines set out in RFC 6266, Section 5 and RFC 5987,
     * Section 3.2.1. Other than included characters (attr-char), permitted without quoting, this is essentially percent
     * encoding.
     *
     * @param parameterValue the header field param text.
     * @param charset        the charset of the header field param string,
     *                       only the US-ASCII, UTF-8 and ISO-8859-1 charsets are supported
     * @return the encoded header field param
     * @see <a href="https://www.rfc-editor.org/rfc/rfc6266">RFC 5266</a> and <a href="https://tools.ietf.org/html/rfc5987">RFC 5987</a>
     */
    public static String rfc6266EncodeHttpHeaderParameter(String parameterValue, Charset charset) {
        Assert.notNull(parameterValue, "The HTTP header parameter value text may not be null");
        Assert.notNull(charset, "The HTTP header parameter value character set may not be null");

        Assert.isFalse(StandardCharsets.US_ASCII.equals(charset), "ASCII does not require encoding");
        Assert.isTrue(UTF_8.equals(charset) || ISO_8859_1.equals(charset), "Only UTF-8 and ISO-8859-1 are supported (See RFC 5987, Section 3.2)");

        byte[] source = parameterValue.getBytes(charset);
        StringBuilder sb = new StringBuilder(source.length << 1);
        sb.append(charset.name());
        sb.append("''");
        for (byte b : source) {
            if (isRfc5987AttrChar(b)) {
                sb.append((char) b);
            } else {
                // Percent encode the character
                sb.append('%');
                char hex1 = hexDigit(b >> 4);
                char hex2 = hexDigit(b);
                sb.append(hex1);
                sb.append(hex2);
            }
        }
        return sb.toString();
    }


    /**
     * Whether a given octet represents an RFC 5987 Attribute Character (attr-char), as specified in the grammar
     * in Section 3.2.1.
     *
     * @param b the byte/octet to be tested as being an <code>attr-char</code> (not requiring to be quoted under the scheme).
     * @return true if the octet is in the set of <code>attr-char</code>, false otherwise.
     */
    private static boolean isRfc5987AttrChar(byte b) {
        return (b >= 'a' && b <= 'z') || (b >= 'A' && b <= 'Z') // Alpha
                || (b >= '0' && b <= '9') // Digit
                || b == '!' || b == '#' || b == '$' || b == '&' || b == '+' || b == '-' || b == '.'
                || b == '^' || b == '_' || b == '`' || b == '|' || b == '~'; // Token, except ( "*" | "'" | "%" )
    }
}
