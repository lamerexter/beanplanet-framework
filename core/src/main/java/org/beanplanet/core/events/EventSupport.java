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

package org.beanplanet.core.events;

import org.beanplanet.core.lang.Assert;
import org.beanplanet.core.lang.TypeNotFoundException;
import org.beanplanet.core.lang.TypeUtil;
import org.beanplanet.core.events.Event;
import org.beanplanet.core.events.EventDispatcher;
import org.beanplanet.core.events.EventListener;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Provides generic support for any event type. The concurrent addition and
 * removal of listeners is supported through this implementation.
 * 
 * @author Gary Watson.
 */
@SuppressWarnings("unchecked")
public class EventSupport {
   /** A mapping from event class to list of event listeners. */
   private ConcurrentHashMap<Class<?>, EventInfo<?, ?>> eventListenerMap = new ConcurrentHashMap<>();

   class EventInfo<E extends Event, L extends EventListener> {
      /** A disspatcher for the event and listener types. */
      private EventDispatcher<E, L> dispatcher;
      /** The list of event listeners for this class of event. */
      private Set<L>                listeners;

      public EventInfo(EventDispatcher<E, L> dispatcher, Set<L> listeners) {
         setDispatcher(dispatcher);
         setListeners(listeners);
      }

      public final EventDispatcher<E, L> getDispatcher() {
         return dispatcher;
      }

      public final void setDispatcher(EventDispatcher<E, L> dispatcher) {
         this.dispatcher = dispatcher;
      }

      public final Set<L> getListeners() {
         return listeners;
      }

      public final void setListeners(Set<L> listeners) {
         this.listeners = listeners;
      }

   }

   /**
    * Adds a listener to the set of listeners for the given class of event.
    *
    * @param eventClass
    *           the class of event for which the listener is to be added.
    * @param listener
    *           the listener to add.
    * @param dispatcher the dispatcher for the given event and listener types.
    * @return true if the listener was added, false otherwise.
    */
   public <E extends Event, L extends EventListener> boolean addListener(Class<E> eventClass, L listener, EventDispatcher<E, L> dispatcher) {
      return getOrCreateEventInfo(eventClass, dispatcher).getListeners().add(listener);
   }

   public <E extends Event, L extends EventListener> EventInfo<E, L> getOrCreateEventInfo(Class<E> eventClass, EventDispatcher<E, L> dispatcher) {
      EventInfo<E, L> newEventInfo = new EventInfo<E, L>(dispatcher, Collections.synchronizedSet(new LinkedHashSet<L>()));
      EventInfo<E, L> eventInfo = (EventInfo<E, L>) eventListenerMap.putIfAbsent(eventClass, newEventInfo);
      if (eventInfo == null) {
         eventInfo = newEventInfo;
      }
      return eventInfo;
   }

   public <E extends Event, L extends EventListener> EventInfo<E, L> getEventInfo(Class<?> eventClass) {
      return (EventInfo<E, L>)eventListenerMap.get(eventClass);
   }

   /**
    * Adds a listener to the set of listeners for the given class of event. The same package is automatically searched for
    * associated event type and dispatcher classes called <code>&lt;event&gt;Event</code> and <code>&lt;event&gt;Dispatcher</code> given a
    * listener whose class name is <code>&lt;event&gt;Listener</code>. The dispatcher found must have a no-arg constructor.
    * 
    * @param eventClass the class of event for which the listener is to be removed.
    * @param listener the listener to add.
    * @return true if the listener was added, false otherwise.
    */
   public <E extends Event, L extends EventListener> boolean addListener(Class<E> eventClass, L listener) {
      return addListener(eventClass, listener, (EventDispatcher<E, L>)lookupDispatcher(eventClass));
   }

   @SuppressWarnings("unchecked")
   private EventDispatcher<?, ?> lookupDispatcher(Class<?> eventClass) {
      String eventClassName = eventClass.getName();

      //----------------------------------------------------------------------------------------------------------------
      // Check if there is a dispatcher class explicitly declared in the same package.
      //----------------------------------------------------------------------------------------------------------------
      if ( eventClassName.endsWith("Event") ) {
         try {
            return (EventDispatcher) TypeUtil.instantiateClass(eventClassName + "Dispatcher");
         } catch (TypeNotFoundException ignoreEx) {}
      }

      //----------------------------------------------------------------------------------------------------------------
      // Check superclass hierarchy for registered dispatcher.
      //----------------------------------------------------------------------------------------------------------------
      Class<?> superclass = eventClass.getSuperclass();
      while (superclass != null) {
         EventInfo<?, ?> superclassEventInfo = getEventInfo(superclass);
         if (superclassEventInfo != null) return superclassEventInfo.getDispatcher();
         superclass = superclass.getSuperclass();
      }

      //----------------------------------------------------------------------------------------------------------------
      // Check super-interface hierarchy for registered dispatcher, via superclass hierarchy upwards.
      //----------------------------------------------------------------------------------------------------------------
      superclass = eventClass;
      while (superclass != null) {
         for (Class<?> superinterface : superclass.getInterfaces()) {
            EventInfo<?, ?> superinterfaceEventInfo = getEventInfo(superinterface);
            if (superinterfaceEventInfo != null) return superinterfaceEventInfo.getDispatcher();
         }
         superclass = superclass.getSuperclass();
      }

      throw new IllegalArgumentException("No associated event dispatcher could be found on the classpath because the event class ["+eventClassName+"] was not named <event>Event and none of its superclasses or supe-rinterfaces have registered dispatchers either.");
   }

