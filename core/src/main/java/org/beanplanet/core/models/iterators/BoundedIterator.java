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

package org.beanplanet.core.models.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A useful iterator that wraps another and limits the number of iterations to a
 * maximum, upper bound.
 *
 * @param <E> the element type
 */
public class BoundedIterator<E> implements Iterator<E> {
    /** The iterator this bounded iterator wraps. */
    protected Iterator<E> wrappedIterator;
    /** The index of the item this bounded iterator returns up to. */
    protected int maximumSize = Integer.MAX_VALUE;

    /** The current result idx. */
    private int currentResultIdx;

    /**
     * Instantiates a new bounded iterator.
     *
     * @param wrappedIterator the wrapped iterator
     */
    public BoundedIterator(Iterator<E> wrappedIterator) {
        this.wrappedIterator = wrappedIterator;
    }

    /**
     * Instantiates a new bounded iterator.
     *
     * @param wrappedIterator the wrapped iterator
     * @param maximumSize the maximum size
     */
    public BoundedIterator(Iterator<E> wrappedIterator, int maximumSize) {
        this.wrappedIterator = wrappedIterator;
        this.maximumSize = maximumSize;
    }

    public boolean hasNext() {
        return (wrappedIterator != null && currentResultIdx < maximumSize && wrappedIterator.hasNext());
    }

    public E next() {
        if (wrappedIterator == null) {
            throw new IllegalStateException("The wrapped iterator may not be null");
        }

        if (currentResultIdx == maximumSize) {
            throw new NoSuchElementException("The current maximum limit ["+maximumSize+"] of this iterator has already been reached");
        }

        currentResultIdx++;
        return wrappedIterator.next();
    }

    public void remove() {
        if (wrappedIterator == null) {
            throw new IllegalStateException("The wrapped iterator may not be null");
        }

        wrappedIterator.remove();
    }

    /**
     * Gets the index of the item this bounded iterator returns up to.
     *
     * @return the index of the item this bounded iterator returns up to
     */
    public int getMaximumSize() {
        return maximumSize;
    }

    /**
     * Sets the index of the item this bounded iterator returns up to.
     *
     * @param maximumSize
     *           the new index of the item this bounded iterator returns up to
     */
    public void setMaximumSize(int maximumSize) {
        this.maximumSize = maximumSize;
    }
}
