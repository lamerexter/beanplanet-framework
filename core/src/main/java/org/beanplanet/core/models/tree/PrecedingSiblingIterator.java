package org.beanplanet.core.models.tree;

import java.util.Iterator;

public class PrecedingSiblingIterator<T> implements Iterator<T> {
    private final Tree<T> tree;
    private final T from;

    private final T parent;
    private int currentSiblingIdx;

    public PrecedingSiblingIterator(final Tree<T> tree, final T from) {
            this.tree = tree;
            this.from = from;
        parent = tree.getParent(from);
        if (parent == null) {
            currentSiblingIdx = -1;
        }
        else {
            currentSiblingIdx = 0;
        }
    }

    public boolean hasNext() {
        if (parent == null) {
            return false;
        }
        return currentSiblingIdx >= 0 && currentSiblingIdx < tree.getIndexOfChild(parent, from);
    }

    public T next() {
        return tree.getChild(parent, currentSiblingIdx++);
    }

    public void remove() {
        throw new java.lang.UnsupportedOperationException("remove() on " + getClass().getName());
    }
}
