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

import java.util.Map;

/**
 * A model of an HTTP message, common to both request and response.
 *
 * @author Gary Watson
 */
public class HttpMessage {
    /** The headers associated with the message. */
    private MultiValueListMapImpl<String, String> headers;
    /** The entity associated with the message. */
    private Resource entity;

    @SuppressWarnings("unchecked")
    public <B extends HttpMessage> B withBuilder(Class<B> builder) {
        return (B)this;
    }
    /**
     * Gets the headers associated with the message.
     *
     * @return the headers associated with the message.
     */
    public MultiValueListMapImpl<String, String> getHeaders() {
        return headers;
    }

    /**
     * Sets the headers associated with the message.
     *
     * @param headers the headers associated with the message.
     */
    public void setHeaders(MultiValueListMapImpl<String, String> headers) {
        this.headers = headers;
    }

    public HttpMessage withHeaders(Map<String, Object> headers) {
        if (headers == null) return null;

        for (Map.Entry<String, Object> header : headers.entrySet()) {
            withHeader(header.getKey(), String.valueOf(header.getValue()));
        }

        return this;
    }

    public HttpMessage withHeader(String name, String value) {
        if (headers == null) {
            this.headers = new MultiValueListMapImpl<>();
        }

        headers.addValue(name, value);
        return this;
    }

    public HttpMessage withContentType(MediaType contentType) {
        return withContentType(contentType.getName());
    }

    public HttpMessage withContentType(String contentType) {
        return withHeader("Content-Type", contentType);
    }

    public HttpMessage withContentLength(int contentLength) {
        return withHeader("Content-Length", Integer.toString(contentLength));
    }

    /**
     * Gets the entity associated with the message.
     *
     * @return the entity associated with the message.
     */
    public Resource getEntity() {
        return entity;
    }

    /**
     * Sets the entity associated with the message.
     *
     * @param entity the entity associated with the message.
     */
    public void setEntity(Resource entity) {
        this.entity = entity;
    }

    /**
     * Sets the entity associated with the message.
     *
     * @param entity the entity associated with the message.
     * @return this instance for method chaining.
     *
     */
    public HttpMessage withEntity(Resource entity) {
        setEntity(entity);
        return this;
    }
}
