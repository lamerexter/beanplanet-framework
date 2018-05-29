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
import org.beanplanet.core.io.Path;
import org.beanplanet.core.util.PropertyBasedToStringBuilder;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;


/**
 * A base class for resource implementations, providing much of the basic common functionality between resource types.
 *
 * <p>
 * While this implementation does not implement the <code>{@link ReadableResource}</code> or
 * <code>{@link WritableResource}</code> interfaces it does implement suitable versions for most the methods in those
 * interfaces, leaving little that subclasses have to implement to become effective resources.
 * </p>
 *
 * <p>
 * In this implementation the resource is unreadable and unwritable.
 * </p>
 *
 * @author Gary Watson
 *
 */
public abstract class AbstractResource implements Resource {
    /**
     *
     */
    public static final long serialVersionUID = 1L;

    /**
     * Determines whether this resource can be read, either as byte or character-orientated stream.
     *
     * <p>
     * This basic implementation assumes the resource can be read <em>if</em> it is an instance of
     * <code>{@link ReadableResource}</code>.
     * </p>
     *
     * @return true is this resource supports reading, false otherwise.
     */
    public boolean canRead() {
        return this instanceof ReadableResource;
    }

    /**
     * Determines whether this resource can be written to, either as byte or character-orientated stream.
     *
     * <p>
     * This basic implementation assumes the resource can be written to <em>if</em> it is an instance of
     * <code>{@link WritableResource}</code>.
     * </p>
     *
     * @return true is this resource supports writing, false otherwise.
     */
    public boolean canWrite() {
        return this instanceof WritableResource;
    }

    /**
     * Whether this resource supports random access, in terms of reading and writing randomly within the resource
     * content.
     *
     * <p>
     * This basic implementation assumes the resource supports random access <em>if</em> it is an instance of
     * <code>{@link RandomAccessResource}</code>.
     * </p>
     *
     * @return true, if this type of resource supports random access, false otherwise.
     */
    public boolean supportsRandomAccess() {
        return this instanceof RandomAccessResource;
    }

    /**
     * Returns the full path of the resource, including any filename, if the resource type supports path references.
     *
     * @return always null in this implementation.
     */
    public Path getPath() throws UnsupportedOperationException {
        return null;
    }

    /**
     * Returns a Uniform Resource Identifier (URI) for the resource, if the resource type supports URI
     * references.
     *
     * @return always null in this implementation.
     */
    public URI getUri() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * Attempts to return a Uniform Resource Locator (URL) for the resource, if the resource type supports URL
     * references.
     *
     * @return always null in this implementation.
     */
    public URL getUrl() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    /**
     * This abstract resource knows nothing about how to create a stream from the resource so simply throws an
     * <code>{@link UnsupportedOperationException}<code>. Implementations must provide a useful, specific,
     * implementation of this method suited to the type of backing resource.
     *
     * @return a newly created input stream for reading the resource.
     * @throws IoException if an error occurs creating the stream or if the operation is not supported.
     */
    public InputStream getInputStream() {
        throw new UnsupportedOperationException(
            "Unable to create an input stream to the resource - please provide a subclass to implement this functionality");
    }

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
    public Reader getReader() throws IoException {
        return new InputStreamReader(getInputStream());
    }

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
    public Reader getReader(String charSetName) throws IoException {
        try {
            return new InputStreamReader(getInputStream(), charSetName);
        } catch (Exception ex) {
            throw new IoException(ex);
        }
    }

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
    public Reader getReader(Charset charSet) throws IoException {
        return new InputStreamReader(getInputStream(), charSet);
    }

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
    public Reader getReader(CharsetDecoder charSetDecoder) throws IoException {
        return new InputStreamReader(getInputStream(), charSetDecoder);
    }

    /**
     * This abstract resource knows nothing about how to create a stream from the resource so simply throws an
     * <code>{@link UnsupportedOperationException}<code>. Implementations must provide a useful, specific,
     * implementation of this method suited to the type of backing resource.
     *
     * @return a newly created output stream for writing to the resource.
     * @throws IoException if an error occurs creating the stream or if the operation is not supported.
     */
    public OutputStream getOutputStream() throws IoException {
        throw new UnsupportedOperationException(
            "Unable to create an output stream to the resource - please provide a subclass to implement this functionality");
    }

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
    public Writer getWriter() throws IoException {
        try {
            return new OutputStreamWriter(getOutputStream());
        } catch (Exception ex) {
            throw new IoException(ex);
        }
    }

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
    public Writer getWriter(String charsetName) throws IoException {
        try {
            return new OutputStreamWriter(getOutputStream(), charsetName);
        } catch (Exception fnfEx) {
            throw new IoException(fnfEx);
        }
    }

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
    public Writer getWriter(Charset charSet) throws IoException {
        try {
            return new OutputStreamWriter(getOutputStream(), charSet);
        } catch (Exception fnfEx) {
            throw new IoException(fnfEx);
        }
    }

    /**
     * Creates a new writer, suitable for writing to the resource. It is the caller's responsibility to close the writer.
     *
     * @param charSetEncoder the character set encoder to use in encoding bytes written to the resource.
     * @return a newly created reader for writing the resource or an existing reader, created by a previous call to a
     *         <code>getWriter(...)</code> method on an already open resource.
     * @throws IoException if an error occurs creating the stream or if the operation is not supported.
     * @see #getWriter(String)
     * @see #getWriter(Charset)
     */
    public Writer getWriter(CharsetEncoder charSetEncoder) throws IoException {
        try {
            return new OutputStreamWriter(getOutputStream(), charSetEncoder);
        } catch (Exception fnfEx) {
            throw new IoException(fnfEx);
        }
    }

    /**
     * This abstract resource knows nothing about how to create a random accessor for the resource so simply throws an
     * <code>{@link UnsupportedOperationException}<code>. Implementations must provide a useful, specific,
     * implementation of this method suited to the type of backing resource.
     *
     * @return always throws <code>{@link UnsupportedOperationException}<code>.
     * @throws UnsupportedOperationException if this resource does not support random access.
     * @throws IoException if an I/O error occurs creating the random accessor
     */
    public RandomAccessor getRandomReadAccessor() throws IoException {
        throw new UnsupportedOperationException(
            "Unable to create a random read accessor for this resource - please provide a subclass to implement this functionality");
    }

    /**
     * This abstract resource knows nothing about how to create a random accessor for the resource so simply throws an
     * <code>{@link UnsupportedOperationException}<code>. Implementations must provide a useful, specific,
     * implementation of this method suited to the type of backing resource.
     *
     * @return always throws <code>{@link UnsupportedOperationException}<code>.
     * @throws UnsupportedOperationException if this resource does not support random access.
     * @throws IoException if an I/O error occurs creating the random accessor
     */
    public RandomAccessor getRandomReadWriteAccessor() throws IoException {
        throw new UnsupportedOperationException(
            "Unable to create a random read/write accessor for this resource - please provide a subclass to implement this functionality");
    }

    @Override
    public Resource getParentResource() {
        throw new UnsupportedOperationException();
    }

    /**
     * Provides a convenient string representation of this resource.
     */
    @Override
    public String toString() {
        return new PropertyBasedToStringBuilder(this).build();
    }
}
