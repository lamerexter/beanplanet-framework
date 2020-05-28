/*
 *  MIT Licence:
 *
 *  Copyright (C) 2020 Beanplanet Ltd
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

import org.beanplanet.core.models.Builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Useful utility for building a list of elements using the fluid builder pattern.
 */
public class ListBuilder<T> implements Builder<List<T>> {
    private List<T> building = new ArrayList<>();

    private ListBuilder() {}

    public static <T> ListBuilder<T> builder() {
        return new ListBuilder<>();
    }

    public final <E extends T> ListBuilder<T> add(E ... elements) {
        if ( elements != null ) {
            for (E element : elements) {
                building.add(element);
            }
        }
        return this;
    }

    public final <E extends T> ListBuilder<T> addNotNull(E ... elements) {
        if ( elements != null ) {
            for (E element : elements) {
                if ( element != null ) {
                    building.add(element);
                }
            }
        }
        return this;
    }

    public final <E extends T> ListBuilder<T> addAll(Collection<E> elements) {
        building.addAll(elements);
        return this;
    }

    public final <E extends T> ListBuilder<T> addAllNotNull(Collection<E> elements) {
        if ( elements != null ) {
            for (E element : elements) {
                if ( element != null ) {
                    building.add(element);
                }
            }
        }

        return this;
    }

    @Override
    public List<T> build() {
        return new ArrayList<>(building);
    }
}
