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
import org.beanplanet.core.lang.Assert;
import org.beanplanet.core.lang.TypeUtil;
import org.beanplanet.core.util.PropertyBasedToStringBuilder;

import java.io.*;
import java.net.URI;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.beanplanet.core.io.PathUtil.emptyPath;
import static org.beanplanet.core.util.IteratorUtil.asStream;


/**
 * An implementation of a resource, backed by an operating system file.
 *
 * @author Gary Watson
 *
 */
public class FileResource extends AbstractResource implements ReadableResource, WritableResource, RandomAccessResource, UrlCapableResource {
    /**
     *
     */
    public static final long serialVersionUID = 1L;
    /** The underlying file that backs this file resource. */
    private File file;

    /**
     * Constructs a new file resource with no initial backing file.
     */
    public FileResource() {
    }

    /**
     * Constructs a new file resource backed by the specified file.
     *
     * @param filename the name of the file backing this resource.
     */
    public FileResource(String filename) {
        this(new File(filename));
    }

    /**
     * Constructs a new file resource backed by the specified file.
     *
     * @param file the file backing this resource.
     */
    public FileResource(File file) {
        this.file = file;
    }

    /**
     * Returns the file backing this file resource.
     *
     * @return the file backing this resource, which may be null.
     */
    public final File getFile() {
        return file;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileResource)) return false;
        FileResource that = (FileResource) o;
        return Objects.equals(getFile(), that.getFile());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFile());
    }

    /**
     * Returns the full path of the file resource.
     *
     * @return the path of the file resource.
     */
    public Path<Resource> getPath()  {
        return file == null ? null : new FileResourcePath(file);
    }

    /**
     * Sets the file backing this resource.
     *
     * @param file the file to back this file resource, which may be null.
     */
    public final void setFile(File file) {
        this.file = file;
    }

    /**
     * Whether this file resource is absolute. A file resource is absolute if it's backing file is absolute.
     *
     * @return true if the file resource is absolute, false otherwise.
     */
    public boolean isAbsolute() {
        return file.isAbsolute();
    }

    /**
     * Determines whether this resource exists in some form. Use the <code>canRead()</code> and <code>canWrite()</code>
     * methods to determine if the resource can actually be accessed.
     *
     * @return true, if and only if this resource exists in some physical form, false otherwise.
     */
    public boolean exists() {
        return file != null && file.exists();
    }

    /**
     * Determines whether this resource can be read, either as byte or character-orientated stream.
     *
     * @return true if this resource supports reading, false otherwise.
     */
    @Override
    public boolean canRead() {
        return file.exists() && file.canRead();
    }

    /**
     * Determines whether this resource can be written to, either as byte or character-orientated stream.
     *
     * @return true if this resource supports writing, false otherwise.
     */
    @Override
    public boolean canWrite() {
        return file.exists() && file.canWrite();
    }


    /**
     * Returns a Uniform Resource Identifier (URI) for the file resource.
     *
     * @return a URI of the file backing this resource, or null if the file is null.
     */
    @Override
    public URI getUri()  {
        return file == null ? null : file.toURI();
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
        Assert.notNull(file, "The file may not be null");
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException fnfEx) {
            throw new IoException(fnfEx);
        }
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
        Assert.notNull(file, "The file may not be null");
        try {
            return new FileOutputStream(file);
        } catch (FileNotFoundException fnfEx) {
            throw new IoException(fnfEx);
        }
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
     * @throws IoException if an I/O error occurs creating the random accessor
     */
    @Override
    public RandomAccessor getRandomReadAccessor() throws IoException {
        try {
            return new FileResourceRandomAccessor(getFile(), "r");
        } catch (FileNotFoundException fnfEx) {
            throw new IoException("Unable to create read-only file resource accessor: ", fnfEx);
        }
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
     * @throws IoException if an I/O error occurs creating the random accessor
     */
    @Override
    public RandomAccessor getRandomReadWriteAccessor() throws IoException {
        try {
            return new FileResourceRandomAccessor(getFile(), "rwd");
        } catch (FileNotFoundException fnfEx) {
            throw new IoException("Unable to create read/write file resource accessor: ", fnfEx);
        }
    }

    @Override
    public FileResource getParentResource() {
        return new FileResource(file.getParentFile());
    }

    /**
     * Provides a convenient string representation of this resource.
     */
    @Override
    public String toString() {
        return new PropertyBasedToStringBuilder(this).build();
    }

    /**
     * Returns the canonical form of this URI-based resource, which is the canonical path to the file backing the
     * resource.
     *
     * @return the absolute path of the file backing this resource.
     */
    public String getCanonicalForm() {
        try {
            return getFile() != null ? getFile().getCanonicalPath() : TypeUtil.getBaseName(getClass())+"[not file set]";
        } catch (IOException e) {
            throw new IoException("Unable to determine canonical form of file resource ["+file.getName()+"]");
        }
    }

    private class FileResourcePath implements Path<Resource> {
        private FileResourcePath(File file) {
            FileResource.this.file = file;
        }

        @Override
        public URI toUri() {
            return FileResource.super.getUri();
        }

        @Override
        public boolean isAbsolute() {
            return file != null && file.isAbsolute();
        }

        @Override
        public  Path<Resource> toAbsolutePath() {
            return file == null ? null : new FileResourcePath(file.getAbsoluteFile());
        }

        @Override
        public Path<Resource> getRoot() {
            return file != null && file.isAbsolute() ? new FileResourcePath(file.toPath().getRoot().toFile()) : null;
        }

        @Override
        public List<Path<Resource>> getPathElements() {
            if ( file == null ) return Collections.emptyList();

            return asStream(file.toPath().iterator())
                    .map(java.nio.file.Path::toFile)
                    .map(FileResource::new)
                    .map(FileResource::getPath)
                    .collect(Collectors.toList());
        }

        @Override
        public Resource getRootElement() {
            return file != null && file.isAbsolute() ? new FileResource(file.toPath().getRoot().toFile()) : null;
        }

        @Override
        public Resource getElement() {
            return file != null ? new FileResource(file) : null;
        }

        @Override
        public String getNameSeparator() {
            return File.separator;
        }

        @Override
        public String getName() {
            return file != null ? file.getName() : null;
        }

        @Override
        public List<String> getNameElements() {
            if ( file == null ) return Collections.emptyList();

            return asStream(file.toPath().iterator())
                    .map(java.nio.file.Path::toFile)
                    .map(File::getName)
                    .collect(Collectors.toList());
        }








        @Override
        public Path<Resource> normalise() {
            try {
                return file == null ? null : new FileResource(file.getCanonicalPath()).getPath();
            } catch (IOException e) {
                throw new IoException(e);
            }
        }

        @Override
        public Path<Resource> getPathElement(int index) {
            return null;
        }

        @Override
        public Path<Resource> join(Path<Resource> other) {
            if (other.getRoot() == null) {
                return new FileResourcePath(new File(file, other.getNameElements().stream().collect(Collectors.joining(getNameSeparator()))));
            }

            throw new UnsupportedOperationException("Path join to absolute file resource is not supported at this time");
        }

        @Override
        public Path<Resource> relativeTo(Path<Resource> other) {
            return null;
        }

        /**
         * Returns an iterator over elements of type {@code T}.
         *
         * @return an Iterator.
         */
        @Override
        public Iterator<Path<Resource>> iterator() {
            return getPathElements().iterator();
        }

        public String toCanonicalPath() {
            try {
                return file == null ? emptyPath().toCanonicalPath() : file.getCanonicalPath();
            } catch (IOException e) {
                throw new IoException(e);
            }
        }

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
        Path<Resource> thisPath = getPath();
        return thisPath == null ? null : new FileResource(thisPath.resolve(path).toCanonicalPath());
    }
}
