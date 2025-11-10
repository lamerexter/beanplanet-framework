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
package org.beanplanet.core.models.lifecycle;

/**
 * A simple life-cycle interface for services that wish to be
 * notified when it is time to begin initialisation or when the services are being started-up.
 * 
 * <p>Understood by many service factories within the <a href="http://www.beanplanet.org">BeanPlanet Framework</a>
 * which create services that are to be made aware of their startup duties.<p>
 * 
 * <p>Implement this interface to nicely encapsulate startup behaviour which will be initiated at extension points within
 * the <a href="http://www.beanplanet.org">BeanPlanet Framework</a> or your own frameworks.</p>
 * 
 * <p>Unless specified by specific implementations, life-cycle methods of this interface are <b>not</b> thread safe. Callers
 * in a multi-threaded environment should synchronise access to these methods accordingly.</p>
 * @see ShutdownLifecycle
 */
public interface StartupLifecycle
{
   /**
    * Invoked when this object is started-up.
    * 
    * <p>Implementers should encapsulate startup behaviour in this method, perhaps creating or referencing the required
    * system resources for use later.</p>
    * 
    * <p>While this method does not force this behaviour on implementations, it is
    * strongly recommended that implementations are capable of handling successive calls to this method: either by
    * dealing with the first and ignoring subsequent calls or by dealing with the first and then shutting down, if appropriate, before
    * dealing with each successive call. This will ensure graceful restart behaviour.</p>
    * 
    * @exception Exception if an error occurs during startup to
    * let the receiver know the component could not be started.
    */
   public void startup() throws Exception;
}