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

import org.beanplanet.core.lang.ShallowCopyable;
import org.beanplanet.core.models.iterators.TwoStageIterator;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.beanplanet.core.util.IteratorUtil.asStream;

/**
 * Model of a tree. This model makes no assumption about how the tree node information is stored or where it comes from.
 *
 * @author Gary Watson
 */
public interface Tree<E> extends TreeIterators<E>, TreeStreams<E>, Serializable, ShallowCopyable<Tree<E>> {
    /**
     * Performs shallow copy of this tree. Not implemented in this abstraction.
     *
     * @return a new shallow-copied instance of the tree.
     * @throws UnsupportedOperationException as subclasses must override this method to produce a specific tree.
     */
    default Tree<E> copyShallow() { throw new UnsupportedOperationException("Shallow copy of abstract tree not supported!"); }

    /**
    * Returns the root of the tree, if this tree model represents a single tree instance.
    * <p>
    * This could be an instance of <code>TreeNode</code> or any other use-defined type. This model does not place any
    * constraints on the type of nodes in the tree model.
    * </p>
    *
    * @return the root node/object of the tree,
    * @see #getRoot(Object) for a non-instance alternative
    */
    E getRoot();

    /**
     * Returns a subtree of this tree, rooted at the given node.
     * <p>
     * This could be an instance of <code>TreeNode</code> or any other use-defined type. This model does not place any
     * constraints on the type of nodes in the tree model.
     * </p>
     *
     * @param from the new tree model root.
     * @return the substree of this tree, from the given node as root of the subtree.
     */
    default Tree<E> subtree(E from) {
        throw new UnsupportedOperationException("Subtree is not supported!");
    }

    /**
    * Returns the root of the tree associated with the specified from node, if supported by the tree model.
    * <p>
    * This could be an instance of <code>TreeNode</code> or any other use-defined type. This model does not place any
    * constraints on the type of nodes in the tree model.
    *
    * @param fromNode a node on the tree whose root is to be returned.
    * @return the root node/object of the tree which will be either the oldest ancestor with no parent node or the node
    * returned from a call to {@link #getRoot()} if that node is encountered first when walking the ancestry path from
    * the given node.
    * @see #getRoot() for an instance specific alternative
    */
    default E getRoot(E fromNode) {
        E modelRootNode = getRoot();
        while (fromNode != null) {
            E parent = getParent(fromNode);
            if (parent == null || fromNode.equals(modelRootNode)) return fromNode;
            fromNode = parent;
        }

        return fromNode;
    }

    /**
    * Returns the parent node of the specified child.
    *
    * @param child the child whose parent is to be returned.
    * @return the parent of the specified child node, or null if the child has no parent (is the root node).
    */
    E getParent(E child);

    /**
     * Returns the previous sibling of the node specified.
     *
     * @return the sibling preceding that of the given node, or null if there is none.
     * @param fromNode the node whose previous sibling is to be returned, which may not be null.
     */
    default E getPreviousSibling(E fromNode) {
        if (fromNode == null) return null;

        E parent = getParent(fromNode);
        int childIndex = getIndexOfChild(parent, fromNode);
        return childIndex > 0 ? getChild(parent, childIndex-1) : null;
    }


    /**
     * Returns the next sibling of the node specified.
     *
     * @return the sibling following that of the given node, or null if there is none.
     * @param fromNode the node whose next sibling is to be returned, which may not be null.
     */
    default E getNextSibling(E fromNode) {
        if (fromNode == null) return null;

        E parent = getParent(fromNode);
        int childIndex = getIndexOfChild(parent, fromNode);
        return childIndex < getNumberOfChildren(parent)-1 ? getChild(parent, childIndex+1) : null;
    }

    /**
    * Returns the child node at the specified index of the parent node specified.
    *
    * @return the node at the specified index of the parent.
    * @param parent the parent whose child node is to be located.
    * @param childIndex the index of the child to obtain.
    * @exception ArrayIndexOutOfBoundsException thrown if the index specified is out of range.
    */
    E getChild(E parent, int childIndex);

    /**
    * Returns the number of child nodes of the node specified.
    *
    * @return the number of children
    */
    int getNumberOfChildren(E parent);

    /**
    * Returns the index of a child node, within the list of child nodes of a parent node.
    *
    * @param parent the parent of the child whose index is to be found.
    * @param child the node whose index is to be found.
    * @return the index of the specified child, within the list of child nodes of a parent, or -1 if the node is not a
    *         child of the given parent.
    */
    int getIndexOfChild(E parent, E child);

