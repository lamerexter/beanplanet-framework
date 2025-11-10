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
package org.beanplanet.core.cache;

/**
 * Implementation of a Least-Recently-Used (LRU) caching policy.
 * <p>
 * Items that have been least used recently are ejected before items used recently.
 * <p>
 * When a 'cache hit' occurs on an item, the item is put to the 'back of the queue' and will then be the last of the
 * items to be expelled from the cache, at that moment in time.
 */
public class LruCachePolicy<K, V> implements CachePolicy<K, V> {
    /** Maximum number of items allowed in the cache before the LRY caching is applied. */
    private int lruCacheSize = 500;

    public LruCachePolicy() {}

    public LruCachePolicy(int lruCacheSize) {
        this.lruCacheSize = lruCacheSize;
    }

    /**
     * Gets the LRU cache size which determines the maximum number of items allowed in the cache before the LRY caching
     * policy is applied to maintain that level.
     *
     * @return the LRU cache size.
     */
    public int getLruCacheSize() {
        return lruCacheSize;
    }

    /**
     * Sets the LRU cache size which determines the maximum number of items allowed in the cache before the LRY caching
     * policy is applied to maintain that level.
     */
    public void setLruCacheSize(int lruCacheSize) {
        this.lruCacheSize = lruCacheSize;
    }

    @Override
    public void onCacheCleared(CacheClearedEvent<K, V> event) {

    }

    @Override
    public void onCacheItemsAdded(CacheItemsAddedEvent<K, V> event) {

    }

    @Override
    public void onCacheItemsRemoved(CacheItemsRemovedEvent<K, V> event) {

    }

    @Override
    public void onCacheMiss(CacheMissEvent<K, V> event) {

    }

    @Override
    public void onCacheHit(CacheHitEvent<K, V> event) {

    }

    @Override
    public String getName() {
        return "LRU Cache Policy";
    }

//   /** The LRU buffer size. */
//   protected int lruBufferSize = 500;
//
//   /** The LRU buffer in head-tail preference order. */
//   private DoublyLinkedList<K> lruBuffer = new DoublyLinkedListImpl<K>();
//
//   /** A mapping of cache keys to cached item nodes, in the LRU buffer. */
//   private Map<K, DoublyLinkedListNode<K>> keyToItemMap;
//
//   protected SeverityLogger logger;
//
//   /**
//    * Constructs a LRU Caching Policy with a default cache LRU buffer lruBufferSize. Useful, when configuring the policy
//    * as a bean.
//    *
//    * @see #setLRUBufferSize(int)
//    */
//   public LRUCachePolicy() {
//      this(500, new HashMap<K, DoublyLinkedListNode<K>>(500));
//   }
//
//   public LRUCachePolicy(int lruBufferSize) {
//      this(lruBufferSize, new HashMap<K, DoublyLinkedListNode<K>>(lruBufferSize));
//   }
//
//   public LRUCachePolicy(int lruBufferSize, Map<K, DoublyLinkedListNode<K>> keyToItemMap) {
//      super("LRUCachePolicy", "Least-Recently-Used Caching Policy",
//            "A Least-Recently-Used caching policy. Items that have been used "
//                  + "most recently are chosen to stay in the cache over items that have not been "
//                  + "used recently. When a 'cache hit' occurs on an item, the "
//                  + "item is put to the 'back of the queue' and will then be the "
//                  + "last of the items to be expelled from the cache, at that moment in time.");
//      logger = BeanPlanetLogFactory.getSeverityLogger(this);
//      setLRUBufferSize(lruBufferSize);
//      this.keyToItemMap = keyToItemMap;
//   }
//
//   public int getLRUBufferSize() {
//      return lruBufferSize;
//   }
//
//   public void setLRUBufferSize(int lruBufferSize) {
//      this.lruBufferSize = lruBufferSize;
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
//      // ------------------------------------------------------------------------
//      // Add each key to the list of those recorded in our LRU buffer.
//      // If the key is not already present we need to eject the least recently
//      // used one.
//      // ------------------------------------------------------------------------
//      for (int n = 0; n < keys.length; n++) {
//         K key = keys[n];
//         DoublyLinkedListNode<K> itemNode = keyToItemMap.remove(key);
//         if (itemNode != null) {
//            lruBuffer.remove(itemNode);
//         }
//         else {
//            // item is not already in the cache. Create a new node for it to add
//            // later.
//            itemNode = new DoublyLinkedListNode<K>(key);
//
//            // If the LRU buffer has reached its limit eject the LRU item to
//            // make
//            // room for this new one.
//            if (lruBuffer.size() >= lruBufferSize) {
//               K removedKey = lruBuffer.removeFirst();
//               keyToItemMap.remove(removedKey);
//               cache.remove(removedKey); // remove LRU item from cache
//            }
//         }
//
//         lruBuffer.addLast(itemNode);
//         keyToItemMap.put(key, itemNode); // Add to tail
////
////         if ( logger.isDebugEnabled() ) {
////            logger.debug("Added LRU cache item [key="+key+"]");
////         }
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
//
//      for (int n = 0; n < keys.length; n++) {
//         K key = keys[n];
//         DoublyLinkedListNode<K> itemNode = keyToItemMap.remove(key);
//         if (itemNode != null) {
//            lruBuffer.remove(itemNode);
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
//      keyToItemMap.clear();
//      lruBuffer.clear();
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
//         K key = keys[n];
//         // logger.debug("LRU cache hit [key="+key+"]");
//         DoublyLinkedListNode<K> itemNode = keyToItemMap.remove(key);
//         if (itemNode != null) {
//            lruBuffer.remove(itemNode);
//         }
//         else {
//            // item is not already in the cache. Create a new node for it to add
//            // later.
//            itemNode = new DoublyLinkedListNode<K>(key);
//
//            // If the LRU buffer has reached its limit eject the LRU item to
//            // make
//            // room for this new one.
//            if (lruBuffer.size() >= lruBufferSize) {
//               K removedKey = lruBuffer.removeFirst();
//               keyToItemMap.remove(removedKey);
//               cache.remove(removedKey); // remove LRU item from cache
//            }
//         }
//
//         lruBuffer.addLast(itemNode);
//         keyToItemMap.put(key, itemNode); // Add to tail
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
//      StringBuilder s = new StringBuilder();
//      s.append(ClassUtil.getClassBaseName(getClass())).append("[name=").append(getName())
//      .append(", lruBufferSize=").append(lruBufferSize)
//      .append("]");
//      return s.toString();
//   }
}
