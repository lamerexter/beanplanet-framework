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

import java.util.function.Predicate;

/**
 * Validator which optionally holds a condition that must evaluate to true for the validation to be applied.
 *
 * @param <T>   The type of the context object containing the field being validated.
 */
public interface ConditionalValidator<T> extends Validator<T> {
    /**
     * Get the {@link Predicate} that has been set on this validator.
     *
     * @return the {@link Predicate} that has been set.  May be null.
     */
    Predicate<T> getCondition();
}
