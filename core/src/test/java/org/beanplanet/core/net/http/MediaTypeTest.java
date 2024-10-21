package org.beanplanet.core.net.http;

import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class MediaTypeTest {
    @Test
    public void givenAFullSpecMediaType_whenConstructed_theElementsAreParsedCorrectly() {
        assertThat(new MediaType("type/subtype; charset=utf-8").getType(), equalTo("type"));
        assertThat(new MediaType("type/subtype; charset=utf-8").getSubtype(), equalTo("subtype"));
        assertThat(new MediaType("type/subtype; charset=utf-8").getParameters(), equalTo(new Parameters(Map.of("charset", "utf-8"))));
        assertThat(new MediaType("type/subtype; Charset=\"utf-8\"").getParameters(), equalTo(new Parameters(Map.of("Charset", "utf-8"))));
        assertThat(new MediaType("type/subtype; Charset=\"utf-8\"").getParameters().get("charSET").orElse(null), equalTo("utf-8"));
    }
}