/*
 *  MIT Licence:
 *
 *  Copyright (C) 2020 Beanplanet Ltd
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

package org.beanplanet.core.lang;

import org.beanplanet.core.lang.conversion.TypeConverter;
import org.junit.Test;

import static org.beanplanet.core.lang.TypeUtil.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class TypeUtilTest {
    @Test
    public void determineCommonSuperclass_autoboxing_withNull_returnsCannotBeDetermined() {
        assertThat(determineCommonSuperclass((Class<?>[])null), nullValue());
    }

    @Test
    public void determineCommonSuperclass_autoboxing_withEmpty_returnsCannotBeDetermined() {
        assertThat(determineCommonSuperclass((Class<?>[])null), nullValue());
    }

    @Test
    public void determineCommonSuperclass_autoboxing_withSingleClass_returnsSingleClass() {
        assertThat(determineCommonSuperclass(int.class), equalTo(int.class));

        assertThat(determineCommonSuperclass(Object.class), equalTo(Object.class));
        assertThat(determineCommonSuperclass(Integer.class), equalTo(Integer.class));
    }

    @Test
    public void determineCommonSuperclass_autoboxing_withMutipleClassesOfSameType_returnsSameType() {
        assertThat(determineCommonSuperclass(int.class, int.class), equalTo(int.class));

        assertThat(determineCommonSuperclass(Object.class, Object.class), equalTo(Object.class));
        assertThat(determineCommonSuperclass(Integer.class, Integer.class, Integer.class), equalTo(Integer.class));
    }

    @Test
    public void determineCommonSuperclass_autoboxing_withMutipleClassesHavingCommonSuperclass_returnsCommopnSuperclass() {
        assertThat(determineCommonSuperclass(Integer.class, Long.class, Double.class), equalTo(Number.class));
    }

    @Test
    public void determineCommonSuperclass_autoboxing_withMixedPrimitiveAndPrimitiveWrapperClasses_returnsCannotBeDetermined() {
        assertThat(determineCommonSuperclass(Integer.class, int.class), equalTo(Integer.class));
    }

    @Test
    public void determineCommonSuperclass_noboxing_withNull_returnsCannotBeDetermined() {
        assertThat(determineCommonSuperclass(false, (Class<?>[])null), nullValue());
    }

    @Test
    public void determineCommonSuperclass_noboxing_withEmpty_returnsCannotBeDetermined() {
        assertThat(determineCommonSuperclass(false, (Class<?>[])null), nullValue());
    }

    @Test
    public void determineCommonSuperclass_noboxing_withSingleClass_returnsSingleClass() {
        assertThat(determineCommonSuperclass(false, int.class), equalTo(int.class));

        assertThat(determineCommonSuperclass(false, Object.class), equalTo(Object.class));
        assertThat(determineCommonSuperclass(false, Integer.class), equalTo(Integer.class));
    }

    @Test
    public void determineCommonSuperclass_noboxing_withMutipleClassesOfSameType_returnsSameType() {
        assertThat(determineCommonSuperclass(false, int.class, int.class), equalTo(int.class));

        assertThat(determineCommonSuperclass(false, Object.class, Object.class), equalTo(Object.class));
        assertThat(determineCommonSuperclass(false, Integer.class, Integer.class, Integer.class), equalTo(Integer.class));
    }

    @Test
    public void determineCommonSuperclass_noboxing_withMutipleClassesHavingCommonSuperclass_returnsCommopnSuperclass() {
        assertThat(determineCommonSuperclass(false, Integer.class, Long.class, Double.class), equalTo(Number.class));
    }

    @Test
    public void determineCommonSuperclass_noboxing_withMixedPrimitiveAndPrimitiveWrapperClasses_returnsCannotBeDetermined() {
        assertThat(determineCommonSuperclass(false, Integer.class, int.class), nullValue());
    }

    @Test
    public void determineArrayDimensions_nullType() {
        assertThat(determineArrayDimensions(null), is(0));
    }

    @Test
    public void determineArrayDimensions_nonArrayType() {
        assertThat(determineArrayDimensions(boolean.class), is(0));
        assertThat(determineArrayDimensions(Object.class), is(0));
    }

    @Test
    public void determineArrayDimensions_singleDimensionType() {
        assertThat(determineArrayDimensions(boolean[].class), is(1));
        assertThat(determineArrayDimensions(Object[].class), is(1));
    }

    @Test
    public void determineArrayDimensions_multipleDimensionTypes() {
        assertThat(determineArrayDimensions(int[][].class), is(2));
        assertThat(determineArrayDimensions(Long[][].class), is(2));

        assertThat(determineArrayDimensions(float[][][][][].class), is(5));
        assertThat(determineArrayDimensions(Double[][][][][].class), is(5));
    }

    @Test
    public void getForNameTypeDescription_forPrimitive() {
        assertThat(getForNameTypeDescription(byte.class, 0), equalTo("B"));
        assertThat(getForNameTypeDescription(boolean.class, 0), equalTo("Z"));
        assertThat(getForNameTypeDescription(char.class, 0), equalTo("C"));
        assertThat(getForNameTypeDescription(double.class, 0), equalTo("D"));
        assertThat(getForNameTypeDescription(float.class, 0), equalTo("F"));
        assertThat(getForNameTypeDescription(int.class, 0), equalTo("I"));
        assertThat(getForNameTypeDescription(long.class, 0), equalTo("J"));
        assertThat(getForNameTypeDescription(short.class, 0), equalTo("S"));
    }

    @Test
    public void getForNameTypeDescription_forPrimitiveArray() {
        assertThat(getForNameTypeDescription(byte.class, 1), equalTo("[B"));
        assertThat(getForNameTypeDescription(boolean.class, 1), equalTo("[Z"));
        assertThat(getForNameTypeDescription(char.class, 1), equalTo("[C"));
        assertThat(getForNameTypeDescription(double.class, 1), equalTo("[D"));
        assertThat(getForNameTypeDescription(float.class, 1), equalTo("[F"));
        assertThat(getForNameTypeDescription(int.class, 1), equalTo("[I"));
        assertThat(getForNameTypeDescription(long.class, 1), equalTo("[J"));
        assertThat(getForNameTypeDescription(short.class, 1), equalTo("[S"));

        assertThat(getForNameTypeDescription(byte[].class, 1), equalTo("[[B"));
    }

    @Test
    public void getForNameTypeDescription_forPrimitiveMultiDimensionalArray() {
        assertThat(getForNameTypeDescription(byte.class, 3), equalTo("[[[B"));
        assertThat(getForNameTypeDescription(boolean.class, 3), equalTo("[[[Z"));
        assertThat(getForNameTypeDescription(char.class, 3), equalTo("[[[C"));
        assertThat(getForNameTypeDescription(double.class, 3), equalTo("[[[D"));
        assertThat(getForNameTypeDescription(float.class, 3), equalTo("[[[F"));
        assertThat(getForNameTypeDescription(int.class, 3), equalTo("[[[I"));
        assertThat(getForNameTypeDescription(long.class, 3), equalTo("[[[J"));
        assertThat(getForNameTypeDescription(short.class, 3), equalTo("[[[S"));
    }

    @Test
    public void getForNameTypeDescription_forObject() {
        assertThat(getForNameTypeDescription(Object.class, 0), equalTo("Ljava.lang.Object;"));
        assertThat(getForNameTypeDescription(TypeConverter.class, 0), equalTo("L" + TypeConverter.class.getName() + ";"));
    }

    @Test
    public void getForNameTypeDescription_forObjectArray() {
        assertThat(getForNameTypeDescription(Object.class, 1), equalTo("[Ljava.lang.Object;"));
        assertThat(getForNameTypeDescription(TypeConverter.class, 1), equalTo("[L" + TypeConverter.class.getName() + ";"));
    }

    @Test
    public void getForNameTypeDescription_forPrimitiveObjectArray() {
        assertThat(getForNameTypeDescription(Object.class, 3), equalTo("[[[Ljava.lang.Object;"));
        assertThat(getForNameTypeDescription(TypeConverter.class, 3), equalTo("[[[L" + TypeConverter.class.getName() + ";"));
    }


    @Test
    public void forName_forPrimitive() {
        assertThat(forName(byte.class, 0), equalTo((Class) byte.class));
        assertThat(forName(boolean.class, 0), equalTo((Class) boolean.class));
        assertThat(forName(char.class, 0), equalTo((Class) char.class));
        assertThat(forName(double.class, 0), equalTo((Class) double.class));
        assertThat(forName(float.class, 0), equalTo((Class) float.class));
        assertThat(forName(int.class, 0), equalTo((Class) int.class));
        assertThat(forName(long.class, 0), equalTo((Class) long.class));
        assertThat(forName(short.class, 0), equalTo((Class) short.class));
    }

    @Test
    public void forName_forPrimitiveArray() {
        assertThat(forName(byte.class, 1), equalTo((Class) byte[].class));
        assertThat(forName(boolean.class, 1), equalTo((Class) boolean[].class));
        assertThat(forName(char.class, 1), equalTo((Class) char[].class));
        assertThat(forName(double.class, 1), equalTo((Class) double[].class));
        assertThat(forName(float.class, 1), equalTo((Class) float[].class));
        assertThat(forName(int.class, 1), equalTo((Class) int[].class));
        assertThat(forName(long.class, 1), equalTo((Class) long[].class));
        assertThat(forName(short.class, 1), equalTo((Class) short[].class));

        assertThat(forName(byte[].class, 1), equalTo((Class) byte[][].class));
    }

    @Test
    public void forName_forPrimitiveMultiDimensionalArray() {
        assertThat(forName(byte.class, 3), equalTo((Class) byte[][][].class));
        assertThat(forName(boolean.class, 3), equalTo((Class) boolean[][][].class));
        assertThat(forName(char.class, 3), equalTo((Class) char[][][].class));
        assertThat(forName(double.class, 3), equalTo((Class) double[][][].class));
        assertThat(forName(float.class, 3), equalTo((Class) float[][][].class));
        assertThat(forName(int.class, 3), equalTo((Class) int[][][].class));
        assertThat(forName(long.class, 3), equalTo((Class) long[][][].class));
        assertThat(forName(short.class, 3), equalTo((Class) short[][][].class));
    }

    @Test
    public void forName_forNameForObject() {
        assertThat(forName(Object.class, 0), equalTo((Class) Object.class));
        assertThat(forName(TypeConverter.class, 0), equalTo((Class) TypeConverter.class));
    }

    @Test
    public void forName_forNameForObjectArray() {
        assertThat(forName(Object.class, 1), equalTo((Class) Object[].class));
        assertThat(forName(TypeConverter.class, 1), equalTo((Class) TypeConverter[].class));
    }

    @Test
    public void forName_forNameForPrimitiveObjectArray() {
        assertThat(forName(Object.class, 3), equalTo((Class) Object[][][].class));
        assertThat(forName(TypeConverter.class, 3), equalTo((Class) TypeConverter[][][].class));
    }
}