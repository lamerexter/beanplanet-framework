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
package org.beanplanet.core.lang.conversion.system;

import org.beanplanet.core.lang.conversion.annotations.TypeConverter;

import java.lang.reflect.Array;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Conversion functions from a {@link Stream} to various sequence types (arrays, collections etc).
 */
@TypeConverter
public final class StreamToSequenceReductionConverters {
    /**
     * Converts the specified {@link Stream} to an array of the given target type.
     *
     * @param value      the stream to be reduced to an array whose component type was specified.
     * @param targetType the target type to be created, which must be an array type. All elements in the stream MUST be assignable to the
     *                      component type of the array type.
     * @throws ArrayStoreException if one or more elements from the stream are not assignable to the given component type.
     */
    @SuppressWarnings("unchecked")
    @TypeConverter
    public static <T> T[] streamToArray(final Stream<T> value, final Class<T> targetType) {
        final Class<?> componentType = targetType.isArray() ? targetType.getComponentType() : targetType;
        return (value == null ? null : value.toArray(size -> (T[]) Array.newInstance(componentType, size)));
    }

    /**
     * Converts the specified {@link Stream} to a {@link List} of the given component type.
     *
     * @param value the stream to be reduced to a list.
     */
    @SuppressWarnings("unchecked")
    @TypeConverter
    public static <T> List<T> streamToList(final Stream<T> value) {
        return (value == null ? null : value.collect(Collectors.toList()));
    }

    /**
     * Converts the specified {@link Stream} to a {@link java.util.LinkedHashSet} whose order is that of the stream.
     *
     * @param value the stream to be reduced to an ordered set.
     */
    @SuppressWarnings("unchecked")
    @TypeConverter
    public static <T> LinkedHashSet<T> streamToLinkedHashSet(final Stream<T> value) {
        return (value == null ? null : value.collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    /**
     * Converts the specified {@link Stream} to a {@link java.util.Set} whose order is that of the stream.
     *
     * @param value the stream to be reduced to an ordered set.
     */
    @SuppressWarnings("unchecked")
    @TypeConverter
    public static <T> Set<T> streamToSet(final Stream<T> value) {
        return streamToLinkedHashSet(value);
    }
}
