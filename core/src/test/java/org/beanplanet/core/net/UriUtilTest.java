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

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class UriUtilTest {
    @Test
    public void mergePaths_null_null() {
        assertThat(UriUtil.mergePaths(null, null), equalTo(null));
    }

    @Test
    public void mergePaths_empty_null() {
        assertThat(UriUtil.mergePaths("", null), equalTo(""));
    }

    @Test
    public void mergePaths_null_empty() {
        assertThat(UriUtil.mergePaths(null, ""), equalTo(""));
    }

    @Test
    public void mergePaths_empty_empty() {
        assertThat(UriUtil.mergePaths("", ""), equalTo(""));
    }

    @Test
    public void mergePaths_root_null() {
        assertThat(UriUtil.mergePaths("/", null), equalTo("/"));
    }

    @Test
    public void mergePaths_root_empty() {
        assertThat(UriUtil.mergePaths("/", ""), equalTo("/"));
    }

    @Test
    public void mergePaths_null_root() {
        assertThat(UriUtil.mergePaths(null, "/"), equalTo("/"));
    }

    @Test
    public void mergePaths_root_root() {
        assertThat(UriUtil.mergePaths("/", "/"), equalTo("/"));
    }

    @Test
    public void mergePaths_absPath_null() {
        assertThat(UriUtil.mergePaths("/a", null), equalTo("/a"));
    }

    @Test
    public void mergePaths_absPath_empty() {
        assertThat(UriUtil.mergePaths("/a", ""), equalTo("/a"));
    }

    @Test
    public void mergePaths_absPath_relPath() {
        assertThat(UriUtil.mergePaths("/a", "b"), equalTo("/a/b"));
        assertThat(UriUtil.mergePaths("/a", "b/c"), equalTo("/a/b/c"));
    }

    @Test
    public void mergePaths_absPath_absPath() {
        assertThat(UriUtil.mergePaths("/a", "/"), equalTo("/"));
        assertThat(UriUtil.mergePaths("/a", "/b"), equalTo("/b"));
        assertThat(UriUtil.mergePaths("/a", "/b/c"), equalTo("/b/c"));
    }
}