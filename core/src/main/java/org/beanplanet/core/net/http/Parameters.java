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

package org.beanplanet.core.net.http;

import org.beanplanet.core.models.Pair;
import org.beanplanet.core.util.PropertyBasedToStringBuilder;
import org.beanplanet.core.util.Streamable;

import java.nio.charset.Charset;
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

    public Optional<Charset> getCharset() {
        return get("charset").map(Charset::forName);
    }
}
