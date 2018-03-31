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

import org.beanplanet.core.lang.Assert;
import org.beanplanet.core.lang.ErrorPropagatingExceptionHandler;
import org.beanplanet.core.lang.ExceptionHandler;
import org.beanplanet.core.lang.TypeUtil;

/**
 * A useful base class for proxy factories, providing many of the 'plumbing' methods for creation of proxies.
 * 
 * @author Gary Watson
 * 
 */
public abstract class ProxyFactoryBase implements ProxyFactory {

   /**
    * Creates a proxy guaranteed to implement or extend the specified <code>proxyType</code>, depending on whether an
    * interface or concrete class type is provided. Invocations of methods on the proxy object will be passed to the
    * method call interceptor provided.
    * 
    * <p>
    * The interceptor may choose to simply return a value, pass on the call to some target object or perform the
    * necessary work itself in order to complete the method call, depending on the implementation. Any exceptions thrown
    * by the interceptor will be passed to the exception handler provided, if any.
    * <p>
    * 
    * @param classLoader the class loader under which the new proxy instance will be created. May be null.
    * @param proxyType the type of the proxy to create; if an interface is specified the proxy instance created will
    *        implement the interface and if a concrete class is specified the proxy instance will be of a type that
    *        extends the concrete class. May not be null.
    * @param interceptor the method call interceptor which will handle invocations of methods on the proxy object. May
    *        not be null.
    * @param exceptionHandler the handler of any exceptions throw by the interceptor. May be null and if not specified
    *        thrown exceptions will be propagated up to the caller of the method on the proxy.
    * @return a proxy object, which extends the specified superclass type or implements the specified interface type.
    */
   @SuppressWarnings("unchecked")
   public <T> T createProxy(ClassLoader classLoader, Class<T> proxyType, MethodCallInterceptor interceptor,
         ExceptionHandler exceptionHandler) throws ProxyException {
      Assert.notNull(proxyType, "The proxy type may not be null");
      Assert.notNull(interceptor, "The method call interceptor may not be null");

      if (proxyType.isInterface()) {
         return (T) createProxyUsingDefaults(classLoader, null, new Class<?>[] { proxyType }, interceptor,
               exceptionHandler);
      }
      else {
         return (T) createProxyUsingDefaults(classLoader, proxyType, (Class<?>[]) null, interceptor, exceptionHandler);
      }
   }

   /**
    * Creates a proxy guaranteed to implement the specified interfaces. Invocations of methods on the proxy object will
    * be passed to the method call interceptor provided.
    * 
    * <p>
    * The interceptor may choose to simply return a value, pass on the call to some target object or perform the
    * necessary work itself in order to complete the method call, depending on the implementation. Any exceptions thrown
    * by the interceptor will be passed to the exception handler provided, if any.
    * <p>
    * 
    * @param classLoader the class loader under which the new proxy instance will be created. May be null.
    * @param proxyInterfaces the interfaces the proxy is required to implement. May not be null and must contain at
    *        least one interface.
    * @param interceptor the method call interceptor which will handle invocations of methods on the proxy object. May
    *        not be null.
    * @param exceptionHandler the handler of any exceptions throw by the interceptor. May be null and if not specified
    *        thrown exceptions will be propagated up to the caller of the method on the proxy.
    * @return a proxy object, which implements the specified interfaces.
    */
   public Object createProxy(ClassLoader classLoader, Class<?> proxyInterfaces[], MethodCallInterceptor interceptor,
         ExceptionHandler exceptionHandler) throws ProxyException {
      Assert.notNull(proxyInterfaces, "The proxy interfaces may not be null");
      Assert.notNull(interceptor, "The method call interceptor may not be null");

      return createProxyUsingDefaults(classLoader, null, proxyInterfaces, interceptor, exceptionHandler);
   }

