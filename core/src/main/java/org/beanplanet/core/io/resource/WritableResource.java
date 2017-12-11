/*
 * Copyright (C) 2017 Beanplanet Ltd
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.beanplanet.core.io.resource;

import org.beanplanet.core.io.IoException;

import java.io.OutputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * A tagging and functionality interface specifically for resources that actually support write operations.
 *
 * @author Gary Watson
 */
public interface WritableResource extends Resource {
    /**
     * Creates a new output stream, suitable for writing to the resource. It is the caller's responsibility to close the
     * output stream.
     *
     * @return a newly created output stream for writing to the resource.
     * @throws IoException if an error occurs creating the stream or if the operation is not supported.
     */
    OutputStream getOutputStream() throws IoException;

    /**
     * Creates a new writer, suitable for writing to the resource. It is the caller's responsibility to close the writer.
     *
     * <p>
     * This is a convenience method which assumes the system-default character encoding is appropriate. To specify a
     * different character encoding, use one of the other <code>getWriter(...)</code> methods, specifying the appropriate
     * character set encoding.
     * </p>
     *
     * @return a newly created reader for writing the resource or an existing reader, created by a previous call to a
     *         <code>getReader(...)</code> method on an already open resource.
     * @throws IoException if an error occurs creating the stream or if the operation is not supported.
     * @see #getWriter(String)
     * @see #getWriter(Charset)
     * @see #getWriter(CharsetEncoder)
     */
    Writer getWriter() throws IoException;

    /**
     * Creates a new writer, suitable for writing to the resource. It is the caller's responsibility to close the writer.
     *
     * @param charSetName the name of the character set encoding to apply to bytes written to the resource.
     * @return a newly created reader for writing the resource or an existing reader, created by a previous call to a
     *         <code>getWriter(...)</code> method on an already open resource.
     * @throws IoException if an error occurs creating the stream or if the operation is not supported.
     * @see #getWriter(Charset)
     * @see #getWriter(CharsetEncoder)
     */
    Writer getWriter(String charSetName) throws IoException;

    /**
     * Creates a new writer, suitable for writing to the resource. It is the caller's responsibility to close the writer.
     *
     * @param charSet the character set encoding to apply to bytes written to the resource.
     * @return a newly created reader for writing the resource or an existing reader, created by a previous call to a
     *         <code>getWriter(...)</code> method on an already open resource.
     * @throws IoException if an error occurs creating the stream or if the operation is not supported.
     * @see #getWriter(String)
     * @see #getWriter(CharsetEncoder)
     */
    Writer getWriter(Charset charSet) throws IoException;

    /**
     * Creates a new writer, suitable for writing to the resource. It is the caller's responsibility to close the writer.
     *
     * <p>
     * Before calling this method the resource should be put into write mode via a call to
     * <code>{@link Resource#openForWriting()}</code>. It may also be prudent to check if the resource is writable by
     * first calling <code>{@link Resource#supportsWriting()}</code>.
     * </p>
     *
     * @param charSetEncoder the character set encoder to use in encoding bytes written to the resource.
     * @return a newly created reader for writing the resource or an existing reader, created by a previous call to a
     *         <code>getWriter(...)</code> method on an already open resource.
     * @throws IoException if an error occurs creating the stream or if the operation is not supported.
     * @see #getWriter(String)
     * @see #getWriter(Charset)
     */
    Writer getWriter(CharsetEncoder charSetEncoder) throws IoException;
}
