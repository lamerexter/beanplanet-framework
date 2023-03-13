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
import org.beanplanet.core.mediatypes.MediaType;
import org.beanplanet.core.util.MultiValueListMapImpl;
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
public class HttpResponse extends HttpMessage {
    public static final int OK = 200;
    public static final int BAD_REQUEST = 400;

    /** The HTTP response status code. */
    private int statusCode;
    /** The phrase associated with the given status code. */
    private String reasonPhrase;


    public HttpResponse() {
    }

    public HttpResponse(int status, MultiValueListMapImpl<String, String> headers) {
        this.statusCode = status;
        setHeaders(headers);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public HttpResponse withStatusCode(int statusCode) {
        setStatusCode(statusCode);
        return this;
    }

    public HttpResponse ok() {
        return withStatusCode(OK);
    }

    public HttpResponse badRequest() {
        return withStatusCode(BAD_REQUEST);
    }


    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }

    public HttpResponse withReasonPhrase(String reasonPhrase) {
        setReasonPhrase(reasonPhrase);
        return this;
    }

    public HttpResponse withHeaders(Map<String, Object> headers) {
        return (HttpResponse)super.withHeaders(headers);
    }

    public HttpResponse withHeader(String name, String value) {
        return (HttpResponse)super.withHeader(name, value);
    }

    public HttpResponse withContentType(MediaType contentType) {
        return (HttpResponse)super.withContentType(contentType);
    }

    public HttpResponse withContentType(String contentType) {
        return (HttpResponse)super.withContentType(contentType);
    }

    public HttpResponse withContentLength(int contentLength) {
        return (HttpResponse)super.withContentLength(contentLength);
    }

    public HttpResponse withEntity(Resource entity) {
        super.withEntity(entity);
        return this;
    }

    public String toString() {
        return new PropertyBasedToStringBuilder(this).build();
    }

    /**
     * Gets all cookies associated with the message by looking through the HTTP headers named "Cookie", for
     * a cookie with the given name.
     *
     * @return the cookies associated with this message, or an empty list of there are none.
     */
    public List<Cookie> getCookies() {
        final MultiValueListMapImpl<String, String> headers = getHeaders();
        if ( headers == null ) return Collections.emptyList();

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
        return (HttpResponse)super.withHeader(Cookie.HTTP_RESPONSE_HEADER_NAME, cookie.toHttpResponseHeaderValue());
    }
}
