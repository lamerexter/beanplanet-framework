/*
 * Copyright (c) 2001-present the original author or authors (see NOTICE herein).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.beanplanet.core.io;

import org.beanplanet.core.io.resource.ByteArrayResource;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

import static org.beanplanet.core.io.DigestUtil.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Hexadecimal digests keep every byte as two digits, leading zero bytes included: a SHA-256 digest is always 64
 * characters, an SHA-1 40 and an MD5 32.
 */
public class DigestUtilHexadecimalTest {

    @Test
    public void hashToHexadecimalString_keepsLeadingZeroBytes() {
        assertThat(hashToHexadecimalString(new byte[] {0x00, 0x01}), equalTo("0001"));
        assertThat(hashToHexadecimalString(new byte[] {0x00, 0x00, 0x01}), equalTo("000001"));
        assertThat(hashToHexadecimalString(new byte[] {0x00}), equalTo("00"));
        assertThat(hashToHexadecimalString(new byte[32]), equalTo("0".repeat(64)));
    }

    @Test
    public void hashToHexadecimalString_keepsLeadingZeroNibbleAndInnerZeroBytes() {
        assertThat(hashToHexadecimalString(new byte[] {0x0a, 0x01}), equalTo("0a01"));
        assertThat(hashToHexadecimalString(new byte[] {0x01, 0x00, 0x02}), equalTo("010002"));
        assertThat(hashToHexadecimalString(new byte[] {(byte) 0xff, 0x00}), equalTo("ff00"));
    }

    @Test
    public void hashToHexadecimalString_emptyHash() {
        assertThat(hashToHexadecimalString(new byte[0]), equalTo(""));
    }

    @Test
    public void sha256_digestStartingWithZeroByte() throws Exception {
        // SHA-256("x84") = 00 9d 9e 88 ...: the leading 00 byte was dropped, giving 62 characters
        String expected = jdkHex("SHA-256", "x84");
        assertThat(expected.substring(0, 2), equalTo("00"));

        assertThat(sha256HashByteStreamToHexadecimal("x84"), equalTo(expected));
        assertThat(sha256HashByteStreamToHexadecimal(new ByteArrayResource("x84".getBytes(StandardCharsets.UTF_8))), equalTo(expected));
        assertThat(sha256HashByteStreamToHexadecimal("x84").length(), equalTo(64));
    }

    @Test
    public void digests_matchTheJdkAndAreFixedLength() throws Exception {
        // About 1 input in 256 has a digest starting with a 00 byte: enough inputs that every algorithm meets several.
        for (int i = 0; i < 3_000; i++) {
            String input = "event-" + i;
            assertThat(input, sha256HashByteStreamToHexadecimal(input), equalTo(jdkHex("SHA-256", input)));
            assertThat(input, sha1HashByteStreamToHexadecimal(input), equalTo(jdkHex("SHA-1", input)));
            assertThat(input, md5HashByteStreamToHexadecimal(input), equalTo(jdkHex("MD5", input)));
            assertThat(input, sha256HashByteStreamToHexadecimal(input).length(), equalTo(64));
            assertThat(input, sha1HashByteStreamToHexadecimal(input).length(), equalTo(40));
            assertThat(input, md5HashByteStreamToHexadecimal(input).length(), equalTo(32));
        }
    }

    private static String jdkHex(String algorithm, String input) throws Exception {
        return HexFormat.of().formatHex(MessageDigest.getInstance(algorithm).digest(input.getBytes(StandardCharsets.UTF_8)));
    }
}
