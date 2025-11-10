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
package org.beanplanet.core.beans;

/**
 * An exception thrown when a requested property could not be found on a bean.
 *
 * @author Gary Watson
 * @since 19th July, 2003
 */
public class PropertyNotFoundException extends BeanException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new <code>PropertyNotFoundException</code> with no initial detail message or root cause.
     */
    public PropertyNotFoundException() {
    }

    /**
     * Creates a new <code>PropertyNotFoundException</code> with the specified detail message.
     *
     * @param message the detail message.
     */
    public PropertyNotFoundException(String message) {
        super(message);
    }

    /**
     * Creates a <code>BeanException</code> with the specified detail message and root coause.
     *
     * @param message the detail message
     * @param cause the nested root cause of the exception.
     */
    public PropertyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
