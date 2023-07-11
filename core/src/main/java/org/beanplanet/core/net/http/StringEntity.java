package org.beanplanet.core.net.http;

import org.beanplanet.core.io.resource.Resource;
import org.beanplanet.core.io.resource.StringResource;

/**
 * A very basic HTTP entity which simply wraps an underlying content resource.
 */
public class StringEntity extends ContentWrappingHttpEntity {
    protected StringEntity() {
        this((Resource)null);
    }

    /**
     * Creates a text-based HTTP entity which wraps the given string content provided.
     *
     * @param content the underlying content of this entity.
     */
    public StringEntity(final CharSequence content) {
        this(content, null);
    }

    /**
     * Creates a text-based HTTP entity which wraps the given string content provided.
     *
     * @param content the underlying content of this entity.
     * @param contentType the content type associated with the entity body, which may be null.
     */
    public StringEntity(final CharSequence content, final ContentType contentType) {
        this(new StringResource(content == null ? null : content.toString()), contentType);
    }

    /**
     * Creates an HTTP entity which wraps the content provided.
     *
     * @param content the underlying content of this entity.
     */
    public StringEntity(final Resource content) {
        super(content, null);
    }

    /**
     * Creates an HTTP entity which wraps the content provided.
     *
     * @param content entity body content.
     * @param contentType the content type associated with the entity body, which may be null.
     */
    public StringEntity(final Resource content, final ContentType contentType) {
        super(content, contentType);
    }
}
