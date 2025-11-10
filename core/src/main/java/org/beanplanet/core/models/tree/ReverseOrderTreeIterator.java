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

/**
 * Reverses the traversal order of iteration.  This iterator can be used wrap any other {@link TreeIterator} to reverse the
 * traversal order of the iterator, at any time.
 *
 * <p>For any call to {@link #hasNext()} or {@link #next()} this iterator calls
 * {@link TreeIterator#hasPrevious()} and {@link TreeIterator#previous()} respectfully and any call to
 * {@link #hasPrevious()} or {@link #previous()} ()} calls
 * {@link TreeIterator#hasNext()} ()} and {@link TreeIterator#next()} ()} respectfully.
 * </p>
 */
public class ReverseOrderTreeIterator<E> implements TreeIterator<E> {
    private TreeIterator<E> wrappedIterator;

    public ReverseOrderTreeIterator(TreeIterator<E> wrappedIterator) {
        this.wrappedIterator = wrappedIterator;

    }
    @Override
    public boolean hasPrevious() {
        return wrappedIterator.hasNext();
    }

    @Override
    public boolean hasNext() {
        return wrappedIterator.hasPrevious();
    }

    @Override
    public E previous() {
        return wrappedIterator.next();
    }

    @Override
    public E next() {
        return wrappedIterator.previous();
    }

    @Override
    public void remove() {
        wrappedIterator.remove();
    }

    @Override
    public void set(E replacement) {
        wrappedIterator.set(replacement);
    }
}
