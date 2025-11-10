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

import static org.beanplanet.core.Predicates.*;
import static org.beanplanet.messages.domain.MessageImpl.globalMessage;
import static org.beanplanet.messages.domain.MessagesImpl.messages;
import static org.beanplanet.validation.PatternValidator.DEFAULT_MESSAGE_CODE;
import static org.beanplanet.validation.PatternValidator.DEFAULT_PARAM_MESSAGE;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for @{@link PatternValidator}.
 */
public class PatternValidatorTest {
    @Test
    public void ctorPattern() {
        // Given
        Message message = globalMessage(DEFAULT_MESSAGE_CODE, DEFAULT_PARAM_MESSAGE);
        Messages messages = messages();
        PatternValidator<Object> validator = new PatternValidator<>("\\d+");

        // Then
        assertThat(validator.getCondition(), nullValue());
        assertThat(validator.getValueProvider(), nullValue());
        assertThat(validator.getMessage(), equalTo(message));

        assertThat(validator.getValidation(), notNullValue());
        assertThat(validator.getValidation().test("123"), is(true));
        assertThat(validator.getValidation().test("a12"), is(false));
        assertThat(validator.getPattern().pattern(), equalTo("\\d+"));

        // When a valid object is validated
        Messages returnedMessages = validator.validate("123", messages);

        // Then no error is added
        assertThat(returnedMessages, sameInstance(messages));
        assertThat(returnedMessages.hasErrors(), is(false));

        // When an invalid object is validated
        returnedMessages = validator.validate("123a", messages);

        // Then an error is added
        assertThat(returnedMessages, sameInstance(messages));
        assertThat(returnedMessages.hasErrorLike(message), is(true));

        // When an invalid object is validated, but under a false condition
        validator = new PatternValidator<>(falsePredicate(), message, "\\d+");
        returnedMessages = validator.validate("123a", messages());

        // Then no error is added
        assertThat(returnedMessages.hasErrors(), is(false));
    }

    @Test
    public void ctorMessageAndPattern() {
        // Given
        Message message = globalMessage("theCode", "theMessage");
        Messages messages = messages();
        PatternValidator<Object> validator = new PatternValidator<>(message, "\\d+");

        // Then
        assertThat(validator.getCondition(), nullValue());
        assertThat(validator.getValueProvider(), nullValue());
        assertThat(validator.getMessage(), equalTo(message));
        assertThat(validator.getValidation(), notNullValue());
        assertThat(validator.getValidation().test("123"), is(true));
        assertThat(validator.getValidation().test("a12"), is(false));
        assertThat(validator.getPattern().pattern(), equalTo("\\d+"));

        // When an invalid object is validated
        Messages returnedMessages = validator.validate("123", messages);

        // Then an error is added
        assertThat(returnedMessages, sameInstance(messages));
        assertThat(returnedMessages.hasErrors(), is(false));

        // When an invalid object is validated
        returnedMessages = validator.validate("123a", messages);

        // Then an error is added
        assertThat(returnedMessages, sameInstance(messages));
        assertThat(returnedMessages.hasErrorLike(message), is(true));
    }

    @Test
    public void ctorConditionMessageAndPattern() {
        // Given
        Message message = globalMessage("theCode", "theMessage");
        Messages messages = messages();
        Predicate<Object> condition = truePredicate();
        PatternValidator<Object> validator = new PatternValidator<>(condition, message, "\\d+");

        // Then
        assertThat(validator.getCondition(), sameInstance(condition));
        assertThat(validator.getValueProvider(), nullValue());
        assertThat(validator.getMessage(), equalTo(message));
        assertThat(validator.getValidation(), notNullValue());
        assertThat(validator.getValidation().test("123"), is(true));
        assertThat(validator.getValidation().test("a12"), is(false));
        assertThat(validator.getPattern().pattern(), equalTo("\\d+"));

        // When a valid object is validated
        Messages returnedMessages = validator.validate("123", messages);

        // Then no error is added
        assertThat(returnedMessages, sameInstance(messages));
        assertThat(returnedMessages.hasErrors(), is(false));

        // When an invalid object is validated
        returnedMessages = validator.validate("123a", messages);

        // Then an error is added
        assertThat(returnedMessages, sameInstance(messages));
        assertThat(returnedMessages.hasErrorLike(message), is(true));

        // When an invalid object is validated, but under a false condition
        validator = new PatternValidator<>(falsePredicate(), message, "\\d+");
        returnedMessages = validator.validate("123a", messages());

        // Then no error is added
        assertThat(returnedMessages.hasErrors(), is(false));
    }

