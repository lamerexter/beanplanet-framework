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

import org.beanplanet.core.lang.proxy.FinalClassProxyOperationException;
import org.beanplanet.core.lang.proxy.TargetInvokingMethodCallInterceptor;
import org.beanplanet.core.lang.proxy.UnsupportedProxyOperationException;
import org.beanplanet.testing.proxies.ABProxiedClassImpl;
import org.beanplanet.testing.proxies.FinalClass;
import org.beanplanet.testing.proxies.ProxiedClass;

public class CGLibProxyFactoryTests extends AbstractCommonProxyFactoryTests {

    public void setUp() {
        proxyFactory = new CGLibProxyFactory();
    }

    public void testSimpleTargetProxyOperation() {
        ABProxiedClassImpl target = new ABProxiedClassImpl();

        try {
            @SuppressWarnings("unused")
            Object proxy = proxyFactory.createProxy(null, target.getClass(), new TargetInvokingMethodCallInterceptor(
                    target), null);
        } catch (UnsupportedProxyOperationException unEx) {
            fail("The CGLIbProxyFactory is supposed to support proxy via concrete types!");
        }
    }

    public void testSimpleTargetProxy() {
        ProxiedClass target = new ProxiedClass();
        ProxiedClass proxy = proxyFactory.createProxy(null, target.getClass(), new TargetInvokingMethodCallInterceptor(
                target), null);
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

    public void testAttemptToProxyFinalClassFailsTest() {
        FinalClass target = new FinalClass();

        try {
            @SuppressWarnings("unused")
            Object proxy = proxyFactory.createProxy(null, target.getClass(), new TargetInvokingMethodCallInterceptor(
                    target), null);
            fail("The CGLIb framework is not supposed to be able to proxy concrete types marked as final!");
        } catch (FinalClassProxyOperationException unEx) {
        }
    }
}
