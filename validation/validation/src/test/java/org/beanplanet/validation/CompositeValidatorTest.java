/*
 * Copyright (C) 2017 Beanplanet Ltd
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

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
        CompositeValidator<Object> composite = new CompositeValidator<>(true, validator1, validator2);

        // When
        Messages returnedMessages = composite.validate(validatedObject, messages);

        // Then
        assertThat(returnedMessages, sameInstance(messages));
        verify(validator2).validate(validatedObject, messages);
        verifyNoMoreInteractions(validator2);
    }
}