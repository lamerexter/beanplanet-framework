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

package org.beanplanet.core.net.http;

/**
 * An HTTP response message, as defined in the <a href="https://www.rfc-editor.org/rfc/rfc2616">HTTP RFC Specification</a>.
 */
public interface Response extends HttpMessage {
    /**
     * Returns the HTTP Response status line information: code and reason phrase.
     *
     * @return he HTTP Response status line information, which will never be null.
     */
    ResponseStatus getStatus();

    /**
     * Returns the HTTP response status code.
     *
     * @return the HTTP response status code.
     */
    default int getStatusCode() {
        return getStatus().getCode();
    }

    /**
     * Returns the phrase associated with the given status code.
     *
     * @return the HTTP response reason phrase, which may be null.
     */
     default String getReasonPhrase() {
         return getStatus().getReason();
     }
}
