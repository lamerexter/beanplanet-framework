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

import org.beanplanet.core.util.StringUtil;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.beanplanet.core.util.StringUtil.asDsvList;
import static org.beanplanet.core.util.StringUtil.isEmptyOrNull;

/**
 * A simple name-path implementation where the name elements are string delimited. By default
 * this path implementation is never absolute and does not have any root.
  */
public class DelimitedNamePath implements NamePath {
    protected final Supplier<String> path;
    protected final Supplier<String> delimiter;

    public DelimitedNamePath(String path, String delimiter) {
        this(() -> path, () -> delimiter);
    }

    public DelimitedNamePath(Supplier<String> path, Supplier<String> delimiter) {
        this.path = path;
        this.delimiter = delimiter;
    }

    /**
     * Gets the delimiter configured on this instance.
     *
     * return the delimiter of this name path.
     */
    public String getDelimiter() {
        return delimiter.get();
    }

    @Override
    public List<String> getElements() {
        String pathString = path.get();
        String delimiterString = delimiter.get();
        if (StringUtil.isEmptyOrNull(pathString) || isEmptyOrNull(delimiterString)) return Collections.emptyList();

        return asDsvList(path.get(), delimiter.get());
    }

    @Override
    public Path<String> parentPath() {
        String pathString = path.get();
        String delimiterString = delimiter.get();
        if (StringUtil.isEmptyOrNull(pathString) || isEmptyOrNull(delimiterString)) return null;

        int lastDelimIndex = pathString.lastIndexOf(delimiterString);
        return lastDelimIndex < 0 ? null : new DelimitedNamePath(() -> pathString.substring(0, lastDelimIndex), delimiter);
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
    public DelimitedNamePath join(Path<String> other) {
        if ( other == null ) return this;

        return new DelimitedNamePath(() ->
                Stream.concat(getElements().stream(), other.getElements().stream())
                      .collect(Collectors.joining(delimiter.get())),
                delimiter
        );
    }

    @Override
    public URI toUri() {
        return null;
    }

    public boolean equals(Object other) {
        if (other == this) return true;
        if ( !(other instanceof NamePath) ) return false;

        NamePath that = (NamePath)other;
        return Objects.equals(this.isAbsolute(), that.isAbsolute())
        && Objects.equals(this.getElements(), that.getElements());
    }

    public int hashCode() {
        return Objects.hash(isAbsolute(), getElements());
    }

    /**
     * Joins this name path into a single path string, with path elements delimited by the delimiter.
     * @return a path string of all elements, delimited by the delimiter of this instance.
     */
    public String join() {
        return NamePath.super.join(this.delimiter.get());
    }
}
