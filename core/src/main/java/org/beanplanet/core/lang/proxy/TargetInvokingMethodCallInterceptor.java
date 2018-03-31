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
