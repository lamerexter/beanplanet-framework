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
package org.beanplanet.messages.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.beanplanet.core.collections.ListBuilder;
import org.beanplanet.core.util.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Definition of standard messages container.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(as = MessagesImpl.class)
public interface Messages {
    /**
     * Get all errors held in the messages container.
     *
     * @return the list of errors, or an empty list if there are no errors.
     */
    List<Message> getErrors();

    /**
     * Get all field errors held in the messages container.
     *
     * @return the list of field errors, or an empty list if there are no errors associated with fields.
     */
    default List<Message> getFieldErrors() {
        return getErrors().stream().filter(m -> StringUtil.isNotBlank(m.getField())).collect(Collectors.toList());
    }

    /**
     * Get all errors relating to the given field held in the messages container.
     *
     * @param fieldName the name of the field for which any errors are to be returned.
     * @return the list of field errors, or an empty list if there are no errors associated with fields.
     */
    default List<Message> getFieldErrors(final String fieldName) {
        return getFieldErrors().stream().filter(m -> Objects.equals(m.getField(), fieldName)).collect(Collectors.toList());
    }

    /**
     * Get all warnings held in the messages container.
     *
     * @return the list of warnings, or an empty list if there are no warnings.
     */
    List<Message> getWarnings();

    /**
     * Get all infos held in the messages container.
     *
     * @return the list of infos, or an empty list if there are no infos.
     */
    List<Message> getInfos();

    /**
     * Determines if there are no messages.
     *
     * @return true if there are no info, warning or error messages, false otherwise.
     */
    default boolean isEmpty() {
        return !(hasInfos() || hasWarnings() || hasErrors());
    }

    /**
     * Add an error with the supplied code and message text.
     *
     * @param code                 the code of the message.
     * @param parameterisedMessage the text of the message.
     * @return this container, to allow method chaining.
     */
    Messages addError(String code, String parameterisedMessage);

    /**
     * Add an error with the supplied code and message text, and arguments that will be injected into
     * the message text in accordance with {@link java.text.MessageFormat} usage.
     *
     * @param code                 the code of the message.
     * @param parameterisedMessage the text of the message.
     * @param messageParameters    parameters to be interpolated in the message text.
     * @return this container, to allow method chaining.
     */
    Messages addError(String code, String parameterisedMessage, Object... messageParameters);

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
    Messages addError(Throwable cause, String code, String parameterisedMessage, Object... messageParameters);

    /**
     * Add a pre-defined error.
     *
     * @param message the error to be added.
     * @return this container, to allow method chaining.
     */
    Messages addError(Message message);

    /**
     * Add a warning with the supplied code and message text.
     *
     * @param code                 the code of the message.
     * @param parameterisedMessage the text of the message.
     * @return this container, to allow method chaining.
     */
    Messages addWarning(String code, String parameterisedMessage);

    /**
     * Add a warning with the supplied code and message text, and arguments that will be injected into
     * the message text in accordance with {@link java.text.MessageFormat} usage.
     *
     * @param code                 the code of the message.
     * @param parameterisedMessage the text of the message.
     * @param messageParameters    parameters to be interpolated in the message text.
     * @return this container, to allow method chaining.
     */
    Messages addWarning(String code, String parameterisedMessage, Object... messageParameters);

    /**
     * Add a pre-defined warning.
     *
     * @param message the warning to be added.
     * @return this container, to allow method chaining.
     */
    Messages addWarning(Message message);

    /**
     * Add an info with the supplied code and message text.
     *
     * @param code                 the code of the message.
     * @param parameterisedMessage the text of the message.
     * @return this container, to allow method chaining.
     */
    Messages addInfo(String code, String parameterisedMessage);

    /**
     * Add an info with the supplied code and message text, and arguments that will be injected into
     * the message text in accordance with {@link java.text.MessageFormat} usage.
     *
     * @param code                 the code of the message.
     * @param parameterisedMessage the text of the message.
     * @param messageParameters    parameters to be interpolated in the message text.
     * @return this container, to allow method chaining.
     */
    Messages addInfo(String code, String parameterisedMessage, Object... messageParameters);

    /**
     * Add a pre-defined info.
     *
     * @param message the info to be added.
     * @return this container, to allow method chaining.
     */
    Messages addInfo(Message message);

    /**
     * Add an error for a field, with the supplied code and message text.
     *
     * @param field                the field to which the error applies.
     * @param code                 the code of the message.
     * @param parameterisedMessage the text of the message.
     * @return this container, to allow method chaining.
     */
    Messages addFieldError(String field, String code, String parameterisedMessage);

    /**
     * Add an error for a field, with the supplied code and message text, and arguments that will be injected into
     * the message text in accordance with {@link java.text.MessageFormat} usage.
     *
     * @param field                the field to which the error applies.
     * @param code                 the code of the message.
     * @param parameterisedMessage the text of the message.
     * @param messageParameters    parameters to be interpolated in the message text.
     * @return this container, to allow method chaining.
     */
    Messages addFieldError(String field, String code, String parameterisedMessage, Object ... messageParameters);

    /**
     * Whether there is an error matching the specified test.
     *
     * @param test the test to apply to each error in turn.
     * @return the first error matching the specified test.
     */
    default Optional<Message> findError(Predicate<Message> test) {
        if ( !hasErrors() ) return Optional.empty();

        return getErrors().stream().filter(test).findFirst();
    }

