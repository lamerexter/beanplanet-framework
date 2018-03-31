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

import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Unit tests for {@link PreorderTreeIterator}.
 */
public class PostorderTreeIteratorTest {
    @Test
    public void ctor_Tree() {
        // Given
        TreeNodeTree<String> tree = new TreeNodeTree<>("root");
        PostorderIterator<TreeNode<String>> iterator = new PostorderIterator<>(tree);

        // Then
        assertThat(iterator.getTree(), sameInstance(tree));
        assertThat(iterator.getFromNode(), sameInstance(iterator.getTree().getRoot()));
    }

    @Test
    public void ctor_Tree_FromNode() {
        // Given
        TreeNode<String> child1 = new TreeNode<>("child1");
        TreeNode<String> rootNode = new TreeNode<>("root", child1);
        TreeNodeTree<String> tree = new TreeNodeTree<>(rootNode);
        PostorderIterator<TreeNode<String>> iterator = new PostorderIterator<>(tree, child1);

        // Then
        assertThat(iterator.getTree(), sameInstance(tree));
        assertThat(iterator.getFromNode(), sameInstance(child1));
    }

    @Test
    public void forward() {
        // Given
        TreeNode<String> child11 = new TreeNode<>("child11");
        TreeNode<String> child1 = new TreeNode<>("child1", child11);
        TreeNode<String> child221 = new TreeNode<>("child221");
        TreeNode<String> child22 = new TreeNode<>("child22", child221);
        TreeNode<String> child21 = new TreeNode<>("child21");
        TreeNode<String> child2 = new TreeNode<>("child2", child21, child22);
        TreeNode<String> root = new TreeNode<>("root",
                                                   child1, child2
        );
        TreeNodeTree<String> tree = new TreeNodeTree<>(root);

        // Then a full traversal from root is as expected
        assertThat(new PostorderIterator<>(tree).stream()
                           .map(TreeNode::getManagedObject)
                           .collect(Collectors.toList()), equalTo(asList("child11", "child1", "child21", "child221", "child22", "child2", "root")));

        // And a full traversal from child21 is as expected, containing only nodes from that point onwards
        assertThat(new PostorderIterator<>(tree, child21).stream()
                                                   .map(TreeNode::getManagedObject)
                                                   .collect(Collectors.toList()), equalTo(asList("child21", "child221", "child22", "child2", "root")));

        // And a full traversal from child22 is as expected, containing only nodes from that point onwards
        assertThat(new PostorderIterator<>(tree, child22).stream()
                                                            .map(TreeNode::getManagedObject)
                                                            .collect(Collectors.toList()), equalTo(asList("child221", "child22", "child2", "root")));

        // And traversal on sub-tree is as expected (just the sub-tree)
        assertThat(new PostorderIterator<>(new TreeNodeTree<>(child1), child11).stream()
                                                            .map(TreeNode::getManagedObject)
                                                            .collect(Collectors.toList()), equalTo(asList("child11", "child1")));
    }

    @Test(expected = NoSuchElementException.class)
    public void next_exhausted() {
        // Given
        TreeNodeTree<String> tree = new TreeNodeTree<>("root");
        PostorderIterator<TreeNode<String>> iterator = new PostorderIterator<>(tree);

        // Then
        assertThat(iterator.hasNext(), is(true));

        // When
        iterator.next();

        // Then
        assertThat(iterator.hasNext(), is(false));

        // When
        iterator.next();
    }

    @Test(expected = NoSuchElementException.class)
    public void previous_exhausted() {
        // Given
        TreeNodeTree<String> tree = new TreeNodeTree<>("root");
        PostorderIterator<TreeNode<String>> iterator = new PostorderIterator<>(tree);

        // When
        iterator.previous();
        iterator.previous();
    }

    @Test
    public void reverse() {
        // Given
        TreeNode<String> child11 = new TreeNode<>("child11");
        TreeNode<String> child1 = new TreeNode<>("child1", child11);
        TreeNode<String> child221 = new TreeNode<>("child221");
        TreeNode<String> child22 = new TreeNode<>("child22", child221);
        TreeNode<String> child21 = new TreeNode<>("child21");
        TreeNode<String> child2 = new TreeNode<>("child2", child21, child22);
        TreeNode<String> root = new TreeNode<>("root",
                                                   child1, child2
        );
        TreeNodeTree<String> tree = new TreeNodeTree<>(root);

        // Then a full reverse traversal from root is as expected
        assertThat(new PostorderIterator<>(tree).reverse().stream()
                                                .map(TreeNode::getManagedObject)
                                                .collect(Collectors.toList()), equalTo(asList("root", "child2", "child22", "child221", "child21", "child1", "child11")));

        // And a full traversal from child21 is as expected, containing all nodes in reverse order
        assertThat(new PostorderIterator<>(tree, child221).reverse().stream()
                                                            .map(TreeNode::getManagedObject)
                                                            .collect(Collectors.toList()), equalTo(asList("child221", "child21", "child1", "child11")));

        // And traversal on sub-tree is as expected (just the sub-tree)
        assertThat(new PostorderIterator<>(new TreeNodeTree<>(child2), child22).reverse().stream()
                                                                                  .map(TreeNode::getManagedObject)
                                                                                  .collect(Collectors.toList()), equalTo(asList("child22", "child221", "child21")));
    }

    @Test
    public void next_previous_successiveCalls() {
        // Given
        TreeNode<String> child11 = new TreeNode<>("child11");
        TreeNode<String> child1 = new TreeNode<>("child1", child11);
        TreeNode<String> child221 = new TreeNode<>("child221");
        TreeNode<String> child22 = new TreeNode<>("child22", child221);
        TreeNode<String> child21 = new TreeNode<>("child21");
        TreeNode<String> child2 = new TreeNode<>("child2", child21, child22);
        TreeNode<String> root = new TreeNode<>("root", child1, child2);
        TreeNodeTree<String> tree = new TreeNodeTree<>(root);
        PostorderIterator<TreeNode<String>> iterator = new PostorderIterator<>(tree, child11);

        // Then next() returns correct node
        assertThat(iterator.next(), equalTo(child11));
        assertThat(iterator.next(), equalTo(child1));
        assertThat(iterator.next(), equalTo(child21));
        assertThat(iterator.next(), equalTo(child221));

        // And previous returns the correct node
        assertThat(iterator.previous(), equalTo(child221));
        assertThat(iterator.previous(), equalTo(child21));
        assertThat(iterator.previous(), equalTo(child1));
        assertThat(iterator.previous(), equalTo(child11));
        assertThat(iterator.hasPrevious(), is(false));

        // And next() returns correct node
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), equalTo(child11));
        assertThat(iterator.next(), equalTo(child1));
        assertThat(iterator.next(), equalTo(child21));
        assertThat(iterator.next(), equalTo(child221));
        assertThat(iterator.next(), equalTo(child22));
        assertThat(iterator.next(), equalTo(child2));
        assertThat(iterator.next(), equalTo(root));
        assertThat(iterator.hasNext(), is(false));
    }
}