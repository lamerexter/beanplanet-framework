
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
import org.beanplanet.core.io.IoUtil;
import org.beanplanet.core.lang.TypeUtil;
import org.beanplanet.core.models.path.NamePath;
import org.beanplanet.core.models.path.Path;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/**
 * A model of a system resource, such as a file, URL, input stream or output stream.
 *
 * <p>
 * For completeness, a resource purports to support read, write and random access operations. However, resources that
 * can be read from, written to and accessed in a random access manner implement the <code>{@link ReadableResource}</code>, <code>{@link WritableResource}</code>
 * and <code>{@link RandomAccessResource}</code>
 * tagging interfaces respectively and clients should call the <code>{@link #canRead()}</code> and
 * <code>{@link #canWrite()}</code> methods to determine if an operation is permissible on the resource at that moment
 * or under current circumstances.
 * </p>
 *
 * @author Gary Watson
 * @see ReadableResource
 * @see WritableResource
 * @see #canRead()
 * @see #canWrite()
 */
public interface Resource extends Cloneable {
    /**
     * Whether this resource is absolute. A resource is absolute if it is a path-based resource and it does not need any
     * further information in order to locate or resolve it.
     *
     * @return true if the resource is absolute, false otherwise.
     */
    default boolean isAbsolute() {
        return false;
    }

    /**
     * Returns the well-known form of the resource a.k.a. the resource canonical form. For example, a URI based
     * resource will be the URI/URL itself; for a file resource this will be the absolute path to the file and for
     * a classpath resource this might be the location on the filesystem or path within a JAR.
     *
     * @return the resource canonical form, useful for logging, debugging, display to a user and for configuration of
     * a resource. Guaranteed to be non-null.
     */
    default String getCanonicalForm() {
        return toString();
    }

    /**
     * Determines whether this resource exists in some form.
     *
     * @return true, if and only if this resource exists in some physical form, false otherwise.
     */
    default boolean exists() {
        return false;
    }

    /**
     * Determines whether this resource can be read, either as byte or character-orientated stream.
     *
     * @return true is this resource supports reading, false otherwise.
     */
    default boolean canRead() {
        return false;
    }

    /**
     * Determines whether this resource can be written to, either as byte or character-orientated stream.
     *
     * @return true is this resource supports writing, false otherwise.
     */
    default boolean canWrite() {
        return false;
    }

    /**
     * Whether this resource supports random access, in terms of reading and writing randomly within the resource
     * content.
     *
     * <p>
     * For a resource that supports random access operations, the <code>{@link #getRandomReadAccessor()}</code> method may be
     * subsequently invoked to return an accessor which provides random access functionality.
     * </p>
     *
     * @return true, if this type of resource supports random access, false otherwise.
     */
    default boolean supportsRandomAccess() {
        return false;
    }

    /**
     * Returns the name-path to this resource.
     *
     * @return nsme-path of this resource, or the empty path is this resource is not name-path based.
     */
    default NamePath getNamePath() {
        return NamePath.EMPTY_NAME_PATH;
    }

    /**
     * Returns the name of this resource. Where this resource is also path-based, this will return the last name in the path.
     * For example the resource <code>a/b/d.pdf</code> would return <code>d.pdf</code>.
     *
     * @return nsme of this resource.
     */
    default String getName() {
        return getNamePath().getLastElement();
    }

