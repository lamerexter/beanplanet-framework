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
