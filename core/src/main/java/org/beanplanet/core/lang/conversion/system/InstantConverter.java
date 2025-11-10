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
