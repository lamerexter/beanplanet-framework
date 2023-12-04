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

public interface DepthFirstTreeIterators<E> {
    /**
     * Returns a preorder iterator over all nodes in the tree.
     *
     * @return a preorder iterator over all nodes in the tree, starting from the tree root node.
     */
    TreeIterator<E> preorderIterator();

    /**
     * Returns a preorder iterator over all nodes in the tree, starting with the given subtree node.
     *
     * @param from the node from which preorder iteration will start.
     * @return a preorder iterator over all nodes in the subtree, starting at the given node.
     */
    TreeIterator<E> preorderIterator(E from);

    TreeIterator<E> preorderParentUnawareIterator();

    /**
     * Returns an inorder iterator over all nodes in the tree, starting from the root node.
     *
     * @return an inorder iterator over all nodes in the subtree, starting at the root node.
     */
    TreeIterator<E> inorderIterator();

    /**
     * Returns an inorder iterator over all nodes in the tree, starting with the given subtree node.
     *
     * @param from the node from which inorder iteration will start.
     * @return an inorder iterator over all nodes in the subtree, starting at the given node.
     */
    TreeIterator<E> inorderIterator(E from);

    /**
     * Returns a postorder iterator over all nodes in the tree.
     *
     * @return a postorder iterator over all nodes in the tree, starting from the tree root node.
     */
    TreeIterator<E> postorderIterator();

    /**
     * Returns a postorder iterator over all nodes in the tree, starting with the given subtree node.
     *
     * @param from the node from which the postorder iteration will start.
     * @return a postorder iterator over all nodes in the subtree, starting at the given node.
     */
    TreeIterator<E> postorderIterator(E from);
}
