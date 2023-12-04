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
