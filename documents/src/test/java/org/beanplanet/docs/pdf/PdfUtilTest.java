package org.beanplanet.docs.pdf;

import org.beanplanet.core.io.DigestUtil;
import org.beanplanet.core.io.resource.ByteArrayResource;
import org.beanplanet.core.io.resource.Resource;
import org.beanplanet.core.io.resource.UrlResource;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static org.beanplanet.docs.pdf.PdfUtil.convertImageToPdf;
import static org.beanplanet.docs.pdf.PdfUtil.extractPdfPageToPNGBytes;
import static org.junit.Assert.assertEquals;

public class PdfUtilTest {
    @Test
    public void convertImageToPdf_jpeg_xAndYBothLessThanA4() {
        final Resource destination = new ByteArrayResource(new ByteArrayOutputStream());
        convertImageToPdf(new UrlResource(PdfUtilTest.class.getResource("apple_260x191.jpg")), destination);
        final byte[] pageBytes = extractPdfPageToPNGBytes(destination, 0);
        assertEquals("c925700ccbb6afd0037e46a281b2c20b", DigestUtil.md5HashByteStreamToHexadecimal(new ByteArrayResource(pageBytes)));
    }

    @Test
    public void convertImageToPdf_jpeg_xAndYBothLargerThanA4_landscape() {
        final Resource destination = new ByteArrayResource(new ByteArrayOutputStream());
        convertImageToPdf(new UrlResource(PdfUtilTest.class.getResource("apple_2600x1908.jpg")), destination);
        final byte[] pageBytes = extractPdfPageToPNGBytes(destination, 0);
        assertEquals("adcc5d89bb237427b3da3967555c7ddd", DigestUtil.md5HashByteStreamToHexadecimal(new ByteArrayResource(pageBytes)));
    }

    @Test
    public void convertImageToPdf_jpeg_xAndYBothLargerThanA4_portrait() {
//        final var destination = new FileResource("/tmp/test.pdf");
        final Resource destination = new ByteArrayResource(new ByteArrayOutputStream());
        convertImageToPdf(new UrlResource(PdfUtilTest.class.getResource("apple_2600x1908_rotated_clockwise_90.jpg")), destination);
        final byte[] pageBytes = extractPdfPageToPNGBytes(destination, 0);
        assertEquals("be8402bf31a52389983f1c75f01aeb20", DigestUtil.md5HashByteStreamToHexadecimal(new ByteArrayResource(pageBytes)));
    }

    @Test
    public void convertImageToPdf_jpeg_exif_orientation_correct_Landscape() {
        final String[] expectedPageHashes = new String[]{
                "a55d9ae56023bb21a0c44ff116821f76",
                "01335e5a7366fb2815015d79add75b3d",
                "bd2de23b1932e93ceb00ddf3d447aaab",
                "a142f092afda30bc45db0ced1fad58db",
                "27eb0e4492f9152b785ed88c0b4eb4a5",
                "c62a0fc96d4e39fc2f69423f59c42d57",
                "0ee96e9c093c2a3d9a351f3e78a111a7",
                "0c4d1add9edab81ca80488c30cb5c36d",
                "2329f0b6a717194fe714b36d82d9e034"
        };
        for (int n = 0; n <= 8; n++) {
//            final var destination = new FileResource("/tmp/Landscape_"+n+".pdf");
            final Resource destination = new ByteArrayResource(new ByteArrayOutputStream());
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
                "4d73f15fa0fad14bb22e6f6d6da3bd6e",
                "6ca3fa2ec33c9c90bfc403149556bdd1",
                "1c8a17f0615548afad052b4c7e87c693",
                "232856e504236bfbfa4da1f41896ca6c",
                "72a0326f0c91a9ba9dfccd10b1474c10",
                "cb79a1f04ef35028c999ca918a0435e8",
                "d909426ba0762a9d6c30c43fbd83685d"
        };
        for (int n = 0; n <= 8; n++) {
//            final var destination = new FileResource("/tmp/Portrait_"+n+".pdf");
            final Resource destination = new ByteArrayResource(new ByteArrayOutputStream());
            convertImageToPdf(new UrlResource(PdfUtilTest.class.getResource("Portrait_" + n + ".jpg")), destination);
            final byte[] pageBytes = extractPdfPageToPNGBytes(destination, 0);
            assertEquals(expectedPageHashes[n], DigestUtil.md5HashByteStreamToHexadecimal(new ByteArrayResource(pageBytes)));
        }
    }
}