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

import org.junit.Test;

import java.time.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

/**
 * Temporal utility test cases.
 */
public class TemporalUtilTest {

    @Test
    public void testToIsoInstantStringWithInstantInUtc() {
        Instant instant = Instant.parse("2023-01-15T10:15:30Z");
        String result = TemporalUtil.toIsoInstantInUtcString(instant);
        assertEquals("2023-01-15T10:15:30Z", result);
    }

    @Test
    public void testToIsoInstantInUtcStringWithZonedDateTime() {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse("2023-01-15T10:15:30+01:00");
        String result = TemporalUtil.toIsoInstantInUtcString(zonedDateTime);
        assertEquals("2023-01-15T09:15:30Z", result); // Adjusted to UTC
    }

    @Test
    public void testToIsoInstantInUtcStringWithOffsetDateTime() {
        OffsetDateTime offsetDateTime = OffsetDateTime.parse("2023-01-15T10:15:30+01:00");
        String result = TemporalUtil.toIsoInstantInUtcString(offsetDateTime);
        assertEquals("2023-01-15T09:15:30Z", result); // Adjusted to UTC
    }

    @Test
    public void testToIsoInstantInUtcStringWithLocalDate() {
        // LocalDate is converted to ZonedDateTime at start of day in UTC
        LocalDate localDate = LocalDate.parse("2023-01-15");
        String result = TemporalUtil.toIsoInstantInUtcString(localDate);
        assertEquals("2023-01-15T00:00:00Z", result);
    }

    @Test
    public void testToIsoInstantInUtcStringWithLocalDateTime() {
        // LocalDateTime is converted to ZonedDateTime in UTC
        LocalDateTime localDateTime = LocalDateTime.parse("2023-01-15T10:15:30");
        String result = TemporalUtil.toIsoInstantInUtcString(localDateTime);
        assertEquals("2023-01-15T10:15:30Z", result);
    }

    @Test
    public void testToIsoInstantInUtcStringWithLocalDateTimeAtZone() {
        // Convert LocalDateTime to ZonedDateTime to make it compatible with ISO_INSTANT
        LocalDateTime localDateTime = LocalDateTime.parse("2023-01-15T10:15:30");
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("UTC"));
        String result = TemporalUtil.toIsoInstantInUtcString(zonedDateTime);
        assertEquals("2023-01-15T10:15:30Z", result);
    }

    @Test
    public void testToIsoInstantInUtcStringWithLocalDateAtZone() {
        // Convert LocalDate to ZonedDateTime to make it compatible with ISO_INSTANT
        LocalDate localDate = LocalDate.parse("2023-01-15");
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.of("UTC"));
        String result = TemporalUtil.toIsoInstantInUtcString(zonedDateTime);
        assertEquals("2023-01-15T00:00:00Z", result);
    }
    @Test
    public void testToIsoInstantAtEndOfDayInUtcStringWithStandardDate() {
        // Standard date converted to end of day in UTC
        LocalDate localDate = LocalDate.parse("2023-01-15");
        String result = TemporalUtil.toIsoInstantAtEndOfDayInUtcString(localDate);
        assertEquals("2023-01-15T23:59:59.999999999Z", result);
    }

    @Test
    public void testToIsoInstantAtEndOfDayInUtcStringWithMonthEnd() {
        // Date at end of month converted to end of day in UTC
        LocalDate localDate = LocalDate.parse("2023-01-31");
        String result = TemporalUtil.toIsoInstantAtEndOfDayInUtcString(localDate);
        assertEquals("2023-01-31T23:59:59.999999999Z", result);
    }

    @Test
    public void testToIsoInstantAtEndOfDayInUtcStringWithYearEnd() {
        // Date at end of year converted to end of day in UTC
        LocalDate localDate = LocalDate.parse("2023-12-31");
        String result = TemporalUtil.toIsoInstantAtEndOfDayInUtcString(localDate);
        assertEquals("2023-12-31T23:59:59.999999999Z", result);
    }

    @Test
    public void testToIsoInstantAtEndOfDayInUtcStringWithLeapYear() {
        // Date in leap year (February 29) converted to end of day in UTC
        LocalDate localDate = LocalDate.parse("2020-02-29");
        String result = TemporalUtil.toIsoInstantAtEndOfDayInUtcString(localDate);
        assertEquals("2020-02-29T23:59:59.999999999Z", result);
    }

    @Test
    public void testToEndOfDayWithStandardDate() {
        // Standard date converted to end of day
        LocalDate localDate = LocalDate.parse("2023-01-15");
        LocalDateTime result = TemporalUtil.toEndOfDay(localDate);
        assertEquals(LocalDateTime.of(2023, 1, 15, 23, 59, 59, 999999999), result);
    }

    @Test
    public void testToEndOfDayWithMonthEnd() {
        // Date at end of month converted to end of day
        LocalDate localDate = LocalDate.parse("2023-01-31");
        LocalDateTime result = TemporalUtil.toEndOfDay(localDate);
        assertEquals(LocalDateTime.of(2023, 1, 31, 23, 59, 59, 999999999), result);
    }

    @Test
    public void testToEndOfDayWithYearEnd() {
        // Date at end of year converted to end of day
        LocalDate localDate = LocalDate.parse("2023-12-31");
        LocalDateTime result = TemporalUtil.toEndOfDay(localDate);
        assertEquals(LocalDateTime.of(2023, 12, 31, 23, 59, 59, 999999999), result);
    }

    @Test
    public void testToEndOfDayWithLeapYear() {
        // Date in leap year (February 29) converted to end of day
        LocalDate localDate = LocalDate.parse("2020-02-29");
        LocalDateTime result = TemporalUtil.toEndOfDay(localDate);
        assertEquals(LocalDateTime.of(2020, 2, 29, 23, 59, 59, 999999999), result);
    }

    @Test
    public void testToEndOfDayWithNull() {
        // Null input should return null
        assertNull(TemporalUtil.toEndOfDay(null));
    }
}
