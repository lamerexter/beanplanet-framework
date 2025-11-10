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
import org.beanplanet.core.models.path.NamePath;
import org.beanplanet.core.models.path.Path;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.function.Supplier;

public class DelegatingResource extends AbstractResource {
    /** A supplier of the resource to delegate to. */
    private final Supplier<Resource> delegateSupplier;

    /** Whether this delegating resource should cache the supplied delegate on first resource delegation operation.*/
    private final boolean cacheDelegate;

    private Resource delegate;

    /**
     * Constructs a new delegating resource with the given delegate supplier and caching strategy. Depending on the
     * ability to cache supplied delegates, the supplier may be called to supply the delegate once (on first resource
     * operation) or multiple times (once on each resource operation).
     *
     * @param delegateSupplier a supplier of the instance to which operations will be delegated.
     * @param cacheDelegate whether this delegating resource may cache the supplied delegate (on first operation).
     */
    public DelegatingResource(final Supplier<Resource> delegateSupplier, final boolean cacheDelegate) {
        this.delegateSupplier = delegateSupplier;
        this.cacheDelegate = cacheDelegate;
    }

    /**
     * Constructs a new delegating resource with the given delegate supplier. The supplier will only be called once, on
     * first resource operation, and cached thereafter, for use in subsequent resource operations.
     *
     * @param delegateSupplier a supplier of the instance to which operations will be delegated.
     * @see #DelegatingResource(Supplier, boolean)
     */
    public DelegatingResource(final Supplier<Resource> delegateSupplier) {
        this(delegateSupplier, true);
    }

    /**
     * <p>Gets the length of content backing this resource, which will always be either a positive amount, zero to indicate
     * none or -1 to indicate the length is unknown or cannot be determined owing to the nature of the resource.</p>
     *
     * <p>The default implementation reads the input stream (via {@link #getInputStream()}) fully in order to determine
     * the content length. It is highly recommended to override the default to determine the content length more
     * efficiently, based on the type if resource. All known subclasses backing 'real' world resources provide more
     * efficient and direct resource-related mechanisms for determining the content length. For example, a {@link FileResource}
     * simple returns the size of the underlying Operating System file.</p>
     *
     * @return the content length of this resource: zero, a positive amount or -1 to indicate the content length is
     * either unknown or cannot be determined at the tome of the call.
     *
     */
    public long getContentLength() {
        return delegate().getContentLength();
    }

    private Resource delegate() {
        if ( delegate != null ) return delegate;

        Resource newDelegate = delegateSupplier.get();
        if (cacheDelegate) this.delegate = newDelegate;

        return newDelegate;
    }

    /**
     * Whether this resource is absolute. A resource is absolute if it is a path-based resource and it does not need any
     * further information in order to locate or resolve it.
     *
     * @return true if the resource is absolute, false otherwise.
     */
    public boolean isAbsolute() {
        return delegate().isAbsolute();

    }

    /**
     * Returns the well-known form of the resource a.k.a. the resource canonical form. For example, a URI based
     * resource will be the URI/URL itself; for a file resource this will be the absolute path to the file and for
     * a classpath resource this might be the location on the filesystem or path within a JAR.
     *
     * @return the resource canonical form, useful for logging, debugging, display to a user and for configuration of
     * a resource. Guaranteed to be non-null.
     */
    public String getCanonicalForm() {
        return delegate().getCanonicalForm();
    }

    /**
     * Determines whether this resource exists in some form.
     *
     * @return true, if and only if this resource exists in some physical form, false otherwise.
     */
    public boolean exists() {
        return delegate().exists();
    }

    /**
     * Determines whether this resource can be read, either as byte or character-orientated stream.
     *
     * @return true is this resource supports reading, false otherwise.
     */
    public boolean canRead() {
        return delegate().canRead();
    }

