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

import java.util.ListIterator;
import java.util.stream.Stream;

/**
 * Defines all the stream operations over a tree model.
 */
public interface TreeStreams<E> {
    /**
     * Creates a stream over the elements of the tree. All nodes will be visited but the order is not guaranteed.
     *
     * @return a stream over every node in the tree.
     */
    default Stream<E> stream() {
        return preorderStream();
    }

    /**
     * Creates an ancestor stream over the elements of the tree, from the given node.
     * @param from the node from which the ancestor stream is to be returned.
     * @return a stream over the ancestor nodes from the given context node..
     */
    Stream<E> ancestorStream(E from);

    /**
     * Creates an ancestor or self stream over the elements of the tree, from the given node.
     * @param from the node from which the ancestor or self stream is to be returned.
     * @return a stream over the ancestor or self nodes from the given context node.
     */
    Stream<E> ancestorOrSelfStream(E from);

    /**
     * Creates an child stream over the elements of the tree, from the given node.
     * @param from the node from which the child stream is to be returned.
     * @return a stream over the ancestor nodes from the given context node.
     */
    Stream<E> childStream(E from);

    /**
     * Creates a descendant stream over the elements of the tree, from the given node.
     * @param from the node from which the descendant nodes stream is to be returned.
     * @return a stream over the descendant nodes from the given context node.
     */
    Stream<E> descendantStream(E from);

    /**
     * Creates a descendant or self stream over the elements of the tree, from the given node.
     * @param from the node from which the descendant or self nodes stream is to be returned.
     * @return a stream over the descendant or self nodes from the given context node.
     */
    Stream<E> descendantOrSelfStream(E from);

    /**
     * Creates a following stream over the elements of the tree, from the given node.
     * @param from the node from which the following nodes stream is to be returned.
     * @return a stream over the following nodes from the given context node.
     */
    Stream<E> followingStream(E from);

    /**
     * Creates a following sibling stream over the elements of the tree, from the given node.
     * @param from the node from which the following sibling nodes stream is to be returned.
     * @return a stream over the following sibling nodes from the given context node.
     */
    Stream<E> followingSiblingStream(E from);

    /**
     * Creates a preceding stream over the elements of the tree, from the given node.
     * @param from the node from which the preceding nodes stream is to be returned.
     * @return a stream over the preceding nodes from the given context node.
     */
    Stream<E> precedingStream(E from);

    /**
     * Creates a preceding sibling stream over the elements of the tree, from the given node.
     * @param from the node from which the preceding sibling nodes stream is to be returned.
     * @return a stream over the preceding sibling nodes from the given context node.
     */
    Stream<E> precedingSiblingStream(E from);

    /**
     * Creates a stream over the elements of the tree. All nodes will be visited depth-first in pre-order. The tree
     * model must support child and parent awareness. If the underlying model only supports child awareness then use
     * the {@link #preorderParentUnawareStream()} method to return a stream of nodes of the tree requiring that nodes
     * only need know about their children.

     * @return a stream over every node in the tree by traversal of the tree in a depth-first pre-order.
     */
    Stream<E> preorderStream();

    /**
     * Returns a pre-order stream over all nodes in the subtree with the given subtree root node.
     *
     * @return a pre-order stream over all nodes in the subtree rooted at the given node.
     */
    Stream<E> preorderStream(E from);

    /**
     * Creates a stream over the elements of the tree. All nodes will be visited depth-first in pre-order. The tree
     * model must support child awareness.

     * @return a stream over every node in the tree by traversal of the tree in a depth-first pre-order.
     */
    Stream<E> preorderParentUnawareStream();

    /**
     * Creates a post-order stream over the elements of the tree. All nodes will be visited depth-first in post-order.

     * @return a stream over every node in the tree by traversal of the tree in a depth-first post-order.
     */
    Stream<E> postorderStream();

    /**
     * Returns a post-order stream over all nodes in the subtree with the given subtree root node.
     *
     * @return a post-order stream over all nodes in the subtree rooted at the given node.
     */
    Stream<E> postorderStream(E from);
}
