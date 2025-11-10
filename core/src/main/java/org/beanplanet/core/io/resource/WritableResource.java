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
     * @param charsetName the name of the character set encoding to apply to bytes written to the resource.
     * @return a newly created reader for writing the resource or an existing reader, created by a previous call to a
     *         <code>getWriter(...)</code> method on an already open resource.
     * @throws IoException if an error occurs creating the stream or if the operation is not supported.
     * @see #getWriter(Charset)
     * @see #getWriter(CharsetEncoder)
     */
    Writer getWriter(String charsetName) throws IoException;

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
