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

import java.util.Iterator;
import java.util.ListIterator;

/**
 * Defines all the iterator operations over a tree model.
 */
public interface TreeIterators<E> extends Iterable<E>, DepthFirstTreeIterators<E>, DepthFirstTreeIterables<E> {
    /**
     * Returns an iterator over the ancestors of the specified from node.
     *
     * @param from the node whose ancestor nodes iterator is to be returned.
     * @return an iterator over the ancestors of the specified from node; empty if the from node has no ancestors.
     */
    TreeIterator<E> ancestorIterator(E from);

    /**
     * Returns an iterator over the ancestors or self of the specified from node.
     *
     * @param from the node whose ancestor or self nodes iterator is to be returned.
     * @return an iterator over the ancestors or self of the specified from node.
     */
    TreeIterator<E> ancestorOrSelfIterator(E from);

    /**
     * Returns an iterator over the children of the specified parent node. Optionally, the iterator backs the
     * children and modifications to the iterator, such as <code>{@link ListIterator#add(Object)}</code> or
     * <code>{@link ListIterator#set(Object)}</code>, affect the children of the given node.
     *
     * @param parent the parent whose child nodes iterator is to be returned.
     * @return an iterator over the children of the specified parent node; empty if the parent has no children.
     */
    TreeIterator<E> childIterator(E parent);

    /**
     * Returns an iterator over the descendant nodes from the given node.
     *
     * @param from the node whose descendant nodes iterator is to be returned.
     * @return an iterator over the descendant nodes from the given node.
     */
    Iterator<E> descendantIterator(E from);

    /**
     * Returns an iterator over the descendant or self nodes from the given node.
     *
     * @param from the node whose descendant or self nodes iterator is to be returned.
     * @return an iterator over the descendant or self nodes from the given node.
     */
    TreeIterator<E> descendantOrSelfIterator(E from);

    /**
     * Returns an iterator over the following nodes from the given node.
     *
     * @param from the node whose following nodes iterator is to be returned.
     * @return an iterator over the following nodes from the given node.
     */
    Iterator<E> followingIterator(E from);

    /**
     * Returns an iterator over the following sibling nodes from the given node.
     *
     * @param from the node whose following sibling nodes iterator is to be returned.
     * @return an iterator over the following sibling nodes from the given node.
     */
    Iterator<E> followingSiblingIterator(E from);

    /**
     * Returns an iterator over the preceding nodes from the given node.
     *
     * @param from the node whose preceding nodes iterator is to be returned.
     * @return an iterator over the preceding nodes from the given node.
     */
    Iterator<E> precedingIterator(E from);

    /**
     * Returns an iterator over the preceding sibling nodes from the given node.
     *
     * @param from the node whose preceding sibling nodes iterator is to be returned.
     * @return an iterator over the preceding sibling nodes from the given node.
     */
    Iterator<E> precedingSiblingIterator(E from);
}
