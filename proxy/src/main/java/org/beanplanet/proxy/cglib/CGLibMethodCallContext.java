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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.beanplanet.core.lang.proxy.MethodCallContext;

import net.sf.cglib.proxy.MethodProxy;
import org.beanplanet.core.lang.proxy.ProxyException;

public class CGLibMethodCallContext extends MethodCallContext {
   protected MethodProxy methodProxy;

   public CGLibMethodCallContext(Object target, Method method, Object parameters[], MethodProxy methodProxy) {
      super(target, method, parameters);
      this.methodProxy = methodProxy;
   }

   public MethodProxy getMethodProxy() {
      return methodProxy;
   }

   @Override
   public Object invokeOnTarget(Object target) throws Throwable {
      try {
         return methodProxy.invoke(target, parameters);
      } catch (InvocationTargetException itEx) {
         throw itEx.getTargetException();
      }
   }
}
