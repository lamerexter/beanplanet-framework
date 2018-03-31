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
import org.beanplanet.core.lang.ExceptionHandler;

/**
 * Static utility class containing useful methods for creating proxy objects. In most cases, this utility class
 * delegates calls to similar methods in <code>{@link DefaultProxyFactory}</code>.
 * 
 * @author Gary Watson
 * 
 */
public class ProxyUtil {
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
    * @param factory the proxy factory to use to create the proxy object.
    * @param proxyType the type of the proxy to create; if an interface is specified the proxy instance created will
    *        implement the interface and if a concrete class is specified the proxy instance will be of a type that
    *        extends the concrete class. May not be null.
    * @param interceptor the method call interceptor which will handle invocations of methods on the proxy object. May
    *        not be null.
    * @return a proxy object, which extends the specified superclass type or implements the specified interface type.
    */
   public static final <P> P createProxy(ProxyFactory factory, Class<P> proxyType, MethodCallInterceptor interceptor)
         throws ProxyException {
      return createProxy(factory, proxyType, interceptor, null);
   }

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
    * @param factory the proxy factory to use to create the proxy object.
    * @param proxyType the type of the proxy to create; if an interface is specified the proxy instance created will
    *        implement the interface and if a concrete class is specified the proxy instance will be of a type that
    *        extends the concrete class. May not be null.
    * @param interceptor the method call interceptor which will handle invocations of methods on the proxy object. May
    *        not be null.
    * @param exceptionHandler the handler of any exceptions throw by the interceptor. May be null and if not specified
    *        thrown exceptions will be propagated up to the caller of the method on the proxy.
    * @return a proxy object, which extends the specified superclass type or implements the specified interface type.
    */
   public static final <P> P createProxy(ProxyFactory factory, Class<P> proxyType, MethodCallInterceptor interceptor,
         ExceptionHandler exceptionHandler) throws ProxyException {
      Assert.notNull(factory, "The proxy factory may not be null");
      Assert.notNull(proxyType, "The proxy type may not be null");
      Assert.notNull(interceptor, "The method call interceptor may not be null");

      return factory.createProxy(null, proxyType, interceptor, exceptionHandler);
   }

   /**
    * Creates a proxy guaranteed to implement or extend the specified <code>proxyType</code>, depending on whether an
    * interface or concrete class type is provided. Invocations of methods on the proxy object will be passed to the
    * method call interceptor provided.
    * 
    * <p>
    * This implementation delegates to
    * <code>{@link DefaultProxyFactory#createProxy(Class, MethodCallInterceptor, ExceptionHandler)}</code>.
    * </p>
    * 
    * 
    * @param proxyType the type of the proxy to create; if an interface is specified the proxy instance created will
    *        implement the interface and if a concrete class is specified the proxy instance will be of a type that
    *        extends the concrete class. May not be null.
    * @param interceptor the method call interceptor which will handle invocations of methods on the proxy object. May
    *        not be null.
    * @param exceptionHandler the handler of any exceptions throw by the interceptor. May be null and if not specified
    *        thrown exceptions will be propagated up to the caller of the method on the proxy.
    * @return a proxy object, which extends the specified superclass type or implements the specified interface type.
    * @see DefaultProxyFactory#createProxy(Class, MethodCallInterceptor, ExceptionHandler)
    */
   public static final <T> T createProxy(Class<T> proxyType, MethodCallInterceptor interceptor,
         ExceptionHandler exceptionHandler) throws ProxyException {
      return DefaultProxyFactory.getInstance().createProxy(null, proxyType, interceptor, exceptionHandler);
   }

   /**
    * Creates a proxy guaranteed to implement or extend the specified <code>proxyType</code>, depending on whether an
    * interface or concrete class type is provided. Invocations of methods on the proxy object will be passed to the
    * method call interceptor provided.
    * 
    * <p>
    * This implementation delegates to
    * <code>{@link DefaultProxyFactory#createProxy(ClassLoader, Class, MethodCallInterceptor, ExceptionHandler)}</code>.
    * </p>
    * 
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
    * @see DefaultProxyFactory#createProxy(ClassLoader, Class, MethodCallInterceptor, ExceptionHandler)
    */
   public static final <T> T createProxy(ClassLoader classLoader, Class<T> proxyType,
         MethodCallInterceptor interceptor, ExceptionHandler exceptionHandler) throws ProxyException {
      return DefaultProxyFactory.getInstance().createProxy(classLoader, proxyType, interceptor, exceptionHandler);
   }

