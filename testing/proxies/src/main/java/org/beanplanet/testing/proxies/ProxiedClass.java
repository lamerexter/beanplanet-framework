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
