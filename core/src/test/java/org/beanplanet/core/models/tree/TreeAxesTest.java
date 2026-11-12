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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * XPath's axes over the tree model, checked against an independent oracle (computed from a plain recursive walk of the
 * tree's children) for every node of several trees: forwards, backwards, and in random sequences of {@code next} and
 * {@code previous} compared step by step with {@link java.util.ListIterator}.
 *
 * <p>The trees include equal siblings (nodes are equal by name), a root below the top of the nodes (the boundary), no
 * root at all, {@link TreeNodeTree}s and {@link FilesystemTree}s. The trees used for the oracle checks refuse
 * positional access ({@link Tree#getChild}, {@link Tree#getIndexOfChild}), so the axes are shown to need only the
 * children and parent of each node.</p>
 */
class TreeAxesTest {
    // =================================================================================================================
    // Test trees
    // =================================================================================================================

    /** A node, equal to any other of the same name. */
    static final class Node {
        final String name;
        Node parent;
        final List<Node> children = new ArrayList<>();

        Node(final String name, final Node... children) {
            this.name = name;
            for (Node child : children) {
                child.parent = this;
                this.children.add(child);
            }
        }

        @Override
        public boolean equals(final Object other) {
            return other instanceof Node && ((Node) other).name.equals(name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }

        @Override
        public String toString() {
            return name;
        }
    }

    static Node n(final String name, final Node... children) {
        return new Node(name, children);
    }

    /** A tree of {@link Node}s defined only by children, parents and an optional root: positional access is refused. */
    static class NodeTree implements Tree<Node> {
        private final Node root;
        int childrenCalls, parentCalls;

        NodeTree(final Node root) {
            this.root = root;
        }

        @Override
        public Node getRoot() {
            return root;
        }

        @Override
        public Node getParent(final Node child) {
            parentCalls++;
            return child.parent;
        }

        @Override
        public List<Node> getChildren(final Node parent) {
            childrenCalls++;
            return parent.children;
        }

        @Override
        public Node getChild(final Node parent, final int childIndex) {
            throw new UnsupportedOperationException("positional access");
        }

        @Override
        public int getNumberOfChildren(final Node parent) {
            throw new UnsupportedOperationException("positional access");
        }

        @Override
        public int getIndexOfChild(final Node parent, final Node child) {
            throw new UnsupportedOperationException("positional access");
        }
    }

    /** child1/child11, child2/{child21, child22/child221}: the tree of the iterator tests. */
    static Node classicTree() {
        return n("root", n("child1", n("child11")), n("child2", n("child21"), n("child22", n("child221"))));
    }

    /** Equal siblings at every level: the same names, in different places. */
    static Node equalSiblingsTree() {
        return n("r",
                 n("a", n("x"), n("x", n("y"))),
                 n("a", n("x")),
                 n("b"),
                 n("a", n("a", n("a")), n("a")),
                 n("x"));
    }

    /** A random tree of the given size with names from a small alphabet (so many siblings are equal). */
    static Node randomTree(final long seed, final int size) {
        final Random random = new Random(seed);
        final Node root = n("t");
        final List<Node> nodes = new ArrayList<>(List.of(root));
        while (nodes.size() < size) {
            final Node parent = nodes.get(random.nextInt(nodes.size()));
            final Node child = n(String.valueOf((char) ('a' + random.nextInt(3))));
            child.parent = parent;
            parent.children.add(child);
            nodes.add(child);
        }
        return root;
    }

    static List<Node> trees() {
        return asList(classicTree(), equalSiblingsTree(), randomTree(1, 60), randomTree(2, 120), n("alone"));
    }

    // =================================================================================================================
    // The oracle
    // =================================================================================================================

    /**
     * The expected axes, from a plain recursive walk of a tree's children below a top node. Nodes are keyed by
     * identity, or by equality for trees that make new node objects each time they list children (files).
     */
    static final class Oracle<E> {
        final Tree<E> tree;
        final E top;
        final boolean identity;
        final List<E> document = new ArrayList<>();
        final List<E> postorder = new ArrayList<>();
        final Map<E, Integer> documentPosition;
        final Map<E, Integer> postorderPosition;
        final Map<E, Integer> size;
        final Map<E, E> parent;
        final Map<E, List<E>> children;

        Oracle(final Tree<E> tree, final E top, final boolean identity) {
            this.tree = tree;
            this.top = top;
            this.identity = identity;
            documentPosition = map();
            postorderPosition = map();
            size = map();
            parent = map();
            children = map();
            walk(top);
        }

        private <V> Map<E, V> map() {
            return identity ? new IdentityHashMap<>() : new HashMap<>();
        }

        private int walk(final E node) {
            documentPosition.put(node, document.size());
            document.add(node);
            final List<E> nodeChildren = tree.getChildren(node) == null ? Collections.emptyList() : new ArrayList<>(tree.getChildren(node));
            children.put(node, nodeChildren);
            int nodes = 1;
            for (E child : nodeChildren) {
                parent.put(child, node);
                nodes += walk(child);
            }
            size.put(node, nodes);
            postorderPosition.put(node, postorder.size());
            postorder.add(node);
            return nodes;
        }

        List<E> ancestorsOrSelf(final E node) {
            final List<E> ancestors = new ArrayList<>();
            for (E ancestor = node; ancestor != null; ancestor = parent.get(ancestor)) ancestors.add(ancestor);
            return ancestors;
        }

        List<E> ancestors(final E node) {
            return ancestorsOrSelf(node).subList(1, ancestorsOrSelf(node).size());
        }

        List<E> descendantsOrSelf(final E node) {
            final int at = documentPosition.get(node);
            return document.subList(at, at + size.get(node));
        }

        List<E> descendants(final E node) {
            final int at = documentPosition.get(node);
            return document.subList(at + 1, at + size.get(node));
        }

        List<E> following(final E node) {
            return document.subList(documentPosition.get(node) + size.get(node), document.size());
        }

        List<E> preceding(final E node) {
            final List<E> ancestors = ancestors(node);
            final List<E> preceding = new ArrayList<>();
            for (E before : document.subList(0, documentPosition.get(node))) {
                if (ancestors.stream().noneMatch(a -> same(a, before))) preceding.add(before);
            }
            Collections.reverse(preceding);
            return preceding;
        }

        List<E> siblings(final E node) {
            final E nodeParent = parent.get(node);
            return nodeParent == null ? Collections.emptyList() : children.get(nodeParent);
        }

        private int indexAmongSiblings(final E node) {
            final List<E> siblings = siblings(node);
            for (int i = 0; i < siblings.size(); i++) if (same(siblings.get(i), node)) return i;
            throw new AssertionError("not among its siblings: " + node);
        }

        List<E> followingSiblings(final E node) {
            if (parent.get(node) == null) return Collections.emptyList();
            final List<E> siblings = siblings(node);
            return siblings.subList(indexAmongSiblings(node) + 1, siblings.size());
        }

        List<E> precedingSiblings(final E node) {
            if (parent.get(node) == null) return Collections.emptyList();
            final List<E> preceding = new ArrayList<>(siblings(node).subList(0, indexAmongSiblings(node)));
            Collections.reverse(preceding);
            return preceding;
        }

        /** The postorder index of the first node of a node's subtree (its first leaf). */
        int postorderStart(final E node) {
            return postorderPosition.get(node) - size.get(node) + 1;
        }

        boolean same(final E a, final E b) {
            return identity ? a == b : a.equals(b);
        }
    }

    // =================================================================================================================
    // Checks
    // =================================================================================================================

    static <E> void assertSequence(final Oracle<E> oracle, final String what, final List<E> expected, final Iterator<E> actual) {
        final List<E> actualList = new ArrayList<>();
        actual.forEachRemaining(actualList::add);
        assertSameNodes(oracle, what, expected, actualList);
    }

    static <E> void assertSameNodes(final Oracle<E> oracle, final String what, final List<E> expected, final List<E> actual) {
        if (expected.size() != actual.size()) fail(what + ": expected " + expected + " but was " + actual);
        for (int i = 0; i < expected.size(); i++) {
            if (!oracle.same(expected.get(i), actual.get(i))) {
                fail(what + ": expected " + expected + " but was " + actual + " (differs at " + i + ")");
            }
        }
    }

    /**
     * The iterator behaves as a {@link ListIterator} over the expected nodes, positioned at {@code start}: all the way
     * forwards then all the way back, then for a random sequence of calls.
     */
    static <E> void assertCursor(final Oracle<E> oracle, final String what, final List<E> expected, final int start,
                                 final Supplier<TreeIterator<E>> iterators, final Random random) {
        // Forwards to the end, then back to the beginning
        TreeIterator<E> actual = iterators.get();
        final List<E> forwards = new ArrayList<>();
        while (actual.hasNext()) forwards.add(actual.next());
        assertSameNodes(oracle, what + " forwards", expected.subList(start, expected.size()), forwards);
        assertThrows(NoSuchElementException.class, actual::next, what + " next() at the end");
        final List<E> backwards = new ArrayList<>();
        while (actual.hasPrevious()) backwards.add(actual.previous());
        final List<E> expectedBackwards = new ArrayList<>(expected);
        Collections.reverse(expectedBackwards);
        assertSameNodes(oracle, what + " backwards", expectedBackwards, backwards);
        assertThrows(NoSuchElementException.class, actual::previous, what + " previous() at the beginning");

        // A new iterator: what is before its cursor, reversed
        actual = iterators.get();
        final List<E> before = new ArrayList<>(expected.subList(0, start));
        Collections.reverse(before);
        assertSameNodes(oracle, what + " reverse()", before, actual.reverse().toList());

        // Random walks, step by step against ListIterator
        for (int walk = 0; walk < 3; walk++) {
            final ListIterator<E> model = expected.listIterator(start);
            actual = iterators.get();
            final List<String> calls = new ArrayList<>();
            final int forwardBias = random.nextInt(3) + 1;
            for (int step = 0; step < 3 * expected.size() + 6; step++) {
                final int call = random.nextInt(2 + 2 * forwardBias);
                final String trace = what + " after " + calls;
                if (call == 0) {
                    calls.add("hasPrevious");
                    assertEquals(model.hasPrevious(), actual.hasPrevious(), trace + " hasPrevious()");
                } else if (call == 1) {
                    calls.add("previous");
                    if (model.hasPrevious()) {
                        final E node = model.previous();
                        final E actualNode = actual.previous();
                        if (!oracle.same(node, actualNode)) fail(trace + " previous(): expected " + node + " but was " + actualNode);
                    } else {
                        assertThrows(NoSuchElementException.class, actual::previous, trace + " previous()");
                    }
                } else if (call % 2 == 0) {
                    calls.add("hasNext");
                    assertEquals(model.hasNext(), actual.hasNext(), trace + " hasNext()");
                } else {
                    calls.add("next");
                    if (model.hasNext()) {
                        final E node = model.next();
                        final E actualNode = actual.next();
                        if (!oracle.same(node, actualNode)) fail(trace + " next(): expected " + node + " but was " + actualNode);
                    } else {
                        assertThrows(NoSuchElementException.class, actual::next, trace + " next()");
                    }
                }
            }
        }
    }

    /** Every axis and traversal, from every node below the oracle's top. */
    static <E> void assertAllAxes(final Tree<E> tree, final Oracle<E> oracle, final Function<E, String> name) {
        final Random random = new Random(42);
        for (E node : oracle.document) {
            final String at = " from " + name.apply(node);

            assertCursor(oracle, "ancestor" + at, oracle.ancestors(node), 0, () -> tree.ancestorIterator(node), random);
            assertCursor(oracle, "ancestor-or-self" + at, oracle.ancestorsOrSelf(node), 0, () -> tree.ancestorOrSelfIterator(node), random);
            assertCursor(oracle, "child" + at, oracle.children.get(node), 0, () -> tree.childIterator(node), random);
            assertCursor(oracle, "descendant" + at, oracle.descendants(node), 0, () -> tree.descendantIterator(node), random);
            assertCursor(oracle, "descendant-or-self" + at, oracle.descendantsOrSelf(node), 0, () -> tree.descendantOrSelfIterator(node), random);
            assertCursor(oracle, "following" + at, oracle.following(node), 0, () -> tree.followingIterator(node), random);
            assertCursor(oracle, "following-sibling" + at, oracle.followingSiblings(node), 0, () -> tree.followingSiblingIterator(node), random);
            assertCursor(oracle, "preceding" + at, oracle.preceding(node), 0, () -> tree.precedingIterator(node), random);
            assertCursor(oracle, "preceding-sibling" + at, oracle.precedingSiblings(node), 0, () -> tree.precedingSiblingIterator(node), random);

            assertSequence(oracle, "ancestor stream" + at, oracle.ancestors(node), tree.ancestorStream(node).iterator());
            assertSequence(oracle, "ancestor-or-self stream" + at, oracle.ancestorsOrSelf(node), tree.ancestorOrSelfStream(node).iterator());
            assertSequence(oracle, "child stream" + at, oracle.children.get(node), tree.childStream(node).iterator());
            assertSequence(oracle, "descendant stream" + at, oracle.descendants(node), tree.descendantStream(node).iterator());
            assertSequence(oracle, "descendant-or-self stream" + at, oracle.descendantsOrSelf(node), tree.descendantOrSelfStream(node).iterator());
            assertSequence(oracle, "following stream" + at, oracle.following(node), tree.followingStream(node).iterator());
            assertSequence(oracle, "following-sibling stream" + at, oracle.followingSiblings(node), tree.followingSiblingStream(node).iterator());
            assertSequence(oracle, "preceding stream" + at, oracle.preceding(node), tree.precedingStream(node).iterator());
            assertSequence(oracle, "preceding-sibling stream" + at, oracle.precedingSiblings(node), tree.precedingSiblingStream(node).iterator());

            // parent and self
            final E parent = oracle.parent.get(node);
            if (parent != null) {
                if (!oracle.same(parent, tree.getParent(node))) fail("parent" + at + ": expected " + parent + " but was " + tree.getParent(node));
            }

            // The siblings of the node, one at a time
            final List<E> following = oracle.followingSiblings(node), preceding = oracle.precedingSiblings(node);
            final E next = tree.getNextSibling(node), previous = tree.getPreviousSibling(node);
            if (following.isEmpty()) assertNull(next, "next sibling" + at);
            else if (!oracle.same(following.get(0), next)) fail("next sibling" + at + ": expected " + following.get(0) + " but was " + next);
            if (preceding.isEmpty()) assertNull(previous, "previous sibling" + at);
            else if (!oracle.same(preceding.get(0), previous)) fail("previous sibling" + at + ": expected " + preceding.get(0) + " but was " + previous);

            // Traversals positioned at the node, within the tree
            assertCursor(oracle, "preorder" + at, oracle.document, oracle.documentPosition.get(node), () -> tree.preorderIterator(node), random);
            assertCursor(oracle, "postorder" + at, oracle.postorder, oracle.postorderStart(node), () -> tree.postorderIterator(node), random);
            assertSequence(oracle, "preorder stream" + at, oracle.document.subList(oracle.documentPosition.get(node), oracle.document.size()), tree.preorderStream(node).iterator());
            assertSequence(oracle, "postorder stream" + at, oracle.postorder.subList(oracle.postorderStart(node), oracle.postorder.size()), tree.postorderStream(node).iterator());

            // The node's subtree
            final Tree<E> subtree = tree.subtree(node);
            assertSequence(oracle, "subtree preorder" + at, oracle.descendantsOrSelf(node), subtree.preorderIterator());
            assertSequence(oracle, "subtree postorder" + at, oracle.postorder.subList(oracle.postorderStart(node), oracle.postorderPosition.get(node) + 1), subtree.postorderIterator());
        }

        // Whole-tree traversals
        assertCursor(oracle, "preorder", oracle.document, 0, tree::preorderIterator, random);
        assertCursor(oracle, "postorder", oracle.postorder, 0, tree::postorderIterator, random);
        assertCursor(oracle, "parent-unaware preorder", oracle.document, 0, tree::preorderParentUnawareIterator, random);
        assertSequence(oracle, "iterator()", oracle.document, tree.iterator());
        assertSequence(oracle, "stream()", oracle.document, tree.stream().iterator());
        assertEquals(oracle.document.size(), tree.size());
    }

    // =================================================================================================================
    // XPath's axes on node trees
    // =================================================================================================================

    @Test
    void allAxes_rootAtTheTop() {
        for (Node root : trees()) {
            final NodeTree tree = new NodeTree(root);
            assertAllAxes(tree, new Oracle<>(tree, root, true), node -> node.name + "@" + System.identityHashCode(node));
        }
    }

    @Test
    void allAxes_rootBelowTheTop_isTheBoundary() {
        for (Node top : trees()) {
            for (Node root : new Oracle<>(new NodeTree(top), top, true).document) {
                if (root.children.isEmpty() || root == top) continue;
                final NodeTree tree = new NodeTree(root);
                assertAllAxes(tree, new Oracle<>(tree, root, true), node -> node.name + "@" + System.identityHashCode(node));
            }
        }
    }

    @Test
    void allAxes_noRoot_extendsToTheTop() {
        for (Node top : trees()) {
            final NodeTree tree = new NodeTree(null) {
                @Override
                public Node getRoot() {
                    return null;
                }
            };
            final Oracle<Node> oracle = new Oracle<>(tree, top, true);
            final Random random = new Random(7);
            for (Node node : oracle.document) {
                assertCursor(oracle, "ancestor from " + node, oracle.ancestors(node), 0, () -> tree.ancestorIterator(node), random);
                assertCursor(oracle, "ancestor-or-self from " + node, oracle.ancestorsOrSelf(node), 0, () -> tree.ancestorOrSelfIterator(node), random);
                assertCursor(oracle, "descendant from " + node, oracle.descendants(node), 0, () -> tree.descendantIterator(node), random);
                assertCursor(oracle, "following from " + node, oracle.following(node), 0, () -> tree.followingIterator(node), random);
                assertCursor(oracle, "following-sibling from " + node, oracle.followingSiblings(node), 0, () -> tree.followingSiblingIterator(node), random);
                assertCursor(oracle, "preceding from " + node, oracle.preceding(node), 0, () -> tree.precedingIterator(node), random);
                assertCursor(oracle, "preceding-sibling from " + node, oracle.precedingSiblings(node), 0, () -> tree.precedingSiblingIterator(node), random);
                assertCursor(oracle, "preorder from " + node, oracle.document, oracle.documentPosition.get(node), () -> tree.preorderIterator(node), random);
            }
            // With no root, there is nothing to traverse as a whole
            assertFalse(tree.preorderIterator().hasNext());
            assertFalse(tree.postorderIterator().hasNext());
        }
    }

    @Test
    void axesFromANodeOutsideTheRoot_extendToItsOldestAncestor() {
        final Node top = classicTree();
        final Node child1 = top.children.get(0), child2 = top.children.get(1), child22 = child2.children.get(1);
        final NodeTree tree = new NodeTree(child2);
        assertEquals(asList(child1, top), tree.ancestorOrSelfStream(child1).collect(Collectors.toList()));
        assertEquals(asList(child22, child2), tree.ancestorOrSelfStream(child22).collect(Collectors.toList()));
    }

    // =================================================================================================================
    // The examples, by name
    // =================================================================================================================

    static List<String> names(final Stream<Node> nodes) {
        return nodes.map(node -> node.name).collect(Collectors.toList());
    }

    @Test
    void examples_classicTree() {
        final Node root = classicTree();
        final Node child2 = root.children.get(1), child22 = child2.children.get(1), child221 = child22.children.get(0);
        final NodeTree tree = new NodeTree(root);

        assertEquals(asList("child22", "child2", "root"), names(tree.ancestorStream(child221)));
        assertEquals(asList("child221", "child22", "child2", "root"), names(tree.ancestorOrSelfStream(child221)));
        assertEquals(asList("child21", "child22"), names(tree.childStream(child2)));
        assertEquals(asList("child21", "child22", "child221"), names(tree.descendantStream(child2)));
        assertEquals(asList("child2", "child21", "child22", "child221"), names(tree.descendantOrSelfStream(child2)));
        assertEquals(asList("child2", "child21", "child22", "child221"), names(tree.followingStream(root.children.get(0).children.get(0))));
        assertEquals(asList("child22"), names(tree.followingSiblingStream(child2.children.get(0))));
        assertEquals(asList("child21", "child11", "child1"), names(tree.precedingStream(child221)));
        assertEquals(asList("child21"), names(tree.precedingSiblingStream(child22)));
        assertEquals(asList(), names(tree.precedingSiblingStream(root)));
        assertEquals(asList(), names(tree.followingStream(root)));
        assertEquals(asList(), names(tree.precedingStream(root)));
    }

    @Test
    void examples_equalSiblings_eachVisitedOnceInItsPlace() {
        final Node root = equalSiblingsTree();
        final NodeTree tree = new NodeTree(root);
        final Node secondA = root.children.get(1), fourthA = root.children.get(3);

        assertEquals(asList("a", "x", "x", "y", "a", "x", "b", "a", "a", "a", "a", "x"), names(tree.descendantStream(root)));
        assertEquals(asList("b", "a", "a", "a", "a", "x"), names(tree.followingStream(secondA)));
        assertSame(root.children.get(2), tree.followingSiblingIterator(secondA).next());
        assertSame(root.children.get(0), tree.precedingSiblingIterator(secondA).next());
        assertSame(root.children.get(2), tree.getNextSibling(secondA));
        assertSame(root.children.get(0), tree.getPreviousSibling(secondA));
        // The innermost a: its ancestors are the a above it, the fourth a and the root
        final Node innermost = fourthA.children.get(0).children.get(0);
        final List<Node> ancestors = tree.ancestorStream(innermost).collect(Collectors.toList());
        assertSame(fourthA.children.get(0), ancestors.get(0));
        assertSame(fourthA, ancestors.get(1));
        assertSame(root, ancestors.get(2));
    }

    @Test
    void examples_rootBelowTheTop() {
        final Node top = classicTree();
        final Node child2 = top.children.get(1), child21 = child2.children.get(0), child22 = child2.children.get(1);
        final NodeTree tree = new NodeTree(child2);

        assertEquals(asList("child22", "child2"), names(tree.ancestorStream(child22.children.get(0))));
        assertEquals(asList(), names(tree.ancestorStream(child2)));
        assertEquals(asList(), names(tree.followingSiblingStream(child2)));
        assertEquals(asList(), names(tree.precedingSiblingStream(child2)));
        assertEquals(asList(), names(tree.followingStream(child22)));
        assertEquals(asList("child21"), names(tree.precedingStream(child22.children.get(0))));
        assertNull(tree.getNextSibling(child2));
        assertNull(tree.getPreviousSibling(child2));
        assertSame(child22, tree.getNextSibling(child21));
    }

    // =================================================================================================================
    // Nodes that are not among their parent's children (as XML attributes)
    // =================================================================================================================

    /** A node whose parent is the given node, without being one of its children. */
    static Node detached(final Node parent, final Node node) {
        node.parent = parent;
        return node;
    }

    @Test
    void detachedNodes_asXPathAttributes() {
        final Node root = classicTree();
        final Node child1 = root.children.get(0), child2 = root.children.get(1), child21 = child2.children.get(0);
        final Node id = detached(child2, n("@id")), x = detached(child21, n("@x")), r = detached(root, n("@r"));
        final NodeTree tree = new NodeTree(root);

        // An attribute of an element with children: after its element, before the element's children
        assertEquals(asList("child2", "root"), names(tree.ancestorStream(id)));
        assertEquals(asList("@id", "child2", "root"), names(tree.ancestorOrSelfStream(id)));
        assertEquals(asList(), names(tree.followingSiblingStream(id)));
        assertEquals(asList(), names(tree.precedingSiblingStream(id)));
        assertNull(tree.getNextSibling(id));
        assertNull(tree.getPreviousSibling(id));
        assertEquals(asList("child21", "child22", "child221"), names(tree.followingStream(id)));
        assertEquals(asList("child11", "child1"), names(tree.precedingStream(id)));
        assertEquals(asList(), names(tree.descendantStream(id)));
        assertEquals(asList("@id"), names(tree.descendantOrSelfStream(id)));

        // Of a leaf element: the element's following nodes
        assertEquals(asList("child22", "child221"), names(tree.followingStream(x)));
        assertEquals(asList("child11", "child1"), names(tree.precedingStream(x)));

        // Of the root: all of the root's descendants follow it, nothing precedes it
        assertEquals(asList("child1", "child11", "child2", "child21", "child22", "child221"), names(tree.followingStream(r)));
        assertEquals(asList(), names(tree.precedingStream(r)));
        assertEquals(asList("root"), names(tree.ancestorStream(r)));

        // Both ways, as a ListIterator
        final Oracle<Node> oracle = new Oracle<>(tree, root, true);
        final Random random = new Random(3);
        assertCursor(oracle, "following from @id", asList(child21, child2.children.get(1), child2.children.get(1).children.get(0)), 0, () -> tree.followingIterator(id), random);
        assertCursor(oracle, "preceding from @id", asList(child1.children.get(0), child1), 0, () -> tree.precedingIterator(id), random);
        assertCursor(oracle, "ancestor from @id", asList(child2, root), 0, () -> tree.ancestorIterator(id), random);

        // A traversal of the whole tree has no place for it
        assertThrows(IllegalArgumentException.class, () -> tree.preorderIterator(id).hasNext());
        assertThrows(IllegalArgumentException.class, () -> tree.postorderIterator(id).hasNext());
    }

    @Test
    void detachedNodes_withChildren() {
        final Node root = classicTree();
        final Node child1 = root.children.get(0);
        final Node k1 = n("k1"), k2 = n("k2", n("k21"));
        final Node detachedNode = detached(child1, n("@a", k1, k2));
        final NodeTree tree = new NodeTree(root);

        assertEquals(asList("k2", "k21", "child11", "child2", "child21", "child22", "child221"), names(tree.followingStream(k1)));
        assertEquals(asList("k1"), names(tree.precedingStream(k2)));
        assertEquals(asList("k1"), names(tree.precedingStream(k2.children.get(0))));
        assertEquals(asList("k2"), names(tree.followingSiblingStream(k1)));
        assertEquals(asList("@a", "child1", "root"), names(tree.ancestorStream(k1)));
        assertEquals(asList("k1", "k2", "k21"), names(tree.descendantStream(detachedNode)));
    }

    // =================================================================================================================
    // Laziness
    // =================================================================================================================

    /** A tree of infinite depth: every node has two children. */
    static final class InfiniteTree implements Tree<String> {
        int childrenCalls, parentCalls;

        @Override
        public String getRoot() {
            return "r";
        }

        @Override
        public String getParent(final String child) {
            parentCalls++;
            final int dot = child.lastIndexOf('.');
            return dot < 0 ? null : child.substring(0, dot);
        }

        @Override
        public List<String> getChildren(final String parent) {
            childrenCalls++;
            return asList(parent + ".0", parent + ".1");
        }
    }

    @Test
    void lazy_infiniteTree() {
        final InfiniteTree tree = new InfiniteTree();
        assertEquals(asList("r.0", "r.0.0", "r.0.0.0"), tree.descendantStream("r").limit(3).collect(Collectors.toList()));
        assertEquals(asList("r", "r.0", "r.0.0"), tree.preorderStream().limit(3).collect(Collectors.toList()));
        assertEquals(asList("r.0.1", "r.0.1.0", "r.0.1.0.0"), tree.followingStream("r.0.0").limit(3).collect(Collectors.toList()));
        assertEquals(asList("r.1"), tree.followingSiblingStream("r.0").collect(Collectors.toList()));

        final TreeIterator<String> descendants = tree.descendantIterator("r.1");
        assertEquals("r.1.0", descendants.next());
        assertEquals("r.1.0.0", descendants.next());
        assertEquals("r.1.0.0", descendants.previous());
        assertEquals("r.1.0", descendants.previous());
        assertFalse(descendants.hasPrevious());
    }

    @Test
    void lazy_nothingIsDoneUntilUsed_andOnlyWhatIsNeeded() {
        final InfiniteTree tree = new InfiniteTree();
        final String deep = "r.0.1.1.0.1.0.0.1";

        final TreeIterator<String> ancestors = tree.ancestorIterator(deep);
        assertEquals(0, tree.parentCalls);
        assertEquals("r.0.1.1.0.1.0.0", ancestors.next());
        assertEquals(1, tree.parentCalls);
        assertEquals("r.0.1.1.0.1.0", ancestors.next());
        assertEquals(2, tree.parentCalls);
        assertEquals("r.0.1.1.0.1.0", ancestors.previous());
        assertEquals("r.0.1.1.0.1.0.0", ancestors.previous());
        assertEquals(2, tree.parentCalls);

        tree.childrenCalls = 0;
        final Iterator<String> descendants = tree.descendantIterator("r");
        assertEquals(0, tree.childrenCalls);
        descendants.next();
        assertEquals(1, tree.childrenCalls);
        tree.parentCalls = 0;
        tree.descendantStream("r.0.1").limit(10).count();
        assertEquals(0, tree.parentCalls, "the descendant axis consults no parents");
    }

    @Test
    void lazy_wideTree_eachChildListedOnce_noPositionalAccess() {
        final List<Node> children = new ArrayList<>();
        for (int n = 0; n < 200_000; n++) children.add(n("c"));
        final Node root = n("root", children.toArray(new Node[0]));
        final NodeTree tree = new NodeTree(root);

        assertEquals(200_001, tree.preorderStream().count());
        assertEquals(200_001, tree.childrenCalls, "each node's children are listed once");
        assertEquals(199_999, tree.followingSiblingStream(children.get(0)).count());
        assertEquals(199_999, tree.precedingSiblingStream(children.get(199_999)).count());
        assertEquals(199_999, tree.followingStream(children.get(0)).count());
        assertEquals(199_999, tree.precedingStream(children.get(199_999)).count());
        assertEquals(200_001, tree.postorderStream().count());
    }

    // =================================================================================================================
    // TreeNodeTree
    // =================================================================================================================

    static TreeNode<String> treeNodeOf(final Node node) {
        final TreeNode<String>[] children = node.children.stream().map(TreeAxesTest::treeNodeOf).toArray(TreeNode[]::new);
        return new TreeNode<>(node.name, children);
    }

    @Test
    void allAxes_treeNodeTrees() {
        for (Node source : trees()) {
            final TreeNode<String> root = treeNodeOf(source);
            final TreeNodeTree<String> tree = new TreeNodeTree<>(root);
            assertAllAxes(tree, new Oracle<>(tree, root, true), node -> node + "@" + System.identityHashCode(node));

            // Bounded at each inner node
            for (TreeNode<String> inner : new Oracle<>(tree, root, true).document) {
                if (inner == root || inner.getNumberOfChildren() == 0) continue;
                final TreeNodeTree<String> subtree = new TreeNodeTree<>(inner);
                assertAllAxes(subtree, new Oracle<>(subtree, inner, true), node -> node + "@" + System.identityHashCode(node));
            }
        }
    }

    @Test
    void treeNode_equalSubtrees_areEachTheirOwnSibling() {
        final TreeNode<String> first = new TreeNode<>("a", new TreeNode<>("x"));
        final TreeNode<String> second = new TreeNode<>("a", new TreeNode<>("x"));
        final TreeNode<String> root = new TreeNode<>("root", first, second);
        final TreeNodeTree<String> tree = new TreeNodeTree<>(root);

        assertEquals(first, second);
        assertEquals(1, tree.getIndexOfChild(root, second));
        assertSame(second, tree.getNextSibling(first));
        assertNull(tree.getNextSibling(second));
        assertSame(first, tree.getPreviousSibling(second));
        assertEquals(asList(), tree.followingSiblingStream(second).collect(Collectors.toList()));
        assertEquals(4, tree.descendantStream(root).count());
    }

    // =================================================================================================================
    // FilesystemTree
    // =================================================================================================================

    static File fileTree(final Path dir) throws IOException {
        for (String directory : asList("t/a/a1", "t/a/a2", "t/b", "t/c/c1/c11")) Files.createDirectories(dir.resolve(directory));
        for (String file : asList("t/a/a1/x", "t/a/a2/y", "t/a/f", "t/b/z", "t/c/c1/c11/w", "t/c/v", "t/g")) Files.createFile(dir.resolve(file));
        return dir.resolve("t").toFile();
    }

    @Test
    void allAxes_filesystemTree(@TempDir final Path dir) throws IOException {
        final File top = fileTree(dir);
        final FilesystemTree tree = new FilesystemTree(top);
        assertAllAxes(tree, new Oracle<>(tree, top, false), file -> top.toPath().relativize(file.toPath()).toString());

        final File a = new File(top, "a");
        final FilesystemTree bounded = new FilesystemTree(a);
        assertAllAxes(bounded, new Oracle<>(bounded, a, false), file -> top.toPath().relativize(file.toPath()).toString());
    }

    @Test
    void filesystemTree_examples(@TempDir final Path dir) throws IOException {
        final File top = fileTree(dir);
        final FilesystemTree tree = new FilesystemTree(top);
        final Function<Stream<File>, List<String>> relative = files -> files.map(f -> top.toPath().relativize(f.toPath()).toString()).collect(Collectors.toList());

        assertEquals(asList("a", "a/a1", "a/a1/x", "a/a2", "a/a2/y", "a/f", "b", "b/z", "c", "c/c1", "c/c1/c11", "c/c1/c11/w", "c/v", "g"), relative.apply(tree.descendantStream(top)));
        assertEquals(asList("a/a2", "a/a2/y", "a/f", "b", "b/z", "c", "c/c1", "c/c1/c11", "c/c1/c11/w", "c/v", "g"), relative.apply(tree.followingStream(new File(top, "a/a1"))));
        assertEquals(asList("a/f", "a/a2/y", "a/a2", "a/a1/x", "a/a1", "a"), relative.apply(tree.precedingStream(new File(top, "b"))));
        assertEquals(asList("b", "a"), relative.apply(tree.precedingSiblingStream(new File(top, "c"))));
        assertEquals(asList("a/a1", "a", ""), relative.apply(tree.ancestorStream(new File(top, "a/a1/x"))));
    }

    @Test
    void filesystemTree_withoutRoot_ancestorsReachTheFilesystemRoot(@TempDir final Path dir) throws IOException {
        final File top = fileTree(dir);
        final File x = new File(top, "a/a1/x").getAbsoluteFile();
        final List<File> ancestors = new FilesystemTree().ancestorStream(x).collect(Collectors.toList());
        assertEquals(new File(top, "a/a1").getAbsoluteFile(), ancestors.get(0));
        assertNull(ancestors.get(ancestors.size() - 1).getParentFile(), "the last ancestor is the filesystem's root");
        assertTrue(ancestors.contains(top.getAbsoluteFile()));
    }

    @Test
    void fileUtil_deletesATreeInPostorder(@TempDir final Path dir) throws IOException {
        final File top = fileTree(dir);
        assertTrue(org.beanplanet.core.io.FileUtil.deleteIgnoringErrors(top));
        assertFalse(top.exists());
    }

    // =================================================================================================================
    // Positional defaults, the child iterator's changes and the filtered tree
    // =================================================================================================================

    /** A tree defined by children and parents alone, using the defaults for positional access. */
    static class MinimalTree implements Tree<Node> {
        private final Node root;

        MinimalTree(final Node root) {
            this.root = root;
        }

        @Override
        public Node getRoot() {
            return root;
        }

        @Override
        public Node getParent(final Node child) {
            return child.parent;
        }

        @Override
        public List<Node> getChildren(final Node parent) {
            return parent.children;
        }

        @Override
        public boolean remove(final Node parent, final Node child) {
            for (Iterator<Node> children = parent.children.iterator(); children.hasNext(); ) {
                if (children.next() == child) {
                    children.remove();
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean set(final Node parent, final Node old, final Node replacement) {
            for (int n = 0; n < parent.children.size(); n++) {
                if (parent.children.get(n) == old) {
                    parent.children.set(n, replacement);
                    replacement.parent = parent;
                    return true;
                }
            }
            return false;
        }
    }

    @Test
    void positionalDefaults_fromChildren() {
        final Node root = equalSiblingsTree();
        final MinimalTree tree = new MinimalTree(root);
        assertEquals(5, tree.getNumberOfChildren(root));
        assertSame(root.children.get(3), tree.getChild(root, 3));
        assertEquals(3, tree.getIndexOfChild(root, root.children.get(3)), "by identity, though equal to the first a");
        assertEquals(0, tree.getIndexOfChild(root, n("a")), "by equality, failing identity");
        assertEquals(-1, tree.getIndexOfChild(root, n("q")));
        assertTrue(tree.isLeaf(root.children.get(2)));
        assertFalse(tree.isLeaf(root));
        assertEquals(0, tree.getNumberOfChildren(null));
        assertThrows(IndexOutOfBoundsException.class, () -> tree.getChild(root, 5));
    }

    @Test
    void childIterator_removeAndSet_asListIterator() {
        final Node root = n("root", n("a"), n("b"), n("c"), n("d"));
        final MinimalTree tree = new MinimalTree(root);
        final List<Node> model = new ArrayList<>(root.children);
        final ListIterator<Node> expected = model.listIterator();
        final TreeIterator<Node> actual = tree.childIterator(root);

        assertThrows(IllegalStateException.class, actual::remove);
        assertSame(expected.next(), actual.next());   // a
        assertSame(expected.next(), actual.next());   // b
        expected.remove();
        actual.remove();                               // b removed
        assertEquals(model, root.children);
        assertThrows(IllegalStateException.class, actual::remove);
        assertSame(expected.next(), actual.next());   // c
        final Node e = n("e");
        expected.set(e);
        actual.set(e);                                 // c replaced by e
        assertEquals(model, root.children);
        assertSame(expected.previous(), actual.previous()); // e
        assertSame(expected.previous(), actual.previous()); // a
        expected.remove();
        actual.remove();                               // a removed
        assertEquals(model, root.children);
        assertEquals(expected.hasPrevious(), actual.hasPrevious());
        assertSame(expected.next(), actual.next());   // e
        assertSame(expected.next(), actual.next());   // d
        assertFalse(actual.hasNext());
    }

    @Test
    void filteredTree_parentIsTheParent() {
        final Node root = classicTree();
        final Node child2 = root.children.get(1), child22 = child2.children.get(1);
        final FilteredTree<Node> tree = new FilteredTree<>(new MinimalTree(root), node -> !node.name.equals("child21"));
        assertSame(child2, tree.getParent(child22));
        assertNull(tree.getParent(root));
        assertEquals(asList("child2", "child22", "child221"), names(tree.descendantOrSelfStream(child2)));
        assertEquals(asList("child2", "root"), names(tree.ancestorStream(child22)));
        assertEquals(asList(), names(tree.precedingSiblingStream(child22)));
    }

    @Test
    void treeNode_getChild_outOfRange() {
        final TreeNode<String> root = new TreeNode<>("root", new TreeNode<>("a"));
        assertEquals("a", root.getChild(0).getManagedObject());
        assertNull(root.getChild(1));
        assertNull(root.getChild(-1));
    }

    @Test
    void nullFrom_emptyAxes() {
        final MinimalTree tree = new MinimalTree(classicTree());
        assertFalse(tree.ancestorOrSelfIterator(null).hasNext());
        assertFalse(tree.descendantIterator(null).hasNext());
        assertFalse(tree.followingIterator(null).hasNext());
        assertFalse(tree.precedingIterator(null).hasNext());
        assertFalse(tree.followingSiblingIterator(null).hasNext());
        assertFalse(tree.childIterator(null).hasNext());
        assertNull(tree.getNextSibling(null));
        assertFalse(new MinimalTree(null).preorderIterator().hasNext());
    }
}