    /**
    * Determines whether the specified node on the tree is a leaf node. A leaf node is a terminal node (a node with no
    * children).
    *
    * @param node the node to be tested as a leaf node.
    * @return true is the node is known to be a leaf node, false otherwise.
    */
    default boolean isLeaf(E node) {
        return getNumberOfChildren(node) == 0;
    }

    /**
     * Returns an iterator over the ancestors of the specified from node.
     *
     * @param from the node whose ancestor nodes iterator is to be returned.
     * @return an iterator over the ancestors of the specified from node; empty if the from node has no ancestors.
     */
    @Override
    default TreeIterator<E> ancestorIterator(E from) {
        return new AncestorIterator<>(this, from);
    }

    /**
     * Returns an iterator over the ancestors or self of the specified from node.
     *
     * @param from the node whose ancestor or self nodes iterator is to be returned.
     * @return an iterator over the ancestors or self of the specified from node.
     */
    @Override
    default TreeIterator<E> ancestorOrSelfIterator(E from) {
        return new AncestorOrSelfIterator<>(this, from);
    }

    /**
     * Returns an iterator over the children of the specified parent node. Optionally, the iterator backs the
     * children and modifications to the iterator, such as <code>{@link ListIterator#add(Object)}</code> or
     * <code>{@link ListIterator#set(Object)}</code>, affect the children of the given node.
     *
     * @param parent the parent whose child iterator is to be returned.
     * @return an iterator over the children of the specified parent node; empty if the parent has no children.
     */
    @Override
    default TreeIterator<E> childIterator(E parent) {
        return new ChildIterator<>(this, parent);
    }

    /**
     * Returns an iterator over the descendant nodes from the given node.
     *
     * @param from the node whose descendant nodes iterator is to be returned.
     * @return an iterator over the descendant nodes from the given node.
     */
    @Override
    default Iterator<E> descendantIterator(E from) {
        return new TwoStageIterator<>(childIterator(from), this::preorderIterator);
    }

    /**
     * Returns an iterator over the descendant or self nodes deom the given node. This is a convenience method as
     * the descendant-or-self axis is equivalent to the pre-order axis.
     *
     * @param from the node whose descendant or self nodes iterator is to be returned.
     * @return an iterator over the descendant or self nodes from the given node; empty if the parent has no children.
     */
    @Override
    default TreeIterator<E> descendantOrSelfIterator(E from) {
        return preorderIterator();
    }

    /**
     * Returns an iterator over the following nodes from the given node.
     *
     * @param from the node whose following nodes iterator is to be returned.
     * @return an iterator over the following nodes from the given node.
     */
    @Override
    default Iterator<E> followingIterator(E from) {
        return new TwoStageIterator<>(ancestorOrSelfIterator(from), a -> new TwoStageIterator<>(followingSiblingIterator(a), fs -> subtree(fs).preorderIterator()));
    }

    /**
     * Returns an iterator over the following sibling nodes from the given node.
     *
     * @param from the node whose following sibling nodes iterator is to be returned.
     * @return an iterator over the following sibling nodes from the given node.
     */
    @Override
    default Iterator<E> followingSiblingIterator(E from) {
        return new FollowingSiblingIterator<>(this, from);
    }

    /**
     * Returns an iterator over the preceding nodes from the given node.
     *
     * @param from the node whose preceding nodes iterator is to be returned.
     * @return an iterator over the preceding nodes from the given node.
     */
    @Override
    default Iterator<E> precedingIterator(E from) {
        return new TwoStageIterator<>(ancestorOrSelfIterator(from), a -> new TwoStageIterator<>(precedingSiblingIterator(a), ps -> subtree(ps).preorderIterator()));
    }

    /**
     * Returns an iterator over the preceding sibling nodes from the given node.
     *
     * @param from the node whose preceding sibling nodes iterator is to be returned.
     * @return an iterator over the preceding sibling nodes from the given node.
     */
    @Override
    default Iterator<E> precedingSiblingIterator(E from) {
        return new PrecedingSiblingIterator<>(this, from);
    }

    /**
    * Returns a list of the children of the specified parent node. This is an optional method, generally suited to
    * greedy clients or clients which operate more efficiently with bulk collections.
    *
    * @param parent the parent whose children are to be determined.
    * @return a list of the children or the  specified parent node; empty if the parent has no children or null if this operation
    * is not supported in this tree model.
    */
    List<E> getChildren(E parent);

    /**
    * Produces an iterator over the nodes in the tree. Optionally, the iterator backs the
    * tree and modifications to the iterator, such as <code>{@link TreeIterator#set(Object)}</code>, affect associated
    * nodes of the tree.
    *
    * @return an iterator that returns each node in the tree.
    */
    default TreeIterator<E> iterator() {
        return preorderIterator();
    }

