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
import org.beanplanet.core.lang.TypeUtil;

import java.lang.reflect.Proxy;

import static org.beanplanet.core.lang.TypeUtil.areAllInterfaces;

/**
 * The default <code>{@link ProxyFactory}</code> which should be used directly by clients.
 * 
 * <p>
 * This implementation creates either a standard Java 1.3+ dynamic <code>{@link Proxy}</code> or a <a
 * href="http://cglib.sourceforge.net/">CGLIB</a> proxy. Where a proxy based solely on java interfaces is required a
 * Java dynamic proxy is sufficient; for proxies via a concrete class a CGLIB proxy is created.
 * </p>
 * 
 * @author Gary Watson
 */
@SuppressWarnings("unchecked")
public class DefaultProxyFactory implements ProxyFactory {
   protected static final DefaultProxyFactory instance;
   protected ProxyFactoryConfig proxyfactoryConfig;
   protected static final ProxyFactory interfaceBasedProxyFactory;
   protected static ProxyFactory subclassCapableProxyFactory = null;

   private static String subclassProxyFactoryLoadProblem;

   static {
      instance = new DefaultProxyFactory();
      interfaceBasedProxyFactory = new JavaProxyFactory();

      try {
         subclassProxyFactoryLoadProblem = "This proxy factory ["+DefaultProxyFactory.class.getName()+"] is unable to proxy concrete types "
                                           + "because CGLib was not detected on the classpath. To rectify this please add the CGLib library (available from http://cglib.sourceforge.net/) "
                                           + "add the beanplanet-proxy module library (available from http://www.beanplanet.org/) to this application.";
         // Determine if CGLib library is on the classpath.
         TypeUtil.loadClass("net.sf.cglib.proxy.Enhancer");

         subclassProxyFactoryLoadProblem = "This proxy factory ["+DefaultProxyFactory.class.getName()+"] is unable to proxy concrete types "
                                           + "because the BeanPlanet CGLib proxy support library was not detected on the classpath. To rectify this please "
                                           + "add the beanplanet-proxy module library (available from http://www.beanplanet.org/) to this application.";

         // Determine if beanplanet-proxy module (with CGLib support) is on the classpath also.
         Class<ProxyFactory> proxyFactoryClass = (Class<ProxyFactory>) TypeUtil
               .loadClass("org.beanplanet.proxy.cglib.CGLibProxyFactory");
         subclassCapableProxyFactory = proxyFactoryClass.newInstance();
         subclassProxyFactoryLoadProblem = null;
      } catch (Exception ignoreEx) {
      }
   }

   public DefaultProxyFactory() {
      this(new ProxyFactoryConfig());
   }

   public DefaultProxyFactory(ProxyFactoryConfig proxyFactoryConfig) {
      setProxyfactoryConfig(proxyFactoryConfig);
   }

   /**
    * @return the proxyfactoryConfig
    */
   public ProxyFactoryConfig getProxyfactoryConfig() {
      return proxyfactoryConfig;
   }

   /**
    * @param proxyfactoryConfig the proxyfactoryConfig to set
    */
   public void setProxyfactoryConfig(ProxyFactoryConfig proxyfactoryConfig) {
      this.proxyfactoryConfig = proxyfactoryConfig;
   }

   public static DefaultProxyFactory getInstance() {
      return instance;
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
    * @param proxyType the type of the proxy to create; if an interface is specified the proxy instance created will
    *        implement the interface and if a concrete class is specified the proxy instance will be of a type that
    *        extends the concrete class. May not be null.
    * @param interceptor the method call interceptor which will handle invocations of methods on the proxy object. May
    *        not be null.
    * @return a proxy object, which extends the specified superclass type or implements the specified interface type.
    */
   public <P> P createProxy(Class<P> proxyType, MethodCallInterceptor interceptor) throws ProxyException {
      return ProxyUtil.createProxy(this, proxyType, interceptor);
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
    * @param proxyType the type of the proxy to create; if an interface is specified the proxy instance created will
    *        implement the interface and if a concrete class is specified the proxy instance will be of a type that
    *        extends the concrete class. May not be null.
    * @param interceptor the method call interceptor which will handle invocations of methods on the proxy object. May
    *        not be null.
    * @param exceptionHandler the handler of any exceptions throw by the interceptor. May be null and if not specified
    *        thrown exceptions will be propagated up to the caller of the method on the proxy.
    * @return a proxy object, which extends the specified superclass type or implements the specified interface type.
    */
   public <P> P createProxy(Class<P> proxyType, MethodCallInterceptor interceptor, ExceptionHandler exceptionHandler)
         throws ProxyException {
      return ProxyUtil.createProxy(this, proxyType, interceptor, exceptionHandler);
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
   public <T> T createProxy(ClassLoader classLoader, Class<T> proxyType, MethodCallInterceptor interceptor,
         ExceptionHandler exceptionHandler) throws ProxyException {
      Assert.notNull(proxyType, "The proxy type may not be null");
      Assert.notNull(interceptor, "The method call interceptor may not be null");

      boolean proxyingConcreteType = !proxyType.isInterface() || proxyfactoryConfig.getProxyTargetClass();

      if (proxyingConcreteType && subclassCapableProxyFactory == null) {
         throw new UnsupportedProxyOperationException(subclassProxyFactoryLoadProblem);
      }

      if (proxyingConcreteType) {
         return subclassCapableProxyFactory.createProxy(classLoader, proxyType, interceptor, exceptionHandler);
      }
      else {
         return interfaceBasedProxyFactory.createProxy(classLoader, proxyType, interceptor, exceptionHandler);
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
      Assert.isTrue(proxyInterfaces != null && proxyInterfaces.length > 0,
            "One or more proxy interfaces must be specified");
      Assert.isTrue(proxyInterfaces == null
                    || (proxyInterfaces.length > 0 && areAllInterfaces(proxyInterfaces)),
            "Only interface types may be specified as proxy interfaces");
      Assert.notNull(interceptor, "The method call interceptor may not be null");

      boolean proxyingConcreteType = proxyfactoryConfig.getProxyTargetClass();

      if (proxyingConcreteType && subclassCapableProxyFactory == null) {
         throw new UnsupportedProxyOperationException(subclassProxyFactoryLoadProblem);
      }

      if (proxyingConcreteType) {
         return subclassCapableProxyFactory.createProxy(classLoader, proxyInterfaces, interceptor, exceptionHandler);
      }
      else {
         return interfaceBasedProxyFactory.createProxy(classLoader, proxyInterfaces, interceptor, exceptionHandler);
      }
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
   public <T> T createProxy(ClassLoader classLoader, Class<T> superClass, Class<T> proxyInterface,
         MethodCallInterceptor interceptor, ExceptionHandler exceptionHandler) throws ProxyException {
      Assert.notNull(superClass, "The superclass to extend may not be null");
      Assert.notNull(proxyInterface, "The proxy additional interface may not be null");
      Assert.notNull(interceptor, "The method call interceptor may not be null");

      return subclassCapableProxyFactory.createProxy(classLoader, superClass, proxyInterface, interceptor,
            exceptionHandler);
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
      Assert.isTrue(proxyInterfaces != null && proxyInterfaces.length > 0,
            "One or more proxy interfaces must be specified");
      Assert.isTrue(proxyInterfaces == null
                    || (proxyInterfaces.length > 0 && areAllInterfaces(proxyInterfaces)),
            "Only interface types may be specified as proxy interfaces");
      Assert.notNull(interceptor, "The method call interceptor may not be null");

      return subclassCapableProxyFactory.createProxy(classLoader, superClass, proxyInterfaces, interceptor,
            exceptionHandler);
   }
}
