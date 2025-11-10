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

/**
 * Responsible for the creation, destruction and general lifecycle of a specific pooled resource type.
 *
 * @author Gary Watson
 */
public interface PooledResourceManager<E> {
    /**
     * Creates an instance of the pooled resource type.
     *
     * @return an instance of the new resource to be pooled
     * @throws ResourcePoolException if an error occurs creating the pooled item
     */
    E createItem() throws ResourcePoolException;

    /**
     * Disposes of the pooled item, releasing any system resources associated with items of the pooled type.
     *
     * <p>
     * Implementations will generally not want to rethrow exceptions that occur during disposal of the pooled resource.
     * However, this interface does not limit this behaviour.
     * </p>
     *
     * @param pooledItem the item to be destroyed
     * @throws ResourcePoolException if an error occurs destroying the pooled resource
     */
    void destroyItem(E pooledItem) throws ResourcePoolException;

    /**
     * Determines whether the specified pooled resource is in a valid state for pool clients to use. This might also
     * include testing or refreshing the pooled item.
     *
     * <p>
     * For example, for a database connection pool, this method might determine whether the connection item is still
     * connected and whether a basic query or test SQL statement runs successfully to completion.
     * </p>
     *
     * @param pooledItem the pooled resource whose validity and disposition is to be tested.
     * @return true if, and only if, the pooled item is still valid for clients to use and may remain in a pool of such
     * resources, false otherwise.
     */
    boolean isPooledItemValid(E pooledItem);
}
