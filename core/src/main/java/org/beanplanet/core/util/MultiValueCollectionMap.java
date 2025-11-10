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

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.beanplanet.core.util.CollectionUtil.nullSafe;

public interface MultiValueCollectionMap<K, V, C extends Collection<V>> extends MultiValueMap<K, V, C> {
    boolean addValue(K key, V value);
    default boolean addAllValues(K key, List<V> values) {
        boolean allAdded = true;
        for (V value : nullSafe(values)) {
            allAdded = addValue(key, value) && allAdded;
        }
        return allAdded;
    }
    boolean removeValue(K key, V value);
}
