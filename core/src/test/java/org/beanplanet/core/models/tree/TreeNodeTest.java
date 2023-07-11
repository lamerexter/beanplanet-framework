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