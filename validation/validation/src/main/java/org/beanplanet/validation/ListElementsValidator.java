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

import org.beanplanet.core.models.Pair;
import org.beanplanet.core.models.Triple;
import org.beanplanet.messages.domain.Messages;
import org.beanplanet.messages.domain.MessagesImpl;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.beanplanet.core.models.Pair.pairOf;

public class ListElementsValidator<T, E> extends AbstractValueProvidedValidator<T> {
    private final Validator<Pair<Integer, E>>[] delegateValidators;

    private final Function<Triple<Integer, E, Messages>, Messages> messageTransformer;

    /**
     * Conditionally performs validation of a provided value from an enclosing or parent object, by delegating
     * to the given delegates.
     *
     * @param condition          a predicate which determines when validation should be performed on the parent object,
     *                           which may be null to indicate always.
     * @param valueProvider      a supplier of the value to be validated, as a function of the parent object.
     * @param delegateValidators a list of the delegate validators called to validate the computed value.
     */
    @SafeVarargs
    public ListElementsValidator(final Predicate<T> condition,
                                 final Function<T, List<E>> valueProvider,
                                 final Function<Triple<Integer, E, Messages>, Messages> messageTransformer,
                                 final Validator<Pair<Integer, E>>... delegateValidators) {
        super(condition, valueProvider);
        this.messageTransformer = messageTransformer;
        this.delegateValidators = delegateValidators;
    }

    /**
     * Carry out validation.  This method delegates to extending classes.
     *
     * @param object   the object being validated.
     * @param messages standard messages container to which will be added validation messages.
     * @return the messages container provided, containing any new messages post-validation.
     */
    @Override
    protected Messages doValidate(final T object, final Messages messages) {
        if (delegateValidators == null) return messages;

        List<E> valueProvided = getValidationValue(object);

        Messages combinedMessages = messages;
        for (Validator<Pair<Integer, E>> validator : delegateValidators) {
            for (int n=0; n < valueProvided.size(); n++) {
                final E element = valueProvided.get(n);
                Messages validatorMessages = validator.validate(pairOf(n, element), MessagesImpl.messages());

                if (messageTransformer != null) {
                    validatorMessages = messageTransformer.apply(Triple.tripleOf(n, element, validatorMessages));
                }

                combinedMessages = combinedMessages.merge(validatorMessages);
            }
        }

        return combinedMessages;
    }
}
