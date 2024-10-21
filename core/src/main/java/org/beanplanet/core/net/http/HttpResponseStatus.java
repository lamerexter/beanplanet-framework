package org.beanplanet.core.net.http;

/**
 * The HTTP Response status line information: code and reason phrase.
 */
public class HttpResponseStatus implements ResponseStatus {
    /**
     * The HTTP response status code.
     */
    private final int statusCode;

    /**
     * The phrase associated with the given status code.
     */
    private final String reasonPhrase;

    public HttpResponseStatus(int statusCode, String reasonPhrase) {
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
    }

    /**
     * Gets the HTTP response status code.
     *
     * @return the HTTP response status code.
     */
    public int getCode() {
        return statusCode;
    }

    /**
     * Gets the phrase associated with the given status code.
     *
     * @return the phrase associated with the given status code.
     */
    public String getReason() {
        return reasonPhrase;
    }

    /**
     * Determines if a given HTTP response status code is considered 'informational'. A successful response is one whose response code lies within the <code>1xx</code> range of response status codes, as
     * determined by <a href="https://httpwg.org/specs/rfc9110.html#overview.of.status.codes">RFC 9110 - HTTP Semantics</a> and by this implementation.
     *
     * @param status the Http response status to be determined as an informational response.
     * @return true is the status code is determined informational, false otherwise.
     * @see HttpStatusCode.Informational#isInRange(int)
     */
    public static boolean is1xxInformational(ResponseStatus status) {
        return HttpStatusCode.is1xxInformational(status.getCode());
    }

    /**
     * Determines if a given HTTP response status code is considered 'successful'. A successful response is one whose response code lies within the <code>2xx</code> range of response status codes, as
     * determined by <a href="https://httpwg.org/specs/rfc9110.html#overview.of.status.codes">RFC 9110 - HTTP Semantics</a> and by this implementation.
     *
     * @param status the Http response status to be determined as an successful response.
     * @return true is the status code is determined successful, false otherwise.
     * @see HttpStatusCode.Successful#isInRange(int)
     */
    public static boolean is2xxSuccessful(ResponseStatus status) {
        return HttpStatusCode.is2xxSuccessful(status.getCode());
    }

    /**
     * Determines if a given HTTP response status code is an HTTP 200-Ok success.
     *
     * @param status the Http response status to be determined as an HTTP 200-Ok response.
     * @return true is the status code was HTTP 200, false otherwise.
     * @see HttpStatusCode.Successful#is2xxSuccessful(int)
     */
    public static boolean is200Ok(ResponseStatus status) {
        return status.getCode() == HttpStatusCode.Successful.OK;
    }

    /**
     * Determines if a given HTTP response status code is an HTTP 201-Created success.
     *
     * @param status the Http response status to be determined as an HTTP 201-Created response.
     * @return true is the status code was HTTP 201, false otherwise.
     * @see HttpStatusCode.Successful#is2xxSuccessful(int)
     */
    public static boolean is201Created(ResponseStatus status) {
        return status.getCode() == HttpStatusCode.Successful.CREATED;
    }

    /**
     * Determines if a given HTTP response status code is an HTTP 202-Accepted success.
     *
     * @param status the Http response status to be determined as an HTTP 202-Accepted response.
     * @return true is the status code was HTTP 202, false otherwise.
     * @see HttpStatusCode.Successful#is2xxSuccessful(int)
     */
    public static boolean is202Accepted(ResponseStatus status) {
        return status.getCode() == HttpStatusCode.Successful.ACCEPTED;
    }

    /**
     * Determines if a given HTTP response status code is an HTTP 201-Created or 202-Accepted success.
     *
     * @param status the Http response status to be determined as either HTTP 201-Created or 202-Accepted response.
     * @return true is the status code was either HTTP 201 or HTTP 202, false otherwise.
     * @see #is201Created(ResponseStatus)
     * @see #is202Accepted(ResponseStatus)
     */
    public static boolean is201CreatedOr202Accepted(ResponseStatus status) {
        return is201Created(status) || is202Accepted(status);
    }

    /**
     * Determines if a given HTTP response status code is one indicating 'further action' is required by the client - typically a redirect. A further action response is one whose response code lies within the <code>3xx</code> range of response status codes, as
     * determined by <a href="https://httpwg.org/specs/rfc9110.html#overview.of.status.codes">RFC 9110 - HTTP Semantics</a> and by this implementation.
     *
     * @param status the Http response status to be determined as one requiring further action.
     * @return true is the status code indicates further action is requested of the client, false otherwise.
     * @see HttpStatusCode.FurtherAction#isInRange(int)
     */
    public static boolean is3xxFurtherAction(ResponseStatus status) {
        return HttpStatusCode.is3xxFurtherAction(status.getCode());
    }

