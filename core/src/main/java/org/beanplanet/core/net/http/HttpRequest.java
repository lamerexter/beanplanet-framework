/*
 *  MIT Licence:
 *
 *  Copyright (C) 2018 Beanplanet Ltd
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without restriction
 *  including without limitation the rights to use, copy, modify, merge,
 *  publish, distribute, sublicense, and/or sell copies of the Software,
 *  and to permit persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 *  KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 *  PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 *  CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  DEALINGS IN THE SOFTWARE.
 */

package org.beanplanet.core.net.http;

import org.beanplanet.core.net.UriBuilder;
import org.beanplanet.core.util.MultiValueListMap;
import org.beanplanet.core.util.PropertyBasedToStringBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.beanplanet.core.net.http.Request.Method.*;
import static org.beanplanet.core.net.http.Request.Version.HTTP_1_1;


/**
 * A model of an HTTP request.
 */
public class HttpRequest extends AbstractHttpMessage implements Request {
    /**
     * The HTTP method.
     */
    private String method;
    /**
     * The URI of the HTTP request.
     */
    private URI uri;
    /**
     * The HTTP protocol version.
     */
    private Version httpVersion;

    public HttpRequest(final String method,
                       final URI uri,
                       final Version httpVersion,
                       final HttpHeaders headers) {
        super(headers, null);
        this.method = method;
        this.uri = uri;
        this.httpVersion = httpVersion;
    }

    public HttpRequest(final String method,
                       final URI uri,
                       final Version httpVersion) {
        this(method, uri, httpVersion, null);
    }

    public HttpRequest(final Method method,
                       final URI uri,
                       final Version httpVersion,
                       final HttpHeaders headers) {
        this(method.name(), uri, httpVersion, headers);
    }

    public HttpRequest(final Method method,
                       final URI uri,
                       final Version httpVersion) {
        this(method, uri, httpVersion, null);
    }

    public HttpRequest(final Request other) {
        super(other.getHeaders(), other.getBody());
        this.method = other.getMethod();
        this.uri = other.getUri();
        this.httpVersion = other.getHttpVersion();
    }

    public HttpRequest() {
        this(GET, URI.create("/"), HTTP_1_1);
    }


    private HttpRequest(final HttpRequestBuilder<?, ?> builder) {
        super(builder);
        this.method = builder.method;
        this.uri = builder.uri;
        this.httpVersion = builder.httpVersion;
    }

    public static HttpRequestBuilder<?, ?> builder() {
        return new HttpRequestBuilderImpl();
    }

    public HttpRequest merge(final Request other) {
        HttpRequest copy = new HttpRequest(this);

        if ( other.getMethod() != null ) copy.method = other.getMethod();
        if ( other.getUri() != null ) {
            if ( copy.uri == null ) {
                copy.uri = other.getUri();
            } else {
                UriBuilder copyUriBuilder = new UriBuilder(copy.uri);
                copy.uri = copyUriBuilder.merge(new UriBuilder(other.getUri())).build();
            }
        }
        if ( other.getHttpVersion() != null ) copy.httpVersion = other.getHttpVersion();
        copy.headers = copy.getHeaders().merge(other.getHeaders());
        if ( other.getBody() != null ) copy.body = other.getBody();

        return copy;
    }

    public String getMethod() {
        return method;
    }

    public URI getUri() {
        return uri;
    }

    public Version getHttpVersion() {
        return httpVersion;
    }

    public HttpRequest withContentType(MediaType contentType) {
        return (HttpRequest) super.withContentType(contentType);
    }

    public HttpRequest withContentType(String contentType) {
        return (HttpRequest) super.withContentType(contentType);
    }

    public HttpRequest withContentLength(int contentLength) {
        return (HttpRequest) super.withContentLength(contentLength);
    }

    public boolean hasQueryParameter(String name) {
        return uri != null && new UriBuilder(uri).hasQueryParameter(name);
    }

    /**
     * Gets all cookies associated with the message by looking through the HTTP headers named "Cookie", for
     * a cookie with the given name.
     *
     * @return the cookies associated with this message, or an empty list of there are none.
     */
    public List<Cookie> getCookies() {
        final MultiValueListMap<String, String> headers = getHeaders().getAll();
        if (headers == null) return Collections.emptyList();

        return headers.entrySet().stream()
                      .filter(e -> Cookie.HTTP_REQUEST_HEADER_NAME.equalsIgnoreCase(e.getKey()))
                      .map(Map.Entry::getValue)
                      .flatMap(List::stream)
                      .map(Cookie::fromHttpHeaderValue)
                      .collect(Collectors.toList());
    }

    /**
     * Adds a cookie to this request message by adding an HTTP header with the "Cookie" name and cookie formatted value.
     *
     * @param cookie the cookie whose equivalent HTTP header is to be added.
     * @return this message for method chaining.
     */
    public HttpRequest withCookie(final Cookie cookie) {
        getHeaders().set(Cookie.HTTP_REQUEST_HEADER_NAME, cookie.toHttpRequestHeaderValue());
        return this;
    }

    public String toString() {
        return new PropertyBasedToStringBuilder(
                this,
                "method", this::getMethod,
                "uri", this::getUri,
                "httpVersion", this::getHttpVersion,
                "headers", super::getHeaders,
                "body", super::getBody
        ).build();
    }

