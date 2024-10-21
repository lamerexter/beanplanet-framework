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
