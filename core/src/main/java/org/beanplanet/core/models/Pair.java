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
