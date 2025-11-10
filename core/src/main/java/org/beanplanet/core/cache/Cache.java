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

import java.util.Map;

/**
 * Definition of a cache.
 */
public interface Cache<K, V> extends Map<K, V>, CacheEventSource {
    interface Entry<K, V> {
        public K getKey();
        public V getValue();
    }

    /**
     * Adds a caching policy to the cache.
     *
     * @param policy the cache policy to be added, which may not be null.
     * @return true if the policy was added, false otherwise.
     */
    boolean addCachePolicy(CachePolicy<K, V> policy);

    /**
     * Removes a caching policy from the cache.
     *
     * @param policy the cache policy to be removed, which may not be null.
     * @return true if the policy was removed, false otherwise.
     */
    boolean removeCachePolicy(CachePolicy<K, V> policy);
}
