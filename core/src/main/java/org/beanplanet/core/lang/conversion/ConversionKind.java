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
package org.beanplanet.core.lang.conversion;

/**
 * What a type conversion does to a value, as its converter declares it ({@link
 * org.beanplanet.core.lang.conversion.annotations.TypeConverter#kind()}).
 *
 * @author Gary Watson
 */
public enum ConversionKind {
    /**
     * A value of the same kind, converted without loss: it never fails, loses nothing and assumes nothing (an int to a
     * long, a ZonedDateTime to an Instant, an array to a list).
     */
    WIDENING,
    /**
     * A conversion that may lose information (a long to an int, an Instant to a Date's milliseconds), assume something
     * (a time zone, a time of day), fail for some values (text parsed as a number), or change the kind of value (a
     * number to text, a date to its epoch milliseconds).
     */
    NARROWING,
    /** Not declared: never treated as narrowing, nor known to be widening (converters written before kinds existed). */
    UNSPECIFIED;

    /** The combined kind of several converters for the same conversion: widening if any is, else unspecified if any is. */
    public static ConversionKind combined(final Iterable<ConversionKind> kinds) {
        ConversionKind combined = null;
        for (ConversionKind kind : kinds) {
            if (kind == WIDENING) return WIDENING;
            if (kind == UNSPECIFIED || combined == null) combined = kind;
        }
        return combined == null ? UNSPECIFIED : combined;
    }
}
