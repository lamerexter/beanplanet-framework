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

import org.beanplanet.core.util.PropertyBasedToStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;
import static org.beanplanet.messages.domain.MessageImpl.fieldMessage;
import static org.beanplanet.messages.domain.MessageImpl.globalMessage;

/**
 * Standard messages container implementation.
 */
public class MessagesImpl implements Messages {
    /**
     * Holds Informational messages.
     */
    private List<Message> infos;

    /**
     * Holds Warning messages.
     */
    private List<Message> warnings;

    /**
     * Holds Error messages.
     */
    private List<Message> errors;

    /**
     * Static factory method for creating an initially empty messages container.
     */
    public static final MessagesImpl messages() {
        return new MessagesImpl();
    }

    /**
     * Construct an empty message container - one containing no messages.
     */
    public MessagesImpl() {
    }

    /**
     * Construct a message container pre-loaded with any or all of errors, warnings and informational messages.
     *
     * @param infos    the infos the container will be initialised with.
     * @param warnings the warnings the container will be initialised with.
     * @param errors   the errors the container will be initialised with.
     */
    public MessagesImpl(List<Message> infos, List<Message> warnings, List<Message> errors) {
        this.infos = infos;
        this.warnings = warnings;
        this.errors = errors;
    }

    /**
     * Get all errors held in the messages container.
     *
     * @return the list of errors, or an empty list if there are no errors.
     */
    @Override
    public List<Message> getErrors() {
        return errors == null ? emptyList() : errors;
    }

    /**
     * Get all warnings held in the messages container.
     *
     * @return the list of warnings, or an empty list if there are no warnings.
     */
    @Override
    public List<Message> getWarnings() {
        return warnings == null ? emptyList() : warnings;
    }

    /**
     * Get all infos held in the messages container.
     *
     * @return the list of infos, or an empty list if there are no infos.
     */
    @Override
    public List<Message> getInfos() {
        return infos == null ? emptyList() : infos;
    }

    /**
     * Add an info with the supplied code and message text.
     *
     * @param code                 the code of the message.
     * @param parameterisedMessage the text of the message.
     * @return this container, to allow method chaining.
     */
    @Override
    public MessagesImpl addInfo(String code, String parameterisedMessage) {
        return addInfo(code, parameterisedMessage, (Object[])null);
    }

    /**
     * Add an info with the supplied code and message text, and arguments that will be injected into
     * the message text in accordance with {@link java.text.MessageFormat} usage.
     *
     * @param code                 the code of the message.
     * @param parameterisedMessage the text of the message.
     * @param messageParameters    arguments to be injected into any placeholders in the message text.
     * @return this container, to allow method chaining.
     */
    @Override
    public MessagesImpl addInfo(String code, String parameterisedMessage, Object... messageParameters) {
        return addInfo(globalMessage(code, parameterisedMessage, messageParameters));
    }

    /**
     * Add a warning for an info, with the supplied code and message text.
     *
     * @param field                the field to which the info applies.
     * @param code                 the code of the message.
     * @param parameterisedMessage the text of the message.
     * @return this container, to allow method chaining.
     */
    @Override
    public MessagesImpl addFieldInfo(String field, String code, String parameterisedMessage) {
        return addFieldInfo(field, code, parameterisedMessage, (Object[])null);
    }

    /**
     * Add an info for a field, with the supplied code and message text, and arguments that will be injected into
     * the message text in accordance with {@link java.text.MessageFormat} usage.
     *
     * @param field                the field to which the info applies.
     * @param code                 the code of the message.
     * @param parameterisedMessage the text of the message.
     * @param messageParameters    arguments to be injected into any placeholders in the message text.
     * @return this container, to allow method chaining.
     */
    @Override
    public MessagesImpl addFieldInfo(String field, String code, String parameterisedMessage, Object... messageParameters) {
        return addInfo(fieldMessage(field, code, parameterisedMessage, messageParameters));
    }

    /**
     * Add a pre-defined info.
     *
     * @param message the info to be added.
     * @return this container, to allow method chaining.
     */
    @Override
    public MessagesImpl addInfo(Message message) {
        if (infos == null) {
            infos = new ArrayList<>();
        }
        infos.add(message);
        return this;
    }

    /**
     * Add a warning with the supplied code and message text.
     *
     * @param code                 the code of the message.
     * @param parameterisedMessage the text of the message.
     * @return this container, to allow method chaining.
     */
    @Override
    public MessagesImpl addWarning(String code, String parameterisedMessage) {
        return addWarning(code, parameterisedMessage, (Object[]) null);
    }

    /**
     * Add a warning with the supplied code and message text, and arguments that will be injected into
     * the message text in accordance with {@link java.text.MessageFormat} usage.
     *
     * @param code                 the code of the message.
     * @param parameterisedMessage the text of the message.
     * @param messageParameters    arguments to be injected into any placeholders in the message text.
     * @return this container, to allow method chaining.
     */
    @Override
    public MessagesImpl addWarning(String code, String parameterisedMessage, Object... messageParameters) {
        return addWarning(globalMessage(code, parameterisedMessage, messageParameters));
    }

