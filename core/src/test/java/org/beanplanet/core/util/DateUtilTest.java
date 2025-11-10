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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;


/**
 * Date utility class test cases.
 * 
 * @author Gary Watson
 */
public class DateUtilTest {
   @Test
   public void testToday() throws ParseException {
      SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
      Date today = df.parse(df.format(new Date()));
      assertEquals(today, DateUtil.today());
   }

   @Test
   public void testTruncate() throws ParseException {
      SimpleDateFormat testDF = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
      Date testDateTime = testDF.parse("21-04-1992 11:43:23");
      SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
      Date testDate = df.parse(df.format(testDateTime));
      assertEquals(testDate, DateUtil.trunc(testDateTime));
   }
   
   @Test
   public void testParseNullDateStringShouldBeParsedAsNull() {
      assertNull(DateUtil.parse(null, "yyyy-MM-dd'T'HH:mm:ss'Z'"));
   }

   @Test(expected=IllegalArgumentException.class)
   public void testParseNullDateStringFormatShouldThrowIllegalArgumentException() {
      assertNull(DateUtil.parse("abc", (String)null));
   }

   @Test(expected=org.beanplanet.core.lang.ParseException.class)
   public void testParseDateStringNotInSpecifiedFormatThrowsParseException() {
      assertNull(DateUtil.parse("abc", "yyyy-MM-dd'T'HH:mm:ss'Z'"));
   }

   @Test
   public void testParseDateStringInGivenFormat() throws ParseException {
      assertNotNull(DateUtil.parse("2011-01-18T18:43:14Z", "yyyy-MM-dd'T'HH:mm:ss'Z'"));
      
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
      df.setTimeZone(TimeZone.getTimeZone("UTC"));
      assertEquals(df.parse("2011-01-18T18:43:14Z"), DateUtil.parse("2011-01-18T18:43:14Z", "yyyy-MM-dd'T'HH:mm:ss'Z'"));
   }
   
   @Test(expected=org.beanplanet.core.lang.ParseException.class)
   public void invalidISO8601DatetimeShouldFailParsing() {
      DateUtil.parseIso8601Datetime("123456-av-01T99:75:88");
   }

   @Test
   public void testNormativeISO8601Datetime() throws ParseException {
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss z");
      df.setTimeZone(TimeZone.getTimeZone("UTC"));
      assertEquals(df.parse("2011-01-18T18:43:14 GMT"), DateUtil.parseIso8601Datetime("2011-01-18T18:43:14Z"));
   }

   @Test
   public void testISO8601DatetimeWithTimezoneOffset() throws ParseException {
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss Z");
      df.setTimeZone(TimeZone.getTimeZone("UTC"));
      assertEquals(df.parse("2011-01-18T18:43:14 +0330"), DateUtil.parseIso8601Datetime("2011-01-18T18:43:14+03:30"));
   }

   @Test
   public void testISO8601DatetimeWithMillisAndTimezoneOffset() throws ParseException {
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S Z");
      df.setTimeZone(TimeZone.getTimeZone("UTC"));
      assertEquals(df.parse("2011-01-18T18:43:14.512 +0330"), DateUtil.parseIso8601Datetime("2011-01-18T18:43:14.512+03:30"));
   }

   @Test
   public void testISO8601DatetimeWithMillis() throws ParseException {
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S");
      df.setTimeZone(TimeZone.getTimeZone("UTC"));
      assertEquals(df.parse("2011-01-18T18:43:14.650"), DateUtil.parseIso8601Datetime("2011-01-18T18:43:14.650"));
   }

   @Test
   public void testISO8601DatetimeFormat() throws ParseException {
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
      df.setTimeZone(TimeZone.getTimeZone("UTC"));
      assertEquals("2011-01-18T18:43:14Z", DateUtil.toIso8601String(df.parse("2011-01-18T18:43:14")));
   }

