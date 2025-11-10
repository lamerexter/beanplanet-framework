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
package org.beanplanet.testing.proxies;

import org.beanplanet.testing.core.ACheckedException;
import org.beanplanet.testing.core.ARuntimeException;

public class ProxiedClass implements IProxiedClass {
   protected int methodCallCount = 0;

   public int getTestMethodCallCount() {
      return methodCallCount;
   }

   public IProxiedClass getThis() {
      return this;
   }

   public void testMethod() {
      ++methodCallCount;
   }

   public Object anExceptionThrowingMethodWithNoDeclaredExceptions() {
      throw new ARuntimeException("ARuntimeException");
   }

   public Object aMethodWithThrowingACheckedException() throws ACheckedException {
      throw new ACheckedException("ARuntimeException");
   }

   public Object aMethodWithThrowingAnUncheckedException() throws ARuntimeException {
      throw new ARuntimeException("ARuntimeException");
   }
}
