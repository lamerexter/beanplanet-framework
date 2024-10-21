package org.beanplanet.core.io.resource;

import org.beanplanet.core.net.http.MediaTypes;
import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class DataUrlResourceTest {
    @Test
    public void givenATextDataUrl_whenUsedToCreateAResource_thenTheUrlIsCorrect_andCanBeRead() {
        // Given
        final String originalData = "Hello World! £";

        // When
        DataUrlResource data = new DataUrlResource("data:text/html;charset=ISO-8859-1;base64,SGVsbG8gV29ybGQhIKM=");

        // Then
        assertThat(data.getUrl().toExternalForm(), equalTo("data:text/html;charset=ISO-8859-1;base64,SGVsbG8gV29ybGQhIKM="));

        // And
        assertThat(data.readFullyAsString(), equalTo(originalData));
    }

    @Test
    public void givenElementsForATextDataUrl_whenUsedToCreateAResourceWithBase64Encoding_thenTheUrlDataResourceIsCorrect_andCanBeRead() {
        // Given
        final String originalData = "Hello World! £";

        // When
        DataUrlResource data = DataUrlResource.builder()
                                              .mediaType(MediaTypes.Text.HTML)
                                              .base64Encoded(true)
                                              .charset(StandardCharsets.ISO_8859_1)
                                              .data(originalData.getBytes(StandardCharsets.ISO_8859_1))
                                              .build();

        // Then
        assertThat(data.getUrl().toExternalForm(), equalTo("data:text/html;charset=ISO-8859-1;base64,SGVsbG8gV29ybGQhIKM="));

        // And
        assertThat(data.readFullyAsString(), equalTo(originalData));
    }

    @Test
    public void givenElementsForATextDataUrl_whenUsedToCreateAResourceWithUrlEncoding_thenTheUrlDataResourceIsCorrect_andCanBeRead() {
        // Given
        final String originalData = "Hello World! £";

        // When
        DataUrlResource data = DataUrlResource.builder()
                                              .mediaType(MediaTypes.Text.HTML)
                                              .base64Encoded(false)
                                              .charset(StandardCharsets.ISO_8859_1)
                                              .data(originalData.getBytes(StandardCharsets.ISO_8859_1))
                                              .build();

        // Then
        assertThat(data.getUrl().toExternalForm(), equalTo("data:text/html;charset=ISO-8859-1,Hello%20World%21%20%A3"));

        // And
        assertThat(data.readFullyAsString(), equalTo(originalData));
    }

    @Test
    public void givenAnSvgImageDataUrl_whenReadAndWrittenBase64Coded_thenTheCorrectImageIsDecodedAndEncoded() {
        // Given
        final String imageDataUrl = "data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjQiIGhlaWdodD0iMjQiIHZpZXdCb3g9IjAgMCAyNCAyNCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KPHBhdGggZmlsbC1ydWxlPSJldmVub2RkIiBjbGlwLXJ1bGU9ImV2ZW5vZGQiIGQ9Ik01Ljk4ODYgMy45MjkyM0MyLjY2OTE0IDcuMTY4MiAyLjk5OTAyIDEyIDUuOTg4NiAxNS42NTg2TDExLjk5OSAyM0wxOC4wMDk0IDE1LjY1ODZDMjAuOTk5IDEyIDIxLjMyODkgNy4xNjgyIDE4LjAwOTQgMy45MjkyM0MxNC42OSAwLjY5MDI1NyA5LjMwODA1IDAuNjkwMjU3IDUuOTg4NiAzLjkyOTIzWk0xMS45OTkgMTIuNUMxMy42NTU5IDEyLjUgMTQuOTk5IDExLjE1NjkgMTQuOTk5IDkuNUMxNC45OTkgNy44NDMxNSAxMy42NTU5IDYuNSAxMS45OTkgNi41QzEwLjM0MjIgNi41IDguOTk5MDIgNy44NDMxNSA4Ljk5OTAyIDkuNUM4Ljk5OTAyIDExLjE1NjkgMTAuMzQyMiAxMi41IDExLjk5OSAxMi41WiIgZmlsbD0iYmxhY2siLz4KPC9zdmc+Cg==";
        final String expectedSvgImage = "<svg width=\"24\" height=\"24\" viewBox=\"0 0 24 24\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\">\n" +
                "<path fill-rule=\"evenodd\" clip-rule=\"evenodd\" d=\"M5.9886 3.92923C2.66914 7.1682 2.99902 12 5.9886 15.6586L11.999 23L18.0094 15.6586C20.999 12 21.3289 7.1682 18.0094 3.92923C14.69 0.690257 9.30805 0.690257 5.9886 3.92923ZM11.999 12.5C13.6559 12.5 14.999 11.1569 14.999 9.5C14.999 7.84315 13.6559 6.5 11.999 6.5C10.3422 6.5 8.99902 7.84315 8.99902 9.5C8.99902 11.1569 10.3422 12.5 11.999 12.5Z\" fill=\"black\"/>\n" +
                "</svg>\n";
        // When
        DataUrlResource data = new DataUrlResource(imageDataUrl);

        // Then
        assertThat(data.getUrl().toExternalForm(), equalTo(imageDataUrl));
        assertThat(data.readFullyAsString(), equalTo(expectedSvgImage));

        // When
        data = DataUrlResource.builder()
                              .base64Encoded()
                              .mediaType(MediaTypes.Image.SVG)
                              .data(expectedSvgImage)
                              .build();

        // Then
        assertThat(data.getUrl().toExternalForm(), equalTo(imageDataUrl));
    }

    @Test
    public void givenAnSvgImageDataUrl_whenReadAndWrittenUrlCoded_thenTheCorrectImageIsDecodedAndEncoded() {
        // Given
        final String imageDataUrl = "data:image/svg+xml,%3Csvg%20width%3D%2224%22%20height%3D%2224%22%20viewBox%3D%220%200%2024%2024%22%20fill%3D%22none%22%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%3E%0A%3Cpath%20fill-rule%3D%22evenodd%22%20clip-rule%3D%22evenodd%22%20d%3D%22M5.9886%203.92923C2.66914%207.1682%202.99902%2012%205.9886%2015.6586L11.999%2023L18.0094%2015.6586C20.999%2012%2021.3289%207.1682%2018.0094%203.92923C14.69%200.690257%209.30805%200.690257%205.9886%203.92923ZM11.999%2012.5C13.6559%2012.5%2014.999%2011.1569%2014.999%209.5C14.999%207.84315%2013.6559%206.5%2011.999%206.5C10.3422%206.5%208.99902%207.84315%208.99902%209.5C8.99902%2011.1569%2010.3422%2012.5%2011.999%2012.5Z%22%20fill%3D%22black%22%2F%3E%0A%3C%2Fsvg%3E%0A";
        final String expectedSvgImage = "<svg width=\"24\" height=\"24\" viewBox=\"0 0 24 24\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\">\n" +
                "<path fill-rule=\"evenodd\" clip-rule=\"evenodd\" d=\"M5.9886 3.92923C2.66914 7.1682 2.99902 12 5.9886 15.6586L11.999 23L18.0094 15.6586C20.999 12 21.3289 7.1682 18.0094 3.92923C14.69 0.690257 9.30805 0.690257 5.9886 3.92923ZM11.999 12.5C13.6559 12.5 14.999 11.1569 14.999 9.5C14.999 7.84315 13.6559 6.5 11.999 6.5C10.3422 6.5 8.99902 7.84315 8.99902 9.5C8.99902 11.1569 10.3422 12.5 11.999 12.5Z\" fill=\"black\"/>\n" +
                "</svg>\n";
        // When
        DataUrlResource data = new DataUrlResource(imageDataUrl);

        // Then
        assertThat(data.getUrl().toExternalForm(), equalTo(imageDataUrl));
        assertThat(data.readFullyAsString(), equalTo(expectedSvgImage));

        // When
        data = DataUrlResource.builder()
                              .urlEncoded()
                              .mediaType(MediaTypes.Image.SVG)
                              .data(expectedSvgImage)
                              .build();

        // Then
        assertThat(data.getUrl().toExternalForm(), equalTo(imageDataUrl));
    }
}