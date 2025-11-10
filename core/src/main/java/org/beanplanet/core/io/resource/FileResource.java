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
import org.beanplanet.core.io.IoUtil;
import org.beanplanet.core.lang.Assert;
import org.beanplanet.core.lang.TypeUtil;
import org.beanplanet.core.models.path.NamePath;
import org.beanplanet.core.models.path.Path;
import org.beanplanet.core.util.PropertyBasedToStringBuilder;
import org.beanplanet.core.util.StringUtil;

import java.io.*;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
     * Gets the length of the file backing this resource.
     *
     * @return the content length of the file backing this resource, otherwise zero or
     * -1 if the file does not exist or is a directory.
     */
    @Override
    public long getContentLength() {
        return file == null || !file.exists() || file.isDirectory() ? -1 : file.length();
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
        public org.beanplanet.core.models.path.Path<Resource> relativeTo(org.beanplanet.core.models.path.Path<Resource> other) throws IllegalArgumentException {
            return null;
        }

        /**
         * <p>
         * Resolves the given path against this path.
         * </p>
         *
         * <p>
         * If other is null of an empty path then this method trivially returns this path.
         * Otherwise this method considers this path to be a directory and resolves the given path against this path.
         * In the simplest case, the given path does not have a root component, in which case this method joins the
         * given path to this path and returns a resulting path that ends with the given path. Where the given path has a
         * root component then resolution is highly implementation dependent and therefore unspecified.
         * </p>
         *
         * <p>
         * Resolve the given path against this resource.
         *</p>
         *
         * <p>
         * If the other path is an {@link Path#isAbsolute() absolute} path then this method simply returns the other. If
         * the other path is an <i>empty path</i> then this method simply returns this path. Otherwise this method considers
         * this path to be a directory and resolves the given path against this path. In the simplest case, the given path
         * does not have a {@link Path#getRoot root} component, in which case this method <em>joins</em> the given path to
         * this path  and returns a resulting path that {@link String#endsWith(String)} the given path. Where the given path has
         * a root component then resolution is highly implementation dependent andtherefore unspecified.
         * </p>
         *
         * @param other the path to be resolved against this resource, which may be null.
         * @return a path of the given resource resolved against this path.
         * @throws IllegalArgumentException if the given path cannot be resolved against this path.
         */
        @Override
        public Path<Resource> resolve(Path<Resource> other) {
            // Empty path to resolve against -> return this path
            if (other == null || other.isEmpty()) return this;

            return join(other);
        }

        /**
         * <p>
         * Joins the given path to this path.
         * </p>
         *
         * <p>
         * This method considers this path to be a directory and joins the given path directly onto this path.
         * In the simplest case, the given path does not have a root component, in which case this method joins the
         * given path to this path and returns a resulting path that ends with the given path. Where the given path has a
         * root component then resolution is highly implementation dependent and therefore unspecified.
         * </p>
         *
         * @param other the path to be joined to this path.
         * @return a path, consisting of this path, ending with the given path.
         * @throws UnsupportedOperationException if this path cannot be joined with the given path.
         */
        public Path<Resource> join(Path<Resource> other) {
            // Empty path to resolve against -> return this path
            if (other == null || other.isEmpty()) return this;

            // The other path is rooted -> platform dependent
            if ( other.hasRoot() ) {
                if ( other instanceof FileResourcePath ) {
                    FileResourcePath otherFileResoutcePath = (FileResourcePath)other;
                    File otherFile = ((FileResource)otherFileResoutcePath.getElement()).getFile();
                    File joinecFile = getFile().toPath().resolve(otherFile.toPath()).toFile();
                    return new FileResource(joinecFile).getPath();
                }
                throw new UnsupportedOperationException();
            }

            // 2) The other path is NOT rooted -> join it to this path, retuning a new path
            return new FileResource(new File(getFile(), other.getElement().getNamePath().join(File.separator))).getPath();
        }

        @Override
        public List<Resource> getElements() {
            if ( file == null ) return Collections.emptyList();

            return asStream(file.toPath().iterator())
                    .map(java.nio.file.Path::toFile)
                    .map(FileResource::new)
                    .collect(Collectors.toList());
        }

        @Override
        public Path<Resource> parentPath() {
            return null;
        }

        @Override
        public Path<Resource> normalise() {
            try {
                return file == null ? null : new FileResource(file.getCanonicalPath()).getPath();
            } catch (IOException e) {
                throw new IoException(e);
            }
        }

//        @Override
//        public Path<Resource> relativeTo(Path<Resource> other) {
//            if (other.getRoot() == null) {
//                return new FileResourcePath(new File(file, other.getNameElements().stream().collect(Collectors.joining(getNameSeparator()))));
//            }
//
//            throw new UnsupportedOperationException("Path join to absolute file resource is not supported at this time");
//        }
//
//        @Override
//        public Path<Resource> relativeTo(Path<Resource> other) {
//            return null;
//        }
//
//        public String toCanonicalPath() {
//            try {
//                return file == null ? emptyPath().toCanonicalPath() : file.getCanonicalPath();
//            } catch (IOException e) {
//                throw new IoException(e);
//            }
//        }
    }

    /**
     * Resolve the given path relative to this resource.
     *
     * <p>
     * If the {@code path} is an {@link Path#isAbsolute() absolute}
     * path then this method simply returns the resource with the given {@code path}. If {@code path}
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
        if (path == null || path.isEmpty()) return this;

        if (path.isAbsolute()) {
            return path.getElement();
        }

        if ( path.hasRoot() ) {
            // Highly platform dependent how to resolve to another rooted path.
            throw new UnsupportedOperationException();
        }

        // Assume this path represents a directory and join
        return new FileResource(file.toPath().resolve(path.getElement().getNamePath().join(File.separator)).toFile());
    }

    /**
     * Returns the name-path to this resource. This implementation assumes this resource is not name-based and
     * always returns the empty path.
     *
     * @return Always the empty resource in this default abstract implementation.
     */
    public NamePath getNamePath() {
        return new NamePath() {
            @Override
            public List<String> getElements() {
                return StringUtil.asDsvList(file.getPath(), File.separator);
            }

            @Override
            public Path<String> parentPath() {
                FileResource parentResource = getParentResource();
                return parentResource == null ? null : parentResource.getNamePath();
            }

            @Override
            public boolean isAbsolute() {
                return file.isAbsolute();
            }

            @Override
            public Path<String> toAbsolutePath() throws IllegalStateException {
                return new FileResource(file.getAbsoluteFile()).getNamePath();
            }

            @Override
            public Path<String> normalise() {
                return new FileResource(file.toPath().normalize().toFile()).getNamePath();
            }

            @Override
            public Object getRoot() {
                java.nio.file.Path rootFilePath = file.toPath().getRoot();
                return rootFilePath == null ? null : new FileResource(rootFilePath.toFile());
            }

            @Override
            public Path<String> relativeTo(Path<String> other) throws IllegalArgumentException {
                return null;
            }

            @Override
            public Path<String> resolve(Path<String> other) {
                return null;
            }

            @Override
            public Path<String> join(Path<String> other) {
                return null;
            }

            @Override
            public URI toUri() {
                return file.toURI();
            }
        };
    }

}
