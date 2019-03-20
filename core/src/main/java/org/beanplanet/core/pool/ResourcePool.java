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
 * A high-level model of a pool of objects.
 * 
 * <p>
 * Resource pools are useful when a limited number of system resources exist that are needed, potentially, by a larger
 * number of clients. Pools can also be used to restrict the number of resources acquired by clients in order to
 * guarantee consistent levels of service. For example, in a web-facing environment, it is quite feasible the server
 * could become inundated with requests during periods of high activity and performance can suffer as a result of the
 * number of processes or threads being created or the number of open connections that are having to be maintained.
 * </p>
 * 
 * <p>
 * In such cases, having pools of threads and connections guarantees the maximum numbers of each of these resources at
 * any given time. Clients requesting a pooled resource make requests directly to the pool containing the specific
 * resource. If a free resource exists in the pool, it is returned immediately to the client. If a resource is not
 * immediately available, the client can be made to wait for one to become available.
 * </p>
 * 
 * <p>
 * A client that has finished with a resource, simply returns it to the pool from where it was originally obtained:
 * making the resource available to other clients that may be waiting.
 * </p>
 * 
 * <p>
 * The specific type of objects within a pool are <u>not</u> of concern to this interface. This interface sets out to
 * abstract the behaviour of pools in general.
 * </p>
 * 
 * @author Gary Watson
 * @since 1999
 */
public interface ResourcePool<E> {
   /**
    * Returns the total length of the pool.
    * 
    * <p>This is defined to be the <u>current</u> total number of items in the pool and is the sum of the number of <u>available</u> and <u>loaned</u> items.</p>
    *
    * @return the current totals number of items in the pool - loaned or free.
    */
   default int size() {
      return getNumberOfAvailableItems()+getNumberOfLoanedItems();
   }

   /**
    * Returns the number of items in the pool not currently on loan to clients.
    * 
    * @return the number of available (or free) items.
    */
   int getNumberOfAvailableItems();

   /**
    * Returns the number of items in the pool currently on loan to clients.
    * 
    * @return the number of loaned items.
    */
   int getNumberOfLoanedItems();

   /**
    * Call this method to borrow an item from the pool of available (free) items. Invoke the
    * <code>returnItem(Object)</code> to return the borrowed item back to the pool of available items.
    * 
    * @return Object an object from the pool of available resources
    * @see #returnItem(Object)
    * @exception ResourcePoolException thrown if an error occurs loaning the client a pooled resource
    */
   E loanItem() throws ResourcePoolException;

   /**
    * Call this method to return a previously borrowed an item back to pool. All calls to <code>loanItem()</code> must
    * be followed, as early as is practical, by a call to this method to return the pooled resource.
    * 
    * <p>
    * Upon invocation of this method the pool will decide, based upon configuration and rules, whther to return the item
    * back to the pool of available (free) items.
    * </p>
    * 
    * <p>
    * <em>Note:</em> delays in calling this method or outright failure to do so may degrade system performance and stall
    * clients sharing the pool of available resources.
    * </p>
    * 
    * @param item the object to return.
    * @return true if the items specified originated from the pool and was returned or expired successfully, false
    *         otherwise.
    * @see #loanItem()
    */
   boolean returnItem(E item);

   /**
    * Determines whether an item within the pool, is still valid.
    * 
    * <p>
    * It is feasible that resources held within a pool have a finite lifespan. It is also feasible that resources that
    * have been in the pool and have not been used for some time may be invalidated.
    * </p>
    * 
    * <p>
    * Implementations may implement this method to determine if the pooled item specified is still valid for client use.
    * </p>
    * 
    * @param item the object whose validity is to be checked.
    * @return boolean true if the item is still valid for clients to use, false otherwise.
    */
   boolean isItemValid(E item);
}
