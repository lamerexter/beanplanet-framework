/*
 *  MIT Licence:
 *
 *  Copyright (C) 2018 Beanplanet Ltd
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without restriction
 *  including without limitation the rights to use, copy, modify, merge,
 *  publish, distribute, sublicense, and/or sell copies of the Software,
 *  and to permit persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 *  KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 *  PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 *  CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  DEALINGS IN THE SOFTWARE.
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