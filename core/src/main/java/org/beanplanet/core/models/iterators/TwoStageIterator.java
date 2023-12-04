package org.beanplanet.core.models.iterators;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Function;

public class TwoStageIterator<E> implements Iterator<E> {
    protected Iterator<E> initialIterator;

    protected Function<E, Iterator<E>> provider;

    private Iterator<E> subIterator;

    private E currentItem;

    private boolean hasNext;

    protected LinkedList<Iterator<E>> iterators;

    public TwoStageIterator(Iterator<E> initialIterator, Function<E, Iterator<E>> provider) {
        this.initialIterator = initialIterator;
        this.provider = provider;
        this.currentItem = getNextItem();
    }

    public boolean hasNext() {
        return hasNext;
    }

    public E next() {
        E nextItem = currentItem;
        currentItem = getNextItem();
        return nextItem;
    }

    private E getNextItem() {
        if (subIterator != null && subIterator.hasNext()) {
            return subIterator.next();
        }

        while ((subIterator == null || !subIterator.hasNext()) && initialIterator.hasNext()) {
            subIterator = provider.apply(initialIterator.next());
        }

        hasNext = subIterator != null && subIterator.hasNext();
        return (hasNext ? subIterator.next() : null);
    }

    public void remove() {
        throw new java.lang.UnsupportedOperationException("remove() on " + getClass().getName());
    }

}
