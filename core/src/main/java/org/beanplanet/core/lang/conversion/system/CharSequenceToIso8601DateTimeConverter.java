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

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * A type converter between ISO8601 date/time format strings and the various Java types representing dates and times:
 * {@link java.util.Date}, {@link java.time.LocalDate}, {@link java.time.LocalDateTime}, {@link java.time.OffsetDateTime},
 * {@link java.time.ZonedDateTime} and {@link Long}/long (representing epoch milliseconds).
 */
@TypeConverter
public final class CharSequenceToIso8601DateTimeConverter {
    @TypeConverter
    public static Date stringToDate(final CharSequence value) {
        return value == null ? null : Date.from(stringToInstant(value));
    }

    @TypeConverter
    public static Instant stringToInstant(final CharSequence value) {
        return value == null ? null : Instant.parse(value);
    }

    @TypeConverter
    public static LocalDate stringToLocalDate(final CharSequence value) {
        return value == null ? null : LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @TypeConverter
    public static LocalDateTime stringToLocalDateTime(final CharSequence value) {
        return value == null ? null : LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    @TypeConverter
    public static OffsetDateTime stringToOffsetDateTime(final CharSequence value) {
        return value == null ? null : OffsetDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    @TypeConverter
    public static OffsetTime stringToOffsetTime(final CharSequence value) {
        return value == null ? null : OffsetTime.parse(value, DateTimeFormatter.ISO_OFFSET_TIME);
    }

    @TypeConverter
    public static ZonedDateTime stringToZonedDateTime(final CharSequence value) {
        return value == null ? null : ZonedDateTime.parse(value, DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }
}
