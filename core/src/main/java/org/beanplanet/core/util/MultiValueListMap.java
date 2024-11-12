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

import java.util.List;

public interface MultiValueListMap<K, V> extends MultiValueCollectionMap<K, V, List<V>> {
    static <KK, VV> MultiValueListMap<KK, VV> empty() {
        return MultiValueListMapImpl.empty();
    }

    static <KK, VV> MultiValueListMapBuilder<KK, VV> builder() {
        return MultiValueListMapImpl.builder();
    }

    interface MultiValueListMapBuilder<K, V> {
        <KK extends K, VV extends V> MultiValueListMapBuilder<K, V> add(KK key, VV value);

        default <KK extends K, VV extends V> MultiValueListMapBuilder<K, V> addAll(KK key, List<VV> values) {
            values.forEach(v -> add(key, v));
            return this;
        }

        <KK extends K, VV extends V> MultiValueListMapBuilder<K, V> setEntries(KK key, List<VV> values);

        MultiValueListMap<K, V> build();
    }
}