    @Test
    public void ctorMessageValueExpressionAndPattern() {
        // Given
        Message message = globalMessage("theCode", "theMessage");
        Messages messages = messages();
        PatternValidator<TestBean> validator = new PatternValidator<>(message,
                                                                      "stringProperty",
                                                                      "\\d+"
        );

        // Then
        assertThat(validator.getCondition(), nullValue());
        assertThat(validator.getValueProvider(), notNullValue());
        assertThat(validator.getMessage(), equalTo(message));
        assertThat(validator.getValidation(), notNullValue());
        assertThat(validator.getValidation().test("123"), is(true));
        assertThat(validator.getValidation().test("a12"), is(false));
        assertThat(validator.getPattern().pattern(), equalTo("\\d+"));

        // When a valid object is validated
        Messages returnedMessages = validator.validate(new TestBean("1234"), messages);

        // Then no error is added
        assertThat(returnedMessages, sameInstance(messages));
        assertThat(returnedMessages.hasErrors(), is(false));

        // When an invalid object is validated
        returnedMessages = validator.validate(new TestBean("123a"), messages);

        // Then an error is added
        assertThat(returnedMessages, sameInstance(messages));
        assertThat(returnedMessages.hasErrorLike(message), is(true));

        // When an invalid object is validated, but under a false condition
        validator = new PatternValidator<>(falsePredicate(), message, "\\d+");
        returnedMessages = validator.validate(new TestBean("123a"), messages());

        // Then no error is added
        assertThat(returnedMessages.hasErrors(), is(false));
    }

    @Test
    public void ctorMessageValueProviderAndPattern() {
        // Given
        Function<TestBean, String> valueProvider = TestBean::getStringProperty;
        Message message = globalMessage("theCode", "theMessage");
        Messages messages = messages();
        PatternValidator<TestBean> validator = new PatternValidator<>(message,
                                                                      valueProvider,
                                                                      "\\d+"
        );

        // Then
        assertThat(validator.getCondition(), nullValue());
        assertThat(validator.getValueProvider(), sameInstance(valueProvider));
        assertThat(validator.getMessage(), equalTo(message));
        assertThat(validator.getValidation(), notNullValue());
        assertThat(validator.getValidation().test("123"), is(true));
        assertThat(validator.getValidation().test("a12"), is(false));
        assertThat(validator.getPattern().pattern(), equalTo("\\d+"));

        // When a valid object is validated
        Messages returnedMessages = validator.validate(new TestBean("1234"), messages);

        // Basic Pattern Validator validation
        assertThat(validator.getValidation(), notNullValue());
        assertThat(validator.getValidation().test("123"), is(true));
        assertThat(validator.getValidation().test("a12"), is(false));
        assertThat(validator.getPattern().pattern(), equalTo("\\d+"));

        // Then no error is added
        assertThat(returnedMessages, sameInstance(messages));
        assertThat(returnedMessages.hasErrors(), is(false));

        // When an invalid object is validated
        returnedMessages = validator.validate(new TestBean("123a"), messages);

        // Then an error is added
        assertThat(returnedMessages, sameInstance(messages));
        assertThat(returnedMessages.hasErrorLike(message), is(true));

        // When an invalid object is validated, but under a false condition
        validator = new PatternValidator<>(falsePredicate(), message, "\\d+");
        returnedMessages = validator.validate(new TestBean("123a"), messages());

        // Then no error is added
        assertThat(returnedMessages.hasErrors(), is(false));
    }

