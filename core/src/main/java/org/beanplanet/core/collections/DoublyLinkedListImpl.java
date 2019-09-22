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

import org.beanplanet.core.util.StringUtil;

import java.lang.reflect.Array;
import java.util.*;

import static org.beanplanet.core.util.StringUtil.asDelimitedString;

/**
 * Implementation of a doubly linked list. Although slightly more demanding on memory than a sngly linked list (one
 * extra pointer) this imlementation allows for both forward and reverse traversal of the list.
 *
 * @author Gary Watson
 */
public class DoublyLinkedListImpl<E> implements DoublyLinkedList<E> {
    private DoublyLinkedListNode<E> head;

    private DoublyLinkedListNode<E> tail;

    private int size;

    public DoublyLinkedListImpl() {}

    public DoublyLinkedListImpl(Collection<? extends E> other) {
        addAllInternal(0, other);
    }

    /**
     * Returns the number of elements in this list. If this list contains more than <tt>Integer.MAX_VALUE</tt> elements,
     * returns <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of elements in this list.
     */
    public int size() {
        return size;
    }

    /**
     * Returns <tt>true</tt> if this list contains no elements.
     *
     * @return <tt>true</tt> if this list contains no elements.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns <tt>true</tt> if this list contains the specified element. More formally, returns <tt>true</tt> if and
     * only if this list contains at least one element <tt>e</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
     *
     * @param o element whose presence in this list is to be tested.
     * @return <tt>true</tt> if this list contains the specified element.
     * @throws ClassCastException   if the type of the specified element is incompatible with this list (optional).
     * @throws NullPointerException if the specified element is null and this list does not support null elements
     *                              (optional).
     */
    public boolean contains(Object o) {
        DoublyLinkedListNode<E> currentNode = head;

        while (currentNode != null) {
            if ( Objects.equals(currentNode, o) ) return true;
            currentNode = currentNode.getNextNode();
        }

        return false;
    }

    /**
     * Returns an iterator over the elements in this list in proper sequence.
     *
     * @return an iterator over the elements in this list in proper sequence.
     */
    public ListIterator<E> iterator() {
        return listIterator();
    }

    @Override
    public ListIterator<DoublyLinkedListNode<E>> nodeIteratorHeadToTail() {
        return new DoublyLinkedListNodeIterator(true);
    }

    @Override
    public ListIterator<DoublyLinkedListNode<E>> nodeIteratorTailToHead() {
        return new DoublyLinkedListNodeIterator(false);
    }

    /**
     * Returns an array containing all of the elements in this list in proper sequence. Obeys the general contract of the
     * <tt>Collection.toArray</tt> method.
     *
     * @return an array containing all of the elements in this list in proper sequence.
     * @see java.util.Arrays#asList(Object[])
     */
    public Object[] toArray() {
        return toArray(new Object[size]);
    }

    /**
     * Returns an array containing all of the elements in this list in proper sequence; the runtime type of the returned
     * array is that of the specified array. Obeys the general contract of the <tt>Collection.toArray(Object[])</tt>
     * method.
     *
     * @param a the array into which the elements of this list are to be stored, if it is big enough; otherwise, a new
     *          array of the same runtime type is allocated for this purpose.
     * @return an array containing the elements of this list.
     * @throws ArrayStoreException  if the runtime type of the specified array is not a supertype of the runtime type of
     *                              every element in this list.
     * @throws NullPointerException if the specified array is <tt>null</tt>.
     */
    @SuppressWarnings("unchecked")
    public E[] toArray(Object a[]) {
        Object values[] = a.length == size ? a : (Object[]) Array.newInstance(a.getClass().getComponentType(), size);

        DoublyLinkedListNode<E> currentNode = head;

        for (int n = 0; currentNode != null; n++) {
            values[n] = currentNode.getValue();
            currentNode = currentNode.getNextNode();
        }

        return (E[]) values;
    }

    /**
     * Appends the specified element to the end of this list (optional operation).
     * <p>
     * <p/>
     * Lists that support this operation may place limitations on what elements may be added to this list. In particular,
     * some lists will refuse to add null elements, and others will impose restrictions on the type of elements that may
     * be added. List classes should clearly specify in their documentation any restrictions on what elements may be
     * added.
     *
     * @param o element to be appended to this list.
     * @return <tt>true</tt> (as per the general contract of the <tt>Collection.add</tt> method).
     * @throws UnsupportedOperationException if the <tt>add</tt> method is not supported by this list.
     * @throws ClassCastException            if the class of the specified element prevents it from being added to this list.
     * @throws NullPointerException          if the specified element is null and this list does not support null elements.
     * @throws IllegalArgumentException      if some aspect of this element prevents it from being added to this list.
     */
    public boolean add(E o) {
        return addLast(new DoublyLinkedListNode<E>(o));
    }

    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E element() {
        return null;
    }

    @Override
    public void push(E e) {

    }

