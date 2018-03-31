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
