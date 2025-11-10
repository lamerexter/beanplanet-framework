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

import java.time.*;
import java.util.Date;

/**
 * A type converter from {@link LocalDate} to meaningful target types.
 */
@TypeConverter
public final class LocalDateConverter {
    @TypeConverter
    public static Date toDate(final LocalDate value) {
        return DateTimeConversionUtil.toDate(value);
    }

    @TypeConverter
    public static Instant toInstant(final LocalDate value) {
        return DateTimeConversionUtil.toInstant(value);
    }

    @TypeConverter
    public static LocalDateTime toLocalDateTime(final LocalDate value) {
        return DateTimeConversionUtil.toLocalDateTime(value);
    }

    @TypeConverter
    public static long toLongPrimitive(final LocalDate value) {
        return DateTimeConversionUtil.toLong(value);
    }

    @TypeConverter
    public static Long toLongPrimitiveWrapper(final LocalDate value) {
        return DateTimeConversionUtil.toLong(value);
    }

    @TypeConverter
    public static OffsetDateTime toOffsetDateTime(final LocalDate value) {
        return DateTimeConversionUtil.toOffsetDateTime(value);
    }

    @TypeConverter
    public static ZonedDateTime toZonedDateTime(final LocalDate value) {
        return DateTimeConversionUtil.toZonedDateTime(value);
    }
}
