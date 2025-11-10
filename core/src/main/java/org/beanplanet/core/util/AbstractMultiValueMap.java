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

package org.beanplanet.core.util;

import org.beanplanet.core.models.Factory;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractMultiValueMap<K, V, C extends Collection<V>> implements MultiValueMap<K, V, C> {
    private Map<K, C> backingMap;
    private Factory<C> factory;

    public AbstractMultiValueMap(Map<K, C> backingMap, Factory<C> factory) {
        this.backingMap = backingMap;
        this.factory = factory;
    }

    public int size() {
        return backingMap.size();
    }

    public boolean isEmpty() {
        return backingMap.isEmpty();
    }

    public boolean containsKey(Object key) {
        return backingMap.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return backingMap.containsValue(value);
    }

    public C get(Object key) {
        return backingMap.get(key);
    }

    public C put(K key, C value) {
        return backingMap.put(key, value);
    }

    public C remove(Object key) {
        return backingMap.remove(key);
    }

    public void putAll(Map<? extends K, ? extends C> m) {
        backingMap.putAll(m);
    }

    public void clear() {
        backingMap.clear();
    }

    public Set<K> keySet() {
        return backingMap.keySet();
    }

    public Collection<C> values() {
        return backingMap.values();
    }

    public Set<Map.Entry<K, C>> entrySet() {
        return backingMap.entrySet();
    }

    public boolean addValue(K key, V value) {
        return backingMap.computeIfAbsent(key, v -> factory.create()).add(value);
    }

    public boolean removeValue(K key, V value) {
        final boolean valueRemoved[] = { false };
        backingMap.computeIfPresent(key, (k, lov) -> { valueRemoved[0] = lov.remove(value); return lov; });
        return valueRemoved[0];
    }

    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        return Objects.equals(backingMap, ((AbstractMultiValueMap)other).backingMap);
    }

    public int hashCode() {
        return Objects.hash(backingMap);
    }

    public String toString() {
        return backingMap.toString();
    }
}
