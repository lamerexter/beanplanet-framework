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
 * The following axis: the nodes after a node in document order, other than its descendants, within the tree (below
 * its root, if it has one). Lazy: the path down to the node is found first, then each step moves one node. For a node
 * that is not among its parent's children (as an XML attribute), XPath's: its parent's descendants, then the nodes
 * following its parent.
 *
 * @param <T> the type of the tree's nodes.
 */
class FollowingIterator<T> extends CursorTreeIterator<T> {
    private final Tree<T> tree;
    private final T from;
    private TreePath<T> path;

    FollowingIterator(final Tree<T> tree, final T from) {
        super(true);
        this.tree = tree;
        this.from = from;
    }

    @Override
    protected boolean first() {
        if (from == null) return false;
        path = TreePath.fromTop(tree, from, true);
        return path.afterSubtree();
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
