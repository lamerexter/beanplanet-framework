package org.beanplanet.core.net.http.converter;

import org.beanplanet.core.UncheckedException;

/**
 * The superclass of HTTP message body conversion exceptions.
 */
public class HttpMessageBodyConversionException extends UncheckedException {
    public HttpMessageBodyConversionException(final String message) {
        super(message);
    }
}
