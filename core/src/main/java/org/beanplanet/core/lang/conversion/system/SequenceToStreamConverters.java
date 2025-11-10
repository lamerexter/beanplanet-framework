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
package org.beanplanet.core.lang.conversion.system;

import org.beanplanet.core.lang.conversion.annotations.TypeConverter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Conversion functions from various sequence types (arrays, collections etc) to a {@link java.util.stream.Stream} of elements.
 */
@TypeConverter
public final class SequenceToStreamConverters {

    /**
     * Converts the specified array to a {@link java.util.stream.Stream}.
     *
     * @param value the array to be streamed.
     */
    @TypeConverter
    public static <T> Stream<T> arrayToStream(T[] value) {
        return (value == null ? null : Arrays.stream(value));
    }

    /**
     * Converts the specified array to a {@link java.util.stream.Stream}.
     *
     * @param value the array to be streamed.
     */
    @TypeConverter
    public static Stream<Integer> arrayToStream(int[] value) {
        return (value == null ? null : Arrays.stream(value).boxed());
    }

    /**
     * Converts the specified list to a {@link java.util.stream.Stream}.
     *
     * @param value the list to be streamed.
     */
    @TypeConverter
    public static <T> Stream<T> listToStream(List<T> value) {
        return (value == null ? null : value.stream());
    }

    /**
     * Converts the specified set to a {@link java.util.stream.Stream}.
     *
     * @param value the set to be streamed.
     */
    @TypeConverter
    public static <T> Stream<T> setToStream(Set<T> value) {
        return (value == null ? null : value.stream());
    }

    /**
     * Converts the specified map to a {@link java.util.stream.Stream} of its entries.
     *
     * @param value the map to be streamed.
     */
    @TypeConverter
    public static <K, V> Stream<Map.Entry<K, V>> mapToStream(Map<K, V> value) {
        return (value == null ? null : value.entrySet().stream());
    }
}