   /**
    * Creates a proxy guaranteed to implement the specified interfaces. Invocations of methods on the proxy object will
    * be passed to the method call interceptor provided.
    * 
    * <p>
    * This implementation delegates to
    * <code>{@link DefaultProxyFactory#createProxy(ClassLoader, Class[], MethodCallInterceptor, ExceptionHandler)}</code>
    * .
    * </p>
    * 
    * @param classLoader the class loader under which the new proxy instance will be created. May be null.
    * @param proxyInterfaces the interfaces the proxy is required to implement. May not be null and must contain at
    *        least one interface.
    * @param interceptor the method call interceptor which will handle invocations of methods on the proxy object. May
    *        not be null.
    * @param exceptionHandler the handler of any exceptions throw by the interceptor. May be null and if not specified
    *        thrown exceptions will be propagated up to the caller of the method on the proxy.
    * @return a proxy object, which implements the specified interfaces.
    * @see DefaultProxyFactory#createProxy(ClassLoader, Class[], MethodCallInterceptor, ExceptionHandler)
    */
   public static final Object createProxy(ClassLoader classLoader, Class<?> proxyInterfaces[],
         MethodCallInterceptor interceptor, ExceptionHandler exceptionHandler) throws ProxyException {
      return DefaultProxyFactory.getInstance().createProxy(classLoader, proxyInterfaces, interceptor, exceptionHandler);
   }

   /**
    * Creates a proxy guaranteed to implement the specified interfaces. Invocations of methods on the proxy object will
    * be passed to the method call interceptor provided.
    * 
    * <p>
    * This implementation delegates to
    * <code>{@link DefaultProxyFactory#createProxy(ClassLoader, Class[], MethodCallInterceptor, ExceptionHandler)}</code>
    * .
    * </p>
    * 
    * @param proxyInterfaces the interfaces the proxy is required to implement. May not be null and must contain at
    *        least one interface.
    * @param interceptor the method call interceptor which will handle invocations of methods on the proxy object. May
    *        not be null.
    * @param exceptionHandler the handler of any exceptions throw by the interceptor. May be null and if not specified
    *        thrown exceptions will be propagated up to the caller of the method on the proxy.
    * @return a proxy object, which implements the specified interfaces.
    * @see DefaultProxyFactory#createProxy(ClassLoader, Class[], MethodCallInterceptor, ExceptionHandler)
    */
   public static final Object createProxy(Class<?> proxyInterfaces[], MethodCallInterceptor interceptor,
         ExceptionHandler exceptionHandler) throws ProxyException {
      return DefaultProxyFactory.getInstance().createProxy(null, proxyInterfaces, interceptor, exceptionHandler);
   }

   /**
    * Creates a proxy guaranteed to extend the specified superclass and implement the additional interface. Invocations
    * of methods on the proxy object will be passed to the method call interceptor provided.
    * 
    * <p>
    * This implementation delegates to
    * <code>{@link DefaultProxyFactory#createProxy(ClassLoader, Class, Class, MethodCallInterceptor, ExceptionHandler)}</code>
    * .
    * </p>
    * 
    * @param classLoader the class loader under which the new proxy instance will be created. May be null.
    * @param superClass a concrete super class type the created proxy is required to extend. May not be null.
    * @param proxyInterface an additional interface the proxy is required to implement. May not be null.
    * @param interceptor the method call interceptor which will handle invocations of methods on the proxy object. May
    *        not be null.
    * @param exceptionHandler the handler of any exceptions throw by the interceptor. May be null and if not specified
    *        thrown exceptions will be propagated up to the caller of the method on the proxy.
    * @return a proxy object, which extends the specified superclass and implements the specified additional interface.
    * @see DefaultProxyFactory#createProxy(ClassLoader, Class, MethodCallInterceptor, ExceptionHandler)
    */
   public static final <T> T createProxy(ClassLoader classLoader, Class<T> superClass, Class<T> proxyInterface,
         MethodCallInterceptor interceptor, ExceptionHandler exceptionHandler) throws ProxyException {
      return DefaultProxyFactory.getInstance().createProxy(classLoader, superClass, proxyInterface, interceptor,
            exceptionHandler);
   }

