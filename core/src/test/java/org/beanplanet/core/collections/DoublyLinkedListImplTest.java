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

import org.beanplanet.core.util.IteratorUtil;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.beanplanet.core.util.IteratorUtil.asStream;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class DoublyLinkedListImplTest {
    @Test
    public void ctor_noArgs() {
        assertThat(new DoublyLinkedListImpl<>().isEmpty(), is(true));
    }

    @Test
    public void ctor_collection() {
        // Given
        List<Integer> otherCollection = asList(1, 2, 3);

        // When
        DoublyLinkedListImpl<Integer> dll = new DoublyLinkedListImpl<>(otherCollection);

        // Then
        assertThat(dll.isEmpty(), is(false));
        assertThat(dll.size(), equalTo(otherCollection.size()));
        assertThat(otherCollection, equalTo(dll));
    }

    @Test
    public void add() {
        // Given
        DoublyLinkedListImpl<Integer> dll = new DoublyLinkedListImpl<>();

        // When
        dll.add(11);

        // Then
        assertThat(dll.isEmpty(), is(false));
        assertThat(dll.size(), equalTo(1));
        assertThat(dll.get(0), equalTo(11));
        assertThat(dll.peek(), equalTo(11));
        assertThat(dll.peekHead(), equalTo(11));
        assertThat(dll.peekFirst(), equalTo(11));
        assertThat(dll.peekTail(), equalTo(11));
        assertThat(dll.peekLast(), equalTo(11));

        // When
        dll.add(22);

        // Then
        assertThat(dll.size(), equalTo(2));
        assertThat(dll.get(1), equalTo(22));
        assertThat(dll.peekHead(), equalTo(11));
        assertThat(dll.peekFirst(), equalTo(11));
        assertThat(dll.peekTail(), equalTo(22));
        assertThat(dll.peekLast(), equalTo(22));
    }

    @Test
    public void add_index() {
        // Given
        DoublyLinkedListImpl<Integer> dll = new DoublyLinkedListImpl<>();

        // When
        dll.add(0, 11);

        // Then
        assertThat(dll, equalTo(asList(11)));

        // When
        dll.add(0, 22);

        // Then
        assertThat(dll, equalTo(asList(22, 11)));

        // When
        dll.add(1, 33);

        // Then
        assertThat(dll, equalTo(asList(22, 33, 11)));

        // When
        dll.add(3, 44);

        // Then
        assertThat(dll, equalTo(asList(22, 33, 11, 44)));
    }

    @Test
    public void addAll() {
        // Given
        DoublyLinkedListImpl<Integer> dll = new DoublyLinkedListImpl<>();

        // When I add the empty list
        dll.addAll(emptyList());

        // Then it remains unchanged
        assertThat(dll.isEmpty(), is(true));

        // When I add elements
        boolean result = dll.addAll(asList(11, 22));

        // Then the elements are added
        assertThat(dll, equalTo(asList(11, 22)));

        // When I add more elements
        result = dll.addAll(asList(33, 22, 44));

        // Then the elements are added
        assertThat(dll, equalTo(asList(11, 22, 33, 22, 44)));
    }

    @Test
    public void addAll_index() {
        // Given
        DoublyLinkedListImpl<Integer> dll = new DoublyLinkedListImpl<>();

        // When I add the empty list
        dll.addAll(0, emptyList());

        // Then it remains unchanged
        assertThat(dll.isEmpty(), is(true));

        // When I add elements
        boolean result = dll.addAll(0, asList(11, 22));

        // Then the elements are added
        assertThat(dll, equalTo(asList(11, 22)));

        // When I add more elements
        result = dll.addAll(1, asList(33, 22, 44));

        // Then the elements are added
        assertThat(dll, equalTo(asList(11, 33, 22, 44, 22)));

        // When I add more elements to penultimate
        result = dll.addAll(4, asList(55, 66));

        // Then the elements are added
        assertThat(dll, equalTo(asList(11, 33, 22, 44, 55, 66, 22)));

        // When I add more elements to the tail
        result = dll.addAll(dll.size(), asList(77));

        // Then the elements are added
        assertThat(dll, equalTo(asList(11, 33, 22, 44, 55, 66, 22, 77)));
    }

    @Test
    public void addAfter() {
        // Given
        DoublyLinkedListImpl<Integer> dll = new DoublyLinkedListImpl<>();
        dll.add(11);

        // When
        boolean added = dll.addAfter(dll.getNode(0), 22);

        // Then
        assertThat(added, is(true));
        assertThat(dll, equalTo(asList(11, 22)));

        // When
        added = dll.addAfter(dll.getNode(0), 33);

        // Then
        assertThat(added, is(true));
        assertThat(dll, equalTo(asList(11, 33, 22)));

    }

    @Test
    public void addBefore() {
        // Given
        DoublyLinkedListImpl<Integer> dll = new DoublyLinkedListImpl<>();
        dll.add(11);

        // When
        boolean added = dll.addBefore(dll.getNode(0), 22);

        // Then
        assertThat(added, is(true));
        assertThat(dll, equalTo(asList(22, 11)));

        // When
        added = dll.addBefore(dll.getNode(1), 33);

        // Then
        assertThat(added, is(true));
        assertThat(dll, equalTo(asList(22, 33, 11)));

    }

    @Test
    public void nodeIteratorHeadToTail() {
        assertThat(
                asStream(new DoublyLinkedListImpl<>(asList(11, 22, 33)).nodeIteratorHeadToTail())
                        .map(DoublyLinkedListNode::getValue)
                        .collect(Collectors.toList()),
                equalTo(asList(11, 22, 33))
        );
    }

    @Test
    public void nodeIteratorHeadToTail_forwards() {
        // Given
        ListIterator<DoublyLinkedListNode<Integer>> dll = new DoublyLinkedListImpl<>(asList(11, 22, 33)).nodeIteratorHeadToTail();

        // Then
        assertThat(dll.hasPrevious(), is(false));
        assertThat(dll.hasNext(), is(true));
        assertThat(dll.next().getValue(), equalTo(11));

        assertThat(dll.hasPrevious(), is(true));
        assertThat(dll.hasNext(), is(true));
        assertThat(dll.previousIndex(), equalTo(0));
        assertThat(dll.previous().getValue(), equalTo(11));

        assertThat(dll.hasPrevious(), is(false));
        assertThat(dll.hasNext(), is(true));
        assertThat(dll.next().getValue(), equalTo(11));

        assertThat(dll.hasPrevious(), is(true));
        assertThat(dll.hasNext(), is(true));
        assertThat(dll.next().getValue(), equalTo(22));

        assertThat(dll.hasPrevious(), is(true));
        assertThat(dll.hasNext(), is(true));
        assertThat(dll.next().getValue(), equalTo(33));

        assertThat(dll.hasPrevious(), is(true));
        assertThat(dll.hasNext(), is(false));
        assertThat(dll.previous().getValue(), equalTo(33));
    }

    @Test
    public void nodeIteratorHeadToTail_forward_adds() {
        // Given an empty lis
        DoublyLinkedList<Integer> dll = new DoublyLinkedListImpl<>();
        ListIterator<DoublyLinkedListNode<Integer>> iter = dll.nodeIteratorHeadToTail();

        // When a first element is added at the start
        iter.add(new DoublyLinkedListNode<>(11));

        // Then the element is added correctly
        assertThat(IteratorUtil.toList((dll.iterator())), equalTo(asList(11)));

        // And the next and previous elements are correct
        assertThat(iter.hasNext(), is(true));
        assertThat(iter.hasPrevious(), is(false));

        // Given
        iter = dll.nodeIteratorHeadToTail();

        // When an element is added at the start
        iter.add(new DoublyLinkedListNode<>(44));

        // Then the element is added correctly
        assertThat(IteratorUtil.toList((dll.iterator())), equalTo(asList(44, 11)));

        // And the next and previous elements are correct
        assertThat(iter.hasNext(), is(true));
        assertThat(iter.hasPrevious(), is(false));

        // When an element is added to the end
        iter.next();
        iter.next();
        iter.add(new DoublyLinkedListNode<>(55));

        // Then the element is added correctly
        assertThat(IteratorUtil.toList((dll.iterator())), equalTo(asList(44, 11, 55)));

        // And the next and previous elements are correct
        assertThat(iter.hasNext(), is(true));
        assertThat(iter.hasPrevious(), is(true));
    }

    @Ignore("TODO")
    @Test
    public void nodeIteratorHeadToTail_remove() {
        // Given a list
        DoublyLinkedList<Integer> dll = new DoublyLinkedListImpl<>(asList(11, 22));
        ListIterator<DoublyLinkedListNode<Integer>> iter = dll.nodeIteratorHeadToTail();
        iter.next();

        // When a first element is removed
        iter.remove();

        // Then the element is removed correctly
        assertThat(IteratorUtil.toList((dll.iterator())), equalTo(asList(22)));

        // And the next and previous elements are correct
        assertThat(iter.hasNext(), is(true));
        assertThat(iter.hasPrevious(), is(false));

        assertThat(iter.previousIndex(), equalTo(-1));
        assertThat(iter.nextIndex(), equalTo(0));
    }

    @Test
    public void remove_Object() {
        // Given
        DoublyLinkedListImpl<Integer> dll = new DoublyLinkedListImpl<>(asList(11, 22, 33));

        // When
        boolean removed = dll.remove((Object)11);

        // Then
        assertThat(removed, is(true));
        assertThat(dll, equalTo(asList(22, 33)));

        // When I attempt to remove a non-existent element
        removed = dll.remove((Object)999);

        // Then
        assertThat(removed, is(false));
    }

    @Test
    public void remove_Node() {
        // Given
        DoublyLinkedListImpl<Integer> dll = new DoublyLinkedListImpl<>(asList(11, 22, 33));

        // When I remove from the middle
        boolean removed = dll.remove(dll.getNode(1));

        // Then
        assertThat(removed, is(true));
        assertThat(dll, equalTo(asList(11, 33)));

        // When I remove from the tail
        removed = dll.remove(dll.getNode(1));

        // Then
        assertThat(removed, is(true));
        assertThat(dll, equalTo(asList(11)));

        // When I remove the last element
        removed = dll.remove(dll.getNode(0));

        // Then
        assertThat(removed, is(true));
        assertThat(dll.isEmpty(), is(true));

    }

    @Test
    public void containsAll() {
        // Given
        DoublyLinkedListImpl<Integer> dll = new DoublyLinkedListImpl<>(asList(11, 22, 33));

        // When I check if the list contains the empty list
        boolean result = dll.containsAll(emptyList());

        // Then it always contains the emapty list
        assertThat(result, is(true));

        // When
        result = dll.containsAll(asList(22, 11));

        // Then
        assertThat(result, is(true));

        // When I check if it contains a superlist
        result = dll.containsAll(asList(22, 11, 999));

        // Then it does not
        assertThat(result, is(false));
    }
}