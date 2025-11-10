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

import static java.util.Arrays.asList;
import static org.beanplanet.core.lang.conversion.system.CollectionToCollectionTypeConverter.listToSetConversion;
import static org.beanplanet.core.lang.conversion.system.CollectionToCollectionTypeConverter.setToListConversion;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

public class CollectionToCollectionTypeConverterTest {
    @Test
    public void givenANullListOfValues_whenTheListIsConvertedToASet_thenNullIsReturned() {
        assertThat(listToSetConversion(null), nullValue());
    }

    @Test
    public void givenAListOfValues_whenTheListIsConvertedToASet_thenASetOfValuesIsReturnedInTheSameOrder() {
        assertThat(listToSetConversion(asList(5, 1, 2, 3, 2, 4)), equalTo(new LinkedHashSet<>(asList(5, 1, 2, 3, 2, 4))));
    }

    @Test
    public void givenANullSetOfValues_whenTheSetIsConvertedToAList_thenNullIsReturned() {
        assertThat(setToListConversion(null), nullValue());
    }

    @Test
    public void givenASetOfValues_whenTheSetIsConvertedToASet_thenAListOfValuesIsReturnedInTheSameOrder() {
        assertThat(setToListConversion(new LinkedHashSet<>(asList(5, 1, 2, 3, 2, 4))), equalTo(asList(5, 1, 2, 3, 4)));
    }
}