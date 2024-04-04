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
import java.util.List;

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
    public static final boolean isEmptyOrNull(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Determines whether a given collection is null or is empty.
     *
     * @param collection the collection to determine if null or empty.
     * @return true if the collection is null or contains no items, false otherwise.
     */
    public static final boolean isNullOrEmpty(Collection<?> collection) {
        return isEmptyOrNull(collection);
    }

    /**
     * A convenient method to not have to check if a collection is null. Particularly useful
     * in the new for loop which does not do this check.
     *
     * @param collection the collection to check if null.
     * @return the specified collection if not null or an immutable empty collection otherwise.
     */
    public static final <T> Collection<T> nullSafe(Collection<T> collection) {
        return collection != null ? collection : Collections.emptyList();
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
     * Returns the last element in a list.
     *
     * @param list the list whose last element is to be returned.
     * @return the last element
     */
    public static <T> T lastOrNull(List<T> list) {
        return list == null || list.isEmpty() ? null : list.get(list.size()-1);
    }
}

