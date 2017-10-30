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
