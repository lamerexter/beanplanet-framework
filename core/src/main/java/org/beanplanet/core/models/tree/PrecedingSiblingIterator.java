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
 * The preceding-sibling axis: the siblings before a node, nearest first (XPath's order for this reverse axis). Empty
 * for the tree's root. The node is found among its siblings once, as the same object or else the first equal to it.
 *
 * @param <T> the type of the tree's nodes.
 */
public class PrecedingSiblingIterator<T> extends SiblingAxisIterator<T> {
    public PrecedingSiblingIterator(final Tree<T> tree, final T from) {
        super(tree, from, false);
    }
}
