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

import org.beanplanet.messages.domain.MessageImpl;
import org.beanplanet.messages.domain.Messages;
import org.beanplanet.messages.domain.MessagesImpl;
import org.junit.Test;

import java.util.function.Predicate;

import static org.beanplanet.core.Predicates.falsePredicate;
import static org.beanplanet.core.Predicates.truePredicate;
import static org.beanplanet.messages.domain.MessageImpl.fieldMessage;
import static org.beanplanet.messages.domain.MessageImpl.globalMessage;
import static org.beanplanet.messages.domain.MessagesImpl.messages;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Unit test for @{@link FixedErrorValidator}.
 */
public class FixedErrorValidatorTest {
    @Test
    public void ctorMessage() {
        // Given
        MessageImpl message = globalMessage("theCode",
                                            "theMessage"
                                           );
        FixedErrorValidator<Object> validator = new FixedErrorValidator<>(message);

        // When
        validator.validate(new Object(), messages());

        // Then
        assertThat(validator.getCondition(), sameInstance(truePredicate()));
        assertThat(validator.getMessage(), sameInstance(message));
        assertThat(validator.getValueProvider(), nullValue());
    }

    @Test
    public void ctorMessageCodeAndMessage() {
        // Given
        MessageImpl message = globalMessage("theCode",
                                            "theMessage"
                                           );
        FixedErrorValidator<Object> validator = new FixedErrorValidator<>(message.getCode(),
                                                                          message.getParameterisedMessage());

        // When
        validator.validate(new Object(), messages());

        // Then
        assertThat(validator.getCondition(), sameInstance(truePredicate()));
        assertThat(validator.getMessage(), not(sameInstance(message)));
        assertThat(validator.getMessage(), equalTo(message));
        assertThat(validator.getValueProvider(), nullValue());
    }

    @Test
    public void ctorFieldMessageCodeAndMessage() {
        // Given
        MessageImpl message = fieldMessage("theField",
                                           "theCode",
                                            "theMessage"
                                           );
        FixedErrorValidator<Object> validator = new FixedErrorValidator<>(message.getField(),
                                                                          message.getCode(),
                                                                          message.getParameterisedMessage());

        // When
        validator.validate(new Object(), messages());

        // Then
        assertThat(validator.getCondition(), sameInstance(truePredicate()));
        assertThat(validator.getMessage(), not(sameInstance(message)));
        assertThat(validator.getMessage(), equalTo(message));
        assertThat(validator.getValueProvider(), nullValue());
    }

    @Test
    public void ctorConditionMessage() {
        // Given
        MessageImpl message = globalMessage("theCode",
                                            "theMessage"
                                           );
        Predicate<Object> condition = mock(Predicate.class);
        FixedErrorValidator<Object> validator = new FixedErrorValidator<>(condition, message);

        // When
        validator.validate(new Object(), messages());

        // Then
        assertThat(validator.getCondition(), sameInstance(condition));
        assertThat(validator.getMessage(), sameInstance(message));
        assertThat(validator.getValueProvider(), nullValue());
    }

    @Test
    public void ctorConditionMessageCodeAndMessage() {
        // Given
        MessageImpl message = globalMessage("theCode",
                                            "theMessage"
                                           );
        Predicate<Object> condition = mock(Predicate.class);
        FixedErrorValidator<Object> validator = new FixedErrorValidator<>(condition,
                                                                          message.getCode(),
                                                                          message.getParameterisedMessage());

        // When
        validator.validate(new Object(), messages());

        // Then
        assertThat(validator.getCondition(), sameInstance(condition));
        assertThat(validator.getMessage(), not(sameInstance(message)));
        assertThat(validator.getMessage(), equalTo(message));
        assertThat(validator.getValueProvider(), nullValue());
    }

    @Test
    public void ctorConditionFieldMessageCodeAndMessage() {
        // Given
        MessageImpl message = fieldMessage("theField",
                                           "theCode",
                                            "theMessage"
                                           );
        Predicate<Object> condition = mock(Predicate.class);
        FixedErrorValidator<Object> validator = new FixedErrorValidator<>(condition,
                                                                          message.getField(),
                                                                          message.getCode(),
                                                                          message.getParameterisedMessage());

        // When
        validator.validate(new Object(), messages());

        // Then
        assertThat(validator.getCondition(), sameInstance(condition));
        assertThat(validator.getMessage(), not(sameInstance(message)));
        assertThat(validator.getMessage(), equalTo(message));
        assertThat(validator.getValueProvider(), nullValue());
    }

    @Test
    public void alwaysAddsAnErrorWithDefaultMessageParametersWithTrueCondition() {
        // Given
        Object objectToValidate = new Object();
        MessageImpl message = globalMessage("theCode",
                                            "theMessage"
                                           );
        MessagesImpl messages = messages();
        FixedErrorValidator<Object> validator = new FixedErrorValidator<>(message);

        // When
        Messages returnedMessages = validator.validate(objectToValidate, messages);

        // Then
        assertThat(returnedMessages, sameInstance(messages));
        assertThat(returnedMessages.hasErrorLike(message), is(true));
        assertThat(returnedMessages.getErrors().get(0).getMessageParameters().length, is(2));
        assertThat(returnedMessages.getErrors().get(0).getMessageParameters()[0], sameInstance(objectToValidate));
        assertThat(returnedMessages.getErrors().get(0).getMessageParameters()[1], sameInstance(objectToValidate));

    }

    @Test
    public void alwaysAddsAnErrorWithSuppliedMessageParametersWithTrueCondition() {
        // Given
        Object objectToValidate = new Object();
        Object messageParam1 = "param1", messageParam2 = "param2", messageParam3 = "param3";
        MessageImpl message = globalMessage("theCode",
                                            "theMessage",
                                            messageParam1, messageParam2,messageParam3
                                           );
        MessagesImpl messages = messages();
        FixedErrorValidator<Object> validator = new FixedErrorValidator<>(message);

        // When
        Messages returnedMessages = validator.validate(objectToValidate, messages);

        // Then
        assertThat(returnedMessages, sameInstance(messages));
        assertThat(returnedMessages.hasErrorLike(message), is(true));
        assertThat(returnedMessages.getErrors().get(0).getMessageParameters().length, is(3));
        assertThat(returnedMessages.getErrors().get(0).getMessageParameters(), equalTo(new Object[] {
                messageParam1,
                messageParam2,
                messageParam3
        }));
    }

    @Test
    public void doesNotAddAnErrorWithFalseCondition() {
        // Given
        Object objectToValidate = new Object();
        MessageImpl message = globalMessage("theCode",
                                            "theMessage"
                                           );
        MessagesImpl messages = messages();
        FixedErrorValidator<Object> validator = new FixedErrorValidator<>(falsePredicate(),
                                                                          message);

        // When
        Messages returnedMessages = validator.validate(objectToValidate, messages);

        // Then
        assertThat(returnedMessages, sameInstance(messages));
        assertThat(returnedMessages.hasErrors(), is(false));
    }
}