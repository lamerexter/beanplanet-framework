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

import static org.beanplanet.core.lang.TypeUtil.getBaseName;

import org.beanplanet.core.io.IoException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;


/**
 * An implementation of a resource which is backed by a <code>{@link ByteArrayOutputStream}</code>.
 */
public class ByteArrayOutputStreamResource extends AbstractResource implements ReadableResource {
    /** The <code>{@link ByteArrayOutputStream}</code> backing this resource. */
    protected ByteArrayOutputStream byteArrayOutputStream;

    /**
     * Constructs a <code>ByteArrayResource</code> with no initial array.
     */
    public ByteArrayOutputStreamResource() {
        this(new ByteArrayOutputStream());
    }

    /**
     * Constructs a <code>ByteArrayResource</code> with the specified backing <code>{@link ByteArrayOutputStream}</code>.
     *
     * @param byteArrayOutputStream the byte array to back this resource.
     */
    public ByteArrayOutputStreamResource(final ByteArrayOutputStream byteArrayOutputStream) {
        this.byteArrayOutputStream = byteArrayOutputStream;
    }

    /**
     * Returns the <code>ByteArrayOutputStream</code> backing this resource.
     *
     * @return the <code>ByteArrayOutputStream</code> backing this resource, which may be null.
     */
    public ByteArrayOutputStream getByteArrayOutputStream() {
        return byteArrayOutputStream;
    }

    /**
     * Always throws an <code>UnsupportedOperationException</code> as this is an in-memory resource.
     *
     * @return never returns but always throws an <code>UnsupportedOperationException</code>
     * @exception UnsupportedOperationException always thrown as this resource is an in-memory resource.
     */
    @Override
    public URL getUrl() throws UnsupportedOperationException {
        throw new UnsupportedOperationException(getBaseName(ByteArrayOutputStreamResource.class)
            + " are in-memory resources and have no URL");
    }

    /**
     * Determines whether this resource can be read, either as byte or character-orientated stream.
     *
     * @return true if there is an array or <code>ByteArrayOutputStream</code> backing this resource, false otherwise.
     */
    @Override
    public boolean canRead() {
        return byteArrayOutputStream != null;
    }

    /**
     * Determines whether this resource can be written to, either as byte or character-orientated stream.
     *
     * @return true if there is a <code>ByteArrayOutputStream</code> backing this resource, false otherwise.
     */
    @Override
    public boolean canWrite() {
        return byteArrayOutputStream != null;
    }

    /**
     * Creates a new input stream, suitable for reading the resource. It is the caller's responsibility to close the
     * input stream.
     *
     * @return a newly created input stream for reading the resource.
     * @throws IoException if an error occurs creating the stream or if the operation is not supported.
     */
    @Override
    public InputStream getInputStream() throws IoException {
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    /**
     * Creates a new output stream, suitable for writing to the resource. It is the caller's responsibility to close the
     * output stream.
     *
     * @return a newly created output stream for writing to the resource.
     * @throws IoException if an error occurs creating the stream or if the operation is not supported.
     */
    @Override
    public OutputStream getOutputStream() throws IoException {
        return byteArrayOutputStream;
    }
}
