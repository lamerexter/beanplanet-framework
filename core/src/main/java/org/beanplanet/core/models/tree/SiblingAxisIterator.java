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

import java.util.List;

/**
 * The following-sibling and preceding-sibling axes: the siblings after a node, in document order, or before it,
 * nearest first (XPath's order for that reverse axis). Empty for the tree's root (its siblings, if it has any, are not
 * in the tree), and for a node that is not among its parent's children (as an XML attribute). The node is found among
 * its siblings once, as the same object or else the first equal to it.
 *
 * @param <T> the type of the tree's nodes.
 */
class SiblingAxisIterator<T> extends CursorTreeIterator<T> {
    private final Tree<T> tree;
    private final T from;
    private final boolean following;
    private List<T> siblings;
    private int index;

    SiblingAxisIterator(final Tree<T> tree, final T from, final boolean following) {
        super(true);
        this.tree = tree;
        this.from = from;
        this.following = following;
    }

    @Override
    protected boolean first() {
        final T parent = TreePath.parentWithin(tree, from);
        if (parent == null) return false;
        siblings = TreePath.childrenOf(tree, parent);
        index = TreePath.indexOf(siblings, from);
        // A node that is not among its parent's children (as an XML attribute) has no siblings
        return index >= 0 && forward();
    }

    @Override
    protected T current() {
        return siblings.get(index);
    }

    @Override
    protected boolean forward() {
        return following ? to(index + 1) : to(index - 1);
    }

    @Override
    protected boolean backward() {
        return following ? to(index - 1) : to(index + 1);
    }

    private boolean to(final int sibling) {
        if (sibling < 0 || sibling >= siblings.size()) return false;
        index = sibling;
        return true;
    }
}
