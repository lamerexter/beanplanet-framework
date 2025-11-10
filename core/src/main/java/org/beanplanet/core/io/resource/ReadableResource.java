
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
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * A tagging and functionality interface specifically for resources that actually support read operations.
 *
 * @author Gary Watson
 */
public interface ReadableResource extends Resource {
    /**
     * Creates a new input stream, suitable for reading the resource. It is the caller's responsibility to close the
     * input stream.
     *
     * @return a newly created input stream for reading the resource.
     * @throws IoException if an error occurs creating the stream or if the operation is not supported.
     */
    InputStream getInputStream() throws IoException;

    /**
     * Creates a new reader, suitable for reading from the resource. It is the caller's responsibility to close the
     * reader.
     *
     * <p>
     * This is a convenience method which assumes the system-default character encoding is appropriate. To specify a
     * different character encoding, use one of the other <code>getReader(...)</code> methods, specifying the appropriate
     * character set encoding.
     * </p>
     *
     * @return a newly created reader for reading the resource.
     * @throws IoException if an error occurs creating the reader or if the operation is not supported.
     * @see #getReader(String)
     * @see #getReader(Charset)
     * @see #getReader(CharsetDecoder)
     */
    Reader getReader() throws IoException;

    /**
     * Creates a new reader, suitable for reading from the resource. It is the caller's responsibility to close the
     * reader.
     *
     * @param charSetName the name of the character set encoding to apply to bytes read from the resource.
     * @return a newly created reader for reading the resource or an existing reader, created by a previous call to a
     *         <code>getReader(...)</code> method on an already open resource.
     * @throws IoException if an error occurs creating the reader or if the operation is not supported.
     * @see #getReader(Charset)
     * @see #getReader(CharsetDecoder)
     */
    Reader getReader(String charSetName) throws IoException;

    /**
     * Creates a new reader, suitable for reading from the resource. It is the caller's responsibility to close the
     * reader.
     *
     * @param charSet the character set encoding to apply to bytes read from the resource.
     * @return a newly created reader for reading the resource or an existing reader, created by a previous call to a
     *         <code>getReader(...)</code> method on an already open resource.
     * @throws IoException if an error occurs creating the reader or if the operation is not supported.
     * @see #getReader(String)
     * @see #getReader(CharsetDecoder)
     */
    Reader getReader(Charset charSet) throws IoException;

    /**
     * Creates a new reader, suitable for reading from the resource. It is the caller's responsibility to close the
     * reader.
     *
     * @param charSetDecoder the character set decoder to use in decoding bytes read from the resource.
     * @return a newly created reader for reading the resource or an existing reader, created by a previous call to a
     *         <code>getReader(...)</code> method on an already open resource.
     * @throws IoException if an error occurs creating the reader or if the operation is not supported.
     * @see #getReader(String) as an alternative to specify an appropriate character set encoding
     * @see #getReader(Charset) as an alternative to specify an appropriate character set encoding
     */
    Reader getReader(CharsetDecoder charSetDecoder) throws IoException;
}
