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
package org.beanplanet.core.lang.proxy;

/**
 * Defines a proxy capable of identifying its target. Useful for delayed or lazy target method invocation or method
 * interception where the target may also be swapped at runtime.
 * 
 * @author Gary Watson
 */
public interface Proxy<T> {
   /**
    * Returns the current target identified by the proxy.
    * 
    * @return the proxied target, which may be null.
    */
   T getTarget();

   /**
    * Sets the target identified by the proxy. Any existing target will be lost and it is the responsibility of the
    * caller to ensure the new target is capable of handling subsequent invocations.
    * 
    * @param target the new target to proxy, which must be compatible with the type expected and capable of receiving
    *        invocations.
    */
   <S extends T> void setTarget(S target);
}
