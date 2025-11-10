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
