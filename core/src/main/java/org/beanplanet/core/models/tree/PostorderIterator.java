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
 * A postorder traversal of a tree, within its root: positioned at the start of a node's subtree in postorder (its first
 * leaf, reached through first children; the root's, if no node is given), {@link #next()} returns the nodes from there
 * to the end of the tree, the root last; {@link #previous()} returns those before it. To traverse just the subtree of a
 * node, use {@link Tree#subtree(Object)}'s {@link Tree#postorderIterator()}.
 *
 * <p>Lazy in both directions: children are listed as the traversal reaches them, and each step moves one node, without
 * looking any node up among its siblings. Starting at the root, no parents are consulted.</p>
 *
 * @param <E> the type of the tree's nodes.
 */
public class PostorderIterator<E> extends CursorTreeIterator<E> {
    private final Tree<E> tree;
    private final E fromNode;
    private final boolean fromRoot;
    private TreePath<E> path;

    public PostorderIterator(final Tree<E> tree) {
        super(false);
        this.tree = tree;
        this.fromNode = tree.getRoot();
        this.fromRoot = true;
    }

    public PostorderIterator(final Tree<E> tree, final E fromNode) {
        super(false);
        this.tree = tree;
        this.fromNode = fromNode;
        this.fromRoot = false;
    }

    public Tree<E> getTree() {
        return tree;
    }

    public E getFromNode() {
        return fromNode;
    }

    @Override
    protected boolean first() {
        if (fromNode == null) return false;
        path = fromRoot ? TreePath.at(tree, fromNode) : TreePath.fromTop(tree, fromNode, false);
        path.firstLeaf();
        return true;
    }

    @Override
    protected E current() {
        return path.node();
    }

    @Override
    protected boolean forward() {
        return path.postorderNext();
    }

    @Override
    protected boolean backward() {
        return path.postorderPrevious();
    }
}
