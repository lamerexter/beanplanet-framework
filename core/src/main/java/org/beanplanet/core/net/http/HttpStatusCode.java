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

    public static class Redirection {

        public static boolean isInRange(int statusCode) {
            return statusCode >= 300 && statusCode <= 399;
        }
    }

    public static class ClientError {
        public static final int BAD_REQUEST = 400;

        public static boolean isInRange(int statusCode) {
            return statusCode >= 400 && statusCode <= 499;
        }
    }

    public static class ServerError {

        public static boolean isInRange(int statusCode) {
            return statusCode >= 500 && statusCode <= 599;
        }
    }

    public static boolean isInformational(int statusCode) {
        return Informational.isInRange(statusCode);
    }

    /**
     * Determoines if a given HTTP response status code is considered 'successful'. A successful response is one whose response code lies within the <code>2xx</code> range of success codes, as
     * determined by <a href="https://httpwg.org/specs/rfc9110.html#overview.of.status.codes">RFC 9110 - HTTP Semantics</a> and by this implementation.
     *
     * @param statusCode the Http status code to be determined as a successful response.
     * @return true is the status code is determined successful, false otherwise.
     * @see Successful#isInRange(int)
     */
    public static boolean isSuccessful(int statusCode) {
        return Successful.isInRange(statusCode);
    }

    public static boolean isRedirection(int statusCode) {
        return Redirection.isInRange(statusCode);
    }

    public static boolean isClientError(int statusCode) {
        return ClientError.isInRange(statusCode);
    }

    public static boolean isServerError(int statusCode) {
        return ServerError.isInRange(statusCode);
    }
}
