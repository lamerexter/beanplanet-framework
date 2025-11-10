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

import java.util.Objects;

/**
 * Maintains the root node of this tree model.
 */
public abstract class AbstractTree<T> implements Tree<T> {
    /** The root node of this tree model instance, */
    protected T root;

    /**
     * Constructs an abstract tree model with the given root node in this instance.
     *
     * @param root the known root of this tree model.
     */
    public AbstractTree(T root) {
        this.root = root;
    }

    /**
     * Returns the known root node of this tree model instance.
     *
     * @return the root node of this tree model.
     */
    @Override
    public T getRoot() {
        return root;
    }

    public boolean equals(Object other) {
        if (this == other) return true;

        if ( !(other instanceof Tree) ) return false;
        final Tree<?> that = (Tree<?>)other;

        return Objects.equals(this.getRoot(), that.getRoot());
    }

    public int hashCode() {
        return Objects.hash(getRoot());
    }

}