    public static abstract class HttpRequestBuilder<C extends HttpRequest, B extends HttpRequestBuilder<C, B>> extends HttpMessageBuilder<C, B> {
        /**
         * The HTTP method.
         */
        protected String method = GET.name();
        /**
         * The request URI
         */
        protected URI uri;
        /**
         * The HTTP protocol version.
         */
        protected Version httpVersion;

        protected abstract B self();

        public B connect(final String uri) {
            method(CONNECT);
            return uri(uri);
        }

        public B connect(final URI uri) {
            method(CONNECT);
            return uri(uri);
        }

        public B connect() {
            method(CONNECT);
            return self();
        }

        public B delete(final String uri) {
            method(DELETE);
            return uri(uri);
        }

        public B delete(final URI uri) {
            method(DELETE);
            return uri(uri);
        }

        public B delete() {
            method(DELETE);
            return self();
        }

        public B get(final String uri) {
            method(GET);
            return uri(uri);
        }

        public B get(final URI uri) {
            method(GET);
            return uri(uri);
        }

        public B get() {
            method(GET);
            return self();
        }

        public B options(final String uri) {
            method(OPTIONS);
            return uri(uri);
        }

        public B options(final URI uri) {
            method(OPTIONS);
            return uri(uri);
        }

        public B options() {
            method(OPTIONS);
            return self();
        }

        public B post(final String uri) {
            method(POST);
            return uri(uri);
        }

        public B post(final URI uri) {
            method(POST);
            return uri(uri);
        }

        public B post() {
            method(POST);
            return self();
        }

        public B put(final String uri) {
            method(PUT);
            return uri(uri);
        }

        public B put(final URI uri) {
            method(PUT);
            return uri(uri);
        }

        public B put() {
            method(PUT);
            return self();
        }

        public B trace(final String uri) {
            method(TRACE);
            return uri(uri);
        }

        public B trace(final URI uri) {
            method(TRACE);
            return uri(uri);
        }

        public B trace() {
            method(TRACE);
            return self();
        }

        public B method(final HttpRequest.Method method) {
            this.method = method.name();
            return self();
        }

        public B method(final String methodOrExtension) {
            this.method = methodOrExtension;
            return self();
        }

        public B uri(final Consumer<UriBuilder> uriBuilderConsumer) {
            UriBuilder uriBuilder = new UriBuilder();
            uriBuilderConsumer.accept(uriBuilder);
            this.uri = uriBuilder.toUri();
            return self();
        }

        public B uri(final URI uri) {
            this.uri = uri;
            return self();
        }

        public B uri(final String uri) {
            this.uri = URI.create(uri);
            return self();
        }

        public B version(final Version version) {
            this.httpVersion = version;
            return self();
        }

        public B cookie(final Cookie cookie) {
            return header(Cookie.HTTP_REQUEST_HEADER_NAME, cookie.toHttpRequestHeaderValue());
        }

        public abstract C build();
    }

    private static final class HttpRequestBuilderImpl extends HttpRequestBuilder<HttpRequest, HttpRequestBuilderImpl> {
        private HttpRequestBuilderImpl() {
        }

        protected HttpRequestBuilderImpl self() {
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(this);
        }
    }

    public interface HttpRequestMethodBuilderSpec<B extends HttpMessageHeadersBuilderSpec<B>> {
        default B connect() {
            return method(CONNECT);
        }

        default B delete() {
            return method(DELETE);
        }

        default B get() {
            return method(GET);
        }

        default B options() {
            return method(OPTIONS);
        }

        default B post() {
            return method(POST);
        }

        default B put() {
            return method(PUT);
        }

        default B trace() {
            return method(TRACE);
        }

        default B method(final HttpRequest.Method method) {
            return method(method.name());
        }

        B method(final String methodOrExtension);
    }

    public interface HttpRequestUriBuilderSpec<B extends HttpRequestUriBuilderSpec> {
        B uri(final URI uri);

        default B uri(final String uri) {
            return uri(URI.create(uri));
        }

        default B uri(final Consumer<UriBuilder> uriBuilderConsumer) {
            UriBuilder uriBuilder = new UriBuilder();
            uriBuilderConsumer.accept(uriBuilder);
            return uri(uriBuilder.toUri());
        }

    }

    public interface HttpRequestBuilderSpec<B extends HttpRequestBuilderSpec<B>>
            extends HttpRequestMethodBuilderSpec<B>, HttpRequestUriBuilderSpec<B>, HttpMessageHeadersBuilderSpec<B> {

        default B connect(final String uri) {
            return method(CONNECT).uri(uri);
        }

        default B connect(final URI uri) {
            return method(CONNECT).uri(uri);
        }

        default B delete(final String uri) {
            return method(DELETE).uri(uri);
        }

        default B delete(final URI uri) {
            return method(DELETE).uri(uri);
        }

        default B get(final String uri) {
            return method(GET).uri(uri);
        }

        default B get(final URI uri) {
            return method(GET).uri(uri);
        }

        default B options(final String uri) {
            return method(OPTIONS).uri(uri);
        }

        default B options(final URI uri) {
            return method(OPTIONS).uri(uri);
        }

        default B post(final String uri) {
            return method(POST).uri(uri);
        }

        default B post(final URI uri) {
            return method(POST).uri(uri);
        }

        default B put(final String uri) {
            return method(PUT).uri(uri);
        }

        default B put(final URI uri) {
            return method(PUT).uri(uri);
        }

        default B trace(final String uri) {
            return method(TRACE).uri(uri);
        }

        default B trace(final URI uri) {
            return method(TRACE).uri(uri);
        }
    }
}
