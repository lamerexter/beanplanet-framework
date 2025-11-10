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