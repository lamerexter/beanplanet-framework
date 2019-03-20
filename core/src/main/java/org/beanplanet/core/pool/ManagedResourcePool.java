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

import org.beanplanet.core.models.lifecycle.StartupShutdownLifecycle;
import org.beanplanet.core.util.SizeUtil;

import java.util.HashMap;
import java.util.LinkedList;

import static org.beanplanet.core.pool.ResourcePoolEvent.PoolEventType.ITEM_LOANED;
import static org.beanplanet.core.pool.ResourcePoolEvent.PoolEventType.ITEM_RETURNED;

/**
 * Implements an in-memory pool of resources.
 * 
 * <p>
 * Provides an abstract pool of arbitrary object types, where implementing classes provide the mechanism for creating
 * specific object types for the pool.
 * </p>
 * 
 * @author Gary Watson
 * @since 1999
 */
public class ManagedResourcePool<E> extends ManagedResourcePoolBase<E> implements StartupShutdownLifecycle {
   private String name;

   /**
    * List of items in the pool that are not on loan to clients. A [doubly] linked list is used to ensure a round-robin
    * loaning strategy.
    */
   protected LinkedList<E> availableItems = new LinkedList<E>();

   /** List of items in the pool that are on loan to clients. */
   protected HashMap<E, Long> loanedItems = new HashMap<E, Long>();

   /** The maximum number of items in the pool. */
   private Integer maximumTotalSize;

   /** The initial number of items to create in the pool at pool startup and maintain subsequently */
   private Integer initialAvailableResources;

   /** The minimum number of items that should exist in the pool. */
   private Integer minimumAvailableResources;

   /**
    * The maximum number of attempts to try to create a new pooled resource item.
    */
   private int maximumNumberOfNewItemCreationTriesBeforeFailure = 3;

   /**
    * The maximum time (ms) a client should be made to wait for an item from the pool.
    */
   private Integer maximumClientWaitMillis;

   /**
    * Creates a named pool with a minimal configuration.
    */
   public ManagedResourcePool(PooledResourceManager<E> pooledResourceManager) {
      super(pooledResourceManager);
   }

   /**
    * Gets the name of this resource pool.
    *
    * @return the name of the pool.
    */
   public String getName() {
      return name;
   }

   /**
    * Sets the name of this resource pool.
    *
    * @param name the name of the pool.
    */
   public void setName(String name) {
      this.name = name;
   }

   /**
    * Returns the initial capacity of the pool and the minimum number of available resources to maintain.
    * 
    * return the initial length of the pool and minimum number of available resources to maintain.
    */
   public Integer getMinimumAvailableResources() {
      return minimumAvailableResources;
   }

   /**
    * Sets the initial capacity of the pool. That is, the initial number of free items that will reside in the pool at
    * startup.
    * 
    * @param minimumAvailableResources the initial length of the pool and minimum number of available resources to
    *        maintain.
    */
   public void setMinimumAvailableResources(Integer minimumAvailableResources) {
      this.minimumAvailableResources = minimumAvailableResources;
   }

   /**
    * Returns the maximum allowed capacity of the pool. That is, the maximum total number of free + loaned items that
    * are allowed.
    * 
    * @return the maximum allowed length of the pool, or <code>Integer.MAX_VALUE</code> if no maximum length has been
    *         configured.
    */
   public Integer getMaximumTotalSize() {
      return maximumTotalSize;
   }

   /**
    * Sets the maximum allowed capacity of the pool. That is, the maximum total number of free + loaned items that are
    * allowed.
    * 
    * @param maximumTotalSize the maximum length of the pool.
    */
   public void setMaximumTotalSize(Integer maximumTotalSize) {
      this.maximumTotalSize = maximumTotalSize;
   }

   public void startup() throws Exception {
      debug("Starting up ...");
      reset();
      debug("Startup complete");
   }

   /**
    * Shuts down the pool gracefully.
    */
   public void shutdown() {
      debug("Shutting down ...");

      // removeHousekeepers();
      clear();
      debug("Shutdown complete");
   }