    @Override
    public E pop() {
        return null;
    }

    /**
     * Adds the specified element into the list after the specified element's node.
     * <p>
     * <p/>
     *
     * @param node the node before which the element is to be added in the list
     * @param o    element to be appended to this list.
     * @return <tt>true</tt> (as per the general contract of the <tt>Collection.add</tt> method).
     * @throws UnsupportedOperationException if the <tt>add</tt> method is not supported by this list.
     * @throws ClassCastException            if the class of the specified element prevents it from being added to this list.
     * @throws NullPointerException          if the specified node or element are null and this list does not support null
     *                                       elements.
     * @throws IllegalArgumentException      if some aspect of this element prevents it from being added to this list.
     */
    public boolean addBefore(DoublyLinkedListNode<E> node, E o) {
        return addBefore(node, new DoublyLinkedListNode<>(o));
    }

    public boolean addBefore(DoublyLinkedListNode<E> node, DoublyLinkedListNode<E> o) {
        if (node == head) {
            return addFirst(o);
        } else {
            o.setPreviousNode(node.getPreviousNode());
            o.setNextNode(node);

            node.getPreviousNode().setNextNode(o);
            node.setPreviousNode(o);
            size++;
            return true;
        }
    }

    /**
     * Adds the specified element into the list after the specified element's node.
     * <p>
     * <p/>
     *
     * @param node the node after which the element is to be added in the list
     * @param o    element to be appended to this list.
     * @return <tt>true</tt> (as per the general contract of the <tt>Collection.add</tt> method).
     * @throws UnsupportedOperationException if the <tt>add</tt> method is not supported by this list.
     * @throws ClassCastException            if the class of the specified element prevents it from being added to this list.
     * @throws NullPointerException          if the specified node or element are null and this list does not support null
     *                                       elements.
     * @throws IllegalArgumentException      if some aspect of this element prevents it from being added to this list.
     */
    public boolean addAfter(DoublyLinkedListNode<E> node, E o) {
       return addAfter(node, new DoublyLinkedListNode<>(o));
    }

    public boolean addAfter(DoublyLinkedListNode<E> node, DoublyLinkedListNode<E> o) {
        if (node == tail) {
            return addLast(o);
        } else {
            o.setPreviousNode(node);
            o.setNextNode(node.getNextNode());

            node.getNextNode().setPreviousNode(o);
            node.setNextNode(o);
            size++;
            return true;
        }
    }

    /**
     * Removes the first occurrence in this list of the specified element (optional operation). If this list does not
     * contain the element, it is unchanged. More formally, removes the element with the lowest index i such that
     * <tt>(o==null ? get(i)==null : o.equals(get(i)))</tt> (if such an element exists).
     *
     * @param o element to be removed from this list, if present.
     * @return <tt>true</tt> if this list contained the specified element.
     * @throws ClassCastException            if the type of the specified element is incompatible with this list (optional).
     * @throws NullPointerException          if the specified element is null and this list does not support null elements
     *                                       (optional).
     * @throws UnsupportedOperationException if the <tt>remove</tt> method is not supported by this list.
     */
    public boolean remove(Object o) {
        boolean found = false;
        DoublyLinkedListNode<E> currentNode = head;

        while (currentNode != null && !found) {
            Object nodeValue = currentNode.getValue();
            found = (o == null && nodeValue == null) || (o != null && o.equals(nodeValue)) || (nodeValue != null && nodeValue.equals(o));
            if (found) {
                remove(currentNode);
            } else {
                currentNode = currentNode.getNextNode();
            }
        }

        return found;
    }

    /**
     * Removes the specified node from this list.
     *
     * @param node the node to be removed from this list, if present.
     * @return <tt>true</tt> if this list contained the specified node and it was removed.
     * @throws NullPointerException          if the specified node is null.
     * @throws UnsupportedOperationException if the <tt>remove</tt> method is not supported by this list.
     */
    public boolean remove(DoublyLinkedListNode<E> node) {
        if (node.getPreviousNode() != null) {
            node.getPreviousNode().setNextNode(node.getNextNode());
        }
        if (node.getNextNode() != null) {
            node.getNextNode().setPreviousNode(node.getPreviousNode());
        }
        if (head == node) {
            head = node.getNextNode();
        }
        if (tail == node) {
            tail = node.getPreviousNode();
        }
        size--;

        return true;
    }

    /**
     * Returns <tt>true</tt> if this list contains all of the elements of the specified collection.
     *
     * @param c collection to be checked for containment in this list.
     * @return <tt>true</tt> if this list contains all of the elements of the specified collection.
     * @throws ClassCastException   if the types of one or more elements in the specified collection are incompatible with
     *                              this list (optional).
     * @throws NullPointerException if the specified collection contains one or more null elements and this list does not
     *                              support null elements (optional).
     * @throws NullPointerException if the specified collection is <tt>null</tt>.
     * @see #contains(Object)
     */
    public boolean containsAll(Collection<?> c) {
        boolean containsAll = true;
        for (Iterator<?> iter = c.iterator(); iter.hasNext() & containsAll; ) {
            containsAll = contains(iter.next());
        }

        return containsAll;
    }

