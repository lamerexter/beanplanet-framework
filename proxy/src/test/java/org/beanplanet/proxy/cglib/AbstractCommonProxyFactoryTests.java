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
package org.beanplanet.proxy.cglib;

import junit.framework.TestCase;
import org.beanplanet.core.lang.proxy.ProxyFactory;
import org.beanplanet.core.lang.proxy.TargetInvokingMethodCallInterceptor;
import org.beanplanet.testing.core.ACheckedException;
import org.beanplanet.testing.core.ARuntimeException;
import org.beanplanet.testing.proxies.*;
import org.junit.Assert;

public abstract class AbstractCommonProxyFactoryTests extends TestCase {
    protected ProxyFactory proxyFactory;

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
