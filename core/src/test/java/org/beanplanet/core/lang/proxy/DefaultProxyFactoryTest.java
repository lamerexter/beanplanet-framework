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

import org.beanplanet.testing.proxies.ProxiedClass;

public class DefaultProxyFactoryTest extends AbstractCommonProxyFactoryTests {

   public DefaultProxyFactoryTest() {
      ProxyFactoryConfig cfg = new ProxyFactoryConfig();
      cfg.setProxyTargetClass(false);
      proxyFactory = new DefaultProxyFactory();
   }

   public void testSimpleTargetProxyFailsDueToNoConcreteProxySupport() {
      try {
         ProxiedClass target = new ProxiedClass();
         proxyFactory.createProxy(null, target.getClass(), new TargetInvokingMethodCallInterceptor(target), null);
         fail("Proxy of concrete types should fail in BeanPlanet core owing to no CGLib and/or beanplanet-proxy module support libraries present");
      } catch (UnsupportedProxyOperationException unsupportedProxyEx) {
      }
   }

   public void testProxySubclassConfiguration() {
      assertFalse(((DefaultProxyFactory)proxyFactory).getProxyfactoryConfig().getProxyTargetClass());

      ProxyFactoryConfig cfg = new ProxyFactoryConfig();
      cfg.setProxyTargetClass(true);
      proxyFactory = new DefaultProxyFactory(cfg);
      assertTrue(((DefaultProxyFactory)proxyFactory).getProxyfactoryConfig().getProxyTargetClass());
   }
}
