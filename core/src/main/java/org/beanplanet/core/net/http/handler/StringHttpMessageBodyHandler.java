package org.beanplanet.core.net.http.handler;

import org.beanplanet.core.io.IoException;
import org.beanplanet.core.net.http.HttpMessage;
import org.beanplanet.core.net.http.MediaTypes;
import org.beanplanet.core.net.http.handler.annotations.HttpMessageBodyHandler;

import java.io.IOException;
import java.io.Writer;

import static org.beanplanet.core.net.http.MediaTypes.Text;

@HttpMessageBodyHandler
public class StringHttpMessageBodyHandler extends AbstractHttpMessageBodyHandler<Object> implements HttpMessageBodyInputOutputHandler<Object> {
    /**
     * Creates a new String based HTTP message body handler capable of reading strings from all <code>text/*</code>
     * media types amd writing strings as <code>text/plain</code> media type.
     *
     */
    public StringHttpMessageBodyHandler() {
        super(Text.PLAIN, Text.HTML, Text.XML, MediaTypes.ALL);
    }

    /**
     * Whether this handler supports reading from or writing to a given type.
     *
     * @param type the type to be tested for support by this handler.
     * @return true if the type is {@link String}, false otherwise.
     */
    @Override
    public boolean supports(Class<?> type) {
        return String.class == type;
    }

    /**
     * Reads an object from the given input message.
     *
     * @param type the type of object to be read from the input, which must be an instance of
     *             <code>{@link  CharSequence}</code>.
     * @param message the request message from which the body string is to be read.
     * @return the string representation of the message body..
     */
    @Override
    public String read(Class<Object> type, HttpMessage message) {
        return message.getBody().readFullyAsString(charsetFor(message));
    }

    /**
     * Writes the given object to the output message.
     * @param object the object to be written.
     * @param message the response message where the object is to be written.
     */
    @Override
    public void write(Object object, HttpMessage message) {
        try (Writer writer = message.getBody().getWriter(charsetFor(message))) {
            writer.write(object == null ? "" : object.toString());
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }
}
