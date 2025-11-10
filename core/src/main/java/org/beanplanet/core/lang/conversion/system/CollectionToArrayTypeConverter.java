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

import java.lang.reflect.Array;
import java.util.*;

/**
 * A type converter of various {@link java.util.Collection} type to array type.
 */
@TypeConverter
public final class CollectionToArrayTypeConverter {
    /**
     * Converts a list of values to a <code>{@link LinkedHashSet}</code>.
     *
     * @param value a set containing the list values preserved in order of appearance in the list, or null if the list was null.
     */
    @SuppressWarnings("unchecked")
    @TypeConverter
    public static <T> T[] collectionToArray(Collection<T> value, Class<T> arraytype) {
        return (value == null ? null : value.stream().toArray(size -> (T[])Array.newInstance(arraytype.getComponentType(), size)));
    }
}
