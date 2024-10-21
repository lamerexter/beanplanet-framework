package org.beanplanet.core.net.http;

import java.net.URI;

/**
 * An HTTP request message, as defined in the <a href="https://www.rfc-editor.org/rfc/rfc2616">HTTP RFC Specification</a>.
 */
public interface Request extends HttpMessage {
    /**
     * Enumeration of the most common HTTP request methods. Note, there may also be "extension-method" - broadly a
     * string token, not covered here, and most like client-server specific.
     */
    enum Method {
        CONNECT, DELETE, GET, HEAD, OPTIONS, PATCH, POST, PUT, TRACE;
    }

    /**
     * Enumeration of the most common HTTP request methods. Note, there may also be "extension-method" - broadly a
     * string token, not covered here, and most like client-server specific.
     */
    enum Version {
        HTTP_1_0, HTTP_1_1, HTTP_2;
    }

    /**
     * Returns the HTTP method of the request, which will usually be one of those specified by {@link Method}.
     *
     * @return the HTTP method, which will never be null.
     */
    String getMethod();

    /**
     * Returns the URI of the HTTP request.
     *
     * @return the HTTP request URI, which will never be null.
     */
     URI getUri();

    /**
     * Returns the protocol version of the HTTP request, which will usually be one of those specified by {@link Version}.
     *
     * @return the HTTP version, which will never be null.
     */
    Version getHttpVersion();
}