    /**
     * Determines if a given HTTP response status code is one indicating the client should redirect the request.
     *
     * @param status the Http response status to be determined as one being a redirect.
     * @return true is the status code indicates the client should redirect teh request, false otherwise.
     * @see HttpStatusCode.FurtherAction.Redirection#isInRange(int)
     */
    public static boolean is3xxRedirection(ResponseStatus status) {
        return HttpStatusCode.FurtherAction.Redirection.isInRange(status.getCode());
    }

    /**
     * Determines if a given HTTP response status code is one indicating a permanent redirect.
     *
     * @param status the Http response status to be determined as one requiring teh client to permanently redirect its request.
     * @return true is the status code indicates the client should permanently redirect requests with the URI permanently, false otherwise.
     * @see HttpStatusCode.FurtherAction.Redirection#isInRange(int)
     */
    public static boolean is301MovedPermanently(ResponseStatus status) {
        return status.getCode() == HttpStatusCode.FurtherAction.Redirection.PERMANENT;
    }

    /**
     * Determines if a given HTTP response status code is one indicating a temporary redirect.
     *
     * @param status the Http response status to be determined as one requiring teh client to temporarily redirect its request.
     * @return true is the status code indicates the client should permanently redirect requests with the URI temporarily, false otherwise.
     * @see HttpStatusCode.FurtherAction.Redirection#isInRange(int)
     */
    public static boolean is302MovedTemporarily(ResponseStatus status) {
        return status.getCode() == HttpStatusCode.FurtherAction.Redirection.TEMPORARY;
    }

    /**
     * Determines if a given HTTP response status code is one indicating the requested resource has not changed or
     * otherwise been modified.
     *
     * @param status the Http response status to be determined as being one indicating the requested resource has not been modified.
     * @return true is the status code indicates the requested resource has not been modified, false otherwise.
     */
    public static boolean is304NotModified(ResponseStatus status) {
        return status.getCode() == HttpStatusCode.FurtherAction.NOT_MODIFIED;
    }

    /**
     * Determines if a given HTTP response status code is considered a 'client error'. A client error response is one whose response code lies within the <code>4xx</code> range of response status codes, as
     * determined by <a href="https://httpwg.org/specs/rfc9110.html#overview.of.status.codes">RFC 9110 - HTTP Semantics</a> and by this implementation. Typically, this
     * would mean the client has erred - for example as a result of sending an incomplete or invalid request, or one which was not understood or could not
     * be carried out at the time the request was made.
     *
     * @param status the Http response status to be determined as client error response.
     * @return true is the status code indicates a client error, false otherwise.
     * @see HttpStatusCode.ClientError#isInRange(int)
     */
    public static boolean is4xxClientError(ResponseStatus status) {
        return HttpStatusCode.is4xxClientError(status.getCode());
    }

    /**
     * Determines if a given HTTP response status code is an HTTP 400-BadRequest - typically received when the client has erred, for example as a result of sending an incomplete or invalid request, or one which was not understood or could not
     * be carried out at the time the request was made.
     *
     * @param status the Http response status to be determined as client error response 400-BadRequest.
     * @return true is the status code indicates an HTTP 400 client error, false otherwise.
     * @see HttpStatusCode.ClientError#isInRange(int)
     */
    public static boolean is400BadRequest(ResponseStatus status) {
        return status.getCode() == HttpStatusCode.ClientError.BAD_REQUEST;
    }

    /**
     * Determines if a given HTTP response status code is an HTTP 401-Unauthorised - typically received when the client has failed to authenticate
     * properly with the requested service.
     *
     * @param status the Http response status to be determined as client error response 401-Unauthorised.
     * @return true is the status code indicates an HTTP 401 client error, false otherwise.
     * @see HttpStatusCode.ClientError#isInRange(int)
     */
    public static boolean is401Unauthorised(ResponseStatus status) {
        return status.getCode() == HttpStatusCode.ClientError.UNAUTHORISED;
    }

    /**
     * Determines if a given HTTP response status code is an HTTP 401-Forbidden - typically received when the client has
     * made a valid request but the server refuses or forbids it.
     *
     * @param status the Http response status to be determined as client error response 403-Forbidden.
     * @return true is the status code indicates an HTTP 403 client error, false otherwise.
     * @see HttpStatusCode.ClientError#isInRange(int)
     */
    public static boolean is403Forbidden(ResponseStatus status) {
        return status.getCode() == HttpStatusCode.ClientError.FORBIDDEN;
    }

