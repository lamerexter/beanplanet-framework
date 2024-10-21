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
    void encode(InputStream input, OutputStream outputStream);

    default byte[] encode(byte[] input) {
        return encode(new ByteArrayResource(input)).readFullyAsBytes();
    }

    default Resource encode(Resource input, Resource output) {
        try (InputStream is = input.getInputStream(); OutputStream os = output.getOutputStream()) {
            encode(is, os);
            return output;
        } catch (IOException e) {
            throw new IoException(e);
        }
    }

    default Resource encode(Resource input) {
        return encode(input, new ByteArrayOutputStreamResource());
    }
}
