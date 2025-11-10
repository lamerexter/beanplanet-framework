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

import java.util.function.Function;
import java.util.function.Predicate;

import static org.beanplanet.messages.domain.MessageImpl.globalMessage;
import static org.beanplanet.messages.domain.MessagesImpl.messages;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CompositeValidator}.
 */
public class CompositeValidatorTest {
    @Test
    public void ctorValidators() {
        // Given
        Validator<Object> validator1 = mock(Validator.class), validator2 = mock(Validator.class);

        // When
        CompositeValidator<Object> composite = new CompositeValidator<>(validator1, validator2);

        // Then
        assertThat(composite.getCondition(), nullValue());
        assertThat(composite.getValueProvider(), nullValue());
        assertThat(composite.isStopOnFirstError(), is(false));
        assertThat(composite.getValidators(), notNullValue());
        assertThat(composite.getValidators(), equalTo(new Object[]{validator1, validator2}));
    }

    @Test
    public void ctorValueProviderExpressionAndValidators() {
        // Given
        Validator<Object> validator1 = mock(Validator.class), validator2 = mock(Validator.class);

        // When
        CompositeValidator<Object> composite = new CompositeValidator<>("theExpr",
                                                                        validator1, validator2);

        // Then
        assertThat(composite.getCondition(), nullValue());
        assertThat(composite.getValueProvider(), notNullValue());
        assertThat(composite.isStopOnFirstError(), is(false));
        assertThat(composite.getValidators(), notNullValue());
        assertThat(composite.getValidators(), equalTo(new Object[]{validator1, validator2}));
    }

    @Test
    public void ctorValueProviderAndValidators() {
        // Given
        Function<Object, Object> valueProvider = mock(Function.class);
        Validator<Object> validator1 = mock(Validator.class), validator2 = mock(Validator.class);

        // When
        CompositeValidator<Object> composite = new CompositeValidator<>(valueProvider,
                                                                        validator1, validator2);

        // Then
        assertThat(composite.getCondition(), nullValue());
        assertThat(composite.getValueProvider(), sameInstance(valueProvider));
        assertThat(composite.isStopOnFirstError(), is(false));
        assertThat(composite.getValidators(), notNullValue());
        assertThat(composite.getValidators(), equalTo(new Object[]{validator1, validator2}));
    }

    @Test
    public void ctorWithStopOnFirstAndValidators() {
        // Given
        Validator<Object> validator1 = mock(Validator.class), validator2 = mock(Validator.class);

        // When
        CompositeValidator<Object> composite = new CompositeValidator<>(true, validator1, validator2);

        // Then
        assertThat(composite.getCondition(), nullValue());
        assertThat(composite.getValueProvider(),  nullValue());
        assertThat(composite.isStopOnFirstError(), is(true));
        assertThat(composite.getValidators(), notNullValue());
        assertThat(composite.getValidators(), equalTo(new Object[]{validator1, validator2}));
    }

    @Test
    public void ctorWithStopOnFirstValueExpressionAndValidators() {
        // Given
        Validator<Object> validator1 = mock(Validator.class), validator2 = mock(Validator.class);

        // When
        CompositeValidator<Object> composite = new CompositeValidator<>(true,
                                                                        "theExpr",
                                                                        validator1, validator2);

        // Then
        assertThat(composite.getCondition(), nullValue());
        assertThat(composite.getValueProvider(),  notNullValue());
        assertThat(composite.isStopOnFirstError(), is(true));
        assertThat(composite.getValidators(), notNullValue());
        assertThat(composite.getValidators(), equalTo(new Object[]{validator1, validator2}));
    }

    @Test
    public void ctorWithStopOnFirstValueProviderAndValidators() {
        // Given
        Function<Object, Object> valueProvider = mock(Function.class);
        Validator<Object> validator1 = mock(Validator.class), validator2 = mock(Validator.class);

        // When
        CompositeValidator<Object> composite = new CompositeValidator<>(true,
                                                                        valueProvider,
                                                                        validator1, validator2);

        // Then
        assertThat(composite.getCondition(), nullValue());
        assertThat(composite.getValueProvider(),  sameInstance(valueProvider));
        assertThat(composite.isStopOnFirstError(), is(true));
        assertThat(composite.getValidators(), notNullValue());
        assertThat(composite.getValidators(), equalTo(new Object[]{validator1, validator2}));
    }

