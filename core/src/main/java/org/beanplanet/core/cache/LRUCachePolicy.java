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
 * Implementation of a Least-Recently-Used caching policy.
 * <p>
 * Items that have been least used recently are ejected before items used recently.
 * <p>
 * When a 'cache hit' occurs on an item, the item is put to the 'back of the queue' and will then be the last of the
 * items to be expelled from the cache, at that moment in time.
 * 
 * @author Gary Watson
 */
public class LRUCachePolicy<K, V> { // extends CachePolicy<K, V> {
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
