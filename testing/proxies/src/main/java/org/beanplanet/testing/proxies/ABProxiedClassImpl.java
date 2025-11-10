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

public class ABProxiedClassImpl extends ProxiedClass implements IA, IB  {
   protected boolean aCalled = false;
   protected boolean bCalled = false;
   
   public void a() {
      aCalled = true;
   }

   public boolean getACalled() {
      return aCalled;
   }

   public void b() {
      bCalled = true;
   }

   public boolean getBCalled() {
      return bCalled;
   }
}
