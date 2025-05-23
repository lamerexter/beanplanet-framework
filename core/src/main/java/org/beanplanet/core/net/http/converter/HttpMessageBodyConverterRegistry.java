package org.beanplanet.core.net.http.converter;

import org.beanplanet.core.models.Registry;
import org.beanplanet.core.net.http.MediaType;

import java.lang.reflect.Type;
import java.util.stream.Stream;

public interface HttpMessageBodyConverterRegistry extends Registry<MediaType, HttpMessageBodyConverter<?>> {

    /**
     * Find handlers that support read the given media type and class.
     *
     * @param mediaType the media type the handler should support reading from.
     * @param type the type the handler should support reading from the given media type.
     * @return a list of the read handlers, which will never be null but may be empty.
     * @param <T> the type of object the handler will support reading.
     */
    <T> Stream<HttpMessageBodyConverter<T>> findFromConverters(MediaType mediaType, Type type);

    /**
     * Find handlers that support writing the given media type and class.
     *
     * @param mediaType the media type the handler should support writing to.
     * @param type the type the handler should support writing to the given media type.
     * @return a list of the write handlers, which will never be null but may be empty.
     * @param <T> the type of object the handler will support writing.
     */
    <T> Stream<HttpMessageBodyConverter<T>> findToConverters(MediaType mediaType, Type type);
}
