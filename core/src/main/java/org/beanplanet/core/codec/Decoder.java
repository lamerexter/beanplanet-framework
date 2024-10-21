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
    void decode(InputStream input, OutputStream outputStream);

    default byte[] decode(byte[] input) {
        return decode(new ByteArrayResource(input)).readFullyAsBytes();
    }

    default Resource decode(Resource input, Resource output) {
        try (InputStream is = input.getInputStream(); OutputStream os = output.getOutputStream()) {
            decode(is, os);
            return output;
        } catch (IOException e) {
            throw new IoException(e);
        }
    }

    default Resource decode(Resource input) {
        return decode(input, new ByteArrayOutputStreamResource());
    }
}
