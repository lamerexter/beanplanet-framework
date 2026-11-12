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
 * The descendant and descendant-or-self axes: the nodes below a node (and the node itself), in document order, and
 * nothing beyond them. Lazy: children are listed as the iteration reaches them; no parents are consulted.
 *
 * @param <T> the type of the tree's nodes.
 */
class DescendantIterator<T> extends CursorTreeIterator<T> {
    private final Tree<T> tree;
    private final T from;
    private final boolean includeSelf;
    private TreePath<T> path;

    DescendantIterator(final Tree<T> tree, final T from, final boolean includeSelf) {
        super(true);
        this.tree = tree;
        this.from = from;
        this.includeSelf = includeSelf;
    }

    @Override
    protected boolean first() {
        if (from == null) return false;
        path = TreePath.at(tree, from);
        return includeSelf || path.firstChild();
    }

    @Override
    protected T current() {
        return path.node();
    }

    @Override
    protected boolean forward() {
        return path.preorderNext();
    }

    @Override
    protected boolean backward() {
        return path.preorderPrevious();
    }
}
