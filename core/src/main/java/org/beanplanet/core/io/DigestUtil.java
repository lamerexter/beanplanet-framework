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

import org.beanplanet.core.io.resource.ByteArrayResource;
import org.beanplanet.core.io.resource.Resource;
import org.beanplanet.core.io.resource.StringResource;
import org.beanplanet.core.lang.Assert;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;

import static org.beanplanet.core.io.IoUtil.DEFAULT_TRANSFER_BUF_SIZE;

/**
 * I/O Stream utility class.
 *
 * @author Gary Watson
 */
public class DigestUtil {
    private static final char[] HEXDIGITS = "0123456789ABCDEF".toCharArray();

    /**
     * Calculates a message digest of the specified {@link Resource} byte stream.
     *
     * @param resource the resource whose byte stream is to be digested.
     * @return the digest hash value calculated over the byte stream.
     * @throws IoException thrown if an error occurs performing the message digest.
     */
    public static String md5HashByteStreamToHexadecimal(Resource resource) throws IoException {
        return hashToHexadecimalString(hashByteStream(resource, "MD5"));
    }

    /**
     * Calculates a message digest of the specified {@link Resource} byte stream.
     *
     * @param resource the resource whose byte stream is to be digested.
     * @param messageDigestAlgorthmName the message digest algorithm required (such as "SHA", "MD5", and so on).
     * @return the digest hash value calculated over the byte stream.
     * @throws IoException thrown if an error occurs performing the message digest.
     */
    public static String hashByteStreamToHexadecimal(Resource resource, String messageDigestAlgorthmName) throws IoException {
        return hashToHexadecimalString(hashByteStream(resource, messageDigestAlgorthmName));
    }

    /**
     * Calculates a message digest of the specified {@link Resource} byte stream.
     *
     * @param resource the resource whose byte stream is to be digested.
     * @param messageDigestAlgorthmName the message digest algorithm required (such as "SHA", "MD5", and so on).
     * @return the digest hash value calculated over the byte stream.
     * @throws IoException thrown if an error occurs performing the message digest.
     */
    public static byte[] hashByteStream(Resource resource, String messageDigestAlgorthmName) throws IoException {
        try {
            try (InputStream is=resource.getInputStream()) {
                return hashByteStream(is, messageDigestAlgorthmName, null, DEFAULT_TRANSFER_BUF_SIZE);
            }
        } catch (IOException e) {
            throw new IoException("An error occurred hashing resource content: ", e);
        }
    }

    /**
     * Calculates a message digest of the specified {@link Resource} byte stream.
     *
     * @param resource the resource whose byte stream is to be digested.
     * @param messageDigestAlgorthmName the message digest algorithm required (such as "SHA", "MD5", and so on).
     * @param providerName the message digest algorithm provider (such as "BC" for BouncyCastle), which may be null; in
     *        which case the default, if any, will be used.
     * @param transferBufSize the I/O transfer buffer size to use in the digest; large buffers improve performance but
     *        use more memory.
     * @return the digest hash value calculated over the byte stream.
     * @throws IoException thrown if an error occurs performing the message digest.
     */
    public static byte[] hashByteStream(Resource resource, String messageDigestAlgorthmName, String providerName,
                                        int transferBufSize) throws IoException {
        try {
            try (InputStream is=resource.getInputStream()) {
                return hashByteStream(is, messageDigestAlgorthmName, providerName, transferBufSize);
            }
        } catch (IOException e) {
            throw new IoException("An error occurred hashing resource content: ", e);
        }
    }

    /**
     * Calculates a message digest of the specified byte stream.
     *
     * @param is the stream to digest.
     * @param messageDigestAlgorthmName the message digest algorithm required (such as "SHA", "MD5", and so on).
     * @param providerName the message digest algorithm provider (such as "BC" for BouncyCastle), which may be null; in
     *        which case the default, if any, will be used.
     * @param transferBufSize the I/O transfer buffer size to use in the digest; large buffers improve performance but
     *        use more memory.
     * @return the digest hash value calculated over the byte stream.
     * @throws IoException thrown if an error occurs performing the message digest.
     */
    public static byte[] hashByteStream(InputStream is, String messageDigestAlgorthmName, String providerName,
                                        int transferBufSize) throws IoException  {
        try {
            MessageDigest md = providerName != null ? MessageDigest.getInstance(messageDigestAlgorthmName, providerName)
                : MessageDigest.getInstance(messageDigestAlgorthmName);
            byte tempBuf[] = new byte[transferBufSize];
            int readCount = 0;
            while ((readCount = is.read(tempBuf)) >= 0) {
                md.update(tempBuf, 0, readCount);
            }
            return md.digest();
        } catch (Throwable th) {
            throw new IoException("Unable to perform message digest (\"" + messageDigestAlgorthmName + "\" provided by \""
                + providerName + "\"): " + th.getMessage());
        }
    }

