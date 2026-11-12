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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * A position in a tree: the path of nodes from a top node down to the current node, each with the list of its siblings
 * and its index there. The traversals move along the path one step at a time, using only {@link Tree#getChildren(Object)}:
 * a node's children are listed once, when the path first descends into them, and no node is ever looked up among its
 * siblings, so the steps work for trees whose nodes are equal to one another (two equal siblings, say) and take the
 * same time however many siblings a node has.
 *
 * <p>The path never moves above or beside its <em>floor</em>, the depth of its top node: a traversal of a subtree has
 * its floor at the subtree's root.</p>
 *
 * <p>Every step either moves the path and returns true, or leaves it as it was and returns false.</p>
 *
 * <p>A node may have a parent without being one of its parent's children, as an XML attribute has its element as
 * parent. XPath places such a node, in document order, after its parent and before its parent's children: so the nodes
 * following it are its parent's descendants and then the nodes following its parent; those preceding it are those
 * preceding its parent; it has no siblings. A path down to such a node holds it <em>detached</em>, as the only node at
 * its level; once the path has moved off it, it is never reached again (it is no one's child).</p>
 *
 * @param <E> the type of the tree's nodes.
 */
final class TreePath<E> {
    private final Tree<E> tree;
    private final List<E> nodes = new ArrayList<>();
    /** The children of the node above each node: the list each node is in (null for the top node). */
    private final List<List<E>> siblings = new ArrayList<>();
    private int[] indexes = new int[16];
    /** Whether the node at each depth is detached: not among its parent's children. */
    private boolean[] detached = new boolean[16];

    private TreePath(final Tree<E> tree, final E top) {
        this.tree = tree;
        nodes.add(top);
        siblings.add(null);
    }

    /**
     * A path whose top (and floor) is the given node. No parents are consulted.
     */
    static <E> TreePath<E> at(final Tree<E> tree, final E node) {
        return new TreePath<>(tree, node);
    }

    /**
     * The path from the top of the tree down to the given node, with its floor at the top. The top is the tree's root,
     * if it is an ancestor-or-self of the node, or else the node's oldest ancestor. A node on the way that is not among
     * its parent's children is held detached, if allowed.
     *
     * @throws IllegalArgumentException if a node is not among the children of its parent, and that is not allowed.
     */
    static <E> TreePath<E> fromTop(final Tree<E> tree, final E node, final boolean detachedAllowed) {
        final Deque<E> chain = new ArrayDeque<>();
        E ancestorOrSelf = node;
        while (true) {
            chain.push(ancestorOrSelf);
            if (isRoot(tree, ancestorOrSelf)) break;
            final E parent = tree.getParent(ancestorOrSelf);
            if (parent == null) break;
            ancestorOrSelf = parent;
        }

        final TreePath<E> path = new TreePath<>(tree, chain.pop());
        while (!chain.isEmpty()) {
            final E child = chain.pop();
            final List<E> children = path.childrenOf(path.node());
            final int index = indexOf(children, child);
            if (index >= 0) {
                path.push(children.get(index), children, index);
            } else if (detachedAllowed) {
                path.push(child, Collections.singletonList(child), 0);
                path.detached[path.depth()] = true;
            } else {
                throw new IllegalArgumentException("The tree node [" + child + "] has no place in a traversal of the tree: it is not among the children of its parent [" + path.node() + "]");
            }
        }
        return path;
    }

    /**
     * Whether a node is the tree's root, the boundary of its axes: the root object itself; or a node equal to it, for
     * trees that make new node objects each time (files), unless the root object itself is above that node (so a node
     * that merely equals the root, below it, is not taken for it). A tree with no root (null) has no boundary.
     */
    static <E> boolean isRoot(final Tree<E> tree, final E node) {
        final E root = tree.getRoot();
        if (root == null || node == null) return false;
        if (node == root) return true;
        if (!node.equals(root)) return false;
        for (E above = tree.getParent(node); above != null; above = tree.getParent(above)) {
            if (above == root) return false;
        }
        return true;
    }

    /**
     * The index of a node among its siblings: the same object, else the first equal to it; -1 if neither.
     */
    static <E> int indexOf(final List<E> siblings, final E node) {
        if (siblings == null) return -1;
        for (int n = 0; n < siblings.size(); n++) {
            if (siblings.get(n) == node) return n;
        }
        return siblings.indexOf(node);
    }

    /**
     * The parent of a node within the tree: none for the tree's root (nothing above the root is in the tree).
     */
    static <E> E parentWithin(final Tree<E> tree, final E node) {
        return node == null || isRoot(tree, node) ? null : tree.getParent(node);
    }

    List<E> childrenOf(final E node) {
        if (node == null) return Collections.emptyList();
        final List<E> children = tree.getChildren(node);
        return children == null ? Collections.emptyList() : children;
    }

    E node() {
        return nodes.get(nodes.size() - 1);
    }

    /** The depth of the current node below the top (0 at the top). */
    int depth() {
        return nodes.size() - 1;
    }

    /** The current node's index among its siblings, at the given depth (1 or more). */
    int indexAt(final int depth) {
        return indexes[depth];
    }

    /** The current path's indexes, from depth 1 to the current depth (a copy). */
    int[] indexes() {
        return Arrays.copyOfRange(indexes, 0, nodes.size());
    }

    private void push(final E node, final List<E> nodeSiblings, final int index) {
        nodes.add(node);
        siblings.add(nodeSiblings);
        if (nodes.size() > indexes.length) {
            indexes = Arrays.copyOf(indexes, indexes.length * 2);
            detached = Arrays.copyOf(detached, detached.length * 2);
        }
        indexes[nodes.size() - 1] = index;
        detached[nodes.size() - 1] = false;
    }

    private void popTo(final int depth) {
        while (depth() > depth) {
            nodes.remove(nodes.size() - 1);
            siblings.remove(siblings.size() - 1);
        }
    }

    private void moveTo(final int index) {
        final int depth = depth();
        final E sibling = siblings.get(depth).get(index);
        nodes.set(depth, sibling);
        indexes[depth] = index;
    }

    private boolean hasNextSiblingAt(final int depth) {
        return depth > 0 && indexes[depth] + 1 < siblings.get(depth).size();
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Single steps
    // -----------------------------------------------------------------------------------------------------------------

    boolean firstChild() {
        final List<E> children = childrenOf(node());
        if (children.isEmpty()) return false;
        push(children.get(0), children, 0);
        return true;
    }

    boolean lastChild() {
        final List<E> children = childrenOf(node());
        if (children.isEmpty()) return false;
        push(children.get(children.size() - 1), children, children.size() - 1);
        return true;
    }

    /** Down to the first leaf below the current node, through first children (no move for a leaf). */
    void firstLeaf() {
        while (firstChild()) { }
    }

    /** Down to the last node below the current node, through last children (no move for a leaf). */
    void lastDescendantOrSelf() {
        while (lastChild()) { }
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Document order (preorder)
    // -----------------------------------------------------------------------------------------------------------------

    /** To the next node in document order. */
    boolean preorderNext() {
        return firstChild() || afterSubtree();
    }

    /**
     * To the first node after the current node and all of its descendants, in document order. After a detached node
     * (and its descendants) comes its parent's first child.
     */
    boolean afterSubtree() {
        for (int depth = depth(); depth > 0; depth--) {
            if (detached[depth]) {
                if (!childrenOf(nodes.get(depth - 1)).isEmpty()) {
                    popTo(depth - 1);
                    firstChild();
                    return true;
                }
            } else if (hasNextSiblingAt(depth)) {
                popTo(depth);
                moveTo(indexes[depth] + 1);
                return true;
            }
        }
        return false;
    }

    /** To the previous node in document order. */
    boolean preorderPrevious() {
        final int depth = depth();
        if (depth == 0) return false;
        if (indexes[depth] > 0) {
            moveTo(indexes[depth] - 1);
            lastDescendantOrSelf();
        } else {
            popTo(depth - 1);
        }
        return true;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Postorder
    // -----------------------------------------------------------------------------------------------------------------

    /** To the next node in postorder. */
    boolean postorderNext() {
        final int depth = depth();
        if (depth == 0) return false;
        if (hasNextSiblingAt(depth)) {
            moveTo(indexes[depth] + 1);
            firstLeaf();
        } else {
            popTo(depth - 1);
        }
        return true;
    }

    /** To the previous node in postorder. */
    boolean postorderPrevious() {
        if (lastChild()) return true;
        for (int depth = depth(); depth > 0; depth--) {
            if (indexes[depth] > 0) {
                popTo(depth);
                moveTo(indexes[depth] - 1);
                return true;
            }
        }
        return false;
    }
}
