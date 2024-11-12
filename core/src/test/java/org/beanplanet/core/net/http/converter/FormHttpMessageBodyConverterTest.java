package org.beanplanet.core.net.http.converter;

import org.beanplanet.core.io.resource.StringResource;
import org.beanplanet.core.net.http.HttpHeaders;
import org.beanplanet.core.net.http.MediaTypes;
import org.beanplanet.core.util.MultiValueCollectionMap;
import org.beanplanet.core.util.MultiValueListMap;
import org.beanplanet.core.util.MultiValueMap;
import org.beanplanet.core.util.MultiValueSetMap;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;

public class FormHttpMessageBodyConverterTest {
    private FormHttpMessageBodyConverter<List<Object>> converter = new FormHttpMessageBodyConverter<>();

    @Test
    public void givenTypes_whenTestedForSupport_thenTheFormMessageConverterReturnsTheCorrectAnswer() {
        assertThat(converter.supports(String.class), is(false));
        assertThat(converter.supports(MultiValueMap.class), is(true));
        assertThat(converter.supports(MultiValueCollectionMap.class), is(true));
        assertThat(converter.supports(MultiValueListMap.class), is(true));
        assertThat(converter.supports(MultiValueSetMap.class), is(true));
    }

    @Test
    public void givenMediaTypes_whenTestedForWriteSupport_thenTheFormMessageConverterReturnsTheCorrectAnswer() {
        assertThat(converter.canConvertTo(String.class, MediaTypes.Image.SVG), is(false));
        assertThat(converter.canConvertTo(String.class, MediaTypes.Application.FORM_URLENCODED), is(false));
        assertThat(converter.canConvertTo(String.class, MediaTypes.Multipart.FORM_DATA), is(false));

        assertThat(converter.canConvertTo(MultiValueMap.class, MediaTypes.Multipart.FORM_DATA), is(true));
        assertThat(converter.canConvertTo(MultiValueMap.class, MediaTypes.allForType(MediaTypes.Multipart.FORM_DATA.getType())), is(true));
        assertThat(converter.canConvertTo(MultiValueMap.class, MediaTypes.ALL), is(true));
    }

    @SuppressWarnings("unchecked,rawtypes")
    @Test
    public void givenAFormWithSimpleValues_whenConverted_thenTheFormIsSuccessfullyConverted_andWritten() {
        // Given
        MultiValueListMap<String, Object> map = MultiValueListMap
                .<String, Object>builder()
                .add("field1", "field1Value")
                .setEntries("field2", List.of("field2Value1", "field2Value2"))
                .add("field3", "field3Value")
                .build();
        HttpHeaders headers = HttpHeaders.builder().build();

        // When
        String resultBody = converter.convertTo(map, headers).readFullyAsString();

        // Then
        assertThat(resultBody, startsWith("--"));

        assertThat(resultBody, containsString("Content-Type: text/plain\r\n" +
                "Content-Disposition: form-data; name=\"field1\"\r\n" +
                "\r\n" +
                "field1Value\r\n"));

        assertThat(resultBody, containsString("Content-Type: text/plain\r\n" +
                "Content-Disposition: form-data; name=\"field2\"\r\n" +
                "\r\n" +
                "field2Value1\r\n"));

        assertThat(resultBody, containsString("Content-Type: text/plain\r\n" +
                "Content-Disposition: form-data; name=\"field2\"\r\n" +
                "\r\n" +
                "field2Value2\r\n"));

        assertThat(resultBody, containsString("Content-Type: text/plain\r\n" +
                "Content-Disposition: form-data; name=\"field3\"\r\n" +
                "\r\n" +
                "field3Value\r\n"));
    }

    @Test
    public void givenAFormWithResourceValues_whenConverted_thenTheFormIsSuccessfullyConverted_andWritten() {
        // Given
        MultiValueMap<String, Object, List<Object>> map = MultiValueListMap.
                <String, Object>builder()
                .add("field1", "field1Value")
                .add("resourceField", new StringResource("Hello World!") {
                    public String getName() {
                        return "filename.html";
                    }
                })
                .build();
        HttpHeaders headers = HttpHeaders.builder().build();

        // When
        String resultBody = converter.convertTo(map, headers).readFullyAsString();

        // Then
        assertThat(resultBody, startsWith("--"));

        assertThat(resultBody, containsString("Content-Type: text/plain\r\n" +
                "Content-Disposition: form-data; name=\"field1\"\r\n" +
                "\r\n" +
                "field1Value\r\n"));

        assertThat(resultBody, containsString("Content-Type: text/html\r\n" +
                "Content-Disposition: form-data; name=\"resourceField\"; filename=\"filename.html\"\r\n" +
                "\r\n" +
                "Hello World!"));
    }

    @Test
    public void givenAFormWithResourceValues_andCharset_whenConverted_thenTheFormIsSuccessfullyConverted_andWritten() {
        // Given
        MultiValueMap<String, Object, List<Object>> map = MultiValueListMap.
                <String, Object>builder()
                .add("field1", "field1Value")
                .add("resourceField", new StringResource("Hello World!") {
                    public String getName() {
                        return "filename.html";
                    }
                })
                .build();
        HttpHeaders headers = HttpHeaders.builder()
                .contentType(MediaTypes.Multipart.FORM_DATA, StandardCharsets.UTF_8)
                                         .build();

        // When
        String resultBody = converter.convertTo(map, headers).readFullyAsString();

        // Then
        assertThat(resultBody, startsWith("--"));

        assertThat(resultBody, containsString("Content-Type: text/plain\r\n" +
                "Content-Disposition: form-data; name=\"field1\"\r\n" +
                "\r\n" +
                "field1Value\r\n"));

        assertThat(resultBody, containsString("Content-Type: text/html\r\n" +
                "Content-Disposition: form-data; name=\"resourceField\"; filename=\"=?UTF-8?Q?filename.html?=\"; filename*=UTF-8''filename.html\r\n" +
                "\r\n" +
                "Hello World!"));
    }
}