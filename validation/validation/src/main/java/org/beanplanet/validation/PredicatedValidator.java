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

import org.beanplanet.messages.domain.Message;
import org.beanplanet.messages.domain.Messages;

import java.util.function.Function;
import java.util.function.Predicate;

import static org.beanplanet.messages.domain.MessageImpl.fieldMessage;

/**
 * A validator configured with or predicated on its given validation logic at runtime.
 *
 * @param <T> The type of the context object to validate.
 */
public abstract class PredicatedValidator<T> extends AbstractValidator<T> {
    private Predicate<?> validation;

    public PredicatedValidator(Message message) {
        this(message, null);
    }

    public PredicatedValidator(Predicate<T> condition, Message message) {
        this(condition, message, null);
    }

    public PredicatedValidator(Message message, Predicate<?> validation) {
        super(message);
        this.validation = validation;
    }

    public PredicatedValidator(Predicate<T> condition, Message message, Predicate<?> validation) {
        super(condition, message);
        this.validation = validation;
    }

    public PredicatedValidator(String runtimeExpression, Message message) {
        this(null, runtimeExpression, message, null);
    }

    public PredicatedValidator(Predicate<T> condition, String runtimeExpression, Message message) {
        this(condition, runtimeExpression, message, null);
    }

    public PredicatedValidator(String runtimeExpression, Message message, Predicate<?> validation) {
        this(null, runtimeExpression, message, validation);
    }

    public PredicatedValidator(Predicate<T> condition, String runtimeExpression, Message message, Predicate<?> validation) {
        super(condition, message, runtimeExpression);
        this.validation = validation;
    }

    public PredicatedValidator(Function<T, ?> valueProvider, Message message) {
        this(null, valueProvider, message, null);
    }

    public PredicatedValidator(Predicate<T> condition, Function<T, ?> valueProvider, Message message) {
        this(condition, valueProvider, message, null);
    }

    public PredicatedValidator(Function<T, ?> valueProvider, Message message, Predicate<?> validation) {
        this(null, valueProvider, message, validation);
    }

    public PredicatedValidator(Predicate<T> condition, Function<T, ?> valueProvider, Message message, Predicate<?> validation) {
        super(condition, message, valueProvider);
        this.validation = validation;
    }

    @SuppressWarnings("unchecked")
    public <T> Predicate<T> getValidation() {
        return (Predicate)validation;
    }

    public void setValidation(Predicate<?> validation) {
        this.validation = validation;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Messages doValidate(T object, Messages messages) {
        Object rtValue = getValidationValue(object);
        if (!((Predicate) validation).test(rtValue)) {
            messages.addError(fieldMessage(getMessage().getField(),
                    getMessage().getCode(),
                    getMessage().getParameterisedMessage(),
                    getMessage().getMessageParameters() != null ? getMessage().getMessageParameters() :
                            new Object[]{
                                    object,
                                    getValidationValue(object)
                            })
            );
        }
        return messages;
    }
}
