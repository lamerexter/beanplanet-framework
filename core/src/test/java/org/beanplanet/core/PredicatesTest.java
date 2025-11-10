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

package org.beanplanet.core;

import org.junit.Test;

import static org.beanplanet.core.Predicates.falsePredicate;
import static org.beanplanet.core.Predicates.truePredicate;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Unit tests for @{@link Predicates}.
 */
public class PredicatesTest {
    @Test
    public void ctor() {
        assertThat(new Predicates(), notNullValue());
    }

    @Test
    public void truePredicateTest() {
        assertThat(truePredicate().test(new Object()), is(true));
    }

    @Test
    public void falsePredicateTest() {
        assertThat(falsePredicate().test(new Object()), is(false));
    }
}