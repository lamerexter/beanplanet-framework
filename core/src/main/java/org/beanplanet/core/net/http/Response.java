package org.beanplanet.core.net.http;

/**
 * An HTTP response message, as defined in the <a href="https://www.rfc-editor.org/rfc/rfc2616">HTTP RFC Specification</a>.
 */
public interface Response extends HttpMessage {
    /**
     * Returns the HTTP Response status line information: code and reason phrase.
     *
     * @return he HTTP Response status line information, which will never be null.
     */
    ResponseStatus getStatus();

    /**
     * Returns the HTTP response status code.
     *
     * @return the HTTP response status code.
     */
    default int getStatusCode() {
        return getStatus().getCode();
    }

    /**
     * Returns the phrase associated with the given status code.
     *
     * @return the HTTP response reason phrase, which may be null.
     */
     default String getReasonPhrase() {
         return getStatus().getReason();
     }
}
