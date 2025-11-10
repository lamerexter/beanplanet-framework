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

package org.beanplanet.core.models;

import java.util.Objects;

public class Pair<L, R> {
    private final L left;
    private final R right;

    public Pair() {
        this(null, null);
    }

    public Pair(final L left) {
        this(left, null);
    }

    public Pair(final L left, final R right) {
        this.left = left;
        this.right = right;
    }

    public static <A, B> Pair<A, B> of(final A left, final B right) {
        return new Pair<>(left, right);
    }

    public static <A, B> Pair<A, B> pairOf(final A left, final B right) {
        return of(left, right);
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }

    public boolean leftIsNotNull() {
        return Objects.nonNull(left);
    }

    public boolean rightIsNotNull() {
        return Objects.nonNull(right);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> that = (Pair<?, ?>) o;
        return Objects.equals(getLeft(), that.getLeft()) && Objects.equals(getRight(), that.getRight());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLeft(), getRight());
    }
}
