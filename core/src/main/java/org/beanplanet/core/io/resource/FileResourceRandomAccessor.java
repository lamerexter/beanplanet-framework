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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * Implementation of random access operations over a file resource.
 *
 * @author Gary Watson
 */
public class FileResourceRandomAccessor implements RandomAccessor {

    /** The random access file backing this random accessor. */
    private RandomAccessFile raf;

    /**
     * Constructs a new random resource accessor for the given file with the specified access mode.
     *
     * @param file the file to be randomly accessed.
     * @param mode the mode of random access required to the file: read and/or write; refer to
     *        <code>{@link RandomAccessFile#RandomAccessFile(File, String)}</code> for more information of the mode
     *        parameter.
     */
    public FileResourceRandomAccessor(File file, String mode) throws FileNotFoundException {
        raf = new RandomAccessFile(file, mode);
    }

    /**
     * Constructs a new random resource accessor for the given file resource with the specified access mode.
     *
     * @param resource the file resource to be randomly accessed.
     * @param mode the mode of random access required to the file: read and/or write; refer to
     *        <code>{@link RandomAccessFile#RandomAccessFile(File, String)}</code> for more information of the mode
     *        parameter.
     */
    public FileResourceRandomAccessor(FileResource resource, String mode) throws FileNotFoundException {
        this(resource.getFile(), mode);
    }

    /**
     * Closes this random accessor, freeing any related system resources.
     *
     * @throws IoException if an error occurs closing the accessor
     */
    public void close() {
        try {
            getRandomAccessFile().close();
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    /**
     * Sets the file-pointer offset, measured from the beginning of this resource, at which the next read or write
     * occurs. The offset may be set beyond the end of the resource content. Setting the offset beyond the end of the
     * resource may or may not change the resource length. The resource length will certainly change by writing after the
     * offset has been set beyond the end of the resource.
     *
     * @param pos the offset position, measured in bytes from the beginning of the resource, at which to set the file
     *        pointer.
     * @throws IoException if pos is less than 0 or if an I/O error occurs.
     */
    public void seek(long pos) {
        try {
            getRandomAccessFile().seek(pos);
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    /**
     * Reads up to <code>b.length</code> bytes of data from this file into an array of bytes. This method blocks until at
     * least one byte of input is available.
     *
     * <p>
     * Although <code>RandomAccessor</code> is not a subclass of InputStream, this method behaves in exactly the same way
     * as the <code>{@link InputStream#read(byte[])}</code> method of <code>InputStream</code>.
     * <p>
     *
     * @param b the buffer into which the data is read
     * @return the total number of bytes read into the buffer, or <code>-1</code> is there is no more data because the
     *         end of the stream has been reached.
     */
    public int read(byte[] b) {
        try {
            return getRandomAccessFile().read(b);
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    /**
     * Reads up to <code>len</code> bytes of data from this file into an array of bytes. This method blocks until at
     * least one byte of input is available.
     *
     * <p>
     * Although <code>RandomAccessor</code> is not a subclass of InputStream, this method behaves in exactly the same way
     * as the <code>{@link InputStream#read(byte[], int, int)}</code> method of <code>InputStream</code>.
     * <p>
     *
     * @param b the buffer into which the data is read.
     * @param off the start offset of the data.
     * @param len the maximum number of bytes read.
     * @return the total number of bytes read into the buffer, or <code>-1</code> if there is no more data because the
     *         end of the file has been reached.
     */
    public int read(byte[] b, int off, int len) {
        try {
            return getRandomAccessFile().read(b, off, len);
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    private RandomAccessFile getRandomAccessFile() {
        Assert.notNull(raf, "The random access file backing this resource acessor may not be null");
        return raf;
    }

    public boolean readBoolean() {
        try {
            return getRandomAccessFile().readBoolean();
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    public byte readByte() throws IoException {
        try {
            return getRandomAccessFile().readByte();
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    public char readChar() throws IoException {
        try {
            return getRandomAccessFile().readChar();
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    public double readDouble() throws IoException {
        try {
            return getRandomAccessFile().readDouble();
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    public float readFloat() throws IoException {
        try {
            return getRandomAccessFile().readFloat();
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    public void readFully(byte[] b) throws IoException {
        try {
            getRandomAccessFile().readFully(b);
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    public void readFully(byte[] b, int off, int len) throws IoException {
        try {
            getRandomAccessFile().readFully(b, off, len);
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    public int readInt() throws IoException {
        try {
            return getRandomAccessFile().readInt();
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    public String readLine() throws IoException {
        try {
            return getRandomAccessFile().readLine();
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    public long readLong() throws IoException {
        try {
            return getRandomAccessFile().readLong();
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    public short readShort() throws IoException {
        try {
            return getRandomAccessFile().readShort();
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    public String readUTF() throws IoException {
        try {
            return getRandomAccessFile().readUTF();
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    public int readUnsignedByte() throws IoException {
        try {
            return getRandomAccessFile().readUnsignedByte();
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    public int readUnsignedShort() throws IoException {
        try {
            return getRandomAccessFile().readUnsignedShort();
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    public int skipBytes(int n) throws IoException {
        try {
            return getRandomAccessFile().skipBytes(n);
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    public void write(int b) throws IoException {
        try {
            getRandomAccessFile().write(b);
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    public void write(byte[] b) throws IoException {
        try {
            getRandomAccessFile().write(b);
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    public void write(byte[] b, int off, int len) throws IoException {
        try {
            getRandomAccessFile().write(b, off, len);
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    public void writeBoolean(boolean b) throws IoException {
        try {
            getRandomAccessFile().writeBoolean(b);
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    public void writeByte(int b) throws IoException {
        try {
            getRandomAccessFile().write(b);
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    public void writeBytes(String s) throws IoException {
        try {
            getRandomAccessFile().writeBytes(s);
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    public void writeChar(int ch) throws IoException {
        try {
            getRandomAccessFile().writeChar(ch);
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    public void writeChars(String s) throws IoException {
        try {
            getRandomAccessFile().writeChars(s);
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    public void writeDouble(double d) throws IoException {
        try {
            getRandomAccessFile().writeDouble(d);
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    public void writeFloat(float f) throws IoException {
        try {
            getRandomAccessFile().writeFloat(f);
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    public void writeInt(int i) throws IoException {
        try {
            getRandomAccessFile().writeInt(i);
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    public void writeLong(long l) throws IoException {
        try {
            getRandomAccessFile().writeLong(l);
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    public void writeShort(int s) throws IoException {
        try {
            getRandomAccessFile().writeShort(s);
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    public void writeUTF(String str) throws IoException {
        try {
            getRandomAccessFile().writeUTF(str);
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }
}
