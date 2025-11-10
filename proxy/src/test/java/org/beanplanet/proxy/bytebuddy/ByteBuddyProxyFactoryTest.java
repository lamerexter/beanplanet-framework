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