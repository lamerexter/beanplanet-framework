package org.beanplanet.core.net.http.converter;

import org.beanplanet.core.io.resource.Resource;
import org.beanplanet.core.net.http.*;
import org.beanplanet.core.net.http.converter.annotations.HttpMessageBodyConverter;

import java.util.Optional;

/**
 * An HTTP message body converter capable of converting generic {@link Resource} types to/from
 * HTTP message bodies.
 */
@HttpMessageBodyConverter
public class ResourceHttpMessageBodyConverter extends AbstractHttpMessageBodyConverter<Resource> {
    public ResourceHttpMessageBodyConverter() {
        super(MediaTypes.ALL);
    }

    /**
     * Whether this handler supports reading from or writing to a given type.
     *
     * @param type the type to be tested for support by this handler.
     * @return true because this handler will attempt to marshall from/to any type, in context of the supported
     * media types.
     */
    @Override
    public boolean supports(Class<?> type) {
        return Resource.class.isAssignableFrom(type);
    }

    /**
     * Reads an object from the given input message.
     *
     * @param type    the type of object to be read from the input.
     * @param message the request message from which te object is to be read.
     * @return the object read.
     */
    @Override
    public Resource convertFrom(Class<Resource> type, HttpMessage message) {
        throw new UnsupportedOperationException();
    }

    /**
     * Writes the given object to the output message.
     *
     * @param resource       the resource to be written.
     * @param messageHeaders the headers of the message where the object is to be written.
     * @return the given resource.
     */
    @Override
    public Resource convertTo(Resource resource, HttpMessageHeaders messageHeaders) {
        MediaType resourceContentType = SystemMediaTypesRegistry.getInstance().findMediaType(resource).orElse(MediaTypes.Application.OCTET_STREAM);
        messageHeaders.setContentType(resourceContentType);

        return resource;
    }
}
