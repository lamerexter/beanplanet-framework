package org.beanplanet.core.codec;

import org.beanplanet.core.io.IoUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * </p>
 * The ubiquitous base-64 codec which converts binary octets to <a href="https://en.wikipedia.org/wiki/Base64">base-64</a> text.
 * This codec uses a default character set of {@link StandardCharsets#US_ASCII}
 * given that the output text is always in that character range.
 *
 *
 * <p>
 * An implementation of Base64 content transfer encoding encoder, as specified by RFC 1521, Section 5.2, Base64
 * Content-Transfer-Encoding. Refer to <A href="Base64 Content-Transfer-Encoding" alt="RFC 1521 for Base64 Encoding">RFC
 * 1521</A> for further information about the algorithm. This implementation is pretty fast.
 * </p>
 *
 * @author Gary Watson
 * @since 04/04/2002
 */
public class Base64Codec implements Encoder, Decoder {
    /**
     * The Base64 alphabet.
     */
    private static final char[] base64Alphabet = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
            'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
            'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', '+', '/'};

    /**
     * Performs decoding of the input to the given output stream. All input bytes are consumed until end-of-file (EOF).
     *
     * @param input  the input stream whose bytes are to be read and decoded.
     * @param output the output stream where encoded bytes are to be written.
     * @throws IOException if an I/O error occurs reading from the input or writing to the output.
     */
    @Override
    public void decode(final InputStream input, final OutputStream output) throws IOException {
        IoUtil.transfer(new Base64DecodingFilterStream(input), output);
        output.flush();
    }

    /**
     * Performs encoding of the input to the given output stream. All input bytes are consumed until end-of-file (EOF).
     *
     * @param input  the input stream whose bytes are to be read and encoded.
     * @param output the output stream where encoded bytes are to be written.
     * @throws IOException if an I/O error occurs reading from the input or writing to the output.
     */
    @Override
    public void encode(InputStream input, OutputStream output) throws IOException {
        final Base64EncodingFilterStream base64OutputStream = new Base64EncodingFilterStream(output);
        IoUtil.transfer(input, base64OutputStream);
        base64OutputStream.writeRemaining();
        base64OutputStream.flush();
    }

    /**
     * A base-64 encoding filter stream. This output stream wraps another, base-64 encoding bytes which are
     * then written to the wrapped output stream.
     */
    public static class Base64EncodingFilterStream extends FilterOutputStream {
        /**
         * Whether to include line breaks in encoded output.
         */
        private boolean outputLineBreaks = false;

        /**
         * The total number of input bytes encoded.
         */
        private int inputCharCount;

        /**
         * The remaining bits, not yet encoded.
         */
        private int remainingBits;

        public Base64EncodingFilterStream(OutputStream out, boolean outputLineBreaks) {
            super(out);
            this.outputLineBreaks = outputLineBreaks;
        }

        /**
         * Creates new Base64Encoder with the specified target output stream.
         *
         * @param os the underlying <code>OutputStream</code> where Base64 encoded bytes will be written.
         */
        public Base64EncodingFilterStream(OutputStream os) {
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
            } else if (inputCharCount % 3 == 1) {
                // Second byte of a 24-bit input set (4 encoded chars set)
                // Use the remaining 2 bits from last input byte
                lookupIdx = (remainingBits << 4) | (b >> 4);
                remainingBits = b & 0x0f; // Keep low-order last 4 bits
            } else // inputCharCount % 3 == 2
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
         * @param b   the byte array to encode
         * @param off the offset within the array to begin encoding
         * @param len the number of bytes, from the offset specified, to encode
         * @throws IOException if an I/O error occurs.
         * @see #write(int)
         */
        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            for (int n = 0; n < len; n++) {
                write(b[off + n]);
            }
        }

        /**
         * Writes any remaining quadruples to the output stream. This method MUST be called as the last write operation
         * to ensure any remainder is written out. In many cases, such as closing this filter stream, this is done
         * automatically.
         *
         * @throws IOException if an I/O error occurs.
         * @see #write(int)
         */
        public void writeRemaining() throws IOException {
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
            } else if (inputCharCount % 3 == 2) // case 3) above?
            {
                out.write(base64Alphabet[remainingBits << 2]);
                out.write('=');
            }
        }

        /**
         * Closes the Base64 encoding stream and underlying output stream.
         *
         * <p>
         * This method <b>MUST</b> be called in order to ensure that any remaining quadruples are written to the output
         * stream.
         *
         * @throws IOException if an I/O error occurs.
         * @see #write(int)
         */
        @Override
        public void close() throws IOException {
            writeRemaining();
            super.close();
        }
    }

    public static class Base64DecodingFilterStream extends FilterInputStream {
        /**
         * US-ASCII to Base64 character set conversion table (alphabet translation). That is, a table of the Base64
         * characters that are from the US-ASCII character set.
         */
        private static final int[] USAsciiToBase64Charset = new int[128];

        static {
            for (int n = 0; n < 64; n++) {
                // Specifiy the Base64 character value for this US-ASCII character value
                USAsciiToBase64Charset[base64Alphabet[n]] = n;
            }
        }

        /**
         * The total number of input bytes encoded.
         */
        private int inputCharCount;

        /**
         * The remaining bits, not yet encoded.
         */
        private int remainingBits;

        /**
         * Determines if the stream is closed.
         */
        private boolean isStreamClosed = false;

        /**
         * Constructs a new Base64 decoder that reads base64 encoded imput from the specified InputStream.
         *
         * @param is the underlying <code>InputStream</code> where Base64 decoded bytes will be written.
         */
        public Base64DecodingFilterStream(InputStream is) {
            super(is);
        }

        /**
         * Reads a byte from the underlying input stream, decodes it and returns the decoded byte.
         *
         * @return a decoded byte from the underlying Base64 encoded stream.
         * @throws IOException if an I/O error occurs.
         */
        @Override
        public int read() throws IOException {
            if (isStreamClosed) {
                return -1;
            }

            // ------------------------------------------------------------------------
            // As per RFC 1521, ignore any characters outside the Base64 alphabet.
            // Although a very slight performance hit, this meets specification compliance.
            // ------------------------------------------------------------------------
            int b;
            while ((b = in.read()) != -1 && ((Character.isWhitespace((char) b)) || b > 127)
                    && !(b == '=' || (USAsciiToBase64Charset[b]) != 0 || b == 'A')) {
            }

            if (b == -1) {
                isStreamClosed = true;
                return -1;
            } else if (b == '=') {
                isStreamClosed = true;
                // Consume any remaining padding characters
                while (((b = in.read()) != -1) && b == '=' && inputCharCount % 4 != 3) {
                    inputCharCount++;
                }
                return -1;
            } else {
                // Must be US-ASCII character that is in the Base64 alphabet at this point.
                int base64Char = USAsciiToBase64Charset[b];
                if (inputCharCount % 4 == 0) {
                    // First byte of a new 24-bit encoded input set (3 decoded chars set)
                    // Keep all 6 bits and request another byte.
                    remainingBits = base64Char;
                    inputCharCount++;
                    return read();
                } else if (inputCharCount % 4 == 1) {
                    // Second byte of a 24-bit encoded input set (3 encoded chars set)
                    // Use the remaining 6 bits from last input byte plus 2 from this byte
                    b = remainingBits << 2 | base64Char >> 4;
                    remainingBits = base64Char & 0x0f; // Save lower 4 bits
                } else if (inputCharCount % 4 == 2) {
                    // Third byte of a 24-bit encoded input set (3 encoded chars set)
                    // Use the remaining 4 bits from last input byte, coupled
                    // with 4 from this byte
                    b = remainingBits << 4 | base64Char >> 2;
                    remainingBits = base64Char & 0x03; // Save lower 2 bits
                } else if (inputCharCount % 4 == 3) {
                    b = remainingBits << 6 | base64Char;
                    remainingBits = 0; // No bits left over
                }
                inputCharCount++;

                return b;
            }
        }

        /**
         * Decodes the specified byte array, from the specified offset and number of bytes, reading bytes to decode from the
         * underlying input stream.
         *
         * <p>
         * Simply calls <code>read(int)</code> for each byte of the array in the range specified.
         *
         * @param b   the byte array to decode
         * @param off the offset within the array to begin decoding
         * @param len the number of bytes, from the offset specified, to decode
         * @return the number of bytes decoded or -1 of End-Of-File encountered. This will actually be 3/4 of the bytes
         * actually read from the underlying stream as the process of decoding produces 3 decoded octets for every 4
         * encoded read.
         * @throws IOException if an I/O error occurs.
         * @see #read()
         */
        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            if (b.length < (off + len - 1)) {
                throw new IOException("Error during Base64 decoding, Base64Decoder::read(byte[],int,int) - "
                        + "the specified buffer array has length=" + b.length + " which is less than "
                        + "the requested ofset=" + off + " and length=" + len);
            }

            int readCount;
            for (readCount = 0; readCount < len; readCount++) {
                int byteRead = read();
                if (byteRead == -1) {
                    if (readCount == 0) {
                        return -1; // First byte EOF then EOF
                    } else {
                        break; // Second higher byte returns the size read
                    }
                }
                b[off + readCount] = (byte) byteRead;
            }

            return readCount;
        }
    }
}
