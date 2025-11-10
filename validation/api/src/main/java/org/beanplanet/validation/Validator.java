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

import static org.beanplanet.messages.domain.MessagesImpl.messages;

/**
 * Definition of a simple validator.
 *
 * @param <T>   The type of the context object being validated.
 */
public interface Validator<T> {

    /**
     * Applies the validation to the given object, with a new, initially empty, instance of messages. This default
     * implementation simply calls {@link #validate(Object, Messages)} with a new messages instance to capture
     * any messages output by the validator.
     *
     * @param object        the object being validated.
     * @return              the messages container in its post-validation state.
     * @see #validate(Object, Messages)
     */
    default Messages validate(T object) {
        return validate(object, messages());
    }

    /**
     * Applies the validation to the given object.
     *
     * @param object        the object being validated.
     * @param messages      standard messages container to which any validation messages can be added.
     * @return              the messages container in its post-validation state.
     */
    Messages validate(T object, Messages messages);
}