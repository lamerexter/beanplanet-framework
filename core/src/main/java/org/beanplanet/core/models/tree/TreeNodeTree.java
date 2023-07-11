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
