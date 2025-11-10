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

import org.beanplanet.core.models.path.NamePath;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TreeNodeTree<E> extends AbstractTree<TreeNode<E>> {
    public TreeNodeTree(E root) {
        this(new TreeNode<>(root));
    }

    public TreeNodeTree(TreeNode<E> root) {
        super(root);
    }

    /**
     * Performs shallow copy of this tree. Not implemented in this abstraction.
     *
     * @return a new shallow-copied instance of the tree.
     * @throws UnsupportedOperationException as subclasses must override this method to produce a specific tree.
     */
    public TreeNodeTree<E> copyShallow() { return new TreeNodeTree<>(root.copyShallow()); }

    public static <S> TreeNodeTree<S> copyFrom(final Tree<S> sourceTree) {
        return copyFrom(sourceTree, s -> s);
    }

    public static <S, R> TreeNodeTree<R> copyFrom(final Tree<S> sourceTree, final Function<S, R> transformer) {
        if ( sourceTree == null ) return null;

        final Deque<TreeNode<R>> destNodeStack = new ArrayDeque<>();
        final Deque<S> sourceNodeStack = new ArrayDeque<>();
        final TreeNode<R> destRootNode = new TreeNode<>(transformer.apply(sourceTree.getRoot()));
        destNodeStack.push(destRootNode);
        sourceNodeStack.push(sourceTree.getRoot());

        final Deque<S> nextChildrenDeque = new ArrayDeque<>();
        final Deque<TreeNode<R>> destChildrenDeque = new ArrayDeque<>();

        while ( !destNodeStack.isEmpty()) {
            final TreeNode<R> nextDest = destNodeStack.pop();
            final S nextSource = sourceNodeStack.pop();
            List<S> nextChildren = sourceTree.getChildren(nextSource);
            List<TreeNode<R>> destChildren = nextChildren.stream().map(transformer).map(TreeNode::new).collect(Collectors.toList());
            nextDest.setChildren(destChildren);
            nextChildrenDeque.clear();
            for (S child : nextChildren) {
                nextChildrenDeque.push(child);
            }
            while ( !nextChildrenDeque.isEmpty() ) {
                sourceNodeStack.push(nextChildrenDeque.pop());
            }
            destChildrenDeque.clear();
            for (TreeNode<R> destChild : destChildren) {
                destChildrenDeque.push(destChild);
            }
            while ( !destChildrenDeque.isEmpty() ) {
                destNodeStack.push(destChildrenDeque.pop());
            }
        }

        return new TreeNodeTree<>(destRootNode);
    }

    public NamePath parentNamePath(final TreeNode<E> from, final Function<E, String> nameSupplier) {
        return from.parentNamePath(nameSupplier);
    }

    public Optional<TreeNode<E>> find(final NamePath path, final Function<E, String> nameSupplier) {
        if ( path.isEmpty() ) return Optional.empty();

        TreeNode<E> from = path.getFirstElement().equals(nameSupplier.apply(getRoot().getManagedObject())) ? getRoot() : null;

        for (int n=1; n < path.length() && from != null; n++) {
            final String pathNameElement = path.getElement(n);
            from = from.getChildren()
                    .stream()
                    .filter(node -> pathNameElement.equals(nameSupplier.apply(node.getManagedObject())))
                    .findFirst()
                    .orElse(null);
        }

        return Optional.ofNullable(from);
    }

    @Override
    public TreeNode<E> getParent(TreeNode<E> child) {
        return child.getParent();
    }

    @Override
    public TreeNode<E> getChild(TreeNode<E> parent, int childIndex) {
        return parent.getChild(childIndex);
    }

    @Override
    public int getNumberOfChildren(TreeNode<E> parent) {
        return parent == null ? 0 : parent.getNumberOfChildren();
    }

    @Override
    public int getIndexOfChild(TreeNode<E> parent, TreeNode<E> child) {
        if (parent == null) return -1;

        return parent.getIndexOfChild(child);
    }

    @Override
    public List<TreeNode<E>> getChildren(TreeNode<E> parent) {
        return parent.getChildren();
    }

    @Override
    public boolean remove(TreeNode<E> parent, TreeNode<E> child) {
        return false;
    }

    @Override
    public boolean set(TreeNode<E> parent, TreeNode<E> old, TreeNode<E> replacement) {
        return false;
    }

    @Override
    public boolean add(TreeNode<E> parent, TreeNode<E> afterNode, TreeNode<E> node) {
        return false;
    }

    @Override
    public TreeIterator<TreeNode<E>> preorderIterator() {
        return new PreorderIterator<>(this);
    }

    @Override
    public TreeIterator<TreeNode<E>> inorderIterator() {
        return null;
    }

    @Override
    public TreeIterator<TreeNode<E>> postorderIterator() {
        return new PostorderIterator<>(this);
    }

    /**
     * Finds all leaf nodes and extracts the managed objects for each node found.
     *
     * @return a list pf the manage objects that are leaf nodes in this tree.
     * @see #getLeaves()
     */
    public List<E> getManagedLeaves() {
        return leafNodeStream().map(TreeNode::getManagedObject).collect(Collectors.toList());
    }
}
