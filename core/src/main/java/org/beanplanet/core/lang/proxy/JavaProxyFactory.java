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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import org.beanplanet.core.lang.Assert;
import org.beanplanet.core.lang.TypeUtil;

import static org.beanplanet.core.lang.TypeUtil.areAllInterfaces;
import static org.beanplanet.core.lang.TypeUtil.getClassLoaderInContext;

public class JavaProxyFactory extends ProxyFactoryBase {

   /**
    * Internal method designed to be implemented by subclasses to create a proxy guaranteed to extend the specified
    * superclass and/or implement the additional interfaces. Invocations of methods on the proxy object will be passed
    * to the method call interceptor provided.
    * 
    * <p>
    * The interceptor may choose to simply return a value, pass on the call to some target object or perform the
    * necessary work itself in order to complete the method call, depending on the implementation. Any exceptions thrown
    * by the interceptor will be passed to the exception handler provided, if any.
    * <p>
    * 
    * @param classLoader the class loader under which the new proxy instance will be created. Guaranteed not to be null.
    * @param superClass a concrete super class type the created proxy is required to extend. May be null if
    *        <code>proxyInterfaces</code> have been specified.
    * @param proxyInterfaces the interfaces the proxy is required to implement. May not be null and must contain at
    *        least one interface unless <code>superClass</code> has been specified.
    * @param interceptor the method call interceptor which will handle invocations of methods on the proxy object. May
    *        not be null.
    * @return a proxy object, implementing the specified interface and/or extending the specified superclass.
    */
   @Override
   protected Object createProxyInternal(ClassLoader classLoader, Class<?> superClass, Class<?> proxyInterfaces[],
         MethodCallInterceptor interceptor) throws ProxyException {
      Assert.notNull(classLoader, "The classloader may not be null");
      Assert.isTrue(superClass != null || (proxyInterfaces != null && proxyInterfaces.length > 0),
            "The superclass to extend or one or more proxy interfaces may not be null");
      Assert.isTrue(proxyInterfaces == null
                    || (proxyInterfaces.length > 0 && areAllInterfaces(proxyInterfaces)),
            "Only interface types may be specified as proxy interfaces");

      if (superClass != null) {
         throw new UnsupportedProxyOperationException(
               "The JDK 1.3+ java.lang.Proxy implementation only supports interface types and may not be used to proxy concrete classes by subclassing");
      }

      if (classLoader == null) {
         classLoader = getClassLoaderInContext(proxyInterfaces[0]);
      }

      InvocationHandler invocationHandler = new InvocationHandlerToMethodCallInterceptorAdaptor(interceptor);
      Object proxy = Proxy.newProxyInstance(classLoader, proxyInterfaces, invocationHandler);
      return proxy;
   }
}
