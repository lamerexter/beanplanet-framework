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

package org.beanplanet.core.io.resource;

import org.beanplanet.core.UncheckedException;
import org.beanplanet.core.io.IoException;
import org.beanplanet.core.io.Path;
import org.beanplanet.core.net.UriBuilder;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

import static org.beanplanet.core.util.StringUtil.isEmptyOrNull;


/**
 * Provides some of the functionality of URL-based resources to subclasses.
 *
 * @author Gary Watson
 *
 */
public abstract class AbstractUriBasedResource extends AbstractResource implements UriCapableResource, Comparable<UrlCapableResource> {
    /** The URI backing this resource */
    private URI uri;

    /**
     * Constructs a new abstract URI resource with initial URI.
     */
    public AbstractUriBasedResource() {
    }

    /**
     * Constructs a new abstract URI resource with the given URI.
     *
     * @param uri the URI of the resource.
     */
    public AbstractUriBasedResource(URI uri) {
        this.uri = uri;
    }

    /**
     * Constructs a new abstract URI resource with the given URI string.
     *
     * @param uri the URI of the resource.
     * @exception URISyntaxException if the URI specified is invalid.
     */
    public AbstractUriBasedResource(String uri) throws URISyntaxException {
        setUri(new URI(uri));
    }

    /**
     * Returns the URI backing this URI resource.
     *
     * @return the URI backing this resource.
     */
    @Override
    public URI getUri() {
        return uri;
    }

    /**
     * Sets the URI backing this URI resource.
     *
     * @param uri the URI to back this resource.
     */
    public void setUri(URI uri) {
        this.uri = uri;
    }

    /**
     * Returns a Uniform Resource Locator (URL) for the resource, if the resource type supports URL
     * references.
     *
     * @return the URL of the resource.
     */
    public URL getUrl() {
        try {
            return uri == null ? null : uri.toURL();
        } catch (MalformedURLException e) {
            throw new IoException(String.format("The given URI resource [%s] could not be represented as a URL", uri), e);
        }
    }


    /**
     * Returns the full path of the resource, including any filename, from the URL of this resource.
     *
     * @return the full path of the resource, as returned from the URL of the resource.
     */
    @Override
    public Path<Resource> getPath() {
        return new UriResourcePath<>(this);
    }

    /**
     * Compares this resource to another.
     *fre
     * @param other the resource to be compared with this instance.
     * @return -1, 0 or 1 if this resource is less than, equal or greater than the specified resource.
     */
    public int compareTo(UrlCapableResource other) {
        URI thisURI  = getUri();
        URI otherURI = (other == null || other.getUri() == null ? null : other.getUri());
        if (thisURI == null && otherURI == null) {
            return 0;
        } else if (thisURI == null) {
            return -1;
        } else if (otherURI == null) {
            return 1;
        }

        return thisURI.compareTo(otherURI);
    }

    /**
     * Determines whether this resource is equal to another.
     *
     * @return true if this resource is equivalent to the specified resource, false otherwise.
     */
    public boolean equals(Object other) {
        if ( !(other instanceof UriCapableResource) ) {
            return false;
        }

        return compareTo((UrlCapableResource)other) == 0;
    }

    /**
     * Computes a hash code of this resource.
     *
     * @return the hash code of this resource, based on the resource URL.
     */
    public int hashCode() {
        return Objects.hash(this.getUri());
    }

    public String toString() {
        return getCanonicalForm();
    }

