package org.beanplanet.core.io.resource;

import org.junit.Test;

import static org.beanplanet.core.io.PathUtil.emptyPath;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class UriResourceTest {
    @Test
    public void resolve_withEmptyPath() throws Exception {
        assertThat(new UriResource("a").resolve(null), equalTo(new UriResource("a")));
        assertThat(new UriResource("a").resolve(emptyPath()), equalTo(new UriResource("a")));
    }

    @Test
    public void resolve_withAbsolutePath() throws Exception {
        assertThat(new UriResource("http://hostname/a/b").resolve(new UriResource("https://otherHostname/a/b").getPath()), equalTo(new UriResource("https://otherHostname/a/b")));
    }

    @Test
    public void resolve_withRelativePath() throws Exception {
        assertThat(new UriResource("http://hostname?z=1").resolve(new UriResource("c/d").getPath()), equalTo(new UriResource("http://hostname/c/d?z=1")));
        assertThat(new UriResource("http://hostname/a/b?x=1&y=2").resolve(new UriResource("c/d").getPath()), equalTo(new UriResource("http://hostname/a/c/d?x=1&y=2")));

        assertThat(new UriResource("http://hostname").resolve(new UriResource("").getPath()), equalTo(new UriResource("http://hostname")));
        assertThat(new UriResource("http://hostname/a/b").resolve(new UriResource("../c").getPath()), equalTo(new UriResource("http://hostname/a/../c")));
    }

}