package org.beanplanet.core.net.http;

import org.beanplanet.core.io.resource.Resource;

import java.util.Optional;

import static org.beanplanet.core.net.http.HttpHeaders.CONTENT_TYPE;

/**
 * An HTTP message, as defined in the <a href="https://www.rfc-editor.org/rfc/rfc2616">HTTP RFC Specification</a>.
 */
public interface HttpMessage {
    /**
     * Gets the headers associated with the message.
     *
     * @return the HTTP headers of the message, never null but may be empty.
     */
    HttpHeaders getHeaders();

    /**
     * Gets the body of the HTTP message.
     *
     * @return the HTTP message body, which may be null if there is none.
     */
    Resource getBody();

    /**
     * Determines the content/media type of the message through the presence of a {@link HttpHeaders#CONTENT_TYPE}
     * header.
     *
     * @return the content type determined for the message, which may be empty indicating none was evident.
     */
    default Optional<MediaType> getContentType() {
        String contentType = getHeaders().getLast(CONTENT_TYPE);
        return Optional.ofNullable(contentType == null ? null : new MediaType(contentType));
    }
}
