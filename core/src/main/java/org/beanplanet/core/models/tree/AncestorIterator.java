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
