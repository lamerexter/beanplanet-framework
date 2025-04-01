package org.beanplanet.core.net.http.converter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.beanplanet.core.io.IoException;
import org.beanplanet.core.io.resource.ByteArrayOutputStreamResource;
import org.beanplanet.core.io.resource.Resource;
import org.beanplanet.core.lang.ParameterisedTypeReference;
import org.beanplanet.core.net.http.HttpMessage;
import org.beanplanet.core.net.http.HttpMessageHeaders;
import org.beanplanet.core.net.http.MediaTypes;
import org.beanplanet.core.net.http.converter.annotations.HttpMessageBodyConverter;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;

@HttpMessageBodyConverter
public class Jackson2HttpMessageBodyConverter extends AbstractHttpMessageBodyConverter<Object> implements org.beanplanet.core.net.http.converter.HttpMessageBodyConverter<Object> {
    /** The Jackson object mapper used by this handler. */
    private final ObjectMapper objectMapper;

    /**
     * Creates a new Jackson based HTTP message body handler with the given object mapper.
     *
     * @param objectMapper the Jackson object mapper to be used by this handler.
     */
    public Jackson2HttpMessageBodyConverter(final ObjectMapper objectMapper) {
        super(MediaTypes.Application.JSON);
        this.objectMapper = objectMapper;
    }

    /**
     * Creates a new Jackson based HTTP message body handler with a default object mapper.
     */
    public Jackson2HttpMessageBodyConverter() {
        this(new ObjectMapper());
        objectMapper.findAndRegisterModules();
    }

    /**
     * Whether this handler supports reading from or writing to a given type.
     *
     * @param type the type to be tested for support by this handler.
     * @return true because this handler will attempt to marshall from/to any type, in context of the supported
     * media types.
     */
    @Override
    public boolean supports(Type type) {
        return true;
    }

    /**
     * Reads an object from the given input message.
     *
     * @param type the type of object to be read from the input.
     * @param message the request message from which te object is to be read.
     * @return the object read.
     */
    @Override
    public Object convertFrom(ParameterisedTypeReference<Object> type, HttpMessage message) {
        try (Reader reader = message.getBody().getReader(charsetFor(message))) {
            JavaType javaType = this.objectMapper.constructType(type.getType());
            return objectMapper.readValue(reader, javaType);
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    /**
     * Writes the given object to the output message.
     * @param object the object to be written.
     * @param messageHeaders the headers of the message where the object is to be written.
     */
    @Override
    public Resource convertTo(Object object, HttpMessageHeaders messageHeaders) {
        ByteArrayOutputStreamResource baosr = new ByteArrayOutputStreamResource();
        try (Writer writer = baosr.getWriter(charsetFor(messageHeaders))) {
            objectMapper.writeValue(writer, object);
            return baosr;
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }
}
