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
 * A preorder traversal of a whole tree, from its root, consulting only the children of each node (never a parent): see
 * {@link PreorderIterator}.
 *
 * @param <E> the type of the tree's nodes.
 */
class PreorderParentUnawareIterator<E> extends PreorderIterator<E> {
    public PreorderParentUnawareIterator(final Tree<E> tree) {
        super(tree);
    }
}
