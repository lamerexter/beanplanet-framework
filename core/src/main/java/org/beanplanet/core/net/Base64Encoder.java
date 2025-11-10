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

package org.beanplanet.core.net;

import org.beanplanet.core.io.IoException;
import org.beanplanet.core.io.IoUtil;
import org.beanplanet.core.io.resource.ByteArrayOutputStreamResource;
import org.beanplanet.core.io.resource.FileResource;
import org.beanplanet.core.io.resource.Resource;
import org.beanplanet.core.lang.Assert;
import org.beanplanet.core.util.StringUtil;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * A utility class for performing Base64 string and stream encoding operations.
 *
 * <p>
 * An implementation of Base64 content transfer encoding encoder, as specified by RFC 1521, Section 5.2, Base64
 * Content-Transfer-Encoding. Refer to <A href="Base64 Content-Transfer-Encoding" alt="RFC 1521 for Base64 Encoding">RFC
 * 1521</A> for further information about the algorithm.
 * </p>
 *
 * @author Gary Watson
 * @since 04/04/2002
 *
 */
public class Base64Encoder extends FilterOutputStream {
    /** The Base64 alphabet. */
    private static final char base64Alphabet[] = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
            'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
            'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', '+', '/' };

    /** Whether or not to include line breaks in encoded output. */
    private boolean outputLineBreaks = false;

    /** The total number of input bytes encoded. */
    private int inputCharCount;

    /** The remaining bits, not yet encoded. */
    private int remainingBits;

    /**
     * Creates new Base64Encoder with the specified target output stream.
     *
     * @param os the underlying <code>OutputStream</code> where Base64 encoded bytes will be written.
     */
    public Base64Encoder(OutputStream os) {
        super(os);
    }

    /**
     * Whether this encoder outputs line breaks (Line Feed, 0x0D) every 76th output character (according to the Base64
     * encoding specification).
     *
     * @return true if this encoder is to output line breaks
     */
    public boolean isOutputtingLineBreaks() {
        return outputLineBreaks;
    }

    /**
     * Whether this encoder outputs line breaks (Line Feed, 0x0D) every 76th output character (according to the Base64
     * encoding specification).
     *
     * @param newValue true if this encoder is to output line breaks
     */
    public void setOutputtingLineBreaks(boolean newValue) {
        outputLineBreaks = newValue;
    }

    /**
     * Encodes the specified byte and writes the encoded value to the underlying output stream.
     *
     * @param b the byte to encode
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void write(int b) throws IOException {
        // Remove sign-bit on any negative input values or
        // values >
        b = b & 0xff;

        int lookupIdx;
        if (inputCharCount % 3 == 0) {
            // First byte of a new 24-bit input set (4 encoded chars set)
            lookupIdx = b >> 2;
            remainingBits = b & 0x03; // Keep low-order last 2 bits
        }
        else if (inputCharCount % 3 == 1) {
            // Second byte of a 24-bit input set (4 encoded chars set)
            // Use the remaining 2 bits from last input byte
            lookupIdx = (remainingBits << 4) | (b >> 4);
            remainingBits = b & 0x0f; // Keep low-order last 4 bits
        }
        else // inputCharCount % 3 == 2
        {
            // Third byte of a 24-bit input set (4 encoded chars set)
            // Use the remaining 4 bits from last input byte, coupled
            // with the 8 input bits means we write to encoded chars
            // this time.
            lookupIdx = (remainingBits << 2) | (b >> 6);
            out.write(base64Alphabet[lookupIdx]);

            lookupIdx = b & 0x3f; // Keep low-order last 6 bits
            remainingBits = 0;
        }
        out.write(base64Alphabet[lookupIdx]);
        inputCharCount++; // Increment number of encoded chars written out

        // ------------------------------------------------------------------------
        // As per RFC 1521, output encoded characters should be broken into lines
        // of 76 characters or 57 (== 76 * 6/8ths) input characters.
        // ------------------------------------------------------------------------
        if (inputCharCount % 57 == 0 && outputLineBreaks) {
            out.write('\n');
        }
    }

    /**
     * Encodes the specified byte array, from the specified offset and number of bytes, and writes the encoded bytes to
     * the underlying output stream.
     *
     * <p>
     * Simple calls <code>write(int)</code> for each byte of the array in the range specified.
     *
     * @param b the byte array to encode
     * @param off the offset within the array to begin encoding
     * @param len the number of bytes, from the offset specified, to encode
     * @throws IOException if an I/O error occurs.
     * @see #write(int)
     */
    @Override
    public void write(byte b[], int off, int len) throws IOException {
        for (int n = 0; n < len; n++) {
            write(b[off + n]);
        }
    }

    /**
     * Encodes the specified byte buffer.
     *
     * <p>
     * Simple calls <code>write(int)</code> for each byte of the buffer, from the current position to the buffer's limit.
     *
     * @param byteBuffer the buffer to encode.
     * @throws IOException if an I/O error occurs.
     * @see #write(int)
     */
    public void write(ByteBuffer byteBuffer) throws IOException {
        while (byteBuffer.hasRemaining()) {
            write(byteBuffer.get());
        }
    }

    /**
     * Closes the Base64 encoding stream and underlying output stream.
     *
     * <p>
     * This method <b>MUST</b> be called in order to ensure that any remaining quadruples are written to the output
     * stream.
     *
     * @see #write(int)
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void close() throws IOException {
        // -----------------------------------------------------------------
        // Flush any remaining elements of the last 24-bit quantum. Base64
        // output must be an integral number of 24 bit quantums with padding
        // of the last quantum to 24 bits, if necessary.
        // Possible cases are that the final quantum of encoding:
        //
        // 1) Was an integral multiple of 24 bits - in which case we need
        // take no further action since all encoded characters have been
        // written out.
        // 2) Is exactly 8 bits - in which case we need to pad with 16-bits
        // which produces 2 encoded characters (12 bits) and two padding
        // "=" characters.
        // 3) Is exactly 16 bits - in which case we need to pad with 8-bits
        // which produces 3 encoded characters (18 bits) and one padding
        // "=" character.
        // -----------------------------------------------------------------
        if (inputCharCount % 3 == 1) // case 2) above?
        {
            out.write(base64Alphabet[remainingBits << 4]);
            out.write('=');
            out.write('=');
        }
        else if (inputCharCount % 3 == 2) // case 3) above?
        {
            out.write(base64Alphabet[remainingBits << 2]);
            out.write('=');
        }
        super.close();
    }

    /**
     * Returns the Base64 encoded form of the specified input string.
     *
     * <p>
     * This method uses the platform's default character encoding to comprise the result base 64 string.
     * </p>
     *
     * @param inputString the string to encode
     * @return the Base64 encoded form of the input string
     */
    public static String encode(String inputString) {
        return encode(inputString, true);
    }

    /**
     * Returns the Base64 encoded form of the specified input string with line feeds included or excluded from the
     * output.
     *
     * <p>
     * This method uses the platform's default character encoding to comprise the result base 64 string.
     * </p>
     *
     * @param inputString the string to encode
     * @param outputLineBreaks whether or not line breaks are to be included in the encoded output
     * @return the Base64 encoded form of the input string
     */
    public static String encode(String inputString, boolean outputLineBreaks) {
        return encode(inputString.getBytes(), outputLineBreaks);
    }

    /**
     * Convenience method which reads from the specified source resource and outputs the Base64 encoded form to the
     * destination resource.
     *
     * @param source the input to be encoded
     * @param destination the destination where the Base64 encoded form is to be output.
     */
    public static void encode(File source, File destination) {
        encode(new FileResource(source), new FileResource(destination));
    }

    /**
     * Convenience method which reads from the specified source resource and outputs the Base64 encoded form to the
     * destination resource.
     *
     * @param source the input to be encoded
     * @param destination the destination where the Base64 encoded form is to be output.
     */
    public static void encode(Resource source, Resource destination) {
        Assert.notNull(source, "The source resource to encode may not be null");
        Assert.notNull(destination, "The destination may not be null");
        try (InputStream sourceIS = source.getInputStream(); OutputStream destOS = new Base64Encoder(destination.getOutputStream())) {
            IoUtil.transfer(sourceIS, destOS);
        } catch (IOException ioEx) {
            throw new IoException("Unable to complete Base64 encode operation [source=" + source + ", destination=" + destination + "]: ", ioEx);
        }
    }

    /**
     * Convenience method which reads from the specified source resource and returns the Base64 encoded form as a
     * string. Careful when using this method, which is only intended for use with short source content.
     *
     * @param source the input to be encoded.
     * @return a string of the base664 encoded source content.
     */
    public static String encode(Resource source) {
        Assert.notNull(source, "The source resource to encode may not be null");
        ByteArrayOutputStreamResource destination = new ByteArrayOutputStreamResource();
        encode(source, destination);

        return new String(destination.readFullyAsBytes(), StandardCharsets.US_ASCII);
    }

    /**
     * Returns the Base64 encoded form of the specified bytes.
     *
     * @param bytes the bytes to encode
     * @return a string containing human-readable Base64 encoded form of the bytes.
     */
    public static String encode(byte[] bytes) {
        return encode(ByteBuffer.wrap(bytes), true);
    }

    /**
     * Returns the Base64 encoded form of the specified bytes.
     *
     * @param bytes the bytes to encode
     * @param outputLineBreaks whether or not line breaks are to be included in the encoded output
     * @return a string containing human-readable Base64 encoded form of the bytes.
     */
    public static String encode(byte[] bytes, boolean outputLineBreaks) {
        return encode(ByteBuffer.wrap(bytes), outputLineBreaks);
    }

    /**
     * Returns the Base64 encoded form of the specified byte buffer.
     *
     * @param byteBuffer the byte buffer to encode
     * @return a string containing human-readable Base64 encoded form of the bytes.
     */
    public static String encode(ByteBuffer byteBuffer) {
        return encode(byteBuffer, true);
    }

    /**
     * Returns the Base64 encoded form of the specified byte buffer.
     *
     * @param byteBuffer the byte buffer to encode
     * @param outputLineBreaks whether or not line breaks are to be included in the encoded output
     * @return a string containing human-readable Base64 encoded form of the bytes.
     */
    public static String encode(ByteBuffer byteBuffer, boolean outputLineBreaks) {
        // Maximum output size will be 137% of input.
        ByteArrayOutputStream baos = new ByteArrayOutputStream((int) (byteBuffer.capacity() * 1.37));
        Base64Encoder encoder = new Base64Encoder(baos);
        encoder.setOutputtingLineBreaks(outputLineBreaks);

        try {
            encoder.write(byteBuffer);
            encoder.close();

            return baos.toString(StringUtil.getDefaultCharacterEncoding());
        } catch (IOException ex) {
            // Should never get here as the I/O streams are in-memory and
            // should not fail.
            throw new IoException("I/O error encountered during Base64 encode operation.", ex);
        }
    }
}
