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

import org.beanplanet.core.events.BaseEvent;

public class ResourcePoolEvent<E> extends BaseEvent {

   public enum PoolEventType {
      LISTENER_REGISTERED, LISTENER_UNREGISTERED,
      ITEM_LOANED, ITEM_RETURNED,
      ITEM_CREATED, ITEM_DESTROYED,
      ITEM_VALID, ITEM_INVALID;
   }

   protected PoolEventType eventType;
   protected ResourcePool<E> pool;
   protected E resource;

   public ResourcePoolEvent(PoolEventType eventType, ResourcePool<E> pool, E resource) {
      super(pool);
      this.eventType = eventType;
      this.pool = pool;
      this.resource = resource;
   }

   public PoolEventType getEventType() {
      return eventType;
   }

   public void setEventType(PoolEventType eventType) {
      this.eventType = eventType;
   }

   public ResourcePool<E> getPool() {
      return pool;
   }

   public void setPool(ResourcePool<E> pool) {
      this.pool = pool;
   }

   public E getResource() {
      return resource;
   }

   public void setResource(E resource) {
      this.resource = resource;
   }
}
