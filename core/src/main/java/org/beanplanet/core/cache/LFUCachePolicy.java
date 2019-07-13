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
 * Implementation of a Least-Frequently-Used caching policy.
 * <p>
 * Items that have been least often used are ejected before items used more often.
 * <p>
 * This caching policy maintains list of cached items, sorted according to the number of times each item has been hit.
 * The size of the least-frequent list maintained is determined by the <u>LFUBufferSize</u> property.
 * <p>
 * When a cache hit occurs on an item, the hit count for the item is incremented. When a new item is added to the cache
 * and the least-frequently-used buffer has reached <u>LFUBufferSize</u>, the least frequently used item is ejected from
 * the cache to make room for the new arrival.
 * 
 * @author Gary Watson
 */
public class LFUCachePolicy<K, V> implements CachePolicy<K, V> {
   /** The LFU buffer size. */
   private int lfuBufferSize;

   public int getLfuBufferSize() {
      return lfuBufferSize;
   }

   public void setLfuBufferSize(int lfuBufferSize) {
      this.lfuBufferSize = lfuBufferSize;
   }

   @Override
   public String getName() {
      return null;
   }

   @Override
   public void onCacheItemsAdded(CacheItemsAddedEvent<K, V> event) {

   }

//   /** The maximum number of LFU count lists. */
//   private int maxLFUCountList;
//
//   /** The LFU buffer in head-tail preference order. */
//   private List<DoublyLinkedList<LFUCacheItemInfo<K>>> lfuList = new ArrayList<DoublyLinkedList<LFUCacheItemInfo<K>>>();
//
//   /** A mapping of cache key->LFU count list entry. * */
//   private Map<K, DoublyLinkedListNode<LFUCacheItemInfo<K>>> cacheKeyToLFUEntry = new HashMap<K, DoublyLinkedListNode<LFUCacheItemInfo<K>>>();
//
//   protected SeverityLogger logger;
//
//   private static class LFUCacheItemInfo<E> {
//      public int numberofUses;
//
//      public E item;
//
//      public LFUCacheItemInfo() {
//      }
//
//      public LFUCacheItemInfo(E item) {
//         this.item = item;
//      }
//   }
//
//   /**
//    * Constructs an LFU Caching Policy with a default LFU buffer size. Useful, when configuring the policy as a bean.
//    *
//    * @see #LFUCachePolicy(int)
//    */
//   public LFUCachePolicy() {
//      this(500);
//   }
//
//   /**
//    * Constructs an LFU Caching Policy with the specified LFU buffer size.
//    *
//    * @param lfuBufferSize the size of the LFU buffer that tracks cached items.
//    */
//   public LFUCachePolicy(int lfuBufferSize) {
//      this(lfuBufferSize, Integer.MAX_VALUE);
//   }
//
//   /**
//    * Constructs an LFU Caching Policy with the specified LFU buffer size and maximum number of LFU count lists.
//    *
//    * @param lfuBufferSize the size of the LFU buffer that tracks cached items.
//    * @param maxNumberOfLFUCountLists the maximum number of LFU lists maintained that keep count of each cached objects
//    *        hits.
//    */
//   public LFUCachePolicy(int lfuBufferSize, int maxNumberOfLFUCountLists) {
//      super("LRUCachePolicy", "Least-Frequently-Used Caching Policy",
//            "A Least-Frequently-Used caching policy. This caching policy maintains list of cached items, "
//                  + "sorted according to the number of times each item has been hit. The size of the least-frequent "
//                  + "list maintained is determined by the LFUBufferSize property." + "\n"
//                  + "When a cache hit occurs on an item, the hit count for the item is incremented. When a "
//                  + "new item is added to the cache and the least-frequently-used buffer has reached LFUBufferSize, "
//                  + "the least frequently used item is ejected from the cache to make room for the new arrival.");
//      logger = BeanPlanetLogFactory.getSeverityLogger(this);
//      setLFUBufferSize(lfuBufferSize);
//      setMaximumNumberOfLFUCountLists(maxNumberOfLFUCountLists);
//   }
//
//   public int getLFUBufferSize() {
//      return lfuBufferSize;
//   }
//
//   public void setLFUBufferSize(int lfuBufferSize) {
//      this.lfuBufferSize = lfuBufferSize;
//   }
//
//   public int getMaximumNumberOfLFUCountLists() {
//      return maxLFUCountList;
//   }
//
//   public void setMaximumNumberOfLFUCountLists(int maxLFUCountList) {
//      this.maxLFUCountList = maxLFUCountList;
//   }
//
//   /**
//    * Called when items are added to the cache.
//    *
//    * @param ev the event that relates to the cached items
//    */
//   @Override
//   public final void itemsAdded(CacheEvent<K, V> ev) {
//      K keys[] = ev.getAffectedItemKeys();
//      Cache<K, V> cache = ev.getCache();
//
//      DoublyLinkedList<LFUCacheItemInfo<K>> zeroHitsListImpl = getOrCreateLFUCountList(0);
//      for (int n = 0; n < keys.length; n++) {
//         while (cacheKeyToLFUEntry.size() >= lfuBufferSize) {
//            removeLFUEntry(cache);
//         }
//
//         LFUCacheItemInfo<K> itemInfo = new LFUCacheItemInfo<K>(keys[n]);
//         DoublyLinkedListNode<LFUCacheItemInfo<K>> newNode = zeroHitsListImpl.addToHead(itemInfo);
//         cacheKeyToLFUEntry.put(keys[n], newNode);
//         if ( logger.isDebugEnabled() ) {
//            logger.debug("Added LFU cache item,  key=" + keys[n] + " cache size=" + cache.size());
//         }
//      }
//   }
//
//   /**
//    * Called when items are removed from the cache.
//    *
//    * @param ev the event that relates to the cached items
//    */
//   @Override
//   public final void itemsRemoved(CacheEvent<K, V> ev) {
//      K keys[] = ev.getAffectedItemKeys();
//      Cache<K, V> cache = ev.getCache();
//
//      for (int n = 0; n < keys.length; n++) {
//         DoublyLinkedListNode<LFUCacheItemInfo<K>> itemNode = cacheKeyToLFUEntry.get(keys[n]);
//         if (itemNode == null) {
//            logger
//                  .error("Cache internal error: cache item removed from cache without prior knowledge of it by this cache policy!");
//            continue;
//         }
//
//         LFUCacheItemInfo<K> itemInfo = itemNode.getValue();
//         DoublyLinkedList<LFUCacheItemInfo<K>> currentUsageListImpl = getOrCreateLFUCountList(itemInfo.numberofUses);
//
//         cacheKeyToLFUEntry.remove(keys[n]);
//         currentUsageListImpl.remove(itemNode);
//
//         if ( logger.isDebugEnabled() ) {
//            logger.debug("Removed LFU cache item, key=" + keys[n] + " hit count=" + itemInfo.numberofUses + " cache size="
//                         + cache.size());
//         }
//      }
//   }
//
//   /**
//    * Called when all items are removed from the cache.
//    *
//    * @param ev the event that relates to the cached items
//    */
//   @Override
//   public final void cacheCleared(CacheEvent<K, V> ev) {
//      cacheKeyToLFUEntry.clear();
//      lfuList.clear();
//      if ( logger.isDebugEnabled() ) {
//         logger.debug("Cleared LFU buffer=" + this);
//      }
//   }
//
//   /**
//    * Called when a cache 'hit' occurs of an item in the cache.
//    *
//    * @param ev the event that relates to the cached item
//    */
//   @Override
//   public final void cacheItemHit(CacheEvent<K, V> ev) {
//      K keys[] = ev.getAffectedItemKeys();
//      Cache<K, V> cache = ev.getCache();
//
//      for (int n = 0; n < keys.length; n++) {
//         DoublyLinkedListNode<LFUCacheItemInfo<K>> itemNode = cacheKeyToLFUEntry.get(keys[n]);
//         if (itemNode == null) {
//            logger
//                  .error("Cache internal error: cache item hit from cache without prior knowledge of the item by this cache policy, key="
//                         + keys[n]);
//            continue;
//         }
//
//         LFUCacheItemInfo<K> itemInfo = itemNode.getValue();
//         DoublyLinkedList<LFUCacheItemInfo<K>> currentUsageListImpl = getOrCreateLFUCountList(itemInfo.numberofUses);
//         DoublyLinkedList<LFUCacheItemInfo<K>> nextUsageListImpl = getOrCreateLFUCountList(++itemInfo.numberofUses);
//
//         currentUsageListImpl.remove(itemNode);
//         nextUsageListImpl.add(itemInfo);
//
//         if ( logger.isDebugEnabled() ) {
//            logger.debug("LFU cache hit, key=" + keys[n] + ", hit count=" + itemInfo.numberofUses + " cache size="
//                         + cache.size());
//         }
//      }
//   }
//
//   private DoublyLinkedList<LFUCacheItemInfo<K>> getOrCreateLFUCountList(int count) {
//      DoublyLinkedList<LFUCacheItemInfo<K>> lfuCountListImpl = null;
//      int lfuCountListIndex = Math.min(count, maxLFUCountList);
//
//      if (lfuCountListIndex >= lfuList.size()) {
//         lfuCountListImpl = new DoublyLinkedListImpl<LFUCacheItemInfo<K>>();
//         lfuList.add(lfuCountListIndex, lfuCountListImpl);
//      }
//      else {
//         lfuCountListImpl = lfuList.get(lfuCountListIndex);
//      }
//
//      return lfuCountListImpl;
//   }
//
//   private void removeLFUEntry(Cache<K, V> cache) {
//      DoublyLinkedList<LFUCacheItemInfo<K>> lfuListWithItemsImpl = null;
//      for (int n = 0; n < lfuList.size() && lfuListWithItemsImpl == null; n++) {
//         DoublyLinkedList<LFUCacheItemInfo<K>> lfuListFoundImpl = lfuList.get(n);
//         if (lfuListFoundImpl.size() > 0) {
//            lfuListWithItemsImpl = lfuListFoundImpl;
//         }
//      }
//
//      if (lfuListWithItemsImpl != null) {
//         LFUCacheItemInfo<K> itemInfo = lfuListWithItemsImpl.peekLast();
//         cache.remove(itemInfo.item);
//      }
//   }
//
//   public int getUsageCount(Object cacheKey) {
//      DoublyLinkedListNode<LFUCacheItemInfo<K>> lfuListNode = cacheKeyToLFUEntry.get(cacheKey);
//      if (lfuListNode == null) {
//         return -1;
//      }
//
//      LFUCacheItemInfo<K> itemInfo = lfuListNode.getValue();
//      return itemInfo.numberofUses;
//   }
//
//   /**
//    * Returns a string report about the cache policy.
//    *
//    * @return a string that represents the internal state of the cache policy.
//    */
//   @Override
//   public String toString() {
//      StringBuilder s = new StringBuilder();
//      s.append(ClassUtil.getClassBaseName(getClass())).append("[name=").append(getName())
//      .append(", lfuBufferSize=")
//      .append(lfuBufferSize).append(", maxLFUCountList=").append(maxLFUCountList)
//      .append("]");
//      return s.toString();
//   }
}
