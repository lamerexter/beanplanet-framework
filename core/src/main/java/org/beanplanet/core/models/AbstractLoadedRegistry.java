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

import org.beanplanet.core.models.lifecycle.StartupShutdownLifecycle;

import java.util.stream.Stream;

public abstract class AbstractLoadedRegistry<K, T> implements Registry<K, T>, StartupShutdownLifecycle {
    private boolean loaded;

    private final RegistryLoader<Registry<K, T>> registryLoader;

    public AbstractLoadedRegistry(RegistryLoader<Registry<K, T>> registryLoader) {
        this.registryLoader = registryLoader;
    }

    @Override
    public final T lookup(K key) {
        checkLoaded();
        return doLookupInternal(key);
    }

    @Override
    public final <E> Stream<E> findEntriesOfType(Class<E> entryType) {
        checkLoaded();
        return doFindEntriesOfTypeInternal(entryType);
    }

    @Override
    public final int size() {
        checkLoaded();
        return sizeInternal();
    }

    protected abstract <E> Stream<E> doFindEntriesOfTypeInternal(Class<E> entryType);

    protected abstract int sizeInternal();

    protected abstract T doLookupInternal(K key);

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
