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

package org.beanplanet.core.models.path;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

/**
 * A simple name-path implementation where the name elements are supplied. By default
 * this path implementation is never absolute and does not have any root.
  */
public class SimpleNamePath implements NamePath {
    private final Supplier<List<String>> elementsSupplier;

    public SimpleNamePath(String name) {
        this(singletonList(name));
    }

    public SimpleNamePath(List<String> elements) {
        this(() -> elements);
    }

    public SimpleNamePath(String ... elements) {
        this(asList(elements));
    }

    public SimpleNamePath(Supplier<List<String>> elementsSupplier) {
        this.elementsSupplier = elementsSupplier;
    }

    @Override
    public List<String> getElements() {
        return elementsSupplier.get();
    }

    @Override
    public SimpleNamePath parentPath() {
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
    public NamePath join(Path<String> other) {
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
