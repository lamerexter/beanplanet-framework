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
