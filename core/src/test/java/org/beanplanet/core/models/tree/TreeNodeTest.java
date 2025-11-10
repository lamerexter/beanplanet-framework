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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class TreeNodeTest {
    @Test
    public void copyShallow() {
        // Given
        TreeNode<String> child1 = new TreeNode<>("child1");
        TreeNode<String> child21 = new TreeNode<>("child21");
        TreeNode<String> child2 = new TreeNode<>("child2", child21);
        TreeNode<String> root = new TreeNode<>("root", child1, child2);
        TreeNode<String> copy = root.copyShallow();

        // Then
        assertThat(copy, is(not(sameInstance(root))));
        assertThat(copy, is(equalTo(root)));
    }
}