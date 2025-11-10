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
package org.beanplanet.core.pool;

import org.beanplanet.core.events.EventSupport;
import org.beanplanet.core.lang.Assert;
import org.beanplanet.core.logging.Logger;

import java.util.ArrayList;
import java.util.List;

import static org.beanplanet.core.pool.ResourcePoolEvent.PoolEventType.*;

public abstract class ManagedResourcePoolBase<E> implements ResourcePool<E>, ResourcePoolEventSource, Logger {
    protected EventSupport eventSupport = new EventSupport();

    /**
     * The manager for the type of resource in this pool, handling the lifecycle of each item from its creation, testing
     * for validity and ultimately its disposal.
     */
    protected PooledResourceManager<E> resourceManager;

    /**
     * A list of resource pool listeners.
     */
    protected List<ResourcePoolListener<E>> listeners = new ArrayList<ResourcePoolListener<E>>();

    public ManagedResourcePoolBase() {
    }

    public ManagedResourcePoolBase(PooledResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    protected void fireResourcePoolEvent(ResourcePoolEvent<E> event) {
        eventSupport.dispatchEvent(event);
    }

    /**
     * Creates a new item that will be added in as a free item in the pool.
     *
     * <p>
     * This method delegates directly to the pooled resource manager lifecycle method
     * <code>{@link PooledResourceManager#createItem()}</code>.
     * </p>
     *
     * @return an newly created/initialised object whose type is specific to the pool.
     * @throws Exception thrown if, for any reason, the pooled resource could not be created.
     * @see PooledResourceManager#createItem()
     */
    protected E createNewItem() throws Exception {
        Assert.notNull(resourceManager, "The Pooled Resource Manager may not be null in this type of resource pool.");
        E newResource = resourceManager.createItem();
        fireResourcePoolEvent(new ResourcePoolEvent<>(ITEM_CREATED, this, newResource));
        return newResource;
    }

    /**
     * Disposes of a pooled resource item, freeing any system resources associated with it.
     *
     * <p>
     * This method delegates directly to the pooled resource manager lifecycle method
     * <code>{@link PooledResourceManager#destroyItem(Object)}</code>.
     * </p>
     *
     * @param item the item to be disposed of.
     * @see PooledResourceManager#destroyItem(Object)
     */
    public void expireItem(E item) {
        Assert.notNull(resourceManager, "The Pooled Resource Manager may not be null in this type of resource pool.");
        resourceManager.destroyItem(item);
        fireResourcePoolEvent(new ResourcePoolEvent<E>(ITEM_DESTROYED, this, item));
    }

    /**
     * Determines whether an item within the pool, is still valid for clients to use.
     *
     * <p>
     * This method delegates directly to the pooled resource manager lifecycle method
     * <code>{@link PooledResourceManager#isPooledItemValid(Object)}</code>.
     * </p>
     *
     * @param item the object whose validity is to be checked.
     * @return boolean true if the item is still valid for clients to use, false otherwise.
     * @see PooledResourceManager#isPooledItemValid(Object)
     */
    public boolean isItemValid(E item) {
        Assert.notNull(resourceManager, "The Pooled Resource Manager may not be null in this type of resource pool.");
        boolean isValid = resourceManager.isPooledItemValid(item);
        fireResourcePoolEvent(new ResourcePoolEvent<E>((isValid ? ITEM_VALID : ITEM_INVALID), this, item));
        return isValid;
    }

    public PooledResourceManager<E> getResourceManager() {
        return resourceManager;
    }

    public void setResourceManager(PooledResourceManager<E> resourceManager) {
        this.resourceManager = resourceManager;
    }
}
