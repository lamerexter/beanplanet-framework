/*
 *  MIT Licence:
 *
 *  Copyright (C) 2019 Beanplanet Ltd
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

import org.beanplanet.core.util.Streamable;

import java.net.URI;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Represents a simple path of element of type T.
 */
public interface Path<T> extends Iterable<T>, Streamable<T> {
    Path<?> EMPTY_PATH = new Path<Object>() {
        @Override
        public Iterator<Object> iterator() {
            return Collections.emptyIterator();
        }

        @Override
        public boolean isAbsolute() {
            return false;
        }

        @Override
        public Path<Object> toAbsolutePath() throws IllegalStateException {
            return this;
        }

        @Override
        public Path<Object> normalise() {
            return this;
        }

        @Override
        public Object getRoot() {
            return null;
        }

        @Override
        public Path<Object> relativeTo(Path<Object> other) {
            return this;
        }

        @Override
        public Path<Object> resolve(Path<Object> other) {
            return this;
        }

        @Override
        public Path<Object> join(Path<Object> other) {
            return this;
        }

        @Override
        public URI toUri() {
            return null;
        }

        /**
         * Returns the elements which comprise the path.
         *
         * @return a lit of path elemments which may be empty but never null.
         */
        @Override
        public List<Object> getElements() {
            return Collections.emptyList();
        }

        @Override
        public Object getElement(int n) {
            throw new IndexOutOfBoundsException();
        }

        @Override
        public Path<Object> parentPath() {
            return null;
        }
    };

    /**
     * Returns the empty path.
     *
     * @return an empty path.
     */
    @SuppressWarnings("unchecked")
    default Path<T> emptyPath() {
        return (Path<T>)EMPTY_PATH;
    }

    /**
     * Returns the element denoted by this path. Usually, this is the target or leaf element of the path. This default
     * implementation returned the 'last' or leaf element of the path.
     *
     * @return the element associated with this path, usually the target or leaf element of the path.
     * @see #getLastElement()
     */
    default T getElement() {
        return getLastElement();
    }

    /**
     * Returns all of the path elements.
     *
     * @return the elements of this path, which may be empty but never null.
     */
    List<T> getElements();

    /**
     * Gets the path element at the given index.
     *
     * @return the path element at the given index.
     */
    default T getElement(int n) {
        return getElements().get(n);
    }

    /**
     * Returns the parent path of this path, if this path has a parent. This defailt implementation
     * returns the sub-path from the root to the parent of this path.
     *
     * @return the parent path, or null if this path has no parent.
     */
    Path<T> parentPath();

    /**
     * Whether this path is empty. This default implementation is based on whether there exists any
     * path elements.
     *
     * @return true if this path is empty, false otherwise.
     */
    default boolean isEmpty() {
        return getElements().isEmpty();
    }

    /**
     * The length of the path. This default implemention is based of the number of path elements, as
     * determined by a call to <code>{@link #getElements()}.size()</code>.
     *
     * @return the number of path elements, which may be zero if this path is empty.
     */
    default int length() {
        return getElements().size();
    }

    /**
     * Returns a stream of the elements of this path.
     *
     * @return a stream of the elements which comprise this path.
     */
    default Stream<T> stream() {
        return getElements().stream();
    }

    /**
     * Gets the first element of this path. This default implementation simply returns the zeroth element.
     *
     * @return the first element of the path.
     */
    default T getFirstElement() {
        return getElement(0);
    }

    /**
     * Gets the last element of this path. This default implementation simply returns the element at index <code>size-1</code>.
     *
     * @return the first element of the path.
     */
    default T getLastElement() {
        return getElement(length()-1);
    }

    /**
     * Returns an iterator over the elements in this path. This impleementation simply
     * returns an iterator over the elements returned from a call to {@link #getElements()}.
     *
     * @return an iterator over the elements of the path.
     */
    default Iterator<T> iterator() {
        return getElements().iterator();
    }

