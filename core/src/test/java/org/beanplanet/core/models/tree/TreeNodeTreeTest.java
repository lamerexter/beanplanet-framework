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

import org.beanplanet.core.util.IteratorUtil;
import org.junit.Test;

import java.util.Collections;
import java.util.Spliterators;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static org.beanplanet.core.util.IteratorUtil.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link TreeNodeTree}
 */
public class TreeNodeTreeTest {
    @Test
    public void ctor_E() {
        // Given
        TreeNode<String> rootNode = new TreeNode<>("root");
        TreeNodeTree<String> root = new TreeNodeTree<>("root");

        // Then
        assertThat(root.getRoot(), notNullValue());
        assertThat(root.getRoot().getManagedObject(), equalTo("root"));
    }

    @Test
    public void ctor_TreeNode() {
        // Given
        TreeNode<String> root = new TreeNode<>("root");
        TreeNode<String> child = new TreeNode<>(root, "child");

        // Then
        assertThat(new TreeNodeTree<>(root).getRoot(), equalTo(root));
    }

    @Test
    public void getRoot_TreeNode() {
        // Given
        TreeNode<String> rootNode = new TreeNode<>("root");
        TreeNode<String> child1 = new TreeNode<>(rootNode, "child1");
        TreeNode<String> child11 = new TreeNode<>(child1, "child11");

        // Then
        assertThat(new TreeNodeTree<>(rootNode).getRoot(rootNode), equalTo(rootNode));
        assertThat(new TreeNodeTree<>(child1).getRoot(child11), equalTo(child1));
    }

    @Test
    public void getParent() {
        // Given
        TreeNode<String> root = new TreeNode<>("root");
        TreeNode<String> child = new TreeNode<>(root, "child");

        // Then
        assertThat(new TreeNodeTree<>(root).getParent(root), nullValue());
        assertThat(new TreeNodeTree<>(child).getParent(child), equalTo(root));
    }

    @Test
    public void getPreviousSibling() {
        // Given
        TreeNode<String> child1 = new TreeNode<>("child1");
        TreeNode<String> child2 = new TreeNode<>("child2");
        TreeNode<String> rootNode = new TreeNode<>("root", child1, child2);
        TreeNodeTree<String> tree = new TreeNodeTree<>(rootNode);

        // Then
        assertThat(tree.getPreviousSibling(child1), nullValue());
        assertThat(tree.getPreviousSibling(child2), equalTo(child1));
    }

    @Test
    public void getNextSibling() {
        // Given
        TreeNode<String> child1 = new TreeNode<>("child1");
        TreeNode<String> child2 = new TreeNode<>("child2");
        TreeNode<String> rootNode = new TreeNode<>("root", child1, child2);
        TreeNodeTree<String> tree = new TreeNodeTree<>(rootNode);

        // Then
        assertThat(tree.getNextSibling(child1), equalTo(child2));
        assertThat(tree.getNextSibling(child2), nullValue());
    }

    @Test
    public void isLeaf_TreeNode() {
        // Given
        TreeNode<String> child1 = new TreeNode<>("child1");
        TreeNode<String> child2 = new TreeNode<>("child2");
        TreeNode<String> root = new TreeNode<>("root", child1, child2);
        TreeNodeTree<String> tree = new TreeNodeTree<>(root);

        // Then
        assertThat(tree.isLeaf(root), is(false));
        assertThat(tree.isLeaf(child1), is(true));
        assertThat(tree.isLeaf(child2), is(true));
    }

    @Test
    public void childIterator() {
        // Given
        TreeNode<String> child1 = new TreeNode<>("child1");
        TreeNode<String> child21 = new TreeNode<>("child21");
        TreeNode<String> child2 = new TreeNode<>("child2", child21);
        TreeNode<String> root = new TreeNode<>("root", child1, child2);
        TreeNodeTree<String> tree = new TreeNodeTree<>(root);

        // Then
        assertThat(tree.childIterator(root).toList(), equalTo(asList(child1, child2)));
        assertThat(tree.childIterator(child1).toList(), equalTo(emptyList()));
        assertThat(tree.childIterator(child2).toList(), equalTo(asList(child21)));
    }

    @Test
    public void getChildren_TreeNode() {
        // Given
        TreeNode<String> child1 = new TreeNode<>("child1");
        TreeNode<String> child21 = new TreeNode<>("child21");
        TreeNode<String> child2 = new TreeNode<>("child2", child21);
        TreeNode<String> root = new TreeNode<>("root", child1, child2);
        TreeNodeTree<String> tree = new TreeNodeTree<>(root);

        // Then
        assertThat(tree.getChildren(root), equalTo(asList(child1, child2)));
        assertThat(tree.getChildren(child1), nullValue());
        assertThat(tree.getChildren(child2), equalTo(asList(child21)));
    }

}