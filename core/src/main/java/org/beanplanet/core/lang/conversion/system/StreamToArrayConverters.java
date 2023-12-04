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
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Conversion functions from a {@link Stream} to various sequence types (arrays, collections etc).
 */
@TypeConverter
public final class StreamToArrayConverters {
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
     * Converts the specified {@link Stream<Boolean>} to an array of primitive boolean.
     *
     * @param value      the stream to be converted to an array of primitive boolean.
     * @return an array of primitive boolean, or null if the stream was null.
     */
    @TypeConverter
    public static boolean[] streamToPrimitiveBooleanArray(final Stream<Boolean> value) {
        if (value == null) return null;

        final List<Boolean> list = value.collect(Collectors.toList());
        boolean[] result = new boolean[list.size()];
        for (int n=0; n < list.size(); n++) {
            result[n] = list.get(n);
        }
        return result;
    }

    /**
     * Converts the specified {@link Stream<Byte>} to an array of primitive byte.
     *
     * @param value      the stream to be converted to an array of primitive byte.
     * @return an array of primitive byte, or null if the stream was null.
     */
    @TypeConverter
    public static byte[] streamToPrimitiveByteArray(final Stream<Byte> value) {
        if (value == null) return null;

        final List<Byte> list = value.collect(Collectors.toList());
        byte[] result = new byte[list.size()];
        for (int n=0; n < list.size(); n++) {
            result[n] = list.get(n);
        }
        return result;
    }

    /**
     * Converts the specified {@link Stream<Character>} to an array of primitive char.
     *
     * @param value      the stream to be converted to an array of primitive char.
     * @return an array of primitive char, or null if the stream was null.
     */
    @TypeConverter
    public static char[] streamToPrimitiveCharArray(final Stream<Character> value) {
        if (value == null) return null;

        final List<Character> list = value.collect(Collectors.toList());
        char[] result = new char[list.size()];
        for (int n=0; n < list.size(); n++) {
            result[n] = list.get(n);
        }
        return result;
    }

    /**
     * Converts the specified {@link Stream<Float>} to an array of primitive float.
     *
     * @param value      the stream to be converted to an array of primitive float.
     * @return an array of primitive float, or null if the stream was null.
     */
    @TypeConverter
    public static float[] streamToPrimitiveFloatArray(final Stream<Float> value) {
        if (value == null) return null;

        final List<Float> list = value.collect(Collectors.toList());
        float[] result = new float[list.size()];
        for (int n=0; n < list.size(); n++) {
            result[n] = list.get(n);
        }
        return result;
    }

    /**
     * Converts the specified {@link Stream<Double>} to an array of primitive double.
     *
     * @param value      the stream to be converted to an array of primitive double.
     * @return an array of primitive double, or null if the stream was null.
     */
    @TypeConverter
    public static double[] streamToPrimitiveDoubleArray(final Stream<Double> value) {
        return value == null ? null : value.mapToDouble(Double::doubleValue).toArray();
    }

    /**
     * Converts the specified {@link Stream<Integer>} to an array of primitive integer.
     *
     * @param value      the stream to be converted to an array of primitive integer.
     * @return an array of primitive integer, or null if the stream was null.
     */
    @TypeConverter
    public static int[] streamToPrimitiveIntArray(final Stream<Integer> value) {
        return value == null ? null : value.mapToInt(Integer::intValue).toArray();
    }

    /**
     * Converts the specified {@link Stream<Long>} to an array of primitive long.
     *
     * @param value      the stream to be converted to an array of primitive long.
     * @return an array of primitive long, or null if the stream was null.
     */
    @TypeConverter
    public static long[] streamToPrimitiveLongArray(final Stream<Long> value) {
        return value == null ? null : value.mapToLong(Long::longValue).toArray();
    }

    /**
     * Converts the specified {@link Stream<Short>} to an array of primitive short.
     *
     * @param value      the stream to be converted to an array of primitive short.
     * @return an array of primitive short, or null if the stream was null.
     */
    @TypeConverter
    public static short[] streamToPrimitiveShortArray(final Stream<Short> value) {
        if (value == null) return null;

        final List<Short> list = value.collect(Collectors.toList());
        short[] result = new short[list.size()];
        for (int n=0; n < list.size(); n++) {
            result[n] = list.get(n);
        }
        return result;
    }
}
