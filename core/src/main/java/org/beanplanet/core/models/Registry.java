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

package org.beanplanet.core.models;

import java.util.stream.Stream;

/**
 * Defines a strategy for a registry of a given type.
 * @param <K> The key type used to lookup an entry.
 * @param <T> The type of entries kept in the registry.
 */
public interface Registry<K, T> {
    boolean addToRegistry(K key, T item);
    boolean removeFromRegistry(K key);

    T lookup(K key);
    <E> Stream<E> findEntriesOfType(Class<E> entryType);

    int size();
}
