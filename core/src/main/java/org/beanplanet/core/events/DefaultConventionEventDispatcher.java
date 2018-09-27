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
