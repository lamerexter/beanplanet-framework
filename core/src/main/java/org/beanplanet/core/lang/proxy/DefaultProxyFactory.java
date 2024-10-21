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

import org.beanplanet.core.lang.TypeUtil;

import java.lang.reflect.Proxy;

/**
 * The default <code>{@link ProxyFactory}</code> which should be used directly by clients.
 * 
 * <p>
 * This implementation creates either a standard Java 1.3+ dynamic <code>{@link Proxy}</code> or a <a
 * href="https://bytebuddy.net">ByteBuddy</a> proxy. Where a proxy based solely on java interfaces is required a
 * Java dynamic proxy is sufficient; for proxies via a concrete class a CGLIB proxy is created.
 * </p>
 * 
 * @author Gary Watson
 */
@SuppressWarnings("unchecked")
public class DefaultProxyFactory implements ProxyFactory {
   protected static final DefaultProxyFactory instance;
   protected static final ProxyFactory interfaceBasedProxyFactory;
   protected static ProxyFactory subclassCapableProxyFactory = null;

   private static String subclassProxyFactoryLoadProblem;

   static {
      instance = new DefaultProxyFactory();
      interfaceBasedProxyFactory = new JavaProxyFactory();

      try {
         subclassProxyFactoryLoadProblem = "This proxy factory ["+DefaultProxyFactory.class.getName()+"] is unable to proxy concrete types "
                                           + "because ByteBuddy was not detected on the classpath. To rectify this please add the ByteBuddy library (available from https://bytebuddy.net or Maven Central) or "
                                           + "add the beanplanet-proxy module library to this application.";
         // Determine if concrete library is on the classpath.
         TypeUtil.loadClass("net.bytebuddy.ByteBuddy");

         // Determine if beanplanet-proxy module (with ByteBuddy support) is on the classpath also.
         Class<ProxyFactory> proxyFactoryClass = (Class<ProxyFactory>) TypeUtil
               .loadClass("org.beanplanet.proxy.bytebuddy.ByteBuddyProxyFactory");
         subclassCapableProxyFactory = proxyFactoryClass.newInstance();
      } catch (Exception ignoreEx) {
      }
   }

   public static DefaultProxyFactory getInstance() {
      return instance;
   }

   @Override
   public <T> T dynamicProxy(ClassLoader classloader, Class<?>[] proxyInterfaces, MethodCallInterceptor handler) throws ProxyException {
      return interfaceBasedProxyFactory.dynamicProxy(classloader, proxyInterfaces, handler);
   }

   @Override
   public <T> T dynamicProxy(ClassLoader classloader, Class<T> superclass, Class<?>[] ctorArgTypes, Object[] ctorArgs, Class<?>[] proxyInterfaces, MethodCallInterceptor handler) throws ProxyException {
      if (subclassCapableProxyFactory == null) {
         throw new UnsupportedProxyOperationException(subclassProxyFactoryLoadProblem);
      }

      return subclassCapableProxyFactory.dynamicProxy(classloader, superclass, ctorArgTypes, ctorArgs, proxyInterfaces, handler);
   }
}
