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

import java.time.*;
import java.util.Date;

public class DateTimeConversionUtil {
    public static Date toDate(final LocalDate localDate) {
        return localDate == null ? null : Date.from(toInstant(localDate));
    }

    public static Date toDate(final long epochMillis) {
        return Date.from(toInstant(epochMillis));
    }

    public static Date toDate(final Long epochMillis) {
        return epochMillis == null ? null : toDate(epochMillis.longValue());
    }

    public static Date toDate(final LocalDateTime localDateTime) {
        return localDateTime == null ? null : Date.from(toInstant(localDateTime));
    }

    public static Date toDate(final OffsetDateTime offsetDateTime) {
        return offsetDateTime == null ? null : Date.from(offsetDateTime.toInstant());
    }

    public static Date toDate(final ZonedDateTime zonedDateTime) {
        return zonedDateTime == null ? null : Date.from(zonedDateTime.toInstant());
    }

    public static Date toDate(final Instant instant) {
        return instant == null ? null : Date.from(instant);
    }

    public  static Instant toInstant(final Date date) {
        return date == null ? null : date.toInstant();
    }

    public static Instant toInstant(final LocalDate localDate) {
        return toInstant(localDate, ZoneId.systemDefault());
    }

    public static Instant toInstant(final LocalDate localDate, final ZoneId zoneId) {
        return localDate == null ? null : localDate.atStartOfDay().atZone(zoneId).toInstant();
    }

