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

import static java.util.Arrays.asList;

/**
 * A type converter supporting arrays to collection type conversions.
 */
@TypeConverter
public final class ArrayToCollectionTypeConverter {
    /**
     * Converts an array of reference type to a <code>{@link LinkedHashSet}</code>.
     *
     * @param values an array containing the list values to be added to a new <code>{@link LinkedHashSet}</code>.
     * @return a <code>{@link LinkedHashSet}</code> containing the array values preserved in order of appearance, or null if the array was null.
     */
    @TypeConverter
    public static <T> Set<T> arrayOfReferenceTypeToLinkedHashSetConversion(T[] values) {
        return values == null ? null : new LinkedHashSet<>(asList(values));
    }

    /**
     * Converts an array of reference type to a <code>{@link List}</code>.
     *
     * @param values an array containing the list values to be added to a new <code>{@link List}</code>.
     * @return a <code>{@link List}</code> containing the array values preserved in order of appearance, or null if the array was null.
     */
    @TypeConverter
    public static <T> List<T> arrayOfReferenceTypeToListConversion(T[] values) {
        return values == null ? null : new ArrayList<>(asList(values));
    }
}
