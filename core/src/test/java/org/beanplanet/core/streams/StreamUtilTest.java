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