    @Test
    public void ctorWithConditionStopOnFirstAndValidators() {
        // Given
        Predicate<Object> condition = mock(Predicate.class);
        Validator<Object> validator1 = mock(Validator.class), validator2 = mock(Validator.class);

        // When
        CompositeValidator<Object> composite = new CompositeValidator<>(condition,
                                                                        true,
                                                                        validator1, validator2
        );

        // Then
        assertThat(composite.getCondition(), sameInstance(condition));
        assertThat(composite.getValueProvider(),  nullValue());
        assertThat(composite.isStopOnFirstError(), is(true));
        assertThat(composite.getValidators(), notNullValue());
        assertThat(composite.getValidators(), equalTo(new Object[]{validator1, validator2}));
    }

    @Test
    public void ctorWithConditionStopOnFirstValueExpressionAndValidators() {
        // Given
        Predicate<Object> condition = mock(Predicate.class);
        Validator<Object> validator1 = mock(Validator.class), validator2 = mock(Validator.class);

        // When
        CompositeValidator<Object> composite = new CompositeValidator<>(condition,
                                                                        true,
                                                                        "theExpr",
                                                                        validator1, validator2
        );

        // Then
        assertThat(composite.getCondition(), sameInstance(condition));
        assertThat(composite.getValueProvider(),  notNullValue());
        assertThat(composite.isStopOnFirstError(), is(true));
        assertThat(composite.getValidators(), notNullValue());
        assertThat(composite.getValidators(), equalTo(new Object[]{validator1, validator2}));
    }

    @Test
    public void ctorWithConditionStopOnFirstValueProviderAndValidators() {
        // Given
        Predicate<Object> condition = mock(Predicate.class);
        Function<Object, Object> valueProvider = mock(Function.class);
        Validator<Object> validator1 = mock(Validator.class), validator2 = mock(Validator.class);

        // When
        CompositeValidator<Object> composite = new CompositeValidator<>(condition,
                                                                        true,
                                                                        valueProvider,
                                                                        validator1, validator2
        );

        // Then
        assertThat(composite.getCondition(), sameInstance(condition));
        assertThat(composite.getValueProvider(),  sameInstance(valueProvider));
        assertThat(composite.isStopOnFirstError(), is(true));
        assertThat(composite.getValidators(), notNullValue());
        assertThat(composite.getValidators(), equalTo(new Object[]{validator1, validator2}));
    }

    @Test
    public void doValidateStopsOnFirstErrorWhenConfigured() {
        // Given
        Object validatedObject = new Object();
        Messages messages = messages();
        Validator<Object> validator1 = new FixedErrorValidator<Object>(globalMessage("theCode",
                                                                                     "theMessage"
                                                                                    ));
        Validator<Object> validator2 = mock(Validator.class);
        CompositeValidator<Object> composite = new CompositeValidator<>(true, validator1, validator2);

        // When
        Messages returnedMessages = composite.validate(validatedObject, messages);

        // Then
        assertThat(returnedMessages, sameInstance(messages));
        verifyNoMoreInteractions(validator2);
    }

    @Test
    public void doValidateDoesNotStopsOnFirstErrorWhenOnlyInfosOrWarningsAdded() {
        // Given
        Object validatedObject = new Object();
        Messages messages = messages();
        Validator<Object> validator1 = new AbstractValidator<Object>(globalMessage(
                "theCode",
                "theMessage"
                                                                                  )) {
            protected Messages doValidate(Object object, Messages messages) {
                messages
                        .addInfo("someInfoCode", "someInfoMessage")
                        .addWarning("someWarningCode", "someWarningMessage");
                return messages;
            }
        };
        Validator<Object> validator2 = mock(Validator.class);
        when(validator2.validate(any(), eq(messages))).thenReturn(messages);
        CompositeValidator<Object> composite = new CompositeValidator<>(true, validator1, validator2);

        // When
        Messages returnedMessages = composite.validate(validatedObject, messages);

        // Then
        assertThat(returnedMessages, sameInstance(messages));
        verify(validator2).validate(validatedObject, messages);
        verifyNoMoreInteractions(validator2);
    }
}