    /**
     * Whether there is an error with the code apecified.
     *
     * @param code the code of the error message to find.
     * @return the first error with the specified code.
     */
    default Optional<Message> findErrorWithCode(final String code) {
        return findError(error -> Objects.equals(error.getCode(), code));
    }

    /**
     * Whether there is an error similar to the one specified.  Ignoring thr rendered message, matching occurs against any
     * non-null value in the message specified for field mame, code, parameterised message and message parameters.
     *
     * @param prototype the message whose properties are to be used to determine if this message container has a similar
     *                  error message.
     * @return true if there exists an error with properties the same as the one specified, false otherwise.
     */
    default boolean hasErrorLike(final Message prototype) {
        return findError(error -> (prototype.getField() == null || Objects.equals(prototype.getField(), error.getField()))
                                  && (prototype.getCode() == null || Objects.equals(prototype.getCode(), error.getCode()))
                                  && (prototype.getParameterisedMessage() == null || Objects.equals(prototype.getParameterisedMessage(), error.getParameterisedMessage()))
                                  && (prototype.getMessageParameters() == null
                                      || Arrays.equals(prototype.getMessageParameters(), error.getMessageParameters()))).isPresent();
    }

    /**
     * Whether there is an error present with the code specified.
     *
     * @param code the code of the error message to find.
     * @return true if there exists an error message with the given code, false otherwise.
     */
    default boolean hasErrorWithCode(String code) {
       return findError(error -> Objects.equals(code, error.getCode())).isPresent();
    }

    /**
     * Whether there is an error present with message containing the given string.
     *
     * @param messageText the error message text whose presence in any error is to be determined.
     * @return true if there exists an error message containing the given message text, false otherwise.
     */
    default boolean hasErrorMessageContaining(final String messageText) {
        return hasErrors() && getErrors().stream().map(Message::getRenderedMessage).filter(Objects::nonNull).anyMatch(errMsg -> errMsg.contains(messageText));
    }

    /**
     * Add a warning for a field, with the supplied code and message text.
     *
     * @param field                the field to which the warning applies.
     * @param code                 the code of the message.
     * @param parameterisedMessage the text of the message.
     * @return this container, to allow method chaining.
     */
    Messages addFieldWarning(String field, String code, String parameterisedMessage);

    /**
     * Add a warning for a field, with the supplied code and message text, and arguments that will be injected into
     * the message text in accordance with {@link java.text.MessageFormat} usage.
     *
     * @param field                the field to which the warning applies.
     * @param code                 the code of the message.
     * @param parameterisedMessage the text of the message.
     * @param messageParameters    parameters to be interpolated in the message text.
     * @return this container, to allow method chaining.
     */
    Messages addFieldWarning(String field, String code, String parameterisedMessage, Object... messageParameters);

    /**
     * Add a warning for an info, with the supplied code and message text.
     *
     * @param field                the field to which the info applies.
     * @param code                 the code of the message.
     * @param parameterisedMessage the text of the message.
     * @return this container, to allow method chaining.
     */
    Messages addFieldInfo(String field, String code, String parameterisedMessage);

    /**
     * Add an info for a field, with the supplied code and message text, and arguments that will be injected into
     * the message text in accordance with {@link java.text.MessageFormat} usage.
     *
     * @param field                the field to which the info applies.
     * @param code                 the code of the message.
     * @param parameterisedMessage the text of the message.
     * @param messageParameters    parameters to be interpolated in the message text.
     * @return this container, to allow method chaining.
     */
    Messages addFieldInfo(String field, String code, String parameterisedMessage, Object... messageParameters);

    /**
     * Get the total number of messages held in this message container.
     *
     * @return the total number of messages held in this message container.
     */
    int size();

    /**
     * Get whether the container holds any error messages.
     *
     * @return true if the container holds at least one error, or false otherwise.
     */
    boolean hasErrors();

    /**
     * Determines whether there are field errors held in the messages container.
     *
     * @return true if there are field errors.
     */
    default boolean hasFieldErrors() {
        return getErrors().stream().anyMatch(m -> StringUtil.isNotBlank(m.getField()));
    }

    /**
     * Determines if there are errors for the given field in the messages container.
     *
     * @param fieldName the name of the field for which any errors are to be determined.
     * @return true if there are errors for the given field.
     */
    default boolean hasFieldErrors(final String fieldName) {
        return getFieldErrors().stream().anyMatch(m -> StringUtil.isNotBlank(m.getField())
                && Objects.equals(m.getField(), fieldName));
    }

    /**
     * Get whether the container holds any warning messages.
     *
     * @return true if the container holds at least one warning, or false otherwise.
     */
    boolean hasWarnings();

    /**
     * Get whether the container holds any info messages.
     *
     * @return true if the container holds at least one info, or false otherwise.
     */
    boolean hasInfos();

    /**
     * Merges these messages with the other messages provided, returning a new instance of {@link Messages} containing the
     * messages from both.
     *
     * @param other the other messages to be combined with these messages.
     * @return a new {@link Messages} instance containing the messages from this instance plus the messages from the other instance.
     */
    default Messages merge(final Messages other) {
        return new MessagesImpl(
                ListBuilder.<Message>builder()
                        .addAll(this.getInfos())
                        .addAll(other.getInfos())
                        .build(),
                ListBuilder.<Message>builder()
                           .addAll(this.getWarnings())
                           .addAll(other.getWarnings())
                           .build(),
                ListBuilder.<Message>builder()
                           .addAll(this.getErrors())
                           .addAll(other.getErrors())
                           .build()
        );

    }
}
