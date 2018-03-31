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

import org.beanplanet.messages.domain.Message;
import org.beanplanet.messages.domain.Messages;
import org.beanplanet.testing.beans.TestBean;
import org.junit.Test;

import java.util.function.Function;
import java.util.function.Predicate;

import static org.beanplanet.core.Predicates.falsePredicate;
import static org.beanplanet.core.Predicates.truePredicate;
import static org.beanplanet.messages.domain.MessageImpl.globalMessage;
import static org.beanplanet.messages.domain.MessagesImpl.messages;
import static org.beanplanet.validation.StringLengthValidator.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Unit test for @{@link StringLengthValidator}.
 *
 * <p>This validator supports validation on the top-level validation context object directly
 * or on a value provided by runtime-expression or other value provider. Great for nested
 * properties</p>
 */
public class StringLengthValidatorTest {
    @Test
    public void ctorMinMax() {
        // Given
        Messages messages = messages();
        Message message = globalMessage(DEFAULT_MESSAGE_CODE, DEFAULT_PARAM_MESSAGE_MIN_MAX);
        StringLengthValidator<String> validator = new StringLengthValidator<>(5, 10);

        // Then the validator is configured correctly
        assertThat(validator.getCondition(), nullValue());
        assertThat(validator.getMessage(), equalTo(message));
        assertThat(validator.getMinLengthInclusive(), equalTo(5));
        assertThat(validator.getMaxLengthInclusive(), equalTo(10));


        // When a string whose length equals the minimum bound is validated
        Messages returnedMessages = validator.validate("12345", messages);

        // Then no error is reported
        assertThat(returnedMessages.hasErrors(), is(false));
        assertThat(returnedMessages, sameInstance(messages));

        // When a string whose length equals the maximum bound is validated
        returnedMessages = validator.validate("1234567890", messages());

        // Then no error is reported
        assertThat(returnedMessages.hasErrors(), is(false));

        // When a string whose length is less than the minimum is validated
        returnedMessages = validator.validate("123", messages());

        // Then an error is reported
        assertThat(returnedMessages.hasErrors(), is(true));
        assertThat(returnedMessages.hasErrorLike(message), is(true));

        // When a string whose length is greater than the minimum is validated
        returnedMessages = validator.validate("12345678901", messages());

        // Then an error is reported
        assertThat(returnedMessages.hasErrors(), is(true));
        assertThat(returnedMessages.hasErrorLike(message), is(true));

        // When a validator is constructed with only a minimum bound
        validator = new StringLengthValidator<>(5, null);

        // Then the default message is appropriate for minimum bound only
        assertThat(validator.getMessage(), equalTo(globalMessage(DEFAULT_MESSAGE_CODE, DEFAULT_PARAM_MESSAGE_MIN)));

        // When a validator is constructed with only a maximum bound
        validator = new StringLengthValidator<>(null, 10);

        // Then the default message is appropriate for maximum bound only
        assertThat(validator.getMessage(), equalTo(globalMessage(DEFAULT_MESSAGE_CODE, DEFAULT_PARAM_MESSAGE_MAX)));

        // When a validator is constructed with no bound
        validator = new StringLengthValidator<>(null, null);

        // Then the default message fallback contains no parameterised message
        assertThat(validator.getMessage(), equalTo(globalMessage(DEFAULT_MESSAGE_CODE, null)));
    }

    @Test
    public void ctorMessageMinMax() {
        // Given
        Messages messages = messages();
        Message message = globalMessage("theCode", "theMessage");
        StringLengthValidator<String> validator = new StringLengthValidator<>(message, 5, 10);

        // Then the validator is configured correctly
        assertThat(validator.getCondition(), nullValue());
        assertThat(validator.getMessage(), equalTo(message));
        assertThat(validator.getMinLengthInclusive(), equalTo(5));
        assertThat(validator.getMaxLengthInclusive(), equalTo(10));


        // When a string whose length equals the minimum bound is validated
        Messages returnedMessages = validator.validate("12345", messages);

        // Then no error is reported
        assertThat(returnedMessages.hasErrors(), is(false));
        assertThat(returnedMessages, sameInstance(messages));

        // When a string whose length equals the maximum bound is validated
        returnedMessages = validator.validate("1234567890", messages());

        // Then no error is reported
        assertThat(returnedMessages.hasErrors(), is(false));

        // When a string whose length is less than the minimum is validated
        returnedMessages = validator.validate("123", messages());

        // Then an error is reported
        assertThat(returnedMessages.hasErrors(), is(true));
        assertThat(returnedMessages.hasErrorLike(message), is(true));

        // When a string whose length is greater than the minimum is validated
        returnedMessages = validator.validate("12345678901", messages());

        // Then an error is reported
        assertThat(returnedMessages.hasErrors(), is(true));
        assertThat(returnedMessages.hasErrorLike(message), is(true));
    }