   @Test
   public void testToEndOfDay() throws ParseException {
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
      assertEquals(DateUtil.parseIso8601Datetime("2011-01-18T23:59:59.999"), DateUtil.toEndOfDay(df.parse("2011-01-18T18:43:14")));
   }
   
   @Test
   public void testToEndOfDayAtEndOfCalendarMonthWhenNull() {
      assertNull(DateUtil.toEndOfDayAtEndOfCalendarMonth(null));
   }

   @Test
   public void testToEndOfDayAtEndOfCalendarMonth() throws ParseException {
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
      df.setTimeZone(TimeZone.getTimeZone("UTC"));
      assertEquals(DateUtil.parseIso8601Datetime("2010-01-31T23:59:59.999"), DateUtil.toEndOfDayAtEndOfCalendarMonth(df.parse("2010-01-02T13:43:14")));
   }

   @Test
   public void testToEndOfDayAtEndOfCalendarMonthForFebLeapYear() throws ParseException {
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
      df.setTimeZone(TimeZone.getTimeZone("UTC"));
      assertEquals(DateUtil.parseIso8601Datetime("2004-02-29T23:59:59.999"), DateUtil.toEndOfDayAtEndOfCalendarMonth(df.parse("2004-02-02T13:43:14")));
   }
   
   @Test
   public void testMaxWithNull() {
      assertThat(DateUtil.max((Date[])null), nullValue());
   }

   @Test
   public void testMaxWithListOfNulls() {
      assertThat(DateUtil.max(new Date[5]), nullValue());
   }

   @Test
   public void testMaxWithSingleDate() {
      Date now = DateUtil.now();
      assertThat(DateUtil.max(now), equalTo(now));
   }

   @Test
   public void testMax() {
      Date now = DateUtil.now();
      Date latest = new Date(now.getTime()+SizeUtil.DAYS_IN_MS*100L);
      assertThat(DateUtil.max(latest, now), equalTo(latest));
   }

   @Test
   public void testMinWithNull() {
      assertThat(DateUtil.min((Date[])null), nullValue());
   }

   @Test
   public void testMinWithListOfNulls() {
      assertThat(DateUtil.min(new Date[5]), nullValue());
   }

   @Test
   public void testMinWithSingleDate() {
      Date now = DateUtil.now();
      assertThat(DateUtil.min(now), equalTo(now));
   }

   @Test
   public void testMin() {
      Date now = DateUtil.now();
      Date latest = new Date(now.getTime()+SizeUtil.DAYS_IN_MS*100L);
      assertThat(DateUtil.min(latest, now), equalTo(now));
   }
   
   @Test
   public void testDateRangesOverlapWithAllNulls() {
      assertThat(DateUtil.dateRangesOverlap(null, null, null, null), is(true));
   }

   @Test
   public void testDateRangesOverlapWithAllNullFirstRange() {
      assertThat(DateUtil.dateRangesOverlap(null, null, DateUtil.today(), DateUtil.now()), is(true));
   }

   @Test
   public void testDateRangesOverlapWithAllNullSecondRange() {
      assertThat(DateUtil.dateRangesOverlap(DateUtil.today(), DateUtil.now(), null, null), is(true));
   }

   @Test
   public void testDateRangesOverlapWhenRange1LiesWithinRange2() {
      assertThat(DateUtil.dateRangesOverlap(DateUtil.parseIso8601Datetime("2011-01-02T00:00:00"), DateUtil.parseIso8601Datetime("2011-02-01T00:00:00"), DateUtil.parseIso8601Datetime("2011-01-01T00:00:00"), DateUtil.parseIso8601Datetime("2011-02-02T00:00:00")), is(true));
   }

   @Test
   public void testDateRangesOverlapWhenRange1LiesWithinRange2OnLHS() {
      assertThat(DateUtil.dateRangesOverlap(DateUtil.parseIso8601Datetime("2011-01-01T00:00:00"), DateUtil.parseIso8601Datetime("2011-02-01T00:00:00"), DateUtil.parseIso8601Datetime("2011-01-01T00:00:00"), DateUtil.parseIso8601Datetime("2011-02-02T00:00:00")), is(true));
   }

