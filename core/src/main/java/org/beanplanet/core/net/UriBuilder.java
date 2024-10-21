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

import org.beanplanet.core.models.path.DelimitedNamePath;
import org.beanplanet.core.models.path.NamePath;
import org.beanplanet.core.models.path.PathUtil;
import org.beanplanet.core.util.MultiValueListMap;
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
import static org.beanplanet.core.util.StringUtil.*;

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

    private MultiValueListMap<String, String> queryParameters;

    public UriBuilder() {
    }

    public UriBuilder(final UriBuilder other) {
        this.scheme = other.scheme;
        this.fragment = other.fragment;
        this.userInfo = other.userInfo;
        this.host = other.host;
        this.port = other.port;
        this.path = other.path;
    }

    public UriBuilder(URI uri) {
        uri(uri);
    }

    public UriBuilder uri(URI uri) {
        if (uri != null) {
            scheme(uri.getScheme());
            userInfo(uri.getUserInfo());
            host(uri.getHost());
            withPort(uri.getPort());
            path(uri.getPath());

            if (!isBlank(uri.getQuery())) {
                queryParameters(new MultiValueListMapImpl<>(
                        Arrays.asList(uri.getQuery().split("&")).stream()
                              .map(s -> s.split("="))
                              .collect(groupingBy(s -> decode(s[0]), mapping(s -> decode(s[1]), toList())))
                ));
            }

            fragment(uri.getFragment());
        }

        return this;
    }

    public UriBuilder merge(UriBuilder override) {
        UriBuilder copy = new UriBuilder(this);

        if ( isNotBlank(override.scheme) ) copy.scheme = override.scheme;
        if ( isNotBlank(override.userInfo) ) copy.userInfo = override.userInfo;
        if ( isNotBlank(override.host) ) copy.host = override.host;
        if ( override.port >=0 ) copy.port = override.port;

        if ( isNotBlank(override.path)) {
            if ( isBlank(copy.path) ) {
                copy.path = override.path;
            } else if ( isNotBlank(override) ) {
                DelimitedNamePath copyPath = new DelimitedNamePath(copy.path, "/");
                DelimitedNamePath subPath = new DelimitedNamePath(override.path, "/");
                copy.path = copyPath.join(subPath).join();
            }
        }

        return copy;
    }

    public String scheme() {
        return scheme;
    }

    public UriBuilder scheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    public String fragment() {
        return fragment;
    }

    public UriBuilder fragment(String fragment) {
        this.fragment = fragment;
        return this;
    }

    public String userInfo() {
        return userInfo;
    }

    public UriBuilder userInfo(String userInfo) {
        this.userInfo = userInfo;
        return this;
    }

    public String host() {
        return host;
    }

    public UriBuilder host(String host) {
        this.host = host;
        return this;
    }

    public Integer port() {
        return port;
    }

    public UriBuilder port(Integer port) {
        this.port = port;
        return this;
    }

    public UriBuilder withPort(Integer port) {
        port(port);
        return this;
    }

    public String path() {
        return path;
    }

    public UriBuilder path(String path) {
        this.path = path;
        return this;
    }

    public MultiValueListMap<String, String> queryParameters() {
        return queryParameters;
    }

    public boolean hasQueryParameter(String name) {
        return queryParameters != null && !queryParameters.getOrDefault(name, Collections.emptyList()).isEmpty();
    }

    public UriBuilder queryParameters(MultiValueListMap<String, String> queryParameters) {
        this.queryParameters = queryParameters;
        return this;
    }

    public boolean hasAuthority() {
        return StringUtil.notEmptyAndNotNull(userInfo())
                || StringUtil.notEmptyAndNotNull(host())
                || port() != null;
    }

    public URI toUri() {
        try {
            return new URI(scheme(),
                    userInfo(),
                    host(),
                    port == null ? -1 : port(),
                    hasAuthority() ? (isEmptyOrNull(path()) ? "" : ensureHasPrefix(path(), "/")) : path(),
                    queryParameters == null ? null : queryParameters.entrySet()
                                                                    .stream()
                                                                    .map(e -> e.getValue()
                                                                               .stream()
                                                                               .map(v -> encode(e.getKey()) + "=" + encode(v))
                                                                               .collect(Collectors.joining("&")))
                                                                    .collect(Collectors.joining("&")),
                    fragment());
        } catch (URISyntaxException e) {
            throw new NetworkException(format("An attempt was made to create an incomplete or invalid URI from a built version [%s]", toString()), e);
        }
    }

    public URI build() {
        return toUri();
    }

    public String toString() {
        return new PropertyBasedToStringBuilder(this).build();
    }
}
