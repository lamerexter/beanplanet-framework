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
import org.beanplanet.core.io.resource.ByteArrayOutputStreamResource;
import org.beanplanet.core.io.resource.ByteArrayResource;
import org.beanplanet.core.io.resource.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * An encoder of binary data.
 */
public interface Encoder {
    /**
     * Performs encoding of the input to the given output stream. All input bytes are consumed until end-of-file (EOF).
     *
     * @param input  the input stream whose bytes are to be read and encoded.
     * @param output the output stream where encoded bytes are to be written.
     * @throws IOException if an I/O error occurs reading from the input or writing to the output.
     */
    void encode(InputStream input, OutputStream output) throws IOException;

    /**
     * Encodes bytes according to the logic of this encoder.
     *
     * @param input the bytes to be read and encoded.
     * @return the input bytes encoded.
     */
    default byte[] encode(byte[] input) {
        try {
            return encode(new ByteArrayResource(input)).readFullyAsBytes();
        } catch (IOException e) {
            throw new IoException(e);
        }
    }

    /**
     * Performs encoding of the input to the given output resources. An input stream is opened from the given input
     * resource and all input bytes are consumed and encoded until end-of-file (EOF). Encoded bytes are written to
     * an output stream opened on the given output resource.
     *
     * @param input  the input resource whose bytes are to be read and encoded.
     * @param output the output resource where encoded bytes are to be written.
     * @throws IOException if an I/O error occurs reading from the input or writing to the output.
     */
    default Resource encode(Resource input, Resource output) throws IOException {
        try (InputStream is = input.getInputStream(); OutputStream os = output.getOutputStream()) {
            encode(is, os);
            return output;
        }
    }

    /**
     * Performs encoding of the input to the given output resources. An input stream is opened from the given input
     * resource and all input bytes are consumed and encoded until end-of-file (EOF). Encoded bytes are written to
     * a newly created temporary output resource by this implementation - so care must be taken to ensure the given
     * input stream content is not too large to overly consume system resources.
     *
     * @param input the input resource whose bytes are to be read and encoded.
     * @return a resource output the output resource where encoded bytes are to be written.
     * @throws IOException if an I/O error occurs reading from the input or writing to the output.
     */
    default Resource encode(Resource input) throws IOException {
        return encode(input, new ByteArrayOutputStreamResource());
    }
}
