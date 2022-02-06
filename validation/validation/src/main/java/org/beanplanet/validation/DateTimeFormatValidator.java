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
