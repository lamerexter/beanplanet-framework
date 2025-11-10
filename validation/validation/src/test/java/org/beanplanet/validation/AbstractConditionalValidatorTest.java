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

import org.beanplanet.messages.domain.Messages;
import org.junit.Test;

import java.util.function.Predicate;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link AbstractConditionalValidator}.
 */
public class AbstractConditionalValidatorTest {
    @Test
    public void ctorNoArgs() {
        assertThat(new AbstractConditionalValidator<Object>() {
            @Override
            protected Messages doValidate(Object object, Messages messages) {
                return null;
            }
        }.getCondition(), nullValue());
    }

    @Test
    public void ctorCondition() {
        // Given
        Predicate<Object> condition = mock(Predicate.class);

        // Then
        assertThat(new AbstractConditionalValidator<Object>(condition) {
            @Override
            protected Messages doValidate(Object object, Messages messages) {
                return null;
            }
        }.getCondition(), sameInstance(condition));
    }

    @Test
    public void validateWithNullPredicate() {
        // Given
        Messages messages = mock(Messages.class);
        Messages messagesReturnedFromValidate = mock(Messages.class);
        final boolean hasValidated[] = { false };
        AbstractConditionalValidator<Object> validator = new AbstractConditionalValidator<Object>() {
            @Override
            protected Messages doValidate(Object object, Messages messages) {
                hasValidated[0] = true;
                return messagesReturnedFromValidate;
            }
        };

        // When
        Messages returnedMessages = validator.validate(new Object(), messages);

        // Then
        assertThat(hasValidated[0], is(true));
        assertThat(returnedMessages, sameInstance(messagesReturnedFromValidate));
        verifyNoMoreInteractions(messages);
    }

    @Test
    public void validateWithTrueCondition() {
        // Given
        Object testObject = new Object();
        Predicate<Object> condition = mock(Predicate.class);
        when(condition.test(testObject)).thenReturn(true);
        Messages messages = mock(Messages.class);
        final boolean hasValidated[] = { false };
        AbstractConditionalValidator<Object> validator = new AbstractConditionalValidator<Object>(condition) {
            @Override
            protected Messages doValidate(Object object, Messages messages) {
                hasValidated[0] = true;
                return messages;
            }
        };

        // When
        Messages returnedMessages = validator.validate(testObject, messages);

        // Then
        assertThat(hasValidated[0], is(true));
        assertThat(returnedMessages, sameInstance(messages));
        verify(condition).test(testObject);
        verifyNoMoreInteractions(condition, messages);
    }

    @Test
    public void validateWithFalseCondition() {
        // Given
        Object testObject = new Object();
        Predicate<Object> condition = mock(Predicate.class);
        when(condition.test(testObject)).thenReturn(false);
        Messages messages = mock(Messages.class);
        final boolean hasValidated[] = { false };
        AbstractConditionalValidator<Object> validator = new AbstractConditionalValidator<Object>(condition) {
            @Override
            protected Messages doValidate(Object object, Messages messages) {
                hasValidated[0] = true;
                return messages;
            }
        };

        // When
        Messages returnedMessages = validator.validate(testObject, messages);

        // Then
        assertThat(hasValidated[0], is(false));
        assertThat(returnedMessages, sameInstance(messages));
        verify(condition).test(testObject);
        verifyNoMoreInteractions(condition, messages);
    }

    @Test
    public void toStringTest() {
        Predicate<Object> condition = mock(Predicate.class);
        assertThat(new AbstractConditionalValidator<Object>(condition) {
            @Override
            protected Messages doValidate(Object object, Messages messages) {
                return messages;
            }
        }.toString(), containsString("condition="));
    }
}