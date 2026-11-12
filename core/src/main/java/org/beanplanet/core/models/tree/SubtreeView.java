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

/**
 * The subtree of a tree below one of its nodes: the same nodes, with that node as root, so every axis and traversal
 * stays within it.
 *
 * @param <E> the type of the tree's nodes.
 */
class SubtreeView<E> implements Tree<E> {
    private final Tree<E> tree;
    private final E root;

    SubtreeView(final Tree<E> tree, final E root) {
        this.tree = tree;
        this.root = root;
    }

    @Override
    public E getRoot() {
        return root;
    }

    @Override
    public Tree<E> subtree(final E from) {
        return new SubtreeView<>(tree, from);
    }

    @Override
    public E getParent(final E child) {
        return tree.getParent(child);
    }

    @Override
    public E getChild(final E parent, final int childIndex) {
        return tree.getChild(parent, childIndex);
    }

    @Override
    public int getNumberOfChildren(final E parent) {
        return tree.getNumberOfChildren(parent);
    }

    @Override
    public int getIndexOfChild(final E parent, final E child) {
        return tree.getIndexOfChild(parent, child);
    }

    @Override
    public List<E> getChildren(final E parent) {
        return tree.getChildren(parent);
    }

    @Override
    public boolean remove(final E parent, final E child) {
        return tree.remove(parent, child);
    }

    @Override
    public boolean set(final E parent, final E old, final E replacement) {
        return tree.set(parent, old, replacement);
    }

    @Override
    public boolean add(final E parent, final E afterNode, final E node) {
        return tree.add(parent, afterNode, node);
    }
}
