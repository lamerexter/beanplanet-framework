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
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A lazy, bidirectional {@link TreeIterator} over a sequence of nodes, with the cursor semantics of
 * {@link java.util.ListIterator}: the cursor lies between two nodes; {@link #next()} returns the node after it and
 * moves it forwards, {@link #previous()} the node before it and moves it backwards, so {@code next()} followed by
 * {@code previous()} returns the same node twice. A new iterator's cursor is before its first node.
 *
 * <p>Subclasses provide the sequence as a current node and single steps: {@link #first()} positions on the first node,
 * {@link #forward()} and {@link #backward()} move one node either way, each returning false, and leaving the position as
 * it was, when there is no node that way. Nothing is computed until the iterator is first used, and then only one step
 * at a time.</p>
 *
 * <p>When {@code boundedAtFirst}, the iterator never moves before its first node, so {@link #backward()} is only asked
 * to retrace steps already taken forwards (axes); otherwise, the sequence may extend before the first node
 * (traversals positioned at a node of a whole tree).</p>
 *
 * @param <E> the type of the tree's nodes.
 */
abstract class CursorTreeIterator<E> implements TreeIterator<E> {
    private final boolean boundedAtFirst;
    private boolean started;
    private boolean empty;
    /** Whether the cursor is before the current node (true) or after it (false). */
    private boolean beforeCurrent = true;
    /** The current node's place in the sequence, counted from the first node. */
    private long position;

    protected CursorTreeIterator(final boolean boundedAtFirst) {
        this.boundedAtFirst = boundedAtFirst;
    }

    /** Positions on the first node, returning false if there is none. */
    protected abstract boolean first();

    /** The node at the current position. */
    protected abstract E current();

    /** Moves to the next node, returning false (and not moving) if there is none. */
    protected abstract boolean forward();

    /** Moves to the previous node, returning false (and not moving) if there is none. */
    protected abstract boolean backward();

    /**
     * The nodes after the cursor, as a lazy ordered stream. A new iterator's stream steps the sequence directly, one
     * {@link #forward()} per node (the stream then owns the iterator, as for any stream made from an iterator).
     */
    @Override
    public Stream<E> stream() {
        if (started) return TreeIterator.super.stream();
        started = true;
        return StreamSupport.stream(new Spliterators.AbstractSpliterator<E>(Long.MAX_VALUE, Spliterator.ORDERED) {
            private boolean positioned;

            @Override
            public boolean tryAdvance(final Consumer<? super E> action) {
                if (!positioned) {
                    positioned = true;
                    if (!first()) {
                        empty = true;
                        return false;
                    }
                } else if (empty || !stepForward()) {
                    return false;
                }
                beforeCurrent = false;
                action.accept(current());
                return true;
            }

            @Override
            public void forEachRemaining(final Consumer<? super E> action) {
                while (tryAdvance(action)) { }
            }
        }, false);
    }

    private boolean isEmpty() {
        if (!started) {
            started = true;
            empty = !first();
        }
        return empty;
    }

    private boolean stepForward() {
        if (!forward()) return false;
        position++;
        return true;
    }

    private boolean stepBackward() {
        if (boundedAtFirst && position == 0) return false;
        if (!backward()) return false;
        position--;
        return true;
    }

    @Override
    public boolean hasNext() {
        if (isEmpty()) return false;
        if (beforeCurrent) return true;
        if (!stepForward()) return false;
        beforeCurrent = true;
        return true;
    }

    @Override
    public E next() {
        if (isEmpty()) throw new NoSuchElementException("There are no tree nodes in this iteration");
        if (beforeCurrent) {
            beforeCurrent = false;
            return current();
        }
        if (stepForward()) return current();
        throw new NoSuchElementException("Call to next() when there are no more tree nodes. Did you first call hasNext()?");
    }

    @Override
    public boolean hasPrevious() {
        if (isEmpty()) return false;
        if (!beforeCurrent) return true;
        if (!stepBackward()) return false;
        beforeCurrent = false;
        return true;
    }

    @Override
    public E previous() {
        if (isEmpty()) throw new NoSuchElementException("There are no tree nodes in this iteration");
        if (!beforeCurrent) {
            beforeCurrent = true;
            return current();
        }
        if (stepBackward()) return current();
        throw new NoSuchElementException("Call to previous() when there are no prior tree nodes. Did you first call hasPrevious()?");
    }
}
