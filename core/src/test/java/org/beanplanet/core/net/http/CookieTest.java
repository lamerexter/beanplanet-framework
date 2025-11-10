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

package org.beanplanet.core.net.http;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class CookieTest {
    @Test
    public void fromHttpHeaderValue_null() {
        assertThat(Cookie.fromHttpHeaderValue(null), nullValue());
    }

    @Test
    public void fromHttpHeaderValue_parseNameValueAndAttributes() {
        final String unQuotedAttrValues = "theName=theValue; Comment=theComment; Domain=.com.eu; Max-Age=9999; Path=/acme; Secure=true; SameSite=Strict; Version=123";
        assertThat(Cookie.fromHttpHeaderValue(unQuotedAttrValues).getName(), equalTo("theName"));
        assertThat(Cookie.fromHttpHeaderValue(unQuotedAttrValues).getValue(), equalTo("theValue"));
        assertThat(Cookie.fromHttpHeaderValue(unQuotedAttrValues).getComment(), equalTo("theComment"));
        assertThat(Cookie.fromHttpHeaderValue(unQuotedAttrValues).getDomain(), equalTo(".com.eu"));
        assertThat(Cookie.fromHttpHeaderValue(unQuotedAttrValues).getMaxAge(), equalTo(9999));
        assertThat(Cookie.fromHttpHeaderValue(unQuotedAttrValues).getPath(), equalTo("/acme"));
        assertThat(Cookie.fromHttpHeaderValue(unQuotedAttrValues).isSecure(), equalTo(true));
        assertThat(Cookie.fromHttpHeaderValue(unQuotedAttrValues).getSameSite(), equalTo(Cookie.SameSite.Strict));
        assertThat(Cookie.fromHttpHeaderValue(unQuotedAttrValues).getVersion(), equalTo(123));

        final String quotedAttrValues = "theName=\"theValue\"; Comment=\"The comment with spaces\"; Domain=\".com.eu\"; Max-Age=\"9999\"; Path=\"/acme\"; Secure=\"true\"; SameSite=\"Strict\"; Version=\"123\"";
        assertThat(Cookie.fromHttpHeaderValue(quotedAttrValues).getName(), equalTo("theName"));
        assertThat(Cookie.fromHttpHeaderValue(quotedAttrValues).getValue(), equalTo("theValue"));
        assertThat(Cookie.fromHttpHeaderValue(quotedAttrValues).getComment(), equalTo("The comment with spaces"));
        assertThat(Cookie.fromHttpHeaderValue(quotedAttrValues).getDomain(), equalTo(".com.eu"));
        assertThat(Cookie.fromHttpHeaderValue(quotedAttrValues).getMaxAge(), equalTo(9999));
        assertThat(Cookie.fromHttpHeaderValue(quotedAttrValues).getPath(), equalTo("/acme"));
        assertThat(Cookie.fromHttpHeaderValue(quotedAttrValues).isSecure(), equalTo(true));
        assertThat(Cookie.fromHttpHeaderValue(quotedAttrValues).getSameSite(), equalTo(Cookie.SameSite.Strict));
        assertThat(Cookie.fromHttpHeaderValue(quotedAttrValues).getVersion(), equalTo(123));

        final String quotedWithWhitespaceInValueAttrValues = "theName=\"theValue word2\"; Comment=\"The comment with spaces\"; Domain=\".com.eu\"; Max-Age=\"9999\"; Path=\"/acme\"; Secure=\"true\"; SameSite=\"Strict\"; Version=\"123\"";
        assertThat(Cookie.fromHttpHeaderValue(quotedWithWhitespaceInValueAttrValues).getName(), equalTo("theName"));
        assertThat(Cookie.fromHttpHeaderValue(quotedWithWhitespaceInValueAttrValues).getValue(), equalTo("theValue word2"));
        assertThat(Cookie.fromHttpHeaderValue(quotedWithWhitespaceInValueAttrValues).getComment(), equalTo("The comment with spaces"));
        assertThat(Cookie.fromHttpHeaderValue(quotedWithWhitespaceInValueAttrValues).getDomain(), equalTo(".com.eu"));
        assertThat(Cookie.fromHttpHeaderValue(quotedWithWhitespaceInValueAttrValues).getMaxAge(), equalTo(9999));
        assertThat(Cookie.fromHttpHeaderValue(quotedWithWhitespaceInValueAttrValues).getPath(), equalTo("/acme"));
        assertThat(Cookie.fromHttpHeaderValue(quotedWithWhitespaceInValueAttrValues).isSecure(), equalTo(true));
        assertThat(Cookie.fromHttpHeaderValue(quotedWithWhitespaceInValueAttrValues).getSameSite(), equalTo(Cookie.SameSite.Strict));
        assertThat(Cookie.fromHttpHeaderValue(quotedWithWhitespaceInValueAttrValues).getVersion(), equalTo(123));
    }

    @Test
    public void fromHttpHeaderValue_toHttpRequestHeaderValue() {
        final String unQuotedAttrValues = "theName=theValue; Comment=theComment; Domain=.com.eu; Max-Age=9999; Path=/acme; Secure=true; SameSite=Strict; Version=123";
        assertThat(Cookie.fromHttpHeaderValue(unQuotedAttrValues).toHttpRequestHeaderValue(), equalTo(unQuotedAttrValues));

        final String quotedAttrValues = "theName=\"theValue\"; Comment=\"The comment with spaces\"; Domain=\".com.eu\"; Max-Age=\"9999\"; Path=\"/acme\"; Secure=\"true\"; SameSite=\"Strict\"; Version=\"123\"";
        assertThat(Cookie.fromHttpHeaderValue(quotedAttrValues).toHttpRequestHeaderValue(), equalTo("theName=theValue; Comment=\"The comment with spaces\"; Domain=.com.eu; Max-Age=9999; Path=/acme; Secure=true; SameSite=Strict; Version=123"));

        final String quotedWithWhiteSpaceValueAttrValues = "theName=\"theValue word2\"; Comment=\"The comment with spaces\"; Domain=\".com.eu\"; Max-Age=\"9999\"; Path=\"/acme\"; Secure=\"true\"; SameSite=\"Strict\"; Version=\"123\"";
        assertThat(Cookie.fromHttpHeaderValue(quotedWithWhiteSpaceValueAttrValues).toHttpRequestHeaderValue(), equalTo("theName=\"theValue word2\"; Comment=\"The comment with spaces\"; Domain=.com.eu; Max-Age=9999; Path=/acme; Secure=true; SameSite=Strict; Version=123"));
    }

    @Test
    public void fromHttpHeaderValue_toHttpResponseHeaderValue() {
        final String unQuotedAttrValues = "theName=theValue; Comment=theComment; Domain=.com.eu; Max-Age=9999; Path=/acme; Secure=true; SameSite=Strict; Version=123";
        assertThat(Cookie.fromHttpHeaderValue(unQuotedAttrValues).toHttpResponseHeaderValue(), equalTo("theName=theValue"));

        final String quotedAttrValues = "theName=\"theValue\"; Comment=\"The comment with spaces\"; Domain=\".com.eu\"; Max-Age=\"9999\"; Path=\"/acme\"; Secure=\"true\"; SameSite=\"Strict\"; Version=\"123\"";
        assertThat(Cookie.fromHttpHeaderValue(quotedAttrValues).toHttpResponseHeaderValue(), equalTo("theName=theValue"));

        final String quotedWithWhiteSpaceValueAttrValues = "theName=\"theValue word2\"; Comment=\"The comment with spaces\"; Domain=\".com.eu\"; Max-Age=\"9999\"; Path=\"/acme\"; Secure=\"true\"; SameSite=\"Strict\"; Version=\"123\"";
        assertThat(Cookie.fromHttpHeaderValue(quotedWithWhiteSpaceValueAttrValues).toHttpResponseHeaderValue(), equalTo("theName=\"theValue word2\""));
    }
}