    /**
     * Returns the full path of the resource, including any filename, if the resource type supports path references.
     *
     * @return the full path of the resource, or null if the path is null or if this is not a path based resource.
     * @throws UnsupportedOperationException if this resource is not a path-based resource.
     */
    default Path<Resource> getPath() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This resource ["+ TypeUtil.getBaseName(getClass())
                +"] is not a path-based resource and therefore does not support path-based operations. Did you check instanceof "
                +TypeUtil.getBaseName(PathBasedResource.class)
                +" prior to calling getPath() ?"
        );
    }

    /**
     * Returns a Uniform Resource Identifier (URI) for the resource, if the resource type supports URI
     * references.
     *
     * @return the URI of the resource, or null if the URI is null or if this is not a URI based resource.
     */
    URI getUri() throws UnsupportedOperationException;

    /**
     * Attempts to return a Uniform Resource Locator (URL) for the resource, if the resource type supports URL
     * references.
     *
     * @return the URL of the resource, or null if the URL is null or if this is not a URL based resource.
     */
    default URL getUrl() throws UnsupportedOperationException {
        try {
            return getUri().toURL();
        } catch (MalformedURLException malformedURLEx) {
            throw new UnsupportedOperationException("The resource [" + getCanonicalForm()
                    + "] is not suitable for reference as a URL: ", malformedURLEx);
        }
    }

    /**
     * Creates a new input stream, suitable for reading the resource. It is the caller's responsibility to close the
     * input stream.
     *
     * @return a newly created input stream for reading the resource (never <code>null</code>).
     * @throws UnsupportedOperationException if this resource is not readable or the operation is not supported
     * @throws IoException if an error occurs creating the stream.
     */
    default InputStream getInputStream() throws UnsupportedOperationException, IoException {
        throw new UnsupportedOperationException("This resource is not readable.");
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
     * @throws UnsupportedOperationException if this resource is not readable or the operation is not supported
     * @throws IoException if an error occurs creating the reader.
     * @see #getReader(String)
     * @see #getReader(Charset)
     * @see #getReader(CharsetDecoder)
     */
    default Reader getReader() throws UnsupportedOperationException, IoException {
        throw new UnsupportedOperationException("This resource is not readable.");
    }

    /**
     * Creates a new reader, suitable for reading from the resource. It is the caller's responsibility to close the
     * reader.
     *
     * @param charSetName the name of the character set encoding to apply to bytes read from the resource.
     * @return a newly created reader for reading the resource or an existing reader, created by a previous call to a
     *         <code>getReader(...)</code> method on an already open resource.
     * @throws UnsupportedOperationException if this resource is not readable or the operation is not supported
     * @throws IoException if an error occurs creating the reader.
     * @see #getReader(Charset)
     * @see #getReader(CharsetDecoder)
     */
    default Reader getReader(String charSetName) throws UnsupportedOperationException, IoException {
        throw new UnsupportedOperationException("This resource is not readable.");

    }

    /**
     * Creates a new reader, suitable for reading from the resource. It is the caller's responsibility to close the
     * reader.
     *
     * @param charSet the character set encoding to apply to bytes read from the resource.
     * @return a newly created reader for reading the resource or an existing reader, created by a previous call to a
     *         <code>getReader(...)</code> method on an already open resource.
     * @throws UnsupportedOperationException if this resource is not readable or the operation is not supported
     * @throws IoException if an error occurs creating the reader.
     * @see #getReader(String)
     * @see #getReader(CharsetDecoder)
     */
    default Reader getReader(Charset charSet) throws UnsupportedOperationException, IoException {
        throw new UnsupportedOperationException("This resource is not readable.");
    }

    /**
     * Creates a new reader, suitable for reading from the resource. It is the caller's responsibility to close the
     * reader.
     *
     * @param charSetDecoder the character set decoder to use in decoding bytes read from the resource.
     * @return a newly created reader for reading the resource or an existing reader, created by a previous call to a
     *         <code>getReader(...)</code> method on an already open resource.
     * @throws UnsupportedOperationException if this resource is not readable or the operation is not supported
     * @throws IoException if an error occurs creating the reader.
     * @see #getReader(String) as an alternative to specify an appropriate character set encoding
     * @see #getReader(Charset) as an alternative to specify an appropriate character set encoding
     */
    default Reader getReader(CharsetDecoder charSetDecoder) throws UnsupportedOperationException, IoException {
        throw new UnsupportedOperationException("This resource is not readable.");
    }

    /**
     * Creates a new output stream, suitable for writing to the resource. It is the caller's responsibility to close the
     * output stream.
     *
     * @return a newly created output stream for writing to the resource.
     * @throws UnsupportedOperationException if this resource is not readable or the operation is not supported
     * @throws IoException if an error occurs creating the stream.
     */
    default OutputStream getOutputStream() throws UnsupportedOperationException, IoException {
        throw new UnsupportedOperationException("This resource is not writable.");
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
     * @throws UnsupportedOperationException if this resource is not readable or the operation is not supported
     * @throws IoException if an error occurs creating the stream.
     * @see #getWriter(String)
     * @see #getWriter(Charset)
     * @see #getWriter(CharsetEncoder)
     */
    default Writer getWriter() throws UnsupportedOperationException, IoException {
        throw new UnsupportedOperationException("This resource is not writable.");
    }

    /**
     * Creates a new writer, suitable for writing to the resource. It is the caller's responsibility to close the writer.
     *
     * @param charsetName the name of the character set encoding to apply to bytes written to the resource.
     * @return a newly created reader for writing the resource or an existing reader, created by a previous call to a
     *         <code>getWriter(...)</code> method on an already open resource.
     * @throws UnsupportedOperationException if this resource is not readable or the operation is not supported
     * @throws IoException if an error occurs creating the stream.
     * @see #getWriter(Charset)
     * @see #getWriter(CharsetEncoder)
     */
    default Writer getWriter(String charsetName) throws UnsupportedOperationException, IoException {
        throw new UnsupportedOperationException("This resource is not writable.");
    }

    /**
     * Creates a new writer, suitable for writing to the resource. It is the caller's responsibility to close the writer.
     *
     * @param charSet the character set encoding to apply to bytes written to the resource.
     * @return a newly created reader for writing the resource or an existing reader, created by a previous call to a
     *         <code>getWriter(...)</code> method on an already open resource.
     * @throws UnsupportedOperationException if this resource is not readable or the operation is not supported
     * @throws IoException if an error occurs creating the stream.
     * @see #canWrite()
     * @see #getWriter(String)
     * @see #getWriter(CharsetEncoder)
     */
    default Writer getWriter(Charset charSet) throws UnsupportedOperationException, IoException {
        throw new UnsupportedOperationException("This resource is not writable.");
    }

    /**
     * Creates a new writer, suitable for writing to the resource. It is the caller's responsibility to close the writer.
     *
     * @param charSetEncoder the character set encoder to use in encoding bytes written to the resource.
     * @return a newly created reader for writing the resource or an existing reader, created by a previous call to a
     *         <code>getWriter(...)</code> method on an already open resource.
     * @throws UnsupportedOperationException if this resource is not writable or the operation is not supported
     * @throws IoException if an error occurs creating the stream.
     * @see #canWrite()
     * @see #getWriter(String)
     * @see #getWriter(Charset)
     */
    default Writer getWriter(CharsetEncoder charSetEncoder) throws UnsupportedOperationException, IoException {
        throw new UnsupportedOperationException("This resource is not writable.");
    }

    /**
     * Creates a new random accessor which provides functionality to read, at random, content of this resource. The
     * random accessor will be initialised in read-only mode and any attempts to invoke write operations will result in
     * an <code>{@link IoException}</code>.
     *
     * <p>
     * Some resources (such as files and in-memory arrays) support random access. Use this method to expose random access
     * capabilities of those resources.
     * </p>
     *
     * @return a random accessor which exposes the random read access operations. The accessor should be closed after
     *         use.
     * @throws UnsupportedOperationException if this resource is not randomly accessible or the operation is not supported
     * @throws IoException if an I/O error occurs creating the random accessor
     */
    default RandomAccessor getRandomReadAccessor() throws IoException {
        throw new UnsupportedOperationException("This resource is not randomly accessible.");
    }

    /**
     * Creates a new random accessor which provides functionality to read and/or write, at random, over or beyond the
     * content of this resource.
     *
     * <p>
     * Some resources (such as files and in-memory arrays) support random access. Use this method to expose random access
     * capabilities of those resources.
     * </p>
     *
     * @return a random accessor which exposes the random access operations. The accessor should be closed after use.
     * @throws UnsupportedOperationException if this resource is not randomly accessible or the operation is not supported
     * @throws IoException if an I/O error occurs creating the random accessor
     */
    default RandomAccessor getRandomReadWriteAccessor() throws IoException {
        throw new UnsupportedOperationException("This resource is not randomly accessible.");
    }

    /**
     * Convenience method to read the resource as a string, using the platform default character set encoding. Obviously
     * this assumes tbe contents of the resource to be a string of manageable size, readable in the platform's default character set
     * encoding - so care must be taken when using this method.
     *
     * @return the string of the contents of the resource.
     */
    default String readFullyAsString() {
        return IoUtil.transferAndClose(this.getReader(), new StringWriter()).toString();
    }

    /**
     * Convenience method to read the resource as a byte array. Obviously this assumes tbe resource to be of manageable
     * size - so care must be taken when using this method.
     *
     * @return a byte array of the contents of the resource.
     */
    default byte[] readFullyAsBytes() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IoUtil.transferAndClose(this.getInputStream(), baos);
        return baos.toByteArray();
    }

    /**
     * Resolve the given path relative to this resource.
     *
     * <p>
     * If the {@code path} is an {@link Path#isAbsolute() absolute}
     * path then this method simply returns a new resource with the given {@code path}. If {@code path}
     * is an <i>empty path</i> then this method simply returns this resource.
     * Otherwise this method considers this resource to be a directory and resolves
     * the given path against this resource's path. In the simplest case, the given path
     * does not have a {@link Path#getRoot root} component, in which case this method
     * <em>joins</em> the given path to the path of this resource and returns a resulting resource with path
     * that {@link String#endsWith(String)} the given path. Where the given path has
     * a root component then resolution is highly implementation dependent and
     * therefore unspecified.
     * </p>
     *
     * @param path the path to be resolved against this resource.
     * @return a new resource whose path is resolved relative to the path of this resource.
     * @throws UnsupportedOperationException if this resource is not a path-based resource or if the operation is not
     * supported on this type of resource.
     */
    default Resource resolve(Path<Resource> path) {
        throw new UnsupportedOperationException("Path resolution is not supported by this resource");
    }

    /**
     * Finds and returns the 'parent' recource in the space of this child. This might be a parent directory in a filesystem or some
     * other parent in a hierarchical resource space.
     *
     * @return the parent resource or null if there is no such parent.
     */
    default Resource getParentResource() {
        return null;
    }
}
