package org.beanplanet.proxy.bytebuddy;

import org.beanplanet.core.lang.proxy.FinalClassProxyOperationException;
import org.beanplanet.core.lang.proxy.TargetInvokingMethodCallInterceptor;
import org.beanplanet.testing.proxies.FinalClass;
import org.beanplanet.testing.proxies.ProxiedClass;
import org.junit.Before;
import org.junit.Test;

import static org.beanplanet.core.lang.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ByteBuddyProxyFactoryTest extends AbstractCommonProxyFactoryTests {
    @Before
    public void setup() {
        proxyFactory = new ByteBuddyProxyFactory();
    }

    @Test
    public void testSimpleTargetProxy() {
        ProxiedClass target = new ProxiedClass();
        ProxiedClass proxy = proxyFactory.dynamicProxy(target.getClass(), new TargetInvokingMethodCallInterceptor(target));
        assertTrue(proxy != target);
        assertTrue(target == proxy.getThis());
        assertTrue(target == target.getThis());
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
        assertTrue(proxy.equals(target));
        assertTrue(proxy.hashCode() == target.hashCode());
    }

    @Test
    public void testAttemptToProxyFinalClassFailsTest() {
        FinalClass target = new FinalClass();

        try {
            proxyFactory.dynamicProxy(target.getClass(), new TargetInvokingMethodCallInterceptor(target));
            fail("The CGLIb framework is not supposed to be able to proxy concrete types marked as final!");
        } catch (FinalClassProxyOperationException unEx) {
        }
    }

}