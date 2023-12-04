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

import org.beanplanet.core.models.lifecycle.StartupShutdownLifecycle;

import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class AbstractRegistry<K, T> implements Registry<K, T>, StartupShutdownLifecycle {
    private boolean loaded;

    private ConcurrentHashMap<K, T> catalog = new ConcurrentHashMap<>();

    private RegistryLoader<Registry<K, T>> registryLoader;

    public AbstractRegistry() {}

    public AbstractRegistry(RegistryLoader<Registry<K, T>> registryLoader) {
        this.registryLoader = registryLoader;
    }

    @Override
    public boolean addToRegistry(K key, T item) {
        return catalog.putIfAbsent(key, item) == null;
    }

    @Override
    public boolean removeFromRegistry(K key) {
        return catalog.remove(key) != null;
    }

    @Override
    public T lookup(K key) {
        checkLoaded();
        return catalog.get(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E> Stream<E> findEntriesOfType(Class<E> entryType) {
        return catalog.values().stream()
                .filter(v -> entryType.isAssignableFrom(v.getClass()))
                .map(v -> (E)v);
    }

    @Override
    public int size() {
        checkLoaded();
        return catalog.size();
    }

    protected synchronized void checkLoaded() {
        if ( loaded ) return;

        boolean initialLoadingStatus = loaded;
        try {
            loaded = true; // Indicate loading, even to this thread
            loadResources();
        } catch (Throwable th) {
            loaded = initialLoadingStatus;
            throw th;
        }
    }

    private synchronized void loadResources() {
        registryLoader.loadIntoRegistry(this);
    }

    /**
     * Invoked when this object is started-up.
     *
     * <p>Implementers should encapsulate startup behaviour in this method, perhaps creating or referencing the required
     * system resources for use later.</p>
     *
     * <p>While this method does not force this behaviour on implementations, it is
     * strongly recommended that implementations are capable of handling successive calls to this method: either by
     * dealing with the first and ignoring subsequent calls or by dealing with the first and then shutting down, if appropriate, before
     * dealing with each successive call. This will ensure graceful restart behaviour.</p>
     *
     * @throws Exception if an error occurs during startup to
     *                   let the receiver know the component could not be started.
     */
    @Override
    public void startup() throws Exception {
        checkLoaded();
    }

    /**
     * Invoked when this object is shutdown.
     *
     * <p>Implementers should encapsulate shutdown behaviour in this method, perhaps releasing system resources or references to
     * resources created at startup.</p>
     *
     * <p>While this method does not force this behaviour on implementations, it is
     * strongly recommended that implementations are capable of handling successive calls to this method: either by
     * dealing with the first and ignoring subsequent calls or by dealing with the first and then starting-up, if appropriate, before
     * dealing with each successive call. This will ensure graceful restart behaviour.</p>
     *
     * @throws Exception if an error occurs during startup to
     *                   let the receiver know the component could not be started
     */
    @Override
    public void shutdown() {

    }
}
