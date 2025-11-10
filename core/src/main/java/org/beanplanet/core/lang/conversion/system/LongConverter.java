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
