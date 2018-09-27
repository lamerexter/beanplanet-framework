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

package org.beanplanet.core.models;

import org.beanplanet.core.util.IterableUtil;

import java.util.List;
import java.util.stream.Stream;

import static org.beanplanet.core.util.CollectionUtil.isEmptyOrNull;

public interface Path<T> extends Iterable<Path<T>> {
    Path<?> EMPTY_PATH = new FilePath("");

    @SuppressWarnings("unchecked")
    default Path<T> emptyPath() {
        return (Path<T>)EMPTY_PATH;
    }

    boolean isAbsolute();

    Path<T> toAbsolutePath();

    Path<T> normalise();

    T getRoot();

    List<Path<T>> getPathElements();

    default Path<T> parentPath() {
        return isEmptyOrNull(getPathElements()) ? emptyPath() : subPath(0, size()-1);
    }

    default boolean isEmpty() {
        return isEmptyOrNull(getPathElements());
    }

    default int size() {
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
}
