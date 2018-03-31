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

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static org.beanplanet.messages.domain.MessageImpl.globalMessage;

/**
 * A Validator for checking a text field matches a regular expression.
 *
 * <p>This validator supports validation on the top-level validation context object directly
 * or on a value provided by runtime-expression or other value provider. Great for nested
 * properties</p>
 */
public class PatternValidator<T> extends PredicatedValidator<T> {
    public static final String DEFAULT_MESSAGE_CODE = "pattern";
    public static final String DEFAULT_PARAM_MESSAGE = "Value is required and must not be empty";

    private Pattern pattern;
    private final Predicate<?> VALIDATION_PREDICATE = v -> getPattern().matcher(v.toString()).matches();


    public PatternValidator(String pattern) {
        this(null,
             globalMessage(DEFAULT_MESSAGE_CODE, DEFAULT_PARAM_MESSAGE),
             (Function<T, ?>)null, pattern);
    }

    public PatternValidator(Message message, String pattern) {
        this(null, message, (Function<T, ?>)null, pattern);
    }

    public PatternValidator(Predicate<T> condition, Message message, String pattern) {
        this(condition, message, (Function<T, ?>)null, pattern);
    }

    public PatternValidator(Message message, String runtimeExpression, String pattern) {
        this(null, message, runtimeExpression, pattern);
    }

    public <R> PatternValidator(Message message, Function<T, R> valueProvider, String pattern) {
        this(null, message, valueProvider, pattern);
    }

    public PatternValidator(Predicate<T> condition, Message message, String runtimeExpression, String pattern) {
        super(condition, runtimeExpression, message);
        this.pattern = Pattern.compile(pattern);
        setValidation(VALIDATION_PREDICATE);
    }

    public <R> PatternValidator(Predicate<T> condition, Message message, Function<T, R> valueProvider, String pattern) {
        super(condition, valueProvider, message);
        this.pattern = Pattern.compile(pattern);
        setValidation(VALIDATION_PREDICATE);
    }

    public Pattern getPattern() {
        return pattern;
    }
}