    /**
     * Appends all of the elements in the specified collection to the end of this list, in the order that they are
     * returned by the specified collection's iterator (optional operation). The behavior of this operation is
     * unspecified if the specified collection is modified while the operation is in progress. (Note that this will occur
     * if the specified collection is this list, and it's nonempty.)
     *
     * @param c collection whose elements are to be added to this list.
     * @return <tt>true</tt> if this list changed as a result of the call.
     * @throws UnsupportedOperationException if the <tt>addAll</tt> method is not supported by this list.
     * @throws ClassCastException            if the class of an element in the specified collection prevents it from being added to
     *                                       this list.
     * @throws NullPointerException          if the specified collection contains one or more null elements and this list does not
     *                                       support null elements, or if the specified collection is <tt>null</tt>.
     * @throws IllegalArgumentException      if some aspect of an element in the specified collection prevents it from being
     *                                       added to this list.
     * @see #add(Object)
     */
    public boolean addAll(Collection<? extends E> c) {
        return addAll(size, c);
    }

    /**
     * Inserts all of the elements in the specified collection into this list at the specified position (optional
     * operation). Shifts the element currently at that position (if any) and any subsequent elements to the right
     * (increases their indices). The new elements will appear in this list in the order that they are returned by the
     * specified collection's iterator. The behavior of this operation is unspecified if the specified collection is
     * modified while the operation is in progress. (Note that this will occur if the specified collection is this list,
     * and it's nonempty.)
     *
     * @param index index at which to insert first element from the specified collection.
     * @param c     elements to be inserted into this list.
     * @return <tt>true</tt> if this list changed as a result of the call.
     * @throws UnsupportedOperationException if the <tt>addAll</tt> method is not supported by this list.
     * @throws ClassCastException            if the class of one of elements of the specified collection prevents it from being
     *                                       added to this list.
     * @throws NullPointerException          if the specified collection contains one or more null elements and this list does not
     *                                       support null elements, or if the specified collection is <tt>null</tt>.
     * @throws IllegalArgumentException      if some aspect of one of elements of the specified collection prevents it from
     *                                       being added to this list.
     * @throws IndexOutOfBoundsException     if the index is out of range (index &lt; 0 || index &gt; size()).
     */
    public boolean addAll(int index, Collection<? extends E> c) {
        return addAllInternal(index, c);
    }

    private final boolean addAllInternal(int index, Collection<? extends E> c) {
        for (Iterator<? extends E> iter = c.iterator(); iter.hasNext(); ) {
            add(index++, iter.next());
        }

        return true;
    }

    /**
     * Removes from this list all the elements that are contained in the specified collection (optional operation).
     *
     * @param c collection that defines which elements will be removed from this list.
     * @return <tt>true</tt> if this list changed as a result of the call.
     * @throws UnsupportedOperationException if the <tt>removeAll</tt> method is not supported by this list.
     * @throws ClassCastException            if the types of one or more elements in this list are incompatible with the specified
     *                                       collection (optional).
     * @throws NullPointerException          if this list contains one or more null elements and the specified collection does not
     *                                       support null elements (optional).
     * @throws NullPointerException          if the specified collection is <tt>null</tt>.
     * @see #remove(Object)
     * @see #contains(Object)
     */
    public boolean removeAll(Collection<?> c) {
        boolean removedAll = !c.isEmpty();
        DoublyLinkedListNode<E> currentNode = head;

        while (currentNode != null) {
            E nodeValue = currentNode.getValue();
            if (c.contains(nodeValue)) {
                remove(currentNode);
            } else {
                removedAll = false;
            }
            currentNode = currentNode.getNextNode();
        }

        return removedAll;
    }

    /**
     * Retains only the elements in this list that are contained in the specified collection (optional operation). In
     * other words, removes from this list all the elements that are not contained in the specified collection.
     *
     * @param c collection that defines which elements this set will retain.
     * @return <tt>true</tt> if this list changed as a result of the call.
     * @throws UnsupportedOperationException if the <tt>retainAll</tt> method is not supported by this list.
     * @throws ClassCastException            if the types of one or more elements in this list are incompatible with the specified
     *                                       collection (optional).
     * @throws NullPointerException          if this list contains one or more null elements and the specified collection does not
     *                                       support null elements (optional).
     * @throws NullPointerException          if the specified collection is <tt>null</tt>.
     * @see #remove(Object)
     * @see #contains(Object)
     */
    public boolean retainAll(Collection<?> c) {
        boolean listChanged = false;
        DoublyLinkedListNode<E> currentNode = head;

        while (currentNode != null) {
            Object nodeValue = currentNode.getValue();
            if (!c.contains(nodeValue)) {
                remove(currentNode);
                listChanged = true;
            }
            currentNode = currentNode.getNextNode();
        }

        return listChanged;
    }