    /**
     * Whether this path is absolute. An absolute path is one that has a root and can be navigated to
     * from the root. Note that the root is generally <b>not</b> an element on the path itself, may be
     * an element on a parent path. However, this is implementation dependent.
     *
     * @return true is this path is absolute, false otherwise.
     */
    boolean isAbsolute();

    /**
     * Returns the absolute path of this path. This path must have a root and be navigable from the root.
     *
     * @return the absoolute path.
     * @throws IllegalStateException if the absolute path
     */
    Path<T> toAbsolutePath() throws IllegalStateException;

    /**
     * Normailises this path by removing redundant elenents (for example <code>./a/b/../c</code> might
     * yield <code>a/b</code>).
     *
     * @return this path normalised, or simply this path if normalisation would yield the same.
     */
    Path<T> normalise();

    /**
     * Returns the root of this path, if the path is rooted. Resource paths should extend {@link RootedPath} to
     * indicate they are root based.
     *
     * @return the root of this path, or null if the path does not have a root.
     */
    Object getRoot();

    /**
     * Whether this path is rooted.
     *
     * @return true if thus path has a root, false otherwise.
     */
    default boolean hasRoot() {
        return getRoot() != null;
    }

    /**
     * <p>
     * Constructs a relative path between this path and a given path.
     * </p>
     *
     * <p>
     * <i>Relative path construction</i> is the inverse of <i>resolution</i>. This method attempts to construct a
     * relative path that when resolved against this path, yields a path that locates the same file as the given path.
     * For example, on UNIX, if this path is "/a/b" and the given path is "/a/b/c/d" then the resulting relative path
     * would be "c/d". When this path and the given path do not have a root, then a relative path can be constructed. A
     * relative path cannot be constructed if only one of the paths have a root component. Where both paths have a root
     * component then it is implementation dependent if a relative path can be constructed. If this path and the given
     * path are equal then an empty path is returned.
     *
     * <p>
     * For any two normalized paths p and q, where q does not have a root component,
     * p.relativeTo(p.resolve(q)).equals(q)
     * </p>
     *
     * @param other the other path which this path is to resolved relative to.
     *
     * @return the path
     */
    Path<T> relativeTo(Path<T> other) throws IllegalArgumentException;

    /**
     * <p>
     * Resolves the given path against this path.
     * </p>
     *
     * <p>
     * If other is an empty path then this method trivially returns this path.
     * Otherwise this method considers this path to be a directory and resolves the given path against this path.
     * In the simplest case, the given path does not have a root component, in which case this method joins the
     * given path to this path and returns a resulting path that ends with the given path. Where the given path has a
     * root component then resolution is highly implementation dependent and therefore unspecified.
     * </p>
     *
     * <p>
     * Resolve the given path against this resource.
     *</p>
     *
     * <p>
     * If the other path is an {@link Path#isAbsolute() absolute} path then this method simply returns the other. If
     * the other path is an <i>empty path</i> then this method simply returns this path. Otherwise this method considers
     * this path to be a directory and resolves the given path against this path. In the simplest case, the given path
     * does not have a {@link Path#getRoot root} component, in which case this method <em>joins</em> the given path to
     * this path  and returns a resulting path that {@link String#endsWith(String)} the given path. Where the given path has
     * a root component then resolution is highly implementation dependent andtherefore unspecified.
     * </p>
     *
     * @param other the path to be resolved against this resource.
     * @return a path of the given resource resolved against this path.
     * @throws IllegalArgumentException if the given path cannot be resolved against this path.
     */
    Path<T> resolve(Path<T> other);

    /**
     * <p>
     * Joins the given path to this path.
     * </p>
     *
     * <p>
     * This method considers this path to be a directory and joins the given path directly onto this path.
     * In the simplest case, the given path does not have a root component, in which case this method joins the
     * given path to this path and returns a resulting path that ends with the given path. Where the given path has a
     * root component then resolution is highly implementation dependent and therefore unspecified.
     * </p>
     *
     * @param other the path to be joined to this path.
     * @return a path, consisting of this path, ending with the given path.
     * @throws UnsupportedOperationException if this path cannot be joined with the given path.
     */
    Path<T> join(Path<T> other);


    URI toUri();
}
