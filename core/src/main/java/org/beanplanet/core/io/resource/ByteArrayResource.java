/*
 *  MIT Licence:
 *
 *  Copyright (C) 2018 Beanplanet Ltd
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without restriction
 *  including without limitation the rights to use, copy, modify, merge,
 *  publish, distribute, sublicense, and/or sell copies of the Software,
 *  and to permit persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 *  KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 *  PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 *  CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  DEALINGS IN THE SOFTWARE.
 */

package org.beanplanet.core.io.resource;

import org.beanplanet.core.io.IoException;
import org.beanplanet.core.lang.Assert;
import org.beanplanet.core.models.path.NamePath;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import static org.beanplanet.core.lang.TypeUtil.getBaseName;


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
 *
 */
public class ByteArrayResource extends AbstractResource implements ReadableResource {
    /** The byte array backing this resource. */
    protected byte[] byteArray;

    /** The <code>{@link ByteArrayOutputStream}</code> backing this resource. */
    protected ByteArrayOutputStream byteArrayOutputStream;

    /**
     * Constructs a <code>ByteArrayResource</code> with no initial array.
     */
    public ByteArrayResource() {
    }

    /**
     * Constructs a <code>ByteArrayResource</code> with the specified backing array.
     *
     * @param byteArray the byte array to back this resource.
     */
    public ByteArrayResource(byte[] byteArray) {
        setByteArray(byteArray);
    }

    /**
     * Constructs a <code>ByteArrayResource</code> with the specified backing <code>{@link ByteArrayOutputStream}</code>.
     *
     * @param byteArrayOutputStream the byte array to back this resource.
     */
    public ByteArrayResource(ByteArrayOutputStream byteArrayOutputStream) {
        setByteArrayOutputStream(byteArrayOutputStream);
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
     * Returns the <code>ByteArrayOutputStream</code> backing this resource.
     *
     * @return the <code>ByteArrayOutputStream</code> backing this resource, which may be null.
     */
    public ByteArrayOutputStream getByteArrayOutputStream() {
        return byteArrayOutputStream;
    }

    /**
     * Sets the <code>ByteArrayOutputStream</code> backing this resource.
     *
     * @return the <code>ByteArrayOutputStream</code> backing this resource, which may be null.
     */
    public void setByteArrayOutputStream(ByteArrayOutputStream byteArrayOutputStream) {
        this.byteArrayOutputStream = byteArrayOutputStream;
    }

    /**
     * Sets the array backing this resource.
     *
     * @param byteArray the array to back this resource, which may be null.
     */
    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
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
     * @exception UnsupportedOperationException always thrown as this resource is an in-memory resource.
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
        return byteArray != null || byteArrayOutputStream != null;
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

    @Override
    public NamePath getNamePath() {
        return null;
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
        Assert
            .isTrue(
                byteArray != null || byteArrayOutputStream != null,
                "There must be an array or ByteArrayOutputStream backing this resource for the resource to be readable - consider calling canRead() first");
        return new ByteArrayInputStream(byteArray != null ? byteArray : byteArrayOutputStream.toByteArray());
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
        Assert
            .isTrue(
                byteArrayOutputStream != null,
                "There must be an ByteArrayOutputStream backing this resource for the resource to be writable - consider calling canWrite() first");
        byteArrayOutputStream.reset();
        return byteArrayOutputStream;
    }

    /**
     * Provides a convenient string representation of this resource.
     */
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(getBaseName(getClass())).append("[byteArray=").append(byteArray).append(
            ", byteArrayOutputStream=").append(byteArrayOutputStream).append("]");
        return buf.toString();
    }
}