    /**
     * Resolve the given path relative to this resource.
     *
     * <p>
     * If the {@code path} is an {@link Path#isAbsolute() absolute}
     * path then this method simply returns a new resource with the given {@code path}. If {@code path}
     * is an <i>empty path</i> then this method simply returns this resource.
     * Otherwise this method considers this resource to be a directory and resolves
     * the given path against this resource's path. In the simplest case, the given path
     * does not have a {@link Path#getRoot root} component, in which case this method
     * <em>joins</em> the given path to the path of this resource and returns a resulting resource with path
     * that {@link java.nio.file.Path#endsWith ends} with the given path. Where the given path has
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
        if (path == null || path == Path.EMPTY_PATH || path.isEmpty()) return  this;

        if (path.isAbsolute()) {
            AbstractUriBasedResource newUriResource = cloneUnchecked(this);
            newUriResource.setUri(new UriBuilder(newUriResource.getUri()).withPath(path.toCanonicalPath()).toUri());
            return newUriResource;
        }

//        if (path.isAbsolute()) {
//            try {
//                AbstractUriBasedResource resourceWithAbsolutePath = (AbstractUriBasedResource)clone();
//                UriBuilder uriBuilder = new UriBuilder();
//                if (resourceWithAbsolutePath.getUri() != null) {
//                    uriBuilder.withUri(resourceWithAbsolutePath.getUri()).withPath(path.toAbsolutePath().toString());
//                }
//                uriBuilder.getPath(path);
//                resourceWithAbsolutePath.;
//            } catch (CloneNotSupportedException e) {
//                e.printStackTrace();
//            }
//        }
        throw new UnsupportedOperationException("Path resolution is not supported by this resource");
    }

    protected static AbstractUriBasedResource cloneUnchecked(AbstractUriBasedResource resource) {
        try {
            return  (AbstractUriBasedResource)resource.clone();
        } catch (CloneNotSupportedException e) {
            throw new UncheckedException(e);
        }
    }

    public static class UriResourcePath<Resource> implements Path<Resource> {
        private AbstractUriBasedResource resource;

        public UriResourcePath(AbstractUriBasedResource resource) {
            this.resource = resource;
        }

        @Override
        public URI toUri() {
            return resource.getUri();
        }

        @Override
        public boolean isAbsolute() {
            return resource.isAbsolute();
        }

        @Override
        public Path<Resource> normalise() {
            return this;
        }

        @Override
        public Path<Resource> getRoot() {
            AbstractUriBasedResource rootResource = cloneUnchecked(resource);
            rootResource.setUri(URI.create("/"));

            // TODO: Implement!
            return null; //(Resource)rootResource;
        }

        @Override
        public Resource getRootElement() {
            // TODO: Implement!
            return null;
        }

        @Override
        public Resource getElement(int index) {
            return null;
        }

        @Override
        public List<Resource> getElements() {
            return null;
        }

        @Override
        public List<Path<Resource>> getPathElements() {
            if (resource.getUri() == null || resource.getUri().getPath() == null) return Collections.emptyList();

            String uriPath = resource.getUri().getPath();
            if ( isEmptyOrNull(uriPath)) return Collections.emptyList();

            ArrayList<Path<Resource>> pathResources = new ArrayList<>();
            int fromIdx = 0, toIdx;
            while (fromIdx < uriPath.length()) {
                toIdx = uriPath.indexOf("/", fromIdx+1);
                if (toIdx < 0) toIdx = uriPath.length();

                AbstractUriBasedResource pathResource = cloneUnchecked(resource);
                pathResource.setUri(URI.create(uriPath.substring(0, (toIdx > 0 ? toIdx : uriPath.length()))));
                pathResources.add(new UriResourcePath<>(pathResource));

                fromIdx = toIdx+1;
            }

            return pathResources;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public List<String> getNameElements() {
            return null;
        }

        @Override
        public String getNameSeparator() {
            return null;
        }

        @Override
        public Path<Resource> relativeTo(Path<Resource> other) {
            return null;
        }

        @Override
        public Path<Resource> join(Path<Resource> other) {
            // TODO: Implement!
            return null;
        }

        /**
         * Returns an iterator over elements of type {@code T}.
         *
         * @return an Iterator.
         */
        @Override
        public Iterator<Path<Resource>> iterator() {
            return getPathElements().iterator();
        }
    }
}
