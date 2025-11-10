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

/**
 * Conversions between arrays whose component types are either primitive or primitive wrapper types (e.g. int[] <-> Integer[])
 */
@TypeConverter
public final class ArrayBoxingTypeConverters {

    /**
     * Converts a primitive integer array to its boolean wrapper array counterpart.
     *
     * @param value the array to be converted.
     * @return an array of {@link Boolean} containing the same elements as source array, or null if the array provided was null.
     */
    @TypeConverter
    public static Boolean[] primitiveBooleanArrayToBooleanWrapperArray(boolean[] value) {
        if (value == null) return null;

        Boolean[] result = new Boolean[value.length];
        for (int n=0; n < value.length; n++) {
            result[n] = value[n];
        }

        return result;
    }

    /**
     * Converts a primitive byte array to its byte wrapper array counterpart.
     *
     * @param value the array to be converted.
     * @return an array of {@link Byte} containing the same elements as source array, or null if the array provided was null.
     */
    @TypeConverter
    public static Byte[] primitiveByteArrayToByteWrapperArray(byte[] value) {
        if (value == null) return null;

        Byte[] result = new Byte[value.length];
        for (int n=0; n < value.length; n++) {
            result[n] = value[n];
        }

        return result;
    }

    /**
     * Converts a primitive char array to its character wrapper array counterpart.
     *
     * @param value the array to be converted.
     * @return an array of {@link Character} containing the same elements as source array, or null if the array provided was null.
     */
    @TypeConverter
    public static Character[] primitiveCharArrayToCharacterWrapperArray(char[] value) {
        if (value == null) return null;

        Character[] result = new Character[value.length];
        for (int n=0; n < value.length; n++) {
            result[n] = value[n];
        }

        return result;
    }

    /**
     * Converts a primitive double array to its double wrapper array counterpart.
     *
     * @param value the array to be converted.
     * @return an array of {@link Double} containing the same elements as source array, or null if the array provided was null.
     */
    @TypeConverter
    public static Double[] primitiveDoubleArrayToDoubleWrapperArray(double[] value) {
        return value == null ? null : Arrays.stream(value).boxed().toArray(Double[]::new);
    }

    /**
     * Converts a primitive float array to its float wrapper array counterpart.
     *
     * @param value the array to be converted.
     * @return an array of {@link Float} containing the same elements as source array, or null if the array provided was null.
     */
    @TypeConverter
    public static Float[] primitiveFloatArrayToFloatWrapperArray(float[] value) {
        if (value == null) return null;

        Float[] result = new Float[value.length];
        for (int n=0; n < value.length; n++) {
            result[n] = value[n];
        }

        return result;
    }

    /**
     * Converts a primitive integer array to its integer wrapper counterpart.
     *
     * @param value the array to be converted.
     * @return an array of {@link Integer} containing the same numbers, or null if the array provided was null.
     */
    @TypeConverter
    public static Integer[] primitiveIntArrayToIntegerWrapperArray(int[] value) {
        return value == null ? null : Arrays.stream(value).boxed().toArray(Integer[]::new);
    }

    /**
     * Converts a primitive long array to its long wrapper counterpart.
     *
     * @param value the array to be converted.
     * @return an array of {@link Long} containing the same numbers, or null if the array provided was null.
     */
    @TypeConverter
    public static Long[] primitiveLongArrayToLongWrapperArray(long[] value) {
        return value == null ? null : Arrays.stream(value).boxed().toArray(Long[]::new);
    }

    /**
     * Converts a primitive short array to its short wrapper array counterpart.
     *
     * @param value the array to be converted.
     * @return an array of {@link Short} containing the same elements as source array, or null if the array provided was null.
     */
    @TypeConverter
    public static Short[] primitiveFloatArrayToFloatWrapperArray(short[] value) {
        if (value == null) return null;

        Short[] result = new Short[value.length];
        for (int n=0; n < value.length; n++) {
            result[n] = value[n];
        }

        return result;
    }
}
