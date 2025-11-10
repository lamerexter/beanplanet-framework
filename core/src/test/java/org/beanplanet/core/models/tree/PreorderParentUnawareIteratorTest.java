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

import org.beanplanet.core.util.IteratorUtil;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Unit tests for {@link PreorderParentUnawareIterator}.
 */
public class PreorderParentUnawareIteratorTest {
    @Test
    public void ctor_Tree() {
        // Given
        TreeNodeTree<String> tree = new TreeNodeTree<>("root");
        PreorderParentUnawareIterator<TreeNode<String>> iterator = new PreorderParentUnawareIterator<>(tree);

        // Then
        assertThat(iterator.getTree(), sameInstance(tree));
        assertThat(iterator.getFromNode(), sameInstance(iterator.getTree().getRoot()));
    }

    @Test
    public void next_successiveCalls() {
        // Given
        TreeNode<String> child11 = new TreeNode<>("child11");
        TreeNode<String> child221 = new TreeNode<>("child221");
        TreeNode<String> child22 = new TreeNode<>("child22", child221);
        TreeNode<String> child21 = new TreeNode<>("child21");
        TreeNode<String> child2 = new TreeNode<>("child2", child21, child22);
        TreeNode<String> child1 = new TreeNode<>("child1", child11);
        TreeNode<String> root = new TreeNode<>("root", child1, child2);
        TreeNodeTree<String> tree = new TreeNodeTree<>(root);
        PreorderParentUnawareIterator<TreeNode<String>> iterator = new PreorderParentUnawareIterator<>(tree);

        // Then next() returns correct nodes
        assertThat(IteratorUtil.toList(iterator), equalTo(asList(root, child1, child11, child2, child21, child22, child221)));
    }
}