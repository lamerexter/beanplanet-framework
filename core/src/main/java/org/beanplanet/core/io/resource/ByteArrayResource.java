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

import java.io.*;
import java.net.URL;


/**
 * An implementation of a resource, which can be backed by a byte array or <code>{@link ByteArrayOutputStream}</code>.
 *
 * <p>
 * If backed by a <code>ByteArrayOutputStream</code> this resource is not thread safe. Any backing
 * <code>ByteArrayOutputStream</code> is reset upon each successive invocation of
 * <code>{@link #getOutputStream()}</code>.
 * </p>
 *
 * <p>
 * Naturally, this resource is readable.
 * </p>
 *
 * @author Gary Watson
 */
public class ByteArrayResource extends AbstractResource implements ReadableResource {
    /**
     * The byte array backing this resource.
     */
    private final byte[] byteArray;

    /**
     * The starting offset within the byte array backing this resource.
     */
    private int offset;

    /**
     * The number of bytes within the array backing this resource, from the offset specified.
     */
    private int length;

    private static byte[] EMPTY_ARRAY = {};


    /**
     * Constructs a <code>ByteArrayResource</code> with no initial array.
     */
    public ByteArrayResource() {
        this(EMPTY_ARRAY, 0, 0);
    }

    /**
     * Constructs a <code>ByteArrayResource</code> with the specified backing array.
     *
     * @param byteArray the byte array to back this resource.
     */
    public ByteArrayResource(byte[] byteArray) {
        this(byteArray, 0, byteArray == null ? 0 : byteArray.length);
    }

    /**
     * Constructs a <code>ByteArrayResource</code> with the specified backing array, offset
     * and number of bytes.
     *
     * @param byteArray the byte array to back this resource.
     * @param offset    the starting offset within the byte array backing this resource.
     * @param length    the number of bytes within the array backing this resource, from the offset specified.
     */
    public ByteArrayResource(byte[] byteArray, int offset, int length) {
        this.byteArray = byteArray;
        this.offset = byteArray == null ? 0 : offset;
        this.length = byteArray == null ? 0 : length;
    }

    /**
     * Returns the array backing this resource.
     *
     * @return the array backing this resource, which may be null.
     */
    public byte[] getByteArray() {
        return byteArray;
    }

    /**
     * Gets the length of the byte array backing this resource.
     *
     * @return the length of the backing byte array.
     */
    @Override
    public long getContentLength() {
        return byteArray == null ? -1 : length;
    }

    /**
     * Always throws an <code>UnsupportedOperationException</code> as this is an in-memory resource.
     *
     * @return never returns but always throws an <code>UnsupportedOperationException</code>
     * @throws UnsupportedOperationException always thrown as this resource is an in-memory resource.
     */
    @Override
    public URL getUrl() throws UnsupportedOperationException {
        throw new UnsupportedOperationException(getBaseName(ByteArrayResource.class)
                + " are in-memory resources and have no URL");
    }

    /**
     * Determines whether this resource can be read, either as byte or character-orientated stream.
     *
     * @return true if there is an array or <code>ByteArrayOutputStream</code> backing this resource, false otherwise.
     */
    @Override
    public boolean canRead() {
        return byteArray != null;
    }

    /**
     * Determines whether this resource can be written to, either as byte or character-orientated stream.
     *
     * @return true if there is a <code>ByteArrayOutputStream</code> backing this resource, false otherwise.
     */
    @Override
    public boolean canWrite() {
        return byteArray != null;
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
        return new ByteArrayInputStream(byteArray, offset, length);
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
        return new ByteArrayBackedOutputStream();
    }

    private class ByteArrayBackedOutputStream extends OutputStream {
        private int writeIndex = offset;
        private int lastIndexExcluding = offset + length;

        @Override
        public void write(int b) throws IOException {
            checkIndexOutOfBounds();
            byteArray[writeIndex++] = (byte) b;
        }

        private void checkIndexOutOfBounds() {
            if (writeIndex >= lastIndexExcluding)
                throw new IndexOutOfBoundsException("Index " + writeIndex + " out of bounds of bye array backed output stream, offset=" + offset + ", length=" + length);
        }
    }
}
