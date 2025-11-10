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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class MultiValueListMapImpl<K, V> extends MultiValueCollectionMapImpl<K, V, List<V>> implements MultiValueListMap<K, V> {
    private static final MultiValueListMapImpl<?, ?> _EMPTY = new MultiValueListMapImpl<>();

    public MultiValueListMapImpl() {
        super();
    }

    @SuppressWarnings("unchecked, rawtypes")
    public static <K, V> MultiValueListMapImpl<K, V> fromMappedArrayValues(Map<K, V[]> from) {
        return new MultiValueListMapImpl<>((Map) from.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> asList(e.getValue()))));
    }

    public MultiValueListMapImpl(Map<K, List<V>> backingMap) {
        super(backingMap, ArrayList::new);
    }

    public MultiValueListMapImpl(MultiValueListMap<K, V> other) {
        super(other.entrySet().stream().collect(Collectors.toMap(Entry::getKey, e -> new ArrayList<>(e.getValue()))));
    }

    @SuppressWarnings("unchecked")
    public <C extends List<V>> MultiValueListMapImpl(Map<K, C> backingMap, Factory<C> listFactory) {
        super((Map<K, List<V>>)backingMap, listFactory);
    }

    @SuppressWarnings("unchecked")
    public static <KK, VV> MultiValueListMap<KK, VV> empty() {
        return (MultiValueListMap<KK, VV>) _EMPTY;
    }

    public static <KK, VV> MultiValueListMapImplBuilder<KK, VV> builder() {
        return new MultiValueListMapImplBuilder<>();
    }

    public static class MultiValueListMapImplBuilder<K, V> implements MultiValueListMapBuilder<K, V> {
        private MultiValueListMapImpl<K, V> map = new MultiValueListMapImpl<>();

        @Override
        public <KK extends K, VV extends V> MultiValueListMapBuilder<K, V> add(KK key, VV values) {
            map.addValue(key, values);
            return this;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <KK extends K, VV extends V> MultiValueListMapBuilder<K, V> setEntries(KK key, List<VV> values) {
            map.put(key, (List<V>)values);
            return this;
        }

        @Override
        public MultiValueListMap<K, V> build() {
            return map;
        }
    }
}
