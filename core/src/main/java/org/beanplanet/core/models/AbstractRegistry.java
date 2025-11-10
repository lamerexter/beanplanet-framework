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

import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public abstract class AbstractRegistry<K, T> extends AbstractLoadedRegistry<K, T> {
    private boolean loaded;

    private ConcurrentHashMap<K, T> catalog = new ConcurrentHashMap<>();

    public AbstractRegistry(RegistryLoader<Registry<K, T>> registryLoader) {
       super(registryLoader);
    }

    @Override
    public boolean addToRegistry(K key, T item) {
        return catalog.putIfAbsent(key, item) == null;
    }

    @Override
    public boolean removeFromRegistry(K key) {
        return catalog.remove(key) != null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E> Stream<E> doFindEntriesOfTypeInternal(Class<E> entryType) {
        return catalog.values().stream()
                .filter(v -> entryType.isAssignableFrom(v.getClass()))
                .map(v -> (E)v);
    }

    @Override
    public int sizeInternal() {
        return catalog.size();
    }

    @Override
    public T doLookupInternal(K key) {
        return catalog.get(key);
    }

}
