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

package org.beanplanet.core.models.lifecycle;

/**
 * A simple life-cycle interface for services that wish to be
 * notified when it is time to shutdown.
 * 
 * <p>Understood by many service factories within the <a href="http://www.beanplanet.org">BeanPlanet Framework</a>
 * which create services that are to be made aware of their shutdown duties.<p>
 * 
 * <p>Implement this interface to nicely encapsulate shutdown or destruction behaviour which will be initiated at extension points within
 * the <a href="http://www.beanplanet.org">BeanPlanet Framework</a> or your own frameworks.</p>
 * 
 * <p>Unless specified by specific implementations, life-cycle methods of this interface are <b>not</b> thread safe. Callers
 * in a multi-threaded environment should synchronise access to these methods accordingly.</p>
 * @see StartupLifecycle
 */
public interface ShutdownLifecycle
{
    /**
    * Invoked when this object is shutdown.
    *
    * <p>Implementers should encapsulate shutdown behaviour in this method, perhaps releasing system resources or references to
    * resources created at startup.</p>
    *
    * <p>While this method does not force this behaviour on implementations, it is
    * strongly recommended that implementations are capable of handling successive calls to this method: either by
    * dealing with the first and ignoring subsequent calls or by dealing with the first and then starting-up, if appropriate, before
    * dealing with each successive call. This will ensure graceful restart behaviour.</p>
    *
    * @exception Exception if an error occurs during startup to
    * let the receiver know the component could not be started
    */
    public void shutdown();
}