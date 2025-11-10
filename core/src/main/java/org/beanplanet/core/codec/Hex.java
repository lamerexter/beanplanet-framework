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

package org.beanplanet.core.codec;

import org.beanplanet.core.io.IoException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * A hexadecimal codec which converts octets to <a href="https://en.wikipedia.org/wiki/Hexadecimal">base-16 or hexadecimal
 * representation</a>. This codec uses a default character set of {@link java.nio.charset.StandardCharsets#US_ASCII}
 * given that the output text is always in that character range.
 */
public class Hex implements Encoder, Decoder {
    public static final Charset DEFAULT_CHARSET = StandardCharsets.US_ASCII;
    private static final String HEXDIGITS_STR = "0123456789ABCDEF";
    static final char[] HEXDIGITS = HEXDIGITS_STR.toLowerCase().toCharArray(); // We produce lowercase hex text
    private static final Hex _instance = new Hex();

    /**
     * Performs hexadecimal encoding of the input to a string.
     *
     * @param input the input whose bytes are to be read and hexadecimal encoded.
     * @return a string of hexadecimal encoded bytes from the input.
     */
    public static String encodeToHexString(byte[] input) {
        return input == null ? null : new String(_instance.encode(input), DEFAULT_CHARSET);
    }

    /**
     * Performs hexadecimal decoding of the input to a byte array.
     *
     * @param input the input string whose bytes are to be read and hexadecimal decoded.
     * @return hexadecimal decoded bytes from the input.
     */
    public static byte[] decodeFromHexString(String input) {
        return input == null ? null : _instance.decode(input.getBytes(DEFAULT_CHARSET));
    }

    /**
     * Performs hexadecimal encoding of the input to the given output stream. All input bytes are consumed until end-of-file (EOF).
     *
     * @param input  the input stream whose bytes are to be read and hexadecimal encoded.
     * @param output the output stream where hexadecimal encoded bytes are to be written.
     * @throws IOException if an I/O error occurs reading from the input or writing to the output.
     */
    @Override
    public void encode(final InputStream input, final OutputStream output) throws IOException {
        int b;
        while ((b = input.read()) >= 0) {
            output.write(HEXDIGITS[(b & 0xf0) >> 4]);
            output.write(HEXDIGITS[b & 0x0f]);
        }
        output.flush();
    }

    /**
     * Performs decoding of the input to the given output resources. An input stream is opened from the given input
     * resource and all input bytes are consumed and decoded until end-of-file (EOF). Decoded bytes are written to
     * an output stream opened on the given output resource.
     *
     * @param input  the input resource whose bytes are to be read and decoded.
     * @param output the output resource where decoded bytes are to be written.
     * @throws IOException if an I/O error occurs reading from the input or writing to the output.
     */
    @Override
    public void decode(InputStream input, OutputStream output) throws IOException {
        int b1;
        while ((b1 = input.read()) >= 0) {
            int b2 = input.read();
            if (b2 < 0) {
                throw new IoException("Invalid hexadecimal encoding - reached EOF before presence of second nibble.");
            }

            output.write((byte) ((HEXDIGITS_STR.indexOf(b1 >= 'a' ? b1 - 32 : b1) << 4) + HEXDIGITS_STR.indexOf(b2 >= 'a' ? b2 - 32 : b2)));
        }
        output.flush();
    }

    public static char hexDigit(int b) {
        return HEXDIGITS[b & 0xF];
    }
}
