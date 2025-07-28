package org.beanplanet.core.codec;

import org.beanplanet.core.io.IoException;
import org.beanplanet.core.io.resource.ByteArrayOutputStreamResource;
import org.beanplanet.core.io.resource.ByteArrayResource;
import org.beanplanet.core.io.resource.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A decoder of binary data.
 */
public interface Decoder {
    /**
     * Performs decoding of the input to the given output stream. All input bytes are consumed until end-of-file (EOF).
     *
     * @param input  the input stream whose bytes are to be read and decoded.
     * @param output the output stream where encoded bytes are to be written.
     * @throws IOException if an I/O error occurs reading from the input or writing to the output.
     */
    void decode(InputStream input, OutputStream output) throws IOException;

    /**
     * Decodes bytes according to the logic of this decoder.
     *
     * @param input the bytes to be read and decoded.
     * @return the input bytes decoded.
     */
    default byte[] decode(byte[] input) {
        try {
            return decode(new ByteArrayResource(input)).readFullyAsBytes();
        } catch (IOException e) {
            throw new IoException(e);
        }
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
    default Resource decode(Resource input, Resource output) throws IOException {
        try (InputStream is = input.getInputStream(); OutputStream os = output.getOutputStream()) {
            decode(is, os);
            return output;
        }
    }

    /**
     * Performs decoding of the input to the given output resources. An input stream is opened from the given input
     * resource and all input bytes are consumed and decoded until end-of-file (EOF). Decoded bytes are written to
     * a newly created temporary output resource by this implementation - so care must be taken to ensure the given
     * input stream content is not too large to overly consume system resources.
     *
     * @param input the input resource whose bytes are to be read and decoded.
     * @return a resource output the output resource where decoded bytes are to be written.
     * @throws IOException if an I/O error occurs reading from the input or writing to the output.
     */
    default Resource decode(Resource input) throws IOException {
        return decode(input, new ByteArrayOutputStreamResource());
    }
}
