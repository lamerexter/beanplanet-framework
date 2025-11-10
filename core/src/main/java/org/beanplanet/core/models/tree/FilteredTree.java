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

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A wrapping tree which simply delegates to an underlying tree model, filtering nodes dynamically according to a
 * configured filter.
 */
public class FilteredTree<T> implements Tree<T> {
    private Tree<T> delegate;
    private Predicate<T> filter;

    public FilteredTree(Tree<T> delegate, Predicate<T> filter) {
        this.delegate = delegate;
        this.filter = filter;
    }

    /**
     * Returns the root of the tree, if this tree model represents a single tree instance.
     * <p>
     * This could be an instance of <code>TreeNode</code> or any other use-defined type. This model does not place any
     * constraints on the type of nodes in the tree model.
     *
     * @return the root node/object of the tree,
     * @see #getRoot(Object) for a non-instance alternative
     */
    @Override
    public T getRoot() {
        if ( delegate.getRoot() == null ) return null;

        return filter.test(delegate.getRoot()) ? delegate.getRoot() : null;
    }

    /**
     * Returns the parent node of the specified child.
     *
     * @param child the child whose parent is to be returned.
     * @return the parent of the specified child node, or null if the child has no parent (is the root node).
     */
    @Override
    public T getParent(T child) {
        T parent = delegate.getParent(child);
        return filter.test(parent) ? delegate.getRoot() : null;
    }

    /**
     * Returns the child node at the specified index of the parent node specified.
     *
     * @param parent     the parent whose child node is to be located.
     * @param childIndex the index of the child to obtain.
     * @return the node at the specified index of the parent.
     * @throws ArrayIndexOutOfBoundsException thrown if the index specified is out of range.
     */
    @Override
    public T getChild(T parent, int childIndex) {
        return getChildren(parent).get(childIndex);
    }

    /**
     * Returns the number of child nodes of the node specified.
     *
     * @param parent
     * @return the number of children
     */
    @Override
    public int getNumberOfChildren(T parent) {
        return getChildren(parent).size();
    }

    /**
     * Returns the index of a child node, within the list of child nodes of a parent node.
     *
     * @param parent the parent of the child whose index is to be found.
     * @param child  the node whose index is to be found.
     * @return the index of the specified child, within the list of child nodes of a parent, or -1 if the node is not a
     * child of the given parent.
     */
    @Override
    public int getIndexOfChild(T parent, T child) {
        return getChildren(parent).indexOf(child);
    }

    /**
     * Returns a list of the children of the specified parent node. This is an optional method, generally suited to
     * greedy clients or clients which operate more efficiently with bulk collections.
     *
     * @param parent the parent whose children are to be determined.
     * @return a list of the children or the  specified parent node; empty if the parent has no children or null if this operation
     * is not supported in this tree model.
     */
    @Override
    public List<T> getChildren(T parent) {
        return delegate.getChildren(parent)
                .stream()
                .filter(filter)
                .collect(Collectors.toList());
    }
}
