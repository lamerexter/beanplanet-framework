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
