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