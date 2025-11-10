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

package org.beanplanet.core.net;

import org.beanplanet.core.UncheckedException;

/**
 * The superclass of all network related exceptions.
 *
 * As most clients cannot handle or recover from network exceptions, this is an <i>unchecked</i> exception that should be
 * caught at specific and meaningful points within an application.
 */
public class NetworkException extends UncheckedException {
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new <code>NetworkException</code> with no initial detail message or root cause.
     */
    public NetworkException() {
    }

    /**
     * Creates a new <code>NetworkException</code> with the specified detail message.
     *
     * @param message the detail message.
     */
    public NetworkException(String message) {
        super(message);
    }

    /**
     * Creates a <code>NetworkException</code> with the specified root cause.
     *
     * @param cause the nested root cause of the exception.
     */
    public NetworkException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a <code>NetworkException</code> with the specified detail message and root coause.
     *
     * @param message the detail message
     * @param cause the nested root cause of the exception.
     */
    public NetworkException(String message, Throwable cause) {
        super(message, cause);
    }
}
