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
