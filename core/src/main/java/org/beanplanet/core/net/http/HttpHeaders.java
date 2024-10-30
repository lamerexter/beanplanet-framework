package org.beanplanet.core.net.http;

import org.beanplanet.core.util.CollectionUtil;
import org.beanplanet.core.util.MultiValueListMap;
import org.beanplanet.core.util.MultiValueListMapImpl;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HttpHeaders {
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String CONTENT_TYPE = "Content-Type";

    public static final String DATE_VALUE_FORMAT = "EEE, dd MM yyyy HH:mm:ss GMT";

    private final MultiValueListMap<String, String> headers;

    public HttpHeaders() {
        this(new MultiValueListMapImpl<>());
    }

    public HttpHeaders(final MultiValueListMap<String, String> headers) {
        this.headers = headers;
    }

    public HttpHeaders(final HttpHeaders other) {
        this(new MultiValueListMapImpl<>(other.headers));
    }

    public HttpHeaders merge(final HttpHeaders other) {
        HttpHeaders copy = new HttpHeaders(this);
        copy.addAll(other);
        return copy;
    }

    /**
     * Gets the first value, if any, of a header with the given name.
     *
     * @param name the name of the header whose value is to be returned.
     * @return the first value of the named header, or null if no values for the header exist.
     */
    public String get(final String name) {
        return getFirst(name);
    }

    /**
     * Gets all the headers associated with the message.
     *
     * @return the headers associated with the message, which may be empty but never null.
     */
    public MultiValueListMap<String, String> getAll() {
        return headers;
    }

    /**
     * Gets the values of the given header associated with the message.
     *
     * @return the header values of the header with the given name, which may be null if there are none.
     */
    public List<String> getAll(final String name) {
        return getHeaderValues(name).orElse(null);
    }

    public String getFirst(final String name) {
        return getHeaderValues(name).filter(CollectionUtil::isNotNullOrEmpty).map(l -> l.get(0)).orElse(null);
    }

    public String getLast(final String name) {
        return getHeaderValues(name).filter(CollectionUtil::isNotNullOrEmpty).map(l -> l.get(l.size()-1)).orElse(null);
    }

    public void add(final String name, final String value) {
        headers.addValue(name, value);
    }

    public void addAll(final HttpHeaders other) {
        other.headers.entrySet().forEach(e -> headers.addAllValues(e.getKey(), e.getValue()));
    }

    public void set(final String name, final String value) {
        ArrayList<String> allValues = new ArrayList<>();
        allValues.add(value);
        headers.put(name, allValues);
    }

    public void setAll(final HttpHeaders other) {
        other.headers.keySet().forEach( otherHeaderName-> this.set(otherHeaderName, other.getLast(otherHeaderName) ));
    }

    public void setAll(final Map<String, String> other) {
        other.forEach(this::set);
    }

    public static HttpHeadersBuilder builder() {
        return new HttpHeadersBuilder();
    }

    public static class HttpHeadersBuilder {
        /** The HTTP headers build candidate. */
        private final HttpHeaders httpHeaders = new HttpHeaders();

        public HttpHeadersBuilder add(final String name, final String value) {
            httpHeaders.add(name, value);
            return this;
        }

        public HttpHeadersBuilder add(final HttpHeaders other) {
            httpHeaders.addAll(other);
            return this;
        }

        public HttpHeadersBuilder set(final String name, final String value) {
            httpHeaders.set(name, value);
            return this;
        }

        public HttpHeadersBuilder setAll(final Map<String, String> other) {
            httpHeaders.setAll(other);
            return this;
        }

        public HttpHeadersBuilder contentType(final String mediaType) {
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, mediaType);
            return this;
        }

        public HttpHeadersBuilder contentType(final MediaType mediaType) {
            return contentType(mediaType.getCanonicalForm());
        }

        public HttpHeadersBuilder contentType(final MediaType mediaType, final Charset charset) {
            final MediaType mediaTypeToApply = charset == null ? mediaType : new MediaType(
                    mediaType.getType(), mediaType.getSubtype(),
                    mediaType.getParameters().combine(Parameters.singleton("charset", charset.name()))
            );
            httpHeaders.set(HttpHeaders.CONTENT_TYPE, mediaTypeToApply.getCanonicalForm());
            return this;
        }

        public void contentLength(final long length) {
            httpHeaders.set(HttpHeaders.CONTENT_LENGTH, Long.toString(length));
        }

        public HttpHeaders build() {
            return httpHeaders;
        }
    }

    private Optional<List<String>> getHeaderValues(final String headerName) {
        return headers.entrySet()
                      .stream().
                      filter(e -> e.getKey().equalsIgnoreCase(headerName))
                      .map(Map.Entry::getValue)
                      .findFirst();
    }
}
