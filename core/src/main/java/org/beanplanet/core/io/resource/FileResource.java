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
import org.beanplanet.core.lang.Assert;
import org.beanplanet.core.util.PropertyBasedToStringBuilder;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * An implementation of a resource, backed by an operating system file.
 *
 * @author Gary Watson
 *
 */
public class FileResource extends AbstractUrlBasedResource implements ReadableResource, WritableResource, RandomAccessResource {
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
        super(file.toURI());
        setFile(file);
    }

    /**
     * Returns the file backing this file resource.
     *
     * @return the file backing this resource, which may be null.
     */
    public final File getFile() {
        return file;
    }

    /**
     * Sets the file backing this resource.
     *
     * @param file the file to back this file resource, which may be null.
     */
    public final void setFile(File file) {
        this.file = file;
        setUri(file.toURI());
    }

    /**
     * Determines whether this resource exists in some form. Use the <code>canRead()</code> and <code>canWrite()</code>
     * methods to determine if the resource can actually be accessed.
     *
     * @return true, if and only if this resource exists in some physical form, false otherwise.
     */
    public boolean exists() {
        Assert.notNull(file, "The file may not be null");
        return file.exists();
    }

    /**
     * Determines whether this resource can be read, either as byte or character-orientated stream.
     *
     * @return true if this resource supports reading, false otherwise.
     */
    @Override
    public boolean canRead() {
        Assert.notNull(file, "The file may not be null");
        return file.exists() && file.canRead();
    }

    /**
     * Determines whether this resource can be written to, either as byte or character-orientated stream.
     *
     * @return true if this resource supports writing, false otherwise.
     */
    @Override
    public boolean canWrite() {
        Assert.notNull(file, "The file may not be null");
        return file.exists() && file.canWrite();
    }


    /**
     * Attempts to return a Uniform Resource Locator (URL) for the resource, if the resource type supports URL
     * references.
     *
     * @return the URL of the resource.
     * @throws UnsupportedOperationException if the URL of the resource could not be determined or the type of the
     *         resource is such that URL references are not supported.
     */
    @Override
    public URL getUrl() throws UnsupportedOperationException {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException malformedURLEx) {
            throw new UnsupportedOperationException("The specified file [" + file.getName()
                + "] is not suitable as a URL: ", malformedURLEx);
        }
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
}
