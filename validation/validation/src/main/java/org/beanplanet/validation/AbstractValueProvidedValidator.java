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

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A validator that supports validation on the top-level validation context object directly
 * or on a value provided by a value provider. Great for nested property or expression-based validation.
 */
public abstract class AbstractValueProvidedValidator<T>
        extends AbstractConditionalValidator<T>
        implements ValueProvidedValidator<T> {

    private Function<T, ?> valueProvider;

    public AbstractValueProvidedValidator() {
    }

    public AbstractValueProvidedValidator(Predicate<T> condition) {
        this(condition, (Function<T, ?>) null);
    }

    public AbstractValueProvidedValidator(String validationValueExpression) {
        this(null, validationValueExpression);
    }

    public AbstractValueProvidedValidator(Function<T, ?> valueProvider) {
        this(null, valueProvider);
    }

    public AbstractValueProvidedValidator(Predicate<T> condition, String validationValueExpression) {
        this(condition, new SpringBeanValueProvider<>(validationValueExpression));
    }

    public AbstractValueProvidedValidator(Predicate<T> condition, Function<T, ?> valueProvider) {
        super(condition);
        this.valueProvider = valueProvider;
    }

    @SuppressWarnings("unchecked")
    public <R> Function<T, R> getValueProvider() {
        return (Function<T, R>) valueProvider;
    }

    protected <R> R getValidationValue(T context) {
        if (valueProvider == null) return (R) context;

        return (R) valueProvider.apply(context);
    }

    protected String getValidationValueAsString(T context) {
        Object value = getValidationValue(context);

        return value == null ? null : value.toString();
    }

}