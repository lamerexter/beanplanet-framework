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

import org.beanplanet.messages.domain.Messages;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A validator that is composed of one or more other validators, each of which may be a composite validator
 * or a terminal-node validator.
 *
 * <p>This validator supports validation on the top-level validation context object directly
 * or on a value provided by runtime-expression or other value provider. Great for nested
 * properties</p>
 *
 * @param <T> The type of the context object being validated.
 */
public class CompositeValidator<T> extends AbstractValueProvidedValidator<T> {

    private final boolean stopOnFirstError;

    private Supplier<Validator<?>[]> validatorsSupplier;

    private final Validator<?>[] EMPTY_VALIDATORS = new Validator[0];

    @SafeVarargs
    @SuppressWarnings("unchecked,rawtypes")
    public CompositeValidator(Validator<T>... validators) {
        this(null, false, (Function) null, () -> validators);
    }

    @SafeVarargs
    public CompositeValidator(String valueProviderExpression,
                              Validator<T>... validators) {
        this(null, false, valueProviderExpression, () -> validators);
    }

    @SafeVarargs
    public <R> CompositeValidator(Function<T, R> valueProvider,
                                  Validator<R>... validators) {
        this(null, false, valueProvider, () -> validators);
    }

    @SafeVarargs
    @SuppressWarnings("unchecked,rawtypes")
    public CompositeValidator(boolean stopOnFirstError, Validator<T>... validators) {
        this(null, stopOnFirstError, (Function) null, () -> validators);
    }

    @SafeVarargs
    public CompositeValidator(boolean stopOnFirstError,
                              String valueProviderExpression,
                              Validator<T>... validators) {
        this(null, stopOnFirstError, valueProviderExpression, () -> validators);
    }

    @SafeVarargs
    public <R> CompositeValidator(boolean stopOnFirstError,
                                  Function<T, R> valueProvider,
                                  Validator<R>... validators) {
        this(null, stopOnFirstError, valueProvider, () -> validators);
    }

    @SafeVarargs
    @SuppressWarnings("unchecked,rawtypes")
    public CompositeValidator(Predicate<T> condition, boolean stopOnFirstError, Validator<T>... validators) {
        this(condition, stopOnFirstError, (Function) null, validators);
    }

    @SafeVarargs
    public <R> CompositeValidator(Predicate<T> condition,
                                  boolean stopOnFirstError,
                                  String valueProviderExpression,
                                  Validator<T>... validators) {
        this(condition, stopOnFirstError, valueProviderExpression, () -> validators);
    }

    @SafeVarargs
    public <R> CompositeValidator(Predicate<T> condition,
                                  boolean stopOnFirstError,
                                  Function<T, R> valueProvider,
                                  Validator<R>... validators) {
        this(condition, stopOnFirstError, valueProvider, () -> validators);
    }

    @SuppressWarnings("unchecked")
    public <R> CompositeValidator(Predicate<T> condition,
                                  boolean stopOnFirstError,
                                  String valueProviderExpression) {
        this(condition, stopOnFirstError, valueProviderExpression, (Supplier<Validator<R>[]>) null);
    }

    @SuppressWarnings("unchecked")
    public <R> CompositeValidator(Predicate<T> condition,
                                  boolean stopOnFirstError,
                                  Function<T, R> valueProvider) {
        this(condition, stopOnFirstError, valueProvider, (Supplier<Validator<R>[]>) null);
    }

    @SuppressWarnings("unchecked")
    public <R> CompositeValidator(Predicate<T> condition,
                                  boolean stopOnFirstError,
                                  String valueProviderExpression,
                                  Supplier<Validator<R>[]> validatorsSupplier) {
        super(condition, valueProviderExpression);
        this.stopOnFirstError = stopOnFirstError;
        this.validatorsSupplier = (Supplier) validatorsSupplier;
    }

    @SuppressWarnings("unchecked")
    public <R> CompositeValidator(Predicate<T> condition,
                                  boolean stopOnFirstError,
                                  Function<T, R> valueProvider,
                                  Supplier<Validator<R>[]> validatorsSupplier) {
        super(condition, valueProvider);
        this.stopOnFirstError = stopOnFirstError;
        this.validatorsSupplier = (Supplier) validatorsSupplier;
    }

    @SuppressWarnings("unchecked")
    protected Messages doValidate(final T object, final Messages messages) {
        Object valueProvided = getValidationValue(object);

        Messages combinedMessages = messages;
        int originalErrorCount = messages.hasErrors() ? messages.getErrors().size() : 0;
        for (Validator<?> validator : getValidators()) {
            combinedMessages = ((Validator<Object>) validator).validate(valueProvided, combinedMessages);
            int errorsNow = messages.hasErrors() ? combinedMessages.getErrors().size() : 0;
            if (stopOnFirstError && (errorsNow > originalErrorCount)) {
                break;
            }
        }

        return combinedMessages;
    }

    public boolean isStopOnFirstError() {
        return stopOnFirstError;
    }

    @SuppressWarnings("unchecked")
    public <R> Validator<R>[] getValidators() {
        return (Validator<R>[]) (validatorsSupplier == null ? EMPTY_VALIDATORS : validatorsSupplier.get());
    }

    public void setValidators(Validator<?> ... validators) {
        this.validatorsSupplier = () -> validators;
    }
}
