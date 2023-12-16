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

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * A type converter from {@link Long} to meaningful target types.
 */
@TypeConverter
public final class LongConverter {
    @TypeConverter
    public static Date toDate(final long value) {
        return DateTimeConversionUtil.toDate(value);
    }

    @TypeConverter
    public static Date toDate(final Long value) {
        return DateTimeConversionUtil.toDate(value);
    }

    @TypeConverter
    public static Instant toInstant(final long value) {
        return DateTimeConversionUtil.toInstant(value);
    }

    @TypeConverter
    public static Instant toInstant(final Long value) {
        return DateTimeConversionUtil.toInstant(value);
    }

    @TypeConverter
    public static LocalDate toLocalDate(final long value) {
        return DateTimeConversionUtil.toLocalDate(value);
    }

    @TypeConverter
    public static LocalDate toLocalDate(final Long value) {
        return DateTimeConversionUtil.toLocalDate(value);
    }

    @TypeConverter
    public static ZonedDateTime toOffsetDateTime(final long value) {
        return DateTimeConversionUtil.toZonedDateTime(value);
    }

    @TypeConverter
    public static ZonedDateTime toOffsetDateTime(final Long value) {
        return DateTimeConversionUtil.toZonedDateTime(value);
    }

    @TypeConverter
    public static ZonedDateTime toZonedDateTime(final long value) {
        return DateTimeConversionUtil.toZonedDateTime(value);
    }

    @TypeConverter
    public static ZonedDateTime toZonedDateTime(final Long value) {
        return DateTimeConversionUtil.toZonedDateTime(value);
    }
}
