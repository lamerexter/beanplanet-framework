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
import org.beanplanet.core.io.resource.ByteArrayResource;
import org.beanplanet.core.io.resource.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * A utility for the ubiquitous base-64 codec which converts binary octets to
 * <a href="https://en.wikipedia.org/wiki/Base64">base-64</a> text. This codec uses a default character set of
 * {@link java.nio.charset.StandardCharsets#US_ASCII} given that the output text is always in that character range.
 *
 * @see Base64Codec
 */
public class Base64 {
    private static final Base64Codec _instance = new Base64Codec();

    /**
     * Private utility class constructor.
     */
    private Base64() {
    }

    /**
     * Base-64 decodes all of the input to the given output stream. All input bytes are consumed until end-of-file (EOF).
     *
     * @param input  the input stream whose bytes are to be read and decoded.
     * @param output the output stream where encoded bytes are to be written.
     * @throws IOException if an I/O error occurs reading from the input or writing to the output.
     * @see Base64Codec#decode(InputStream, OutputStream)
     */
    public static void decode(final InputStream input, final OutputStream output) throws IOException {
        _instance.decode(input, output);
    }

    /**
     * Base-64 decodes bytes.
     *
     * @param input the bytes to be read and decoded.
     * @return the input bytes decoded.
     * @see Base64Codec#decode(byte[])
     */
    public static byte[] decode(byte[] input) {
        try {
            return _instance.decode(new ByteArrayResource(input)).readFullyAsBytes();
        } catch (IOException e) {
            throw new IoException(e);
        }
    }

    /**
     * Base-64 decodes an input string. This method assumes an encoding of "US-ASCII" of the input string.
     *
     * @param input the string to decode
     * @return the base-64 decoded form of the input string
     * @see Base64Codec#decode(String)
     */
    public static byte[] decode(String input) {
        return _instance.decode(input.getBytes(StandardCharsets.US_ASCII));
    }

    /**
     * Base-64 encodes of the input to the given output stream. All input bytes are consumed until end-of-file (EOF).
     *
     * @param input  the input stream whose bytes are to be read and encoded.
     * @param output the output stream where encoded bytes are to be written.
     * @throws IOException if an I/O error occurs reading from the input or writing to the output.
     * @see Base64Codec#encode(Resource, Resource)
     */
    public static void encode(InputStream input, OutputStream output) throws IOException {
        _instance.encode(input, output);
    }

    /**
     * Base-64 encodes bytes.
     *
     * @param input the bytes to be read and encoded.
     * @return the input bytes encoded.
     * @see Base64Codec#encode(byte[])
     */
    public static String encode(byte[] input) {
        try {
            return _instance.encode(new ByteArrayResource(input)).readFullyAsString(StandardCharsets.US_ASCII);
        } catch (IOException e) {
            throw new IoException(e);
        }
    }

    /**
     * Base-64 encodes a string using the platform default character set.
     *
     * @param input the bytes to be read and encoded.
     * @return the input bytes encoded.
     * @see Base64Codec#encode(byte[])
     */
    public static String encode(String input) {
        return encode(input.getBytes());
    }

    /**
     * Base-64 encodes all the input to the given output resources. An input stream is opened from the given input
     * resource and all input bytes are consumed and encoded until end-of-file (EOF). Encoded bytes are written to
     * an output stream opened on the given output resource.
     *
     * @param input  the input resource whose bytes are to be read and encoded.
     * @param output the output resource where encoded bytes are to be written.
     * @throws IOException if an I/O error occurs reading from the input or writing to the output.
     * @see Base64Codec#encode(Resource, Resource)
     */
    public static Resource encode(Resource input, Resource output) throws IOException {
        return _instance.encode(input, output);
    }

    /**
     * Base-64 encodes all the input to the given output resources. An input stream is opened from the given input
     * resource and all input bytes are consumed and encoded until end-of-file (EOF). Encoded bytes are written to
     * a newly created temporary output resource by this implementation - so care must be taken to ensure the given
     * input stream content is not too large to overly consume system resources.
     *
     * @param input the input resource whose bytes are to be read and encoded.
     * @return a resource output the output resource where encoded bytes are to be written.
     * @throws IOException if an I/O error occurs reading from the input or writing to the output.
     * @see Base64Codec#encode(Resource)
     */
    public static Resource encode(Resource input) throws IOException {
        return _instance.encode(input);
    }
}