    public static Instant toInstant(final LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    public static Instant toInstant(final LocalDateTime localDateTime, final ZoneId zoneId) {
        return localDateTime == null ? null : localDateTime.atZone(zoneId).toInstant();
    }

    public static Instant toInstant(final OffsetDateTime offsetDateTime) {
        return offsetDateTime == null ? null : offsetDateTime.toInstant();
    }

    public  static Instant toInstant(final ZonedDateTime zonedDateTime) {
        return zonedDateTime == null ? null : zonedDateTime.toInstant();
    }

    public  static Instant toInstant(final long epochTimeMillis) {
        return Instant.ofEpochMilli(epochTimeMillis);
    }

    public  static Instant toInstant(final Long epochTimeMillis) {
        return epochTimeMillis == null ? null : toInstant(epochTimeMillis.longValue());
    }

    public static LocalDate toLocalDate(final Date date) {
        return date == null ? null : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDate toLocalDate(final Instant instant) {
        return instant == null ? null : instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDate toLocalDate(final LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.toLocalDate();
    }

    public static LocalDate toLocalDate(final long epochTimeMillis) {
        return toLocalDateTime(epochTimeMillis).toLocalDate();
    }

    public static LocalDate toLocalDate(final Long epochTimeMillis) {
        return epochTimeMillis == null ? null : toLocalDateTime(epochTimeMillis).toLocalDate();
    }

    public static LocalDate toLocalDate(final OffsetDateTime offsetDateTime) {
        return offsetDateTime == null ? null : offsetDateTime.toLocalDate();
    }

    public static LocalDate toLocalDate(final ZonedDateTime zonedDateTime) {
        return zonedDateTime == null ? null : zonedDateTime.toLocalDate();
    }

    public static LocalDateTime toLocalDateTime(final Date date) {
        return date == null ? null : date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDateTime toLocalDateTime(final Instant instant) {
        return instant == null ? null : instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDateTime toLocalDateTime(final LocalDate localDate) {
        return localDate == null ? null : localDate.atStartOfDay();
    }

    public static LocalDateTime toLocalDateTime(final long epochTimeMillis) {
        return Instant.ofEpochMilli(epochTimeMillis).atZone(ZoneId.of("UTC")).toLocalDateTime();
    }

    public static LocalDateTime toLocalDateTime(final OffsetDateTime offsetDateTime) {
        return offsetDateTime == null ? null : offsetDateTime.toLocalDateTime();
    }

    public static LocalDateTime toLocalDateTime(final ZonedDateTime zonedDateTime) {
        return zonedDateTime == null ? null : zonedDateTime.toLocalDateTime();
    }

    public static Long toLong(LocalDate localDate) {
        return localDate == null ? null : localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static Long toLong(LocalDateTime localDate) {
        return localDate == null ? null : localDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public static OffsetDateTime toOffsetDateTime(final Date date) {
        return date == null ? null : date.toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime();
    }

    public static OffsetDateTime toOffsetDateTime(final Instant instant) {
        return instant == null ? null : instant.atZone(ZoneId.systemDefault()).toOffsetDateTime();
    }

    public static OffsetDateTime toOffsetDateTime(final LocalDate localDate) {
        return localDate == null ? null : localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toOffsetDateTime();
    }

    public static OffsetDateTime toOffsetDateTime(final LocalDate localDate, final ZoneOffset zoneOffset) {
        return localDate == null ? null : localDate.atStartOfDay().atOffset(zoneOffset);
    }

    public static OffsetDateTime toOffsetDateTime(final LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.atZone(ZoneId.systemDefault()).toOffsetDateTime();
    }

    public static OffsetDateTime toOffsetDateTime(final LocalDateTime localDateTime,final  ZoneOffset zoneOffset) {
        return localDateTime == null ? null : localDateTime.atOffset(zoneOffset);
    }

    public static OffsetDateTime toOffsetDateTime(final long epochTimeMillis) {
        return OffsetDateTime.ofInstant(toInstant(epochTimeMillis), ZoneId.of("UTC"));
    }

    public static OffsetDateTime toOffsetDateTime(final ZonedDateTime zonedDateTime) {
        return zonedDateTime == null ? null : zonedDateTime.toOffsetDateTime();
    }

    public static ZonedDateTime toZonedDateTime(final Date date) {
        return date == null ? null : date.toInstant().atZone(ZoneId.systemDefault());
    }

    public static ZonedDateTime toZonedDateTime(final Instant instant) {
        return instant == null ? null : instant.atZone(ZoneId.systemDefault());
    }

    public static ZonedDateTime toZonedDateTime(final LocalDate localDate) {
        return localDate == null ? null : toZonedDateTime(localDate, ZoneId.systemDefault());
    }

    public static ZonedDateTime toZonedDateTime(final LocalDate localDate, final ZoneId zoneId) {
        return localDate == null ? null : localDate.atStartOfDay().atZone(zoneId);
    }

    public static ZonedDateTime toZonedDateTime(final LocalDateTime localDateTime) {
        return localDateTime == null ? null : toZonedDateTime(localDateTime, ZoneId.systemDefault());
    }

    public static ZonedDateTime toZonedDateTime(final LocalDateTime localDateTime, final ZoneId zoneId) {
        return localDateTime == null ? null : localDateTime.atZone(zoneId);
    }

    public static ZonedDateTime toZonedDateTime(final long epochTimeMillis) {
        return toZonedDateTime(epochTimeMillis, ZoneId.systemDefault());
    }

    public static ZonedDateTime toZonedDateTime(final long epochTimeMillis, final ZoneId zoneId) {
        return ZonedDateTime.ofInstant(toInstant(epochTimeMillis), zoneId);
    }

    public static ZonedDateTime toZonedDateTime(final OffsetDateTime offsetDateTime) {
        return offsetDateTime == null ? null : offsetDateTime.toZonedDateTime();
    }

    public static Date toUtcDate(final LocalDate localDate) {
        return localDate == null ? null : Date.from(toUtcInstant(localDate));
    }

    public static Date toUtcDate(final LocalDateTime localDateTime) {
        return localDateTime == null ? null : Date.from(toUtcInstant(localDateTime));
    }

    public static Instant toUtcInstant(final LocalDate localDate) {
        return toInstant(localDate, ZoneId.of("UTC"));
    }

    public static Instant toUtcInstant(final LocalDateTime localDateTime) {
        return localDateTime == null ? null : localDateTime.atZone(ZoneId.of("UTC")).toInstant();
    }

    public static Long toLong(Date value) {
        return value == null ? null : value.getTime();
    }

    public static Long toLong(Instant value) {
        return value == null ? null : value.toEpochMilli();
    }

    public static Long toLong(OffsetDateTime value) {
        return value == null ? null : value.toInstant().toEpochMilli();
    }

    public static Long toLong(ZonedDateTime value) {
        return value == null ? null : value.toInstant().toEpochMilli();
    }
}
