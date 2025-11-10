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

package org.beanplanet.core.io.resource;

import org.beanplanet.core.io.IoException;
import org.beanplanet.core.models.path.NamePath;
import org.beanplanet.core.models.path.Path;
import org.beanplanet.core.net.UriBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Arrays.stream;

public class UriResource extends AbstractUriBasedResource implements UriCapableResource {
    public UriResource() {}

    public UriResource(String uri) {
        this(URI.create(uri));
    }

    public UriResource(URI uri) {
        super(uri);
    }

    public UriResource(URL url) {
        this(url != null ? URI.create(url.toExternalForm()) : null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UriResource)) return false;
        UriResource that = (UriResource) o;
        return Objects.equals(getUri(), that.getUri());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUri());
    }

    /**
     * Whether this URI resource is absolute. A resource is absolute if it is a path-based resource and it does not need any
     * further information in order to locate or resolve it. For a URI, this means that it must be a base URI and a URL (scheme).
     *
     * @return true if the resource is absolute, false otherwise.
     */
    @Override
    public boolean isAbsolute() {
        return getUri() != null && getUri().isAbsolute();
    }

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public Resource getParentResource() {
        if (getUri() == null) return null;

        String path = getUri().getPath();
        String[] pathElements = path.split("\\/");
        if (pathElements.length <= 1) return null;

        return new UriResource(stream(pathElements, 0, pathElements.length-1).collect(Collectors.joining("/")));
    }

    /**
     * This abstract resource knows nothing about how to create a stream from the resource so simply throws an
     * <code>{@link UnsupportedOperationException}<code>. Implementations must provide a useful, specific,
     * implementation of this method suited to the type of backing resource.
     *
     * @return a newly created input stream for reading the resource.
     */
    @Override
    public InputStream getInputStream() {
        try {
            return getUri().toURL().openStream();
        } catch (IOException e) {
            throw new IoException(e);
        }
    }

    /**
     * Returns the canonical form of this URI-based resource, which is the URI itself.
     *
     * @return the URI of this resource.
     */
    public String getCanonicalForm() {
        return getUri() != null ? getUri().toString() : "UriResource[not URI set]";
    }

    /**
     * Resolve the given path relative to this resource.
     *
     * <p>
     * If the {@code path} is an {@link Path#isAbsolute() absolute}
     * path then this method simply returns a the resource with the given {@code path}. If {@code path}
     * is an <i>empty path</i> then this method simply returns this resource.
     * Otherwise this method considers this resource to be a directory and resolves
     * the given path against this resource's path. In the simplest case, the given path
     * does not have a {@link Path#getRoot root} component, in which case this method
     * <em>joins</em> the given path to the path of this resource and returns a resulting resource with path
     * that {@link String#endsWith(String)} the given path. Where the given path has
     * a root component then resolution is highly implementation dependent and
     * therefore unspecified.
     * </p>
     *
     * @param path the path to be resolved against this resource.
     * @return a new resource whose path is resolved relative to the path of this resource.
     * @throws UnsupportedOperationException if this resource is not a path-based resource or if the operation is not
     * supported on this type of resource.
     */
    public Resource resolve(Path<Resource> path) {
        if (path == null || path.isEmpty()) return this;

        if (path.isAbsolute() || path.hasRoot()) {
            return path.getElement();
        }

        // Assume this path represents a directory and join
        final Path<String> parentNamePath = getNamePath().parentPath();
        final Path<String> parentOrEmptyNamePath = parentNamePath == null ? NamePath.EMPTY_NAME_PATH : parentNamePath;
        final List<String> parentPathElements = parentOrEmptyNamePath.stream().collect(Collectors.toList());
        final List<String> otherPathElements = path.getElement().getNamePath().stream().collect(Collectors.toList());
        final List<String> joinPath = new ArrayList<>(parentPathElements);
        joinPath.addAll(otherPathElements);
        return new UriResource(new UriBuilder(getUri())
                                       .path(joinPath.stream().collect(Collectors.joining("/"))).toUri());
    }

    /**
     * Resolve the given path relative to this resource.
     *
     * <p>
     * If the {@code path} is an {@link org.beanplanet.core.models.path.Path#isAbsolute() absolute}
     * path then this method simply returns a new resource with the given {@code path}. If {@code path}
     * is an <i>empty path</i> then this method simply returns this resource.
     * Otherwise this method considers this resource to be a directory and resolves
     * the given path against this resource's path. In the simplest case, the given path
     * does not have a {@link org.beanplanet.core.models.path.Path#getRoot root} component, in which case this method
     * <em>joins</em> the given path to the path of this resource and returns a resulting resource with path
     * that {@link String#endsWith(String)} the given path. Where the given path has
     * a root component then resolution is highly implementation dependent and
     * therefore unspecified.
     * </p>
     *
     * @param path the path to be resolved against this resource.
     * @return a new resource whose path is resolved relative to the path of this resource.
     * @throws UnsupportedOperationException if this resource is not a path-based resource or if the operation is not
     * supported on this type of resource.
     */
    public Path<Resource> relativeTo(Path<Resource> path) {
        Path<Resource> thisPath = getPath();
        return thisPath == null ? null : thisPath.relativeTo(path);
    }


}
