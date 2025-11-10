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

import org.beanplanet.core.util.DateUtil;
import org.beanplanet.core.util.StringUtil;

import java.util.*;

import static org.beanplanet.core.net.http.HttpHeaders.DATE_VALUE_FORMAT;

/**
 * Represents a cookie, as specified by RFC 2109.
 *
 * <p>A cookie is a small, textual based, amount of information sent from a web server
 * to a web client, such as a web browser.  The cookie contains information that
 * pertains to one or more processes running on the server and, once received by the
 * web client, is sent back to the server in subsequent requests.
 *
 * <p>As the web protocol of today is HTTP which is a stateless protocol, common uses of
 * cookies today include user tracking and session management.
 *
 * <p>A cookie has a name, a single value, and optional attributes such as a comment,
 * path and domain qualifiers, a maximum age, and a version number.
 */
public class Cookie {
    public static final String HTTP_REQUEST_HEADER_NAME = "Cookie";
    public static final String HTTP_RESPONSE_HEADER_NAME = "Set-Cookie";
    public static final String NAME = "Name";
    public static final String VALUE = "Value";
    public static final String COMMENT = "Comment";
    public static final String DOMAIN = "Domain";
    public static final String MAXAGE = "Max-Age";
    public static final String EXPIRES = "Expires";
    public static final String PATH = "Path";
    public static final String SAMESITE = "SameSite";
    public static final String SECURE = "Secure";
    public static final String VERSION = "Version";

    public enum SameSite {
        Lax, None, Strict;
    }

    /**
     * A table of name/value attribute pairs that comprise the cookie properties.
     */
    private final Map<String, String> attributes;

    public Cookie(String name, String value) {
        this(name, value, new LinkedHashMap<>());
    }

    public Cookie(String name, String value, Map<String, String> attributes) {
        this(attributes);
        attributes.put(NAME, name);
        attributes.put(VALUE, value);
    }

