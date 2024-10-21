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
import org.beanplanet.core.models.path.DelimitedNamePath;
import org.beanplanet.core.models.path.NamePath;
import org.beanplanet.core.models.path.Path;
import org.beanplanet.core.net.UriBuilder;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.beanplanet.core.util.StringUtil.isEmptyOrNull;


/**
 * Provides some of the functionality of URL-based resources to subclasses.
 *
 * @author Gary Watson
 *
 */
public abstract class AbstractUriBasedResource extends AbstractResource implements UriCapableResource, Comparable<UriCapableResource> {
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
        return new UriResourcePath(this);
    }

    /**
     * Compares this resource to another.
     *fre
     * @param other the resource to be compared with this instance.
     * @return -1, 0 or 1 if this resource is less than, equal or greater than the specified resource.
     */
    public int compareTo(UriCapableResource other) {
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

        return compareTo((UriCapableResource)other) == 0;
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

    protected static AbstractUriBasedResource cloneUnchecked(AbstractUriBasedResource resource) {
        try {
            return  (AbstractUriBasedResource)resource.clone();
        } catch (CloneNotSupportedException e) {
            throw new UncheckedException(e);
        }
    }

    public static class UriResourcePath implements Path<Resource> {
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
        public Path<Resource> toAbsolutePath() {
            return this;
        }

        @Override
        public Resource getRoot() {
            AbstractUriBasedResource rootResource = cloneUnchecked(resource);
            rootResource.setUri(URI.create("/"));

            return rootResource;
        }

        @Override
        public boolean hasRoot() {
            return resource != null && resource.isAbsolute();
        }

        @Override
        public Resource getElement() {
            return resource;
        }

        @Override
        public List<Resource> getElements() {
            if (resource.getUri() == null || resource.getUri().getPath() == null) return Collections.emptyList();

            String uriPath = resource.getUri().getPath();
            if ( isEmptyOrNull(uriPath)) return Collections.emptyList();

            List<Resource> pathResources = new ArrayList<>();
            int fromIdx = 0, toIdx;
            while (fromIdx < uriPath.length()) {
                toIdx = uriPath.indexOf("/", fromIdx+1);
                if (toIdx < 0) toIdx = uriPath.length();

                AbstractUriBasedResource pathResource = cloneUnchecked(resource);
                pathResource.setUri(URI.create(uriPath.substring(0, (toIdx > 0 ? toIdx : uriPath.length()))));
                pathResources.add(pathResource);

                fromIdx = toIdx+1;
            }

            return pathResources;
        }

        @Override
        public Path<Resource> parentPath() {
            return null;
        }

        @Override
        public Path<Resource> normalise() {
            return this;
        }

        @Override
        public Path<Resource> relativeTo(Path<Resource> other) {
            return null;
        }

        @Override
        public Path<Resource> resolve(Path<Resource> other) {
            return null;
        }

        @Override
        public Path<Resource> join(Path<Resource> other) {
            // Only the URI path will change, so preserve all other URI elements such as scheme, query parameters, fragment etc.
            UriBuilder uriBuilder = new UriBuilder(resource.getUri());
            URI otherUri = other.toUri();
            boolean otherHasRootPath = otherUri.getPath() != null && otherUri.getPath().startsWith("/");
            if (otherHasRootPath) {
                uriBuilder.path(otherUri.getPath());
            } else {
                UriResource otherResource = new UriResource(otherUri);

                List<String> pathNameElements = resource.getNamePath().getElements();
                List<String> otherPathNameElements = otherResource.getNamePath().getElements();
                StringBuilder combinedUriPath = new StringBuilder();
                boolean thisHasRootPath = resource.getUri().getPath() != null && resource.getUri().getPath().startsWith("/");
                for (int n=0; n < pathNameElements.size()-1; n++) {
                    combinedUriPath.append(combinedUriPath.length() > 0 ? "/" : "").append(pathNameElements.get(n));
                }
                for (int n=0; n < otherPathNameElements.size(); n++) {
                    combinedUriPath.append(combinedUriPath.length() > 0 ? "/" : "").append(otherResource.getNamePath().getElements().get(n));
                }
                uriBuilder.path((thisHasRootPath || (pathNameElements.isEmpty() && !otherPathNameElements.isEmpty()) ? "/" : "") + combinedUriPath.toString());
            }

            return new UriResource(uriBuilder.toUri()).getPath();
        }
    }

    /**
     * Returns the name-path to this URI resource. For a URI, the name-path is simple the URI path, if present.
     *
     * @return Always the empty resource in this default abstract implementation.
     */
    public NamePath getNamePath() {
        return new DelimitedNamePath(() -> getUri().getPath(), () -> "/");
    }
}
