package org.beanplanet.core.net.http;

import org.beanplanet.core.io.resource.Resource;
import org.beanplanet.core.lang.Assert;
import org.beanplanet.core.mediatypes.MediaType;

import java.io.Reader;
import java.nio.charset.Charset;

import static org.beanplanet.core.util.StringUtil.isBlank;

/**
 * <p>The HTTP entity of an HTTP message, which has an underlying content resource.</p>
 *
 * <p>Whether the underlying resource is associated with an open stream, wraps another resource, is in-memory or is some other resource is entirely implementation dependent.</p>
 */
public interface HttpEntity {
    /**
     * Gets the resource backing the content associated with this HTTP entity.
     *
     * @return the entity's content resource, which may be null.
     */
    Resource getContent();

    /**
     * Gets the content type associated with this HTTP entity.
     *
     * @return the entity's content type, which may be null.
     */
    ContentType getContentType();

    /**
     * Gets the content type asociated with this HTTP entity.
     *
     * @return the entity's content type, which may be null.
     */
    default Charset getCharset() {
        return getContentType() != null ? getContentType().getCharset() : null;
    }

    default MediaType getMediaType() {
        return getContentType() != null ? getContentType().getMediaType() : null;
    }

    /**
     * Reads the entity body content as a string, using the given character set encoding or, if null, the encoding associated with the entity itself. Obviously
     * this assumes tbe contents of the entity to be a string of manageable size, so care must be taken when using this method.
     *
     * @param charset the character set encoding which will be used to read the entity content, which may be null in which case the encoding associated with the entity itself or the system default will be used.
     * @return the string of the contents of the entity.
     */
    default String readFullyAsString(final Charset charset) {
        Assert.notNull(getContent(), "HTTP entity does not have content");
        return getContent().readFullyAsString(charset != null ? charset : getCharset());
    }

    /**
     * Reads the entity body content as a string, using the given character set encoding or, if null, the encoding associated with the entity itself. Obviously
     * this assumes tbe contents of the entity to be a string of manageable size, so care must be taken when using this method.
     *
     * @param charset the character set encoding which will be used to read the entity content, which may be null in which case the encoding associated with the entity itself or the system default will be used.
     * @return the string of the contents of the entity.
     */
    default String readFullyAsString(final String charset) {
        Assert.notNull(getContent(), "HTTP entity does not have content");
        return isBlank(charset) ? getContent().readFullyAsString(getCharset()) : getContent().readFullyAsString(charset);
    }

    /**
     * Reads the entity body content as a string, using the encoding associated with the entity itself or the system default character set encoding. Obviously
     * this assumes tbe contents of the entity to be a string of manageable size, so care must be taken when using this method.
     *
     * @return the string of the contents of the entity.
     */
    default String readFullyAsString() {
        return readFullyAsString(getContentType() != null ? getContentType().getCharset() : null);
    }

    /**
     * Obtains a reader of the entity body content, using the encoding associated with the entity itself or the system default character set encoding.
     *
     * @return a reader of the contents of the entity.
     */
    default Reader getReader() {
        Assert.notNull(getContent(), "HTTP entity does not have content");
        return getContent().getReader(getContentType() != null ? getContentType().getCharset() : null);
    }

    /**
     * Obtains a reader of the entity body content, using the given character set encoding or, if null, the encoding associated with the entity itself.
     *
     * @param charset the character set encoding which will be used to read the entity content, which may be null in which case the encoding associated with the entity itself or the system default will be used.
     * @return the string of the contents of the entity.
     */
    default Reader getReader(final Charset charset) {
        Assert.notNull(getContent(), "HTTP entity does not have content");
        return getContent().getReader(charset != null ? charset : getCharset());
    }

    /**
     * Obtains a reader of the entity body content as a string, using the given character set encoding or, if null, the encoding associated with the entity itself. Obviously
     * this assumes tbe contents of the entity to be a string of manageable size, so care must be taken when using this method.
     *
     * @param charset the character set encoding which will be used to read the entity content, which may be null in which case the encoding associated with the entity itself or the system default will be used.
     * @return the string of the contents of the entity.
     */
    default Reader getReader(final String charset) {
        Assert.notNull(getContent(), "HTTP entity does not have content");
        return isBlank(charset) ? getContent().getReader(getCharset()) : getContent().getReader(charset);
    }
}