   /**
    * Returns the maximum number of attempts at creation of a new resource for the pool before an error is thrown.
    * 
    * @return the maximum number of new resource item attempts before failure.
    */
   public int getMaximumNumberOfNewItemCreationTriesBeforeFailure() {
      return maximumNumberOfNewItemCreationTriesBeforeFailure;
   }

   /**
    * Sets the maximum number of attempts at creation of a new resource for the pool before an error is thrown.
    * 
    * @param maximumNumberOfNewItemCreationTriesBeforeFailure the maximum number of new resource item attempts before
    *        failure.
    */
   public void setMaximumNumberOfNewItemCreationTriesBeforeFailure(int maximumNumberOfNewItemCreationTriesBeforeFailure) {
      this.maximumNumberOfNewItemCreationTriesBeforeFailure = maximumNumberOfNewItemCreationTriesBeforeFailure;
   }

   /**
    * Returns the maximum time (ms) a client waiting for an item from the pool is required to wait.
    * 
    * <p>
    * Clients that are still unable to obtain an item from the pool, after this time, are returned with an exception.
    * 
    * @return the maximum time a client requesting an item from the pool must wait, or -1 if clients must wait up to
    *         forever.
    */
   public int getMaximumClientWaitMillis() {
      return maximumClientWaitMillis;
   }

   /**
    * Sets the maximum time, in milliseconds, a client is to be made to wait for an item from the pool before returning
    * an error to the client.
    * 
    * <p>
    * Clients unable to obtain an item from the pool, after this time, are returned with an exception.
    * </p>
    * 
    * @param maximumClientWaitMillis the maximum time a client requesting an item from the pool must wait, or -1 if
    *        clients must wait up to forever.
    */
   public void setMaximumClientWaitMillis(int maximumClientWaitMillis) {
      this.maximumClientWaitMillis = maximumClientWaitMillis;
   }

   /**
    * Re-initialises the connection pool.
    * 
    */
   public synchronized void reset() {
      debug("reset started [pool=" + getName() + ", minimumAvailable=" + minimumAvailableResources
                   + ", maximumCapacity=" + maximumTotalSize + "]");
      clear();

      checkMinimumAvailableResources();
      debug("reset ended [pool=" + getName() + ", minimumAvailable=" + minimumAvailableResources
                   + ", maximumCapacity=" + maximumTotalSize + ", report=" + getReport() + "]");
   }

   private void checkMinimumAvailableResources() {
      for (int n = 0; n < minimumAvailableResources; n++) {
         try {
            availableItems.add(createNewItem());
         } catch (Exception e) {
            // Don't worry if we can't make the initial object at this time.
            // Perhaps now is a bad time. Clients requesting objects from the
            // pool later
            // will cause a new one to be made.
            //
            // However, this could be catastrophic so log it.
            error("Unable to create initial item: ", e);
         }
      }
   }

   /**
    * Returns the total length of the pool.
    * 
    * <p>
    * Equivalent to <code>getNumberOfLoanedItems()</code> + <code>getNumberOfFreeItems()</code>.
    * </p>
    * 
    * @return the current totals number of items in the pool - loaned or free.
    */
   public int size() {
      return getNumberOfLoanedItems() + getNumberOfAvailableItems();
   }

   /**
    * Returns the number of items in the pool currently on loan to clients.
    * 
    * @return the number of loaned items.
    */
   public int getNumberOfLoanedItems() {
      return loanedItems.size();
   }

   /**
    * Returns the number of items in the pool not currently on loan to clients.
    * 
    * @return the number of free items.
    */
   public int getNumberOfAvailableItems() {
      return availableItems.size();
   }