    @Test
    public void ctorConditionMessageMinMax() {
        // Given
        Predicate<String> condition = truePredicate();
        Messages messages = messages();
        Message message = globalMessage("theCode", "theMessage");
        StringLengthValidator<String> validator = new StringLengthValidator<>(condition, message, 5, 10);

        // Then the validator is configured correctly
        assertThat(validator.getCondition(), sameInstance(condition));
        assertThat(validator.getMessage(), equalTo(message));
        assertThat(validator.getMinLengthInclusive(), equalTo(5));
        assertThat(validator.getMaxLengthInclusive(), equalTo(10));


        // When a string whose length equals the minimum bound is validated
        Messages returnedMessages = validator.validate("12345", messages);

        // Then no error is reported
        assertThat(returnedMessages.hasErrors(), is(false));
        assertThat(returnedMessages, sameInstance(messages));

        // When a string whose length equals the maximum bound is validated
        returnedMessages = validator.validate("1234567890", messages());

        // Then no error is reported
        assertThat(returnedMessages.hasErrors(), is(false));

        // When a string whose length is less than the minimum is validated
        returnedMessages = validator.validate("123", messages());

        // Then an error is reported
        assertThat(returnedMessages.hasErrors(), is(true));
        assertThat(returnedMessages.hasErrorLike(message), is(true));

        // When a string whose length is greater than the minimum is validated
        returnedMessages = validator.validate("12345678901", messages());

        // Then an error is reported
        assertThat(returnedMessages.hasErrors(), is(true));
        assertThat(returnedMessages.hasErrorLike(message), is(true));

        // When a string whose length is outside the bounds is validates under a false predicate condition
        validator = new StringLengthValidator<>(falsePredicate(), message, 5, 10);
        returnedMessages = validator.validate("12345678901", messages());

        // Then no error is reported
        assertThat(returnedMessages.hasErrors(), is(false));
    }

    @Test
    public void ctorMessageValueExpressionMinMax() {
        // Given
        Messages messages = messages();
        Message message = globalMessage("theCode", "theMessage");
        StringLengthValidator<TestBean> validator = new StringLengthValidator<>(message,
                                                                                "stringProperty",
                                                                                5, 10);

        // Then the validator is configured correctly
        assertThat(validator.getCondition(), nullValue());
        assertThat(validator.getMessage(), equalTo(message));
        assertThat(validator.getValueProvider(), notNullValue());
        assertThat(validator.getMinLengthInclusive(), equalTo(5));
        assertThat(validator.getMaxLengthInclusive(), equalTo(10));


        // When a string whose length equals the minimum bound is validated
        Messages returnedMessages = validator.validate(new TestBean("12345"), messages);

        // Then no error is reported
        assertThat(returnedMessages.hasErrors(), is(false));
        assertThat(returnedMessages, sameInstance(messages));

        // When a string whose length equals the maximum bound is validated
        returnedMessages = validator.validate(new TestBean("1234567890"), messages());

        // Then no error is reported
        assertThat(returnedMessages.hasErrors(), is(false));

        // When a string whose length is less than the minimum is validated
        returnedMessages = validator.validate(new TestBean("123"), messages());

        // Then an error is reported
        assertThat(returnedMessages.hasErrors(), is(true));
        assertThat(returnedMessages.hasErrorLike(message), is(true));

        // When a string whose length is greater than the minimum is validated
        returnedMessages = validator.validate(new TestBean("12345678901"), messages());

        // Then an error is reported
        assertThat(returnedMessages.hasErrors(), is(true));
        assertThat(returnedMessages.hasErrorLike(message), is(true));
    }

    @Test
    public void ctorMessageValueProviderMinMax() {
        // Given
        Messages messages = messages();
        Message message = globalMessage("theCode", "theMessage");
        Function<TestBean, String> valueProvider = TestBean::getStringProperty;
        StringLengthValidator<TestBean> validator = new StringLengthValidator<>(message,
                                                                                valueProvider,
                                                                                5, 10);

        // Then the validator is configured correctly
        assertThat(validator.getCondition(), nullValue());
        assertThat(validator.getMessage(), equalTo(message));
        assertThat(validator.getValueProvider(), sameInstance(valueProvider));
        assertThat(validator.getMinLengthInclusive(), equalTo(5));
        assertThat(validator.getMaxLengthInclusive(), equalTo(10));


        // When a string whose length equals the minimum bound is validated
        Messages returnedMessages = validator.validate(new TestBean("12345"), messages);

        // Then no error is reported
        assertThat(returnedMessages.hasErrors(), is(false));
        assertThat(returnedMessages, sameInstance(messages));

        // When a string whose length equals the maximum bound is validated
        returnedMessages = validator.validate(new TestBean("1234567890"), messages());

        // Then no error is reported
        assertThat(returnedMessages.hasErrors(), is(false));

        // When a string whose length is less than the minimum is validated
        returnedMessages = validator.validate(new TestBean("123"), messages());

        // Then an error is reported
        assertThat(returnedMessages.hasErrors(), is(true));
        assertThat(returnedMessages.hasErrorLike(message), is(true));

        // When a string whose length is greater than the minimum is validated
        returnedMessages = validator.validate(new TestBean("12345678901"), messages());

        // Then an error is reported
        assertThat(returnedMessages.hasErrors(), is(true));
        assertThat(returnedMessages.hasErrorLike(message), is(true));
    }

