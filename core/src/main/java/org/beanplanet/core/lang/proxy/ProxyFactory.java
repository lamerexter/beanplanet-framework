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

import static org.beanplanet.core.lang.Assert.isTrue;
import static org.beanplanet.core.lang.TypeUtil.areAllInterfaces;

/**
 * Definition of a factory for creating dynamic proxies to objects at runtime.
 * <p>
 * <p>
 * Implementations may be able to create proxies to abstract types, via a known interface or set of interfaces (as is
 * the case of the Java <code>{@link java.lang.reflect.Proxy}</code> utility), or be able to create proxies to concrete
 * types (existing instances) at runtime, or both.
 * </p>
 *
 * @author Gary Watson
 */
public interface ProxyFactory {
    /**
     * Creates a dynamic proxy instance which either extends the specified concrete superclass or implements it. Invocations
     * of methods on the proxy object will be passed to the method call handler provided.
     * <p>
     * <p>This default implementation calls {@link #dynamicProxy(Class, Class[], Object[], Class[], MethodCallInterceptor)} with
     * the classloader derived from the superclass specified.
     * </p>
     *
     * @param superclass a concrete super class type the created proxy is required to extend. May not be null.
     * @param handler    the method call handler which will handle invocations of methods on the proxied object. May
     *                   not be null.
     * @return a proxy object, which extends the specified superclass and delegates all method calls to the handler specified.
     */
    @SuppressWarnings("unchecked")
    default <T> T dynamicProxy(Class<T> superclass, MethodCallInterceptor handler) throws ProxyException {
        isTrue(superclass != null, "The superclass may not be null");
        return superclass.isInterface() ?
               (T)dynamicProxy(new Class<?>[] { superclass }, handler)
               : dynamicProxy(superclass, null, null, handler);
    }

    /**
     * Creates a dynamic proxy instance which extends the specified superclass and implements all of the additional interfaces. Invocations
     * of methods on the proxy object will be passed to the method call handler provided.
     * <p>
     * <p>This default implementation simply calls {@link #dynamicProxy(Class, Class[], Object[], MethodCallInterceptor)} with
     * the classloader derived from the superclass specified.
     * </p>
     *
     * @param superclass a concrete super class type the created proxy is required to extend. May not be null.
     * @param proxyInterfaces the interfaces the proxy is required to implement. May be null; if specified must contain
     *                        at least one element and all elements must be interface types.
     * @param handler    the method call handler which will handle invocations of methods on the proxied object. May
     *                   not be null.
     * @return a proxy object, which extends the specified superclass and delegates all method calls to the handler specified.
     * @see #dynamicProxy(ClassLoader, Class, Class[], MethodCallInterceptor)
     */
    default <T> T dynamicProxy(Class<T> superclass, Class<?> proxyInterfaces[], MethodCallInterceptor handler) throws ProxyException {
        return dynamicProxy(TypeUtil.getClassLoaderInContext(superclass), superclass, proxyInterfaces, handler);
    }

    /**
     * Creates a dynamic proxy instance which extends the specified superclass and implements all of the additional interfaces. Invocations
     * of methods on the proxy object will be passed to the method call handler provided.
     * <p>
     * <p>This default implementation simply calls {@link #dynamicProxy(ClassLoader, Class, Class[], Object[], Class[], MethodCallInterceptor)} with
     * the classloader derived from the superclass specified.
     * </p>
     *
     * @param superclass      a concrete super class type the created proxy is required to extend. May not be null.
     * @param ctorArgTypes    the types of the arguments of the constructor to be used in the construction of the new
     *                        proxy/superclass, which may be null to indicate the no-arg constructor should be used.  If
     *                        specified, all argument types must be non-null.
     * @param ctorArgs        arguments to be passed to the constructor of the new proxy/superclass for its
     *                        construction, which may be null.  In addition, one or more of the arguments may be null.
     * @param proxyInterfaces the interfaces the proxy is required to implement. May be null; if specified must contain
     *                        at least one element and all elements must be interface types.
     * @param handler         the method call handler which will handle invocations of methods on the proxied object. May
     *                        not be null.
     * @return a proxy object, which extends the specified superclass and delegates all method calls to the handler specified.
     * @see #dynamicProxy(ClassLoader, Class, Class[], Object[], Class[], MethodCallInterceptor)
     */
    default <T> T dynamicProxy(Class<T> superclass, Class<?> ctorArgTypes[], Object ctorArgs[], Class<?> proxyInterfaces[], MethodCallInterceptor handler) throws ProxyException {
        return dynamicProxy(TypeUtil.getClassLoaderInContext(superclass), superclass, ctorArgTypes, ctorArgs, proxyInterfaces, handler);
    }

