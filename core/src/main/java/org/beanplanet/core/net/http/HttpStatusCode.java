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

public class HttpStatusCode {
    public static final int OK = 200;
    public static final int BAD_REQUEST = 400;

    public static class Informational {

        public static boolean isInRange(int statusCode) {
            return statusCode >= 100 && statusCode <= 199;
        }
    }

    public static class Successful {
        public static final int OK = 200;
        public static final int CREATED = 201;
        public static final int ACCEPTED = 202;
        public static final int NON_AUTHORITATIVE = 203;
        public static final int NO_CONTENT = 204;
        public static final int RESET_CONTENT = 205;
        public static final int PARTIAL_CONTENT = 206;

        public static boolean isInRange(int statusCode) {
            return statusCode >= 200 && statusCode <= 299;
        }
    }

    public static class FurtherAction {

        public static final int NOT_MODIFIED = 304;

        public static boolean isInRange(int statusCode) {
            return statusCode >= 300 && statusCode <= 399;
        }

        public static class Redirection {

            public static final int PERMANENT = 301;
            public static final int TEMPORARY = 302;

            public static boolean isInRange(int statusCode) {
                return (statusCode >= 301 && statusCode <= 303)
                        || statusCode == 307
                        || statusCode == 308;
            }
        }

    }

    public static class ClientError {
        public static final int BAD_REQUEST = 400;
        public static final int UNAUTHORISED = 401;
        public static final int PAYMENT_REQUIRED = 402;
        public static final int FORBIDDEN = 403;
        public static final int NOT_FOUND = 404;
        public static final int METHOD_NOT_ALLOWED = 405;
        public static final int NOT_ACCEPTABLE = 406;
        public static final int PROXY_AUTH_REQUIRED = 407;
        public static final int REQUEST_TIMEOUT = 408;
        public static final int CONFLICT = 409;
        public static final int GONE = 410;
        public static final int LENGTH_REQUIRED = 411;
        public static final int PRECONDITION_FAILED = 412;
        public static final int REQUEST_ENTITY_TOO_LARGE = 413;
        public static final int REQUEST_URI_TOO_LONG = 414;
        public static final int UNSUPPORTED_MEDIA_TYPE = 415;
        public static final int REQUESTED_RANGE_MOT_SATISFIABLE = 416;
        public static final int EXPECTATION_FAILED = 417;

        public static boolean isInRange(int statusCode) {
            return statusCode >= 400 && statusCode <= 499;
        }
    }

    public static class ServerError {
        public static final int INTERNAL_SERVER_ERROR = 500;
        public static final int NOT_IMPLEMENTED = 501;
        public static final int BAD_GATEWAY = 502;
        public static final int SERVICE_UNAVAILABLE = 503;
        public static final int GATEWAY_TIMEOUT = 504;
        public static final int HTTP_VERSION_NOT_SUPPORTED = 504;

        public static boolean isInRange(int statusCode) {
            return statusCode >= 500 && statusCode <= 599;
        }
    }

    /**
     * Determines if a given HTTP response status code is considered 'informational'. A successful response is one whose response code lies within the <code>1xx</code> range of response status codes, as
     * determined by <a href="https://httpwg.org/specs/rfc9110.html#overview.of.status.codes">RFC 9110 - HTTP Semantics</a> and by this implementation.
     *
     * @param statusCode the Http response status code to be determined as an informational response.
     * @return true is the status code is determined informational, false otherwise.
     * @see HttpStatusCode.Informational#isInRange(int)
     */
    public static boolean is1xxInformational(int statusCode) {
        return Informational.isInRange(statusCode);
    }

    /**
     * Determines if a given HTTP response status code is considered 'successful'. A successful response is one whose response code lies within the <code>2xx</code> range of response status codes, as
     * determined by <a href="https://httpwg.org/specs/rfc9110.html#overview.of.status.codes">RFC 9110 - HTTP Semantics</a> and by this implementation.
     *
     * @param statusCode the Http response status code to be determined as an successful response.
     * @return true is the status code is determined successful, false otherwise.
     * @see HttpStatusCode.Successful#isInRange(int)
     */
    public static boolean is2xxSuccessful(int statusCode) {
        return Successful.isInRange(statusCode);
    }

    /**
     * Determines if a given HTTP response status code is one indicating 'further action' is required by the client - typically a redirect. A further action respnse is one whose response code lies within the <code>3xx</code> range of response status codes, as
     * determined by <a href="https://httpwg.org/specs/rfc9110.html#overview.of.status.codes">RFC 9110 - HTTP Semantics</a> and by this implementation.
     *
     * @param statusCode the Http response status code to be determined as one requiring further action.
     * @return true is the status code indicates further action is requested of the client, false otherwise.
     * @see HttpStatusCode.FurtherAction#isInRange(int)
     */
    public static boolean is3xxFurtherAction(int statusCode) {
        return FurtherAction.isInRange(statusCode);
    }

    /**
     * Determines if a given HTTP response status code is considered a 'client error'. A client error response is one whose response code lies within the <code>4xx</code> range of response status codes, as
     * determined by <a href="https://httpwg.org/specs/rfc9110.html#overview.of.status.codes">RFC 9110 - HTTP Semantics</a> and by this implementation. Typically, this
     * would mean the client has erred - for example as a result of sending an incomplete or invalid request, or one which was not understood or could not
     * be carried out at the time the request was made.
     *
     * @param statusCode the Http response status code to be determined as client error response.
     * @return true is the status code indicates a client error, false otherwise.
     * @see HttpStatusCode.ClientError#isInRange(int)
     */
    public static boolean is4xxClientError(int statusCode) {
        return ClientError.isInRange(statusCode);
    }

    /**
     * Determines if a given HTTP response status code is considered a 'server error'. A server error response is one whose response code lies within the <code>5xx</code> range of response status codes, as
     * determined by <a href="https://httpwg.org/specs/rfc9110.html#overview.of.status.codes">RFC 9110 - HTTP Semantics</a> and by this implementation. Typically, this
     * would mean the service invoked has rejected the request or is unable to carry it out - for example as a result of an internal service error, or because the client did not authenticate
     * with the service or has insufficient privileges to invoke the service.
     *
     * @param statusCode the Http response status code to be determined as server error response.
     * @return true is the status code indicates a server error, false otherwise.
     * @see HttpStatusCode.ServerError#isInRange(int)
     */
    public static boolean is5xxServerError(int statusCode) {
        return ServerError.isInRange(statusCode);
    }
}
