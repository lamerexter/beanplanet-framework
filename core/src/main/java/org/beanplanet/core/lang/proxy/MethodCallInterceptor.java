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
 * Defines a <a href="http://en.wikipedia.org/wiki/Template_method_pattern">Template</a> with methods to handle the
 * interception of a method call a.k.a. Aspect Orientated Programming (AOP).
 * 
 * @author Gary Watson
 */
public interface MethodCallInterceptor {
   /**
    * Callback when the invocation of a method is intercepted. Implementations may chose to simply return a cached
    * value, pass on the invocation or otherwise perform the necessary work themselves.
    * 
    * @param context the method call information
    * @return a value to be returned as if returned by the method invocation itself.
    * @throws Throwable thrown either by the underlying method, if invoked, or by this handler in performing work
    *         itself.
    */
   public Object interceptMethodCall(MethodCallContext context) throws Throwable;
}
