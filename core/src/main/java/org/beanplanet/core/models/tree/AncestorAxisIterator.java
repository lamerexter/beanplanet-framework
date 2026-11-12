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

import java.util.ArrayList;
import java.util.List;

/**
 * The ancestor and ancestor-or-self axes: the node itself (for ancestor-or-self), then its parent, its parent's parent
 * and so on, nearest first (XPath's order for these reverse axes), up to and including the tree's root, if the tree
 * has one above the node, else the node's oldest ancestor. Lazy: each parent is found when the iteration reaches it;
 * {@link #previous()} walks back towards the node.
 *
 * @param <T> the type of the tree's nodes.
 */
class AncestorAxisIterator<T> extends CursorTreeIterator<T> {
    private final Tree<T> tree;
    private final T from;
    private final boolean includeSelf;
    /** The nodes reached so far, from the node itself upwards. */
    private final List<T> chain = new ArrayList<>();
    private int index;

    AncestorAxisIterator(final Tree<T> tree, final T from, final boolean includeSelf) {
        super(true);
        this.tree = tree;
        this.from = from;
        this.includeSelf = includeSelf;
    }

    @Override
    protected boolean first() {
        if (from == null) return false;
        chain.add(from);
        index = 0;
        return includeSelf || forward();
    }

    @Override
    protected T current() {
        return chain.get(index);
    }

    @Override
    protected boolean forward() {
        if (index + 1 < chain.size()) {
            index++;
            return true;
        }
        final T parent = TreePath.parentWithin(tree, chain.get(index));
        if (parent == null) return false;
        chain.add(parent);
        index++;
        return true;
    }

    @Override
    protected boolean backward() {
        if (index == 0) return false;
        index--;
        return true;
    }
}
