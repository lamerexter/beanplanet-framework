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

package org.beanplanet.core.cache;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@Ignore
public class LruCachePolicyTest {
    private ManagedCache<String, String> cache;
    private LruCachePolicy<String, String> policy;

    @Before
    public void setUp() {
        cache = new ManagedCache<>();
        policy = new LruCachePolicy<>();
        cache.addCacheListener(policy);
    }

    @Test
    public void put_appliedWhenCaceAtLruCacheSize() {
        // Given
        policy.setLruCacheSize(3);

        // When the cache size <= LRU cache size
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");

        // Then the cache operates normally
        assertThat(cache.size(), equalTo(3));
        assertThat(cache.containsKey("key1"), is(true));
        assertThat(cache.containsKey("key2"), is(true));
        assertThat(cache.containsKey("key3"), is(true));

        // When the cache is set to grow beyond the LRU cache size
        cache.put("key4", "value4");

        // Then the cache policy accepts the new item and evicts the LRU item
        assertThat(cache.size(), equalTo(3));
        assertThat(cache.containsKey("key2"), is(true));
        assertThat(cache.containsKey("key3"), is(true));
        assertThat(cache.containsKey("key4"), is(true));
    }

}