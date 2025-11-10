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

import org.beanplanet.core.lang.TypeUtil;

import java.lang.reflect.Proxy;

/**
 * The default <code>{@link ProxyFactory}</code> which should be used directly by clients.
 * 
 * <p>
 * This implementation creates either a standard Java 1.3+ dynamic <code>{@link Proxy}</code> or a <a
 * href="https://bytebuddy.net">ByteBuddy</a> proxy. Where a proxy based solely on java interfaces is required a
 * Java dynamic proxy is sufficient; for proxies via a concrete class a <a href="https://bytebuddy.net">ByteBuddy</a>
 * proxy is created.
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
