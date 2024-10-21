package org.beanplanet.core.util;

import org.junit.Test;

import java.util.Collections;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class IterableUtilTest {
    @Test
    public void toList_null() {
        assertThat(IterableUtil.toList(null), nullValue());
    }

    @Test
    public void toList_empty() {
        assertThat(IterableUtil.toList(Collections.emptyList()), equalTo(Collections.emptyList()));
    }

    @Test
    public void toList() {
        assertThat(IterableUtil.toList(asList("a", "b", "c")), equalTo(asList("a", "b", "c")));
    }
}