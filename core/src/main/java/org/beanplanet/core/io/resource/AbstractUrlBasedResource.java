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

import org.beanplanet.core.io.IoException;
import org.beanplanet.core.io.Path;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;


/**
 * Provides some of the functionality of URL-based resources to subclasses.
 *
 * @author Gary Watson
 *
 */
public abstract class AbstractUrlBasedResource extends AbstractResource implements UrlBasedResource, Comparable<UrlBasedResource> {
    /** The URI backing this resource */
    private URI uri;

    /**
     * Constructs a new abstract URI resource with initial URI.
     */
    public AbstractUrlBasedResource() {
    }

    /**
     * Constructs a new abstract URI resource with the given URI.
     *
     * @param uri the URI of the resource.
     */
    public AbstractUrlBasedResource(URI uri) {
        this.uri = uri;
    }

    /**
     * Constructs a new abstract URI resource with the given URI string.
     *
     * @param uri the URI of the resource.
     * @exception URISyntaxException if the URI specified is invalid.
     */
    public AbstractUrlBasedResource(String uri) throws URISyntaxException {
        setUri(new URI(uri));
    }

    /**
     * Returns the URI backing this URI resource.
     *
     * @return the URI backing this resource.
     */
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
    public Path getPath() {
        throw new UnsupportedOperationException();
    }

    /**
     * Compares this resource to another.
     *
     * @param other the resource to be compared with this instance.
     * @return -1, 0 or 1 if this resource is less than, equal or greater than the specified resource.
     */
    public int compareTo(UrlBasedResource other) {
        URI thisURI  = getUri();
        URI otherURI = (other == null || other.getURI() == null ? null : other.getURI());
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
        if ( !(other instanceof UriBasedResource) ) {
            return false;
        }

        return compareTo((UrlBasedResource)other) == 0;
    }

    /**
     * Computes a hash code of this resource.
     *
     * @return the hash code of this resource, based on the resource URL.
     */
    public int hashCode() {
        return Objects.hash(getURI());
    }
}
