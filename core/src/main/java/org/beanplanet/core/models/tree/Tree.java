/*
 *  MIT Licence:
 *
 *  Copyright (C) 2018 Beanplanet Ltd
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without restriction
 *  including without limitation the rights to use, copy, modify, merge,
 *  publish, distribute, sublicense, and/or sell copies of the Software,
 *  and to permit persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 *  KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 *  PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 *  CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  DEALINGS IN THE SOFTWARE.
 */
package org.beanplanet.core.models.tree;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * Model of a tree. This model makes no assumption about how the tree node information is stored or where it comes from.
 *
 * @author Gary Watson
 */
public interface Tree<E> extends TreeIterators<E>, Serializable {
    /**
    * Returns the root of the tree, if this tree model represents a single tree instance.
    * <p>
    * This could be an instance of <code>TreeNode</code> or any other use-defined type. This model does not place any
    * constraints on the type of nodes in the tree model.
    *
    * @return the root node/object of the tree,
    * @see #getRoot(Object) for a non-instance alternative
    */
    E getRoot();

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
     * Returns an iterator over the children of the specified parent node. Optionally, the iterator backs the
     * children and modifications to the iterator, such as <code>{@link ListIterator#add(Object)}</code> or
     * <code>{@link ListIterator#set(Object)}</code>, affect the children of the given node.
     *
     * @param parent the parent whose child iterator is to be returned.
     * @return an iterator over the children of the specified parent node; empty if the parent has no children.
     */
    default TreeIterator<E> childIterator(E parent) {
        return new ChildIterator<E>(this, parent);
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
     */
    boolean remove(E parent, E child);

    /**
     * Replaces the old child with the new child under the given parent node.
     *
     * @param parent the parent whose child is to be replaced.
     * @param old the child to be replaced.
     * @param replacement the child node to replace the old node.
     * @return true if the old node was substituted by the replacement, false otherwise.
     */
    boolean set(E parent, E old, E replacement);

    /**
     * Adds a node after the old child with the new child under the given parent node.
     *
     * @param parent the parent whose child is to be replaced.
     * @param afterNode the child after which the new node will be added.
     * @param node the child node add after the given sibling.
     * @return true if the node was added, false otherwise.
     */
    boolean add(E parent, E afterNode, E node);

    default TreeIterator<E> preorderIterator() {
        return new PreorderTreeIterator<>(this);
    }

    default TreeIterator<E> inorderIterator() {
        throw new UnsupportedOperationException();
    }

    default TreeIterator<E> postorderIterator() {
        return new PostorderIterator<E>(this);
    }
}
