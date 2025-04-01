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

import java.util.Arrays;
import java.util.Iterator;

/**
 * Source types to {@link Iterable} type conversions.
 */
@TypeConverter
public final class ToIterableTypeConverter {
    /**
     * Converts an array of reference type, T, to a <code>{@link Iterable<T>}</code>.
     *
     * @param values an array containing the list values to be converted to a new <code>{@link Iterable<T>}</code>.
     * @return an <code>{@link Iterable}</code> over the elements of the array values preserved in order of appearance, or null if the array was null.
     */
    @TypeConverter
    public static <T> Iterable<T> arrayOfReferenceTypeToIterableConversion(T[] values) {
        return values == null ? null : Arrays.asList(values);
    }

    /**
     * Converts an array of primitive byte to an <code>{@link Iterable<Byte>}</code>.
     *
     * @param values an array containing the list values to be converted to a new <code>{@link Iterable<Byte>}</code>.
     * @return an <code>{@link Iterable<Byte>}</code> over the elements of the array values preserved in order of appearance, or null if the array was null.
     */
    @TypeConverter
    public static Iterable<Byte> arrayOfPrimitiveByteTypeToIterableConversion(byte[] values) {
        return values == null ? null : () -> new Iterator<>() {
            private int currIdx = 0;

            @Override
            public boolean hasNext() {
                return currIdx < values.length;
            }

            @Override
            public Byte next() {
                return values[currIdx++];
            }
        };
    }

    /**
     * Converts an array of primitive boolean to an <code>{@link Iterable<Boolean>}</code>.
     *
     * @param values an array containing the list values to be converted to a new <code>{@link Iterable<Boolean>}</code>.
     * @return an <code>{@link Iterable<Boolean>}</code> over the elements of the array values preserved in order of appearance, or null if the array was null.
     */
    @TypeConverter
    public static Iterable<Boolean> arrayOfPrimitiveByteTypeToIterableConversion(boolean[] values) {
        return values == null ? null : () -> new Iterator<>() {
            private int currIdx = 0;

            @Override
            public boolean hasNext() {
                return currIdx < values.length;
            }

            @Override
            public Boolean next() {
                return values[currIdx++];
            }
        };
    }

    /**
     * Converts an array of primitive char to an <code>{@link Iterable<Character>}</code>.
     *
     * @param values an array containing the list values to be converted to a new <code>{@link Iterable<Character>}</code>.
     * @return an <code>{@link Iterable<Character>}</code> over the elements of the array values preserved in order of appearance, or null if the array was null.
     */
    @TypeConverter
    public static Iterable<Character> arrayOfPrimitiveByteTypeToIterableConversion(char[] values) {
        return values == null ? null : () -> new Iterator<>() {
            private int currIdx = 0;

            @Override
            public boolean hasNext() {
                return currIdx < values.length;
            }

            @Override
            public Character next() {
                return values[currIdx++];
            }
        };
    }

    /**
     * Converts an array of primitive double to an <code>{@link Iterable<Double>}</code>.
     *
     * @param values an array containing the list values to be converted to a new <code>{@link Iterable<Double>}</code>.
     * @return an <code>{@link Iterable<Double>}</code> over the elements of the array values preserved in order of appearance, or null if the array was null.
     */
    @TypeConverter
    public static Iterable<Double> arrayOfPrimitiveBooleanTypeToIterableConversion(double[] values) {
        return values == null ? null : () -> new Iterator<>() {
            private int currIdx = 0;

            @Override
            public boolean hasNext() {
                return currIdx < values.length;
            }

            @Override
            public Double next() {
                return values[currIdx++];
            }
        };
    }

    /**
     * Converts an array of primitive float to an <code>{@link Iterable<Float>}</code>.
     *
     * @param values an array containing the list values to be converted to a new <code>{@link Iterable<Float>}</code>.
     * @return an <code>{@link Iterable<Float>}</code> over the elements of the array values preserved in order of appearance, or null if the array was null.
     */
    @TypeConverter
    public static Iterable<Float> arrayOfPrimitiveFloatTypeToIterableConversion(float[] values) {
        return values == null ? null : () -> new Iterator<>() {
            private int currIdx = 0;

            @Override
            public boolean hasNext() {
                return currIdx < values.length;
            }

            @Override
            public Float next() {
                return values[currIdx++];
            }
        };
    }

    /**
     * Converts an array of primitive int to an <code>{@link Iterable<Integer>}</code>.
     *
     * @param values an array containing the list values to be converted to a new <code>{@link Iterable<Integer>}</code>.
     * @return an <code>{@link Iterable<Integer>}</code> over the elements of the array values preserved in order of appearance, or null if the array was null.
     */
    @TypeConverter
    public static Iterable<Integer> arrayOfPrimitiveIntTypeToIterableConversion(int[] values) {
        return values == null ? null : () -> new Iterator<>() {
            private int currIdx = 0;

            @Override
            public boolean hasNext() {
                return currIdx < values.length;
            }

            @Override
            public Integer next() {
                return values[currIdx++];
            }
        };
    }

    /**
     * Converts an array of primitive long to an <code>{@link Iterable<Long>}</code>.
     *
     * @param values an array containing the list values to be converted to a new <code>{@link Iterable<Long>}</code>.
     * @return an <code>{@link Iterable<Long>}</code> over the elements of the array values preserved in order of appearance, or null if the array was null.
     */
    @TypeConverter
    public static Iterable<Long> arrayOfPrimitiveLongTypeToIterableConversion(long[] values) {
        return values == null ? null : () -> new Iterator<>() {
            private int currIdx = 0;

            @Override
            public boolean hasNext() {
                return currIdx < values.length;
            }

            @Override
            public Long next() {
                return values[currIdx++];
            }
        };
    }

    /**
     * Converts an array of primitive short to an <code>{@link Iterable<Short>}</code>.
     *
     * @param values an array containing the list values to be converted to a new <code>{@link Iterable<Short>}</code>.
     * @return an <code>{@link Iterable<Short>}</code> over the elements of the array values preserved in order of appearance, or null if the array was null.
     */
    @TypeConverter
    public static Iterable<Short> arrayOfPrimitiveShortTypeToIterableConversion(short[] values) {
        return values == null ? null : () -> new Iterator<>() {
            private int currIdx = 0;

            @Override
            public boolean hasNext() {
                return currIdx < values.length;
            }

            @Override
            public Short next() {
                return values[currIdx++];
            }
        };
    }

    /**
     * Converts an array of an enumerated type, T, to an <code>{@link Iterable<Enum<T>>}</code>.
     *
     * @param values an array containing the list values to be converted to a new <code>{@link Iterable<Enum<T>>}</code>.
     * @return an <code>{@link Iterable<Enum<T>>}</code> over the elements of the array values preserved in order of appearance, or null if the array was null.
     */
    @TypeConverter
    public static <T extends Enum<T>> Iterable<Enum<T>> arrayOfPrimitiveByteTypeToIterableConversion(Enum<T>[] values) {
        return values == null ? null : () -> new Iterator<>() {
            private int currIdx = 0;

            @Override
            public boolean hasNext() {
                return currIdx < values.length;
            }

            @Override
            public Enum<T> next() {
                return values[currIdx++];
            }
        };
    }
}
