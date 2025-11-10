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

import org.beanplanet.core.util.IteratorUtil;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Definition of a tree specific iterator, extending the java collections iterator and providing tree iteration features
 * such as forward and reverse iteration and, where applicable, the ability to set values during iteration.
 * 
 * @author Gary Watson
 */
public interface TreeIterator<E> extends Iterator<E> {
   boolean hasPrevious();

   boolean hasNext();

   E previous();

   E next();

   default void remove() {
      throw new UnsupportedOperationException("Removal of tree nodes from model via iterator is unsupported");
   }

   default void set(E replacement) {
      throw new UnsupportedOperationException("Set of tree node in model via iterator is unsupported");
   }

   default TreeIterator<E> reverse() {
      return new ReverseOrderTreeIterator<>(this);
   }

   default Stream<E> stream() {
      return IteratorUtil.asStream(this);
   }

   default List<E> toList() {
      return IteratorUtil.toList(this);
   }
}