    /**
     * Removes all of the elements from this list (optional operation). This list will be empty after this call returns
     * (unless it throws an exception).
     *
     * @throws UnsupportedOperationException if the <tt>clear</tt> method is not supported by this list.
     */
    public void clear() {
        // Although we would love to just simply abandon the head and tail and set
        // the size to zero we
        // can't incase this list is backing another. If that is the case, we need
        // to ensure an operation
        // such as:
        //
        // subList(from, to).clear()
        //
        // actually clears the relevant range of items from the backing list.
        DoublyLinkedListNode<E> currentNode = head;
        DoublyLinkedListNode<E> nextNode = null;
        while (currentNode != null) {
            nextNode = currentNode.nextNode;
            remove(currentNode);
            currentNode = nextNode;
        }

        // Having cleared our items, we can clean-up now.
        head = tail = null;
        size = 0;
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of element to return.
     * @return the element at the specified position in this list.
     * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &gt;= size()).
     */
    public E get(int index) {
        return getNode(index).getValue();
    }

    /**
     * Returns the node at the specified position in this list.
     *
     * @param index the index of node to return.
     * @return the node at the specified position in this list.
     * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &gt;= size()).
     */
    public DoublyLinkedListNode<E> getNode(int index) {
        checkBounds(index, 0, size - 1);
        DoublyLinkedListNode<E> currentNode;

        if (index <= size / 2) {
            currentNode = head;
            for (int n = 0; n < index; n++) {
                currentNode = currentNode.getNextNode();
            }
        } else {
            currentNode = tail;
            for (int n = size - 1; n > index; n--) {
                currentNode = currentNode.getPreviousNode();
            }
        }

        return currentNode;
    }

    @Override
    public Iterator<E> iteratorHeadToTail() {
        return null;
    }

    @Override
    public Iterator<E> iteratorTailToHead() {
        return null;
    }

    /**
     * Replaces the element at the specified position in this list with the specified element (optional operation).
     *
     * @param index   index of element to replace.
     * @param element element to be stored at the specified position.
     * @return the element previously at the specified position.
     * @throws UnsupportedOperationException if the <tt>set</tt> method is not supported by this list.
     * @throws ClassCastException            if the class of the specified element prevents it from being added to this list.
     * @throws NullPointerException          if the specified element is null and this list does not support null elements.
     * @throws IllegalArgumentException      if some aspect of the specified element prevents it from being added to this
     *                                       list.
     * @throws IndexOutOfBoundsException     if the index is out of range (index &lt; 0 || index &gt;= size()).
     */
    public E set(int index, E element) {
        DoublyLinkedListNode<E> node = getNode(index);
        E previousValue = node.getValue();
        node.setValue(element);
        return previousValue;
    }

    public DoublyLinkedListNode<E> set(int index, DoublyLinkedListNode<E> node) {
        DoublyLinkedListNode<E> currentNode = null;
        if (index == 0) {
            currentNode = head;
            addToHead(node);
        } else if (index == size) {
            currentNode = tail;
            addToTail(node);
        } else {
            DoublyLinkedListNode<E> previousNode = getNode(index - 1);
            currentNode = previousNode.getNextNode();
            node.setPreviousNode(previousNode);
            node.setNextNode(currentNode.getNextNode());
        }

        return currentNode;
    }

    /**
     * Inserts the specified element at the specified position in this list (optional operation). Shifts the element
     * currently at that position (if any) and any subsequent elements to the right (adds one to their indices).
     *
     * @param index   index at which the specified element is to be inserted.
     * @param element element to be inserted.
     * @throws UnsupportedOperationException if the <tt>add</tt> method is not supported by this list.
     * @throws ClassCastException            if the class of the specified element prevents it from being added to this list.
     * @throws NullPointerException          if the specified element is null and this list does not support null elements.
     * @throws IllegalArgumentException      if some aspect of the specified element prevents it from being added to this
     *                                       list.
     * @throws IndexOutOfBoundsException     if the index is out of range (index &lt; 0 || index &gt; size()).
     */
    public void add(int index, E element) {
        if (index == 0) {
            addToHead(new DoublyLinkedListNode<E>(element));
        } else if (index == size) {
            addToTail(new DoublyLinkedListNode<E>(element));
        } else {
            DoublyLinkedListNode<E> nodeBefore = getNode(index);
            DoublyLinkedListNode<E> newNode = new DoublyLinkedListNode<E>(element, nodeBefore.getPreviousNode(), nodeBefore);
            if (nodeBefore.getPreviousNode() != null) {
                nodeBefore.getPreviousNode().setNextNode(newNode);
            }
            nodeBefore.setPreviousNode(newNode);
            size++;
        }
    }

