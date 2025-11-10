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
