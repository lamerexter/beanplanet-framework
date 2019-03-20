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

package org.beanplanet.core.io;

import org.beanplanet.core.io.resource.Resource;
import org.beanplanet.core.util.StringUtil;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * I/O Stream utility class.
 *
 * @author Gary Watson
 */
public class IoUtil {
    private static final char[] HEXDIGITS = "0123456789ABCDEF".toCharArray();

    /**
     * The length of any transfer buffer, byte or chracter orientated, used by the transfer utility methods.
     */
    public static final int DEFAULT_TRANSFER_BUF_SIZE = (32 * 1024);

    /** The character value for Carriage-Return (0x0A = 10). */
    public static final int CR = '\r';

    /** The character value for Line-Feed (0x0D = 13). */
    public static final int LF = '\n';

    /** The character values for Carriage-Return and Line-Feed (0x0A0D = 13 10). */
    public static final String CRLF = "\r\n";

    /**
     * Flushes an <code>OutputStream</code> ignoring any errors.
     *
     * @param outputStream the stream to flush, which can be null
     */
    public static void flushIgnoringErrors(OutputStream outputStream) {
        if (outputStream == null) {
            return;
        }
        try {
            outputStream.flush();
        } catch (IOException ignoreEx) {
        }
    }

    /**
     * Closes a closable ignoring any errors.
     *
     * @param closeable the closable to close, which may be null
     */
    public static void closeIgnoringErrors(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (java.io.IOException ignoreEx) {
        }
    }

