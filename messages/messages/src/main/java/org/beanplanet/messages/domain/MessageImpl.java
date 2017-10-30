/*
 * Copyright (C) 2017 Beanplanet Ltd
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.beanplanet.messages.domain;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Standard message implementation.
 */
public class MessageImpl implements Message {

    /**
     * The field to which this message applies.  May be left unset for a general message.
     */
    private String field;

    /**
     * The canonical code for this message.
     */
    private String code;

    /**
     * The text of this message, which may be parameterised in accordance with {@link MessageFormat} usage.
     */
    private String parameterisedMessage;

    /**
     * Optional message parameters which, if supplied, will be interpolated in the message text
     * in accordance with {@link MessageFormat} usage.
     */
    private Object[] messageParameters;

    /**
     * Static factory method to conveniently create a global message.
     *
     * @param code                 the canonical code of this message.
     * @param parameterisedMessage the text of this message, without any parameterisation.
     */
    public static final MessageImpl globalMessage(String code, String parameterisedMessage) {
        return globalMessage(code, parameterisedMessage, null);
    }

    /**
     * Static factory method to conveniently create a global message.
     *
     * @param code                 the canonical code of this message.
     * @param parameterisedMessage the text of this message, with placeholders for supplied parameters.
     * @param messageParameters    arguments to be injected into any placeholders in the message text.
     */
    public static final MessageImpl globalMessage(String code, String parameterisedMessage, Object... messageParameters) {
        return new MessageImpl(null, code, parameterisedMessage, messageParameters);
    }

    /**
     * Static factory method to conveniently create a field-related message.
     *
     * @param field                the field to which the message applies.
     * @param code                 the canonical code of this message.
     * @param parameterisedMessage the text of this message, with placeholders for supplied parameters.
     */
    public static final MessageImpl fieldMessage(String field, String code, String parameterisedMessage) {
        return fieldMessage(field, code, parameterisedMessage, null);
    }

    /**
     * Static factory method to conveniently create a field-related message.
     *
     * @param field                the field to which the message applies.
     * @param code                 the canonical code of this message.
     * @param parameterisedMessage the text of this message, with placeholders for supplied parameters.
     * @param messageParameters    arguments to be injected into any placeholders in the message text.
     */
    public static final MessageImpl fieldMessage(String field, String code, String parameterisedMessage, Object... messageParameters) {
        return new MessageImpl(field, code, parameterisedMessage, messageParameters);
    }

    /**
     * Construct a new parameterised message for a field, with parameters that will be injected into
     * the message text in accordance with {@link MessageFormat} usage.
     *
     * @param field                the field to which the message applies.
     * @param code                 the canonical code of this message.
     * @param parameterisedMessage the text of this message, with placeholders for supplied parameters.
     * @param messageParameters    arguments to be injected into any placeholders in the message text.
     */
    public MessageImpl(String field, String code, String parameterisedMessage, Object... messageParameters) {
        this.field = field;
        this.code = code;
        this.parameterisedMessage = parameterisedMessage;
        this.messageParameters = messageParameters;
    }

    /**
     * Get the field to which the message applies, if a field has been set.
     *
     * @return the fieldname.
     */
    public String getField() {
        return field;
    }

    /**
     * Get the message code, which is expected to be a canonical code for the domain.
     *
     * @return the message code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Get the parameterised text which has been set for this message.
     *
     * @return the message text, which may contain parameter placeholders for interpolation.
     */
    public String getParameterisedMessage() {
        return parameterisedMessage;
    }

    /**
     * Get the rendered text which has been set for this message. The message text is rendered in the default locale.
     *
     * @return the message text, which may contain parameter placeholders for interpolation.
     */
    public String getRenderedMessage() {
        return messageParameters == null ? parameterisedMessage : MessageFormat.format(parameterisedMessage, messageParameters);
    }

    /**
     * Get any message parameters that have been set for this message.  If set, these will be
     * interpolated in the message text in accordance with {@link java.text.MessageFormat} usage.
     *
     * @return the message parameters.
     */
    public Object[] getMessageParameters() {
        return messageParameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageImpl message1 = (MessageImpl) o;
        return Objects.equals(field, message1.field) &&
               Objects.equals(code, message1.code) &&
               Objects.equals(parameterisedMessage, message1.parameterisedMessage) &&
               Arrays.equals(messageParameters, message1.messageParameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, code, parameterisedMessage, messageParameters);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.reflectionToString(this);
    }
}
