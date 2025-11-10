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

package org.beanplanet.core.util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IterableUtil {
    public static <E> Stream<E> asStream(Iterable<E> iterable) {
        return IteratorUtil.asStream(iterable.iterator());
    }

    /**
     * Guarantees to return a non-null {@link Iterable} for a possibly null enumeration.
     *
     * @param enumerationSupplier a supplier of an enumeration over whose elements the iterable will iterate, which may be null.
     * @return an iterable, either backed by the enumeration supplied if the enumeration was not null, or an empty collection otherwise.
     */
    public static <T> Iterable<T> nullSafeEnumerationIterable(final Supplier<Enumeration<T>> enumerationSupplier) {
        if (enumerationSupplier == null) return Collections.emptyList();

        return new EnumerationIterable<>(enumerationSupplier);
    }

    /**
     * A convenient method to not have to check if a collection is null. Particularly useful
     * in the new for loop which does not do this check.
     *
     * @param iterable the iterable collection.
     * @return the iterable collection or an empty collection if null.
     */
    public static final <T> Iterable<T> nullSafe(Iterable<T> iterable) {
        return iterable != null ? iterable : Collections.emptyList();
    }

    /**
     * Returns an empty immutable iterable.
     *
     * @return an immutable iterable.
     */
    @SuppressWarnings("unchecked")
    public static <T> Iterable<T> emptyImmutableIterable() {
        return (Iterable<T>)Collections.emptyList();
    }

    /**
     * Converts an {@link Iterable<T>} to a {@link List<T>}.#
     *
     * @param iterable the iterable to be converted to a list.
     * @return a list, containing all the elements provided by the iterable sequence.
     */
    public static <T> List<T> toList(final Iterable<T> iterable) {
        return iterable == null ? null : asStream(iterable).collect(Collectors.toList());
    }
}
