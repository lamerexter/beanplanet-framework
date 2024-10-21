package org.beanplanet.core.net.http;

import org.beanplanet.core.models.Pair;
import org.beanplanet.core.util.PropertyBasedToStringBuilder;
import org.beanplanet.core.util.Streamable;

import java.util.*;
import java.util.stream.Stream;

import static org.beanplanet.core.util.StringUtil.isNotBlank;

public class Parameters implements Iterable<Pair<String, String>>, Streamable<Pair<String, String>> {
    private static final Parameters EMPTY = new Parameters();
    private final Map<String, String> parameterMap;

    public Parameters(Map<String, String> parameterMap) {
        this.parameterMap = Collections.unmodifiableMap(parameterMap);
    }

    public Parameters() {
        this(Collections.emptyMap());
    }

    public static Parameters empty() {
        return EMPTY;
    }

    public static Parameters singleton(final String name, final String value) {
        return new Parameters(Map.of(name, value));
    }

    public Parameters combine(final Parameters other) {
        Map<String, String> combined = new LinkedHashMap<>();
        combined.putAll(parameterMap);
        combined.putAll(other.parameterMap);

        return new Parameters(combined);
    }

    public boolean isEmpty() {
        return parameterMap.isEmpty();
    }

    public int size() {
        return parameterMap.size();
    }

    public Optional<String> get(final String name) {
        return parameterMap.entrySet().stream().filter(e -> e.getKey().equalsIgnoreCase(name)).map(Map.Entry::getValue).findFirst();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Parameters that = (Parameters) object;
        return Objects.equals(parameterMap, that.parameterMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parameterMap);
    }

    @Override
    public String toString() {
        return new PropertyBasedToStringBuilder(this).build();
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Pair<String, String>> iterator() {
        return stream().iterator();
    }

    @Override
    public Stream<Pair<String, String>> stream() {
        return parameterMap.entrySet().stream().map(e -> Pair.of(e.getKey(), e.getValue()));
    }

    public boolean hasCharset() {
        return isNotBlank(get("charset").orElse(null));
    }
}
