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

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.beanplanet.core.lang.Assert;

/**
 * Adapts a standard Java 1.3+ <code>{@link InvocationHandler}</code> to a generic method call handler.
 * 
 * @author Gary Watson
 * 
 */
public class InvocationHandlerToMethodCallInterceptorAdaptor implements InvocationHandler {
   protected static final InvocationTargetExceptionUnwinder INVOCATION_TARGET_EX_TRANSLATOR = new InvocationTargetExceptionUnwinder();
   protected MethodCallInterceptor interceptor;

   public InvocationHandlerToMethodCallInterceptorAdaptor(MethodCallInterceptor interceptor) {
      Assert.notNull(interceptor, "The method call interceptor must not be null");
      this.interceptor = interceptor;
   }

   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      MethodCallContext context = new MethodCallContext(proxy, method, args);
      context.setExceptionHandler(INVOCATION_TARGET_EX_TRANSLATOR);

      return interceptor.interceptMethodCall(context);
   }
}
