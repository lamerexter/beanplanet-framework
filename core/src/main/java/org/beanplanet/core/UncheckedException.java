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

package org.beanplanet.core;

/**
 * The superclass of all unchecked exceptions.
 */
public class UncheckedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new unchecked exception with no initial detail message or root cause.
     */
    public UncheckedException() {}

    /**
     * Creates a new unchecked exception with the specified detail message.
     *
     * @param message the detail message.
     */
    public UncheckedException(String message) {
        super(message);
    }

    /**
     * Creates a unchecked exception with the specified root coause.
     *
     * @param cause the nested root cause of the exception.
     */
    public UncheckedException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a unchecked exception with the specified detail message and root coause.
     *
     * @param message the detail message
     * @param cause the nested root cause of the exception.
     */
    public UncheckedException(String message, Throwable cause) {
        super(message, cause);
    }
}
