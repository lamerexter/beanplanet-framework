/*
 * Copyright (c) 2001-present the original author or authors (see NOTICE herein).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
