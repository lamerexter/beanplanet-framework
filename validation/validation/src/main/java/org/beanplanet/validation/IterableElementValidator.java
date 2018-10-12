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

public class IterableElementValidator<T> extends AbstractValueProvidedValidator<T> {
    private boolean stopOnFirstError;

    private Validator<?> validators[];

    public <E, R extends Iterable<E>> IterableElementValidator(Function<T, R> iterableValueProvider,
                                                               Validator<E> ... validators) {
        super(iterableValueProvider);
        this.stopOnFirstError = stopOnFirstError;
        this.validators = validators;
    }

    public <E, R extends Iterable<E>> IterableElementValidator(String iterableValueProvider,
                                                               Validator<E> ... validators) {
        super(iterableValueProvider);
        this.stopOnFirstError = stopOnFirstError;
        this.validators = validators;
    }

    public <E, R extends Iterable<E>> IterableElementValidator(boolean stopOnFirstError,
                                                               Function<T, R> iterableValueProvider,
                                                               Validator<E> ... validators) {
        super(iterableValueProvider);
        this.stopOnFirstError = stopOnFirstError;
        this.validators = validators;
    }

    public <E, R extends Iterable<E>> IterableElementValidator(boolean stopOnFirstError,
                                                               String iterableValueProvider,
                                                               Validator<E> ... validators) {
        super(iterableValueProvider);
        this.stopOnFirstError = stopOnFirstError;
        this.validators = validators;
    }

    public <E, R extends Iterable<E>> IterableElementValidator(Predicate<T> condition,
                                                               boolean stopOnFirstError,
                                                               Function<T, R> iterableValueProvider,
                                                               Validator<E> ... validators) {
        super(condition, iterableValueProvider);
        this.stopOnFirstError = stopOnFirstError;
        this.validators = validators;
    }

    public <E, R extends Iterable<E>> IterableElementValidator(Predicate<T> condition,
                                                               boolean stopOnFirstError,
                                                               String iterableValueProvider,
                                                               Validator<E> ... validators) {
        super(condition, iterableValueProvider);
        this.stopOnFirstError = stopOnFirstError;
        this.validators = validators;
    }

    /**
     * Carry out validation.  This method delegates to extending classes.
     *
     * @param object   the object being validated.
     * @param messages standard messages container to which will be added validation messages.
     * @return the messages container provided, containing any new messages post-validation.
     */
    @SuppressWarnings("unchecked")
    @Override
    protected Messages doValidate(T object, Messages messages) {
        Iterable<?> iterableProvided = getValidationValue(object);
        if ( iterableProvided == null) return messages;

        int originalErrorCount = messages.hasErrors() ? messages.getErrors().size() : 0;

        for (Object valueProvided : iterableProvided) {
            for (Validator<?> validator : validators) {
                ((Validator<Object>)validator).validate(valueProvided, messages);
                int errorsNow = messages.hasErrors() ? messages.getErrors().size() : 0;
                if (stopOnFirstError && (errorsNow >  originalErrorCount)) {
                    break;
                }
            }
        }

        return messages;
    }
}
