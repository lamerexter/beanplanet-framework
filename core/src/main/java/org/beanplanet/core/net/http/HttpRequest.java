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
import org.beanplanet.core.net.UriBuilder;
import org.beanplanet.core.util.PropertyBasedToStringBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Map;


/**
 * A model of an HTTP request.
 *
 * @author Gary Watson
 */
public class HttpRequest extends HttpMessage {
    /** The HTTP method. */
    private String method;
    /** The request URI being built */
    private UriBuilder requestUriBuilder;
    /** The HTTP protocol version. */
    private BigDecimal httpVersion;

    public HttpRequest() {
    }

    public HttpRequest withEntity(Resource entity) { super.withEntity(entity); return this; }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public HttpRequest withMethod(String method) { setMethod(method); return this; }

    public UriBuilder getRequestUriBuilder() {
        return requestUriBuilder;
    }

    public void setRequestUriBuidler(UriBuilder requestUriBuidler) {
        this.requestUriBuilder = requestUriBuidler;
    }

    public HttpRequest withRequestUriBuilder(UriBuilder requestUri) {
        setRequestUriBuidler(requestUriBuilder);
        return this;
    }

    public URI getRequestUri() {
        return requestUriBuilder == null ? null : requestUriBuilder.toUri();
    }

    public void setRequestUri(URI requestUri) {
        this.requestUriBuilder = new UriBuilder(requestUri);
    }

    public HttpRequest withRequestUri(URI requestUri) {
        setRequestUri(requestUri);
        return this;
    }

    public HttpRequest withRequestUri(String requestUri) {
        return withRequestUri(URI.create(requestUri));
    }

    public BigDecimal getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(BigDecimal httpVersion) {
        this.httpVersion = httpVersion;
    }

    public HttpRequest withHttpVersion(BigDecimal httpVersion) {
        setHttpVersion(httpVersion);
        return this;
    }

    public HttpRequest withHeaders(Map<String, Object> headers) {
        return (HttpRequest)super.withHeaders(headers);
    }

    public HttpRequest withHeader(String name, String value) {
       return (HttpRequest)super.withHeader(name, value);
    }

    public HttpRequest withContentType(String contentType) {
        return (HttpRequest)super.withContentType(contentType);
    }

    public HttpRequest withContentLength(int contentLength) {
        return (HttpRequest)super.withContentLength(contentLength);
    }

    public boolean hasQueryParameter(String name) {
        return requestUriBuilder != null && requestUriBuilder.hasQueryParameter(name);
    }

    public String toString() {
        return new PropertyBasedToStringBuilder(this).build();
    }
}