    /**
     * Creates a dynamic proxy instance which extends the specified superclass. Invocations
     * of methods on the proxy object will be passed to the method call handler provided.
     * <p>
     * <p>This default implementation simply calls {@link #dynamicProxy(ClassLoader, Class, Class[], Object[], MethodCallInterceptor)} with
     * the classloader derived from the superclass specified.
     * </p>
     *
     * @param superclass   a concrete super class type the created proxy is required to extend. May not be null.
     * @param ctorArgTypes the types of the arguments of the constructor to be used in the construction of the new
     *                     proxy/superclass, which may be null to indicate the no-arg constructor should be used.  If
     *                     specified, all argument types must be non-null.
     * @param ctorArgs     arguments to be passed to the constructor of the new proxy/superclass for its
     *                     construction, which may be null.  In addition, one or more of the arguments may be null.
     * @param handler      the method call handler which will handle invocations of methods on the proxied object. May
     *                     not be null.
     * @return a proxy object, which extends the specified superclass and delegates all method calls to the handler specified.
     * @see #dynamicProxy(ClassLoader, Class, Class[], Object[], MethodCallInterceptor)
     */
    default <T> T dynamicProxy(Class<T> superclass, Class<?> ctorArgTypes[], Object ctorArgs[], MethodCallInterceptor handler) throws ProxyException {
        return dynamicProxy(TypeUtil.getClassLoaderInContext(superclass), superclass, ctorArgTypes, ctorArgs, handler);
    }

    /**
     * Creates a dynamic proxy which extends the specified superclass and implements all of the additional interfaces. Invocations
     * of methods on the proxy object will be passed to the method call handler provided.
     * <p>
     * <p>This default implementation simply calls {@link #dynamicProxy(ClassLoader, Class, Class[], Object[], Class[], MethodCallInterceptor)} with
     * the classloader derived from the superclass specified.
     * </p>
     *
     * @param classloader  the class loader under which the new proxy instance will be created. May be null.
     * @param superclass   a concrete super class type the created proxy is required to extend. May not be null.
     * @param proxyInterfaces the interfaces the proxy is required to implement. May be null; if specified must contain
     *                        at least one element and all elements must be interface types.
     * @param handler      the method call handler which will handle invocations of methods on the proxied object. May
     *                     not be null.
     * @return a proxy object, which extends the specified superclass and delegates all method calls to the handler specified.
     * @see #dynamicProxy(ClassLoader, Class, Class[], Object[], Class[], MethodCallInterceptor)
     */
    default <T> T dynamicProxy(ClassLoader classloader, Class<T> superclass, Class<?> proxyInterfaces[], MethodCallInterceptor handler) throws ProxyException {
        return dynamicProxy(classloader, superclass, null, null, proxyInterfaces, handler);
    }

    /**
     * Creates a dynamic proxy which extends the specified superclass. Invocations
     * of methods on the proxy object will be passed to the method call handler provided.
     * <p>
     * <p>This default implementation simply calls {@link #dynamicProxy(ClassLoader, Class, Class[], Object[], Class[], MethodCallInterceptor)} with
     * the classloader derived from the superclass specified.
     * </p>
     *
     * @param classloader  the class loader under which the new proxy instance will be created. May be null.
     * @param superclass   a concrete super class type the created proxy is required to extend. May not be null.
     * @param ctorArgTypes the types of the arguments of the constructor to be used in the construction of the new
     *                     proxy/superclass, which may be null to indicate the no-arg constructor should be used.  If
     *                     specified, all argument types must be non-null.
     * @param ctorArgs     arguments to be passed to the constructor of the new proxy/superclass for its
     *                     construction, which may be null.  In addition, one or more of the arguments may be null.
     * @param handler      the method call handler which will handle invocations of methods on the proxied object. May
     *                     not be null.
     * @return a proxy object, which extends the specified superclass and delegates all method calls to the handler specified.
     * @see #dynamicProxy(ClassLoader, Class, Class[], Object[], Class[], MethodCallInterceptor)
     */
    default <T> T dynamicProxy(ClassLoader classloader, Class<T> superclass, Class<?> ctorArgTypes[], Object ctorArgs[], MethodCallInterceptor handler) throws ProxyException {
        return dynamicProxy(classloader, superclass, ctorArgTypes, ctorArgs, null, handler);
    }

