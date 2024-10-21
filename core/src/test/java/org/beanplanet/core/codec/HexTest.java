package org.beanplanet.core.codec;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class HexTest {
    @Test
    public void encodeToHexString() {
        assertThat(Hex.encodeToHexString(null), nullValue());

        assertThat(Hex.encodeToHexString(new byte[0]), equalTo(""));

        assertThat(Hex.encodeToHexString("Hello World".getBytes()), equalToIgnoringCase("48656c6c6f20576f726c64"));
    }

    @Test
    public void decodeFromHexString() {
        assertThat(Hex.decodeFromHexString(null), nullValue());

        assertThat(new String(Hex.decodeFromHexString("48656c6c6f20576f726c64")), equalToIgnoringCase("Hello World"));
        assertThat(new String(Hex.decodeFromHexString("48656c6c6f20576f726c64".toUpperCase())), equalToIgnoringCase("Hello World"));
        assertThat(new String(Hex.decodeFromHexString("48656C6c6f20576F726c64")), equalToIgnoringCase("Hello World"));
    }
}