    @Test
    public void ctorConditionMessageValueExpressionMinMax() {
        // Given
        Predicate<TestBean> condition = truePredicate();
        Messages messages = messages();
        Message message = globalMessage("theCode", "theMessage");
        StringLengthValidator<TestBean> validator = new StringLengthValidator<>(condition,
                                                                                message,
                                                                                "stringProperty",
                                                                                5, 10);

        // Then the validator is configured correctly
        assertThat(validator.getCondition(), sameInstance(condition));
        assertThat(validator.getMessage(), equalTo(message));
        assertThat(validator.getValueProvider(), notNullValue());
        assertThat(validator.getMinLengthInclusive(), equalTo(5));
        assertThat(validator.getMaxLengthInclusive(), equalTo(10));


        // When a string whose length equals the minimum bound is validated
        Messages returnedMessages = validator.validate(new TestBean("12345"), messages);

        // Then no error is reported
        assertThat(returnedMessages.hasErrors(), is(false));
        assertThat(returnedMessages, sameInstance(messages));

        // When a string whose length equals the maximum bound is validated
        returnedMessages = validator.validate(new TestBean("1234567890"), messages());

        // Then no error is reported
        assertThat(returnedMessages.hasErrors(), is(false));

        // When a string whose length is less than the minimum is validated
        returnedMessages = validator.validate(new TestBean("123"), messages());

        // Then an error is reported
        assertThat(returnedMessages.hasErrors(), is(true));
        assertThat(returnedMessages.hasErrorLike(message), is(true));

        // When a string whose length is greater than the minimum is validated
        returnedMessages = validator.validate(new TestBean("12345678901"), messages());

        // Then an error is reported
        assertThat(returnedMessages.hasErrors(), is(true));
        assertThat(returnedMessages.hasErrorLike(message), is(true));

        // When a string whose length is outside the bounds is validates under a false predicate condition
        validator = new StringLengthValidator<>(falsePredicate(),
                                                message,
                                                "stringProperty",
                                                5, 10
        );
        returnedMessages = validator.validate(new TestBean("12345678901"), messages());

        // Then no error is reported
        assertThat(returnedMessages.hasErrors(), is(false));
    }

    @Test
    public void ctorConditionMessageValueProviderMinMax() {
        // Given
        Predicate<TestBean> condition = truePredicate();
        Messages messages = messages();
        Function<TestBean, String> valueProvider = TestBean::getStringProperty;
        Message message = globalMessage("theCode", "theMessage");
        StringLengthValidator<TestBean> validator = new StringLengthValidator<>(condition,
                                                                                message,
                                                                                valueProvider,
                                                                                5, 10);

        // Then the validator is configured correctly
        assertThat(validator.getCondition(), sameInstance(condition));
        assertThat(validator.getMessage(), equalTo(message));
        assertThat(validator.getValueProvider(), sameInstance(valueProvider));
        assertThat(validator.getMinLengthInclusive(), equalTo(5));
        assertThat(validator.getMaxLengthInclusive(), equalTo(10));


        // When a string whose length equals the minimum bound is validated
        Messages returnedMessages = validator.validate(new TestBean("12345"), messages);

        // Then no error is reported
        assertThat(returnedMessages.hasErrors(), is(false));
        assertThat(returnedMessages, sameInstance(messages));

        // When a string whose length equals the maximum bound is validated
        returnedMessages = validator.validate(new TestBean("1234567890"), messages());

        // Then no error is reported
        assertThat(returnedMessages.hasErrors(), is(false));

        // When a string whose length is less than the minimum is validated
        returnedMessages = validator.validate(new TestBean("123"), messages());

        // Then an error is reported
        assertThat(returnedMessages.hasErrors(), is(true));
        assertThat(returnedMessages.hasErrorLike(message), is(true));

        // When a string whose length is greater than the minimum is validated
        returnedMessages = validator.validate(new TestBean("12345678901"), messages());

        // Then an error is reported
        assertThat(returnedMessages.hasErrors(), is(true));
        assertThat(returnedMessages.hasErrorLike(message), is(true));

        // When a string whose length is outside the bounds is validates under a false predicate condition
        validator = new StringLengthValidator<>(falsePredicate(),
                                                message,
                                                "stringProperty",
                                                5, 10
        );
        returnedMessages = validator.validate(new TestBean("12345678901"), messages());

        // Then no error is reported
        assertThat(returnedMessages.hasErrors(), is(false));
    }

}