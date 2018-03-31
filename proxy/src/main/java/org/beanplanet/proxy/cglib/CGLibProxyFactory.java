/******************************************************************************* 
 * Copyright 2004-2010 BeanPlanet Limited
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
 ******************************************************************************/
package org.beanplanet.proxy.cglib;

import java.lang.reflect.Modifier;

import net.sf.cglib.proxy.Enhancer;

import org.beanplanet.core.lang.Assert;
import org.beanplanet.core.lang.TypeUtil;
import org.beanplanet.core.lang.proxy.FinalClassProxyOperationException;
import org.beanplanet.core.lang.proxy.MethodCallInterceptor;
import org.beanplanet.core.lang.proxy.ProxyException;
import org.beanplanet.core.lang.proxy.ProxyFactoryBase;

public class CGLibProxyFactory extends ProxyFactoryBase {

   /**
    * Internal method designed to be implemented by subclasses to create a proxy
    * guaranteed to extend the specified superclass and/or implement the
    * additional interfaces. Invocations of methods on the proxy object will be
    * passed to the method call interceptor provided.
    * 
    * <p>
    * The interceptor may choose to simply return a value, pass on the call to
    * some target object or perform the necessary work itself in order to
    * complete the method call, depending on the implementation. Any exceptions
    * thrown by the interceptor will be passed to the exception handler
    * provided, if any.
    * <p>
    * 
    * @param classLoader
    *           the class loader under which the new proxy instance will be
    *           created. Guaranteed not to be null.
    * @param superClass
    *           a concrete super class type the created proxy is required to
    *           extend. May be null if <code>proxyInterfaces</code> have been
    *           specified.
    * @param proxyInterfaces
    *           the interfaces the proxy is required to implement. May not be
    *           null and must contain at least one interface unless
    *           <code>superClass</code> has been specified.
    * @param interceptor
    *           the method call interceptor which will handle invocations of
    *           methods on the proxy object. May not be null.
    * @return a proxy object, which extends the specified superclass and/or
    *         implements the specified additional interfaces.
    */
   protected Object createProxyInternal(ClassLoader classLoader, Class<?> superClass, Class<?> proxyInterfaces[],
         MethodCallInterceptor interceptor) throws ProxyException {
      Assert.notNull(classLoader, "The classloader may not be null");
      Assert.isTrue(superClass != null || (proxyInterfaces != null && proxyInterfaces.length > 0),
            "The superclass to extend or one or more proxy interfaces may not be null");
      Assert.isTrue(proxyInterfaces == null
                    || (proxyInterfaces.length > 0 && TypeUtil.areAllInterfaces(proxyInterfaces)),
            "Only interface types may be specified as proxy interfaces");

      if (superClass != null && Modifier.isFinal(superClass.getModifiers())) {
         throw new FinalClassProxyOperationException("Unable to proxy object [class=" + superClass.getClass().getName()
                                                     + "]: the class is marked as final.");
      }

      if (classLoader == null) {
         if (superClass != null) {
            classLoader = TypeUtil.getClassLoaderInContext(superClass);
         }
         else {
            classLoader = TypeUtil.getClassLoaderInContext(proxyInterfaces[0]);
         }
      }

      Enhancer e = new Enhancer();
      e.setClassLoader(classLoader);
      if (superClass != null) {
         e.setSuperclass(superClass);
      }
      if (proxyInterfaces != null) {
         e.setInterfaces(proxyInterfaces);
      }
      e.setCallback(new MethodInterceptorToMethodCallInterceptorAdaptor(interceptor));

      Object proxy = e.create();
      return proxy;
   }

}
