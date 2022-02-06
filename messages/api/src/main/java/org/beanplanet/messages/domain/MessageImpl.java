/*
 *  MIT Licence:
 *
 *  Copyright (C) 2019 Beanplanet Ltd
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

package org.beanplanet.messages.domain;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.beanplanet.core.util.PropertyBasedToStringBuilder;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Objects;

/**
 * Standard message implementation.
 */
@JsonInclude(NON_EMPTY)
public class MessageImpl implements Message {

    /**
     * The object associated with the message, which may be null.
     */
    private Object relatedObject;

    /**
     * A possible cause of this message.
     */
    private Throwable cause;


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
        return globalMessage(code, parameterisedMessage, (Object[])null);
    }

    /**
     * Static factory method to conveniently create a global message.
     *
     * @param code                 the canonical code of this message.
     * @param parameterisedMessage the text of this message, with placeholders for supplied parameters.
     * @param messageParameters    arguments to be injected into any placeholders in the message text.
     */
    public static final MessageImpl globalMessage(String code, String parameterisedMessage, Object... messageParameters) {
        return globalMessage(null, code, parameterisedMessage, messageParameters);
    }

    /**
     * Static factory method to conveniently create a global message.
     *
     * @param relatedObject        the object associated with the message, which may be null.
     * @param code                 the canonical code of this message.
     * @param parameterisedMessage the text of this message, with placeholders for supplied parameters.
     * @param messageParameters    arguments to be injected into any placeholders in the message text.
     */
    public static final MessageImpl globalMessage(Object relatedObject, String code, String parameterisedMessage, Object... messageParameters) {
        return globalMessage(null, relatedObject, code, parameterisedMessage, messageParameters);
    }

    /**
     * Static factory method to conveniently create a global message.
     *
     * @param cause                a related cause of this message, which may be null.
     * @param relatedObject        the object associated with the message, which may be null.
     * @param code                 the canonical code of this message.
     * @param parameterisedMessage the text of this message, with placeholders for supplied parameters.
     * @param messageParameters    arguments to be injected into any placeholders in the message text.
     */
    public static final MessageImpl globalMessage(Throwable cause, Object relatedObject, String code, String parameterisedMessage, Object... messageParameters) {
        return new MessageImpl(cause, relatedObject, null, code, parameterisedMessage, messageParameters);
    }

    /**
     * Static factory method to conveniently create a field-related message.
     *
     * @param field                the field to which the message applies.
     * @param code                 the canonical code of this message.
     * @param parameterisedMessage the text of this message, with placeholders for supplied parameters.
     */
    public static final MessageImpl fieldMessage(String field, String code, String parameterisedMessage) {
        return fieldMessage(field, code, parameterisedMessage, (Object[])null);
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
        return fieldMessage(null, null, field, code, parameterisedMessage, messageParameters);
    }

    /**
     * Static factory method to conveniently create a field-related message.
     *
     * @param relatedObject        the object associated with the message, which may be null.
     * @param field                the field to which the message applies.
     * @param code                 the canonical code of this message.
     * @param parameterisedMessage the text of this message, with placeholders for supplied parameters.
     * @param messageParameters    arguments to be injected into any placeholders in the message text.
     */
    public static final MessageImpl fieldMessage(Object relatedObject, String field, String code, String parameterisedMessage, Object... messageParameters) {
        return fieldMessage(null, relatedObject, field, code, parameterisedMessage, messageParameters);
    }

    /**
     * Static factory method to conveniently create a field-related message.
     *
     * @param cause                a related cause of this message, which may be null.
     * @param field                the field to which the message applies.
     * @param field                the field to which the message applies.
     * @param code                 the canonical code of this message.
     * @param parameterisedMessage the text of this message, with placeholders for supplied parameters.
     * @param messageParameters    arguments to be injected into any placeholders in the message text.
     */
    public static final MessageImpl fieldMessage(Throwable cause, Object relatedObject, String field, String code, String parameterisedMessage, Object... messageParameters) {
        return new MessageImpl(cause, relatedObject, field, code, parameterisedMessage, messageParameters);
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
        this(null, field, code, parameterisedMessage, messageParameters);
    }

    /**
     * Construct a new parameterised message for a field, with parameters that will be injected into
     * the message text in accordance with {@link MessageFormat} usage.
     *
     * @param relatedObject        the object associated with the message, which may be null.
     * @param field                the field to which the message applies.
     * @param code                 the canonical code of this message.
     * @param parameterisedMessage the text of this message, with placeholders for supplied parameters.
     * @param messageParameters    arguments to be injected into any placeholders in the message text.
     */
    public MessageImpl(Object relatedObject, String field, String code, String parameterisedMessage, Object... messageParameters) {
        this.relatedObject = relatedObject;
        this.field = field;
        this.code = code;
        this.parameterisedMessage = parameterisedMessage;
        this.messageParameters = messageParameters;
    }

    /**
     * Construct a new parameterised message for a field, with parameters that will be injected into
     * the message text in accordance with {@link MessageFormat} usage.
     *
     * @param cause                a related cause of the message.
     * @param relatedObject        the object associated with the message, which may be null.
     * @param field                the field to which the message applies.
     * @param code                 the canonical code of this message.
     * @param parameterisedMessage the text of this message, with placeholders for supplied parameters.
     * @param messageParameters    arguments to be injected into any placeholders in the message text.
     */
    public MessageImpl(Throwable cause, Object relatedObject, String field, String code, String parameterisedMessage, Object... messageParameters) {
        this.cause = cause;
        this.relatedObject = relatedObject;
        this.field = field;
        this.code = code;
        this.parameterisedMessage = parameterisedMessage;
        this.messageParameters = messageParameters;
    }

    /**
     * The object associated with the message.
     *
     * @return the object referred to by this message, which may be null.
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getRelatedObject() {
        return (T)relatedObject;
    }

    /**
     * The cause of this message.
     *
     * @return the exception related to this message, which may be null indicating none.
     */
    public Throwable getCause() {
        return cause;
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
               Arrays.equals(messageParameters, message1.messageParameters) &&
               Objects.equals(relatedObject, message1.relatedObject) &&
               Objects.equals(cause, message1.cause);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, code, parameterisedMessage, messageParameters);
    }

    @Override
    public String toString() {
        return new PropertyBasedToStringBuilder(this).build();
    }
}
