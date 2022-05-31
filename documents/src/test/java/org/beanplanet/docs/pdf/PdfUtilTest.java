package org.beanplanet.docs.pdf;

import static org.beanplanet.docs.pdf.PdfUtil.*;
import static org.junit.Assert.assertEquals;

import org.beanplanet.core.io.DigestUtil;
import org.beanplanet.core.io.resource.ByteArrayOutputStreamResource;
import org.beanplanet.core.io.resource.ByteArrayResource;
import org.beanplanet.core.io.resource.Resource;
import org.beanplanet.core.io.resource.UrlResource;
import org.junit.Test;

public class PdfUtilTest {
    @Test
    public void convertImageToPdf_jpeg_xAndYBothLessThanA4() {
        final Resource destination = new ByteArrayOutputStreamResource();
        convertImageToPdf(new UrlResource(PdfUtilTest.class.getResource("apple_260x191.jpg")), destination);
        final byte[] pageBytes = extractPdfPageToPNGBytes(destination, 0);
        assertEquals("c925700ccbb6afd0037e46a281b2c20b", DigestUtil.md5HashByteStreamToHexadecimal(new ByteArrayResource(pageBytes)));
    }

    @Test
    public void convertImageToPdf_jpeg_xAndYBothLargerThanA4_landscape() {
        final Resource destination = new ByteArrayOutputStreamResource();
        convertImageToPdf(new UrlResource(PdfUtilTest.class.getResource("apple_2600x1908.jpg")), destination);
        final byte[] pageBytes = extractPdfPageToPNGBytes(destination, 0);
        assertEquals("adcc5d89bb237427b3da3967555c7ddd", DigestUtil.md5HashByteStreamToHexadecimal(new ByteArrayResource(pageBytes)));
    }

    @Test
    public void convertImageToPdf_jpeg_xAndYBothLargerThanA4_portrait() {
        final Resource destination = new ByteArrayOutputStreamResource();
        convertImageToPdf(new UrlResource(PdfUtilTest.class.getResource("apple_2600x1908_rotated_clockwise_90.jpg")), destination);
        final byte[] pageBytes = extractPdfPageToPNGBytes(destination, 0);
        assertEquals("be8402bf31a52389983f1c75f01aeb20", DigestUtil.md5HashByteStreamToHexadecimal(new ByteArrayResource(pageBytes)));
    }

    @Test
    public void convertImageToPdf_jpeg_exif_orientation_correct_Landscape() {
        final String[] expectedPageHashes = new String[]{
                "a55d9ae56023bb21a0c44ff116821f76",
                "01335e5a7366fb2815015d79add75b3d",
                "906aeda6e100bdee18cc9d63177294d6",
                "f8715dd030328295f48088ab1d83a338",
                "0e0a7c95e7492c9d18088214f4e3385c",
                "de3829e1cf7991622bb93de875d0ab55",
                "09ac33990053113f3522f1aae6dd227d",
                "8fa2e6a7e529c5c3f1e54141f9317597",
                "ca2411670a2861454421e6a4c9476e97"
        };
        for (int n = 0; n <= 8; n++) {
            final Resource destination = new ByteArrayOutputStreamResource();
            convertImageToPdf(new UrlResource(PdfUtilTest.class.getResource("Landscape_" + n + ".jpg")), destination);
            final byte[] pageBytes = extractPdfPageToPNGBytes(destination, 0);
            assertEquals(expectedPageHashes[n], DigestUtil.md5HashByteStreamToHexadecimal(new ByteArrayResource(pageBytes)));
        }
    }

    @Test
    public void convertImageToPdf_jpeg_exif_orientation_correct_Portrait() {
        final String[] expectedPageHashes = new String[]{
                "df7cf1e9f4be86720b15e03d552b4b9e",
                "5e514a235abb2c9f1ca7e17ee6ca75fc",
                "7cd625466455abd262a90a2bdc0813c0",
                "5616d1b27a260e3573dbbf233539c32f",
                "4d201dec84bd939ed506d8ce3721d3e3",
                "a7e13e8ad77d5f9b80daaf5207bd0780",
                "5143394f9f751940fa96204da4e4692b",
                "25802915254927468fc3a145a9f6f9d3",
                "7e8efe1775770c82fc3829d32140feb9"
        };
        for (int n = 0; n <= 8; n++) {
            final Resource destination = new ByteArrayOutputStreamResource();
            convertImageToPdf(new UrlResource(PdfUtilTest.class.getResource("Portrait_" + n + ".jpg")), destination);
            final byte[] pageBytes = extractPdfPageToPNGBytes(destination, 0);
            assertEquals(expectedPageHashes[n], DigestUtil.md5HashByteStreamToHexadecimal(new ByteArrayResource(pageBytes)));
        }
    }
}