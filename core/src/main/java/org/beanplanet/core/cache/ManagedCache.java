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
