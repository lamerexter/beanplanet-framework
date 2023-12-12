package org.beanplanet.core.lang.conversion.system;

import org.junit.Test;

import java.sql.Date;
import java.time.*;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class Iso8601DateTimeConverterTest {
    @Test
    public void string_Date() {
        assertThat(Iso8601DateTimeConverter.stringToDate("2023-09-20T10:15:30.123456789Z"), equalTo(Date.from(Instant.parse("2023-09-20T10:15:30.123456789Z"))));
    }

    @Test
    public void string_Instant() {
        assertThat(Iso8601DateTimeConverter.stringToInstant("2023-09-20T10:15:30.123456789Z"), equalTo(Instant.parse("2023-09-20T10:15:30.123456789Z")));
    }

    @Test
    public void string_LocalDate() {
        assertThat(Iso8601DateTimeConverter.stringToLocalDate("2023-09-20"), equalTo(LocalDate.of(2023, 9, 20)));
    }

    @Test
    public void string_LocalDateTime() {
        assertThat(Iso8601DateTimeConverter.stringToLocalDateTime("2023-09-20T10:15:30.123456789"), equalTo(LocalDateTime.of(2023, 9, 20, 10, 15, 30, 123456789)));
    }

    @Test
    public void string_OffsetTime() {
        assertThat(Iso8601DateTimeConverter.stringToOffsetTime("10:15:30.123456789+03:30"), equalTo(OffsetTime.of(10, 15, 30, 123456789, ZoneOffset.ofHoursMinutes(3, 30))));
    }

    @Test
    public void string_OffsetDateTime() {
        assertThat(Iso8601DateTimeConverter.stringToOffsetDateTime("2023-09-20T10:15:30.123456789+03:30"), equalTo(OffsetDateTime.of(2023, 9, 20, 10, 15, 30, 123456789, ZoneOffset.ofHoursMinutes(3, 30))));
    }

    @Test
    public void string_ZonedDateTime() {
        assertThat(Iso8601DateTimeConverter.stringToZonedDateTime("2023-09-20T10:15:30.123456789+01:00[Europe/Paris]"), equalTo(ZonedDateTime.parse("2023-09-20T10:15:30.123456789+01:00[Europe/Paris]", DateTimeFormatter.ISO_ZONED_DATE_TIME)));
        assertThat(Iso8601DateTimeConverter.stringToZonedDateTime("2023-09-20T10:15:30.123456789+01:00"), equalTo(ZonedDateTime.parse("2023-09-20T10:15:30.123456789+01:00", DateTimeFormatter.ISO_ZONED_DATE_TIME)));
    }
}