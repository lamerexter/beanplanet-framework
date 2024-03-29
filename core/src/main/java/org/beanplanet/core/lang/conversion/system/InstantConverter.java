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

import org.beanplanet.core.lang.conversion.DateTimeConversionUtil;
import org.beanplanet.core.lang.conversion.annotations.TypeConverter;

import java.time.*;
import java.util.Date;

/**
 * A type converter from {@link Instant} to meaningful target types.
 */
@TypeConverter
public final class InstantConverter {
    @TypeConverter
    public static Date toDate(final Instant value) {
        return DateTimeConversionUtil.toDate(value);
    }

    @TypeConverter
    public static LocalDate toLocalDate(final Instant value) {
        return DateTimeConversionUtil.toLocalDate(value);
    }

    @TypeConverter
    public static LocalDateTime toLocalDateTime(final Instant value) {
        return DateTimeConversionUtil.toLocalDateTime(value);
    }

    @TypeConverter
    public static long toLongPrimitive(final Instant value) {
        return DateTimeConversionUtil.toLong(value);
    }

    @TypeConverter
    public static Long toLongPrimitiveWrapper(final Instant value) {
        return DateTimeConversionUtil.toLong(value);
    }

    @TypeConverter
    public static OffsetDateTime toOffsetDateTime(final Instant value) {
        return DateTimeConversionUtil.toOffsetDateTime(value);
    }

    @TypeConverter
    public static ZonedDateTime toZonedDateTime(final Instant value) {
        return DateTimeConversionUtil.toZonedDateTime(value);
    }
}
