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

import org.beanplanet.core.io.resource.Resource;
import org.beanplanet.core.util.MultiValueListMap;
import org.beanplanet.core.util.PropertyBasedToStringBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A model of an HTTP response.
 *
 * @author Gary Watson
 */
public class HttpResponse extends AbstractHttpMessage implements Response {
    public static final int OK = 200;
    public static final int BAD_REQUEST = 400;

    /**
     * The status line information associated with the HTTP Response.
     */
    private HttpResponseStatus status;

    public HttpResponse() {
    }

    public HttpResponse(final HttpResponseStatus status,
                        final HttpHeaders headers,
                        final Resource body) {
        super(headers, body);
        this.status = status;
    }

    public HttpResponse(final int statusCode,
                        final String reasonPhrase,
                        final HttpHeaders headers,
                        final Resource body) {
        this(new HttpResponseStatus(statusCode, reasonPhrase), headers, body);
    }

    public HttpResponse(final int statusCode,
                        final HttpHeaders headers) {
        this(statusCode, null, headers, null);
    }

    public HttpResponse(final int statusCode,
                        final HttpHeaders headers,
                        final Resource body) {
        this(statusCode, null, headers, body);
    }

    public HttpResponse(final int statusCode,
                        final Resource body) {
        this(statusCode, null, null, body);
    }

    public HttpResponse(final int statusCode) {
        this(statusCode, null, null, null);
    }

    private HttpResponse(HttpResponseBuilder<?, ?> builder) {
        super(builder);
        this.status = new HttpResponseStatus(builder.statusCode, builder.reasonPhrase);
    }

    public HttpResponseStatus getStatus() {
        return status;
    }

    public HttpResponse ok() {
        return new HttpResponse(OK);
    }

    public HttpResponse badRequest() {
        return new HttpResponse(BAD_REQUEST);
    }

    public String toString() {
        return new PropertyBasedToStringBuilder(
                this,
                "statusCode", this::getStatusCode,
                "reasonPhrase", this::getReasonPhrase,
                "headers", super::getHeaders,
                "body", super::getBody
        ).build();
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
                      .filter(e -> Cookie.HTTP_RESPONSE_HEADER_NAME.equalsIgnoreCase(e.getKey()))
                      .map(Map.Entry::getValue)
                      .flatMap(List::stream)
                      .map(Cookie::fromHttpHeaderValue)
                      .collect(Collectors.toList());
    }

    /**
     * Adds a cookie to this response message by adding an HTTP header with the "Set-Cookie" name and cookie formatted value.
     *
     * @param cookie the cookie whose equivalent HTTP header is to be added.
     * @return this message for method chaining.
     */
    public HttpResponse withCookie(final Cookie cookie) {
        getHeaders().set(Cookie.HTTP_RESPONSE_HEADER_NAME, cookie.toHttpResponseHeaderValue());
        return this;
    }

    public static HttpResponseBuilder<?, ?> builder() {
        return new HttpResponseBuilderImpl();
    }

    public static abstract class HttpResponseBuilder<C extends HttpResponse, B extends HttpResponseBuilder<C, B>> extends HttpMessageBuilder<C, B> {
        /**
         * The HTTP response status code.
         */
        protected int statusCode;
        /**
         * The phrase associated with the given status code.
         */
        protected String reasonPhrase;

        protected abstract B self();

        public B statusCode(final int statusCode) {
            this.statusCode = statusCode;
            return self();
        }

        public B reasonPhrase(final String reasonPhrase) {
            this.reasonPhrase = reasonPhrase;
            return self();
        }

        public abstract C build();
    }

    private static final class HttpResponseBuilderImpl extends HttpResponseBuilder<HttpResponse, HttpResponseBuilderImpl> {
        private HttpResponseBuilderImpl() {
        }

        protected HttpResponseBuilderImpl self() {
            return this;
        }

        public HttpResponse build() {
            return new HttpResponse(this);
        }
    }
}
