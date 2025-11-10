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
