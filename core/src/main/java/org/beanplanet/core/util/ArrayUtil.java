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

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ArrayUtil {
    /**
     * Determines whether a given array is null or empty.
     *
     * @param array the array to be determined as null or empty;
     * @return true is the array is null or contains no elements, false otherwise.
     */
    public static final <T> boolean isEmptyOrNull(T array[]) {
        return array == null || array.length ==0;
    }

    /**
     * A convenient method to not have to check if an array is null. Particularly useful
     * in the new for loop which does not do this check.
     *
     * @param componentType the component type of the array.
     * @param array the array which may be null.
     * @return the array or an empty array if null.
     */
    @SuppressWarnings("unchecked")
    public static final <T> T[] nullSafe(Class<T> componentType, T[] array) {
        return array != null ? array : (T[])ArrayUtil.emptyArray(componentType);
    }

    /**
     * Returns an empty array of the component type inferred from the LHS context.
     *
     * @param componentType the component type of the empty array to be returned.
     * @return an empty array of the component type inferred.
     */
    @SuppressWarnings("unchecked")
    public static final Object emptyArray(Class<?> componentType) {
        return Array.newInstance(componentType, 0);
    }

    /**
     * Returns an empty array of a given number of dimensions of the component type inferred from the LHS context.
     *
     * @param componentType the component type of the empty array to be returned.
     * @return an empty array of the component type inferred.
     */
    @SuppressWarnings("unchecked")
    public static final Object emptyArray(Class<?> componentType, int dimensions) {
        Object targetArray = emptyArray(componentType);
        for (int n=1; n < dimensions; n++) {
            targetArray = emptyArray(targetArray.getClass());
        }

        return targetArray;
    }

    /**
     * Returns in order a list of the non-null elements specified.
     *
     * @param items the items whose non-null elements are to be returned, in order.
     * @return a list of the elements of <code>items</code> that are not null.
     */
    public static <T> List<T> asListOfNotNull(T ... items) {
        return Arrays.stream(items).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * Given an array, returns the size of the specified dimension. In the case of
     * <code>determineArraySize(new boolean[77][88][99], 2)</code>) the size <code>88</code> would be returned.
     *
     * @param array the array whose dimension size is to be determined.
     * @param dimension the number of the dimension whose size is to be determined, starting with one for the first dimension.
     * @return the size of the given dimension of the given array, or zero if the array was null or is not an array.
     */
    public static int determineArraySize(Object array, final int dimension) {
        int length = 0;
        for (int n=1; n <= dimension; n++) {
            if (array instanceof Object[]) {
                length =  ((Object[]) array).length;
                array = ((Object[]) array)[0];
            } else if (array instanceof int[]) {
                length = ((int[]) array).length;
                array = ((int[]) array)[0];
            } else if (array instanceof double[]) {
                length = ((double[]) array).length;
                array = ((double[]) array)[0];
            } else if (array instanceof boolean[]) {
                length = ((boolean[]) array).length;
                array = ((boolean[]) array)[0];
            } else if (array instanceof long[]) {
                length = ((long[]) array).length;
                array = ((long[]) array)[0];
            } else if (array instanceof byte[]) {
                length = ((byte[]) array).length;
                array = ((byte[]) array)[0];
            } else if (array instanceof char[]) {
                length = ((char[]) array).length;
                array = ((char[]) array)[0];
            } else if (array instanceof float[]) {
                length = ((float[]) array).length;
                array = ((float[]) array)[0];
            } else if (array instanceof short[]) {
                length = ((short[]) array).length;
                array = ((short[]) array)[0];
            } else {
                return 0;
            }
        }

        return length;
    }

    /**
     * Given an array, returns the size of the first dimension. Equivalent to {@link #determineArraySize(Object, int)} where
     * the dimension is one.
     *
     * @param array the array whose size is to be determined.
     * @return the size of the first dimension of the given array, or zero if the array was null or is not an array.
     * @see  #determineArraySize(Object, int)
     */
    public static int determineArraySize(final Object array) {
        return determineArraySize(array, 1);
    }

    /**
     * Creates a new instance of an array whose component type and content are that of the given singleton.
     *
     * @param singleton the array's only element
     * @param componentType the type of the singleton element.
     * @return a new array contaning the given element.
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] singletonArray(T singleton, Class<T> componentType) {
        final T[] arr = (T[])Array.newInstance(componentType, 1);
        arr[0] = singleton;
        return arr;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] concat(final T[] left, final T[] right) {
        if (left == null && right == null) return null;
        if (left != null && right == null) return left;
        if (left == null) return right;

        final T[] newArray = (T[]) Array.newInstance(left.getClass().getComponentType(), left.length + right.length);
        System.arraycopy(left, 0, newArray, 0, left.length);
        System.arraycopy(right, 0, newArray, left.length, right.length);

        return newArray;
    }
}
