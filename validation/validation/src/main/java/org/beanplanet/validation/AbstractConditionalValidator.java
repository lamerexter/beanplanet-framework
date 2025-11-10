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

import org.beanplanet.core.util.PropertyBasedToStringBuilder;
import org.beanplanet.messages.domain.Messages;

import java.util.function.Predicate;

/**
 * Validator which performs its validation only if a given predicate answers true.
 */
public abstract class AbstractConditionalValidator<T> implements ConditionalValidator<T> {

    /**
     * An optional condition that must evaluate to true for the validation to be applied.
     */
    private Predicate<T> condition;

    /**
     * Constructs a conditional that always performs validation.
     */
    public AbstractConditionalValidator() {
    }

    /**
     * Constructs a conditional validator with a supplied condition predicate.
     *
     * @param condition a {@link Predicate} that governs whether the validation is applied.
     */
    public AbstractConditionalValidator(Predicate<T> condition) {
        this.condition = condition;
    }

    /**
     * Conditionally-validate the context object, according to the result of evaluating this validator's
     * condition.
     *
     * @param object   the object being validated.
     * @param messages standard messages container to which will be added validation messages.
     * @return the messages container provided, containing any new messages post-validation.
     */
    @Override
    public final Messages validate(T object, Messages messages) {
        if (condition == null || condition.test(object)) {
            return doValidate(object, messages);
        }
        return messages;
    }

    @Override
    public Predicate<T> getCondition() {
        return condition;
    }

    /**
     * Carry out validation.  This method delegates to extending classes.
     *
     * @param object   the object being validated.
     * @param messages standard messages container to which will be added validation messages.
     * @return the messages container provided, containing any new messages post-validation.
     */
    protected abstract Messages doValidate(T object, Messages messages);

    @Override
    public String toString() {
        return new PropertyBasedToStringBuilder(this).build();
    }
}
