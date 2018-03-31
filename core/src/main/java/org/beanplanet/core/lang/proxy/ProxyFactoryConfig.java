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
 * Configuration object for <code>{@link ProxyFactory}</code> implementations.
 * 
 * @author Gary Watson
 * 
 */
public class ProxyFactoryConfig {
   /**
    * Whether the target class/object is to always be proxied via a subclass, even when just proxy interfaces are
    * specified or required.
    */
   protected boolean proxyTargetClass = false;
   /** Whether to decorate the generated proxy with a Proxy interface the client can see and manipulate. */
   protected boolean exposeProxy = true;

   public ProxyFactoryConfig() {
   }

   /**
    * @return the proxyTargetClass
    */
   public boolean getProxyTargetClass() {
      return proxyTargetClass;
   }

   /**
    * @param proxyTargetClass the proxyTargetClass to set
    */
   public void setProxyTargetClass(boolean proxyTargetClass) {
      this.proxyTargetClass = proxyTargetClass;
   }

   /**
    * @return the exposeProxy
    */
   public boolean getExposeProxy() {
      return exposeProxy;
   }

   /**
    * @param exposeProxy the exposeProxy to set
    */
   public void setExposeProxy(boolean exposeProxy) {
      this.exposeProxy = exposeProxy;
   }
}
