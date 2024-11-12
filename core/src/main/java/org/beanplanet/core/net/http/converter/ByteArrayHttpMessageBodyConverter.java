package org.beanplanet.core.net.http.converter;

import org.beanplanet.core.io.resource.ByteArrayResource;
import org.beanplanet.core.io.resource.Resource;
import org.beanplanet.core.net.http.HttpHeaders;
import org.beanplanet.core.net.http.HttpMessage;
import org.beanplanet.core.net.http.HttpMessageHeaders;
import org.beanplanet.core.net.http.MediaTypes;
import org.beanplanet.core.net.http.converter.annotations.HttpMessageBodyConverter;

@HttpMessageBodyConverter
public class ByteArrayHttpMessageBodyConverter extends AbstractHttpMessageBodyConverter<byte[]> implements org.beanplanet.core.net.http.converter.HttpMessageBodyConverter<byte[]> {
    /**
     * Creates a new byte array based HTTP message body handler capable of reading arrays of bytes from all media types
     * (<code>&#42;/*</code>) and writing byte arrays as the generic media type (<code>application/octet-stream</code>).
     */
    public ByteArrayHttpMessageBodyConverter() {
        super(MediaTypes.Application.OCTET_STREAM, MediaTypes.ALL);
    }

    /**
     * Whether this handler supports reading from or writing to a given type.
     *
     * @param type the type to be tested for support by this handler.
     * @return true if the type is byte array, false otherwise.
     */
    @Override
    public boolean supports(Class<?> type) {
        return byte[].class == type;
    }

    /**
     * Reads an object from the given input message.
     *
     * @param type the type of object to be read from the input, which must be an instance of <code>byte[]</code>.
     * @param message the request message from which the body byte array is to be read.
     * @return the byte array representation of the message body.
     */
    @Override
    public byte[] convertFrom(Class<byte[]> type, HttpMessage message) {
        return message.getBody().readFullyAsBytes();
    }

    /**
     * Writes the given object to the output message.
     *
     * @param object the object to be written.
     * @param messageHeaders the headers of the message where the object is to be written.
     */
    @Override
    public Resource convertTo(byte[] object, HttpMessageHeaders messageHeaders) {
        if ( messageHeaders.doesNotHaveHeader(HttpHeaders.CONTENT_TYPE) ) {
            messageHeaders.setHeader(HttpHeaders.CONTENT_TYPE, MediaTypes.Application.OCTET_STREAM.getCanonicalForm());
        }

        return new ByteArrayResource(object);
    }
}
