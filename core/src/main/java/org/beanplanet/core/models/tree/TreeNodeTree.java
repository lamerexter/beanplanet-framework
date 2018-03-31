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

import java.util.List;

public class TreeNodeTree<E> implements Tree<TreeNode<E>> {
    private TreeNode<E> root;

    public TreeNodeTree(E root) {
        this(new TreeNode<>(root));
    }

    TreeNodeTree(TreeNode<E> root) {
        this.root = root;
    }

    @Override
    public TreeNode<E> getRoot() {
        return root;
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
        return parent.getChildNodes();
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
        return null;
    }

    @Override
    public TreeIterator<TreeNode<E>> inorderIterator() {
        return null;
    }

    @Override
    public TreeIterator<TreeNode<E>> postorderIterator() {
        return null;
    }
}
