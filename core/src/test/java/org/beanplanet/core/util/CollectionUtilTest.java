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

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class CollectionUtilTest {
    @Test
    public void isNotNullOrEmpty() {
        assertThat(CollectionUtil.isNotNullOrEmpty(asList("Hello", "World")), is(true));
    }

    @Test
    public void lastOrNull_nulllist() {
        assertThat(CollectionUtil.lastOrNull(null), nullValue());
    }

    @Test
    public void lastOrNull_emptylist() {
        assertThat(CollectionUtil.lastOrNull(Collections.emptyList()), nullValue());
    }

    @Test
    public void lastOrNull() {
        assertThat(CollectionUtil.lastOrNull(asList(1)), equalTo(1));
        assertThat(CollectionUtil.lastOrNull(asList(1, 2, 3)), equalTo(3));
    }

    @Test
    public void toFloatList_null() {
        assertThat(CollectionUtil.toList(null), nullValue());
    }

    @Test
    public void toFloatList_notNull() {
        assertThat(CollectionUtil.toList(new float[0]), equalTo(Collections.emptyList()));
        assertThat(CollectionUtil.toList(new float[]{ 1, 2, 3}), equalTo(List.of(1f, 2f, 3f)));
    }
}