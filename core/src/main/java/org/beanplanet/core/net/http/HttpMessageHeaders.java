package org.beanplanet.core.net.http;

import org.beanplanet.core.models.Pair;
import org.beanplanet.core.util.CollectionUtil;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.beanplanet.core.net.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.beanplanet.core.net.http.HttpHeaders.CONTENT_TYPE;

/**
 * Defines the header-related operations that may be performed on an HTTP Message.
 *
 * @see HttpMessage
 */
public interface HttpMessageHeaders {
    /**
     * Determines whether this message has a non-empty header with the given name.
     *
     * @param headerName the name of the header whose presence in this message is to be checked.
     * @return true if this message has a header with the given name.
     */
    boolean hasHeader(String headerName);

    /**
     * Determines whether this message has a non-empty header with the given name.
     *
     * @param headerName the name of the header whose presence in this message is to be checked.
     * @return true if this message has a header with the given name.
     */
    default boolean doesNotHaveHeader(String headerName) {
        return !hasHeader(headerName);
    }

    /**
     * Gets the first value, if any, of a header with the given name.
     *
     * @param name the name of the header whose value is to be returned.
     * @return the first value of the named header, or null if no values for the header exist.
     */
    default Optional<String> get(final String name) {
        return getFirst(name);
    }

    /**
     * Determines the content/media type of the message through the presence of a {@link HttpHeaders#CONTENT_TYPE}
     * header.
     *
     * @return the content type determined for the message, which may be empty indicating none was evident.
     */
    default Optional<MediaType> getContentType() {
        return getLast(CONTENT_TYPE).map(MediaType::new);
    }

    /**
     * Determines the charset from the presence of a "charset" parameter of {@link HttpHeaders#CONTENT_TYPE}
     * header.
     *
     * @return the character set applied to the message, which may be empty.
     */
    default Optional<Charset> getCharset() {
        return getContentType().flatMap(ct -> ct.getParameters().getCharset());
    }

    /**
     * Gets all the values of the given header associated with the message.
     *
     * @return the header values of the header with the given name, or empty if there are none.
     */
    Optional<List<String>> getHeaderValues(final String headerName);

    /**
     * Gets the values of the given header associated with the message.
     *
     * @return the header values of the header with the given name, or empty if there are none.
     */
    default List<String> getHeaderValuesList(final String name) {
        return getHeaderValues(name).orElse(Collections.emptyList());
    }

    default Optional<String> getFirst(final String name) {
        return getHeaderValues(name).filter(CollectionUtil::isNotNullOrEmpty).map(l -> l.get(0));
    }

    default Optional<String> getLast(final String name) {
        return getHeaderValues(name).filter(CollectionUtil::isNotNullOrEmpty).map(l -> l.get(l.size()-1));
    }

    /**
     * Sets a header in this message with the given name and value. Note that any previous values for the header
     * are replaced by this implementation.
     *
     * @param headerName the name of the header to be set.
     * @param headerValue the value of the header to be set.
     */
    void setHeader(String headerName, String headerValue);

    /**
     * Adds a header in this message with the given name and value. Note that any previous values for the header
     * are left intact and the given value is added as the last value.
     *
     * @param headerName the name of the header to be set.
     * @param headerValue the value of the header to be set.
     */
    void addHeader(final String headerName, final String headerValue);

    default void addAllHeaders(final HttpHeaders other) {
        other.getAll().forEach((k, hl) -> hl.forEach(h -> addHeader(k, h)));
    }

    default void setAllHeaders(final HttpHeaders other) {
        other.getAll().forEach((k, hl) -> hl.forEach(h -> setHeader(k, h)));
    }

    default void setAllHeaders(final Map<String, String> other) {
        other.forEach(this::setHeader);
    }

    /**
     * Sets the content type to the given value, replacing any existing content type. Simply calls
     * {@link #setHeader(String, String)} with header name <code>{@link HttpHeaders#CONTENT_TYPE}</code> and the
     * given value fully specified, including any parameters.
     *
     * @param contentType the content type to be set.
     * @see #setHeader(String, String)
     */
    default void setContentType(MediaType contentType) {
        setHeader(CONTENT_TYPE, contentType.getCanonicalForm());
    }

    /**
     * Sets the content type to the given value, replacing any existing content type. Simply calls
     * {@link #setHeader(String, String)} with header name <code>{@link HttpHeaders#CONTENT_TYPE}</code> and the
     * given value fully specified, including any parameters.
     *
     * @param contentDisposition the content type to be set.
     * @see #setHeader(String, String)
     */
    default void setContentDisposition(String contentDisposition) {
        setHeader(CONTENT_DISPOSITION, contentDisposition);
    }

    /**
     * Streams the names of all headers associated with this HTTP message.
     *
     * @return a stream of all the headers in this message.
     */
    Stream<String> streamHeaderNames();

    /**
     * Streams the names and, possibly multiple values, of all headers associated with this HTTP message.
     *
     * @return a stream of name and list of value(s) of all the headers in this message.
     */
    default Stream<Pair<String, List<String>>> streamAll() {
        return streamHeaderNames().map(h -> Pair.of(h, getHeaderValues(h).orElse(Collections.emptyList())));
    }
}
