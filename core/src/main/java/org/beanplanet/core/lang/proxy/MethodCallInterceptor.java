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
