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

import java.util.ArrayList;
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

    /**
     * Converts a float array to a list of float wrapper.
     *
     * @param floats the array of flost to be converted.
     * @return a new List, comprised of the float array values, or null if the float array was null.
     */
    public static List<Float> toList(final float[] floats) {
        if (floats == null) return null;

        List<Float> floatsList = new ArrayList<>(floats.length);
        for (int n=0; n < floats.length; n++) {
            floatsList.add(floats[n]);
        }

        return floatsList;
    }
}

