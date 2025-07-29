package org.beanplanet.core.codec;

import org.beanplanet.core.io.resource.ByteArrayOutputStreamResource;
import org.beanplanet.core.io.resource.ByteArrayResource;
import org.beanplanet.core.io.resource.Resource;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertArrayEquals;

public class Base64Test {

    @Test
    public void testEncodeWithValidInput() throws IOException {
        // Given
        String inputData = "TestData";
        byte[] expectedOutput = "VGVzdERhdGE=".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // When
        Base64.encode(inputStream, outputStream);

        // Then
        assertArrayEquals(expectedOutput, outputStream.toByteArray());
    }

    @Test
    public void testEncodeEmptyInput() throws IOException {
        // Given
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] expectedOutput = new byte[0];

        // When
        Base64.encode(inputStream, outputStream);

        // Then
        assertArrayEquals(expectedOutput, outputStream.toByteArray());
    }

    @Test
    public void testEncodeSingleByte() throws IOException {
        // Given
        byte[] inputData = new byte[]{(byte) 'A'};
        byte[] expectedOutput = "QQ==".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // When
        Base64.encode(inputStream, outputStream);

        // Then
        assertArrayEquals(expectedOutput, outputStream.toByteArray());
    }

    @Test
    public void testEncodePartialEndingBytes() throws IOException {
        // Given
        byte[] inputData = new byte[]{(byte) 'A', (byte) 'B'};
        byte[] expectedOutput = "QUI=".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // When
        Base64.encode(inputStream, outputStream);

        // Then
        assertArrayEquals(expectedOutput, outputStream.toByteArray());
    }

    @Test
    public void testEncodeLargeInput() throws IOException {
        // Given
        String inputData = "The quick brown fox jumps over the lazy dog.";
        byte[] expectedOutput = "VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wcyBvdmVyIHRoZSBsYXp5IGRvZy4=".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // When
        Base64.encode(inputStream, outputStream);

        // Then
        assertArrayEquals(expectedOutput, outputStream.toByteArray());
    }

    @Test
    public void testDecodeWithValidInput() throws IOException {
        // Given
        byte[] inputData = "VGVzdERhdGE=".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] expectedOutput = "TestData".getBytes(StandardCharsets.UTF_8);

        // When
        Base64.decode(inputStream, outputStream);

        // Then
        assertArrayEquals(expectedOutput, outputStream.toByteArray());
    }

    @Test
    public void testDecodeWithEmptyInput() throws IOException {
        // Given
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] expectedOutput = new byte[0];

        // When
        Base64.decode(inputStream, outputStream);

        // Then
        assertArrayEquals(expectedOutput, outputStream.toByteArray());
    }

    @Test
    public void testDecodeSingleByte() throws IOException {
        // Given
        byte[] inputData = "QQ==".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] expectedOutput = new byte[]{(byte) 'A'};

        // When
        Base64.decode(inputStream, outputStream);

        // Then
        assertArrayEquals(expectedOutput, outputStream.toByteArray());
    }

    @Test
    public void testDecodePartialEndingBytes() throws IOException {
        // Given
        byte[] inputData = "QUI=".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] expectedOutput = new byte[]{(byte) 'A', (byte) 'B'};

        // When
        Base64.decode(inputStream, outputStream);

        // Then
        assertArrayEquals(expectedOutput, outputStream.toByteArray());
    }

    @Test
    public void testDecodeLargeInput() throws IOException {
        // Given
        byte[] inputData = "VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wcyBvdmVyIHRoZSBsYXp5IGRvZy4=".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] expectedOutput = "The quick brown fox jumps over the lazy dog.".getBytes(StandardCharsets.UTF_8);

        // When
        Base64.decode(inputStream, outputStream);

        // Then
        assertArrayEquals(expectedOutput, outputStream.toByteArray());
    }

    @Test
    public void testDecodeFromByteArray() {
        // Given
        byte[] inputData = "QUJDREU=".getBytes(StandardCharsets.US_ASCII);
        byte[] expectedOutput = "ABCDE".getBytes(StandardCharsets.UTF_8);

        // When
        byte[] actualOutput = Base64.decode(inputData);

        // Then
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDecodeFromString() {
        // Given
        String inputData = "QUJDREU=";
        byte[] expectedOutput = "ABCDE".getBytes(StandardCharsets.UTF_8);

        // When
        byte[] actualOutput = Base64.decode(inputData);

        // Then
        assertArrayEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testEncodeFromByteArray() {
        // Given
        byte[] input = "TestInput".getBytes(StandardCharsets.UTF_8);
        String expectedOutput = "VGVzdElucHV0";

        // When
        String actualOutput = Base64.encode(input);

        // Then
        assertArrayEquals(expectedOutput.getBytes(StandardCharsets.UTF_8), actualOutput.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testEncodeFromString() {
        // Given
        String input = "AnotherTest";
        String expectedOutput = "QW5vdGhlclRlc3Q=";

        // When
        String actualOutput = Base64.encode(input);

        // Then
        assertArrayEquals(expectedOutput.getBytes(StandardCharsets.UTF_8), actualOutput.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testEncodeEmptyByteArray() {
        // Given
        byte[] input = new byte[0];
        String expectedOutput = "";

        // When
        String actualOutput = Base64.encode(input);

        // Then
        assertArrayEquals(expectedOutput.getBytes(StandardCharsets.UTF_8), actualOutput.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testEncodeEmptyString() {
        // Given
        String input = "";
        String expectedOutput = "";

        // When
        String actualOutput = Base64.encode(input);

        // Then
        assertArrayEquals(expectedOutput.getBytes(StandardCharsets.UTF_8), actualOutput.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testEncodeWithSpecialCharacters() throws IOException {
        // Given
        String inputData = "!@#$%^&*()_+";
        byte[] expectedOutput = "IUAjJCVeJiooKV8r".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // When
        Base64.encode(inputStream, outputStream);

        // Then
        assertArrayEquals(expectedOutput, outputStream.toByteArray());
    }

    @Test(expected = NullPointerException.class)
    public void testEncodeNullInputByteArray() {
        // Given
        byte[] input = null;

        // When
        Base64.encode(input);

        // Then
        // Expect exception
    }

    @Test(expected = NullPointerException.class)
    public void testEncodeNullInputString() {
        // Given
        String input = null;

        // When
        Base64.encode(input);

        // Then
        // Expect exception
    }

    @Test
    public void testEncodeUsingResource() throws IOException {
        // Given
        String inputData = "TestResourceEncoding";
        byte[] expectedOutput = "VGVzdFJlc291cmNlRW5jb2Rpbmc=".getBytes(StandardCharsets.UTF_8);
        ByteArrayResource inputResource = new ByteArrayResource(inputData.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStreamResource outputResource = new ByteArrayOutputStreamResource();

        // When
        Base64.encode(inputResource, outputResource);

        // Then
        assertArrayEquals(expectedOutput, outputResource.readFullyAsBytes());
    }

    @Test
    public void testEncodeMultilineInput() throws IOException {
        // Given
        String inputData = "Line1\nLine2\nLine3";
        byte[] expectedOutput = "TGluZTEKTGluZTIKTGluZTM=".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // When
        Base64.encode(inputStream, outputStream);

        // Then
        assertArrayEquals(expectedOutput, outputStream.toByteArray());
    }

    @Test
    public void testEncodeWithBinaryData() throws IOException {
        // Given
        byte[] inputData = new byte[]{0x00, 0x01, 0x02, (byte) 0xFF, (byte) 0xFE, 0x7F};
        byte[] expectedOutput = "AAEC//5/".getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputData);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // When
        Base64.encode(inputStream, outputStream);

        // Then
        assertArrayEquals(expectedOutput, outputStream.toByteArray());
    }

    @Test
    public void testEncodeWithResource() throws IOException {
        // Given
        byte[] inputData = new byte[]{0x10, 0x20, 0x30, 0x40, 0x50};
        ByteArrayResource inputResource = new ByteArrayResource(inputData);
        byte[] expectedOutput = "ECAwQFA=".getBytes(StandardCharsets.UTF_8);

        // When
        Resource outputResource = Base64.encode(inputResource);

        // Then
        assertArrayEquals(expectedOutput, outputResource.readFullyAsBytes());
    }
}