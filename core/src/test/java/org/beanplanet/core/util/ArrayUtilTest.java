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
}