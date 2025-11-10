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

import java.util.Iterator;

public class FollowingSiblingIterator<T> implements Iterator<T> {
    private final Tree<T> tree;
    private final T from;

    private final T parent;
    private int currentSiblingIdx;

    public FollowingSiblingIterator(final Tree<T> tree, final T from) {
        this.tree = tree;
        this.from = from;

        parent = tree.getParent(from);
        if (parent == null) {
            currentSiblingIdx = -1;
        }
        else {
            currentSiblingIdx = tree.getIndexOfChild(parent, from) + 1;
        }
    }

    public boolean hasNext() {
        if (parent == null) {
            return false;
        }
        return currentSiblingIdx < tree.getNumberOfChildren(parent);
    }

    public T next() {
        return tree.getChild(parent, currentSiblingIdx++);
    }

    public void remove() {
        throw new java.lang.UnsupportedOperationException("remove() on " + getClass().getName());
    }
}