    /**
     * Removes the element at the specified position in this list (optional operation). Shifts any subsequent elements to
     * the left (subtracts one from their indices). Returns the element that was removed from the list.
     *
     * @param index the index of the element to removed.
     * @return the element previously at the specified position.
     * @throws UnsupportedOperationException if the <tt>remove</tt> method is not supported by this list.
     * @throws IndexOutOfBoundsException     if the index is out of range (index &lt; 0 || index &gt;= size()).
     */
    public E remove(int index) {
        E removedValue = null;
        if (index == 0) {
            removedValue = head.getValue();
            remove(head);
        } else if (index == size - 1) {
            removedValue = tail.getValue();
            remove(tail);
        } else {
            DoublyLinkedListNode<E> node = getNode(index);
            removedValue = node.getValue();
            remove(getNode(index));
        }
        return removedValue;
    }

    /**
     * Returns the index in this list of the first occurrence of the specified element, or -1 if this list does not
     * contain this element. More formally, returns the lowest index <tt>i</tt> such that
     * <tt>(o==null ? get(i)==null : o.equals(get(i)))</tt>, or -1 if there is no such index.
     *
     * @param o element to search for.
     * @return the index in this list of the first occurrence of the specified element, or -1 if this list does not
     * contain this element.
     * @throws ClassCastException   if the type of the specified element is incompatible with this list (optional).
     * @throws NullPointerException if the specified element is null and this list does not support null elements
     *                              (optional).
     */
    public int indexOf(Object o) {
        DoublyLinkedListNode<E> currentNode = head;

        for (int n = 0; currentNode != null; n++) {
            E nodeValue = currentNode.getValue();
            if ((o == null && nodeValue == null) || (o != null && o.equals(nodeValue)) || (nodeValue != null && nodeValue.equals(o))) {
                return n;
            }
            currentNode = currentNode.getNextNode();
        }

        return -1;
    }

    /**
     * Returns the index in this list of the last occurrence of the specified element, or -1 if this list does not
     * contain this element. More formally, returns the highest index <tt>i</tt> such that
     * <tt>(o==null ? get(i)==null : o.equals(get(i)))</tt>, or -1 if there is no such index.
     *
     * @param o element to search for.
     * @return the index in this list of the last occurrence of the specified element, or -1 if this list does not
     * contain this element.
     * @throws ClassCastException   if the type of the specified element is incompatible with this list (optional).
     * @throws NullPointerException if the specified element is null and this list does not support null elements
     *                              (optional).
     */
    public int lastIndexOf(Object o) {
        DoublyLinkedListNode<E> currentNode = head;

        for (int n = size - 1; currentNode != null; n--) {
            E nodeValue = currentNode.getValue();
            if ((o == null && nodeValue == null) || (o != null && o.equals(nodeValue)) || (nodeValue != null && nodeValue.equals(o))) {
                return n;
            }
        }

        return -1;
    }

    /**
     * Returns a list iterator of the elements in this list (in proper sequence).
     *
     * @return a list iterator of the elements in this list (in proper sequence).
     */
    public ListIterator<E> listIterator() {
        return new DoublyLinkedListIterator(true);
    }

    /**
     * Returns a list iterator of the elements in this list (in proper sequence), starting at the specified position in
     * this list. The specified index indicates the first element that would be returned by an initial call to the
     * <tt>next</tt> method. An initial call to the <tt>previous</tt> method would return the element with the specified
     * index minus one.
     *
     * @param index index of first element to be returned from the list iterator (by a call to the <tt>next</tt> method).
     * @return a list iterator of the elements in this list (in proper sequence), starting at the specified position in
     * this list.
     * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &gt; size()).
     */
    public ListIterator<E> listIterator(int index) {
        return subList(index, size()).listIterator();
    }

    /**
     * Returns a view of the portion of this list between the specified <tt>fromIndex</tt>, inclusive, and
     * <tt>toIndex</tt>, exclusive. (If <tt>fromIndex</tt> and <tt>toIndex</tt> are equal, the returned list is empty.)
     * The returned list is backed by this list, so non-structural changes in the returned list are reflected in this
     * list, and vice-versa. The returned list supports all of the optional list operations supported by this list.
     * <p>
     * <p/>
     * This method eliminates the need for explicit range operations (of the sort that commonly exist for arrays). Any
     * operation that expects a list can be used as a range operation by passing a subList view instead of a whole list.
     * For example, the following idiom removes a range of elements from a list:
     *
     * <pre>
     * list.subList(from, to).clear();
     * </pre>
     * <p>
     * Similar idioms may be constructed for <tt>indexOf</tt> and <tt>lastIndexOf</tt>, and all of the algorithms in the
     * <tt>Collections</tt> class can be applied to a subList.
     * <p>
     * <p/>
     * The semantics of the list returned by this method become undefined if the backing list (i.e., this list) is
     * <i>structurally modified</i> in any way other than via the returned list. (Structural modifications are those that
     * change the size of this list, or otherwise perturb it in such a fashion that iterations in progress may yield
     * incorrect results.)
     *
     * @param fromIndex low endpoint (inclusive) of the subList.
     * @param toIndex   high endpoint (exclusive) of the subList.
     * @return a view of the specified range within this list.
     * @throws IndexOutOfBoundsException for an illegal endpoint index value (fromIndex &lt; 0 || toIndex &gt; size ||
     *                                   fromIndex &gt; toIndex).
     */
    public List<E> subList(int fromIndex, int toIndex) {
        // GAW TODO: Check the contract of sublist and adhere to it propertly.
        // I think if sublist(from, to).clear() is called it might not work
        // propertly and change the underlying list correctly.
        checkBounds(fromIndex, 0, size - 1);
        checkBounds(toIndex, 0, size);
        if (fromIndex > toIndex) {
            throw new IndexOutOfBoundsException("Sublist fromindex of " + fromIndex + " cannot be greater than the toIndex " + toIndex + ".");
        }

        DoublyLinkedListImpl<E> subListImpl = new DoublyLinkedListImpl<E>();
        if (toIndex > fromIndex) {
            subListImpl.head = getNode(fromIndex);
            subListImpl.tail = getNode(toIndex - 1);
        }
        subListImpl.size = toIndex - fromIndex;

        return subListImpl;
    }

