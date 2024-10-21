package org.beanplanet.springcore.io;

import org.beanplanet.core.io.resource.AbstractResource;
import org.beanplanet.core.io.resource.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URL;

/**
 * Adapts a Spring {@link org.springframework.core.io.Resource} to a Beanplanet {@link Resource}.
 */
public class SpringResourceAdapter extends AbstractResource {
    private final org.springframework.core.io.Resource adaptedResource;

    public SpringResourceAdapter(final org.springframework.core.io.Resource adaptedResource) {
        this.adaptedResource = adaptedResource;
    }

    public org.springframework.core.io.Resource getAdaptedResource() {
        return adaptedResource;
    }

    /**
     * Gets the length of the Spring resource backing this resource.
     *
     * @return the content length of the Spring resource, via a call to {@link org.springframework.core.io.Resource#contentLength()}.
     */
    @Override
    public long getContentLength() {
        try {
            return adaptedResource == null ? -1 : adaptedResource.contentLength();
        } catch (IOException e) {
            return -1;
        }
    }

    @Override
    public boolean exists() {
        return adaptedResource.exists();
    }

    @Override
    public boolean canRead() {
        return adaptedResource.isReadable();
    }

    @Override
    public String getName() {
        return adaptedResource.getFilename();
    }

    @Override
    public URI getUri() {
        try {
            return adaptedResource.getURI();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public URL getUrl() throws UnsupportedOperationException {
        try {
            return adaptedResource.getURL();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public InputStream getInputStream() {
        try {
            return adaptedResource.getInputStream();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
