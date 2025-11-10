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


import org.beanplanet.core.lang.TypeUtil;
import org.beanplanet.core.logging.Logger;

import java.lang.reflect.Method;

/**
 * A dispatcher of registration events to interested parties.
 */
public class DefaultConventionEventDispatcher<E extends Event, L extends EventListener> implements EventDispatcher<E, L>, Logger {
    /**
     * Dynamically dispatches an event through a listener via on<i>Eventname</i>(<i>event</i>) method of the listener. For example, a <code>RegistrationEvent</code> is dispatched
     * to the <code>onRegistration(RegistrationEvent)</code> method.
     *
     * @param event    the event to be dispatched.
     * @param listener the listener through which the event should be dispatched.
     * @throws Exception if an error occurs dispatching the event.
     */
    public void dispatchEvent(E event, L listener) throws Exception {
        // Dynamically invoke the listener method
        String fqEventName = TypeUtil.getBaseName(event.getClass());
        if ( !fqEventName.endsWith("Event") ) {
            error(String.format("Unrecognised event %s ... ignored", event.getClass()));
            return;
        }

        String eventName = fqEventName.substring(0, fqEventName.lastIndexOf("Event"));
        Method dispatchMethod = listener.getClass().getMethod("on"+eventName, event.getClass());
        if ( dispatchMethod == null ) {
            error(String.format("Unable to dispatch event [class=%s] to listener [class=%s]: no event dispatch method [on%s] found", event.getClass(), listener.getClass(), eventName));
            return;
        }

        dispatchMethod.setAccessible(true);
        dispatchMethod.invoke(listener, event);
    }
}
