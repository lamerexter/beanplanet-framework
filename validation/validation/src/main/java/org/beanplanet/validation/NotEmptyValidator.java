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

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.beanplanet.messages.domain.MessageImpl.globalMessage;

/**
 * A validator that expects a value to be non-null and whose string value is not empty after all surrounding
 * whitespace is removed.
 *
 * <p>This validator supports validation on the top-level validation context object directly
 * or on a value provided by runtime-expression or other value provider. Great for nested
 * properties</p>
 *
 * @param <T>   The type of the context object containing the field being validated.
 */
public class NotEmptyValidator<T> extends PredicatedValidator<T> {
    public static final String DEFAULT_MESSAGE_CODE = "empty";
    public static final String DEFAULT_PARAM_MESSAGE = "Value is required and must not be empty";
    private static final Predicate<?> VALIDATION_PREDICATE = v -> v != null && !(v instanceof Collection ? ((Collection<?>)v).isEmpty() : v.toString().trim().isEmpty());

    public NotEmptyValidator() {
        this(globalMessage(DEFAULT_MESSAGE_CODE, DEFAULT_PARAM_MESSAGE));
    }

    public NotEmptyValidator(Message message) {
        this(null, message, (Function<T, ?>)null);
    }

    public NotEmptyValidator(Message message, String runtimeExpression) {
        this(null, message, runtimeExpression);
    }

    public <R> NotEmptyValidator(Message message, Function<T, R> valueProvider) {
        this(null, message, valueProvider);
    }

    public NotEmptyValidator(Predicate<T> condition, Message message) {
        this(condition, message, (Function<T, ?>)null);
    }

    public NotEmptyValidator(Predicate<T> condition, Message message, String runtimeExpression) {
        super(condition, runtimeExpression, message, VALIDATION_PREDICATE);
    }

    public <R> NotEmptyValidator(Predicate<T> condition, Message message, Function<T, R> valueProvider) {
        super(condition, valueProvider, message, VALIDATION_PREDICATE);
    }
}
