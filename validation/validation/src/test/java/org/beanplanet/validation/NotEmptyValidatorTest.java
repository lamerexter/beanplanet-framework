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

import java.util.function.Predicate;

import static org.beanplanet.messages.domain.MessageImpl.globalMessage;
import static org.beanplanet.messages.domain.MessagesImpl.messages;
import static org.beanplanet.validation.NotEmptyValidator.DEFAULT_MESSAGE_CODE;
import static org.beanplanet.validation.NotEmptyValidator.DEFAULT_PARAM_MESSAGE;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NotEmptyValidatorTest {
    @Test
    public void ctorNoArgs() {
        // Given
        Message message = globalMessage(DEFAULT_MESSAGE_CODE, DEFAULT_PARAM_MESSAGE);
        NotEmptyValidator<Object> validator = new NotEmptyValidator<>();

        // Then
        assertThat(validator.getCondition(), nullValue());
        assertThat(validator.getValueProvider(), nullValue());
        assertThat(validator.getMessage(), equalTo(message));

        assertThat(validator.getValidation(), notNullValue());
        assertThat(validator.getValidation().test(null), is(false));
        assertThat(validator.getValidation().test("  "), is(false));
        assertThat(validator.getValidation().test("  Hello "), is(true));

        // When a null object is validated
        Messages messagesForNullObject = validator.validate(null, messages());

        // Then an error message is added
        assertThat(messagesForNullObject.hasErrorLike(message), is(true));

        // When a non-null object is validated
        Messages messagesForNonNullObject = validator.validate(new Object(), messages());

        // Then no error message is added
        assertThat(messagesForNonNullObject.hasErrorLike(message), is(false));
    }

    @Test
    public void ctorMessage() {
        // Given
        Message message = globalMessage("theCode", "theMessage");
        NotEmptyValidator<Object> validator = new NotEmptyValidator<>(message);

        // Then
        assertThat(validator.getCondition(), nullValue());
        assertThat(validator.getValueProvider(), nullValue());
        assertThat(validator.getMessage(), equalTo(message));

        assertThat(validator.getValidation(), notNullValue());
        assertThat(validator.getValidation().test(null), is(false));
        assertThat(validator.getValidation().test("  "), is(false));
        assertThat(validator.getValidation().test("  Hello "), is(true));

        // When a null object is validated
        Messages messagesForNullObject = validator.validate(null, messages());

        // Then an error message is added
        assertThat(messagesForNullObject.hasErrorLike(message), is(true));

        // When a non-null object is validated
        Messages messagesForNonNullObject = validator.validate(new Object(), messages());

        // Then no error message is added
        assertThat(messagesForNonNullObject.hasErrorLike(message), is(false));
    }

    @Test
    public void ctorMessageRtExpression() {
        // Given
        Message message = globalMessage("theCode", "theMessage");
        NotEmptyValidator<TestBean> validator = new NotEmptyValidator<>(message, "stringProperty");

        // Then
        assertThat(validator.getCondition(), nullValue());
        assertThat(validator.getValueProvider(), notNullValue());
        assertThat(validator.getMessage(), equalTo(message));

        assertThat(validator.getValidation(), notNullValue());
        assertThat(validator.getValidation().test(null), is(false));
        assertThat(validator.getValidation().test("  "), is(false));
        assertThat(validator.getValidation().test("  Hello "), is(true));

        // When a null object is validated
        Messages messagesForNullObject = validator.validate(new TestBean(), messages());

        // Then an error message is added
        assertThat(messagesForNullObject.hasErrorLike(message), is(true));

        // When a non-null object is validated
        Messages messagesForNonNullObject = validator.validate(new TestBean("stringValue"), messages());

        // Then no error message is added
        assertThat(messagesForNonNullObject.hasErrorLike(message), is(false));
    }

    @Test
    public void ctorMessageValueProvider() {
        // Given
        Message message = globalMessage("theCode", "theMessage");
        NotEmptyValidator<TestBean> validator = new NotEmptyValidator<>(message, TestBean::getStringProperty);

        // Then
        assertThat(validator.getCondition(), nullValue());
        assertThat(validator.getValueProvider(), notNullValue());
        assertThat(validator.getMessage(), equalTo(message));

        assertThat(validator.getValidation(), notNullValue());
        assertThat(validator.getValidation().test(null), is(false));
        assertThat(validator.getValidation().test("  "), is(false));
        assertThat(validator.getValidation().test("  Hello "), is(true));

        // When a null object is validated
        Messages messagesForNullObject = validator.validate(new TestBean(), messages());

        // Then an error message is added
        assertThat(messagesForNullObject.hasErrorLike(message), is(true));

        // When a non-null object is validated
        Messages messagesForNonNullObject = validator.validate(new TestBean("stringValue"), messages());

        // Then no error message is added
        assertThat(messagesForNonNullObject.hasErrorLike(message), is(false));
    }

    @Test
    public void ctorConditionMessage() {
        // Given
        Predicate<Object> condition = mock(Predicate.class);
        Message message = globalMessage("theCode", "theMessage");
        NotEmptyValidator<Object> validator = new NotEmptyValidator<>(condition, message);

        // Then
        assertThat(validator.getCondition(), sameInstance(condition));
        assertThat(validator.getValueProvider(), nullValue());
        assertThat(validator.getMessage(), equalTo(message));

        assertThat(validator.getValidation(), notNullValue());
        assertThat(validator.getValidation().test(null), is(false));
        assertThat(validator.getValidation().test("  "), is(false));
        assertThat(validator.getValidation().test("  Hello "), is(true));

        // When a null object is validated (condition == false)
        when(condition.test(any())).thenReturn(false);
        Messages messagesForNullObject = validator.validate(null, messages());

        // Then  no error message is added
        assertThat(messagesForNullObject.hasErrorLike(message), is(false));

        // When a null object is validated (condition == true)
        when(condition.test(any())).thenReturn(true);
        messagesForNullObject = validator.validate(null, messages());

        // Then an error message is added
        assertThat(messagesForNullObject.hasErrorLike(message), is(true));

        // When a non-null object is validated (condition == false)
        when(condition.test(any())).thenReturn(false);
        Messages messagesForNonNullObject = validator.validate(new Object(), messages());

        // Then no error message is added
        assertThat(messagesForNonNullObject.hasErrorLike(message), is(false));

        // When a non-null object is validated (condition == true)
        when(condition.test(any())).thenReturn(false);
        messagesForNonNullObject = validator.validate(new Object(), messages());

        // Then no error message is added
        assertThat(messagesForNonNullObject.hasErrorLike(message), is(false));
    }

    @Test
    public void ctorConditionMessageAndRtExpression() {
        // Given
        Predicate<TestBean> condition = mock(Predicate.class);
        Message message = globalMessage("theCode", "theMessage");
        NotEmptyValidator<TestBean> validator = new NotEmptyValidator<>(condition,
                                                                        message,
                                                                        "stringProperty");

        // Then
        assertThat(validator.getCondition(), sameInstance(condition));
        assertThat(validator.getValueProvider(), notNullValue());
        assertThat(validator.getMessage(), equalTo(message));

        assertThat(validator.getValidation(), notNullValue());
        assertThat(validator.getValidation().test(null), is(false));
        assertThat(validator.getValidation().test("  "), is(false));
        assertThat(validator.getValidation().test("  Hello "), is(true));

        // When a null object is validated (condition == false)
        when(condition.test(any())).thenReturn(false);
        Messages messagesForNullObject = validator.validate(new TestBean(), messages());

        // Then  no error message is added
        assertThat(messagesForNullObject.hasErrorLike(message), is(false));

        // When a null object is validated (condition == true)
        when(condition.test(any())).thenReturn(true);
        messagesForNullObject = validator.validate(new TestBean(), messages());

        // Then an error message is added
        assertThat(messagesForNullObject.hasErrorLike(message), is(true));

        // When a non-null object is validated (condition == false)
        when(condition.test(any())).thenReturn(false);
        Messages messagesForNonNullObject = validator.validate(new TestBean("StringValue"),
                                                               messages());

        // Then no error message is added
        assertThat(messagesForNonNullObject.hasErrorLike(message), is(false));

        // When a non-null object is validated (condition == true)
        when(condition.test(any())).thenReturn(false);
        messagesForNonNullObject = validator.validate(new TestBean("StringValue"), messages());

        // Then no error message is added
        assertThat(messagesForNonNullObject.hasErrorLike(message), is(false));
    }

    @Test
    public void ctorConditionMessageAndValueProvider() {
        // Given
        Predicate<TestBean> condition = mock(Predicate.class);
        Message message = globalMessage("theCode", "theMessage");
        NotEmptyValidator<TestBean> validator = new NotEmptyValidator<>(condition,
                                                                        message,
                                                                        TestBean::getStringProperty);

        // Then
        assertThat(validator.getCondition(), sameInstance(condition));
        assertThat(validator.getValueProvider(), notNullValue());
        assertThat(validator.getMessage(), equalTo(message));

        assertThat(validator.getValidation(), notNullValue());
        assertThat(validator.getValidation().test(null), is(false));
        assertThat(validator.getValidation().test("  "), is(false));
        assertThat(validator.getValidation().test("  Hello "), is(true));

        // When a null object is validated (condition == false)
        when(condition.test(any())).thenReturn(false);
        Messages messagesForNullObject = validator.validate(new TestBean(), messages());

        // Then  no error message is added
        assertThat(messagesForNullObject.hasErrorLike(message), is(false));

        // When a null object is validated (condition == true)
        when(condition.test(any())).thenReturn(true);
        messagesForNullObject = validator.validate(new TestBean(), messages());

        // Then an error message is added
        assertThat(messagesForNullObject.hasErrorLike(message), is(true));

        // When a non-null object is validated (condition == false)
        when(condition.test(any())).thenReturn(false);
        Messages messagesForNonNullObject = validator.validate(new TestBean("StringValue"),
                                                               messages());

        // Then no error message is added
        assertThat(messagesForNonNullObject.hasErrorLike(message), is(false));

        // When a non-null object is validated (condition == true)
        when(condition.test(any())).thenReturn(false);
        messagesForNonNullObject = validator.validate(new TestBean("StringValue"), messages());

        // Then no error message is added
        assertThat(messagesForNonNullObject.hasErrorLike(message), is(false));
    }
}