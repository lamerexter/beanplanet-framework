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

package org.beanplanet.core.lang.conversion.system;

import org.junit.Test;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.beanplanet.core.lang.conversion.SystemTypeConverter.systemTypeConverter;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class StreamToSequenceReductionConvertersTest {
    @Test
    public void streamToArray_TypeConversion() {
        assertThat(systemTypeConverter().convert(asList(1, 2, 3).stream(), Integer[].class), equalTo(new Integer[] { 1, 2, 3}));
    }

    @Test
    public void streamToList_TypeConversion() {
        assertThat(systemTypeConverter().convert(asList(1, 2, 3).stream(), List.class), equalTo(asList(1, 2, 3)));
    }

    @Test
    public void streamToSet_TypeConversion() {
        assertThat(systemTypeConverter().convert(asList(1, 2, 3).stream(), Set.class), equalTo(new LinkedHashSet<>(asList(1, 2, 3))));
    }
}