    /**
     * Creates a dynamic proxy that implements all of the specified interfaces. Invocations
     * of methods on the proxy object will be passed to the method call handler provided.
     *
     * <p>This default implementation simply calls {@link #dynamicProxy(ClassLoader, Class[], MethodCallInterceptor)} with
     * the classloader derived from the superclass specified.
     * </p>
     *
     * @param proxyInterfaces the interfaces the proxy is required to implement. May not be null and must contain
     *                        at least one element and all elements must be interface types.
     * @param handler         the method call handler which will handle invocations of methods on the proxied object. May
     *                        not be null.
     * @return a proxy object, which extends the specified superclass, implements the specified additional interfaces and
     * delegates all method calls to the handler specified.
     */
    default <T> T dynamicProxy(Class<?> proxyInterfaces[], MethodCallInterceptor handler) throws ProxyException {
        isTrue(proxyInterfaces != null
               && proxyInterfaces.length > 0
               && areAllInterfaces(proxyInterfaces),"Only interface types may be specified as proxy interfaces");
        return dynamicProxy(TypeUtil.getClassLoaderInContext(proxyInterfaces[0]), proxyInterfaces, handler);

    }

    /**
     * Creates a dynamic proxy that implements all of the specified interfaces. Invocations
     * of methods on the proxy object will be passed to the method call handler provided.
     *
     * @param classloader     the class loader under which the new proxy instance will be created. May be null, in which
     *                        case the classloader will be derived from the first proxied interface specified.
     * @param proxyInterfaces the interfaces the proxy is required to implement. May not be null and must contain
     *                        at least one element and all elements must be interface types.
     * @param handler         the method call handler which will handle invocations of methods on the proxied object. May
     *                        not be null.
     * @return a proxy object, which extends the specified superclass, implements the specified additional interfaces and
     * delegates all method calls to the handler specified.
     */
    <T> T dynamicProxy(ClassLoader classloader, Class<?> proxyInterfaces[], MethodCallInterceptor handler) throws ProxyException;

    /**
     * Creates a dynamic proxy that extends the specified superclass and implements all of the additional interfaces. Invocations
     * of methods on the proxy object will be passed to the method call handler provided.
     *
     * @param classloader     the class loader under which the new proxy instance will be created. May be null, in which
     *                        case the classloader will be derived from the superclass specified.
     * @param superclass      a concrete super class type the created proxy is required to extend. May not be null.
     * @param ctorArgTypes    the types of the arguments of the constructor to be used in the construction of the new
     *                        proxy/superclass, which may be null to indicate the no-arg constructor should be used.  If
     *                        specified, all argument types must be non-null.
     * @param ctorArgs        arguments to be passed to the constructor of the new proxy/superclass for its
     *                        construction, which may be null.  In addition, one or more of the arguments may be null.
     * @param proxyInterfaces the interfaces the proxy is required to implement. May be null; if specified must contain
     *                        at least one element and all elements must be interface types.
     * @param handler         the method call handler which will handle invocations of methods on the proxied object. May
     *                        not be null.
     * @return a proxy object, which extends the specified superclass, implements the specified additional interfaces and
     * delegates all method calls to the handler specified.
     */
    <T> T dynamicProxy(ClassLoader classloader, Class<T> superclass, Class<?> ctorArgTypes[], Object ctorArgs[], Class<?> proxyInterfaces[], MethodCallInterceptor handler) throws ProxyException;
}
