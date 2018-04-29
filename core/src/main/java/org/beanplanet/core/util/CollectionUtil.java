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

import java.util.Collection;
import java.util.Collections;

/**
 * A utility class containing useful collection methods.
 *
 * @author Chris Taylor
 */
public class CollectionUtil {
    /**
     * Determines whether a given collection is not null and is not empty.
     *
     * @param collection the collection to determine if null or empty.
     * @return true if the collection is not null and contains items, false otherwise.
     */
    public static final boolean isNotNullOrEmpty(Collection<?> collection) {
        return !isNullOrEmpty(collection);
    }

    /**
     * Determines whether a given collection is null or is empty.
     *
     * @param collection the collection to determine if null or empty.
     * @return true if the collection is null or contains no items, false otherwise.
     */
    public static final boolean isNullOrEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * A convenient method to not have to check if a collection is null. Particularly useful
     * in the new for loop which does not do this check.
     *
     * @param iterable the iterable collection.
     * @return the iterable collection or an empty collection if null.
     */
    public static final <T> Iterable<T> nullSafe(Iterable<T> iterable) {
        return iterable != null ? iterable : Collections.<T>emptyList();
    }

    /**
     * A convenient method to not have to check if an array is null. Particularly useful
     * in the new for loop which does not do this check.
     *
     * @param array the array which may be null.
     * @return the array or an empty array if null.
     */
    @SuppressWarnings("unchecked")
    public static final <T> T[] nullSafe(T[] array) {
        return array != null ? array : (T[])ArrayUtil.emptyArray(Object.class);
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
}