    public DoublyLinkedListNode<E> addToHead(E object) {
        DoublyLinkedListNode<E> newNode = new DoublyLinkedListNode<E>(object);
        addToHead(newNode);
        return newNode;
    }

    public boolean addToHead(DoublyLinkedListNode<E> node) {
        node.setPreviousNode(null);
        node.setNextNode(head);

        if (head != null) {
            head.setPreviousNode(node);
        }
        head = node;
        if (tail == null) {
            tail = node;
        }
        size++;

        return true;
    }

    public DoublyLinkedListNode<E> addToTail(E object) {
        DoublyLinkedListNode<E> newNode = new DoublyLinkedListNode<E>(object);
        addToTail(newNode);
        return newNode;
    }

    public boolean addToTail(DoublyLinkedListNode<E> node) {
        node.setPreviousNode(tail);
        node.setNextNode(null);

        if (tail != null) {
            tail.setNextNode(node);
        }
        if (head == null) {
            head = node;
        }
        tail = node;
        size++;

        return true;
    }

    public boolean add(DoublyLinkedListNode<E> node) {
        return addToTail(node);
    }

    public boolean addFirst(DoublyLinkedListNode<E> node) {
        return addToHead(node);
    }

    public boolean addLast(DoublyLinkedListNode<E> node) {
        return addToTail(node);
    }

    public E removeFromHead() {
        if (size == 0) {
            throw new IllegalStateException("Illegal attempt to remove first element from empty doubly linked list.");
        }

        E headValue = head.getValue();
        remove(head);

        return headValue;
    }

    public E removeFromTail() {
        if (size == 0) {
            throw new IllegalStateException("Illegal attempt to remove last element from empty doubly linked list.");
        }

        E tailValue = tail.getValue();
        remove(tail);

        return tailValue;
    }

    @Override
    public void addFirst(E e) {

    }

    @Override
    public void addLast(E e) {

    }

    @Override
    public boolean offerFirst(E e) {
        return false;
    }

    @Override
    public boolean offerLast(E e) {
        return false;
    }

    public E removeFirst() {
        return removeFromHead();
    }

    public E removeLast() {
        return removeFromTail();
    }

    /**
     * Retrieves and removes the first element of this deque,
     * or returns {@code null} if this deque is empty.
     *
     * @return the head of this deque, or {@code null} if this deque is empty
     */
    @Override
    public E pollFirst() {
        return null;
    }

    /**
     * Retrieves and removes the last element of this deque,
     * or returns {@code null} if this deque is empty.
     *
     * @return the tail of this deque, or {@code null} if this deque is empty
     */
    @Override
    public E pollLast() {
        return null;
    }

    @Override
    public E getFirst() {
        return null;
    }

    @Override
    public E getLast() {
        return null;
    }

    public E peekHead() {
        if (size <= 0) {
            throw new IndexOutOfBoundsException("Error attempting to peek list head item for an empty list.");
        }
        return head.getValue();
    }

    public E peekTail() {
        if (size <= 0) {
            throw new IndexOutOfBoundsException("Error attempting to peek list tail item for an empty list.");
        }
        return tail.getValue();
    }

    public E peekFirst() {
        return peekHead();
    }

    public E peekLast() {
        return peekTail();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
    }

    protected void checkBounds(int index, int lowest, int highest) {
        if (index < lowest || index > highest) {
            throw new IndexOutOfBoundsException("List index, idx=" + index + " out of bounds: " + lowest + " < idx > " + highest);
        }
    }

    class DoublyLinkedListNodeIterator implements ListIterator<DoublyLinkedListNode<E>> {
        private boolean headToTail;

        private DoublyLinkedListNode<E> previousNode, nextNode;
        private int previousIndex, nextIndex;

