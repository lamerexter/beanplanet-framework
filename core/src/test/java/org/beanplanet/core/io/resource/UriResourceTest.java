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

package org.beanplanet.core.io.resource;

import org.junit.Test;

import static org.beanplanet.core.models.path.PathUtil.emptyPath;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class UriResourceTest {
    @Test
    public void resolve_withEmptyPath() {
        assertThat(new UriResource("a").resolve(null), equalTo(new UriResource("a")));
        assertThat(new UriResource("a").resolve(emptyPath()), equalTo(new UriResource("a")));
    }

    @Test
    public void resolve_withAbsolutePath() {
        assertThat(new UriResource("http://hostname/a/b").resolve(new UriResource("https://otherHostname/a/b").getPath()), equalTo(new UriResource("https://otherHostname/a/b")));
    }

    @Test
    public void resolve_withRelativePath() {
        assertThat(new UriResource("http://hostname?z=1").resolve(new UriResource("c/d").getPath()), equalTo(new UriResource("http://hostname/c/d?z=1")));
        assertThat(new UriResource("http://hostname/a/b?x=1&y=2").resolve(new UriResource("c/d").getPath()), equalTo(new UriResource("http://hostname/a/c/d?x=1&y=2")));

        assertThat(new UriResource("http://hostname").resolve(new UriResource("").getPath()), equalTo(new UriResource("http://hostname")));
        assertThat(new UriResource("http://hostname/a/b").resolve(new UriResource("../c").getPath()), equalTo(new UriResource("http://hostname/a/../c")));
    }
}