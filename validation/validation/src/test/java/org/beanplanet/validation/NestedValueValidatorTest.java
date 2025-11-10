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

package org.beanplanet.validation;

import org.beanplanet.testing.beans.TestBean;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class NestedValueValidatorTest {
    @Test
    public void givenANestedProperty_whenValidated_thenTheValidationResult() {
        // Given
        TestBean beanWithValidProperty = new TestBean("stringProperty");
        TestBean beanWithInvalidProperty = new TestBean("");
        NestedValueValidator<TestBean, String> validator = new NestedValueValidator<>(TestBean::getStringProperty,
                new NotEmptyValidator<>());

        // Then
        assertThat(validator.validate(beanWithValidProperty).hasErrors(), is(false));
        assertThat(validator.validate(beanWithInvalidProperty).hasErrors(), is(true));
    }
}