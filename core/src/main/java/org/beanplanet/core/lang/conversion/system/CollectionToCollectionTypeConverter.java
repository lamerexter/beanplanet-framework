/*
 *  MIT Licence:
 *
 *  Copyright (C) 2018 Beanplanet Ltd
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without restriction
 *  including without limitation the rights to use, copy, modify, merge,
 *  publish, distribute, sublicense, and/or sell copies of the Software,
 *  and to permit persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 *  KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 *  PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 *  CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  DEALINGS IN THE SOFTWARE.
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
    public static <T> Set<T> listToLinkedHashSetConversion(List<T> value) {
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
