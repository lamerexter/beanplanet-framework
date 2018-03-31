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