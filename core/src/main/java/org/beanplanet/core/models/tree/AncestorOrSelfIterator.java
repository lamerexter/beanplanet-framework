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

/**
 * The ancestor-or-self axis: the node itself, then its parent, its parent's parent and so on, nearest first (XPath's
 * order for this reverse axis), up to and including the tree's root, if the tree has one above the node, else the
 * node's oldest ancestor. Lazy: each parent is found when the iteration reaches it; {@link #previous()} walks back
 * towards the node.
 *
 * @param <T> the type of the tree's nodes.
 */
public class AncestorOrSelfIterator<T> extends AncestorAxisIterator<T> {
    public AncestorOrSelfIterator(final Tree<T> tree, final T from) {
        super(tree, from, true);
    }
}
