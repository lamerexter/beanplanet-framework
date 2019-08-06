/*
 *  MIT Licence:
 *
 *  Copyright (C) 2019 Beanplanet Ltd
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
package org.beanplanet.core.collections;

import java.util.*;

/**
 * <p>Implementation of a double linked list, with previous and next pointers for each node. This is required aince the
 * Java standard {@link LinkedList} does not provide direct access to the nodes which comprise the list. At best then,
 * {@link LinkedList} takes O(n/2) iterations to find an element, whereas this implementation provides O(1) direct access
 * to nodes in the list (pros and cons to that - particularly for concurrency).</p>
 *
 * <p>This list requires synchronised/single-threaded access and protection from concurrent modification, owing to its
 * ability to dynamically find and detach nodes at runtime. Simlpy put, users of this list implementation must ensure
 * only one thread can access the list at one time or use <code>{@link Collections#synchronizedList(List)}</code> to provide
 * that guarantee.</p>
 *
 * @since 10th October, 2004
 */
public interface DoublyLinkedList<E> extends List<E>, Queue<E>, Deque<E> {
    boolean add(DoublyLinkedListNode<E> node);

    boolean addFirst(DoublyLinkedListNode<E> node);

    boolean addLast(DoublyLinkedListNode<E> node);

    boolean addToHead(DoublyLinkedListNode<E> node);

    boolean addToTail(DoublyLinkedListNode<E> node);

    DoublyLinkedListNode<E> addToHead(E object);

    DoublyLinkedListNode<E> addToTail(E object);

    /**
     * Retrieves, but does not remove, the head of the queue represented by
     * this deque (in other words, the first element of this deque), or
     * returns {@code null} if this deque is empty.
     *
     * <p>This method is equivalent to {@link #peekFirst()}.
     *
     * @return the head of the queue represented by this deque, or
     *         {@code null} if this deque is empty
     */
    default E peek() {
        return peekFirst();
    }

    E peekHead();

    E peekTail();

    /**
     * Retrieves, but does not remove, the first element of this deque,
     * or returns {@code null} if this deque is empty.
     *
     * @return the head of this deque, or {@code null} if this deque is empty
     */
    default E peekFirst() {
        return peekHead();
    }

    /**
     * Retrieves, but does not remove, the last element of this deque,
     * or returns {@code null} if this deque is empty.
     *
     * @return the tail of this deque, or {@code null} if this deque is empty
     */
    default E peekLast() {
        return peekTail();
    }

    /**
     * Retrieves and removes the head of the queue represented by this deque
     * (in other words, the first element of this deque), or returns
     * {@code null} if this deque is empty.
     *
     * <p>This method is equivalent to {@link #pollFirst()}.
     *
     * @return the first element of this deque, or {@code null} if
     *         this deque is empty
     */
    default E poll() {
        return pollFirst();
    }

    /**
     * Removes the specified node from this list.
     *
     * @param node the node to be removed from this list, if present.
     * @return <tt>true</tt> if this list contained the specified node and it was removed.
     * @throws NullPointerException          if the specified node is null.
     * @throws UnsupportedOperationException if the <tt>remove</tt> method is not supported by this list.
     */
    boolean remove(DoublyLinkedListNode<E> node);

    /**
     * Retrieves and removes the head of the list represented by this deque
     * (in other words, the first element of this list).
     * This method differs from {@link #poll poll} only in that it throws an
     * exception if this deque is empty.
     *
     * <p>This method is equivalent to {@link #removeFromHead()}.
     *
     * @return the head of the queue represented by this deque
     * @throws NoSuchElementException if this list is empty
     */
    default E remove() {
        return removeFromHead();
    }

    E removeFromHead();

    E removeFromTail();

    default E removeFirst() {
        return removeFromHead();
    }

    default E removeLast() {
        return removeFromTail();
    }

    /**
     * Returns the node at the specified position in this list.
     *
     * @param index the index of node to return.
     * @return the node at the specified position in this list.
     * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &gt;= size()).
     */
    DoublyLinkedListNode<E> getNode(int index);

    Iterator<E> iteratorHeadToTail();

    Iterator<E> iteratorTailToHead();

    @Override
    default public Iterator<E> descendingIterator() {
        return iteratorTailToHead();
    }

    default Iterator<DoublyLinkedListNode<E>> nodeIterator() {
        return nodeIteratorHeadToTail();
    }

    Iterator<DoublyLinkedListNode<E>> nodeIteratorHeadToTail();
    Iterator<DoublyLinkedListNode<E>> nodeIteratorTailToHead();

    DoublyLinkedListNode<E> set(int index, DoublyLinkedListNode<E> node);
}