    /**
     * Determines if a given HTTP response status code is an HTTP 404-Not-Found - received when the client has
     * made request for a resource on the server which does not exist.
     *
     * @param status the Http response status to be determined as client error response 404-Not-Found.
     * @return true is the status code indicates an HTTP 404 client error, false otherwise.
     * @see HttpStatusCode.ClientError#isInRange(int)
     */
    public static boolean is404NotFound(ResponseStatus status) {
        return status.getCode() == HttpStatusCode.ClientError.NOT_FOUND;
    }

    /**
     * Determines if a given HTTP response status code is an HTTP 405-Method-Not-Allowed - received when the client has
     * made request using a request method specifically not allowed for the resource type.
     *
     * @param status the Http response status to be determined as client error response 405-Method-Not-Allowed.
     * @return true is the status code indicates an HTTP 405 client error, false otherwise.
     * @see HttpStatusCode.ClientError#isInRange(int)
     */
    public static boolean is405MethodNotAllowed(ResponseStatus status) {
        return status.getCode() == HttpStatusCode.ClientError.METHOD_NOT_ALLOWED;
    }

    /**
     * Determines if a given HTTP response status code is an HTTP 406-Not-Acceptable - received when the client has
     * made request,with an <code>Accept</code> header, for a resource the server cannot fulfil with those criteria.
     *
     * @param status the Http response status to be determined as client error response 406-Not-Acceptable.
     * @return true is the status code indicates an HTTP 406 client error, false otherwise.
     * @see HttpStatusCode.ClientError#isInRange(int)
     */
    public static boolean is406NotAcceptable(ResponseStatus status) {
        return status.getCode() == HttpStatusCode.ClientError.NOT_ACCEPTABLE;
    }

    /**
     * Determines if a given HTTP response status code is an HTTP 409-Conflict - received when the client has
     * made a request which could not be completed owing to the current state of the resource.
     *
     * @param status the Http response status to be determined as client error response 409-Conflict.
     * @return true is the status code indicates an HTTP 409 client error, false otherwise.
     * @see HttpStatusCode.ClientError#isInRange(int)
     */
    public static boolean is409Conflict(ResponseStatus status) {
        return status.getCode() == HttpStatusCode.ClientError.CONFLICT;
    }

    /**
     * Determines if a given HTTP response status code is an HTTP 413-Request-Entity-Too-Large - received when the client has
     * made request with an entity body deemed too large by the server.
     *
     * @param status the Http response status to be determined as client error response 407-Request-Entity-Too-Large.
     * @return true is the status code indicates an HTTP 407 client error, false otherwise.
     * @see HttpStatusCode.ClientError#isInRange(int)
     */
    public static boolean is413RequestEntityTooLarge(ResponseStatus status) {
        return status.getCode() == HttpStatusCode.ClientError.REQUEST_ENTITY_TOO_LARGE;
    }

    /**
     * Determines if a given HTTP response status code is an HTTP 415-Unsupported-Media-type - received when the client has
     * made request for a resource using a specific media type not supported by the server.
     *
     * @param status the Http response status to be determined as client error response 407-Request-Entity-Too-Large.
     * @return true is the status code indicates an HTTP 407 client error, false otherwise.
     * @see HttpStatusCode.ClientError#isInRange(int)
     */
    public static boolean is415UnsupportedMediaType(ResponseStatus status) {
        return status.getCode() == HttpStatusCode.ClientError.UNSUPPORTED_MEDIA_TYPE;
    }

    /**
     * Determines if a given HTTP response status code is considered a 'server error'. A server error response is one whose response code lies within the <code>5xx</code> range of response status codes, as
     * determined by <a href="https://httpwg.org/specs/rfc9110.html#overview.of.status.codes">RFC 9110 - HTTP Semantics</a> and by this implementation. Typically, this
     * would mean the service invoked has rejected the request or is unable to carry it out - for example as a result of an internal service error, or because the client did not authenticate
     * with the service or has insufficient privileges to invoke the service.
     *
     * @param status the Http response status to be determined as server error response.
     * @return true is the status code indicates a server error, false otherwise.
     * @see HttpStatusCode.ServerError#isInRange(int)
     */
    public static boolean is5xxServerError(ResponseStatus status) {
        return HttpStatusCode.is5xxServerError(status.getCode());
    }

    /**
     * Determines if a given HTTP response status code is an HTTP 500-Internal-Service-Error - received when the client has
     * made request for a resource on the server which does not exist.
     *
     * @param status the Http response status to be determined as client error response 500-Internal-Service-Error.
     * @return true is the status code indicates an HTTP 500 server error, false otherwise.
     * @see HttpStatusCode.ClientError#isInRange(int)
     */
    public static boolean is500InternalServiceError(ResponseStatus status) {
        return status.getCode() == HttpStatusCode.ServerError.INTERNAL_SERVER_ERROR;
    }
}
