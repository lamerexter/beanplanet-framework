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

import org.beanplanet.testing.proxies.ABProxiedClassImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

public class JavaProxyFactoryTest extends AbstractCommonProxyFactoryTests {
   @Before
   public void setUp() {
      proxyFactory = new JavaProxyFactory();
   }

   @Test
   public void testUnsupportedProxyOperations() {
      ABProxiedClassImpl target = new ABProxiedClassImpl();

      try {
         proxyFactory.dynamicProxy(target.getClass(), new TargetInvokingMethodCallInterceptor(target));
         fail("The Java 1.3+ java.lang.Proxy based factory appears to support proxy on concrete types!");
      } catch (UnsupportedProxyOperationException unEx) {
      }
   }
}