   private Class<?> lookupEventClass(Object listener) {
      String listenerClassName = listener.getClass().getName();
      if ( !listenerClassName.endsWith("Listener") ) {
         throw new IllegalArgumentException("No associated event type could be found on the classpath because the event listener ["+listenerClassName+"] was not named <event>Listener");
      }

      return TypeUtil.loadClass(listenerClassName.substring(0, listenerClassName.length()-8)+"Event");
   }

   /**
    * Removes a listener from the set of listeners for the given class of event.
    * 
    * @param eventClass
    *           the class of event for which the listener is to be removed.
    * @param listener
    *           the listener to remove.
    * @return true if the listener was present and was removed, false otherwise.
    */
   public <E, L> boolean removeListener(Class<E> eventClass, L listener) {
      EventInfo<?, ?> eventInfo = eventListenerMap.get(eventClass);

      return (eventInfo != null && eventInfo.getListeners().remove(listener));
   }

   /**
    * Removes a listener from the set of listeners for the given class of event. The same package is automatically searched for
    * the associated event type called <code>&lt;event&gt;Event</code> given a
    * listener whose class name is <code>&lt;event&gt;Listener</code>. The dispatcher found must have a no-arg constructor.
    * 
    * @param listener the listener to add.
    * @return true if the listener was added, false otherwise.
    */
   public <L> boolean removeListener(L listener) {
      return removeListener(lookupEventClass(listener), listener);
   }
   
   /**
    * Removes all listener from the event source.
    */
   public boolean removeAllListeners() {
      boolean wasEmpty = eventListenerMap.isEmpty();
      eventListenerMap.clear();
      return !wasEmpty;
   }

   /**
    * Sets the listeners for the given class of event. Any previous listeners for the given class of event will be removed.
    * 
    * @param eventClass the class of event for which the listeners are to be added.
    * @param listeners  the listeners to be added.
    * @param dispatcher the dispatcher for the given event and listener types.
    * @return true if the listeners were added, false otherwise.
    */
   public <E extends Event, L extends EventListener> boolean setListeners(Class<E> eventClass, List<L> listeners, EventDispatcher<E, L> dispatcher) {
      EventInfo<E, L> newEventInfo = new EventInfo<>(dispatcher, Collections.synchronizedSet(new LinkedHashSet<L>()));
      EventInfo<E, L> eventInfo = (EventInfo<E, L>) eventListenerMap.putIfAbsent(eventClass, newEventInfo);
      if (eventInfo == null) {
         eventInfo = newEventInfo;
      }

      eventInfo.getListeners().clear();
      return eventInfo.getListeners().addAll(listeners);
   }

   /**
    * Returns the set of listeners for a given class of event.
    * 
    * @param eventClass
    *           the class of event for which the listeners are to be returned.
    * @param listenerClass
    *           the class of listeners to be returned.
    * @return the set of listeners for the given eventClass, guaranteed to be non-null.
    */
   public <E, L> Set<L> getListeners(Class<E> eventClass, Class<L> listenerClass) {
      EventInfo<?, ?> eventInfo = eventListenerMap.get(eventClass);

      return (Set<L>)(eventInfo == null ?  Collections.emptySet() : eventInfo.getListeners());
   }

   @SuppressWarnings("unchecked")
   private <E extends Event, L extends EventListener> EventInfo<E, L> lookupEventInfo(Class<E> eventClass) {
      String eventClassName = eventClass.getName();

      //----------------------------------------------------------------------------------------------------------------
      // Check if there is event information already registered for the given event?
      //----------------------------------------------------------------------------------------------------------------
      EventInfo<E, L> eventInfo = getEventInfo(eventClass);
      if (eventInfo != null) return eventInfo;

      //----------------------------------------------------------------------------------------------------------------
      // Check superclass hierarchy for registered event information.
      //----------------------------------------------------------------------------------------------------------------
      Class<?> superclass = eventClass.getSuperclass();
      while (superclass != null) {
         EventInfo<E, L> superclassEventInfo = getEventInfo(superclass);
         if (superclassEventInfo != null) return superclassEventInfo;
         superclass = superclass.getSuperclass();
      }

      //----------------------------------------------------------------------------------------------------------------
      // Check super-interface hierarchy for registered dispatcher, via superclass hierarchy upwards.
      //----------------------------------------------------------------------------------------------------------------
      superclass = eventClass;
      while (superclass != null) {
         for (Class<?> superinterface : superclass.getInterfaces()) {
            EventInfo<E, L> superinterfaceEventInfo = getEventInfo(superinterface);
            if (superinterfaceEventInfo != null) return superinterfaceEventInfo;
         }
         superclass = superclass.getSuperclass();
      }

      return null;
   }

   /**
    * Dispatches an event through the configured event dispatcher and listeners.
    * 
    * @param event the event to be dispatched.
    */
   public <E extends Event, L extends EventListener> void dispatchEvent(E event) {
      Assert.notNull(event, "The event may not be nul");
      EventInfo<E, L> eventInfo = (EventInfo<E, L>)lookupEventInfo(event.getClass());
      if (eventInfo == null) {
         return;
      }
      
      Set<?> listeners = eventInfo.getListeners();
      synchronized (listeners) {
         for (EventListener listener : eventInfo.getListeners()) {
            try {
               eventInfo.getDispatcher().dispatchEvent(event, (L)listener);
            } catch (Exception ex) {
// TODO:              LOG.warn("Caught and ignored exception in event dispatch loop [event="+event+", listener="+listener+"]: ", ex);
            }
         }
      }
   }
}
