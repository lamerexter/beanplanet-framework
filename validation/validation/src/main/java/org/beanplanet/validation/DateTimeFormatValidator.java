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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A Validator for checking a text field matches a date and/or time format.
 *
 * <p>This validator supports validation on the top-level validation context object directly
 * or on a value provided by runtime-expression or other value provider. Great for nested
 * properties</p>
 */
public class DateTimeFormatValidator<T> extends PredicatedValidator<T> {

    private DateTimeFormatter[] dateTimeFormmatters;
    private final Predicate<?> VALIDATION_PREDICATE = v -> dateTimeFormmatters != null
      && Arrays.stream(dateTimeFormmatters).anyMatch(f -> { try { f.parse(v.toString()); return true;  } catch (DateTimeParseException parseEx) { return false; } });


    public DateTimeFormatValidator(Message message, String dateTimeFormat) {
        this(null, message, dateTimeFormat);
    }

    public DateTimeFormatValidator(Predicate<T> condition, Message message, String dateTimeFormat) {
        this(condition, message, (Function<T, ?>)null, dateTimeFormat);
    }

    public DateTimeFormatValidator(Message message, String runtimeExpression, String dateTimeFormat) {
        this(null, runtimeExpression, message, dateTimeFormat);
    }

    public <R> DateTimeFormatValidator(Message message, Function<T, R> valueProvider, String dateTimeFormat) {
        this(null, message, valueProvider, dateTimeFormat);
    }

    public DateTimeFormatValidator(Predicate<T> condition, String runtimeExpression, Message message, String ... dateTimeFormats) {
        this(condition, message, runtimeExpression);
        this.dateTimeFormmatters = (dateTimeFormats == null ? null : Arrays.stream(dateTimeFormats).map(DateTimeFormatter::ofPattern).toArray(DateTimeFormatter[]::new));
        setValidation(VALIDATION_PREDICATE);
    }

    public <R> DateTimeFormatValidator(Predicate<T> condition, Message message, Function<T, R> valueProvider, String ... dateTimeFormats) {
        this(condition, message, valueProvider, dateTimeFormats == null ? null : Arrays.stream(dateTimeFormats).map(DateTimeFormatter::ofPattern).toArray(DateTimeFormatter[]::new));
    }

    public <R> DateTimeFormatValidator(Predicate<T> condition, Message message, Function<T, R> valueProvider, DateTimeFormatter ... dateTimeFormmatters) {
        super(condition, valueProvider, message);
        this.dateTimeFormmatters = dateTimeFormmatters;
        setValidation(VALIDATION_PREDICATE);
    }
}
