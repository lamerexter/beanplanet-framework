
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
