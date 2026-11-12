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
 * Defines all the iterator operations over a tree model: XPath's axes, from a context node. Forward axes are in
 * document order; the reverse axes ({@code ancestor}, {@code ancestor-or-self}, {@code preceding},
 * {@code preceding-sibling}) are nearest first. Every iterator is lazy and bidirectional (see {@link TreeIterator}).
 */
public interface TreeIterators<E> extends Iterable<E>, DepthFirstTreeIterators<E>, DepthFirstTreeIterables<E> {
    /**
     * Returns an iterator over the ancestors of the specified from node, nearest first (its parent first), up to the
     * tree's root.
     *
     * @param from the node whose ancestor nodes iterator is to be returned.
     * @return an iterator over the ancestors of the specified from node; empty if the from node has no ancestors.
     */
    TreeIterator<E> ancestorIterator(E from);

    /**
     * Returns an iterator over the specified from node and its ancestors, nearest first, up to the tree's root.
     *
     * @param from the node whose ancestor or self nodes iterator is to be returned.
     * @return an iterator over the ancestors or self of the specified from node.
     */
    TreeIterator<E> ancestorOrSelfIterator(E from);

    /**
     * Returns an iterator over the children of the specified parent node. Optionally, the iterator backs the
     * children and modifications to the iterator, such as <code>{@link TreeIterator#remove()}</code> or
     * <code>{@link TreeIterator#set(Object)}</code>, affect the children of the given node.
     *
     * @param parent the parent whose child nodes iterator is to be returned.
     * @return an iterator over the children of the specified parent node; empty if the parent has no children.
     */
    TreeIterator<E> childIterator(E parent);

    /**
     * Returns an iterator over the descendant nodes of the given node, in document order.
     *
     * @param from the node whose descendant nodes iterator is to be returned.
     * @return an iterator over the descendant nodes from the given node.
     */
    TreeIterator<E> descendantIterator(E from);

    /**
     * Returns an iterator over the given node and its descendants, in document order.
     *
     * @param from the node whose descendant or self nodes iterator is to be returned.
     * @return an iterator over the descendant or self nodes from the given node.
     */
    TreeIterator<E> descendantOrSelfIterator(E from);

    /**
     * Returns an iterator over the nodes after the given node in document order, other than its descendants.
     *
     * @param from the node whose following nodes iterator is to be returned.
     * @return an iterator over the following nodes from the given node.
     */
    TreeIterator<E> followingIterator(E from);

    /**
     * Returns an iterator over the siblings after the given node, in document order.
     *
     * @param from the node whose following sibling nodes iterator is to be returned.
     * @return an iterator over the following sibling nodes from the given node.
     */
    TreeIterator<E> followingSiblingIterator(E from);

    /**
     * Returns an iterator over the nodes before the given node in document order, other than its ancestors, nearest
     * first.
     *
     * @param from the node whose preceding nodes iterator is to be returned.
     * @return an iterator over the preceding nodes from the given node.
     */
    TreeIterator<E> precedingIterator(E from);

    /**
     * Returns an iterator over the siblings before the given node, nearest first.
     *
     * @param from the node whose preceding sibling nodes iterator is to be returned.
     * @return an iterator over the preceding sibling nodes from the given node.
     */
    TreeIterator<E> precedingSiblingIterator(E from);
}
