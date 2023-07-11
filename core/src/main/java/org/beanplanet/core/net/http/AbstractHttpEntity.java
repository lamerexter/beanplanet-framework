package org.beanplanet.core.net.http;

import org.beanplanet.core.io.resource.Resource;

/**
 * Base implementation of an HTTP Entity, as defined by <a href="https://www.rfc-editor.org/rfc/rfc2616">RFC2616</a>.
 */
public abstract class AbstractHttpEntity implements HttpEntity {
    /** The entity body content. */
    private final Resource content;
    /** The content type associated with the entity body. */
    private final ContentType contentType;

    protected AbstractHttpEntity() { this(null, null); }

    /**
     * Creates an HTTP entity with the given content and content tyoe.
     *
     * @param content entity body content.
     * @param contentType the content type associated with the entity body, which may be null.
     */
    public AbstractHttpEntity(Resource content, ContentType contentType) {
        this.content = content;
        this.contentType = contentType;
    }

    /**
     * Gets the entity body content.
     * @return the entity body content.
     */
    @Override
    public Resource getContent() {
        return content;
    }

    /**
     * Gets the content type associated with the entity body.
     * @return the content type associated with the entity body, which may be null if unknown.
     */
    public ContentType getContentType() {
        return contentType;
    }
}
