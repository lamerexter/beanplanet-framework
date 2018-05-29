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

import java.net.URI;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class UriBasedResourceImpl extends AbstractUrlBasedResource implements UriBasedResource {
    public UriBasedResourceImpl() {}

    public UriBasedResourceImpl(String uri) {
        this(URI.create(uri));
    }

    public UriBasedResourceImpl(URI uri) {
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

        return new UriBasedResourceImpl(stream(pathElements, 0, pathElements.length-1).collect(Collectors.joining("/")));
    }
}
