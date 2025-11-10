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

import org.beanplanet.messages.domain.Message;
import org.beanplanet.messages.domain.Messages;

import java.util.function.Predicate;

import static org.beanplanet.core.Predicates.truePredicate;
import static org.beanplanet.messages.domain.MessageImpl.fieldMessage;
import static org.beanplanet.messages.domain.MessageImpl.globalMessage;

/**
 * A Validator which always adds an error to the list of error messages.  This is a conditional validator which can be used
 * to conditionally add an error to the errors list.
 */
public class FixedErrorValidator<T> extends AbstractValidator<T> {
    public FixedErrorValidator(Message message) {
        super(truePredicate(), message);
    }

    public FixedErrorValidator(String messageCode, String parameterisedMessage) {
        this(truePredicate(), globalMessage(messageCode, parameterisedMessage));
    }

    public FixedErrorValidator(String field, String messageCode, String parameterisedMessage) {
        this(truePredicate(), fieldMessage(field, messageCode, parameterisedMessage));
    }

    public FixedErrorValidator(Predicate<T> condition, Message message) {
        super(condition, message);
    }

    public FixedErrorValidator(Predicate<T> condition, String messageCode, String parameterisedMessage) {
        this(condition, globalMessage(messageCode, parameterisedMessage));
    }

    public FixedErrorValidator(Predicate<T> condition, String field, String messageCode, String parameterisedMessage) {
        this(condition, fieldMessage(field, messageCode, parameterisedMessage));
    }

    @Override
    protected Messages doValidate(T object, Messages messages) {
        messages.addError(fieldMessage(getMessage().getField(),
                                       getMessage().getCode(),
                                       getMessage().getParameterisedMessage(),
                                       getMessage().getMessageParameters() != null ? getMessage().getMessageParameters() :
                                       new Object[]{
                                               object,
                                               getValidationValue(object)
                                       })
                         );
        return messages;
    }
}