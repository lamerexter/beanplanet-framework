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