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

import java.util.NoSuchElementException;

public class ChildIterator<T> implements TreeIterator<T> {
    private Tree<T> tree;
    private T parent;

    private boolean initialised;
    private int childIndex = -1;
    private T previousChild, currentChild, nextChild;

    public ChildIterator(Tree<T> tree) {
        this(tree, tree.getRoot());
    }

    public ChildIterator(Tree<T> tree, T parent) {
        this.tree = tree;
        this.parent = parent;
    }

    @Override
    public boolean hasNext() {
        checkInitialised();
        return nextChild != null;
    }

    private void checkInitialised() {
        if (initialised) return;

        previousChild = currentChild = null;
        nextChild = parent == null || tree.isLeaf(parent) ? null : tree.getChild(parent, 0);
        initialised = true;
    }

    @Override
    public T next() {
        checkInitialised();
        if (nextChild == null) {
            throw new NoSuchElementException("Call to next() when there are no more children of this tree node. Did you first call hasNext()?");
        }

        previousChild = currentChild;
        currentChild = nextChild;
        nextChild = tree.getNextSibling(currentChild);
        return currentChild;
    }

    @Override
    public boolean hasPrevious() {
        checkInitialised();
        return previousChild != null;
    }

    @Override
    public T previous() {
        checkInitialised();
        if (previousChild == null) {
            throw new NoSuchElementException("Call to previous() when there are no prior children of this tree node. Did you first call hasPrevious()?");
        }

        nextChild = currentChild;
        currentChild = previousChild;
        previousChild = tree.getPreviousSibling(currentChild);
        return currentChild;
    }

    @Override
    public void remove() {
        checkInitialised();
        tree.remove(parent, currentChild);
    }

    @Override
    public void set(T t) {
        checkInitialised();
        tree.set(parent, currentChild, t);
    }
}
