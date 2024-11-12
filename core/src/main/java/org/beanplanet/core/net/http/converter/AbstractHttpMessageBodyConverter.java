package org.beanplanet.core.net.http.converter;

import org.beanplanet.core.net.http.HttpMessageHeaders;
import org.beanplanet.core.net.http.MediaType;
import org.beanplanet.core.net.http.HttpMessage;
import org.beanplanet.core.util.ObjectUtil;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.Arrays.asList;

public abstract class AbstractHttpMessageBodyConverter<T> implements HttpMessageBodyConverter<T> {
    /** The default character applied by this handler to textual messages, when no other is specified. */
    private final Charset defaultCharset;
    /** The set of media types this handler can read and write. */
    private final Set<MediaType> supportedMediaTypes;

    protected AbstractHttpMessageBodyConverter(final Charset defaultCharset,
                                               final Set<MediaType> supportedMediaTypes) {
        this.defaultCharset = defaultCharset;
        this.supportedMediaTypes = Collections.unmodifiableSet(supportedMediaTypes);
    }

    protected AbstractHttpMessageBodyConverter(Set<MediaType> supportedMediaTypes) {
        this(null, Collections.unmodifiableSet(supportedMediaTypes));
    }
    protected AbstractHttpMessageBodyConverter(MediaType ... supportedMediaTypes) {
        this(new LinkedHashSet<>(asList(supportedMediaTypes)));
    }

    /**
     * Gets the default character applied by this handler to textual messages, when no other is specified.
     *
     * @return the default character set used by the handler, which may be null if none has been specified.
     */
    public Charset getDefaultCharset() {
        return defaultCharset;
    }

    /**
     * Determines the character set to use for the given message. This will be determined in the following order:
     * <o1>
     *     <li><code>Content-Type</code> header <code>charset</code> encoding, if present</li>
     *     <li>from {@link #defaultCharset}, if configured</li>
     *     <li>from the system default</li>
     * </o1>
     *
     * @param messageHeaders the headers of the HTTP message from which to determine the applicable character set.
     * @return the default character set used by the handler, which may be null if none has been specified.
     */
    public Charset charsetFor(final HttpMessageHeaders messageHeaders) {
        return ObjectUtil.firstNonNull(
                () -> messageHeaders.getContentType()
                             .flatMap(m -> m.getParameters().get("charset").map(Charset::forName))
                             .orElse(null),
                () -> defaultCharset,
                Charset::defaultCharset
        );
    }

    /**
     * Returns the set of media types this handler can read and write.
     *
     * @return the media types supported by this handler.
     */
    @Override
    public Set<MediaType> getSupportedMediaTypes() {
        return supportedMediaTypes;
    }
}
