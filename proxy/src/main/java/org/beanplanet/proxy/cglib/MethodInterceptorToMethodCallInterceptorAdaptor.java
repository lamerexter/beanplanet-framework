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

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.beanplanet.core.lang.Assert;
import org.beanplanet.core.lang.proxy.MethodCallInterceptor;

public class MethodInterceptorToMethodCallInterceptorAdaptor implements MethodInterceptor {
   protected MethodCallInterceptor interceptor;

   public MethodInterceptorToMethodCallInterceptorAdaptor(MethodCallInterceptor interceptor) {
      Assert.notNull(interceptor, "The method call interceptor must not be null");
      this.interceptor = interceptor;
   }

   public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
      CGLibMethodCallContext context = new CGLibMethodCallContext(proxy, method, args, methodProxy);

      return interceptor.interceptMethodCall(context);
   }
}