   @Test
   public void testDateRangesOverlapWhenRange1LiesWithinRange2OnRHS() {
      assertThat(DateUtil.dateRangesOverlap(DateUtil.parseIso8601Datetime("2011-01-02T00:00:00"), DateUtil.parseIso8601Datetime("2011-02-02T00:00:00"), DateUtil.parseIso8601Datetime("2011-01-01T00:00:00"), DateUtil.parseIso8601Datetime("2011-02-02T00:00:00")), is(true));
   }

   @Test
   public void testDateRangesOverlapWhenRange2LiesWithinRange1() {
      assertThat(DateUtil.dateRangesOverlap(DateUtil.parseIso8601Datetime("2011-01-01T00:00:00"), DateUtil.parseIso8601Datetime("2011-02-02T00:00:00"), DateUtil.parseIso8601Datetime("2011-01-02T00:00:00"), DateUtil.parseIso8601Datetime("2011-02-01T00:00:00")), is(true));
   }

   @Test
   public void testDateRangesOverlapWhenRange2LiesWithinRange1OnLHS() {
      assertThat(DateUtil.dateRangesOverlap(DateUtil.parseIso8601Datetime("2011-01-01T00:00:00"), DateUtil.parseIso8601Datetime("2011-02-02T00:00:00"), DateUtil.parseIso8601Datetime("2011-01-01T00:00:00"), DateUtil.parseIso8601Datetime("2011-02-01T00:00:00")), is(true));
   }

   @Test
   public void testDateRangesOverlapWhenRange2LiesWithinRange1OnRHS() {
      assertThat(DateUtil.dateRangesOverlap(DateUtil.parseIso8601Datetime("2011-01-01T00:00:00"), DateUtil.parseIso8601Datetime("2011-02-02T00:00:00"), DateUtil.parseIso8601Datetime("2011-01-02T00:00:00"), DateUtil.parseIso8601Datetime("2011-02-02T00:00:00")), is(true));
   }

   @Test
   public void testDateRangeOverlapWhenRange1UpperLiesWithinRange2() {
      assertThat(DateUtil.dateRangesOverlap(DateUtil.parseIso8601Datetime("2011-01-01T00:00:00"), DateUtil.parseIso8601Datetime("2011-02-02T00:00:00"), DateUtil.parseIso8601Datetime("2011-01-02T00:00:00"), DateUtil.parseIso8601Datetime("2011-02-10T00:00:00")), is(true));
   }

   @Test
   public void testDateRangeOverlapWhenRange1LowerLiesWithinRange2() {
      assertThat(DateUtil.dateRangesOverlap(DateUtil.parseIso8601Datetime("2011-02-08T00:00:00"), DateUtil.parseIso8601Datetime("2011-04-30T00:00:00"), DateUtil.parseIso8601Datetime("2011-01-02T00:00:00"), DateUtil.parseIso8601Datetime("2011-02-10T00:00:00")), is(true));
   }
   
   @Test
   public void testParseIso8601DateWithNull() {
      assertThat(DateUtil.parseIso8601Datetime(null), nullValue());
   }
   
   @Test
   public void testNewUtcCalendar() {
      assertThat(DateUtil.newUtcCalendar().getTimeZone(), equalTo(TimeZone.getTimeZone("UTC")));
   }

   @Test
   public void testNewUtcCalendarWithNullDate() throws InterruptedException {
      assertThat(DateUtil.newUtcCalendar(null), nullValue());
   }

   @Test
   public void testNewUtcCalendarWithDate() throws InterruptedException {
      Date then = DateUtil.now();
      Thread.sleep(10);
      assertThat(DateUtil.newUtcCalendar(then).getTime(), equalTo(then));
   }
}
