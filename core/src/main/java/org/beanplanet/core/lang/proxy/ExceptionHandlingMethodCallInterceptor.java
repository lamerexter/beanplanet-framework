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

import org.beanplanet.core.lang.Assert;
import org.beanplanet.core.lang.ErrorPropagatingExceptionHandler;
import org.beanplanet.core.lang.ExceptionHandler;

/**
 * Convenience method call interceptor which simply wraps another, passing any exceptions thrown by the method call to a
 * configured exception handler.
 * 
 * @author Gary Watson
 * 
 */
public class ExceptionHandlingMethodCallInterceptor implements MethodCallInterceptor {
   /** The method call interceptor wrapped and invoked by this one. */
   protected MethodCallInterceptor interceptor;
   /** The handler of exceptions thrown by the wrapped interceptor. */
   protected ExceptionHandler exceptionHandler;

   public ExceptionHandlingMethodCallInterceptor() {
      setExceptionHandler(new ErrorPropagatingExceptionHandler());
   }

   public ExceptionHandlingMethodCallInterceptor(MethodCallInterceptor interceptor, ExceptionHandler exceptionHandler) {
      setInterceptor(interceptor);
      setExceptionHandler(exceptionHandler);
   }

   /**
    * @return the interceptor
    */
   public MethodCallInterceptor getInterceptor() {
      return interceptor;
   }

   /**
    * @param interceptor the interceptor to set
    */
   public void setInterceptor(MethodCallInterceptor interceptor) {
      this.interceptor = interceptor;
   }

   /**
    * @return the exceptionHandler
    */
   public ExceptionHandler getExceptionHandler() {
      return exceptionHandler;
   }

   /**
    * @param exceptionHandler the exceptionHandler to set
    */
   public void setExceptionHandler(ExceptionHandler exceptionHandler) {
      this.exceptionHandler = exceptionHandler;
   }

   /**
    * Invokes the method on the method call interceptor configured in this instance, passing any exceptions thrown to
    * the configured exsception handler.
    * 
    * @param context the method call context information
    * @return the value returned by the method called.
    * @exception Throwable thrown by the exception handler in response to either the same or a different exception
    *            thrown by the method call itself.
    */
   public Object interceptMethodCall(MethodCallContext context) throws Throwable {
      Assert.notNull(interceptor, "The method call interceptor may not be null");
      Assert.notNull(exceptionHandler, "The exception handler may not be null");

      try {
         return interceptor.interceptMethodCall(context);
      } catch (Throwable th) {
         return exceptionHandler.handleException(th);
      }
   }

}
