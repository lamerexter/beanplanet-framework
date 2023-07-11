package org.beanplanet.core.net.http;

public enum HttpStatus {
    Continue(100, "Continue"),
    Switching(101, "Switching"),
    Ok(200, "OK"),
    Created(201, "Created"),
    Accepted(202, "Accepted"),
    Non_Authoritative(203, "Non-Authoritative Information"),
    No_Content(204, "No Content"),
    Reset_Content(205, "Reset Content"),
    Partial_Content(206, "Partial Content"),
    Multiple_Choices(300, "Multiple Choices"),
    Moved_Permanently(301, "Moved Permanently"),
    Found(302, "Found"),
    See_Other(303, "See Other"),
    Not_Modified(304, "Not Modified"),
    Use_Proxy(305, "Use Proxy"),
    Unused_306(306, "Unused"),
    Temporary_Redirect(307, "Temporary Redirect"),
    Permanent_Redirect(308, "Permanent Redirect"),
    Bad_Request(400, "Bad Request"),
    Unauthorized(401, "Unauthorized"),
    Payment_Required(402, "Payment Required"),
    Forbidden(403, "Forbidden"),
    Not_Found(404, "Not Found"),
    Method_Not_Allowed(405, "Method Not Allowed"),
    Not_Acceptable(406, "Not Acceptable"),
    Proxy_Authentication_Required(407, "Proxy Authentication Required"),
    Request_Timeout(408, "Request Timeout"),
    Conflict(409, "Conflict"),
    Gone(410, "Gone"),
    Length_Required(411, "Length Required"),
    Precondition_Failed(412, "Precondition Failed"),
    Content_Too_Large(413, "Content Too Large"),
    Uri_Too_Long(414, "URI Too Long"),
    Unsupported_Media_Type(415, "Unsupported Media Type"),
    Range_Not_Satisfiable(416, "Range Not Satisfiable"),
    Expectation_Failed(417, "Expectation Failed"),
    Unused_418(418, "Unused"),
    Misdirected_Request(421, "Misdirected Request"),
    Unprocessable_Content(422, "Unprocessable Content"),
    Upgrade_Required(426, "Upgrade Required"),
    Internal_Server_Error(500, "Internal Server Error"),
    Not_Implemented(501, "Not Implemented"),
    Bad_Gateway(502, "Bad Gateway"),
    Service_Unavailable(503, "Service Unavailable"),
    Gateway_Timeout(504, "Gateway Timeout"),
    HTTP_Version_Not_Supported(505, "HTTP Version Not Supported");

    private final int code;
    private final String reasonPhrase;

    HttpStatus(int code, final String reasonPhrase) {
        this.code = code;
        this.reasonPhrase = reasonPhrase;
    }

}
