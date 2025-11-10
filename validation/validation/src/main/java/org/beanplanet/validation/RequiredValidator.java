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

import java.util.function.Function;
import java.util.function.Predicate;

import static org.beanplanet.core.Predicates.truePredicate;
import static org.beanplanet.messages.domain.MessageImpl.fieldMessage;
import static org.beanplanet.messages.domain.MessageImpl.globalMessage;

/**
 * A validator that expects a given field within an object to have a non-null value.  The field
 * may be shallow, as in a top-level field, or deeply-nested, as in belonging to a sub-object
 * of the context object.
 *
 * <p>This validator supports validation on the top-level validation context object directly
 * or on a value provided by runtime-expression or other value provider. Great for nested
 * properties</p>
 *
 * @param <T>   The type of the context object containing the field being validated.
 */
public class RequiredValidator<T> extends PredicatedValidator<T> {
    public static final String DEFAULT_MESSAGE_CODE = "required";
    public static final String DEFAULT_PARAM_MESSAGE = "Value is required";
    private static final Predicate<?> VALIDATION_PREDICATE = v -> v != null;

    public RequiredValidator() {
        this(globalMessage(DEFAULT_MESSAGE_CODE, DEFAULT_PARAM_MESSAGE));
    }

    public RequiredValidator(Message message) {
        this(null, message, (Function<T, ?>)null);
    }

    public RequiredValidator(Message message, String runtimeExpression) {
        this(null, message, runtimeExpression);
    }

    public <R> RequiredValidator(Message message, Function<T, R> valueProvider) {
        this(null, message, valueProvider);
    }

    public RequiredValidator(Predicate<T> condition, Message message) {
        this(condition, message, (Function<T, ?>)null);
    }

    public RequiredValidator(Predicate<T> condition, Message message, String runtimeExpression) {
        super(condition, runtimeExpression, message, VALIDATION_PREDICATE);
    }

    public RequiredValidator(Predicate<T> condition, Message message, Function<T, ?> valueProvider) {
        super(condition, valueProvider, message, VALIDATION_PREDICATE);
    }
}
