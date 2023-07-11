package org.beanplanet.core.net.http;

import org.beanplanet.core.io.resource.ByteArrayResource;
import org.beanplanet.core.io.resource.Resource;

/**
 * An HTTP entity which represents an arbitrary stream of binary data such as, but no limited to, the media type <code>application/octet-stream</code>.
 */
public class ArbitraryDataEntity extends ContentWrappingHttpEntity {
    protected ArbitraryDataEntity() {
        this((Resource)null);
    }

    /**
     * Creates an arbitrary content entity from the array specified.
     *
     * @param content the underlying content of this entity.
     */
    public ArbitraryDataEntity(final byte[] content) {
        this(content, null);
    }

    /**
     * Creates an arbitrary content entity from the array specified.
     *
     * @param content the underlying content of this entity.
     * @param contentType the content type associated with the entity body, which may be null.
     */
    public ArbitraryDataEntity(final byte[] content, final ContentType contentType) {
        this(new ByteArrayResource(content), contentType);
    }

    /**
     * Creates an arbitrary content entity which wraps the content specified.
     *
     * @param content the underlying content of this entity.
     */
    public ArbitraryDataEntity(final Resource content) {
        this(content, null);
    }

    /**
     * Creates an arbitrary content entity which wraps the content specified.
     *
     * @param content entity body content.
     * @param contentType the content type associated with the entity body, which may be null.
     */
    public ArbitraryDataEntity(final Resource content, final ContentType contentType) {
        super(content, contentType);
    }
}
