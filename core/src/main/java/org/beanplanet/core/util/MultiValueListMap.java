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
