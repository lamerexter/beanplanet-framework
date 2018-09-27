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
