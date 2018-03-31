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