   /**
    * Loans an item from the pool. Clients call this method when they want to borrow an item from the pool. When
    * finished with the borrowed item, clients must call <code>returnItem</code> to return the item.
    * 
    * @return Object an object from the pool
    * @see #returnItem(Object)
    * @exception ResourcePoolException thrown if, for any reason, a new poolable object could not be created.
    */
   public synchronized E loanItem() throws ResourcePoolException {
      debug("loanItem started [pool=" + getName() + ", report=" + getReport() + "]");

      try {
         // Log time to get a connection using superclass
         // (PerformanceMonitoringAuditedResourceImpl) methods
         long startTime = System.currentTimeMillis();

         E item = getExistingOrNewValidItem();
         long remaining = maximumClientWaitMillis;

         while (item == null) {
            try {
               if (maximumClientWaitMillis < 0) {
                  wait(); // Wait until one becomes available
               }
               else {
                  wait(remaining); // Wait remaining time or until available
               }
            } catch (InterruptedException e) {
            }

            if (maximumClientWaitMillis >= 0) {
               remaining = maximumClientWaitMillis - (System.currentTimeMillis() - startTime);
               if (remaining <= 0) {
                  String errorMessage = "Pool client timed out waiting on pooled resource for longer than "
                                        + maximumClientWaitMillis + " ms ("
                                        + SizeUtil.getElapsedTimeSpecificationDescription(maximumClientWaitMillis)
                                        + ")";
                  throw new ResourcePoolException(errorMessage);
               }
            }

            item = getExistingOrNewValidItem();
         }

         loanedItems.put(item, new Long(System.currentTimeMillis()));
         fireResourcePoolEvent(new ResourcePoolEvent<E>(ITEM_LOANED, this, item));

         return item;
      } catch (Throwable th) {
         throw new ResourcePoolException(getName() + " pool: error loaning pooled resource: ", th);
      } finally {
         debug("loanItem ended [pool=" + getName() + ", report=" + getReport() + "]");
      }
   }

   /**
    * Loans an item from the pool. This method attempts to use an existing item from the free pool. If no free items
    * exist, either a new one is created, if configured, or no item is returned.
    * 
    * @return a pooled item known to be valid at this instant, or null if all items are loaned and no further expansion
    *         of the pool is allowed.
    * @exception Exception thrown if, for any reason, a new poolable object could not be created.
    */
   private E getExistingOrNewValidItem() throws Exception {
      E item = null;
      int maximumNewTriesCount = 0;

      while (item == null && maximumNewTriesCount < getMaximumNumberOfNewItemCreationTriesBeforeFailure()) {
         if (availableItems.size() > 0) {
            // Pick the first Object in the list
            // using our round-robin strategy.
            item = availableItems.getFirst();
            availableItems.removeFirst();
         }
         else if (loanedItems.size() < maximumTotalSize) {
            maximumNewTriesCount++;
            item = createNewItem();
         }
         else {
            // Unable to loan existing item or create a new one due to the
            // current pool configuration
            break;
         }

         if (!isItemValid(item)) {
            item = null;
         }
      }

      return item;
   }

   /**
    * Returns an item to the pool. Clients call this method when they want to return an item to the pool. The item
    * should have been obtained by a call to <code>loanItem</code> initially.
    * 
    * @param item the object to return.
    * @see #loanItem()
    */
   public synchronized boolean returnItem(E item) {
      debug("returnItem started [pool=" + getName() + ", report=" + getReport() + "]");
      boolean originatedFromPool = loanedItems.containsKey(item);
      if (originatedFromPool) {
         loanedItems.remove(item);

         //
         // Put the connection back at the end of our list for a
         // round-robin connection pooling strategy.
         availableItems.add(item);
         fireResourcePoolEvent(new ResourcePoolEvent<E>(ITEM_RETURNED, this, item));
         notifyAll();
      }
      debug("returnItem ended [pool=" + getName() + ", report=" + getReport() + "]");
      return originatedFromPool;
   }

   /**
    * Returns a useful statistics report about the pool, suitable for human reading.
    * 
    * @return a statistical report summary - nice.
    */
   public String getReport() {
      return "poolSize=" + size() + ", loaned=" + getNumberOfLoanedItems() + ", available="
             + getNumberOfAvailableItems();
   }

   /**
    * Clears out the pool.
    * 
    * <p>
    * A call to this method effectively clears items from the free list of items - the items available for immediate use
    * by clients.
    * </p>
    * 
    * <p>
    * Loaned items are <b>not</b> freed. Instead, loaned items are left with the clients to whom they were originally
    * loaned.
    * </p>
    */
   public final synchronized void clear() {
      if (availableItems.size() > 0) {
         debug("Clearing resource pool available items [" + getReport() + "] ...");
         while (availableItems.size() > 0) {
            expireItem(availableItems.remove(0));
         }
         debug("Clearing resource pool complete [" + getReport() + "] ...");
      }
   }
}
