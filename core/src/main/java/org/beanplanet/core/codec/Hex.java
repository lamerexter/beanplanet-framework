package org.beanplanet.core.codec;

import org.beanplanet.core.io.IoException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * A hexadecimal codec.
 */
public class Hex implements Encoder, Decoder {
    public static final Charset DEFAULT_CHARSET = StandardCharsets.US_ASCII;
    private static final String HEXDIGITS_STR = "0123456789ABCDEF";
    private static final char[] HEXDIGITS = HEXDIGITS_STR.toCharArray();
    private static final Hex _instance = new Hex();

    private Charset charset;

    public Hex(final Charset charset) {
        this.charset = charset;
    }

    public Hex() {
        this(DEFAULT_CHARSET);
    }

    public static String encodeToHexString(byte[] bytes) {
        return bytes == null ? null : new String(_instance.encode(bytes));
    }

    public static byte[] decodeFromHexString(String hex) {
        return hex == null ? null : _instance.decode(hex.getBytes());
    }

    @Override
    public void encode(final InputStream inputStream, final OutputStream outputStream) {
        try {
            int b;
            while ((b = inputStream.read()) >= 0) {
                outputStream.write(HEXDIGITS[(b & 0xf0) >> 4]);
                outputStream.write(HEXDIGITS[b & 0x0f]);
            }
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }

    @Override
    public void decode(InputStream inputStream, OutputStream outputStream) {
        try {
            int b1;
            while ((b1 = inputStream.read()) >= 0) {
                int b2 = inputStream.read();
                if ( b2 < 0 ) {
                    throw new IoException("Invalid hexadecimal encoding - reached EOF before presence of second nibble.");
                }

                outputStream.write((byte)((HEXDIGITS_STR.indexOf(b1 >= 'a' ? b1-32 : b1) << 4) + HEXDIGITS_STR.indexOf(b2 >= 'a' ? b2-32 : b2)));
            }
        } catch (IOException ioEx) {
            throw new IoException(ioEx);
        }
    }
}
