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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class UriResource extends AbstractUriBasedResource implements UriCapableResource {
    public UriResource() {}

    public UriResource(String uri) {
        this(URI.create(uri));
    }

    public UriResource(URI uri) {
        super(uri);
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
}
