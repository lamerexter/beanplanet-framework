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
package org.beanplanet.core.cache;

/**
 * Implementation of cache item time-to-live caching policy.
 * <p>
 * Items placed in a cache have a configured amount of time cached. Once a cached item's time has expired, it is
 * forcibly removed from the cache.
 * 
 * @author Gary Watson
 */
public class TimeToLiveCachePolicy<K, V>  { //extends AbstractCachePolicy<K, V> {
//   /** The time-to-live for cached items in milliseconds. */
//   private long timeToLiveMillis = 60 * SizeUtil.SECONDS_IN_MS;
//
//   private static class TTLCachedItemInfo {
//      public long lastAccessed;
//      public Object key;
//      public Object item;
//   }
//
//   private Map<K, DoublyLinkedListNode<TTLCachedItemInfo>> cachedItemToTTLInfoMap = new HashMap<K, DoublyLinkedListNode<TTLCachedItemInfo>>();
//   private DoublyLinkedList<TTLCachedItemInfo> ttlInfoList = new DoublyLinkedListImpl<TTLCachedItemInfo>();
//
//   private Thread cacheEvictionThread;
//   private boolean evictionThreadRunning = false;
//   private boolean runningEvictionCheck = false;
//
//   /**
//    * Whether this policy is configured to use a reaper thread to periodically check for and evict items whose ttl has
//    * expired.
//    */
//   protected boolean useEvictionThread = true;
//   /** Whether the evictionThread is paused in its processing. It may still be active and running. */
//   protected boolean pauseEvictionThread = false;
//
//   protected Long expiredItemCheckIntervalMillis;
//
//   protected SeverityLogger logger;
//
//   /**
//    * Constructs a Time-To-Live Caching Policy.
//    */
//   public TimeToLiveCachePolicy() {
//      super("TimeToLiveCachePolicy", "Time-To-Live Caching Policy",
//            "A Time-To-Live caching policy. " + "Items placed in a cache have a configured amount of time cached. "
//                  + "Once a cached item's time has expired, it is forcibly removed from the cache.");
//      logger = BeanPlanetLogFactory.getSeverityLogger(this);
//   }
//
//   public TimeToLiveCachePolicy(int timeToLiveMillis) {
//      this();
//      setItemTimeToLiveMillis(timeToLiveMillis);
//   }
//
//   public long getItemTimeToLiveMillis() {
//      return timeToLiveMillis;
//   }
//
//   public void setItemTimeToLiveMillis(long timeToLiveMillis) {
//      this.timeToLiveMillis = timeToLiveMillis;
//   }
//
//   public void setItemTimeToLiveSeconds(int timeToLiveSeconds) {
//      setItemTimeToLiveMillis(timeToLiveSeconds * 1000);
//   }
//
//   public void setItemTimeToLiveMinutes(int timeToLiveMinutes) {
//      setItemTimeToLiveSeconds(timeToLiveMinutes * 60);
//   }
//
//   public void setItemTimeToLiveHours(int timeToLiveHours) {
//      setItemTimeToLiveMinutes(timeToLiveHours * 60);
//   }
//
//   public void setItemTimeToLiveDays(int timeToLiveDays) {
//      setItemTimeToLiveHours(timeToLiveDays * 24);
//   }
//
//   public void setItemTimeToLiveWeeks(int timeToLiveWeeks) {
//      setItemTimeToLiveDays(timeToLiveWeeks * 7);
//   }
//
//   public int getNumberOfTimeToLiveItems() {
//      return cachedItemToTTLInfoMap.size();
//   }
//
//   /**
//    * @return the useEvictionThread
//    */
//   public boolean getUseEvictionThread() {
//      return useEvictionThread;
//   }
//
//   /**
//    * @param useEvictionThread the useEvictionThread to set
//    */
//   public void setUseEvictionThread(boolean useEvictionThread) {
//      this.useEvictionThread = useEvictionThread;
//   }
//
//   /**
//    * @return the pauseEvictionThread
//    */
//   public boolean getPauseEvictionThread() {
//      return pauseEvictionThread;
//   }
//
//   /**
//    * @param pauseEvictionThread the pauseEvictionThread to set
//    */
//   public void setPauseEvictionThread(boolean pauseEvictionThread) {
//      this.pauseEvictionThread = pauseEvictionThread;
//   }
//
//   /**
//    * Called when the listener is first added to the cache.
//    *
//    * @param ev the cache event, containing the cache this listener has been registered with
//    */
//   @Override
//   public void registeredWithCache(CacheEvent<K, V> ev) {
//      startEvictionThread(ev.getCache());
//   }
//
//   /**
//    * Called when the listener is removed from the cache.
//    *
//    * @param ev the cache event, containing the cache this listener has been unregistered with
//    */
//   @Override
//   public void unregisteredFromCache(CacheEvent<K, V> ev) {
//      stopEvictionThread(ev.getCache());
//   }
//
//   private void stopEvictionThread(Cache<K, V> cache) {
//      if (!evictionThreadRunning || cacheEvictionThread == null) {
//         return;
//      }
//      evictionThreadRunning = false;
//      logger.debug("Stopping cache eviction thread ...");
//      try {
//         if (cacheEvictionThread.isAlive()) {
//            cacheEvictionThread.join(getItemTimeToLiveMillis());
//         }
//         if (cacheEvictionThread.isAlive()) {
//            // Still working so attempt an interrupt and hope that does the trick
//            cacheEvictionThread.interrupt();
//         }
//      } catch (Throwable th) {
//         // Ignored as we are shutting down
//      }
//   }
//
//   private void startEvictionThread(final Cache<K, V> cache) {
//      if (evictionThreadRunning) {
//         // Halt any existing eviction thread
//         stopEvictionThread(cache);
//      }
//      if (!useEvictionThread) {
//         return;
//      }
//
//      evictionThreadRunning = true;
//      logger.debug("Starting cache eviction thread ...");
//      cacheEvictionThread = new Thread(new Runnable() {
//         public void run() {
//            while (useEvictionThread && evictionThreadRunning) {
//               try {
//                  Thread.sleep(getExpiredItemCheckIntervalMillis());
//                  if (useEvictionThread && pauseEvictionThread) {
//                     logger.debug("Eviction thread currently paused");
//                  }
//                  else {
//                     logger.debug("Eviction thread performing check for expired items ...");
//                     checkAndEvictExpiredItems(cache);
//                  }
//               } catch (InterruptedException interrupEx) {
//
//               }
//            }
//         }
//      }, ClassUtil.getClassBaseName(getClass()));
//      cacheEvictionThread.setDaemon(true); // Allow for unexpected DIRECT shutdown of the VM
//      cacheEvictionThread.start();
//   }
//
//   /**
//    * Called when items are added to the cache.
//    *
//    * @param ev the event that relates to the cached items
//    */
//   @Override
//   public final void itemsAdded(CacheEvent<K, V> ev) {
//      long addTime = System.currentTimeMillis();
//      for (K key : ev.getAffectedItemKeys()) {
//         TTLCachedItemInfo info = new TTLCachedItemInfo();
//         info.item = ev.getCache().get(key);
//         info.key = key;
//         info.lastAccessed = addTime;
//         DoublyLinkedListNode<TTLCachedItemInfo> ttlNode = new DoublyLinkedListNode<TTLCachedItemInfo>(info);
//         ttlInfoList.add(ttlNode);
//         cachedItemToTTLInfoMap.put(key, ttlNode);
//      }
//      checkAndEvictExpiredItems(ev.getCache());
//   }
//
//   /**
//    * Called when items are removed from the cache.
//    *
//    * @param ev the event that relates to the cached items
//    */
//   @Override
//   public final void itemsRemoved(CacheEvent<K, V> ev) {
//      for (K key : ev.getAffectedItemKeys()) {
//         DoublyLinkedListNode<TTLCachedItemInfo> ttlNode = cachedItemToTTLInfoMap.remove(key);
//         if (ttlNode != null) {
//            ttlInfoList.remove(ttlNode);
//         }
//      }
//      checkAndEvictExpiredItems(ev.getCache());
//   }
//
//   /**
//    * Called when all items are removed from the cache.
//    *
//    * @param ev the event that relates to the cached items
//    */
//   @Override
//   public final void cacheCleared(CacheEvent<K, V> ev) {
//      cachedItemToTTLInfoMap.clear();
//      ttlInfoList.clear();
//   }
//
//   @SuppressWarnings("unchecked")
//   protected void checkAndEvictExpiredItems(Cache<K, V> cache) {
//      synchronized (cache) {
//         if ( runningEvictionCheck ) {
//            return;
//         }
//         try{
//            runningEvictionCheck = true;
//
//            long now = System.currentTimeMillis();
//            long itemValidityPeriodLowerBound = now - getItemTimeToLiveMillis();
//            int itemsEvicted = 0;
//            if (logger.isDebugEnabled()) {
//               logger.debug("Starting ttl item eviction check [cache size=" + cache.size() + ", ttl cache size="
//                     + cachedItemToTTLInfoMap.size()+"/"+ttlInfoList.size()+"] ...");
//            }
//
//            boolean keepChecking = true;
//
//            while (!ttlInfoList.isEmpty() && keepChecking) {
//               DoublyLinkedListNode<TTLCachedItemInfo> infoNode = ttlInfoList.getNode(0);
//               if (infoNode.getValue().lastAccessed < itemValidityPeriodLowerBound) {
//                  cache.remove((K) infoNode.getValue().key);
//                  itemsEvicted++;
//               }
//               else {
//                  keepChecking = false;
//               }
//            }
//            if (logger.isDebugEnabled()) {
//               logger.debug("Completed ttl item eviction check [cache size=" + cache.size() + ", ttl cache size="
//                            + cachedItemToTTLInfoMap.size()+"/"+ttlInfoList.size()+", items evicted =" + itemsEvicted + "] ...");
//            }
//         }
//         finally {
//            runningEvictionCheck = false;
//         }
//      }
//   }
//
//   /**
//    * Returns a string report about the cache policy.
//    *
//    * @return a string that represents the internal state of the cache policy.
//    */
//   @Override
//   public String toString() {
//      StringBuilder sBuf = new StringBuilder();
//      sBuf.append(getClass().getName()).append("[").append("name=")
//      .append(", ttl millis=").append(getItemTimeToLiveMillis())
//      .append("]");
//
//      return sBuf.toString();
//   }
//
//   /**
//    * Called to process an item whose time-to-live has expired.
//    * <p>
//    * In this implementation, the item is simply removed from the cache.
//    *
//    * @param key the key of the item whose time-to-live has now expired.
//    */
//   protected void expireTimeToLiveItem(Cache<K, V> cache, K key) {
//      cache.remove(key); // We'll me messaged about the removal in
//      // itemsRemoved()
//   }
//
//   /**
//    * @return the expiredItemCheckIntervalMillis
//    */
//   public long getExpiredItemCheckIntervalMillis() {
//      return expiredItemCheckIntervalMillis != null ?  expiredItemCheckIntervalMillis.longValue() : getItemTimeToLiveMillis()/2;
//   }
//
//   /**
//    * @param expiredItemCheckIntervalMillis the expiredItemCheckIntervalMillis to set
//    */
//   public void setExpiredItemCheckIntervalMillis(long expiredItemCheckIntervalMillis) {
//      this.expiredItemCheckIntervalMillis = expiredItemCheckIntervalMillis;
//   }
}
