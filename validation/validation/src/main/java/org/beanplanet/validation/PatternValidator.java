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
