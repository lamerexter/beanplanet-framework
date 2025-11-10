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

import junit.framework.TestCase;
import org.beanplanet.testing.core.ACheckedException;
import org.beanplanet.testing.core.ARuntimeException;
import org.beanplanet.testing.proxies.*;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public abstract class AbstractCommonProxyFactoryTests {
    protected ProxyFactory proxyFactory;

    @Test
    public void testExtendedSingleInterfaceProxyInterceptor() {
        ProxiedClass target = new ProxiedClass() {
            public int getTestMethodCallCount() {
                return super.getTestMethodCallCount() * 2;
            }
        };
        IProxiedClass proxy = proxyFactory.dynamicProxy(IProxiedClass.class,
                                                        new TargetInvokingMethodCallInterceptor(target));
        assertEquals(0, target.getTestMethodCallCount());
        proxy.testMethod();
        assertEquals(2, target.getTestMethodCallCount());
        proxy.testMethod();
        proxy.testMethod();
        proxy.testMethod();
        proxy.testMethod();
        assertEquals(10, target.getTestMethodCallCount());
    }

    @Test
    public void testExactMultipleInterfaceProxyInterceptor() {
        ABProxiedClassImpl target = new ABProxiedClassImpl();
        Object proxy = proxyFactory.dynamicProxy(new Class<?>[]{IA.class, IB.class, IProxiedClass.class},
                                                 new TargetInvokingMethodCallInterceptor(target));
        Assert.assertTrue(proxy instanceof IA);
        Assert.assertTrue(proxy instanceof IB);
        Assert.assertTrue(proxy instanceof IProxiedClass);
        Assert.assertFalse(((IA) proxy).getACalled());
        Assert.assertFalse(((IB) proxy).getBCalled());
        assertEquals(0, ((IProxiedClass) proxy).getTestMethodCallCount());

        ((IA) proxy).a();
        Assert.assertTrue(((IA) proxy).getACalled());
        Assert.assertTrue(target.getACalled());

        target.b();
        Assert.assertTrue(((IB) proxy).getBCalled());
        Assert.assertTrue(target.getBCalled());

        ((IProxiedClass) proxy).testMethod();
        assertEquals(1, ((IProxiedClass) proxy).getTestMethodCallCount());
        assertEquals(1, target.getTestMethodCallCount());
    }

    @Test
    public void testSubsetMultipleInterfaceProxyInterceptor() {
        ABProxiedClassImpl target = new ABProxiedClassImpl();
        Object proxy = proxyFactory.dynamicProxy(new Class<?>[]{IA.class, IB.class},
                                                 new TargetInvokingMethodCallInterceptor(target));
        Assert.assertTrue(proxy instanceof IA);
        Assert.assertTrue(proxy instanceof IB);
        Assert.assertFalse(proxy instanceof IProxiedClass);
        Assert.assertFalse(((IA) proxy).getACalled());
        Assert.assertFalse(((IB) proxy).getBCalled());

        ((IA) proxy).a();
        Assert.assertTrue(((IA) proxy).getACalled());
        Assert.assertTrue(target.getACalled());

        target.b();
        Assert.assertTrue(((IB) proxy).getBCalled());
        Assert.assertTrue(target.getBCalled());

        assertEquals(0, target.getTestMethodCallCount());
    }

    @Test
    public void testInvocationOfMethodWithNoDeclaredExceptionsThrowingUncheckedException() {
        IProxiedClass target = new ProxiedClass();
        IProxiedClass proxy = proxyFactory.dynamicProxy(IProxiedClass.class, new TargetInvokingMethodCallInterceptor(target));
        try {
            proxy.anExceptionThrowingMethodWithNoDeclaredExceptions();
        } catch (ARuntimeException expectedEx) {

        } catch (Throwable unexpectedEx) {
            Assert.fail("Unexpected exception caught: " + unexpectedEx.getClass() + " - " + unexpectedEx.getMessage());
        }
    }

    @Test
    public void testSimpleSingleInterfaceProxyInterceptor() {
        ProxiedClass target = new ProxiedClass();
        IProxiedClass proxy = proxyFactory.dynamicProxy(IProxiedClass.class,
                                                        new TargetInvokingMethodCallInterceptor(target));
        Assert.assertTrue(proxy != target);
        Assert.assertTrue(target == proxy.getThis());
        assertEquals(0, proxy.getTestMethodCallCount());
        assertEquals(0, target.getTestMethodCallCount());
        proxy.testMethod();
        assertEquals(1, proxy.getTestMethodCallCount());
        assertEquals(1, target.getTestMethodCallCount());
        proxy.testMethod();
        proxy.testMethod();
        proxy.testMethod();
        proxy.testMethod();
        assertEquals(5, proxy.getTestMethodCallCount());
        assertEquals(5, target.getTestMethodCallCount());
        Assert.assertTrue(proxy.equals(target));
        Assert.assertTrue(proxy.hashCode() == target.hashCode());
    }

    @Test
    public void testInvocationOfCheckedExceptionThrowingMethod() {
        IProxiedClass target = new ProxiedClass();
        IProxiedClass proxy = proxyFactory.dynamicProxy(IProxiedClass.class, new TargetInvokingMethodCallInterceptor(target));
        try {
            proxy.aMethodWithThrowingACheckedException();
        } catch (ACheckedException expectedEx) {

        } catch (Throwable unexpectedEx) {
            Assert.fail("Unexpected exception caught: " + unexpectedEx.getClass() + " - " + unexpectedEx.getMessage());
        }
    }

    @Test
    public void testInvocationOfUncheckedExceptionThrowingMethod() {
        IProxiedClass target = new ProxiedClass();
        IProxiedClass proxy = proxyFactory.dynamicProxy(IProxiedClass.class, new TargetInvokingMethodCallInterceptor(target));
        try {
            proxy.aMethodWithThrowingAnUncheckedException();
        } catch (ARuntimeException expectedEx) {

        } catch (Throwable unexpectedEx) {
            Assert.fail("Unexpected exception caught: " + unexpectedEx.getClass() + " - " + unexpectedEx.getMessage());
        }
    }
}
