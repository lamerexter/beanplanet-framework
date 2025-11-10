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

package org.beanplanet.core.collections;

import org.beanplanet.core.models.Builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * Useful utility for building a list of elements using the fluid builder pattern.
 */
public class ListBuilder<T> implements Builder<List<T>> {
    private List<T> building = new ArrayList<>();

    private ListBuilder() {}

    public static <T> ListBuilder<T> builder() {
        return new ListBuilder<>();
    }

    @SafeVarargs
    public final <E extends T> ListBuilder<T> add(E ... elements) {
        if ( elements != null ) {
            for (E element : elements) {
                building.add(element);
            }
        }
        return this;
    }

    @SafeVarargs
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

    @SafeVarargs
    public final <E extends T> ListBuilder<T> addIf(boolean condition, E ... elements) {
        if ( condition ) {
            add(elements);
        }
        return this;
    }

    public final <E extends T> ListBuilder<T> addIf(boolean condition, Supplier<E> elementSupplier) {
        if ( condition ) {
            add(elementSupplier.get());
        }
        return this;
    }

    public final <E extends T> ListBuilder<T> addNotNull(Object obj, Supplier<E> elementSupplier) {
        return addIf(obj != null, elementSupplier);
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