        public DoublyLinkedListNodeIterator(boolean headToTail) {
            this.headToTail = headToTail;
            previousIndex = headToTail ? -1 : size;
            nextIndex = headToTail ? 0 : size;
            previousNode = null;
            nextNode = headToTail ? head : tail;
        }

        /**
         * Returns <tt>true</tt> if this list iterator has more elements when traversing the list in the forward
         * direction. (In other words, returns <tt>true</tt> if <tt>next</tt> would return an element rather than throwing
         * an exception.)
         *
         * @return <tt>true</tt> if the list iterator has more elements when traversing the list in the forward direction.
         */
        public boolean hasNext() {
            return nextNode != null;
        }

        /**
         * Returns the next element in the list. This method may be called repeatedly to iterate through the list, or
         * intermixed with calls to <tt>previous</tt> to go back and forth. (Note that alternating calls to <tt>next</tt>
         * and <tt>previous</tt> will return the same element repeatedly.)
         *
         * @return the next element in the list.
         * @throws NoSuchElementException if the iteration has no next element.
         */
        @SuppressWarnings("unchecked")
        public DoublyLinkedListNode<E> next() {
            if (!hasNext()) {
                throw new NoSuchElementException("List of size " + size + " and current position, " + nextIndex + ", has no next element.");
            }

            DoublyLinkedListNode<E> currentNode = nextNode;
            previousNode = nextNode;
            nextNode = headToTail ? nextNode.getNextNode() : nextNode.getPreviousNode();

            previousIndex = nextIndex;
            nextIndex = headToTail ? nextIndex+1 : nextIndex-1;

            return currentNode;
        }

        /**
         * Returns <tt>true</tt> if this list iterator has more elements when traversing the list in the reverse
         * direction. (In other words, returns <tt>true</tt> if <tt>previous</tt> would return an element rather than
         * throwing an exception.)
         *
         * @return <tt>true</tt> if the list iterator has more elements when traversing the list in the reverse direction.
         */
        public boolean hasPrevious() {
            return previousNode != null;
        }

        /**
         * Returns the previous element in the list. This method may be called repeatedly to iterate through the list
         * backwards, or intermixed with calls to <tt>next</tt> to go back and forth. (Note that alternating calls to
         * <tt>next</tt> and <tt>previous</tt> will return the same element repeatedly.)
         *
         * @return the previous element in the list.
         * @throws NoSuchElementException if the iteration has no previous element.
         */
        public DoublyLinkedListNode<E> previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException("List of size " + size + " and current position, " + nextIndex + ", has no previous element.");
            }

            DoublyLinkedListNode<E> currentNode = previousNode;
            previousNode = headToTail  ? previousNode.getPreviousNode() : previousNode.getNextNode();
            nextNode = currentNode;

            previousIndex = headToTail ? previousIndex-1 : previousIndex()+1;
            nextIndex = headToTail ? nextIndex-1 : nextIndex+1;

