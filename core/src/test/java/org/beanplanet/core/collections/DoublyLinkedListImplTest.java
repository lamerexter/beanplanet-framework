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

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

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

}