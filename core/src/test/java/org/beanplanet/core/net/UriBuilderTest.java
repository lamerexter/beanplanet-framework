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

package org.beanplanet.core.net;

import org.beanplanet.core.util.MultiValueListMapImpl;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.net.URI;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class UriBuilderTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void noArgConstructor() {
        // When
        UriBuilder builder = new UriBuilder();

        // Then
        assertThat(builder.scheme(), nullValue());
        assertThat(builder.userInfo(), nullValue());
        assertThat(builder.host(), nullValue());
        assertThat(builder.port(), nullValue());
        assertThat(builder.path(), nullValue());
        assertThat(builder.queryParameters(), nullValue());
        assertThat(builder.fragment(), nullValue());
    }

    @Test
    public void uriConstructor() {
        // When
        URI uri = URI.create("https://theUser@theHost:1234?a=1&b=2&c=3&b=22&b=222#theFragment");
        UriBuilder builder = new UriBuilder(uri);

        // Then
        assertThat(builder.scheme(), equalTo(uri.getScheme()));
        assertThat(builder.userInfo(), equalTo(uri.getUserInfo()));
        assertThat(builder.host(), equalTo(uri.getHost()));
        assertThat(builder.port(), equalTo(uri.getPort()));
        assertThat(builder.path(), equalTo(uri.getPath()));

        MultiValueListMapImpl<String, String> expectedQueryParams = new MultiValueListMapImpl<>();
        expectedQueryParams.addValue("a", "1");
        expectedQueryParams.addValue("b", "2");
        expectedQueryParams.addValue("b", "22");
        expectedQueryParams.addValue("b", "222");
        expectedQueryParams.addValue("c", "3");
        assertThat(builder.queryParameters(), equalTo(expectedQueryParams));
        assertThat(builder.fragment(), equalTo(uri.getFragment()));
    }

    @Test
    public void toUri() {
        // When
        URI originalUri = URI.create("https://theUser@theHost:1234?a=1&b=2&b=22&b=222&c=3#theFragment");
        UriBuilder builder = new UriBuilder(originalUri);

        // Then
        assertThat(builder.toUri(), equalTo(originalUri));
    }

    @Test
    public void toUriThrowsAnErrorForAnIncompleteUri() {
        // Expect
        exception.expect(NetworkException.class);
        exception.expectMessage("incomplete or invalid URI");

        // When
        UriBuilder builder = new UriBuilder();
        builder.scheme("https");

        // Then
        builder.toUri();
    }
}