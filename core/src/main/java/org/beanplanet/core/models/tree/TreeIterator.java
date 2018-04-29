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
   public boolean hasPrevious();

   public boolean hasNext();

   public E previous();

   public E next();

   public void remove();

   public void set(E replacement);

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
