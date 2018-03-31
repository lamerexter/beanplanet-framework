/*
 *  MIT Licence:
 *
 *  Copyright (C) 2018 Beanplanet Ltd
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

package org.beanplanet.core.util;

import org.beanplanet.core.models.Factory;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractMultiValueMap<K, V, C extends Collection<V>> implements MultiValueMap<K, V, C>{
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

    @Override
    public boolean addValue(K key, V value) {
        return backingMap.computeIfAbsent(key, v -> factory.create()).add(value);
    }

    @Override
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
