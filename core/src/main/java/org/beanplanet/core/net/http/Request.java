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
