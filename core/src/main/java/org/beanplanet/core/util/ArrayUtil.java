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

import org.beanplanet.core.lang.TypeUtil;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class ArrayUtil {
    private static final Map<Class<?>, Object[]> TYPE_TO_EMPTY_ARRAY = Collections.synchronizedMap(new TreeMap<>(
            (Comparator<Class<?>>) (o1, o2) -> {
                if (o1 == o2) return 0;
                if (o1 == null) return -1;
                if (o2 == null) return 1;

                return TypeUtil.getBaseName(o1).compareTo(TypeUtil.getBaseName(o2));
            }
    ));

    public static final Object[] EMPTY_ARRAY = new Object[0];

    /**
     * Returns an empty array of the component type inferred from the LHS context.
     *
     * @param <T> the component type of the empty array to be returned.
     * @return an empty array of the component type inferred.
     */
    @SuppressWarnings("unchecked")
    public static final <T> T[] emptyArray(Class<T> componentType) {
        return (T[])TYPE_TO_EMPTY_ARRAY.computeIfAbsent(componentType, ct -> (T[])Array.newInstance(ct, 0));
    }
}
