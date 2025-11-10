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
package org.beanplanet.core.models.tree;


/**
 * An empty implementation of a tree iterator.
 * 
 * @author Gary Watson
 */
public class EmptyTreeIterator<E> implements TreeIterator<E> {

   public boolean hasNext() {
      return false;
   }

   public boolean hasPrevious() {
      return false;
   }

   public E next() {
      throw new UnsupportedOperationException("next() on an empty tree iterator is not supported");
   }

   public E previous() {
      throw new UnsupportedOperationException("previous() on an empty tree iterator is not supported");
   }

   public void remove() {
      throw new UnsupportedOperationException("remove() on an empty tree iterator is not supported");
   }

   public void set(E replacement) {
      throw new UnsupportedOperationException("set() on an empty tree iterator is not supported");
   }

}