    /**
     * Calculates a message digest of the specified byte stream. The default transfer buffer size will be used for I/O
     * transfers.
     *
     * @param is the stream to digest.
     * @param messageDigestAlgorthmName the message digest algorithm required (such as "SHA", "MD5", and so on).
     * @param providerName the message digest algorithm provider (such as "BC" for BouncyCastle), which may be null; in
     *        which case the default, if any, will be used.
     * @return the digest hash value calculated over the byte stream.
     * @throws IoException thrown if an error occurs performing the message digest.
     * @see IoUtil#DEFAULT_TRANSFER_BUF_SIZE the default transfer buffer size.
     */
    public static byte[] hashByteStream(InputStream is, String messageDigestAlgorthmName, String providerName)
        throws IoException {
        return hashByteStream(is, messageDigestAlgorthmName, providerName, DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Calculates a message digest of the specified byte stream. The default provider of the specified digest algorithm
     * will be used.
     *
     * @param is the stream to digest.
     * @param messageDigestAlgorthmName the message digest algorithm required (such as "SHA", "MD5", and so on).
     * @param transferBufSize the I/O transfer buffer size to use in the digest; large buffers improve performance but
     *        use more memory.
     * @return the digest hash value calculated over the byte stream.
     * @throws IOException thrown if an error occurs performing the message digest.
     */
    public static byte[] hashByteStream(InputStream is, String messageDigestAlgorthmName, int transferBufSize)
        throws IoException {
        return hashByteStream(is, messageDigestAlgorthmName, null, transferBufSize);
    }

    /**
     * Calculates a message digest of the specified byte stream. The default provider of the specified digest algorithm
     * will be used and the default transfer buffer size will be used for I/O transfers
     *
     * @param is the stream to digest.
     * @param messageDigestAlgorthmName the message digest algorithm required (such as "SHA", "MD5", and so on).
     * @return the digest hash value calculated over the byte stream.
     * @throws IoException thrown if an error occurs performing the message digest.
     * @see IoUtil#DEFAULT_TRANSFER_BUF_SIZE the default transfer buffer size.
     */
    public static byte[] hashByteStream(InputStream is, String messageDigestAlgorthmName) throws IoException {
        return hashByteStream(is, messageDigestAlgorthmName, null, DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Calculates a message digest of the specified byte stream and returns the hexadecimal (Base 16) string of the
     * hashcode.
     *
     * @param is the stream to digest.
     * @param messageDigestAlgorthmName the message digest algorithm required (such as "SHA", "MD5", and so on).
     * @param providerName the message digest algorithm provider (such as "BC" for BouncyCastle), which may be null; in
     *        which case the default, if any, will be used.
     * @param transferBufSize the I/O transfer buffer size to use in the digest; large buffers improve performance but
     *        use more memory.
     * @return the digest hash value calculated over the byte stream.
     * @throws IoException thrown if an error occurs performing the message digest.
     */
    public static String hashByteStreamToHexadecimal(InputStream is, String messageDigestAlgorthmName,
                                                     String providerName, int transferBufSize) throws IoException {
        return hashToHexadecimalString(hashByteStream(is, messageDigestAlgorthmName, providerName, transferBufSize));
    }

    /**
     * Calculates a message digest of the specified byte stream and returns the hexadecimal (Base 16) string of the
     * hashcode. The default transfer buffer size will be used for I/O transfers.
     *
     * @param is the stream to digest.
     * @param messageDigestAlgorthmName the message digest algorithm required (such as "SHA", "MD5", and so on).
     * @param providerName the message digest algorithm provider (such as "BC" for BouncyCastle), which may be null; in
     *        which case the default, if any, will be used.
     * @return the digest hash value calculated over the byte stream.
     * @throws IoException thrown if an error occurs performing the message digest.
     * @see IoUtil#DEFAULT_TRANSFER_BUF_SIZE the default transfer buffer size.
     */
    public static String hashByteStreamToHexadecimal(InputStream is, String messageDigestAlgorthmName,
                                                     String providerName) throws IoException {
        return hashByteStreamToHexadecimal(is, messageDigestAlgorthmName, providerName, DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Calculates a message digest of the specified byte stream and returns the hexadecimal (Base 16) string of the
     * hashcode. The default provider of the specified digest algorithm will be used.
     *
     * @param is the stream to digest.
     * @param messageDigestAlgorthmName the message digest algorithm required (such as "SHA", "MD5", and so on).
     * @param transferBufSize the I/O transfer buffer size to use in the digest; large buffers improve performance but
     *        use more memory.
     * @return the digest hash value calculated over the byte stream.
     * @throws IoException thrown if an error occurs performing the message digest.
     */
    public static String hashByteStreamToHexadecimal(InputStream is, String messageDigestAlgorthmName,
                                                     int transferBufSize) throws IoException {
        return hashByteStreamToHexadecimal(is, messageDigestAlgorthmName, null, transferBufSize);
    }

    /**
     * Calculates a message digest of the specified byte stream and returns the hexadecimal (Base 16) string of the
     * hashcode. The default provider of the specified digest algorithm will be used and the default transfer buffer size
     * will be used for I/O transfers
     *
     * @param is the stream to digest.
     * @param messageDigestAlgorthmName the message digest algorithm required (such as "SHA", "MD5", and so on).
     * @return the digest hash value calculated over the byte stream.
     * @throws IoException thrown if an error occurs performing the message digest.
     * @see IoUtil#DEFAULT_TRANSFER_BUF_SIZE the default transfer buffer size.
     */
    public static String hashByteStreamToHexadecimal(InputStream is, String messageDigestAlgorthmName)
        throws IoException {
        return hashByteStreamToHexadecimal(is, messageDigestAlgorthmName, null, DEFAULT_TRANSFER_BUF_SIZE);
    }

    /**
     * Converts a hash of bytes to hexadecimal string notation.
     *
     * @param hash the hash byte codes to convert.
     * @return the hexadecimal (base 16) string representation of the hash codes.
     */
    public static String hashToHexadecimalString(byte hash[]) {
        String encodedStr = new BigInteger(1, hash).toString(16);

      /* this is important, toString leaves out initial 0 */
        if (encodedStr.length() % 2 > 0) {
            encodedStr = "0" + encodedStr;
        }

        return encodedStr;
    }

    /**
     * Wraps a standard check-exception based write operation with an unchecked version.
     *
     * @param writer the writer through which the data will be written.
     * @param charSequence the character sequence data to be written.
     * @throws IoException if an I/O error occurs performing the write operation.
     */
    public static void writeUnchecked(Writer writer, CharSequence charSequence) throws IoException {
        Assert.notNull(writer, "The writer may not be null");

        if (charSequence == null) {
            return;
        }

        try {
            writer.write(charSequence.toString());
        } catch (IOException ioEx) {
            throw new org.beanplanet.core.io.IoException(ioEx);
        }
    }

    /**
     * Wraps a standard check-exception based close operation with an unchecked version.
     *
     * @param writer the writer to be closed
     * @throws IoException if an I/O error occurs performing the close operation.
     */
    public static void closeUnchecked(Writer writer) throws IoException {
        Assert.notNull(writer, "The writer may not be null");

        try {
            writer.close();
        } catch (IOException ioEx) {
            throw new org.beanplanet.core.io.IoException(ioEx);
        }
    }

    public static String hexDump(String input) {
        return hexDump(input, 15);
    }

    public static String hexDump(Resource input) {
        return hexDump(input, 15);
    }

    public static String hexDump(String input, int width) {
        if (input == null) return null;
        return hexDump(new StringResource(input), width);
    }

    public static String hexDump(Resource input, int width) {
        if (input == null) return null;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayResource bar = new ByteArrayResource(baos);
        hexDump(input, bar, width);
        return new String(baos.toByteArray());
    }

    public static void hexDump(Resource input, Resource output, int width) {
        int offset = 0;
        byte buf[] = new byte[width];


        try (InputStream is = input.getInputStream(); OutputStream os = output.getOutputStream()) {
            int readBytes;
            while ((readBytes = is.read(buf)) > 0) {
                // Print offset information
                os.write(String.format("%06d", offset).getBytes());

                // Consume configurable amount of input
                os.write(' ');
                for (int n=0; n < readBytes; n++) {
                    if (n > 0) {
                        os.write(' ');
                    }
                    os.write(HEXDIGITS[(buf[n] & 0xf0) >> 4]);
                    os.write(HEXDIGITS[buf[n] & 0x0f]);
                }
                os.write(' ');
                for (int n=0; n < width-readBytes; n++) {
                    os.write(' ');
                    os.write(' ');
                    os.write(' ');
                }

                // Output printable character representation
                for (int n=0; n < readBytes; n++) {
                    int ch = buf[n];
                    os.write((ch >= ' ' && ch <= '~') ? ch : '.');
                }
                os.write('\n');
                offset += readBytes;
            }
        } catch (IOException ioEx) {
            throw new org.beanplanet.core.io.IoException(ioEx);
        }
    }
}

