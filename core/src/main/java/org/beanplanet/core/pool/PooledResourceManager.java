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
