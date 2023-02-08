package org.beanplanet.core.io.resource;

import org.beanplanet.core.io.IoException;

public class ResourceNotFoundException extends IoException {
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new <code>ResourceNotFoundException</code> with no initial detail message or root cause.
     */
    public ResourceNotFoundException() {
    }

    /**
     * Creates a new <code>ResourceNotFoundException</code> with the specified detail message.
     *
     * @param message the detail message.
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Creates a <code>ResourceNotFoundException</code> with the specified root cause.
     *
     * @param cause the nested root cause of the exception.
     */
    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a <code>ResourceNotFoundException</code> with the specified detail message and root cause.
     *
     * @param message the detail message
     * @param cause the nested root cause of the exception.
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