    @Test
    public void ctorConditionMessageValueExpressionAndPattern() {
        // Given
        Predicate<TestBean> condition = truePredicate();
        Message message = globalMessage("theCode", "theMessage");
        Messages messages = messages();
        PatternValidator<TestBean> validator = new PatternValidator<>(condition,
                                                                      message,
                                                                      "stringProperty",
                                                                      "\\d+"
        );

        // Then
        assertThat(validator.getCondition(), sameInstance(condition));
        assertThat(validator.getValueProvider(), notNullValue());
        assertThat(validator.getMessage(), equalTo(message));
        assertThat(validator.getValidation(), notNullValue());
        assertThat(validator.getValidation().test("123"), is(true));
        assertThat(validator.getValidation().test("a12"), is(false));
        assertThat(validator.getPattern().pattern(), equalTo("\\d+"));

        // When a valid object is validated
        Messages returnedMessages = validator.validate(new TestBean("1234"), messages);

        // Then no error is added
        assertThat(returnedMessages, sameInstance(messages));
        assertThat(returnedMessages.hasErrors(), is(false));

        // When an invalid object is validated
        returnedMessages = validator.validate(new TestBean("123a"), messages);

        // Then an error is added
        assertThat(returnedMessages, sameInstance(messages));
        assertThat(returnedMessages.hasErrorLike(message), is(true));

        // When an invalid object is validated, but under a false condition
        validator = new PatternValidator<>(falsePredicate(), message, "\\d+");
        returnedMessages = validator.validate(new TestBean("123a"), messages());

        // Then no error is added
        assertThat(returnedMessages.hasErrors(), is(false));
    }

    @Test
    public void ctorConditionMessageValueProviderAndPattern() {
        // Given
        Predicate<TestBean> condition = truePredicate();
        Function<TestBean, String> valueProvider = TestBean::getStringProperty;
        Message message = globalMessage("theCode", "theMessage");
        Messages messages = messages();
        PatternValidator<TestBean> validator = new PatternValidator<>(condition,
                                                                      message,
                                                                      valueProvider,
                                                                      "\\d+"
        );

        // Then
        assertThat(validator.getCondition(), sameInstance(condition));
        assertThat(validator.getValueProvider(), sameInstance(valueProvider));
        assertThat(validator.getMessage(), equalTo(message));
        assertThat(validator.getValidation(), notNullValue());
        assertThat(validator.getValidation().test("123"), is(true));
        assertThat(validator.getValidation().test("a12"), is(false));
        assertThat(validator.getPattern().pattern(), equalTo("\\d+"));

        // When a valid object is validated
        Messages returnedMessages = validator.validate(new TestBean("1234"), messages);

        // Basic Pattern Validator validation
        assertThat(validator.getValidation(), notNullValue());
        assertThat(validator.getValidation().test("123"), is(true));
        assertThat(validator.getValidation().test("a12"), is(false));
        assertThat(validator.getPattern().pattern(), equalTo("\\d+"));

        // Then no error is added
        assertThat(returnedMessages, sameInstance(messages));
        assertThat(returnedMessages.hasErrors(), is(false));

        // When an invalid object is validated
        returnedMessages = validator.validate(new TestBean("123a"), messages);

        // Then an error is added
        assertThat(returnedMessages, sameInstance(messages));
        assertThat(returnedMessages.hasErrorLike(message), is(true));

        // When an invalid object is validated, but under a false condition
        validator = new PatternValidator<>(falsePredicate(), message, "\\d+");
        returnedMessages = validator.validate(new TestBean("123a"), messages());

        // Then no error is added
        assertThat(returnedMessages.hasErrors(), is(false));
    }
}