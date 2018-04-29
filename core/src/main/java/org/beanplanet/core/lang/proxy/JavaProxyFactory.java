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

import static org.beanplanet.core.lang.Assert.isTrue;
import static org.beanplanet.core.lang.Assert.notNull;
import static org.beanplanet.core.lang.TypeUtil.areAllInterfaces;
import static org.beanplanet.core.lang.TypeUtil.getClassLoaderInContext;

public class JavaProxyFactory implements ProxyFactory {
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
    @Override
    @SuppressWarnings("unchecked")
    public <T> T dynamicProxy(ClassLoader classloader, Class<?>[] proxyInterfaces, MethodCallInterceptor handler) throws ProxyException {
        isTrue(proxyInterfaces != null && proxyInterfaces.length > 0, "The proxy interfaces may not be null and must contain at least one interface");
        isTrue(areAllInterfaces(proxyInterfaces), "Only interface types may be specified as proxy interfaces");
        notNull(areAllInterfaces(proxyInterfaces), "Only interface types may be specified as proxy interfaces");

        if (classloader == null) {
            classloader = getClassLoaderInContext(proxyInterfaces[0]);
        }

        InvocationHandler invocationHandler = new InvocationHandlerToMethodCallInterceptorAdaptor(handler);
        return (T)Proxy.newProxyInstance(classloader, proxyInterfaces, invocationHandler);
    }

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
    @Override
    public <T> T dynamicProxy(ClassLoader classloader, Class<T> superclass, Class<?>[] ctorArgTypes, Object[] ctorArgs, Class<?>[] proxyInterfaces, MethodCallInterceptor handler) throws ProxyException {
        throw new UnsupportedProxyOperationException("The JDK 1.3+ java.lang.Proxy implementation only supports interface types and may not be used to proxy concrete classes by sub-classing");
    }
}
