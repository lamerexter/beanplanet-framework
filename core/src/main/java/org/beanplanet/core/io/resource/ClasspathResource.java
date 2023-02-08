package org.beanplanet.core.io.resource;

import org.beanplanet.core.io.IoException;

import java.io.InputStream;

public class ClasspathResource extends AbstractResource implements ReadableResource {
    private final ClassLoader resourceClassLoader;

    private final String descriptor;

    public ClasspathResource(String descriptor, ClassLoader resourceClassLoader) {
        this.descriptor = descriptor;
        this.resourceClassLoader = resourceClassLoader;
    }

    public ClasspathResource(String descriptor, Class<?> resourceClass) {
        this(descriptor, resourceClass.getClassLoader());
    }

    public ClasspathResource(String descriptor) {
        this(descriptor, (ClassLoader) null);
    }

    /**
     * Creates a new input stream, suitable for reading the classpath resource. It is the caller's responsibility to close the
     * input stream.
     *
     * @return a newly created input stream for reading the resource (never <code>null</code>).
     * @throws IoException if an error occurs creating the stream or if the operation is not supported.
     */
    public InputStream getInputStream() throws IoException {
        InputStream resourceStream = null;

        if ( resourceClassLoader != null ) {
            resourceStream = resourceClassLoader.getResourceAsStream(descriptor);
            if (resourceStream != null) return resourceStream;
        }

        if ( Thread.currentThread().getContextClassLoader() != null ) {
            resourceStream = resourceClassLoader.getResourceAsStream(descriptor);
            if (resourceStream != null) return resourceStream;
        }

        resourceStream = getClass().getResourceAsStream(descriptor);
        if (resourceStream != null) return resourceStream;

        throw new ResourceNotFoundException("Classpath resource [" + descriptor + "] not found");
    }
}
