package org.beanplanet.core.net.http.handler;

import org.beanplanet.core.models.AbstractLoadedRegistry;
import org.beanplanet.core.net.http.MediaType;
import org.beanplanet.core.util.MultiValueListMap;
import org.beanplanet.core.util.MultiValueListMapImpl;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class SystemHttpMessageBodyHandlerRegistry extends AbstractLoadedRegistry<MediaType, HttpMessageBodyInputOutputHandler<?>> implements HttpMessageBodyHandlerRegistry {
    private static SystemHttpMessageBodyHandlerRegistry _instance = new SystemHttpMessageBodyHandlerRegistry();

    private Map<String, MultiValueListMap<String, HttpMessageBodyInputOutputHandler<?>>> entries = new ConcurrentHashMap<>();

    public static HttpMessageBodyHandlerRegistry getInstance() {
        return _instance;
    }

    public SystemHttpMessageBodyHandlerRegistry() {
        super(new HttpMessageBodyHandlerLoader());
    }

    @Override
    protected <E> Stream<E> doFindEntriesOfTypeInternal(Class<E> entryType) {
        return entries.values().stream().flatMap(m -> m.values().stream().flatMap(Collection::stream))
                      .filter(entryType::isInstance)
                      .map(entryType::cast);
    }

    @Override
    protected int sizeInternal() {
        return (int) entries.values().stream().flatMap(m -> m.values().stream().flatMap(Collection::stream)).count();
    }

    @Override
    protected HttpMessageBodyInputOutputHandler<?> doLookupInternal(MediaType key) {
        return null;
    }

    @Override
    public boolean addToRegistry(MediaType mediaType, HttpMessageBodyInputOutputHandler<?> item) {
        return entries.computeIfAbsent(mediaType.getType(), k -> new MultiValueListMapImpl<>())
                      .addValue(mediaType.getSubtype(), item);
    }

    @Override
    public boolean removeFromRegistry(MediaType mediaType) {
        final var typeToSubtypeMap = entries.get(mediaType.getType());
        if (typeToSubtypeMap == null) return false;

        return typeToSubtypeMap.remove(mediaType.getSubtype()) != null;
    }

    /**
     * Find handlers that support read the given media type and class.
     *
     * @param mediaType the media type the handler should support reading from.
     * @param type      the type the handler should support reading from the given media type.
     * @param <T>       the type of object the handler will support reading.
     * @return a list of the read handlers, which will never be null but may be empty.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> Stream<HttpMessageBodyInputOutputHandler<T>> findReadHandlers(MediaType mediaType, Class<T> type) {
        checkLoaded();
        return Stream.concat(
                             Stream.concat(
                                     handlersFor(mediaType.getType(), mediaType.getSubtype()),
                                     handlersFor(mediaType.getType(), "*")),
                             handlersFor("*", "*"))
                     .filter(h -> h.canRead(type, mediaType))
                     .map(h -> (HttpMessageBodyInputOutputHandler<T>) h);
    }

    /**
     * Find handlers that support writing the given media type and class.
     *
     * @param mediaType the media type the handler should support writing to.
     * @param type      the type the handler should support writing to the given media type.
     * @param <T>       the type of object the handler will support writing.
     * @return a list of the write handlers, which will never be null but may be empty.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> Stream<HttpMessageBodyInputOutputHandler<T>> findWriteHandlers(MediaType mediaType, Class<T> type) {
        checkLoaded();
        return Stream.concat(
                             Stream.concat(
                                     handlersFor(mediaType.getType(), mediaType.getSubtype()),
                                     handlersFor(mediaType.getType(), "*")),
                             handlersFor("*", "*"))
                     .filter(h -> h.canWrite(type, mediaType))
                     .map(h -> (HttpMessageBodyInputOutputHandler<T>) h);
    }

    private Stream<HttpMessageBodyInputOutputHandler<?>> handlersFor(final String mediaType,
                                                                     final String mediaSubtype) {
        return entries.getOrDefault(mediaType, MultiValueListMap.empty())
                      .getOrDefault(mediaSubtype, Collections.emptyList()).stream();
    }
}
