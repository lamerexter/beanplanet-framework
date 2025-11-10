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