    /**
     * Removed a child from the given parent node.
     *
     * @param parent the parent whose child is to be removed.
     * @param child the child node to remove.
     * @return true if the child node was removed, false otherwise.
     * @throws UnsupportedOperationException if the tree model does not support removal operations
     */
    default boolean remove(E parent, E child) {
        throw new UnsupportedOperationException();
    }

    /**
     * Replaces the old child with the new child under the given parent node.
     *
     * @param parent the parent whose child is to be replaced.
     * @param old the child to be replaced.
     * @param replacement the child node to replace the old node.
     * @return true if the old node was substituted by the replacement, false otherwise.
     * @throws UnsupportedOperationException if the tree model does not support set operations
     */
    default boolean set(E parent, E old, E replacement) {
        throw new UnsupportedOperationException();
    }

    /**
     * Adds a node after the old child with the new child under the given parent node.
     *
     * @param parent the parent whose child is to be replaced.
     * @param afterNode the child after which the new node will be added.
     * @param node the child node add after the given sibling.
     * @return true if the node was added, false otherwise.
     * @throws UnsupportedOperationException if the tree model does not support add operations
     */
    default boolean add(E parent, E afterNode, E node) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns a preorder iterator over all nodes in the tree.
     *
     * @return a preorder iterator over all nodes in the tree, starting from the tree root node.
     */
    @Override
    default TreeIterator<E> preorderIterator() {
        return new PreorderIterator<>(this);
    }

    /**
     * Returns a preorder iterator over all nodes in the tree, starting with the given subtree node.
     *
     * @param from the node from which preorder iteration will start.
     * @return a preorder iterator over all nodes in the subtree, starting at the given node.
     */
    @Override
    default TreeIterator<E> preorderIterator(E from) {
        return new PreorderIterator<>(this, from);
    }

    default TreeIterator<E> preorderParentUnawareIterator() {
        return new PreorderParentUnawareIterator<>(this);
    }

    /**
     * Returns an inorder iterator over all nodes in the tree, starting from the root node.
     *
     * @return an inorder iterator over all nodes in the subtree, starting at the root node.
     */
    @Override
    default TreeIterator<E> inorderIterator() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns an inorder iterator over all nodes in the tree, starting with the given subtree node.
     *
     * @param from the node from which inorder iteration will start.
     * @return an inorder iterator over all nodes in the subtree, starting at the given node.
     */
    @Override
    default TreeIterator<E> inorderIterator(E from) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns a postorder iterator over all nodes in the tree.
     *
     * @return a postorder iterator over all nodes in the tree, starting from the tree root node.
     */
    @Override
    default TreeIterator<E> postorderIterator() {
        return new PostorderIterator<E>(this);
    }

    /**
     * Returns a postorder iterator over all nodes in the tree, starting with the given subtree node.
     *
     * @param from the node from which the postorder iteration will start.
     * @return a postorder iterator over all nodes in the subtree, starting at the given node.
     */
    @Override
    default TreeIterator<E> postorderIterator(E from) {
        return new PostorderIterator<>(this, from);
    }

    default Iterable<E> preorderIterable() {
        return this::preorderIterator;
    }

    default Iterable<E> preorderParentUnawareIterable() {
        return this::preorderParentUnawareIterator;
    }

    default Iterable<E> inorderIterable() {
        return this::inorderIterator;
    }

    default Iterable<E> postorderIterable() {
        return this::postorderIterator;
    }

    /**
     * Creates an ancestor stream over the elements of the tree, from the given node.
     * @param from the node from which the ancestor stream is to be returned.
     * @return a stream over the ancestor nodes from the given context node..
     */
    @Override
    default Stream<E> ancestorStream(E from) {
        return ancestorIterator(from).stream();
    }

    /**
     * Returns an iterator over the ancestors or self of the specified from node.
     *
     * @param from the node whose ancestor or self nodes iterator is to be returned.
     * @return an iterator over the ancestors or self of the specified from node.
     */
    @Override
    default Stream<E> ancestorOrSelfStream(E from) {
        return ancestorOrSelfIterator(from).stream();
    }

    /**
     * Creates an child stream over the elements of the tree, from the given node.
     * @param from the node from which the child stream is to be returned.
     * @return a stream over the ancestor nodes from the given context node..
     */
    @Override
    default Stream<E> childStream(E from) {
        return childIterator(from).stream();
    }

    /**
     * Creates a descendant stream over the elements of the tree, from the given node.
     * @param from the node from which the descendant nodes stream is to be returned.
     * @return a stream over the descendant nodes from the given context node.
     */
    @Override
    default Stream<E> descendantStream(E from) {
        return asStream(descendantIterator(from));
    }

