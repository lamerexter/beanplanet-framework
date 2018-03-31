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
