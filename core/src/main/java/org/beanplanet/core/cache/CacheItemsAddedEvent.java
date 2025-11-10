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

import java.util.List;

public class CacheItemsAddedEvent<K, V> extends CacheEvent<K, V> {
    private List<Cache.Entry<? extends K, ? extends V>> enriesEvicted;

    public CacheItemsAddedEvent(List<Cache.Entry<? extends K, ? extends V>> entriesEvicted,
                                List<Cache.Entry<? extends K, ? extends V>> entriesAdded) {
        super(entriesAdded);
        this.enriesEvicted = entriesEvicted;
    }

    public List<Cache.Entry<? extends K, ? extends V>> getEntriesEvicted() {
        return enriesEvicted;
    }

    public List<Cache.Entry<? extends K, ? extends V>> getEntriesAdded() {
       return getEntriesAffected();
    }
}
