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
