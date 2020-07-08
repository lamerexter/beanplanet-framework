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

package org.beanplanet.core.streams;

import org.junit.Test;

import java.util.stream.Stream;

import static org.beanplanet.core.streams.StreamUtil.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class StreamUtilTest {
    @Test
    public void toPrimitiveBooleanArray() {
        assertThat(Stream.of(true, false, true, true).collect(toBooleanArray()), equalTo(new boolean[] { true, false, true, true}));
    }

    @Test
    public void toPrimitiveByteArray() {
        assertThat(Stream.of((byte)11, (byte)22, (byte)33).collect(toByteArray()), equalTo(new byte[] { 11, 22, 33 }));
    }

    @Test
    public void toPrimitiveCharArray() {
        assertThat(Stream.of('x', 'y', 'z').collect(toCharArray()), equalTo(new char[] { 'x', 'y', 'z' }));
    }

    @Test
    public void toPrimitiveDoubleArray() {
        assertThat(Stream.of(1d, 100d, 1000d ).collect(toDoubleArray()), equalTo(new double[] { 1d, 100d, 1000d }));
    }

    @Test
    public void toPrimitiveFloatArray() {
        assertThat(Stream.of(1f, 100f, 1000f ).collect(toFloatArray()), equalTo(new float[] { 1f, 100f, 1000f }));
;
    }

    @Test
    public void toPrimitiveIntArray() {
        assertThat(Stream.of(100, 200, 300).collect(toIntArray()), equalTo(new int[] { 100, 200, 300 }));
    }

    @Test
    public void toPrimitiveLongArray() {
        assertThat(Stream.of(100L, 200L, 300L).collect(toLongArray()), equalTo(new long[] { 100L, 200L, 300L }));
    }

    @Test
    public void toPrimitiveShortArray() {
        assertThat(Stream.of((short)11, (short)22, (short)33).collect(toShortArray()), equalTo(new short[] { 11, 22, 33 }));
    }
}