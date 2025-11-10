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

import java.util.function.Function;
import java.util.function.Predicate;

import static org.beanplanet.messages.domain.MessageImpl.fieldMessage;

/**
 * A validator configured with or predicated on its given validation logic at runtime.
 *
 * @param <T> The type of the context object to validate.
 */
public class PredicatedValidator<T> extends AbstractValidator<T> {
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
