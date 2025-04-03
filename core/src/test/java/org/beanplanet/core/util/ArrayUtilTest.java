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

import org.junit.Test;

import static org.beanplanet.core.util.ArrayUtil.concat;
import static org.beanplanet.core.util.ArrayUtil.determineArraySize;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.CombinableMatcher.both;
import static org.junit.Assert.assertThat;

/**
 * Unit tests for {@link ArrayUtil}.
 */
public class ArrayUtilTest {
    @Test
    public void isEmptyOrNull() {
        assertThat(ArrayUtil.isEmptyOrNull(null), is(true));
        assertThat(ArrayUtil.isEmptyOrNull(new String[0]), is(true));
        assertThat(ArrayUtil.isEmptyOrNull(new String[] {"Hello"}), is(false));
    }

    @Test
    public void nullSafe() {
        String arr[] = { "Hello", "World" };
        assertThat(ArrayUtil.nullSafe(String.class, arr), sameInstance(arr));
        assertThat(ArrayUtil.nullSafe(String.class, null), both(notNullValue()).and(equalTo(new String[0])));
    }

    @Test
    public void emptyArray() {
        String[] emptyArray = (String[])ArrayUtil.emptyArray(String.class);
        assertThat(emptyArray, notNullValue());
        assertThat(emptyArray.length, equalTo(0));
    }

    @Test
    public void emptyArrayDimensions() {
        Object emptyArray = ArrayUtil.emptyArray(String.class, 2);
        assertThat(emptyArray, notNullValue());
        assertThat(emptyArray, instanceOf(String[][].class));

        String[][] empty2Array = (String[][])emptyArray;
        assertThat(empty2Array.length, equalTo(0));
    }

    @Test
    public void determineArraySize_null() {
        assertThat(determineArraySize(null), equalTo(0));
    }

    @Test
    public void determineArraySize_notAnArray() {
        assertThat(determineArraySize("This is not an array!"), equalTo(0));
    }

    @Test
    public void determineArraySize_primitiveComponentType() {
        assertThat(determineArraySize(new boolean[111]), equalTo(111));
        assertThat(determineArraySize(new byte[222]), equalTo(222));
        assertThat(determineArraySize(new char[333]), equalTo(333));
        assertThat(determineArraySize(new double[444]), equalTo(444));
        assertThat(determineArraySize(new float[555]), equalTo(555));
        assertThat(determineArraySize(new int[666]), equalTo(666));
        assertThat(determineArraySize(new long[777]), equalTo(777));
        assertThat(determineArraySize(new short[888]), equalTo(888));
    }

    @Test
    public void determineArraySize_referenceTypeComponent() {
        assertThat(determineArraySize(new Boolean[111]), equalTo(111));
    }

    @Test
    public void determineArraySize_fromMultiDimension_referenceTypeComponent() {
        final Boolean[][][] array = new Boolean[2][22][222];

        assertThat(determineArraySize(array), equalTo(2));
        assertThat(determineArraySize(array, 1), equalTo(2));
        assertThat(determineArraySize(array, 2), equalTo(22));
        assertThat(determineArraySize(array, 3), equalTo(222));
    }

    @Test
    public void concat_null_null() {
        assertThat(concat(null, null), nullValue());
    }

    @Test
    public void concat_nonNull_null() {
        final Integer[] arr = new Integer[] { 1,2,3 };
        assertThat(concat(arr, null), sameInstance(arr));
    }

    @Test
    public void concat_null_nonNull() {
        final Integer[] arr = new Integer[] { 1,2,3 };
        assertThat(concat(null, arr), sameInstance(arr));
    }

    @Test
    public void concat_nonNull_nonNull() {
        final Integer[] left = new Integer[] { 1,2,3 };
        final Integer[] right = new Integer[] { 4,5,6 };
        assertThat(concat(left, right), equalTo(new Integer[] { 1,2,3,4,5,6 }));
    }
}