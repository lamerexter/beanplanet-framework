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

/**
 * An implementation of <code>{@link MethodCallInterceptor}</code> which, for each method invocation on the proxied
 * object, invokes the corresponding method on the target (pass-through).
 * 
 * @author Gary Watson
 */
public class TargetInvokingMethodCallInterceptor implements MethodCallInterceptor {
   protected Object target;

   public TargetInvokingMethodCallInterceptor() {
   }

   public TargetInvokingMethodCallInterceptor(Object target) {
      setTarget(target);
   }

   /**
    * 
    * @return the proxied target
    */
   public Object getTarget() {
      return target;
   }

   /**
    * @param target the target to set
    */
   public void setTarget(Object target) {
      this.target = target;
   }

   public Object interceptMethodCall(MethodCallContext context) throws Throwable {
      if (target == null) {
         throw new NullTargetException("The proxy target has not yet been set or cannot be determined");
      }

      return context.invokeOnTarget(target);
   }
}
