package org.beanplanet.core.net.http;

import org.beanplanet.core.io.resource.Resource;

/**
 * A very basic HTTP enity which cimply wraps an underlying content resource.
 */
public class ContentWrappingHttpEntity extends AbstractHttpEntity {
    protected ContentWrappingHttpEntity() {
        this(null);
    }

    /**
     * Creates an HTTP entity which wraps the content provided.
     *
     * @param content the underlying content of this entity.
     */
    public ContentWrappingHttpEntity(final Resource content) {
        super(content, null);
    }

    /**
     * Creates an HTTP entity which wraps the content provided.
     *
     * @param content entity body content.
     * @param contentType the content type associated with the entity body, which may be null.
     */
    public ContentWrappingHttpEntity(final Resource content, final ContentType contentType) {
        super(content, contentType);
    }
}
