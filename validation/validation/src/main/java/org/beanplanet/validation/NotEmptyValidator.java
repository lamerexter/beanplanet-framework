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
