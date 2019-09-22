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

import java.util.Objects;

/**
 * Implementation of a node for a doubly linked list implementation.
 *
 * @author Gary Watson
 */
public class DoublyLinkedListNode<E> {
    protected DoublyLinkedListNode<E> previousNode;

    protected DoublyLinkedListNode<E> nextNode;

    protected E value;

    public DoublyLinkedListNode() {
    }

    public DoublyLinkedListNode(E value) {
        setValue(value);
    }

    public DoublyLinkedListNode(E value, DoublyLinkedListNode<E> previousNode, DoublyLinkedListNode<E> nextNode) {
        this(value);
        setPreviousNode(previousNode);
        setNextNode(nextNode);
    }

    public DoublyLinkedListNode<E> getPreviousNode() {
        return previousNode;
    }

    public void setPreviousNode(DoublyLinkedListNode<E> previousNode) {
        this.previousNode = previousNode;
    }

    public DoublyLinkedListNode<E> getNextNode() {
        return nextNode;
    }

    public void setNextNode(DoublyLinkedListNode<E> nextNode) {
        this.nextNode = nextNode;
    }

    public E getValue() {
        return value;
    }

    public void setValue(E value) {
        this.value = value;
    }

    public boolean equals(Object other) {
        return Objects.equals(this.getValue(), other);
    }

    public int hashCode() {
        return Objects.hashCode(getValue());
    }
}