    /**
     * Creates a descendant or self stream over the elements of the tree, from the given node.
     * @param from the node from which the descendant or self nodes stream is to be returned.
     * @return a stream over the descendant or self nodes from the given context node.
     */
    @Override
    default Stream<E> descendantOrSelfStream(E from) {
        return descendantOrSelfIterator(from).stream();
    }

    /**
     * Creates a following stream over the elements of the tree, from the given node.
     * @param from the node from which the following nodes stream is to be returned.
     * @return a stream over the following nodes from the given context node.
     */
    @Override
    default Stream<E> followingStream(E from) {
        return asStream(followingIterator(from));
    }

    /**
     * Creates a following sibling stream over the elements of the tree, from the given node.
     * @param from the node from which the following sibling nodes stream is to be returned.
     * @return a stream over the following sibling nodes from the given context node.
     */
    @Override
    default Stream<E> followingSiblingStream(E from) {
        return asStream(followingSiblingIterator(from));
    }

    /**
     * Creates a preceding stream over the elements of the tree, from the given node.
     * @param from the node from which the preceding nodes stream is to be returned.
     * @return a stream over the preceding nodes from the given context node.
     */
    @Override
    default Stream<E> precedingStream(E from) {
        return asStream(precedingIterator(from));
    }

    /**
     * Creates a preceding sibling stream over the elements of the tree, from the given node.
     * @param from the node from which the preceding sibling nodes stream is to be returned.
     * @return a stream over the preceding sibling nodes from the given context node.
     */
    @Override
    default Stream<E> precedingSiblingStream(E from) {
        return asStream(precedingSiblingIterator(from));
    }

    /**
     * Returns a pre-order stream over all nodes in the subtree with the given sub-stree root node.
     *
     * @return a pre-order stream over all nodes in the subtree rooted at the given node.
     */
    @Override
    default Stream<E> preorderStream(E from) {
        return preorderIterator(from).stream();
    }

    /**
     * Creates a stream over the elements of the tree. All nodes will be visited depth-first in pre-order. The tree
     * model must support child and parent awareness. If the underlying model only supports child awareness then use
     * the {@link #preorderParentUnawareStream()} method to return a stream of nodes of the tree requiring that nodes
     * only need know about their children.
     *
     * @return a stream over every node in the tree by traversal of the tree in a depth-first pre-order.
     */
    @Override
    default Stream<E> preorderStream() {
        return preorderIterator().stream();
    }

    /**
     * Creates a stream over the elements of the tree. All nodes will be visited depth-first in pre-order. The tree
     * model must support child awareness.
     *
     * @return a stream over every node in the tree by traversal of the tree in a depth-first pre-order.
     */
    @Override
    default Stream<E> preorderParentUnawareStream() {
        return preorderParentUnawareIterator().stream();
    }

    /**
     * Creates a stream over the elements of the tree. All nodes will be visited depth-first in post-order.

     * @return a stream over every node in the tree by traversal of the tree in a depth-first post-order.
     */
    @Override
    default Stream<E> postorderStream() {
        return postorderIterator().stream();
    }

    /**
     * Returns a post-order stream over all nodes in the subtree with the given subtree root node.
     *
     * @return a post-order stream over all nodes in the subtree rooted at the given node.
     */
    @Override
    default Stream<E> postorderStream(E from) {
        return postorderIterator(from).stream();
    }

    /**
     * Determines the length of the tree. This default implementation iterates over the entire tree in order
     * to determine the length. Which may be very expensive.
     *
     * @return the total length of the tree;
     */
    default long size() {
        return preorderIterator().stream().count();
    }

    /**
     * Finds and returns an iterator over leaf nodes in pre-order. Depending on the size of the tree model this may be an expensive operation.
     *
     * @return an iterator of all leaf nodes that comprise this tree model.
     */
    default Iterator<E> leafNodeIterator() {
        return leafNodeStream().iterator();
    }

    /**
     * Finds and returns a stream of leaf nodes in pre-order. Depending on the size of the tree model this may be an expensive operation.
     *
     * @return a stream of all leaf nodes that comprise this tree model.
     */
    default Stream<E> leafNodeStream() {
        return preorderStream().filter(this::isLeaf);
    }

    /**
     * Finds and returns all leaf nodes in pre-order. Depending on the size of the tree model this may be an expensive operation.
     *
     * @return a list of all leaf nodes that comprise this tree model.
     */
    default List<E> getLeaves() {
        return leafNodeStream().collect(Collectors.toList());
    }
}