    /**
     * Closes a closable, coverting any checked exception to an unchecked {@link IoException}.
     *
     * @param closeable the closable to close, which may be null
     */
    public static void close(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (java.io.IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    /**
     * Obtains the <code>String</code> representation of the specified <code>ByteBuffer</code> assuming the UTF-16
     * encoding.
     * <p>
     * On exit, the buffer position is as it was on exit.
     *
     * @param buffer the byte buffer whose string value is to be obtained.
     */
    public static CharSequence toString(ByteBuffer buffer) {
        return toString(buffer, "UTF-16");
    }

    /**
     * Obtains the <code>String</code> representation of the specified <code>ByteBuffer</code> using the specified
     * character encoding.
     * <p>
     * On exit, the buffer position is as it was on exit.
     *
     * @param buffer the byte buffer whose string value is to be obtained.
     * @param encoding the assumed encoding of the buffer
     */
    public static CharSequence toString(ByteBuffer buffer, String encoding) {
        buffer.rewind();

        try {
            Charset charset = Charset.forName(encoding);
            return charset.decode(buffer);
        } finally {
            buffer.rewind();
        }
    }

    /**
     * Shuts down the input of a <code>Socket</code> ignoring any errors.
     *
     * @param socket the socket whose input is to be shutdown, can be null
     */
    public static void shutdownInputIgnoringErrors(Socket socket) {
        if (socket == null) {
            return;
        }
        try {
            socket.shutdownInput();
        } catch (IOException ignoreEx) {
        }
    }

    /**
     * Shuts down the output of a <code>Socket</code> ignoring any errors.
     *
     * @param socket the socket whose output is to be shutdown, can be null
     */
    public static void shutdownOutputIgnoringErrors(Socket socket) {
        if (socket == null) {
            return;
        }
        try {
            socket.shutdownOutput();
        } catch (IOException ignoreEx) {
        }
    }

    /**
     * Automatically transfers data from the specified <code>URL</code> to the <code>OutputStream</code> until
     * End-Of-File (EOF) is encountered on the input stream.
     * <p>
     * Note that the input and output streams are not closed by this method.
     *
     * @param inputResource the input resource from which the data will be read
     * @param os the output stream where the data will be written.
     * @exception IoException thrown if an error occurs during the transfer.
     */
    public static void transfer(URL inputResource, OutputStream os) throws IoException {
        transfer(inputResource, os, DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Automatically transfers data from the specified <code>URL</code> to the <code>OutputStream</code> until
     * End-Of-File (EOF) is encountered on the input stream.
     * <p>
     * Note that the input and output streams are not closed by this method.
     *
     * @param inputResource the input resource from which the data will be read
     * @param os the output stream where the data will be written.
     * @param bufferSize the transfer buffer length of the buffer to use during the transfer for efficiency.
     * @exception IoException thrown if an error occurs during the transfer.
     */
    public static void transfer(URL inputResource, OutputStream os, int bufferSize) throws IoException {
        InputStream resourceIS = null;
        try {
            resourceIS = inputResource.openStream();
            transfer(resourceIS, os, bufferSize);
        } catch (IOException e) {
            throw new IoException("An error occurred during transfer: ", e);
        } finally {
            closeIgnoringErrors(resourceIS);
        }
    }

    /**
     * Automatically transfers data from the specified <code>InputStream</code> to the <code>OutputStream</code> until
     * End-Of-File (EOF) is encountered on the input stream.
     * <p>
     * Note that the input and output streams are not closed by this method.
     *
     * @param is the input stream from which the data will be read
     * @param os the output stream where the data will be written.
     * @param bufferSize the transfer buffer length of the buffer to use during the transfer for efficiency.
     * @exception IOException thrown if an error occurs during the transfer.
     */
    public static void transfer(InputStream is, OutputStream os, int bufferSize) throws IoException {
        byte transferBuf[] = new byte[bufferSize];
        int readCount;
        try {
            while ((readCount = is.read(transferBuf)) != -1) {
                os.write(transferBuf, 0, readCount);
            }
        } catch (IOException e) {
            throw new IoException("An error occurred during transfer: ", e);
        }
    }

    /**
     * Automatically transfers data from the specified <code>URL</code> to the <code>OutputStream</code> until
     * End-Of-File (EOF) is encountered on the input stream and closes both streams regardless of any successful or
     * erroneous outcome.
     *
     * @param sourceURL the URL from which the data will be read
     * @param os the output stream where the data will be written.
     * @exception IOException thrown if an error occurs during the transfer.
     * @see #transfer(java.io.InputStream, java.io.OutputStream, int)
     */
    public static void transferAndClose(URL sourceURL, OutputStream os) throws IoException {
        transferAndClose(sourceURL, os, DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Automatically transfers data from the specified <code>URL</code> to the <code>OutputStream</code> until
     * End-Of-File (EOF) is encountered on the input stream and closes both streams regardless of any successful or
     * erroneous outcome.
     *
     * @param sourceURL the URL from which the data will be read
     * @param os the output stream where the data will be written.
     * @param bufferSize the transfer buffer length of the buffer to use during the transfer for efficiency.
     * @exception IOException thrown if an error occurs during the transfer.
     * @see #transfer(java.io.InputStream, java.io.OutputStream, int)
     */
    public static void transferAndClose(URL sourceURL, OutputStream os, int bufferSize) throws IoException {
        try {
            transferAndClose(sourceURL.openStream(), os, bufferSize);
        } catch (IOException e) {
            throw new IoException("An error occurred during transfer: ", e);
        }
    }

    /**
     * Automatically transfers data from the specified <code>InputStream</code> to the <code>OutputStream</code> until
     * End-Of-File (EOF) is encountered on the input stream and closes both streams regardless of any successful or
     * erroneous outcome.
     *
     * @param is the input stream from which the data will be read
     * @param os the output stream where the data will be written.
     * @param bufferSize the transfer buffer length of the buffer to use during the transfer for efficiency.
     * @exception IOException thrown if an error occurs during the transfer.
     * @see #transfer(java.io.InputStream, java.io.OutputStream, int)
     */
    public static void transferAndClose(InputStream is, OutputStream os, int bufferSize) throws IoException {
        try {
            transfer(is, os, bufferSize);
        } finally {
            closeIgnoringErrors(is);
            closeIgnoringErrors(os);
        }
    }

    /**
     * Automatically transfers binary data from from <code>source</code> to <code>destination</code>
     * until End-Of-File (EOF) is encountered on the source resource
     * and closes both resources regardless of any successful or erroneous outcome.
     *
     * <p>
     * The source and destination resources are opened, if necessary, and are subsequetly closed by this operation.
     * </p>
     *
     * @param fromResource the source resource from which the data will be read
     * @param toResource the destination resource where the data will be written.
     * @exception IOException thrown if an error occurs during the transfer.
     * @see #transferResourceCharStreamsAndClose(Resource, Resource, int) for the underlying method
     *      method used by this method
     */
    public static void transferResourceStreamsAndClose(Resource fromResource, Resource toResource)
        throws IoException {
        transferResourceCharStreamsAndClose(fromResource, toResource, DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Automatically transfers binary data from <code>source</code> to <code>destination</code>
     * until End-Of-File (EOF) is encountered on the source resource
     * and closes both resources regardless of any successful or erroneous outcome.
     *
     * <p>
     * The source and destination resources are opened, if necessary, and are subsequetly closed by this operation.
     * </p>
     *
     * @param fromResource the source resource from which the data will be read
     * @param toResource the destination resource where the data will be written.
     * @param bufferSize the transfer buffer length of the buffer to use during the transfer for efficiency.
     * @exception IOException thrown if an error occurs during the transfer.
     * @see #transfer(java.io.InputStream, java.io.OutputStream, int) for the method called by this method to stream the
     *      data
     */
    public static void transferResourceStreamsAndClose(Resource fromResource, Resource toResource,
                                                       int bufferSize) throws IoException {

        transferAndClose(fromResource.getInputStream(), toResource.getOutputStream(), bufferSize);
    }

    /**
     * Automatically transfers binary data from from <code>source</code> to <code>destination</code>
     * until End-Of-File (EOF) is encountered on the source resource
     * and closes both resources regardless of any successful or erroneous outcome.
     *
     * <p>
     * The source and destination resources are opened, if necessary, and are subsequetly closed by this operation.
     * </p>
     *
     * @param fromResource the source resource from which the data will be read
     * @param toResource the destination resource where the data will be written.
     * @exception IOException thrown if an error occurs during the transfer.
     * @see #transferResourceStreamsAndClose(Resource, Resource)
     */
    public static <R extends Resource> R transferAndClose(Resource fromResource, R toResource)
        throws IoException {
        transferResourceStreamsAndClose(fromResource, toResource);
        return toResource;
    }

    /**
     * Automatically transfers binary data from <code>source</code> to <code>destination</code>
     * until End-Of-File (EOF) is encountered on the source resource
     * and closes both resources regardless of any successful or erroneous outcome.
     *
     * <p>
     * The source and destination resources are opened, if necessary, and are subsequetly closed by this operation.
     * </p>
     *
     * @param fromResource the source resource from which the data will be read
     * @param toResource the destination resource where the data will be written.
     * @param bufferSize the transfer buffer length of the buffer to use during the transfer for efficiency.
     * @exception IOException thrown if an error occurs during the transfer.
     * @see #transferResourceStreamsAndClose(Resource, Resource, int)
     */
    public static <R extends Resource> R transferAndClose(Resource fromResource, R toResource,
                                        int bufferSize) throws IoException {
        transferResourceStreamsAndClose(fromResource, toResource, bufferSize);
        return toResource;
    }

    /**
     * Automatically transfers character data from from <code>source</code> to <code>destination</code>
     * until End-Of-File (EOF) is encountered on the source resource
     * and closes both resources regardless of any successful or erroneous outcome.
     *
     * <p>
     * The source and destination resources are opened, if necessary, and are subsequetly closed by this operation.
     * </p>
     *
     * <p>
     * The default system character encoding is used for the character-orientated transfer between source and
     * destination. Consider using an alternative transfer method if alternative character encodings are required for the
     * source or detination streams.
     * </p>
     *
     * @param fromResource the source resource from which the data will be read
     * @param toResource the destination resource where the data will be written.
     * @exception IOException thrown if an error occurs during the transfer.
     * @see #transfer(java.io.Reader, java.io.Writer, int) for the method called by this method to stream the data
     */
    public static void transferResourceCharStreamsAndClose(Resource fromResource, Resource toResource)
        throws IoException {
        transferResourceCharStreamsAndClose(fromResource, toResource, DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Automatically transfers character data from from <code>source</code> to <code>destination</code>
     * until End-Of-File (EOF) is encountered on the source resource
     * and closes both resources regardless of any successful or erroneous outcome.
     *
     * <p>
     * The source and destination resources are opened, if necessary, and are subsequetly closed by this operation.
     * </p>
     *
     * <p>
     * The default system character encoding is used for the character-orientated transfer between source and
     * destination. Consider using an alternative transfer method if alternative character encodings are required for the
     * source or detination streams.
     * </p>
     *
     * @param fromResource the source resource from which the data will be read
     * @param toResource the destination resource where the data will be written.
     * @param bufferSize the transfer buffer length of the buffer to use during the transfer for efficiency.
     * @exception IoException thrown if an error occurs during the transfer.
     * @see #transfer(java.io.InputStream, java.io.OutputStream, int) for the method called by this method to stream the
     *      data
     */
    public static void transferResourceCharStreamsAndClose(Resource fromResource, Resource toResource,
                                                           int bufferSize) throws IoException {
        transferAndClose(fromResource.getReader(), toResource.getWriter(), bufferSize);
    }

    /**
     * Automatically transfers data from the specified <code>InputStream</code> to the <code>OutputStream</code> until
     * End-Of-File (EOF) is encountered on the input stream.
     * <p>
     * Note that the input and output streams are not closed by this method.
     *
     * @param is the input stream from which the data will be read
     * @param os the output stream where the data will be written.
     * @exception IoException thrown if an error occurs during the transfer.
     */
    public static void transfer(InputStream is, OutputStream os) throws IoException {
        transfer(is, os, DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Automatically transfers data from the specified <code>InputStream</code> to the <code>OutputStream</code> until
     * End-Of-File (EOF) is encountered on the input stream and closes both streams regardless of any successful or
     * erroneous outcome.
     * <p>
     * Note that the input and output streams are not closed by this method.
     *
     * @param is the input stream from which the data will be read
     * @param os the output stream where the data will be written.
     * @exception IoException thrown if an error occurs during the transfer.
     * @see #transfer(java.io.InputStream, java.io.OutputStream)
     * @see #transferAndClose(java.io.InputStream, java.io.OutputStream, int)
     */
    public static void transferAndClose(InputStream is, OutputStream os) throws IoException {
        try {
            transfer(is, os);
        } finally {
            closeIgnoringErrors(is);
            closeIgnoringErrors(os);
        }
    }

    /**
     * Automatically transfers data from the specified <code>ByteBuffer</code> to the <code>OutputStream</code>.
     *
     * @param buffer the buffer containing the data to be transferred
     * @param os the output stream to receive the data
     * @exception IoException thrown if an error occurs during the transfer.
     */
    public static void transfer(ByteBuffer buffer, OutputStream os) throws IoException {
        try {
            while (buffer.hasRemaining()) {
                os.write(buffer.get());
            }
        } catch (IOException e) {
            throw new IoException("An error occurred during transfer: ", e);
        }
    }

    /**
     * Automatically transfers data from the specified <code>ByteBuffer</code> to the <code>OutputStream</code> and
     * closes the output stream regardless of any successful or erroneous outcome.
     *
     * @param buffer the buffer containing the data to be transferred
     * @param os the output stream to receive the data
     * @exception IoException thrown if an error occurs during the transfer.
     * @see #transfer(java.nio.ByteBuffer, java.io.OutputStream)
     */
    public static void transferAndClose(ByteBuffer buffer, OutputStream os) throws IoException {
        try {
            while (buffer.hasRemaining()) {
                os.write(buffer.get());
            }
        } catch (IOException e) {
            throw new IoException("An error occurred during transfer: ", e);
        } finally {
            closeIgnoringErrors(os);
        }
    }

    /**
     * Automatically transfers data from the specified <code>Reader</code> to the <code>Writer</code> until End-Of-File
     * (EOF) is encountered.
     * <p>
     * Note that the Reader and Writer are not closed by this method.
     *
     * @param reader the reader from which the data will be read
     * @param writer the writer where the data will be written.
     * @param bufferSize the transfer buffer length of the buffer to use during the transfer for efficiency.
     * @exception IoException thrown if an error occurs during the transfer.
     */
    public static void transfer(Reader reader, Writer writer, int bufferSize) throws IoException {
        char transferBuf[] = new char[bufferSize];
        int readCount;
        try {
            while ((readCount = reader.read(transferBuf)) != -1) {
                writer.write(transferBuf, 0, readCount);
            }
        } catch (IOException e) {
            throw new IoException("Error occurred during transfer: ", e);
        }
    }

    /**
     * Automatically transfers data from the specified <code>Reader</code> to the <code>Writer</code> until End-Of-File
     * (EOF) is encountered and closes both the reader and writer regardless of any successful or erroneous outcome.
     *
     * @param reader the reader from which the data will be read
     * @param writer the writer where the data will be written.
     * @param bufferSize the transfer buffer length of the buffer to use during the transfer for efficiency.
     * @exception IoException thrown if an error occurs during the transfer.
     * @see #transfer(java.io.Reader, java.io.Writer, int)
     */
    public static void transferAndClose(Reader reader, Writer writer, int bufferSize) throws IoException {
        try {
            transfer(reader, writer, bufferSize);
        } finally {
            closeIgnoringErrors(reader);
            closeIgnoringErrors(writer);
        }
    }

    /**
     * Automatically transfers data from the specified <code>Reader</code> to the <code>Writer</code> until End-Of-File
     * (EOF) is encountered.
     * <p>
     * Note that the Reader and Writer are not closed by this method.
     *
     * @param reader the reader from which the data will be read
     * @param writer the writer where the data will be written.
     * @exception IOException thrown if an error occurs during the transfer.
     */
    public static void transfer(Reader reader, Writer writer) throws IoException {
        transfer(reader, writer, DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Automatically transfers data from the specified <code>Reader</code> to the <code>Writer</code> until End-Of-File
     * (EOF) is encountered and closes both the reader and writer regardless of any successful or erroneous outcome.
     *
     * @param reader the reader from which the data will be read
     * @param writer the writer where the data will be written.
     * @exception IoException thrown if an error occurs during the transfer.
     * @see #transfer(java.io.Reader, java.io.Writer, int)
     */
    public static <R extends Writer> R transferAndClose(Reader reader, R writer) throws IoException {
        try {
            transfer(reader, writer);
            return writer;
        } finally {
            closeIgnoringErrors(reader);
            closeIgnoringErrors(writer);
        }
    }

    /**
     * Automatically transfers data from the specified source <code>{@link File}</code> to the <code>OutputStream</code>
     * until End-Of-File (EOF) is encountered on the input stream and closes both streams regardless of any successful or
     * erroneous outcome.
     *
     * @param sourceFile the source file from which data will be read
     * @param destinationFile the destination file where the data will be written. If the file exists prior to this
     *        operation it will be overwritten.
     * @exception IoException thrown if an error occurs during the transfer.
     * @see #transfer(java.io.InputStream, java.io.OutputStream, int)
     */
    public static void transfer(File sourceFile, File destinationFile) throws IoException {
        transfer(sourceFile, destinationFile, DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Automatically transfers data from the specified source <code>{@link File}</code> to the <code>OutputStream</code>
     * until End-Of-File (EOF) is encountered on the input stream and closes both streams regardless of any successful or
     * erroneous outcome.
     *
     * @param sourceFile the source file from which data will be read
     * @param destinationFile the destination file where the data will be written.
     * @param bufferSize the length of th transfer buffer to use during the transfer for efficiency.
     * @exception IoException thrown if an error occurs during the transfer.
     * @see #transfer(java.io.InputStream, java.io.OutputStream, int)
     */
    public static void transfer(File sourceFile, File destinationFile, int bufferSize) throws IoException {
        try {
            transferAndClose(new FileInputStream(sourceFile), new FileOutputStream(destinationFile), bufferSize);
        } catch (FileNotFoundException e) {
            throw new IoException("An error occurred during transfer: ", e);
        }
    }

    /**
     * Reads and returns a line from the specified <code>Reader</code>. A line is considered to be terminated by a
     * line-feed ('\n') or a carriage-return ('\r') line-feed pair.
     *
     * @param reader the reader from which the line will be read.
     * @return a line of text, without the line termination characters, or null if End-of-file reached.
     * @exception IoException thrown if an error occurs during the read operation.
     */
    public static String readLine(Reader reader) throws IoException {
        int ch = 0;
        try {
            ch = reader.read();
            if (ch == LF) {
                return "";
            }

            StringBuffer lineBuf = new StringBuffer();
            while (ch != -1 && ((char) ch) != LF) {
                lineBuf.append((char) ch);
                ch = reader.read();
            }

            if (lineBuf.length() > 0 && lineBuf.charAt(lineBuf.length() - 1) == CR) {
                lineBuf.deleteCharAt(lineBuf.length() - 1);
            }

            return (ch < 0 ? null : lineBuf.toString());
        } catch (IOException e) {
            throw new IoException("An error occurred reading a line from reader: ", e);
        }
    }

    /**
     * Reads and returns a line from the specified <code>InputStream</code>. A line is considered to be terminated by a
     * line-feed ('\n') or a carriage-return ('\r') line-feed pair.
     *
     * @param is the stream from which the line will be read.
     * @return a line of text, without the line termination characters, or null if End-of-file reached. The default
     *         system character encoding is assumed for the input stream.
     * @exception IoException thrown if an error occurs during the read operation.
     */
    public static String readLine(InputStream is) throws IoException {
        return readLine(is, new ByteArrayOutputStream());
    }

    /**
     * Reads and returns a line from the specified <code>InputStream</code>. A line-is considered to be terminated by a
     * line-feed ('\n') or a carriage-return ('\r') line-feed pair.
     *
     * @param is the stream from which the line will be read.
     * @param baos an existing buffer that will be reset and reused for efficiency
     * @return a line of text, without the line termination characters, or null if End-of-file reached. The default
     *         system character encoding is assumed for the input stream.
     * @exception IOException thrown if an error occurs during the read operation.
     */
    public static String readLine(InputStream is, ByteArrayOutputStream baos) throws IoException {
        return readLine(is, StringUtil.getDefaultCharacterEncoding(), baos);
    }

    /**
     * Reads and returns a line from the specified <code>InputStream</code>. A line is considered to be terminated by a
     * line-feed ('\n') or a carriage-return ('\r') line-feed pair.
     *
     * @param is the stream from which the line will be read.
     * @param encoding the character encoding of the byte stream.
     * @return a line of text, without the line termination characters, or null if End-of-file reached.
     * @exception IoException thrown if an error occurs during the read operation.
     * @see java.lang.String for the supported character encodings.
     */
    public static String readLine(InputStream is, String encoding) throws IoException {
        return readLine(is, encoding, new ByteArrayOutputStream());
    }

    /**
     * Reads and returns a line from the specified <code>InputStream</code>. A line is considered to be terminated by a
     * line-feed ('\n') or a carriage-return ('\r') line-feed pair.
     *
     * @param is the stream from which the line will be read.
     * @param encoding the character encoding of the byte stream.
     * @param baos an existing buffer that will be reset and reused for efficiency
     * @return a line of text, without the line termination characters, or null if End-of-file reached.
     * @exception IOException thrown if an error occurs during the read operation.
     * @see java.lang.String for the supported character encodings.
     */
    public static String readLine(InputStream is, String encoding, ByteArrayOutputStream baos) throws IoException {
        baos.reset();
        String line;
        int byteRead = 0;
        try {
            byteRead = is.read();

            while (byteRead != -1 && byteRead != (byte) LF) {
                baos.write(byteRead);
                byteRead = is.read();
            }

            if (baos.size() == 0) {
                line = (byteRead == -1 ? null : ""); // EOF or \n as first byte read
            }
            else {
                byte byteBuf[] = baos.toByteArray();
                line = new String(byteBuf, 0, (byteBuf[byteBuf.length - 1] == CR ? byteBuf.length - 1 : byteBuf.length),
                    encoding);
            }

            return line;
        } catch (IOException e) {
            throw new IoException("An error occurred reading a line from input stream: ", e);
        }
    }

    /**
     * Flushes the specified stream, converting any <code>java.io.IOException</code> thrown to a
     * <code>org.beanplanet.io.IOException</code>.
     *
     * @param os the stream to be flushed
     * @throws IoException throw if an error occurs flushing the stream.
     */
    public static void flushWithRuntimeError(OutputStream os) throws IoException {
        try {
            os.flush();
        } catch (java.io.IOException ioEx) {
            throw new IoException("Error flushing output stream: ", ioEx);
        }
    }

    /**
     * Flushes the specified writer, converting any <code>java.io.IOException</code> thrown to a
     * <code>org.beanplanet.io.IOException</code>.
     *
     * @param writer the writer to be flushed
     * @throws IoException throw if an error occurs flushing the writer.
     */
    public static void flushWithRuntimeError(Writer writer) throws IoException {
        try {
            writer.flush();
        } catch (java.io.IOException ioEx) {
            throw new IoException("Error flushing writer: ", ioEx);
        }
    }

    /**
     * Writes a character sequence through the given writer.
     *
     * @param writer the writer through which the stream of characters will be written.
     * @param characterSequence the sequence of characters to be written.
     */
    public static void write(Writer writer, CharSequence characterSequence) {
        for (int n=0; n < characterSequence.length(); n++) {
            try {
                writer.write(characterSequence.charAt(n));
            } catch (IOException ioEx) {
                throw new IoException(ioEx);
            }
        }
    }
}
