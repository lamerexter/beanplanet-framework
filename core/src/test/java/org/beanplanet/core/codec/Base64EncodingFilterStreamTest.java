package org.beanplanet.core.codec;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class Base64EncodingFilterStreamTest {

    @Test
    public void testIsOutputtingLineBreaksTrue() throws IOException {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64Codec.Base64EncodingFilterStream encodingStream = new Base64Codec.Base64EncodingFilterStream(outputStream, true);

        // When
        boolean result = encodingStream.isOutputtingLineBreaks();

        // Then
        assertEquals(true, result);
    }

    @Test
    public void testIsOutputtingLineBreaksFalse() throws IOException {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64Codec.Base64EncodingFilterStream encodingStream = new Base64Codec.Base64EncodingFilterStream(outputStream, false);

        // When
        boolean result = encodingStream.isOutputtingLineBreaks();

        // Then
        assertEquals(false, result);
    }

    @Test
    public void testWriteSingleByte() throws IOException {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64Codec.Base64EncodingFilterStream encodingStream = new Base64Codec.Base64EncodingFilterStream(outputStream);

        // When
        encodingStream.write((int) 'M');
        encodingStream.writeRemaining();
        encodingStream.flush();

        // Then
        assertEquals("TQ==", outputStream.toString());
    }

    @Test
    public void testWriteMultipleBytes() throws IOException {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64Codec.Base64EncodingFilterStream encodingStream = new Base64Codec.Base64EncodingFilterStream(outputStream);

        // When
        encodingStream.write("Man".getBytes());
        encodingStream.writeRemaining();
        encodingStream.flush();

        // Then
        assertEquals("TWFu", outputStream.toString());
    }

    @Test
    public void testWriteWithPadding() throws IOException {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64Codec.Base64EncodingFilterStream encodingStream = new Base64Codec.Base64EncodingFilterStream(outputStream);

        // When
        encodingStream.write("Ma".getBytes());
        encodingStream.writeRemaining();
        encodingStream.flush();

        // Then
        assertEquals("TWE=", outputStream.toString());
    }

    @Test
    public void testLineBreaksEnabled() throws IOException {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64Codec.Base64EncodingFilterStream encodingStream = new Base64Codec.Base64EncodingFilterStream(outputStream, true);
        StringBuilder input = new StringBuilder();
        for (int i = 0; i < 57; i++) {
            input.append("A");
        }
        input.append("B");

        // When
        encodingStream.write(input.toString().getBytes());
        encodingStream.writeRemaining();
        encodingStream.flush();

        // Then
        assertEquals(
                "QUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFB\nQg==",
                outputStream.toString()
        );
    }

    @Test
    public void testCloseWritesRemaining() throws IOException {
        // Given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64Codec.Base64EncodingFilterStream encodingStream = new Base64Codec.Base64EncodingFilterStream(outputStream);

        // When
        encodingStream.write("M".getBytes());
        encodingStream.close();

        // Then
        assertEquals("TQ==", outputStream.toString());
    }
}