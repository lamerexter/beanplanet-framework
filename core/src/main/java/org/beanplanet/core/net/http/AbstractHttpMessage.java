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

import org.beanplanet.core.io.resource.CharSequenceResource;
import org.beanplanet.core.io.resource.Resource;
import org.beanplanet.core.models.Pair;
import org.beanplanet.core.util.MultiValueListMapImpl;
import org.beanplanet.core.util.PropertyBasedToStringBuilder;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * A model of an HTTP message, common to both request and response.
 *
 * @author Gary Watson
 */
public abstract class AbstractHttpMessage implements HttpMessage {
    /**
     * The headers associated with the message.
     */
    protected HttpHeaders headers;

    /**
     * The body of the message.
     */
    protected Resource body;

    public AbstractHttpMessage(final HttpHeaders headers,
                               final Resource body) {
        this.headers = headers == null ? new HttpHeaders() : headers;
        this.body = body;
    }

    public AbstractHttpMessage(final HttpMessageBuilder<?, ?> builder) {
        this(builder.headersBuilder.build(), builder.body);
    }

    public AbstractHttpMessage(final HttpHeaders headers) {
        this(headers, null);
    }

    public AbstractHttpMessage() {
        this(new HttpHeaders(), null);
    }

    /**
     * Gets the headers associated with the message.
     *
     * @return the headers associated with the message, which may be null if there are none.
     */
    public HttpHeaders getHeaders() {
        return headers;
    }

    public AbstractHttpMessage withContentType(MediaType contentType) {
        return withContentType(contentType.getName());
    }

    public AbstractHttpMessage withContentType(String contentType) {
        getHeaders().set("Content-Type", contentType);
        return this;
    }

    public AbstractHttpMessage withContentLength(int contentLength) {
        getHeaders().set("Content-Length", Integer.toString(contentLength));
        return this;
    }

    /**
     * Gets the body of the message.
     *
     * @return the body of the message, which may be null to indicate none
     */
    public Resource getBody() {
        return body;
    }

    /**
     * Gets all cookies associated with the message.
     *
     * @return the cookies associated with this message, or an empty list of there are none.
     */
    public abstract List<Cookie> getCookies();

    /**
     * Adds a cookie to thie message. Subclasses are responsible for setting the correct header name associated with cookies.
     *
     * @param cookie the cookie whose equivalent HTTP header is to be added.
     */
    public void addCookie(final Cookie cookie) {
        withCookie(cookie);
    }

    /**
     * Adds a cookie to this message. Subclasses are responsible for setting the correct header name associated with cookies.
     *
     * @param cookie the cookie whose equivalent HTTP header is to be added.
     * @return this message for method chaining.
     */
    public abstract AbstractHttpMessage withCookie(final Cookie cookie);

    /**
     * Determines whether this message has a cookie with the specified name.
     *
     * @param name the name of the cookie whose presence in this message is to be determined.
     * @return true if this request contains a cookie with the given name, false otherwise.
     */
    public boolean hasCookie(final String name) {
        return getCookies().stream().anyMatch(c -> name.equalsIgnoreCase(c.getName()));
    }

    /**
     * Optionally gets a cookie with the specified name, by looking through the HTTP headers named "Set-Cookie", for
     * a cookie with the given name.
     *
     * @param name the name of the cookie tp .
     * @return true if this request contains a cookie with the given name, false otherwise.
     */
    public Optional<Cookie> getCookie(final String name) {
        return getCookies().stream().filter(c -> name.equalsIgnoreCase(c.getName())).findFirst();
    }

    /**
     * Streams the names of all headers associated with this HTTP message.
     *
     * @return a stream of all the headers in this message.
     */
    public Stream<String> streamHeaderNames() {
        return headers.streamHeaderNames();
    }

    public String toString() {
        return new PropertyBasedToStringBuilder(
                this,
                Pair.of("headers", this::getHeaders),
                Pair.of("body", this::getBody)
        ).build();
    }

    public static abstract class HttpMessageBuilder<C extends HttpMessage, B extends HttpMessageBuilder<C, B>> implements HttpMessageHeadersBuilderSpec<B> {
        /**
         * The headers associated with the message.
         */
        private HttpHeaders.HttpHeadersBuilder<HttpHeaders, ?> headersBuilder = HttpHeaders.builder();
        /**
         * The body of the message.
         */
        private Resource body;

        public B headers(final Consumer<HttpHeaders.HttpHeadersBuilder> headersBuilderConsumer) {
            headersBuilderConsumer.accept(this.headersBuilder);
            return self();
        }

        public B headers(final HttpHeaders headers) {
            this.headersBuilder.add(headers);
            return self();
        }

        public B headers(final Map<String, List<String>> headers) {
            this.headersBuilder.add(new HttpHeaders(new MultiValueListMapImpl<>(headers)));
            return self();
        }

        public B header(final String name, final String value) {
            this.headersBuilder.add(name, value);
            return self();
        }

        public B contentType(final MediaType mediaType) {
            this.headersBuilder.contentType(mediaType);
            return self();
        }

        public B contentType(final String mediaType) {
            this.headersBuilder.contentType(mediaType);
            return self();
        }

        public B contentType(final MediaType mediaType, final Charset charset) {
            this.headersBuilder.contentType(mediaType, charset);
            return self();
        }

        public B contentLength(final long length) {
            this.headersBuilder.contentLength(length);
            return self();
        }

        public B body(final Resource body, final MediaType mediaType, final Charset charset) {
            if (mediaType != null) {
                this.headersBuilder.contentType(mediaType, charset != null ? charset : mediaType.getParameters().getCharset().orElse(Charset.defaultCharset()));
            }
            this.body = body;
            return self();
        }

        public B body(final Resource body, final MediaType mediaType) {
            return body(body, mediaType, Charset.defaultCharset());
        }

        public B body(final CharSequence string) {
            return body(string == null ? null : new CharSequenceResource(string), null, null);
        }

        public B body(final Resource body) {
            this.body = body;
            return self();
        }

        protected abstract B self();

        public abstract C build();

        public String toString() {
            return new PropertyBasedToStringBuilder(this, "headers", "body").build();
        }
    }
}
