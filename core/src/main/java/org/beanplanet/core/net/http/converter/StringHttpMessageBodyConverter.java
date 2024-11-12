package org.beanplanet.core.net.http.converter;

import org.beanplanet.core.io.resource.Resource;
import org.beanplanet.core.io.resource.StringResource;
import org.beanplanet.core.net.http.HttpHeaders;
import org.beanplanet.core.net.http.HttpMessage;
import org.beanplanet.core.net.http.HttpMessageHeaders;
import org.beanplanet.core.net.http.MediaTypes;
import org.beanplanet.core.net.http.converter.annotations.HttpMessageBodyConverter;

import static org.beanplanet.core.net.http.MediaTypes.Text;

@HttpMessageBodyConverter
public class StringHttpMessageBodyConverter extends AbstractHttpMessageBodyConverter<Object> implements org.beanplanet.core.net.http.converter.HttpMessageBodyConverter<Object> {
    /**
     * Creates a new String based HTTP message body handler capable of reading strings from all <code>text/*</code>
     * media types amd writing strings as <code>text/plain</code> media type.
     *
     */
    public StringHttpMessageBodyConverter() {
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
    public String convertFrom(Class<Object> type, HttpMessage message) {
        return message.getBody().readFullyAsString(charsetFor(message));
    }

    /**
     * Writes the given object to the output message.
     * @param object the object to be written.
     * @param messageHeaders the headers of the message where the object is to be written.
     */
    @Override
    public Resource convertTo(Object object, HttpMessageHeaders messageHeaders) {
        if ( messageHeaders.doesNotHaveHeader(HttpHeaders.CONTENT_TYPE) ) {
            messageHeaders.setHeader(HttpHeaders.CONTENT_TYPE, Text.PLAIN.getCanonicalForm());
        }

        return new StringResource(object == null ? "" : object.toString());
    }
}
