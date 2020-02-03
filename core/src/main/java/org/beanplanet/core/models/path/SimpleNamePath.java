/*
 *  MIT Licence:
 *
 *  Copyright (C) 2020 Beanplanet Ltd
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

package org.beanplanet.core.models.path;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * A simple name-path implementation where the name elements are supplied. By default
 * this path implementation is never absolute and does not have any root.
  */
public class SimpleNamePath implements NamePath {
    private final Supplier<List<String>> elementsSupplier;

    public SimpleNamePath(List<String> elements) {
        this(() -> elements);
    }

    public SimpleNamePath(Supplier<List<String>> elementsSupplier) {
        this.elementsSupplier = elementsSupplier;
    }

    @Override
    public List<String> getElements() {
        return elementsSupplier.get();
    }

    @Override
    public Path<String> parentPath() {
        final List<String> elements = getElements();

        return elements.size() < 2  ? null : new SimpleNamePath(() -> elements.subList(0, elements.size()-1));
    }

    @Override
    public boolean isAbsolute() {
        return false;
    }

    @Override
    public Path<String> toAbsolutePath() throws IllegalStateException {
        return this;
    }

    @Override
    public Path<String> normalise() {
        return this;
    }

    @Override
    public Object getRoot() {
        return null;
    }

    @Override
    public boolean hasRoot() {
        return false;
    }

    @Override
    public Path<String> relativeTo(Path<String> other) throws IllegalArgumentException {
        throw new IllegalArgumentException("Relative operations on delimited name paths is not supported");
    }

    @Override
    public Path<String> resolve(Path<String> other) {
        if (other == null || other.isEmpty()) return this;

        if ( other.isAbsolute() || other.hasRoot() ) return other;

        return join(other);
    }

    @Override
    public Path<String> join(Path<String> other) {
        if ( other == null ) return this;

        return new SimpleNamePath(() -> {
            final List<String> thisElements = getElements();
            final List<String> otherElements = other.getElements();

            final List<String> allElements = new ArrayList<>(thisElements.size()+otherElements.size());
            allElements.addAll(thisElements);
            allElements.addAll(otherElements);

            return allElements;
        });
    }

    @Override
    public URI toUri() {
        return null;
    }

    public boolean equals(Object other) {
        if (other == this) return true;
        if ( !(other instanceof NamePath) ) return false;

        NamePath that = (NamePath)other;
        return Objects.equals(this.getElements(), that.getElements());
    }

    public int hashCode() {
        return Objects.hash(getElements());
    }
}