   /**
    * Creates a proxy guaranteed to extend the specified superclass and implement the additional interface. Invocations
    * of methods on the proxy object will be passed to the method call interceptor provided.
    * 
    * <p>
    * The interceptor may choose to simply return a value, pass on the call to some target object or perform the
    * necessary work itself in order to complete the method call, depending on the implementation. Any exceptions thrown
    * by the interceptor will be passed to the exception handler provided, if any.
    * <p>
    * 
    * @param classLoader the class loader under which the new proxy instance will be created. May be null.
    * @param superClass a concrete super class type the created proxy is required to extend. May not be null.
    * @param proxyInterface an additional interface the proxy is required to implement. May not be null.
    * @param interceptor the method call interceptor which will handle invocations of methods on the proxy object. May
    *        not be null.
    * @param exceptionHandler the handler of any exceptions throw by the interceptor. May be null and if not specified
    *        thrown exceptions will be propagated up to the caller of the method on the proxy.
    * @return a proxy object, which extends the specified superclass and implements the specified additional interface.
    */
   @SuppressWarnings("unchecked")
   public <T> T createProxy(ClassLoader classLoader, Class<T> superClass, Class<T> proxyInterface,
         MethodCallInterceptor interceptor, ExceptionHandler exceptionHandler) throws ProxyException {
      Assert.notNull(superClass, "The superclass to extend may not be null");
      Assert.notNull(proxyInterface, "The proxy additional interface may not be null");
      Assert.notNull(interceptor, "The method call interceptor may not be null");

      return (T) createProxy(classLoader, superClass, new Class<?>[] { proxyInterface }, interceptor, exceptionHandler);
   }

   /**
    * Creates a proxy guaranteed to extend the specified superclass and implement the additional interfaces. Invocations
    * of methods on the proxy object will be passed to the method call interceptor provided.
    * 
    * <p>
    * The interceptor may choose to simply return a value, pass on the call to some target object or perform the
    * necessary work itself in order to complete the method call, depending on the implementation. Any exceptions thrown
    * by the interceptor will be passed to the exception handler provided, if any.
    * <p>
    * 
    * @param classLoader the class loader under which the new proxy instance will be created. May be null.
    * @param superClass a concrete super class type the created proxy is required to extend. May not be null.
    * @param proxyInterfaces the interfaces the proxy is required to implement. May not be null and must contain at
    *        least one interface.
    * @param interceptor the method call interceptor which will handle invocations of methods on the proxy object. May
    *        not be null.
    * @param exceptionHandler the handler of any exceptions throw by the interceptor. May be null and if not specified
    *        thrown exceptions will be propagated up to the caller of the method on the proxy.
    * @return a proxy object, which extends the specified superclass and implements the specified additional interfaces.
    */
   public Object createProxy(ClassLoader classLoader, Class<?> superClass, Class<?> proxyInterfaces[],
         MethodCallInterceptor interceptor, ExceptionHandler exceptionHandler) throws ProxyException {
      Assert.notNull(superClass, "The superclass to extend may not be null");
      Assert.notNull(proxyInterfaces, "The proxy additional interfaces may not be null");
      Assert.notNull(interceptor, "The method call interceptor may not be null");
      return createProxyUsingDefaults(classLoader, superClass, proxyInterfaces, interceptor, exceptionHandler);
   }

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
    * @param classLoader the class loader under which the new proxy instance will be created. May be null.
    * @param superClass a concrete super class type the created proxy is required to extend. May be null if
    *        <code>proxyInterfaces</code> have been specified.
    * @param proxyInterfaces the interfaces the proxy is required to implement. May not be null and must contain at
    *        least one interface unless <code>superClass</code> has been specified.
    * @param interceptor the method call interceptor which will handle invocations of methods on the proxy object. May
    *        not be null.
    * @param exceptionHandler the handler of any exceptions throw by the interceptor. May be null and if not specified
    *        thrown exceptions will be propagated up to the caller of the method on the proxy.
    * @return a proxy object, which extends the specified superclass and/or implements the specified additional
    *         interfaces.
    */
   protected final Object createProxyUsingDefaults(ClassLoader classLoader, Class<?> superClass,
         Class<?> proxyInterfaces[], MethodCallInterceptor interceptor, ExceptionHandler exceptionHandler)
         throws ProxyException {

      // Default the classloader
      if (classLoader == null) {
         if (superClass != null) {
            classLoader = TypeUtil.getClassLoaderInContext(superClass);
         }
         else {
            classLoader = TypeUtil.getClassLoaderInContext(proxyInterfaces[0]);
         }
      }

      // Default the exception handler
      if (exceptionHandler == null) {
         exceptionHandler = new ErrorPropagatingExceptionHandler();
      }
      return createProxyInternal(classLoader, superClass, proxyInterfaces, new ExceptionHandlingMethodCallInterceptor(
            interceptor, exceptionHandler));

   }

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
    * @return a proxy object, which extends the specified superclass and/or implements the specified additional
    *         interfaces.
    */
   protected abstract Object createProxyInternal(ClassLoader classLoader, Class<?> superClass,
         Class<?> proxyInterfaces[], MethodCallInterceptor interceptor) throws ProxyException;
}
