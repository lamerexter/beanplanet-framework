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

package org.beanplanet.core.net;

import org.beanplanet.core.util.MultiValueListMapImpl;
import org.beanplanet.core.util.PropertyBasedToStringBuilder;
import org.beanplanet.core.util.StringUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.stream.Collectors.*;
import static org.beanplanet.core.net.UriUtil.decode;
import static org.beanplanet.core.net.UriUtil.encode;
import static org.beanplanet.core.util.StringUtil.ensureHasPrefix;
import static org.beanplanet.core.util.StringUtil.isEmptyOrNull;

/**
 * A Uniform Resource Identifier builder.  Builds URIs, as defined by <a href="https://tools.ietf.org/html/rfc3986">RFC 3986</a>.  This implementation allows
 * an incomplete URI to be built and provides full access to URI components.
 */
public class UriBuilder {
    // Components of all URIs
    private transient String scheme;
    private transient String fragment;

    // Hierarchical URI components (authority)
    private String userInfo;
    private String host;
    private Integer port;
    private String path;

    private MultiValueListMapImpl<String, String> queryParameters;

    public UriBuilder() {}

    public UriBuilder(URI uri) {
        withUri(uri);
    }

    public UriBuilder withUri(URI uri) {
        if (uri != null) {
            withScheme(uri.getScheme());
            withUserInfo(uri.getUserInfo());
            withHost(uri.getHost());
            withPort(uri.getPort());
            withPath(uri.getPath());

            if (uri.getQuery() != null) {
                withQueryParameters(new MultiValueListMapImpl<>(
                        Arrays.asList(uri.getQuery().split("&")).stream()
                                .map(s -> s.split("="))
                                .collect(groupingBy(s -> decode(s[0]), mapping(s -> decode(s[1]), toList())))
                ));
            }

            withFragment(uri.getFragment());
        }

        return this;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public UriBuilder withScheme(String scheme) {
        setScheme(scheme);
        return this;
    }

    public String getFragment() {
        return fragment;
    }

    public void setFragment(String fragment) {
        this.fragment = fragment;
    }

    public UriBuilder withFragment(String fragment) {
        setFragment(fragment);
        return this;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public UriBuilder withUserInfo(String userInfo) {
        setUserInfo(userInfo);
        return this;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public UriBuilder withHost(String host) {
        setHost(host);
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public UriBuilder withPort(Integer port) {
        setPort(port);
        return this;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public UriBuilder withPath(String path) {
        setPath(path);
        return this;
    }

    public MultiValueListMapImpl<String, String> getQueryParameters() {
        return queryParameters;
    }

    public boolean hasQueryParameter(String name) {
        return queryParameters != null && !queryParameters.getOrDefault(name, Collections.emptyList()).isEmpty();
    }

    public void setQueryParameters(MultiValueListMapImpl<String, String> queryParameters) {
        this.queryParameters = queryParameters;
    }

    public boolean hasAuthority() {
        return StringUtil.notEmptyAndNotNull(getUserInfo())
                || StringUtil.notEmptyAndNotNull(getHost())
                || getPort() != null;
    }

    public UriBuilder withQueryParameters(MultiValueListMapImpl<String, String> queryParameters) {
        setQueryParameters(queryParameters);
        return this;
    }

    public URI toUri() {
        try {
            return new URI(getScheme(),
                           getUserInfo(),
                           getHost(),
                           port == null ? -1 : getPort(),
                           hasAuthority() ? ( isEmptyOrNull(getPath()) ? "" : ensureHasPrefix(getPath(), "/")) : getPath(),
                           queryParameters == null ? null : queryParameters.entrySet()
                                                                           .stream()
                                                                           .map(e -> e.getValue()
                                                                                      .stream()
                                                                                      .map(v -> encode(e.getKey())+"="+encode(v))
                                                                                      .collect(Collectors.joining("&")))
                                                                           .collect(Collectors.joining("&")),
                           getFragment());
        } catch (URISyntaxException e) {
            throw new NetworkException(format("An attempt was made to create an incomplete or invalid URI from a built version [%s]", toString()), e);
        }
    }

    public String toString() {
        return new PropertyBasedToStringBuilder(this).build();
    }
}
