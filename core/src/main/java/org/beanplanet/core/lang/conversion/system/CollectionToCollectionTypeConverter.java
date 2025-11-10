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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * A type converter supporting inter-collection type conversions.
 */
@TypeConverter
public final class CollectionToCollectionTypeConverter {
    /**
     * Converts a list of values to a <code>{@link LinkedHashSet}</code>.
     *
     * @param value a set containing the list values preserved in order of appearance in the list, or null if the list was null.
     */
    @TypeConverter
    public static <T> Set<T> listToSetConversion(List<T> value) {
        return value == null ? null : new LinkedHashSet<>(value);
    }

    /**
     * Converts a set to an <code>{@link ArrayList}</code> of values. The order of values in the returned list is entirely dependent on the
     * order returned by the set implementation.
     *
     * @param value a list containing the values in order of appearance in the set, or null if the set was null.
     */
    @TypeConverter
    public static <T> List<T> setToListConversion(Set<T> value) {
        return value == null ? null : new ArrayList<>(value);
    }
}
