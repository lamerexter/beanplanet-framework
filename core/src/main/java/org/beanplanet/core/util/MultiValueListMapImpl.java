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
