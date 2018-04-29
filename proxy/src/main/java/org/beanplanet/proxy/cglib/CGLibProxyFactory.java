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

import net.sf.cglib.proxy.Enhancer;
import org.beanplanet.core.lang.TypeUtil;
import org.beanplanet.core.lang.proxy.FinalClassProxyOperationException;
import org.beanplanet.core.lang.proxy.MethodCallInterceptor;
import org.beanplanet.core.lang.proxy.ProxyException;
import org.beanplanet.core.lang.proxy.ProxyFactory;

import java.lang.reflect.Modifier;

import static org.beanplanet.core.lang.Assert.isTrue;
import static org.beanplanet.core.lang.TypeUtil.areAllInterfaces;
import static org.beanplanet.core.util.ArrayUtil.isEmptyOrNull;

public class CGLibProxyFactory implements ProxyFactory {
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
        isTrue(proxyInterfaces != null && proxyInterfaces.length > 0, "The superclass to extend or one or more proxy interfaces may not be null");
        isTrue(areAllInterfaces(proxyInterfaces), "Only interface types may be specified as proxy interfaces");

        if (classloader == null) {
            classloader = TypeUtil.getClassLoaderInContext(proxyInterfaces[0]);
        }

        Enhancer e = new Enhancer();
        e.setClassLoader(classloader);
        if (proxyInterfaces != null) {
            e.setInterfaces(proxyInterfaces);
        }
        e.setCallback(new MethodInterceptorToMethodCallInterceptorAdaptor(handler));

        return (T) e.create();
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
    @SuppressWarnings("unchecked")
    public <T> T dynamicProxy(ClassLoader classloader, Class<T> superclass, Class<?>[] ctorArgTypes, Object[] ctorArgs, Class<?>[] proxyInterfaces, MethodCallInterceptor handler) throws ProxyException {
        isTrue(superclass != null, "The superclass may not be null");
        isTrue(isEmptyOrNull(proxyInterfaces) || areAllInterfaces(proxyInterfaces), "Only interface types may be specified as proxy interfaces");

        if (Modifier.isFinal(superclass.getModifiers())) {
            throw new FinalClassProxyOperationException("Unable to proxy object [class=" + superclass.getClass() + "]: the class is marked as final.");
        }

        if (classloader == null) {
            classloader = TypeUtil.getClassLoaderInContext(superclass);
        }

        Enhancer e = new Enhancer();
        e.setClassLoader(classloader);
        e.setSuperclass(superclass);
        if (proxyInterfaces != null) {
            e.setInterfaces(proxyInterfaces);
        }
        e.setCallback(new MethodInterceptorToMethodCallInterceptorAdaptor(handler));

        return (T)(isEmptyOrNull(ctorArgTypes) ? e.create() : e.create(ctorArgTypes, ctorArgs));
    }
}
