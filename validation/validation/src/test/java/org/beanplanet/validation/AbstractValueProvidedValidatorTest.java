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

import org.beanplanet.messages.domain.Message;
import org.beanplanet.messages.domain.Messages;
import org.beanplanet.testing.beans.TestBean;
import org.junit.Test;

import java.util.function.Function;
import java.util.function.Predicate;

import static org.beanplanet.messages.domain.MessagesImpl.messages;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for {@link AbstractValueProvidedValidator}.
 */
public class AbstractValueProvidedValidatorTest {
    @Test
    public void ctorNoArgs() {
        // Given
        Message message = mock(Message.class);
        AbstractValueProvidedValidator<Object> validator = new AbstractValueProvidedValidator<Object>() {
            @Override
            protected Messages doValidate(Object object, Messages messages) {
                return null;
            }
        };

        // Then
        assertThat(validator.getCondition(), nullValue());
        assertThat(validator.getValueProvider(), nullValue());
    }

    @Test
    public void ctorCondition() {
        // Given
        Predicate<Object> condition = mock(Predicate.class);
        AbstractValueProvidedValidator<Object> validator = new AbstractValueProvidedValidator<Object>(condition) {
            @Override
            protected Messages doValidate(Object object, Messages messages) {
                return null;
            }
        };

        // Then
        assertThat(validator.getCondition(), sameInstance(condition));
        assertThat(validator.getValueProvider(), nullValue());
    }

    @Test
    public void ctorValueExpression() {
        // Given
        String valueExpression = "theValueExpression";
        AbstractValueProvidedValidator<Object> validator = new AbstractValueProvidedValidator<Object>(valueExpression) {
            @Override
            protected Messages doValidate(Object object, Messages messages) {
                return null;
            }
        };

        // Then
        assertThat(validator.getCondition(), nullValue());

        assertThat(validator.getValueProvider(), instanceOf(SpringBeanValueProvider.class));
        assertThat(((SpringBeanValueProvider) validator.getValueProvider()).getScript(), sameInstance(valueExpression));
    }

    @Test
    public void ctorValueProvider() {
        // Given
        Function<Object, ?> valueProvider = mock(Function.class);
        AbstractValueProvidedValidator<Object> validator = new AbstractValueProvidedValidator<Object>(valueProvider) {
            @Override
            protected Messages doValidate(Object object, Messages messages) {
                return null;
            }
        };

        // Then
        assertThat(validator.getCondition(), nullValue());
        assertThat(validator.getValueProvider(), sameInstance(valueProvider));
    }

    @Test
    public void ctorConditionAndValueExpression() {
        // Given
        Predicate<Object> condition = mock(Predicate.class);
        String valueExpression = "theValueExpression";
        AbstractValueProvidedValidator<Object> validator = new AbstractValueProvidedValidator<Object>(condition, valueExpression) {
            @Override
            protected Messages doValidate(Object object, Messages messages) {
                return null;
            }
        };

        // Then
        assertThat(validator.getCondition(), sameInstance(condition));

        assertThat(validator.getValueProvider(), instanceOf(SpringBeanValueProvider.class));
        assertThat(((SpringBeanValueProvider) validator.getValueProvider()).getScript(), sameInstance(valueExpression));
    }

    @Test
    public void ctorConditionAndValueProvider() {
        // Given
        Predicate<Object> condition = mock(Predicate.class);
        Function<Object, ?> valueProvider = mock(Function.class);
        AbstractValueProvidedValidator<Object> validator = new AbstractValueProvidedValidator<Object>(condition, valueProvider) {
            @Override
            protected Messages doValidate(Object object, Messages messages) {
                return null;
            }
        };

        // Then
        assertThat(validator.getCondition(), sameInstance(condition));
        assertThat(validator.getValueProvider(), sameInstance(valueProvider));
    }

    @Test
    public void getValidationValueExpression() {
        // Given
        TestBean testBean = new TestBean("theStringProperty");
        String valueExpression = "stringProperty";
        final Object theValueExpressionValue[] = {null};
        AbstractValueProvidedValidator<TestBean> validator = new AbstractValueProvidedValidator<TestBean>(valueExpression) {
            @Override
            protected Messages doValidate(TestBean object, Messages messages) {
                theValueExpressionValue[0] = getValidationValue(object);
                return null;
            }
        };

        // When
        validator.validate(testBean, messages());

        // Then
        assertThat(theValueExpressionValue[0], equalTo(testBean.getStringProperty()));
    }

    @Test
    public void getValidationValueExpressionAsString() {
        // Given
        TestBean testBean = new TestBean();
        testBean.setLongProperty(12345678L);
        String valueExpression = "longProperty";
        final Object theValueExpressionValue[] = {null};
        AbstractValueProvidedValidator<TestBean> validator = new AbstractValueProvidedValidator<TestBean>(valueExpression) {
            @Override
            protected Messages doValidate(TestBean object, Messages messages) {
                theValueExpressionValue[0] = getValidationValueAsString(object);
                return null;
            }
        };

        // When
        validator.validate(testBean, messages());

        // Then
        assertThat(theValueExpressionValue[0], equalTo(String.valueOf(testBean.getLongProperty())));
    }
}