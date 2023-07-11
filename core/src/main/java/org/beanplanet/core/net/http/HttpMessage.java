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

import org.beanplanet.core.mediatypes.MediaType;
import org.beanplanet.core.util.CollectionUtil;
import org.beanplanet.core.util.MultiValueListMapImpl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import static org.beanplanet.core.util.CollectionUtil.lastOrNull;

/**
 * A model of an HTTP message, common to both request and response.
 *
 * @author Gary Watson
 */
public abstract class HttpMessage {
    /** The headers associated with the message. */
    private MultiValueListMapImpl<String, String> headers;
    /** The entity associated with the message. */
    private HttpEntity entity;

    @SuppressWarnings("unchecked")
    public <B extends HttpMessage> B withBuilder(Class<B> builder) {
        return (B)this;
    }

    /**
     * Gets the headers associated with the message.
     *
     * @return the headers associated with the message, which may be null if there are none.
     */
    public MultiValueListMapImpl<String, String> getHeaders() {
        return headers;
    }

    /**
     * Get all headers with the given name.
     *
     * @param headerName the name of header whose instances are to be returned.
     * @return all values of the named header, or the empty list if there are no headers with the given name.
     */
    public List<String> getHeaders(final String headerName) {
        return headers == null ? Collections.emptyList() : headers.entrySet().stream().filter(e -> headerName.equalsIgnoreCase(e.getKey())).map(Entry::getValue).findFirst().orElse(Collections.emptyList());
    }

    /**
     * Get the last header with the given name.
     *
     * @param headerName the name of header whose last instance is to be returned.
     * @return the value of the last instance of the named header, or null if there are no headers with the given name.
     */
    public String getLastHeader(final String headerName) {
        return lastOrNull(getHeaders(headerName));
    }

    /**
     * Adds an HTTP header to this message.
     *
     * @param name the name of the HTTP header to add.
     * @param value the value of the HTTP header to add.
     * @see #withHeader(String, String)
     */
    public void addHeader(final String name, final String value) {
       withHeader(name, value);
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

        for (Entry<String, Object> header : headers.entrySet()) {
            withHeader(header.getKey(), String.valueOf(header.getValue()));
        }

        return this;
    }

    /**
     * Adds an HTTP header to this message.
     *
     * @param name the name of the HTTP header to add.
     * @param value the value of the HTTP header to add.
     * @return this message for method chaining.
     */
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
    public HttpEntity getEntity() {
        return entity;
    }

    /**
     * Sets the entity associated with the message.
     *
     * @param entity the entity associated with the message.
     */
    public void setEntity(HttpEntity entity) {
        this.entity = entity;
    }

    /**
     * Sets the entity associated with the message.
     *
     * @param entity the entity associated with the message.
     * @return this instance for method chaining.
     *
     */
    public HttpMessage withEntity(HttpEntity entity) {
        setEntity(entity);
        return this;
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
    public abstract HttpMessage withCookie(final Cookie cookie);

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
}
