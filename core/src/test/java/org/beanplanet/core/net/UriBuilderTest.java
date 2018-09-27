/*
 *  MIT Licence:
 *
 *  Copyright (C) 2018 Beanplanet Ltd
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without restriction
 *  including without limitation the rights to use, copy, modify, merge,
 *  publish, distribute, sublicense, and/or sell copies of the Software,
 *  and to permit persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 *  KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 *  PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 *  CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  DEALINGS IN THE SOFTWARE.
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
        assertThat(builder.getScheme(), nullValue());
        assertThat(builder.getUserInfo(), nullValue());
        assertThat(builder.getHost(), nullValue());
        assertThat(builder.getPort(), nullValue());
        assertThat(builder.getPath(), nullValue());
        assertThat(builder.getQueryParameters(), nullValue());
        assertThat(builder.getFragment(), nullValue());
    }

    @Test
    public void uriConstructor() {
        // When
        URI uri = URI.create("https://theUser@theHost:1234?a=1&b=2&c=3&b=22&b=222#theFragment");
        UriBuilder builder = new UriBuilder(uri);

        // Then
        assertThat(builder.getScheme(), equalTo(uri.getScheme()));
        assertThat(builder.getUserInfo(), equalTo(uri.getUserInfo()));
        assertThat(builder.getHost(), equalTo(uri.getHost()));
        assertThat(builder.getPort(), equalTo(uri.getPort()));
        assertThat(builder.getPath(), equalTo(uri.getPath()));

        MultiValueListMapImpl<String, String> expectedQueryParams = new MultiValueListMapImpl<>();
        expectedQueryParams.addValue("a", "1");
        expectedQueryParams.addValue("b", "2");
        expectedQueryParams.addValue("b", "22");
        expectedQueryParams.addValue("b", "222");
        expectedQueryParams.addValue("c", "3");
        assertThat(builder.getQueryParameters(), equalTo(expectedQueryParams));
        assertThat(builder.getFragment(), equalTo(uri.getFragment()));
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
        builder.setScheme("https");

        // Then
        builder.toUri();
    }
}