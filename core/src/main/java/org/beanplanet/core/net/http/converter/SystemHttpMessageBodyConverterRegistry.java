package org.beanplanet.core.net.http.converter;

import org.beanplanet.core.models.AbstractLoadedRegistry;
import org.beanplanet.core.net.http.MediaType;
import org.beanplanet.core.util.MultiValueListMap;
import org.beanplanet.core.util.MultiValueListMapImpl;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class SystemHttpMessageBodyConverterRegistry extends AbstractLoadedRegistry<MediaType, HttpMessageBodyConverter<?>> implements HttpMessageBodyConverterRegistry {
    private static SystemHttpMessageBodyConverterRegistry _instance = new SystemHttpMessageBodyConverterRegistry();

    private Map<String, MultiValueListMap<String, HttpMessageBodyConverter<?>>> entries = new ConcurrentHashMap<>();

    public static HttpMessageBodyConverterRegistry getInstance() {
        return _instance;
    }

    public SystemHttpMessageBodyConverterRegistry() {
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
    protected HttpMessageBodyConverter<?> doLookupInternal(MediaType key) {
        return null;
    }

    @Override
    public boolean addToRegistry(MediaType mediaType, HttpMessageBodyConverter<?> item) {
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
    public <T> Stream<HttpMessageBodyConverter<T>> findFromConverters(MediaType mediaType, Class<T> type) {
        checkLoaded();
        return Stream.concat(
                             Stream.concat(
                                     handlersFor(mediaType.getType(), mediaType.getSubtype()),
                                     handlersFor(mediaType.getType(), "*")),
                             handlersFor("*", "*"))
                     .filter(h -> h.canConvertFrom(type, mediaType))
                     .map(h -> (HttpMessageBodyConverter<T>) h);
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
    public <T> Stream<HttpMessageBodyConverter<T>> findToConverters(MediaType mediaType, Class<T> type) {
        checkLoaded();
        return Stream.concat(
                             (mediaType == null ? Stream.empty() :
                             Stream.concat(
                                     handlersFor(mediaType.getType(), mediaType.getSubtype()),
                                     handlersFor(mediaType.getType(), "*"))),
                             handlersFor("*", "*"))
                     .filter(h -> h.canConvertTo(type, mediaType))
                     .map(h -> (HttpMessageBodyConverter<T>) h);
    }

    private Stream<HttpMessageBodyConverter<?>> handlersFor(final String mediaType,
                                                            final String mediaSubtype) {
        return entries.getOrDefault(mediaType, MultiValueListMap.empty())
                      .getOrDefault(mediaSubtype, Collections.emptyList()).stream();
    }
}
