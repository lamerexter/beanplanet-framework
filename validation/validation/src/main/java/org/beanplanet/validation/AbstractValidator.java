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

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.beanplanet.messages.domain.Message;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A conditional validator supporting validation of a single value (but not limited to).
 *
 * @param <T> The type of the context object containing the field being validated.
 */
public abstract class AbstractValidator<T> extends AbstractValueProvidedValidator<T> {
    /**
     * The message template applied by this validator.
     */
    private Message message;

    public AbstractValidator(Message message) {
        this(null, message);
    }

    /**
     * Construct a validator with a supplied condition.
     *
     * @param condition a {@link Predicate} that governs whether the validation is applied.
     */
    public AbstractValidator(Predicate<T> condition) {
        this(condition, null);
    }

    public AbstractValidator(Predicate<T> condition, Message message) {
        this(condition, message, (Function<T, ?>) null);
    }

    public AbstractValidator(Message message, String validationValueExpression) {
        this(null, message, validationValueExpression);
    }

    public AbstractValidator(Message message, Function<T, ?> valueProvider) {
        this(null, message, valueProvider);
    }

    public AbstractValidator(Predicate<T> condition, Message message, String validationValueExpression) {
        this(condition, message, new SpringBeanValueProvider<>(validationValueExpression));
    }

    public AbstractValidator(Predicate<T> condition, Message message, Function<T, ?> valueProvider) {
        super(condition, valueProvider);
        this.message = message;
    }

    /**
     * Gets the message template applied by this validator.
     *
     * @return the message template applied by this validator, which may be null.
     */
    public Message getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this);
    }
}
