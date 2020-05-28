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