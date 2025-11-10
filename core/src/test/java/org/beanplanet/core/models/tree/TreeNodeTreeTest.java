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

import org.junit.Test;

import java.util.Collections;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

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
    public void getLeaves() {
        // Given
        TreeNode<String> child1 = new TreeNode<>("child1");
        TreeNode<String> child21 = new TreeNode<>("child21");
        TreeNode<String> child22 = new TreeNode<>("child22");
        TreeNode<String> child2 = new TreeNode<>("child2", child21, child22);
        TreeNode<String> root = new TreeNode<>("root", child1, child2);
        TreeNodeTree<String> tree = new TreeNodeTree<>(root);

        // Then
        assertThat(tree.getLeaves(), equalTo(asList(child1, child21, child22)));
    }

    @Test
    public void getManagedLeaves() {
        // Given
        TreeNode<String> child1 = new TreeNode<>("child1");
        TreeNode<String> child21 = new TreeNode<>("child1");
        TreeNode<String> child22 = new TreeNode<>("child22");
        TreeNode<String> child2 = new TreeNode<>("child2", child21, child22);
        TreeNode<String> root = new TreeNode<>("root", child1, child2);
        TreeNodeTree<String> tree = new TreeNodeTree<>(root);

        // Then
        assertThat(tree.getManagedLeaves(), equalTo(asList("child1", "child1", "child22")));
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
        assertThat(tree.getChildren(child1), equalTo(Collections.emptyList()));
        assertThat(tree.getChildren(child2), equalTo(asList(child21)));
    }

    @Test
    public void copyShallow() {
        // Given
        TreeNode<String> child1 = new TreeNode<>("child1");
        TreeNode<String> child21 = new TreeNode<>("child21");
        TreeNode<String> child2 = new TreeNode<>("child2", child21);
        TreeNode<String> root = new TreeNode<>("root", child1, child2);
        TreeNodeTree<String> tree = new TreeNodeTree<>(root);
        TreeNodeTree<String> treeCopy = tree.copyShallow();

        // Then
        assertThat(treeCopy, is(not(sameInstance(tree))));
        assertThat(treeCopy, is(equalTo(tree)));
    }
}