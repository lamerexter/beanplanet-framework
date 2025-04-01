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