            return currentNode;
        }

        /**
         * Returns the index of the element that would be returned by a subsequent call to <tt>next</tt>. (Returns list
         * size if the list iterator is at the end of the list.)
         *
         * @return the index of the element that would be returned by a subsequent call to <tt>next</tt>, or list size if
         * list iterator is at end of list.
         */
        public int nextIndex() {
            return nextIndex;
//            return currentIndex + (headToTail ? 1 : -1);
        }

        /**
         * Returns the index of the element that would be returned by a subsequent call to <tt>previous</tt>. (Returns -1
         * if the list iterator is at the beginning of the list.)
         *
         * @return the index of the element that would be returned by a subsequent call to <tt>previous</tt>, or -1 if
         * list iterator is at beginning of list.
         */
        public int previousIndex() {
            return previousIndex;
//            return currentIndex + (headToTail ? 0 : 1);
        }

        /**
         * Removes from the list the last element that was returned by <tt>next</tt> or <tt>previous</tt> (optional
         * operation). This call can only be made once per call to <tt>next</tt> or <tt>previous</tt>. It can be made only
         * if <tt>ListIterator.add</tt> has not been called after the last call to <tt>next</tt> or <tt>previous</tt>.
         *
         * @throws UnsupportedOperationException if the <tt>remove</tt> operation is not supported by this list iterator.
         * @throws IllegalStateException         neither <tt>next</tt> nor <tt>previous</tt> have been called, or <tt>remove</tt>
         *                                       or <tt>add</tt> have been called after the last call to * <tt>next</tt> or <tt>previous</tt>.
         */
        public void remove() {
            if (!hasPrevious()) {
                throw new NoSuchElementException("List of size " + size + " and current position, " + nextIndex + ", has no previous element to remove.");
            }

            previousNode = headToTail ? previousNode.getPreviousNode() : previousNode.getNextNode();
            previousIndex = previousNode != null ? previousIndex-1 : -1;
            DoublyLinkedListImpl.this.remove(previousNode);
        }

        /**
         * Replaces the last element returned by <tt>next</tt> or <tt>previous</tt> with the specified element (optional
         * operation). This call can be made only if neither <tt>ListIterator.remove</tt> nor <tt>ListIterator.add</tt>
         * have been called after the last call to <tt>next</tt> or <tt>previous</tt>.
         *
         * @param o the element with which to replace the last element returned by <tt>next</tt> or <tt>previous</tt>.
         * @throws UnsupportedOperationException if the <tt>set</tt> operation is not supported by this list iterator.
         * @throws ClassCastException            if the class of the specified element prevents it from being added to this list.
         * @throws IllegalArgumentException      if some aspect of the specified element prevents it from being added to this
         *                                       list.
         * @throws IllegalStateException         if neither <tt>next</tt> nor <tt>previous</tt> have been called, or
         *                                       <tt>remove</tt> or <tt>add</tt> have been called after the last call to <tt>next</tt> or
         *                                       <tt>previous</tt>.
         */
        public void set(DoublyLinkedListNode<E> o) {
//            if (currentIndex < 0 || currentIndex >= size || currentNode == null) {
//                throw new IllegalStateException("Unable to set object in list - the list is empty or you have not called next() or previous() first.");
//            }
//            DoublyLinkedListImpl.this.set(currentIndex, o);
        }

        /**
         * Inserts the specified element into the list (optional operation). The element is inserted immediately before
         * the next element that would be returned by <tt>next</tt>, if any, and after the next element that would be
         * returned by <tt>previous</tt>, if any. (If the list contains no elements, the new element becomes the sole
         * element on the list.) The new element is inserted before the implicit cursor: a subsequent call to
         * <tt>next</tt> would be unaffected, and a subsequent call to <tt>previous</tt> would return the new element.
         * (This call increases by one the value that would be returned by a call to <tt>nextIndex</tt> or
         * <tt>previousIndex</tt>.)
         *
         * @param o the element to insert.
         * @throws UnsupportedOperationException if the <tt>add</tt> method is not supported by this list iterator.
         * @throws ClassCastException            if the class of the specified element prevents it from being added to this list.
         * @throws IllegalArgumentException      if some aspect of this element prevents it from being added to this list.
         */
        @SuppressWarnings("unchecked")
        public void add(DoublyLinkedListNode<E> o) {
            if (size == 0) {
                DoublyLinkedListImpl.this.add(o);
            } else if (headToTail) {
                if (nextNode != null) {
                    addBefore(nextNode, o);
                } else if (previousNode != null) {
                    addAfter(previousNode, o);
                }
            } else {
                if (nextNode != null) {
                    addAfter(nextNode, o);
                } else if (previousNode != null) {
                    addBefore(previousNode, o);
                }
            }
            nextNode = o;
        }
    }

    class DoublyLinkedListIterator implements ListIterator<E> {
        private DoublyLinkedListNodeIterator nodeIterator;

        public DoublyLinkedListIterator(boolean headToTail) {
            this.nodeIterator = new DoublyLinkedListNodeIterator(headToTail);
        }

        @Override
        public boolean hasNext() {
            return nodeIterator.hasNext();
        }

        @Override
        public E next() {
            return nodeIterator.next().getValue();
        }

        @Override
        public boolean hasPrevious() {
            return nodeIterator.hasPrevious();
        }

        @Override
        public E previous() {
            return nodeIterator.previous().getValue();
        }

        @Override
        public int nextIndex() {
            return nodeIterator.nextIndex();
        }

        @Override
        public int previousIndex() {
            return nodeIterator.previousIndex();
        }

        @Override
        public void remove() {
            nodeIterator.remove();
        }

        @Override
        public void set(E e) {
            nodeIterator.set(new DoublyLinkedListNode<>(e));
        }

        @Override
        public void add(E e) {
            nodeIterator.add(new DoublyLinkedListNode<>(e));
        }
    }

    /**
     * Determines the equality of this list with another.
     *
     * @param other the other list whose equality to this one is to be determined.
     * @return true if this list is equivalent to the one specified, false otherwise.
     */
    public boolean equals(Object other) {
        if (this == other) return true;

        if ( !(other instanceof List) ) return false;

        List that = (List)other;
        Iterator<?> thisIter = iterator(), thatIter = that.iterator();
        while ( thisIter.hasNext() && thatIter.hasNext() ) {
            if ( !Objects.equals(thisIter.next(), thatIter.next()) ) return false;
        }

        return !thisIter.hasNext() && !thatIter.hasNext();
    }

    public int hashCode() {
        return hashCodeList(this);
    }

    private int hashCodeList(List<?> list) {
        if (list == null)
            return 0;

        int result = 1;

        for (Object element : list)
            result = 31 * result + (element == null ? 0 : element.hashCode());

        return result;
    }

    public String toString() {
        return asDelimitedString(this, ",");
    }
}
