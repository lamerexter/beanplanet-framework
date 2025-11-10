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

package org.beanplanet.core.lang.conversion.system;

import org.junit.Test;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.beanplanet.core.lang.conversion.SystemTypeConverter.systemTypeConverter;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class StreamToCollectionConvertersTest {
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