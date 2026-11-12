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
 * The preceding axis: the nodes before a node in document order, other than its ancestors, within the tree (below its
 * root, if it has one), nearest first (XPath's order for this reverse axis). Lazy: the path down to the node is found
 * first, then each step moves back one node, passing over the node's ancestors. For a node that is not among its
 * parent's children (as an XML attribute), XPath's: the nodes preceding its parent.
 *
 * <p>A node on the current path is one of the context node's ancestors when the path, down to it, follows the context
 * node's own path (compared by index at each depth), so nodes are never compared for equality.</p>
 *
 * @param <T> the type of the tree's nodes.
 */
class PrecedingIterator<T> extends CursorTreeIterator<T> {
    private final Tree<T> tree;
    private final T from;
    private TreePath<T> path;
    /** The context node's indexes, at each depth, and its depth. */
    private int[] fromIndexes;
    private int fromDepth;

    PrecedingIterator(final Tree<T> tree, final T from) {
        super(true);
        this.tree = tree;
        this.from = from;
    }

    @Override
    protected boolean first() {
        if (from == null) return false;
        path = TreePath.fromTop(tree, from, true);
        fromIndexes = path.indexes();
        fromDepth = path.depth();
        return forward();
    }

    @Override
    protected T current() {
        return path.node();
    }

    /** The depth to which the current path follows the context node's path. */
    private int sharedDepth() {
        final int limit = Math.min(path.depth(), fromDepth);
        int depth = 0;
        while (depth < limit && path.indexAt(depth + 1) == fromIndexes[depth + 1]) depth++;
        return depth;
    }

    private boolean isAncestorOfFrom() {
        return path.depth() < fromDepth && sharedDepth() == path.depth();
    }

    /**
     * Whether a node other than an ancestor of the context node is before the current node in document order. Before
     * the current node are: its previous siblings' subtrees, if it has any (none of them an ancestor); its parent and
     * what is before that. A parent below the depth the path shares with the context node's path is no ancestor; at or
     * above it, every node on the path is an ancestor, and only their previous siblings' subtrees remain.
     */
    private boolean anyPrecedingLeft() {
        final int depth = path.depth();
        if (depth == 0) return false;
        if (depth - 1 > sharedDepth()) return true;
        for (int level = depth; level > 0; level--) {
            if (path.indexAt(level) > 0) return true;
        }
        return false;
    }

    @Override
    protected boolean forward() {
        if (!anyPrecedingLeft()) return false;
        do {
            path.preorderPrevious();
        } while (isAncestorOfFrom());
        return true;
    }

    @Override
    protected boolean backward() {
        // Only retraces steps taken forwards (bounded at the first node), so a non-ancestor is always reached.
        do {
            path.preorderNext();
        } while (isAncestorOfFrom());
        return true;
    }
}
