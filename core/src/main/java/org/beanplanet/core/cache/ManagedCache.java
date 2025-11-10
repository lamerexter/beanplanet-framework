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

import org.beanplanet.core.events.EventSupport;
import org.beanplanet.core.util.PropertyBasedToStringBuilder;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class ManagedCache<K, V> implements Cache<K, V> {
    private ConcurrentHashMap<K, V> backingMap;

    private EventSupport eventSupport = new EventSupport();

    public ManagedCache() {
        this(new ConcurrentHashMap<>());
    }

    public ManagedCache(ConcurrentHashMap<K, V> backingMap) {
        this.backingMap = backingMap;
    }

    @Override
    public int size() {
        return backingMap.size();
    }

    @Override
    public boolean isEmpty() {
        return backingMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return backingMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return backingMap.containsValue(value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public V get(Object key) {
        V found = backingMap.get(key);
        eventSupport.dispatchEvent(found == null ? new CacheMissEvent<>((K)key) :  new CacheHitEvent<>((K)key, found));
        return found;
    }

    @Override
    public V put(K key, V value) {
        V previousValue = backingMap.put(key, value);
        eventSupport.dispatchEvent(
                new CacheItemsAddedEvent<>(previousValue == null ? emptyList() : singletonList(new ManagedCacheEntry<>(key, previousValue)),
                                           singletonList(new ManagedCacheEntry<>(key, value)))
        );

        return previousValue;
    }

    @Override
    public V remove(Object key) {
        V valueRemoved = backingMap.remove(key);
        if (valueRemoved != null) {
            eventSupport.dispatchEvent(new CacheItemsRemovedEvent<>(Collections.singletonList(
                    new ManagedCacheEntry<>(key, valueRemoved)
            )));
        }

        return valueRemoved;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        if (m == null) return;

        List<Cache.Entry<? extends K, ? extends V>> entriesEvicted = new ArrayList<>();
        List<Cache.Entry<? extends K, ? extends V>> entriesAdded = m.entrySet()
                                .stream().map(e -> new ManagedCacheEntry<>(e.getKey(), e.getValue()))
                                .collect(Collectors.toList());
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            V previousValue = backingMap.put(entry.getKey(), entry.getValue());
            if (previousValue != null) {
                entriesEvicted.add(new ManagedCacheEntry<>(entry.getKey(), previousValue));
            }
        }

        eventSupport.dispatchEvent(new CacheItemsAddedEvent<>(entriesEvicted, entriesAdded));
    }

    @Override
    public void clear() {
        backingMap.clear();
        eventSupport.dispatchEvent(new CacheClearedEvent<>());
    }

    @Override
    public Set<K> keySet() {
        return backingMap.keySet();
    }

    @Override
    public Collection<V> values() {
        return backingMap.values();
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return backingMap.entrySet();
    }

    @Override
    public boolean addCacheListener(CacheListener listener) {
        return eventSupport.addListener(CacheEvent.class, listener);
    }

    @Override
    public boolean removeCacheListener(CacheListener listener) {
        return eventSupport.removeListener(CacheEvent.class, listener);
    }

    /**
     * Adds a caching policy to the cache.
     *
     * @param policy the cache policy to be added, which may not be null.
     * @return true if the policy was added, false otherwise.
     */
    public boolean addCachePolicy(CachePolicy<K, V> policy) {
        return addCacheListener(policy);
    }

    /**
     * Removes a caching policy from the cache.
     *
     * @param policy the cache policy to be removed, which may not be null.
     * @return true if the policy was removed, false otherwise.
     */
    public boolean removeCachePolicy(CachePolicy<K, V> policy) {
        return removeCacheListener(policy);
    }


    public static class ManagedCacheEntry<K, V> implements Cache.Entry<K, V> {
        private K key;
        private V value;

        public ManagedCacheEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) return true;
            if (other.getClass() != ManagedCacheEntry.class) return false;

            ManagedCacheEntry<?, ?> that = (ManagedCacheEntry<?, ?>)other;
            return Objects.equals(this.getKey(), that.getKey())
                    && Objects.equals(this.getValue(), that.getValue());
        }

        @Override
        public String toString() {
            return new PropertyBasedToStringBuilder(this).build();
        }
    }
}
