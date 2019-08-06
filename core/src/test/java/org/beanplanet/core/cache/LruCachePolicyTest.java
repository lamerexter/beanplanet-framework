/*
 *  MIT Licence:
 *
 *  Copyright (C) 2019 Beanplanet Ltd
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