    /**
     * Add a warning for a field, with the supplied code and message text.
     *
     * @param field                the field to which the warning applies.
     * @param code                 the code of the message.
     * @param parameterisedMessage the text of the message.
     * @return this container, to allow method chaining.
     */
    @Override
    public MessagesImpl addFieldWarning(String field, String code, String parameterisedMessage) {
        return addFieldWarning(field, code, parameterisedMessage, (Object[])null);
    }

    /**
     * Add a warning for a field, with the supplied code and message text, and arguments that will be injected into
     * the message text in accordance with {@link java.text.MessageFormat} usage.
     *
     * @param field                the field to which the warning applies.
     * @param code                 the code of the message.
     * @param parameterisedMessage the text of the message.
     * @param messageParameters    arguments to be injected into any placeholders in the message text.
     * @return this container, to allow method chaining.
     */
    @Override
    public MessagesImpl addFieldWarning(String field, String code, String parameterisedMessage, Object... messageParameters) {
        return addWarning(fieldMessage(field, code, parameterisedMessage, messageParameters));
    }

    /**
     * Add a pre-defined warning.
     *
     * @param message the warning to be added.
     * @return this container, to allow method chaining.
     */
    @Override
    public MessagesImpl addWarning(Message message) {
        if (warnings == null) {
            warnings = new ArrayList<>();
        }
        warnings.add(message);
        return this;
    }

    /**
     * Add an error with the supplied code and message text.
     *
     * @param code                 the code of the message.
     * @param parameterisedMessage the text of the message.
     * @return this container, to allow method chaining.
     */
    @Override
    public MessagesImpl addError(String code, String parameterisedMessage) {
        return addError(code, parameterisedMessage, (Object[])null);
    }

    /**
     * Add an error with the supplied code and message text, and arguments that will be injected into
     * the message text in accordance with {@link java.text.MessageFormat} usage.
     *
     * @param code                 the code of the message.
     * @param parameterisedMessage the text of the message.
     * @param messageParameters    arguments to be injected into any placeholders in the message text.
     * @return this container, to allow method chaining.
     */
    @Override
    public MessagesImpl addError(String code, String parameterisedMessage, Object... messageParameters) {
        return addError(globalMessage(code, parameterisedMessage, messageParameters));
    }

    /**
     * Add an error with the supplied cause, code and message text, and arguments that will be injected into
     * the message text in accordance with {@link java.text.MessageFormat} usage.
     *
     * @param cause                the cause of the message.
     * @param code                 the code of the message.
     * @param parameterisedMessage the text of the message.
     * @param messageParameters    parameters to be interpolated in the message text.
     * @return this container, to allow method chaining.
     */
    @Override
    public Messages addError(Throwable cause, String code, String parameterisedMessage, Object... messageParameters) {
        return addError(globalMessage(cause, null, code, parameterisedMessage, messageParameters));
    }

    /**
     * Add an error for a field, with the supplied code and message text.
     *
     * @param field                the field to which the error applies.
     * @param code                 the code of the message.
     * @param parameterisedMessage the text of the message.
     * @return this container, to allow method chaining.
     */
    @Override
    public MessagesImpl addFieldError(String field, String code, String parameterisedMessage) {
        return addFieldError(field, code, parameterisedMessage, (Object[])null);
    }

    /**
     * Add an error for a field, with the supplied code and message text, and arguments that will be injected into
     * the message text in accordance with {@link java.text.MessageFormat} usage.
     *
     * @param field                the field to which the error applies.
     * @param code                 the code of the message.
     * @param parameterisedMessage the text of the message.
     * @param messageParameters    arguments to be injected into any placeholders in the message text.
     * @return this container, to allow method chaining.
     */
    @Override
    public MessagesImpl addFieldError(String field, String code, String parameterisedMessage, Object... messageParameters) {
        return addError(fieldMessage(field, code, parameterisedMessage, messageParameters));
    }

    /**
     * Add a pre-defined error.
     *
     * @param message the error to be added.
     * @return this container, to allow method chaining.
     */
    @Override
    public MessagesImpl addError(Message message) {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        errors.add(message);
        return this;
    }

    /**
     * Get the total number of messages held in this message container.
     *
     * @return the total number of messages held in this message container.
     */
    @Override
    public int size() {
        return (infos == null ? 0 : infos.size())
               + (warnings == null ? 0 : warnings.size())
               + (errors == null ? 0 : errors.size());
    }

    /**
     * Get whether the container holds any error messages.
     *
     * @return true if the container holds at least one error, or false otherwise.
     */
    @Override
    public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }

    /**
     * Get whether the container holds any warning messages.
     *
     * @return true if the container holds at least one warning, or false otherwise.
     */
    @Override
    public boolean hasWarnings() {
        return warnings != null && !warnings.isEmpty();
    }

    /**
     * Get whether the container holds any info messages.
     *
     * @return true if the container holds at least one info, or false otherwise.
     */
    @Override
    public boolean hasInfos() {
        return infos != null && !infos.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessagesImpl messages = (MessagesImpl) o;
        return Objects.equals(errors, messages.errors) &&
               Objects.equals(warnings, messages.warnings) &&
               Objects.equals(infos, messages.infos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errors, warnings, infos);
    }

    @Override
    public String toString() {
        {
            return new PropertyBasedToStringBuilder(this).build();
        }
    }
}
