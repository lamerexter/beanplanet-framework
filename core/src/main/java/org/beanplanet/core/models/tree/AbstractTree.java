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

package org.beanplanet.core.models.tree;

import java.util.Objects;

/**
 * Maintains the root node of this tree model.
 */
public abstract class AbstractTree<T> implements Tree<T> {
    /** The root node of this tree model instance, */
    protected T root;

    /**
     * Constructs an abstract tree model with the given root node in this instance.
     *
     * @param root the known root of this tree model.
     */
    public AbstractTree(T root) {
        this.root = root;
    }

    /**
     * Returns the known root node of this tree model instance.
     *
     * @return the root node of this tree model.
     */
    @Override
    public T getRoot() {
        return root;
    }

    public boolean equals(Object other) {
        if (this == other) return true;

        if ( !(other instanceof Tree) ) return false;
        final Tree<?> that = (Tree<?>)other;

        return Objects.equals(this.getRoot(), that.getRoot());
    }

    public int hashCode() {
        return Objects.hash(getRoot());
    }

}
