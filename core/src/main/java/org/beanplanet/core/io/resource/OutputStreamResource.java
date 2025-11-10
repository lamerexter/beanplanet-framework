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
import org.beanplanet.core.lang.TypeUtil;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import static org.beanplanet.core.lang.TypeUtil.getBaseName;

/**
 * A writable resource backed by an output stream. This is a single-use resource.
 */
public class OutputStreamResource extends AbstractResource implements ReadableResource {
    /**
     * The output stream backing this resource.
     */
    protected OutputStream outputStream;

    /**
     * Constructs a <code>InputStreamResource</code> with no input stream.
     */
    public OutputStreamResource() {
    }

    /**
     * Constructs a <code>ByteArrayResource</code> with the specified backing array.
     *
     * @param outputStream the output stream backing this resource.
     */
    public OutputStreamResource(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * Sets the inout stream  backing this resource.
     *
     * @param outputStream the output stream backing this resource.
     */
    public void setInputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * Always returns <code>false</code> as this is an in-memory resource.
     *
     * @return always <code>false</code> since this resource is an in-memory resource.
     */
    public boolean exists() {
        return false;
    }

    /**
     * Always throws an <code>UnsupportedOperationException</code> as this is an in-memory resource.
     *
     * @return never returns but always throws an <code>UnsupportedOperationException</code>
     * @throws UnsupportedOperationException always thrown as this resource is an in-memory resource.
     */
    @Override
    public URL getUrl() throws UnsupportedOperationException {
        throw new UnsupportedOperationException(getBaseName(OutputStreamResource.class)
                + " are in-memory resources and have no URL");
    }

    /**
     * Determines whether this resource can be read, either as byte or character-orientated stream.
     *
     * @return true if there is an array or <code>ByteArrayOutputStream</code> backing this resource, false otherwise.
     */
    @Override
    public boolean canRead() {
        return false;
    }

    /**
     * Determines whether this resource can be written to, either as byte or character-orientated stream.
     *
     * @return true if there is a <code>ByteArrayOutputStream</code> backing this resource, false otherwise.
     */
    @Override
    public boolean canWrite() {
        return outputStream != null;
    }

    /**
     * Returns the input stream backing this resource.
     *
     * @return the existing input stream backing this resource, which may be null;
     */
    @Override
    public InputStream getInputStream() throws IoException {
        throw new UnsupportedOperationException(TypeUtil.getBaseName(OutputStreamResource.class)
                + " are in-memory resources and have no output stream");
    }

    /**
     * Creates a new output stream, suitable for writing to the resource. It is the caller's responsibility to close the
     * output stream.
     *
     * @return a newly created output stream for writing to the resource.
     */
    @Override
    public OutputStream getOutputStream() throws IoException {
        return outputStream;
    }
}