    /**
     * Determines whether this resource can be written to, either as byte or character-orientated stream.
     *
     * @return true is this resource supports writing, false otherwise.
     */
    public boolean canWrite() {
        return delegate().canWrite();
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
    public boolean supportsRandomAccess() {
        return delegate().supportsRandomAccess();
    }

    /**
     * Returns the name-path to this resource.
     *
     * @return nsme-path of this resource, or the empty path is this resource is not name-path based.
     */
    public  NamePath getNamePath() {
        return delegate().getNamePath();
    }

    /**
     * Returns the name of this resource. Where this resource is also path-based, this will return the last name in the path.
     * For example the resource <code>a/b/d.pdf</code> would return <code>d.pdf</code>.
     *
     * @return nsme of this resource.
     */
    public String getName() {
        return delegate().getName();
    }

    /**
     * Returns the full path of the resource, including any filename, if the resource type supports path references.
     *
     * @return the full path of the resource, or null if the path is null or if this is not a path based resource.
     * @throws UnsupportedOperationException if this resource is not a path-based resource.
     */
    public Path<Resource> getPath() {
        return delegate().getPath();
    }

    /**
     * Returns a Uniform Resource Identifier (URI) for the resource, if the resource type supports URI
     * references.
     *
     * @return the URI of the resource, or null if the URI is null or if this is not a URI based resource.
     */
    public URI getUri() {
        return delegate().getUri();
    }

    /**
     * Attempts to return a Uniform Resource Locator (URL) for the resource, if the resource type supports URL
     * references.
     *
     * @return the URL of the resource, or null if the URL is null or if this is not a URL based resource.
     */
    public URL getUrl() {
        return delegate().getUrl();
    }

    /**
     * Creates a new input stream, suitable for reading the resource. It is the caller's responsibility to close the
     * input stream.
     *
     * @return a newly created input stream for reading the resource (never <code>null</code>).
     * @throws UnsupportedOperationException if this resource is not readable or the operation is not supported
     * @throws IoException if an error occurs creating the stream.
     */
    public InputStream getInputStream() throws UnsupportedOperationException, IoException {
        return delegate().getInputStream();
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
    public Reader getReader() throws UnsupportedOperationException, IoException {
        return delegate().getReader();
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
    public Reader getReader(String charSetName) throws UnsupportedOperationException, IoException {
        return delegate().getReader(charSetName);
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
    public Reader getReader(Charset charSet) throws UnsupportedOperationException, IoException {
        return delegate().getReader(charSet);
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
    public Reader getReader(CharsetDecoder charSetDecoder) throws UnsupportedOperationException, IoException {
        return delegate().getReader(charSetDecoder);
    }

    /**
     * Creates a new output stream, suitable for writing to the resource. It is the caller's responsibility to close the
     * output stream.
     *
     * @return a newly created output stream for writing to the resource.
     * @throws UnsupportedOperationException if this resource is not readable or the operation is not supported
     * @throws IoException if an error occurs creating the stream.
     */
    public OutputStream getOutputStream() throws UnsupportedOperationException, IoException {
        return delegate().getOutputStream();
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
    public Writer getWriter() throws UnsupportedOperationException, IoException {
        return delegate().getWriter();
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
    public Writer getWriter(String charsetName) throws UnsupportedOperationException, IoException {
        return delegate().getWriter(charsetName);
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
    public Writer getWriter(Charset charSet) throws UnsupportedOperationException, IoException {
        return delegate().getWriter(charSet);
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
    public Writer getWriter(CharsetEncoder charSetEncoder) throws UnsupportedOperationException, IoException {
        return delegate().getWriter(charSetEncoder);
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
    public RandomAccessor getRandomReadAccessor() throws IoException {
        return delegate().getRandomReadAccessor();
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
    public RandomAccessor getRandomReadWriteAccessor() throws IoException {
        return delegate().getRandomReadWriteAccessor();
    }

    /**
     * Convenience method to read the resource as a string, using the platform default character set encoding. Obviously
     * this assumes tbe contents of the resource to be a string of manageable size, readable in the platform's default character set
     * encoding - so care must be taken when using this method.
     *
     * @param charset the character set which will be used to read the resource content, which may be null in which case the system-default character set will be used.
     * @return the string of the contents of the resource.
     */
    public String readFullyAsString(final Charset charset) {
        return delegate().readFullyAsString(charset);
    }

    /**
     * Convenience method to read the resource as a string, using the platform default character set encoding. Obviously
     * this assumes tbe contents of the resource to be a string of manageable size, readable in the platform's default character set
     * encoding - so care must be taken when using this method.
     *
     * @param charset the character set which will be used to read the resource content, which may be null in which case the system-default character set will be used.
     * @return the string of the contents of the resource.
     */
    public String readFullyAsString(final String charset) {
        return delegate().readFullyAsString(charset);
    }

    /**
     * Convenience method to read the resource as a string, using the platform default character set encoding. Obviously
     * this assumes tbe contents of the resource to be a string of manageable size, readable in the platform's default character set
     * encoding - so care must be taken when using this method.
     *
     * @return the string of the contents of the resource.
     */
    public String readFullyAsString() {
        return delegate().readFullyAsString();
    }

    /**
     * Convenience method to read the resource as a byte array. Obviously this assumes tbe resource to be of manageable
     * size - so care must be taken when using this method.
     *
     * @return a byte array of the contents of the resource.
     */
    public byte[] readFullyAsBytes() {
        return delegate().readFullyAsBytes();
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
    public Resource resolve(Path<Resource> path) {
        return delegate().resolve(path);
    }

    /**
     * Finds and returns the 'parent' recource in the space of this child. This might be a parent directory in a filesystem or some
     * other parent in a hierarchical resource space.
     *
     * @return the parent resource or null if there is no such parent.
     */
    public Resource getParentResource() {
        return delegate().getParentResource();
    }
}