   /**
    * Creates a proxy guaranteed to extend the specified superclass and implement the additional interface. Invocations
    * of methods on the proxy object will be passed to the method call interceptor provided.
    * 
    * <p>
    * This implementation delegates to
    * <code>{@link DefaultProxyFactory#createProxy(ClassLoader, Class, Class, MethodCallInterceptor, ExceptionHandler)}</code>
    * .
    * </p>
    * 
    * @param superClass a concrete super class type the created proxy is required to extend. May not be null.
    * @param proxyInterface an additional interface the proxy is required to implement. May not be null.
    * @param interceptor the method call interceptor which will handle invocations of methods on the proxy object. May
    *        not be null.
    * @param exceptionHandler the handler of any exceptions throw by the interceptor. May be null and if not specified
    *        thrown exceptions will be propagated up to the caller of the method on the proxy.
    * @return a proxy object, which extends the specified superclass and implements the specified additional interface.
    * @see DefaultProxyFactory#createProxy(ClassLoader, Class, MethodCallInterceptor, ExceptionHandler)
    */
   public static final <T> T createProxy(Class<T> superClass, Class<T> proxyInterface,
         MethodCallInterceptor interceptor, ExceptionHandler exceptionHandler) throws ProxyException {
      return DefaultProxyFactory.getInstance().createProxy(null, superClass, proxyInterface, interceptor,
            exceptionHandler);
   }

   /**
    * Creates a proxy guaranteed to extend the specified superclass and implement the additional interfaces. Invocations
    * of methods on the proxy object will be passed to the method call interceptor provided.
    * 
    * <p>
    * This implementation delegates to
    * <code>{@link DefaultProxyFactory#createProxy(ClassLoader, Class, Class[], MethodCallInterceptor, ExceptionHandler)}</code>
    * .
    * </p>
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
    * @see DefaultProxyFactory#createProxy(ClassLoader, Class, Class[], MethodCallInterceptor, ExceptionHandler)
    */
   public static final Object createProxy(ClassLoader classLoader, Class<?> superClass, Class<?> proxyInterfaces[],
         MethodCallInterceptor interceptor, ExceptionHandler exceptionHandler) throws ProxyException {
      return DefaultProxyFactory.getInstance().createProxy(classLoader, superClass, proxyInterfaces, interceptor,
            exceptionHandler);
   }

   /**
    * Creates a proxy guaranteed to extend the specified superclass and implement the additional interfaces. Invocations
    * of methods on the proxy object will be passed to the method call interceptor provided.
    * 
    * <p>
    * This implementation delegates to
    * <code>{@link DefaultProxyFactory#createProxy(ClassLoader, Class, Class[], MethodCallInterceptor, ExceptionHandler)}</code>
    * .
    * </p>
    * 
    * @param superClass a concrete super class type the created proxy is required to extend. May not be null.
    * @param proxyInterfaces the interfaces the proxy is required to implement. May not be null and must contain at
    *        least one interface.
    * @param interceptor the method call interceptor which will handle invocations of methods on the proxy object. May
    *        not be null.
    * @param exceptionHandler the handler of any exceptions throw by the interceptor. May be null and if not specified
    *        thrown exceptions will be propagated up to the caller of the method on the proxy.
    * @return a proxy object, which extends the specified superclass and implements the specified additional interfaces.
    * @see DefaultProxyFactory#createProxy(ClassLoader, Class, Class[], MethodCallInterceptor, ExceptionHandler)
    */
   public static final Object createProxy(Class<?> superClass, Class<?> proxyInterfaces[],
         MethodCallInterceptor interceptor, ExceptionHandler exceptionHandler) throws ProxyException {
      return DefaultProxyFactory.getInstance().createProxy(null, superClass, proxyInterfaces, interceptor,
            exceptionHandler);
   }
}
