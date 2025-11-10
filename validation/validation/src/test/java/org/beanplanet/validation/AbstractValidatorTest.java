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
 * Unit tests for {@link AbstractValidator}.
 */
public class AbstractValidatorTest {
    @Test
    public void ctorMessage() {
        // Given
        Message message = mock(Message.class);
        AbstractValidator<Object> validator = new AbstractValidator<Object>(message) {
            @Override
            protected Messages doValidate(Object object, Messages messages) {
                return null;
            }
        };

        // Then
        assertThat(validator.getCondition(), nullValue());
        assertThat(validator.getMessage(), sameInstance(message));
        assertThat(validator.getValueProvider(), nullValue());
    }

    @Test
    public void ctorCondition() {
        // Given
        Predicate<Object> condition = mock(Predicate.class);
        AbstractValidator<Object> validator = new AbstractValidator<Object>(condition) {
            @Override
            protected Messages doValidate(Object object, Messages messages) {
                return null;
            }
        };

        // Then
        assertThat(validator.getCondition(), sameInstance(condition));
        assertThat(validator.getMessage(), nullValue());
        assertThat(validator.getValueProvider(), nullValue());
    }

    @Test
    public void ctorConditionAndMessage() {
        // Given
        Predicate<Object> condition = mock(Predicate.class);
        Message message = mock(Message.class);
        AbstractValidator<Object> validator = new AbstractValidator<Object>(condition, message) {
            @Override
            protected Messages doValidate(Object object, Messages messages) {
                return null;
            }
        };

        // Then
        assertThat(validator.getCondition(), sameInstance(condition));
        assertThat(validator.getMessage(), sameInstance(message));
        assertThat(validator.getValueProvider(), nullValue());
    }

    @Test
    public void ctorMessageAndValueExpression() {
        // Given
        Message message = mock(Message.class);
        String valueExpression = "theValueExpression";
        AbstractValidator<Object> validator = new AbstractValidator<Object>(message, valueExpression) {
            @Override
            protected Messages doValidate(Object object, Messages messages) {
                return null;
            }
        };

        // Then
        assertThat(validator.getCondition(), nullValue());
        assertThat(validator.getMessage(), sameInstance(message));

        assertThat(validator.getValueProvider(), instanceOf(SpringBeanValueProvider.class));
        assertThat(((SpringBeanValueProvider) validator.getValueProvider()).getScript(), sameInstance(valueExpression));
    }

    @Test
    public void ctorMessageAndValueProvider() {
        // Given
        Message message = mock(Message.class);
        Function<Object, ?> valueProvider = mock(Function.class);
        AbstractValidator<Object> validator = new AbstractValidator<Object>(message, valueProvider) {
            @Override
            protected Messages doValidate(Object object, Messages messages) {
                return null;
            }
        };

        // Then
        assertThat(validator.getCondition(), nullValue());
        assertThat(validator.getMessage(), sameInstance(message));
        assertThat(validator.getValueProvider(), sameInstance(valueProvider));
    }

    @Test
    public void ctorConditionMessageAndValueExpression() {
        // Given
        Predicate<Object> condition = mock(Predicate.class);
        Message message = mock(Message.class);
        String valueExpression = "theValueExpression";
        AbstractValidator<Object> validator = new AbstractValidator<Object>(condition, message, valueExpression) {
            @Override
            protected Messages doValidate(Object object, Messages messages) {
                return null;
            }
        };

        // Then
        assertThat(validator.getCondition(), sameInstance(condition));
        assertThat(validator.getMessage(), sameInstance(message));

        assertThat(validator.getValueProvider(), instanceOf(SpringBeanValueProvider.class));
        assertThat(((SpringBeanValueProvider) validator.getValueProvider()).getScript(), sameInstance(valueExpression));
    }

    @Test
    public void ctorConditionMessageAndValueProvider() {
        // Given
        Predicate<Object> condition = mock(Predicate.class);
        Message message = mock(Message.class);
        Function<Object, ?> valueProvider = mock(Function.class);
        AbstractValidator<Object> validator = new AbstractValidator<Object>(condition, message, valueProvider) {
            @Override
            protected Messages doValidate(Object object, Messages messages) {
                return null;
            }
        };

        // Then
        assertThat(validator.getCondition(), sameInstance(condition));
        assertThat(validator.getMessage(), sameInstance(message));
        assertThat(validator.getValueProvider(), sameInstance(valueProvider));
    }

    @Test
    public void toStringTest() {
        // Given
        String valueExpression = "theValueExpression";
        Message message = mock(Message.class);
        AbstractValidator<Object> validator = new AbstractValidator<Object>(message, valueExpression) {
            @Override
            protected Messages doValidate(Object object, Messages messages) {
                return null;
            }
        };

        // Then
        assertThat(validator.toString(), containsString("condition="));
        assertThat(validator.toString(), containsString("message="));
        assertThat(validator.toString(), containsString("valueProvider="));
    }
}