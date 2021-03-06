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
 * Defines all of the stream operations over a tree model.
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
     * Creates a stream over the elements of the tree. All nodes will be visited depth-first in pre-order. The tree
     * model must support child and parent awareness. If the underlying model only supports child awareness then use
     * the {@link #preorderParentUnawareStream()} method to return a stream of nodes of the tree requiring that nodes
     * only need know about their children.

     * @return a stream over every node in the tree by traversal of the tree in a depth-first pre-order.
     */
    Stream<E> preorderStream();

    /**
     * Creates a stream over the elements of the tree. All nodes will be visited depth-first in pre-order. The tree
     * model must support child awareness.

     * @return a stream over every node in the tree by traversal of the tree in a depth-first pre-order.
     */
    Stream<E> preorderParentUnawareStream();

    /**
     * Creates a stream over the elements of the tree. All nodes will be visited depth-first in post-order.

     * @return a stream over every node in the tree by traversal of the tree in a depth-first post-order.
     */
    Stream<E> postorderStream();

}
