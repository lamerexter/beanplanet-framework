package org.beanplanet.core.net.http;

import org.beanplanet.core.util.CollectionUtil;
import org.beanplanet.core.util.MultiValueListMap;
import org.beanplanet.core.util.MultiValueListMapImpl;

import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Stream;

import static org.beanplanet.core.util.StringUtil.isBlank;
import static org.beanplanet.core.util.StringUtil.isNotBlank;

public class HttpHeaders implements HttpMessageHeaders {
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
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
     * Determines whether this message has a non-empty header with the given name.
     *
     * @param headerName the name of the header whose presence in this message is to be checked.
     * @return true if this message has a header with the given name.
     */
    @Override
    public boolean hasHeader(String headerName) {
        return has(headerName);
    }

    /**
     * Gets the first value, if any, of a header with the given name.
     *
     * @param name the name of the header whose value is to be returned.
     * @return the first value of the named header, or null if no values for the header exist.
     */
    public Optional<String> get(final String name) {
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
     * @return the header values of the header with the given name, or empty if there are none.
     */
    public List<String> getAll(final String name) {
        return getHeaderValues(name).orElse(Collections.emptyList());
    }

    public Optional<String> getFirst(final String name) {
        return getHeaderValues(name).filter(CollectionUtil::isNotNullOrEmpty).map(l -> l.get(0));
    }

    public Optional<String> getLast(final String name) {
        return getHeaderValues(name).filter(CollectionUtil::isNotNullOrEmpty).map(l -> l.get(l.size()-1));
    }

    /**
     * Sets a header in this message with the given name and value. Note that any previous values for the header
     * are replaced by this implementation.
     *
     * @param headerName  the name of the header to be set.
     * @param headerValue the value of the header to be set.
     */
    @Override
    public void setHeader(String headerName, String headerValue) {
        set(headerName, headerValue);
    }

    /**
     * Adds a header in this message with the given name and value. Note that any previous values for the header
     * are left intact and the given value is added as the last value.
     *
     * @param headerName  the name of the header to be set.
     * @param headerValue the value of the header to be set.
     */
    @Override
    public void addHeader(String headerName, String headerValue) {
        add(headerName, headerValue);
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
        other.headers.keySet().forEach( otherHeaderName-> this.set(otherHeaderName, other.getLast(otherHeaderName).orElse(null) ));
    }

    public void setAll(final Map<String, String> other) {
        other.forEach(this::set);
    }

    public static HttpHeadersBuilderImpl builder() {
        return new HttpHeadersBuilderImpl();
    }

    /**
     * Determines whether there exists a non-empty header with the given name.
     *
     * @param name the name of the header whose presence is to be checked.
     * @return true if there is a header with the given name.
     */
    public boolean has(String name) {
        return isNotBlank(get(name).orElse(null));
    }

    /**
     * Determines whetherthere exists a non-empty header with the given name.
     *
     * @param name the name of the header whose presence in this message is to be checked.
     * @return true if this message has a header with the given name.
     */
    public boolean doesNotHave(String name) {
        return isBlank(get(name).orElse(null));
    }

    /**
     * Streams the names of all headers associated with this HTTP message.
     *
     * @return a stream of all the headers in this message.
     */
    public Stream<String> streamHeaderNames() {
        return headers.keySet().stream();
    }

    public static class HttpHeadersBuilderImpl implements HttpHeadersBuilder<HttpHeaders, HttpHeadersBuilderImpl> {
        /** The HTTP headers build candidate. */
        private final HttpHeaders httpHeaders = new HttpHeaders();

        public HttpHeadersBuilderImpl add(final String name, final String value) {
            httpHeaders.add(name, value);
            return this;
        }

        public HttpHeadersBuilderImpl add(final HttpHeaders other) {
            httpHeaders.addAll(other);
            return this;
        }

        public HttpHeadersBuilderImpl set(final String name, final String value) {
            httpHeaders.set(name, value);
            return this;
        }

        public HttpHeadersBuilderImpl setAll(final Map<String, String> other) {
            httpHeaders.setAll(other);
            return this;
        }

        public HttpHeadersBuilderImpl contentType(final String mediaType) {
            httpHeaders.add(HttpHeaders.CONTENT_TYPE, mediaType);
            return this;
        }

        public HttpHeadersBuilderImpl contentType(final MediaType mediaType) {
            return contentType(mediaType.getCanonicalForm());
        }

        public HttpHeadersBuilderImpl contentType(final MediaType mediaType, final Charset charset) {
            final MediaType mediaTypeToApply = charset == null ? mediaType : new MediaType(
                    mediaType.getType(), mediaType.getSubtype(),
                    mediaType.getParameters().combine(Parameters.singleton("charset", charset.name()))
            );
            httpHeaders.set(HttpHeaders.CONTENT_TYPE, mediaTypeToApply.getCanonicalForm());
            return this;
        }

        public HttpHeadersBuilderImpl contentLength(final long length) {
            httpHeaders.set(HttpHeaders.CONTENT_LENGTH, Long.toString(length));
            return this;
        }

        public HttpHeaders build() {
            return httpHeaders;
        }
    }

    public Optional<List<String>> getHeaderValues(final String headerName) {
        return headers.entrySet()
                      .stream().
                      filter(e -> e.getKey().equalsIgnoreCase(headerName))
                      .map(Map.Entry::getValue)
                      .findFirst();
    }

    public interface HttpHeadersBuilderSpec<B extends HttpHeadersBuilderSpec<B>> {
        B add(final String name, final String value);

        B add(final HttpHeaders other);

        B set(final String name, final String value);

        B setAll(final Map<String, String> other);

        B contentType(final String mediaType) ;

        B contentType(final MediaType mediaType);

        B contentType(final MediaType mediaType, final Charset charset);

        B contentLength(final long length);
    }

    public interface HttpHeadersBuilder<H, B extends HttpHeadersBuilder<H, B>> extends HttpHeadersBuilderSpec<B> {
        H build();
    }
}
