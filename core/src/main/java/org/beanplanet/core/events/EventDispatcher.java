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

package org.beanplanet.core.events;

import org.beanplanet.core.events.Event;
import org.beanplanet.core.events.EventListener;

/**
 * A generic event dispatcher, capable of dispatching an event of a given class
 * through a corresponding listener type.
 *  
 * @author Gary Watson
 */
public interface EventDispatcher<E extends Event, L extends EventListener> {
   /**
    * Called to dispatch an event through the given listener.
    * 
    * @param event the event to be dispatched. 
    * @param listener the listener through which the event should be dispatched.
    * @throws Exception if an error occurs dispatching the event.
    */
   public void dispatchEvent(E event, L listener) throws Exception;
}
