package org.beanplanet.proxy.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;
import org.beanplanet.core.lang.TypeUtil;
import org.beanplanet.core.lang.proxy.*;

import java.lang.reflect.Modifier;

import static org.beanplanet.core.lang.Assert.isTrue;
import static org.beanplanet.core.lang.TypeUtil.areAllInterfaces;
import static org.beanplanet.core.util.ArrayUtil.isEmptyOrNull;

public class ByteBuddyProxyFactory implements ProxyFactory {
    /**
     * Creates a dynamic proxy that implements all the specified interfaces. Invocations
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
    @SuppressWarnings("unchecked")
    @Override
    public <T> T dynamicProxy(ClassLoader classloader, Class<?>[] proxyInterfaces, MethodCallInterceptor handler) throws ProxyException {
        isTrue(proxyInterfaces != null && proxyInterfaces.length > 0, "The superclass to extend or one or more proxy interfaces may not be null");
        isTrue(areAllInterfaces(proxyInterfaces), "Only interface types may be specified as proxy interfaces");

        if (classloader == null && proxyInterfaces != null && proxyInterfaces.length > 0) {
            classloader = TypeUtil.getClassLoaderInContext(proxyInterfaces[0]);
        }

        Class<?> proxyClass = new ByteBuddy()
                .subclass(Object.class)
                .implement(proxyInterfaces != null ? proxyInterfaces : TypeUtil.EMPTY_TYPES)
                .method(ElementMatchers.any())
                .intercept(InvocationHandlerAdapter.of(new InvocationHandlerToMethodCallInterceptorAdaptor(handler)))
                .make()
                .load(classloader)
                .getLoaded();
        return (T)TypeUtil.instantiateClass(proxyClass);
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
        isTrue(superclass != null, "The superclass may not be null");
        isTrue(isEmptyOrNull(proxyInterfaces) || areAllInterfaces(proxyInterfaces), "Only interface types may be specified as proxy interfaces");

        if (Modifier.isFinal(superclass.getModifiers())) {
            throw new FinalClassProxyOperationException("Unable to proxy object [class=" + superclass.getClass() + "]: the class is marked as final.");
        }

        if (classloader == null) {
            classloader = TypeUtil.getClassLoaderInContext(superclass);
        }

        DynamicType.Builder<T> proxyBuilder = new ByteBuddy().subclass(superclass);

        Class<?> proxyClass = new ByteBuddy()
                .subclass(superclass)
                .implement(proxyInterfaces != null ? proxyInterfaces : TypeUtil.EMPTY_TYPES)
                .method(ElementMatchers.any())
                .intercept(InvocationHandlerAdapter.of(new InvocationHandlerToMethodCallInterceptorAdaptor(handler)))
                .make()
                .load(classloader)
                .getLoaded();
        return (T)TypeUtil.instantiateClass(proxyClass);
    }
}
