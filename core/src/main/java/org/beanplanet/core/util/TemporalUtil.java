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

package org.beanplanet.core.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

/**
 * Temporal utility, for all things date and time related.
 */
public class TemporalUtil {
    /**
     * Formats a given temporal to an ISO-8601 instant string representation in UTC.
     * If the temporal is a LocalDate or LocalDateTime, it will be converted to a ZonedDateTime in UTC (and at the start
     * of the day in the case of LocalDate).
     *
     * @param temporal the temporal whose instant string representation is to be output.
     * @return the ISO-8601 UTC instant string representation of the temporal.
     */
    public static String toIsoInstantInUtcString(final Temporal temporal) {
        if (temporal instanceof LocalDate) {
            return DateTimeFormatter.ISO_INSTANT.format(((LocalDate) temporal).atStartOfDay(ZoneId.of("UTC")));
        } else if (temporal instanceof LocalDateTime) {
            return DateTimeFormatter.ISO_INSTANT.format(((LocalDateTime) temporal).atZone(ZoneId.of("UTC")));
        } else {
            return DateTimeFormatter.ISO_INSTANT.format(temporal);
        }
    }

    /**
     * Converts a {@link LocalDate} to a {@link LocalDateTime} at the end of the day.
     *
     * @param localDate the local date to be converted to a local date and time at the end of the given day.
     * @return a local date and time at the end of the given day represented by the given local date.
     */
    public static LocalDateTime toEndOfDay(final LocalDate localDate) {
        return localDate == null ? null : localDate.atTime(LocalTime.MAX);
    }

    /**
     * Formats a given {@link LocalDate}, at the end of the day, to an ISO-8601 instant string representation in UTC.
     * If the temporal is a LocalDate or LocalDateTime, it will be converted to a ZonedDateTime in UTC (and at the start
     * of the day in the case of LocalDate).
     *
     * @param localDate the local date whose instant string representation is to be output.
     * @return the ISO-8601 UTC instant string representation of the local date, at the end of the given day.
     */
    public static String toIsoInstantAtEndOfDayInUtcString(final LocalDate localDate) {
        return DateTimeFormatter.ISO_INSTANT.format(toEndOfDay(localDate).atZone(ZoneId.of("UTC")));
    }
}
