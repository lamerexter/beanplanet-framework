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

package org.beanplanet.core.codec;

import org.beanplanet.core.io.IoException;
import org.junit.Test;

import static org.beanplanet.core.codec.Hex.HEXDIGITS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

public class HexTest {
    @Test
    public void encodeToHexString() {
        assertThat(Hex.encodeToHexString(null), nullValue());

        assertThat(Hex.encodeToHexString(new byte[0]), equalTo(""));

        assertThat(Hex.encodeToHexString("Hello World".getBytes()), equalTo("48656c6c6f20576f726c64"));
    }

    @Test
    public void decodeFromHexString() {
        assertThat(Hex.decodeFromHexString(null), nullValue());

        assertThat(new String(Hex.decodeFromHexString("48656c6c6f20576f726c64")), equalTo("Hello World"));
        assertThat(new String(Hex.decodeFromHexString("48656c6c6f20576f726c64".toUpperCase())), equalTo("Hello World"));
        assertThat(new String(Hex.decodeFromHexString("48656C6c6f20576F726c64")), equalTo("Hello World"));
    }

    @Test(expected = IoException.class)
    public void decodeFromHexString_whenInputTooSmall_throwsError() {
        Hex.decodeFromHexString("4");
    }

    @Test
    public void hexDigit() {
        for (int c=0; c < HEXDIGITS.length; c++) {
            assertThat(Hex.hexDigit(c), equalTo(HEXDIGITS[c]));
        }
    }
}