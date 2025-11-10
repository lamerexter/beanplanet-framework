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

import java.util.ArrayList;
import java.util.Arrays;

import static org.beanplanet.core.util.ListUtil.moveListElement;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ListUtilTest {
    @Test(expected = IndexOutOfBoundsException.class)
    public void moveListElement_emptyList() {
        moveListElement(new ArrayList<>(), 1, 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void moveListElement_whenListSourceIndexExceedsBounds_thenExceptionIsThrown() {
        moveListElement(new ArrayList<>(Arrays.asList("a", "b")), 2, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void moveListElement_whenListDestinationIndexExceedsBounds_thenExceptionIsThrown() {
        moveListElement(new ArrayList<>(Arrays.asList("a", "b")), 0, 2);
    }

    @Test
    public void moveListElement_whenIndicesAreWithinBounds_thenExceptionIsThrown() {
        assertThat(moveListElement(new ArrayList<>(Arrays.asList("a", "b")), 1,  0), equalTo(Arrays.asList("b", "a")));
    }
}