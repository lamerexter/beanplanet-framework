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

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Objects;

public class AncestorIterator<T> implements TreeIterator<T> {
    final ListIterator<T> ancestorListIterator;

    public AncestorIterator(final Tree<T> tree, T from) {
        LinkedList<T> ancestorList = new LinkedList<T>();
        if ( !Objects.equals(tree.getRoot(), from) ) {
            from = tree.getParent(from);
            while (from != null) {
                ancestorList.add(0, from);
                from = tree.getParent(from);
                if (Objects.equals(tree.getRoot(), from)) break;
            }
        }

        ancestorListIterator = ancestorList.listIterator(0);
    }

    @Override
    public boolean hasNext() {
        return ancestorListIterator.hasNext();
    }
    @Override
    public T next() {
        return ancestorListIterator.next();
    }

    @Override
    public boolean hasPrevious() {
        return ancestorListIterator.hasPrevious();
    }

    @Override
    public T previous() {
        return ancestorListIterator.previous();
    }
}
