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
