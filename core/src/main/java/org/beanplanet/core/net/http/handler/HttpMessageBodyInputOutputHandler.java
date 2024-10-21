package org.beanplanet.core.net.http.handler;

import org.beanplanet.core.net.http.AbstractHttpMessage;
import org.beanplanet.core.net.http.HttpMessage;
import org.beanplanet.core.net.http.MediaType;
import org.beanplanet.core.net.http.MediaTypes;

import java.util.Set;

/**
 * Handler strategy for reading from and writing to Http messages.
 *
 * @see AbstractHttpMessage
 */
public interface HttpMessageBodyInputOutputHandler<T> {
    /**
     * Returns the set of media types this handler can read and write.
     *
     * @return the media types supported by this handler.
     */
    Set<MediaType> getSupportedMediaTypes();

    /**
     * Returns the set of media types this handler can read and write for the given type.
     *
     * @return the media types supported by this handler; defaults to all the supported types returned by
     * {@link #getSupportedMediaTypes()}.
     */
    default Set<MediaType> getSupportedMediaTypesFor(Class<?> type) {
        return getSupportedMediaTypes();
    }

    /**
     * Whether this handler can read an instance of the given type from a source of the given media type.
     *
     * @param type      the type to be read from the source.
     * @param mediaType the media type associated with the source.
     * @return true if this handler can read the type from a source with the given media type; defaults to true if
     * the given media type is in the set of media types returned by {@link #getSupportedMediaTypesFor(Class)}.
     */
    default boolean canRead(Class<?> type, MediaType mediaType) {
        return supports(type)
                && (getSupportedMediaTypesFor(type).contains(mediaType)
                || getSupportedMediaTypesFor(type).contains(MediaTypes.allForType(mediaType.getType()))
                || getSupportedMediaTypesFor(type).contains(MediaTypes.ALL));
    }

    /**
     * Whether this handler can write an instance of the given type to a source of the given media type.
     *
     * @param type      the type to be read from the source.
     * @param mediaType the media type associated with the source.
     * @return true if this handler can write the type to a source with the given media type;  defaults to true if
     * the given media type is in the set of media types returned by {@link #getSupportedMediaTypesFor(Class)}.
     */
    default boolean canWrite(Class<?> type, MediaType mediaType) {
        return supports(type)
                && (getSupportedMediaTypesFor(type).contains(mediaType)
                        || getSupportedMediaTypesFor(type).contains(MediaTypes.allForType(mediaType.getType()))
                        || getSupportedMediaTypesFor(type).contains(MediaTypes.ALL));
    }

    /**
     * Whether this handler supports reading from or writing to a given type.
     *
     * @param type the type to be tested for support by this handler.
     * @return true if this handler supports reading from and writing to the given type, false otherwise.
     */
    boolean supports(Class<?> type);

    /**
     * Reads an object from the given input message.
     *
     * @param type    the type of object to be read from the input.
     * @param message the request message from which te object is to be read.
     * @return the object read.
     */
    T read(Class<T> type, HttpMessage message);

    /**
     * Writes the given object to the output message.
     *
     * @param object  the object to be written.
     * @param message the response message where the object is to be written.
     */
    void write(T object, HttpMessage message);
}