    public Cookie(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public Optional<String> getAttribute(final String name) {
        return attributes.entrySet().stream().filter(e -> name.equalsIgnoreCase(e.getKey())).map(Map.Entry::getValue).filter(Objects::nonNull).findFirst();
    }

    public void setAttribute(final String name, final String value) {
        attributes.put(name, value);
    }

    /**
     * Returns the comment describing the purpose of this cookie,
     * or null if the cookie has no comment.
     *
     * @return String a comment for the cookie, or null if none
     */
    public String getComment() {
        return getAttribute(COMMENT).orElse(null);
    }

    /**
     * Specifies a comment that describes a cookie's purpose.
     * The comment is useful if the browser presents the cookie to the user.
     *
     * <p>Comments are not supported by Netscape Version 0 cookies.
     *
     * @param comment a String specifying the comment to display to the user
     * @see #getComment
     */
    public void setComment(String comment) {
        setAttribute(COMMENT, comment);
    }

    /**
     * Returns the domain within which this cookie should be presented.
     * The form of the domain name is specified by RFC 2109.
     * A domain name begins with a dot (.foobar.com) and means that the cookie
     * is visible to servers in a specified Domain Name System (DNS)
     * zone (for example, www.foo.com, but not a.b.foo.com).
     *
     * <p>By default, cookies are only returned to the server that sent them.
     *
     * @return String the domain name where the cookie resides.
     */
    public String getDomain() {
        return getAttribute(DOMAIN).orElse(null);
    }

    /**
     * Specifies the domain within which this cookie should be presented.
     *
     * <p>The form of the domain name is specified by RFC 2109. A domain name
     * begins with a dot (.foobar.com) and means that the cookie is visible
     * to servers in a specified Domain Name System (DNS) zone (for example,
     * www.foobar.com, but not a.b.foo.com).
     *
     * <p>By default, cookies are only returned to the server that sent them.
     *
     * @param domain a String containing the domain name within which this cookie
     *               is visible; form is according to RFC 2109
     */
    public void setDomain(String domain) {
        setAttribute(DOMAIN, domain);
    }

    /**
     * Returns the computed <code>Date</code> when this cookie will expire. The date
     * will depend either on the date set private the value of the <i>maxAge</i>
     * property.
     *
     * @return the expiry date/time of the cookie in the default timezone.
     */
    public Date getExpiryDate() {
        return getAttribute(EXPIRES).map(dateStr -> DateUtil.parse(dateStr, DATE_VALUE_FORMAT)).orElse(null);
    }

    /**
     * Sets the computed <code>Date</code> when this cookie will expire. The date
     * will depend either on the date set private the value of the <i>maxAge</i>
     * property.
     *
     * @paran expires the expiry date/time of the cookie in the default timezone.
     */
    public void setExpiryDate(Date expires) {
        setAttribute(EXPIRES, DateUtil.toString(expires, DATE_VALUE_FORMAT));
    }

    /**
     * Returns the maximum age of the cookie (seconds).
     *
     * @return int the maximum age of the cookie, in seconds.  By default, -1
     * indicates the cookie will persist until browser shutdown.
     */
    public Integer getMaxAge() {
        return getAttribute(MAXAGE).map(Integer::parseInt).orElse(null);
    }

    /**
     * Sets the maximum age of the cookie in seconds.
     *
     * <p>A positive value indicates that the cookie will expire after that
     * many seconds have passed. Note that the value is the maximum age when
     * the cookie will expire, not the cookie's current age.
     *
     * <p>A negative value means that the cookie is not stored persistently and
     * will be deleted when the Web browser exits. A zero value causes the cookie
     * to be deleted.
     *
     * @param maxAge New value of property maxAge.
     */
    public void setMaxAge(int maxAge) {
        setAttribute(MAXAGE, Integer.toString(maxAge));
    }

    /**
     * Returns the name of the cookie.
     *
     * @return String the name of the cookie.
     */
    public String getName() {
        return getAttribute(NAME).orElse(null);
    }

    /**
     * Sets the name of the cookie.
     *
     * @param name the name of the cookie.
     * @return String the name of the cookie.
     */
    public void setName(final String name) {
        setAttribute(NAME, name);
    }

    /**
     * Returns the path on the server to which the browser returns this cookie.
     * The cookie is visible to all subpaths on the server.
     *
     * @return String specifying a path to this cookie, for example <I>/mycookies</I>.
     */
    public String getPath() {
        return getAttribute(PATH).orElse(null);
    }

    /**
     * Specifies a path for the cookie to which the client should return the cookie.
     *
     * <p>The cookie is visible to all the pages in the directory you specify,
     * and all the pages in that directory's subdirectories.
     *
     * <p>If the cookie originated from a Java Servlet, the path must include the
     * servlet that set the cookie, for example, /catalog, which makes the cookie
     * visible to all directories on the server under /catalog.
     *
     * <p>Consult RFC 2109 (available on the Internet) for more information on setting
     * path names for cookies.
     *
     * @param path New value of property path.
     */
    public void setPath(String path) {
        setAttribute(PATH, path);
    }

    /**
     * Returns true if the cookie requires that it be sent over a secure protocol,
     * or false if the cookie can be sent using any protocol.
     *
     * @return boolean true is the cookie must be sent securely, false if not.
     */
    public Boolean isSecure() {
        return getAttribute(SECURE).filter(Objects::nonNull).map(Boolean::valueOf).orElse(null);
    }

    /**
     * Indicates to the browser whether the cookie should only be sent
     * using a secure protocol, such as HTTPS or SSL.
     *
     * @param secure New value of property secure.
     */
    public void setSecure(boolean secure) {
        setAttribute(SECURE, Boolean.toString(secure));
    }

    /**
     * Returns whether or not a cookie is sent with cross-site requests, providing some protection against cross-site request forgery attacks (CSRF).
     *
     * <p>The possible attribute values are:
     * <dl>
     * <dt>Strict</dt><dd>means that the browser sends the cookie only for same-site requests, that is, requests originating from the same site that set the cookie. If a request originates from a different domain or scheme (even with the same domain), no cookies with the SameSite=Strict attribute are sent.</dd>
     * <dt>Lax</dt><dd>means that the cookie is not sent on cross-site requests, such as on requests to load images or frames, but is sent when a user is navigating to the origin site from an external site (for example, when following a link). This is the default behavior if the SameSite attribute is not specified.</dd>
     * <dt>None</dt><dd>means that the browser sends the cookie with both cross-site and same-site requests. The Secure attribute must also be set when setting this value, like so <code>SameSite=None; Secure</code></dd>
     * </dl>
     * </p>
     *
     * @return boolean true is the cookie must be sent securely, false if not.
     */
    public SameSite getSameSite() {
        return getAttribute(SAMESITE).map(SameSite::valueOf).orElse(null);
    }

    /**
     * Sets whether or not a cookie is sent with cross-site requests, providing some protection against cross-site request forgery attacks (CSRF).
     *
     * <p>The possible attribute values are:
     * <dl>
     * <dt>Strict</dt><dd>means that the browser sends the cookie only for same-site requests, that is, requests originating from the same site that set the cookie. If a request originates from a different domain or scheme (even with the same domain), no cookies with the SameSite=Strict attribute are sent.</dd>
     * <dt>Lax</dt><dd>means that the cookie is not sent on cross-site requests, such as on requests to load images or frames, but is sent when a user is navigating to the origin site from an external site (for example, when following a link). This is the default behavior if the SameSite attribute is not specified.</dd>
     * <dt>None</dt><dd>means that the browser sends the cookie with both cross-site and same-site requests. The Secure attribute must also be set when setting this value, like so <code>SameSite=None; Secure</code></dd>
     * </dl>
     * </p>
     *
     * @param sameSite the value for sameSite.
     * @return boolean true is the cookie must be sent securely, false if not.
     */
    public void setSecure(SameSite sameSite) {
        setAttribute(SECURE, sameSite.name());
    }

    /**
     * Returns the value of the cookie.
     *
     * @return String the cookie's current value.
     */
    public String getValue() {
        return getAttribute(VALUE).orElse(null);
    }

    /**
     * Sets the value of the cookie.
     *
     * @param value the cookie value.
     * @return String the cookie's current value.
     */
    public void setValue(final String value) {
        setAttribute(VALUE, value);
    }

    /**
     * Returns the version of the protocol this cookie complies with.
     * Version 1 complies with RFC 2109, and version 0 complies with the original
     * cookie specification drafted by Netscape.
     *
     * <p>Cookies provided by a browser use and identify the browser's cookie version.
     *
     * @return int 0 if the cookie complies with the original Netscape specification;
     * 1 if the cookie complies with RFC 2109
     */
    public Integer getVersion() {
        return getAttribute(VERSION).map(Integer::parseInt).orElse(null);
    }

    /**
     * Sets the version of the cookie protocol this cookie complies with.
     *
     * <p>Version 0 complies with the original Netscape cookie specification.
     * Version 1 complies with RFC 2109.
     *
     * <p>Since RFC 2109 is still somewhat new, consider version 1 as experimental;
     * do not use it yet on production sites.
     *
     * @param version 0 if the cookie should comply with the original Netscape specification;
     *                1 if the cookie should comply with RFC 2109
     */
    public void setVersion(int version) {
        setAttribute(VALUE, Integer.toString(version));
    }

    /**
     * Creates a cookie from the request/response HTTP header value.
     *
     * @param headerValue the HTTP message header value.
     * @return a cookie, comprised of all of the attributes parsed from the given HTTP header value, or null if the header value was null.
     */
    public static Cookie fromHttpHeaderValue(final String headerValue) {
        if ( headerValue == null ) return null;

        final LinkedHashMap<String, String> cookieAttributes = new LinkedHashMap<>();
        final List<String> attrValueList = StringUtil.asDsvList(headerValue, ";");
        for (int n=0; n < attrValueList.size(); n++) {
            final String attrNameValue = attrValueList.get(n).trim();
            final int equalsPos = attrNameValue.indexOf('=');
            final String attrName = attrNameValue.substring(0, equalsPos >= 0 ? equalsPos : attrNameValue.length());
            final String attrValue = StringUtil.trim(equalsPos < 0 ? null : attrNameValue.substring(equalsPos+1), "\"");

            if (n == 0) {
                cookieAttributes.put(NAME, attrName);
                cookieAttributes.put(VALUE, attrValue);
            } else {
                cookieAttributes.put(attrName, attrValue);
            }
        }

        return new Cookie(cookieAttributes);
    }

    /**
     * Creates an HTTP request (Cookie) header value.
     *
     * @return an HTTP header value, suitable for use an an HTTP request "Cooke: name=value [; attr=value]*" header, which may be null if the cookie does not have a name.
     */
    public String toHttpRequestHeaderValue() {
        final String name = getName();
        if ( StringUtil.isEmptyOrNull(name) ) return null;

        StringBuilder s = new StringBuilder();
        final String value = getValue();
        s.append(name).append("=").append(value == null ? "" : value.indexOf(' ') >= 0 ? "\""+value+"\"" : value);

        attributes.entrySet().stream().filter(e -> !(NAME.equalsIgnoreCase(e.getKey()) || VALUE.equalsIgnoreCase(e.getKey()))).forEach(e -> {
            final String attrName = e.getKey();
            final String attrValue = e.getValue();
            s.append(s.length() == 0 ? "" : "; ").append(attrName).append("=").append(attrValue == null ? "" : attrValue.indexOf(' ') >= 0 ? "\""+attrValue+"\"" : attrValue);
        });

        return s.toString();
    }

    /**
     * Creates an HTTP response (Set-Cookie) header value.
     *
     * @return an HTTP header value, suitable for use an an HTTP response "Set-Cooke: name=value" header, which may be null if the cookie does not have a name.
     */
    public String toHttpResponseHeaderValue() {
        final String name = getName();
        if ( StringUtil.isEmptyOrNull(name) ) return null;

        StringBuilder s = new StringBuilder();
        final String value = getValue();
        s.append(name).append("=").append(value == null ? "" : value.indexOf(' ') >= 0 ? "\""+value+"\"" : value);

        return s.toString();
    }
}
