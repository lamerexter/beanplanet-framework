package org.beanplanet.core.models;

import java.util.Objects;

/**
 * Represents a triple of typed values.
 *
 * @param <T1> the type of the first value.
 * @param <T2> the type of the second value.
 * @param <T3> the type of the third value.
 */
public class Triple<T1, T2, T3> {
    private final T1 first;
    private final T2 second;
    private final T3 third;

    public Triple() {
        this(null, null, null);
    }

    public Triple(final T1 first, final T2 second, final T3 third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public static <A, B, C> Triple<A, B, C> of(final A first, final B second, final C third) {
        return new Triple<>(first, second, third);
    }

    public static <A, B, C> Triple<A, B, C> tripleOf(final A first, final B second, final C third) {
        return of(first, second, third);
    }

    public T1 getFirst() {
        return first;
    }

    public T2 getSecond() {
        return second;
    }

    public T3 getThird() {
        return third;
    }

    public boolean firstIsNotNull() {
        return Objects.nonNull(first);
    }

    public boolean secondIsNotNull() {
        return Objects.nonNull(second);
    }

    public boolean thirdIsNotNull() {
        return Objects.nonNull(third);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triple<?, ?, ?> that = (Triple<?, ?, ?>) o;
        return Objects.equals(getFirst(), that.getFirst())
                && Objects.equals(getSecond(), that.getSecond())
                && Objects.equals(getThird(), that.getThird());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirst(), getSecond(), getThird());
    }
}
