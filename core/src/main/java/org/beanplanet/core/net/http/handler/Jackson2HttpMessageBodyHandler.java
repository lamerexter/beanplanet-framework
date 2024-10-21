package org.beanplanet.core.net.http.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.beanplanet.core.io.IoException;
import org.beanplanet.core.net.http.HttpMessage;
import org.beanplanet.core.net.http.MediaTypes;
import org.beanplanet.core.net.http.handler.annotations.HttpMessageBodyHandler;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

@HttpMessageBodyHandler
public class Jackson2HttpMessageBodyHandler extends AbstractHttpMessageBodyHandler<Object> implements HttpMessageBodyInputOutputHandler<Object> {
    /** The Jackson object mapper used by this handler. */
    private final ObjectMapper objectMapper;

    /**
     * Creates a new Jackson based HTTP message body handler with the given object mapper.
     *
     * @param objectMapper the Jackson object mapper to be used by this handler.
     */
    public Jackson2HttpMessageBodyHandler(final ObjectMapper objectMapper) {
        super(MediaTypes.Application.JSON);
        this.objectMapper = objectMapper;
    }

    /**
     * Creates a new Jackson based HTTP message body handler with a default object mapper.
     */
    public Jackson2HttpMessageBodyHandler() {
        this(new ObjectMapper());
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
    public Object read(Class<Object> type, HttpMessage message) {
        try (Reader reader = message.getBody().getReader(charsetFor(message))) {
            return objectMapper.readValue(reader, type);
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    /**
     * Writes the given object to the output message.
     * @param object the object to be written.
     * @param message the response message where the object is to be written.
     */
    @Override
    public void write(Object object, HttpMessage message) {
        try (Writer writer = message.getBody().getWriter(charsetFor(message))) {
            objectMapper.writeValue(writer, object);
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }
}
