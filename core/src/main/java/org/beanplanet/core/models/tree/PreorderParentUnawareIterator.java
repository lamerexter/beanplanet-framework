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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A pre-order depth-first tree iterator which does not rely on a node having any awareness or reference to its parent. This
 * implementation maintains a stack of parent state so that tree nodes don't have to.
 *
 * <p>As it is prohibitively expensive and complex to implement a bidirectional iterator without using parents, this
 * implementation is currently unidirectional (forward) only</p>
 */
class PreorderParentUnawareIterator<E> implements TreeIterator<E> {
    private final Tree<E> tree;

    private final Deque<E> nodeStack = new ArrayDeque<>();

    private final E fromNode;

    public PreorderParentUnawareIterator(Tree<E> tree) {
        this.tree = tree;
        this.fromNode = tree.getRoot();
        this.nodeStack.push(fromNode);
    }

    public Tree<E> getTree() {
        return tree;
    }

    public E getFromNode() {
        return fromNode;
    }

    public boolean hasNext() {
        return !nodeStack.isEmpty();
    }

    public boolean hasPrevious() {
       throw new UnsupportedOperationException("Reverse iteration over trees in this implementation is not supported.");
    }

    public E next() {
        if (!hasNext()) throw new NoSuchElementException("Exhausted tree iterator. Did you call hasNext() prior to calling next() ?");

        final E next = nodeStack.pop();
        List<E> nextChildren = tree.getChildren(next);
        Deque<E> nextChildrenDeque = new ArrayDeque<>(nextChildren.size());
        for (E child : nextChildren) {
            nextChildrenDeque.push(child);
        }

        while ( !nextChildrenDeque.isEmpty() ) {
            nodeStack.push(nextChildrenDeque.pop());
        }

        return next;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public E previous() {
        throw new UnsupportedOperationException("Reverse iteration over trees in this implementation is not supported.");
    }

    public void set(E replacement) {
        throw new UnsupportedOperationException();
    }
}
