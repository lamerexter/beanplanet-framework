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

import java.util.List;
import java.util.NoSuchElementException;

/**
 * The child axis: the children of a node, in document order, with the cursor semantics of
 * {@link java.util.ListIterator}. The children are listed when the iterator is first used. {@link #remove()} and
 * {@link #set(Object)} change the tree through {@link Tree#remove(Object, Object)} and
 * {@link Tree#set(Object, Object, Object)}, for trees that support them.
 *
 * @param <T> the type of the tree's nodes.
 */
public class ChildIterator<T> implements TreeIterator<T> {
    private final Tree<T> tree;
    private final T parent;
    private List<T> children;
    /** The index of the child after the cursor. */
    private int cursor;
    /** The index of the child last returned, or -1. */
    private int lastReturned = -1;

    public ChildIterator(final Tree<T> tree) {
        this(tree, tree.getRoot());
    }

    public ChildIterator(final Tree<T> tree, final T parent) {
        this.tree = tree;
        this.parent = parent;
    }

    private List<T> children() {
        if (children == null) children = TreePath.childrenOf(tree, parent);
        return children;
    }

    @Override
    public boolean hasNext() {
        return cursor < children().size();
    }

    @Override
    public T next() {
        if (!hasNext()) throw new NoSuchElementException("Call to next() when there are no more children of this tree node. Did you first call hasNext()?");
        lastReturned = cursor++;
        return children.get(lastReturned);
    }

    @Override
    public boolean hasPrevious() {
        children();
        return cursor > 0;
    }

    @Override
    public T previous() {
        if (!hasPrevious()) throw new NoSuchElementException("Call to previous() when there are no prior children of this tree node. Did you first call hasPrevious()?");
        lastReturned = --cursor;
        return children.get(lastReturned);
    }

    @Override
    public void remove() {
        if (lastReturned < 0) throw new IllegalStateException("No child has been returned since the last change");
        if (tree.remove(parent, children.get(lastReturned))) {
            children = null;
            if (lastReturned < cursor) cursor--;
            lastReturned = -1;
        }
    }

    @Override
    public void set(final T replacement) {
        if (lastReturned < 0) throw new IllegalStateException("No child has been returned since the last change");
        if (tree.set(parent, children.get(lastReturned), replacement)) children = null;
    }
}
