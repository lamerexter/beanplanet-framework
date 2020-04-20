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

import org.junit.Test;

import static org.beanplanet.core.lang.TypeUtil.determineCommonSuperclass;
import static org.hamcrest.CoreMatchers.*;
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
}