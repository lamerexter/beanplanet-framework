/*
 *  MIT Licence:
 *
 *  Copyright (C) 2020 Beanplanet Ltd
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

package org.beanplanet.core.models.path;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

public class DelimitedNamePathTest {
    @Test
    public void getElements_nullPath_nullDelimiter() {
        assertThat(new DelimitedNamePath((String)null, null).getElements(), equalTo(emptyList()));
    }

    @Test
    public void getElements_emptyPath_nullDelimiter() {
        assertThat(new DelimitedNamePath("", null).getElements(), equalTo(emptyList()));
    }

    @Test
    public void getElements_nullPath_emptyDelimiter() {
        assertThat(new DelimitedNamePath(null, "").getElements(), equalTo(emptyList()));
    }

    @Test
    public void getElements_emptyPath_emptyDelimiter() {
        assertThat(new DelimitedNamePath("", "").getElements(), equalTo(emptyList()));
    }

    @Test
    public void getElements_singleDelimiterPath() {
        assertThat(new DelimitedNamePath("/", "/").getElements(), equalTo(asList("", "")));
    }

    @Test
    public void getElements_singleElementPath() {
        assertThat(new DelimitedNamePath("abc", "/").getElements(), equalTo(singletonList("abc")));
        assertThat(new DelimitedNamePath("/abc", "/").getElements(), equalTo(asList("", "abc")));
        assertThat(new DelimitedNamePath("abc/", "/").getElements(), equalTo(asList("abc", "")));
        assertThat(new DelimitedNamePath("/abc/", "/").getElements(), equalTo(asList("", "abc", "")));
    }

    @Test
    public void getElements_multipleElementPath() {
        assertThat(new DelimitedNamePath("a/b/c", "/").getElements(), equalTo(asList("a", "b", "c")));
        assertThat(new DelimitedNamePath("/a/b/c", "/").getElements(), equalTo(asList("", "a", "b", "c")));
        assertThat(new DelimitedNamePath("a/b/c/", "/").getElements(), equalTo(asList("a", "b", "c", "")));
        assertThat(new DelimitedNamePath("/a/b/c/", "/").getElements(), equalTo(asList("", "a", "b", "c", "")));
    }







    @Test
    public void parentPath_nullPath_nullDelimiter() {
        assertThat(new DelimitedNamePath((String)null, null).parentPath(), nullValue());
    }

    @Test
    public void parentPath_emptyPath_nullDelimiter() {
        assertThat(new DelimitedNamePath("", null).parentPath(), nullValue());
    }

    @Test
    public void parentPath_nullPath_emptyDelimiter() {
        assertThat(new DelimitedNamePath(null, "").parentPath(), nullValue());
    }

    @Test
    public void parentPath_emptyPath_emptyDelimiter() {
        assertThat(new DelimitedNamePath("", "").parentPath(), nullValue());
    }

    @Test
    public void parentPath_singleDelimiterPath() {
        assertThat(new DelimitedNamePath("/", "/").parentPath(), equalTo(new DelimitedNamePath("", "/")));
    }

    @Test
    public void parentPath_singleElementPath() {
        assertThat(new DelimitedNamePath("abc", "/").parentPath(), nullValue());
        assertThat(new DelimitedNamePath("/abc", "/").parentPath(), equalTo(new DelimitedNamePath("", "/")));
        assertThat(new DelimitedNamePath("abc/", "/").parentPath(), equalTo(new DelimitedNamePath("abc", "/")));
        assertThat(new DelimitedNamePath("/abc/", "/").parentPath(), equalTo(new DelimitedNamePath("/abc", "/")));
    }

    @Test
    public void parentPath_multipleElementPath() {
        assertThat(new DelimitedNamePath("a/b/c", "/").parentPath(), equalTo(new DelimitedNamePath("a/b", "/")));
        assertThat(new DelimitedNamePath("/a/b/c", "/").parentPath(), equalTo(new DelimitedNamePath("/a/b", "/")));
        assertThat(new DelimitedNamePath("a/b/c/", "/").parentPath(), equalTo(new DelimitedNamePath("a/b/c", "/")));
        assertThat(new DelimitedNamePath("/a/b/c/", "/").parentPath(), equalTo(new DelimitedNamePath("/a/b/c", "/")));
    }
}