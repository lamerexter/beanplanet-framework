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

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
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

    public static <T> List<T> asListOfNotNull(T ... notNullItem ) {
        return Arrays.asList(notNullItem).stream().filter(e -> e != null).collect(Collectors.toList());
    }
}
