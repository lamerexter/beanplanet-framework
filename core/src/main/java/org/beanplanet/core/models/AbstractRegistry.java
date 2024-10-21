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
