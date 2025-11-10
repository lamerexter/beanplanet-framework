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

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Conversion functions from a {@link Stream} to various sequence types (arrays, collections etc).
 */
@TypeConverter
public final class StreamToCollectionConverters {
    /**
     * Converts the specified {@link Stream} to a {@link List} of the given component type.
     *
     * @param value the stream to be reduced to a list.
     */
    @SuppressWarnings("unchecked")
    @TypeConverter
    public static <T> List<T> streamToList(final Stream<T> value) {
        return (value == null ? null : value.collect(Collectors.toList()));
    }

    /**
     * Converts the specified {@link Stream} to a {@link java.util.LinkedHashSet} whose order is that of the stream.
     *
     * @param value the stream to be reduced to an ordered set.
     */
    @SuppressWarnings("unchecked")
    @TypeConverter
    public static <T> LinkedHashSet<T> streamToLinkedHashSet(final Stream<T> value) {
        return (value == null ? null : value.collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    /**
     * Converts the specified {@link Stream} to a {@link java.util.Set} whose order is that of the stream.
     *
     * @param value the stream to be reduced to an ordered set.
     */
    @SuppressWarnings("unchecked")
    @TypeConverter
    public static <T> Set<T> streamToSet(final Stream<T> value) {
        return streamToLinkedHashSet(value);
    }
}
