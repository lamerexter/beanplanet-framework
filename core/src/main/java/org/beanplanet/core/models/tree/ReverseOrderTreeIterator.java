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
