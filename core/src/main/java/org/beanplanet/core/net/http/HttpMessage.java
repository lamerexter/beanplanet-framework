/*
 * Copyright (c) 2001-present the original author or authors (see NOTICE herein).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.beanplanet.core.net.http;

import org.beanplanet.core.io.resource.Resource;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * An HTTP message, as defined in the <a href="https://www.rfc-editor.org/rfc/rfc2616">HTTP RFC Specification</a>.
 */
public interface HttpMessage extends HttpMessageHeaders {
    /**
     * Gets the headers associated with the message.
     *
     * @return the HTTP headers of the message, never null but may be empty.
     */
    HttpHeaders getHeaders();

    /**
     * Determines whether this message has a non-empty header with the given name.
     *
     * @param headerName the name of the header whose presence in this message is to be checked.
     * @return true if this message has a header with the given name.
     */
    default boolean hasHeader(String headerName) {
        return getHeaders().has(headerName);
    }

    /**
     * Gets all the values of the given header associated with the message.
     *
     * @return the header values of the header with the given name, or empty if there are none.
     */
    default Optional<List<String>> getHeaderValues(final String headerName) {
        return getHeaders().getHeaderValues(headerName);
    }

    /**
     * Sets a header in this message wth the given name and value. Note that any previous values for the header
     * are replaced by this implementation.
     *
     * @param headerName the name of the header to be set.
     * @param headerValue the value of the header to be set.
     */
    default void setHeader(String headerName, String headerValue) {
        getHeaders().set(headerName, headerValue);
    }

    /**
     * Adds a header in this message with the given name and value. Note that any previous values for the header
     * are left intact and the given value is added as the last value.
     *
     * @param headerName the name of the header to be set.
     * @param headerValue the value of the header to be set.
     */
    default void addHeader(final String headerName, final String headerValue) {
        getHeaders().add(headerName, headerValue);
    }

    /**
     * Gets the body of the HTTP message.
     *
     * @return the HTTP message body, which may be null if there is none.
     */
    Resource getBody();

    interface HttpMessageHeadersBuilderSpec<B extends HttpMessageHeadersBuilderSpec<B>> {
        B headers(final Consumer<HttpHeaders.HttpHeadersBuilder> headersBuilderConsumer);

        B headers(final HttpHeaders headers);

        B headers(final Map<String, List<String>> headers);

        B header(final String name, final String value);

        B contentType(final MediaType mediaType);

        B contentType(final String mediaType);

        B contentType(final MediaType mediaType, final Charset charset);

        B contentLength(final long length);
    }

}
