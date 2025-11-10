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
package org.beanplanet.messages.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Definition of message which may be either a 'global' message or a 'field-related' message.  A global message is not
 * related to a field and is therefore a general message.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(as = MessageImpl.class)
public interface Message {
    /**
     * The object associated with the message.
     *
     * @return the object referred to by this message, which may be null.
     */
    <T> T getRelatedObject();

    /**
     * The related cause of the message.
     *
     * @return the cause of the message, which may be null.
     */
    Throwable getCause();

    /**
     * Get the field to which the message applies, if a field has been set.
     *
     * @return the name of field field, which may be null if this message is a global message not associated with any field.
     */
    String getField();

    /**
     * Get the message code, which is expected to be a canonical code for the domain.
     *
     * @return the message code.
     */
    String getCode();

    /**
     * Get the parameterised text which has been set for this message.
     *
     * @return the message text, which may contain parameter placeholders for interpolation.
     */
    String getParameterisedMessage();

    /**
     * Get the rendered text which has been set for this message. The message text is rendered in the default locale.
     *
     * @return the message text, which may contain parameter placeholders for interpolation.
     */
    String getRenderedMessage();

    /**
     * Get any message parameters that have been set for this message.  If set, these will be
     * interpolated in the message text in accordance with {@link java.text.MessageFormat} usage.
     *
     * @return the message parameters.
     */
    Object[] getMessageParameters();

    static Builder builder() { return new Builder(); }

    class Builder {
        private Object relatedObject;
        private Throwable cause;
        private String field;
        private String code;
        private String message;
        private Object[] parameters;

        @SuppressWarnings("unchecked")
        public <T> T getRelatedObject() {
            return (T)relatedObject;
        }

        public Throwable getCause() {
            return cause;
        }

        public Builder cause(Throwable cause) {
            this.cause = cause;
            return this;
        }

        public Builder relatedObject(Object relatedObject) {
            this.relatedObject = relatedObject;
            return this;
        }

        public Message withRelatedObject(Object relatedObject) {
            this.relatedObject = relatedObject;
            return build();
        }

        public String getField() {
            return field;
        }

        public Builder field(String field) {
            this.field = field;
            return this;
        }

        public Message withField(String field) {
            this.field = field;
            return build();
        }

        public String getCode() {
            return code;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Message withCode(String code) {
            this.code = code;
            return build();
        }

        public Builder message(Message message) {
            if (message.getField() != null) field(message.getField());
            if (message.getCode() != null) code(message.getCode());
            if (message.getParameterisedMessage() != null) parameterisedMessage(message.getParameterisedMessage());
            if (message.getMessageParameters() != null) parameters(message.getMessageParameters());
            if (message.getRelatedObject() != null) relatedObject(message.getRelatedObject());
            return this;
        }

        public Message withMessage(Message message) {
            return message(message).build();
        }

        public String getParameterisedMessage() {
            return message;
        }

        public Builder parameterisedMessage(String message) {
            this.message = message;
            return this;
        }

        public Message withParameterisedMessage(String message) {
            return parameterisedMessage(message).build();
        }

        public Object[] getParameters() {
            return parameters;
        }

        public Builder parameters(Object ... paremeters) {
            this.parameters = paremeters;
            return this;
        }

        public Message withParameters(Object ... paremeters) {
            this.parameters = paremeters;
            return build();
        }

        public Message build() {
            return new MessageImpl(cause, relatedObject, field, code, message, parameters);
        }
    }
}
