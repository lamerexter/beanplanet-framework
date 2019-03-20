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

package org.beanplanet.core.io;

import org.beanplanet.core.util.IterableUtil;

import java.net.URI;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static org.beanplanet.core.io.PathUtil.emptyPath;
import static org.beanplanet.core.util.CollectionUtil.isEmptyOrNull;

/**
 * Created by gary on 10/11/17.
 */
public interface Path<T> extends Iterable<Path<T>> {
    URI toUri();

    public final Path<?> EMPTY_PATH = new EmptyPath();

    boolean isAbsolute();

    default Path<T> toAbsolutePath() {
        return null;
    }

    T getRootElement();
    T getElement(int index);
    List<T> getElements();
    default Stream<T> elementsStream() {
        return getElements().stream();
    }

    Path<T> normalise();

    Path<T> getRoot();
    default Path<T> getPathElement(int index) {
        return getPathElements().get(index);
    }
    List<Path<T>> getPathElements();
    default Stream<Path<T>> pathElementsStream() {
        return getPathElements().stream();
    }

    String getName();
    default String getName(int index) {
        return getNameElements().get(index);
    }
    List<String> getNameElements();
    default Stream<String> nameElementsStream() {
        return getNameElements().stream();
    }
    default String getBasename() {
        return getName();
    }
    default String getDirname() {
        List<Path<T>> pathElements = getPathElements();
        StringBuilder s = new StringBuilder();
        if (getRoot() != null) s.append(getRoot().getName());
        for (int n=0; n < pathElements.size()-1; n++) {
            s.append(s.length() > 0 ? getNameSeparator() : "").append(pathElements.get(n).getName());
        }
        return s.toString();
    }

    String getNameSeparator();

    default Path<T> parentPath() {
        return isEmpty() ? emptyPath() : subPath(0, length()-1);
    }

    default boolean isEmpty() {
        return equals(emptyPath());
    }

    default int length() {
        return isEmptyOrNull(getPathElements()) ? 0 : getPathElements().size();
    }

    default Stream<Path<T>> stream() {
        return IterableUtil.asStream(this);
    }

    default Path<T> subPath(int fromIndexInclusive, int toIndexExclusive) {
        return null; //new FilePath(getPathElements().subList(fromIndexInclusive, toIndexExclusive));
    }

    default Path<T> getFirstElement() {
        return getPathElements().get(0);
    }

    default Path<T> getLastElement() {
        List<Path<T>> pathElements = getPathElements();
        return pathElements.get(pathElements.size()-1);
    }

    Path<T> relativeTo(Path<T> other);

    default Path<T> resolve(Path<T> other) {
        if (other == null || other.isEmpty()) return this;

        if (other.isAbsolute()) return other;

        return join(other);
    }

    Path<T> join(Path<T> other);

    default String toCanonicalPath() {
        List<Path<T>> pathElements = getPathElements();
        StringBuilder s = new StringBuilder();
        if (getRoot() != null) s.append(getRoot());
        for (int n=0; n < pathElements.size()-1; n++) {
            s.append(s.length() > 0 ? getNameSeparator() : "").append(pathElements.get(n).getName());
        }
        return s.toString();
    }

    final class EmptyPath<T> implements Path<T> {
        @Override
        public URI toUri() {
            return null;
        }

        @Override
        public boolean isAbsolute() {
            return false;
        }

        @Override
        public Path<T> normalise() {
            return null;
        }

        @Override
        public Path<T> getRoot() {
            return null;
        }

        @Override
        public Path<T> getPathElement(int index) {
            return null;
        }

        @Override
        public T getRootElement() {
            return null;
        }

        @Override
        public T getElement(int index) {
            return null;
        }

        @Override
        public List<T> getElements() {
            return null;
        }

        @Override
        public List<Path<T>> getPathElements() {
            return Collections.emptyList();
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public List<String> getNameElements() {
            return Collections.emptyList();
        }

        @Override
        public String getNameSeparator() {
            return "";
        }

        @Override
        public Path<T> join(Path<T> other) {
            return other;
        }

        @Override
        public Path<T> relativeTo(Path other) {
            return null;
        }

        @Override
        public Iterator<Path<T>> iterator() {
            return Collections.emptyIterator();
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;

            if ( !(obj instanceof Path) ) return false;

            return ((Path<T>)obj).getPathElements().isEmpty();
        }
    }
}
