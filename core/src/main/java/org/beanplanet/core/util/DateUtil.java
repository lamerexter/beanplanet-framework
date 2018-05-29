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
package org.beanplanet.core.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * A String utility class.
 * <p>
 * Provides some useful utility methods for string manipulation, otherwise not provided by the JDK.
 * </p>
 * 
 * @author Gary Watson
 */
public class DateUtil {
   private static final String UTC = "UTC";
   /** The simple date format for an ISO date and time. */
   public static final String ISO8601_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
   /** The simple date format for an ISO date and time. */
   public static final String ISO8601_DATETIME_FORMAT_WITH_MILLIS = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
   
   /** The earliest possible date. */
   public static final Date MIN_DATE = new Date(0L);
   /** The latest possible date. */
   public static final Date MAX_DATE = new Date(Long.MAX_VALUE);
   
   /**
    * Returns the current date.
    * 
    * @return the current date and time.
    * @see Date#Date()
    */
   public static final Date now() {
      return new Date();
   }
   
   /**
    * Returns todays date, truncated to 12am.
    * 
    * @return the date
    * @see
    */
   public static final Date today() {
      return trunc(new Date());
   }

   /**
    * Returns the given date, truncated to 12am. Effectively the same date
    * without the time element.
    * 
    * @param date
    *           the date
    * @return the date
    * @see
    */
   public static final Date trunc(Date date) {
      if (date == null) {
         return null;
      }
      
      // Truncate the millis time to the nearest day
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      cal.set(Calendar.HOUR, 0);
      cal.set(Calendar.MINUTE, 0);
      cal.set(Calendar.SECOND, 0);
      cal.set(Calendar.MILLISECOND, 0);
      cal.set(Calendar.AM_PM, Calendar.AM);
      
      return cal.getTime();
   }
   
   /**
    * Parses the given string into a date using the simple date format
    * specification supplied.
    * 
    * @param dateString
    *           the string representation of a date to be parsed.
    * @param format
    *           the expected format of the string date.
    * @return the date, parsed from the given string representation or null if
    *         the given string date was null.
    * @throws IllegalArgumentException if the format specified is invalid or null.
    * @throws ParseException if the given date string cannot be parsed using the format specified.
    */
   public static Date parse(String dateString, String format) throws IllegalArgumentException, org.beanplanet.core.lang.ParseException {
      if (dateString == null) {
         return null;
      }
      if (format == null) {
         throw new IllegalArgumentException("The date format may not be null");
      }
      
      try {
         DateFormat df = new SimpleDateFormat(format);
         df.setTimeZone(TimeZone.getTimeZone(UTC));
         return df.parse(dateString);
      } catch(ParseException pEx) {
         throw new org.beanplanet.core.lang.ParseException("Unable to parse the given date string ["+dateString+"] using the format specified ["+format+"]", pEx);
      }
   }
   
   /**
    * Parses the given string into a date using the simple date format
    * specification(s) supplied.
    * 
    * @param dateString
    *           the string representation of a date to be parsed.
    * @param formats
    *           the expected format of the string date.
    * @return the date, parsed from the given string representation(s) or null if
    *         the given string date was null.
    * @throws IllegalArgumentException if the format(s) specified are invalid or null.
    * @throws ParseException if the given date string cannot be parsed using any of the format(s) specified.
    */
   public static Date parse(String dateString, String... formats) throws IllegalArgumentException, org.beanplanet.core.lang.ParseException {
      if (dateString == null) {
         return null;
      }
      if (formats == null) {
         throw new IllegalArgumentException("The date formats may not be null");
      }
      
      for (String format : formats) {
         try {
            return parse(dateString, format);
         } catch (org.beanplanet.core.lang.ParseException pEx) {
            // Handled below...
         }
      }
      
      throw new org.beanplanet.core.lang.ParseException("Unable to parse the given date string ["+dateString+"] using any of the formats specified ["+StringUtil.asDelimitedString(formats, ",")+"]");
   }

   /**
    * Parses the given string into a date using the simple date format
    * specification(s) supplied.
    * 
    * @param dateString
    *           the string representation of a date to be parsed.
    * @param formats
    *           the expected format of the string date.
    * @return the date, parsed from the given string representation(s) or null if
    *         the given string date was null.
    * @throws IllegalArgumentException if the format(s) specified are invalid or null.
    * @throws ParseException if the given date string cannot be parsed using any of the format(s) specified.
    */
//   public static Date parse(String dateString, List<String> formats) throws IllegalArgumentException, org.beanplanet.core.lang.ParseException {
//      if (dateString == null) {
//         return null;
//      }
//      if (formats == null) {
//         throw new IllegalArgumentException("The date formats may not be null");
//      }
//
//      for (String format : formats) {
//         try {
//            return parse(dateString, format);
//         } catch (org.beanplanet.core.lang.ParseException pEx) {
//            // Handled below...
//         }
//      }
//
//      throw new org.beanplanet.core.lang.ParseException("Unable to parse the given date string ["+dateString+"] using any of the formats specified ["+StringUtil.collectionToCSVString(formats)+"]");
//   }

   /**
    * Parses an <a href="http://www.iso.org/iso/date_and_time_format">ISO 8601 date and time specification</a>, of the form YYYY-MM-DDThh:mm:ss.SSSS+dd:ddZ.
    * 
    * @param dateString the string representation of an ISO 8601 date to be parsed, which may be null.
    * @return the ISO 8601 date string, parsed into a date, or null if the date string was null.
    */
   public static Date parseIso8601Datetime(String dateString) {
      // SimpleDateFormat uses GMT [-+]hh:mm for the timezone which differs
      // from the ISO 8601 Z specification.
//      SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssz" );
      if (dateString == null) {
         return null;
      }
      
      //this is zero time so we need to add that TZ indicator for 
      StringBuilder tmpDate = new StringBuilder();
      if ( dateString.endsWith( "Z" ) ) {
         tmpDate.append(dateString.substring( 0, dateString.length() - 1)).append("GMT-00:00");
      } else {
         tmpDate.append(dateString.substring( 0, dateString.length() - 6));
         tmpDate.append("GMT");
         tmpDate.append(dateString.substring( dateString.length() - 6, dateString.length() ));
      }
      
      try {
         return parse(tmpDate.toString(), "yyyy-MM-dd'T'HH:mm:ssz", "yyyy-MM-dd'T'HH:mm:ss.SSSz");
      } catch (org.beanplanet.core.lang.ParseException pEx) {
      }

      // Lenient (shorter) date formats intentionally left to the end
      try {
         return parse(dateString, "yyyy-MM-dd'T'HH:mm:ss.SSS", "yyyy-MM-dd'T'HH:mm:ss");
      } catch (org.beanplanet.core.lang.ParseException pEx) {
         // Handled below...
      }
      
      throw new org.beanplanet.core.lang.ParseException("Unable to parse the given ISO 8601 date string ["+dateString+"]: ");
}
   
   /**
    * Formats a date to a string using the format specified.
    * 
    * @param date the date to be formatted.
    * @param format the format specification of the required string, as specified by <code>{@link SimpleDateFormat}</code>.
    * @return a string representing the given date in the required format, or null if the date was null.
    * @throws IllegalArgumentException if the format specified is invalid or null.
    */
   public static String toString(Date date, String format) {
      if (date == null) {
         return null;
      }
      if (format == null) {
         throw new IllegalArgumentException("The date format may not be null");
      }
         
      SimpleDateFormat df = new SimpleDateFormat(format);
      df.setTimeZone(TimeZone.getTimeZone(UTC));
      return df.format(date);
   }

   /**
    * Formats a date to a string using the ISO 8601 date format.
    * 
    * @param date the date to be formatted.
    * @return a string representing the given date in ISO 8601 format, or null if the date was null.
    */
   public static String toIso8601String(Date date) {
      return toString(date, ISO8601_DATETIME_FORMAT);
   }

   /**
    * Formats the current date to a string using the ISO 8601 date format.
    *
    * @return a string representing the current date in ISO 8601 format.
    */
   public static String nowToIso8601String() {
      return toString(now(), ISO8601_DATETIME_FORMAT);
   }

   /**
    * Formats a date to a string using the ISO 8601 date format, including milliseconds.
    * 
    * @param date the date to be formatted.
    * @return a string representing the given date in ISO 8601 format, or null if the date was null.
    * @throws IllegalArgumentException if the format specified is invalid or null.
    */
   public static String toIso8601WithMillisString(Date date) {
      return toString(date, ISO8601_DATETIME_FORMAT_WITH_MILLIS);
   }

   /**
    * Formats the current date to a string using the ISO 8601 date format, including milliseconds.
    *
    * @return a string representing the given date in ISO 8601 format, or null if the date was null.
    */
   public static String nowToIso8601WithMillisString() {
      return toString(now(), ISO8601_DATETIME_FORMAT_WITH_MILLIS);
   }

   /**
    * Given a model date returns a new date that is the end of the same day (11:59:59.999).
    * 
    * @param date the date whose end of day is to be determined.
    * @return a new date adjusted to the last millisecond of the same day as the given date, or null if the date was null.
    */
   public static Date toEndOfDay(Date date) {
      if (date == null) {
         return null;
      }
      
      // Truncate the millis time to the nearest day
      Calendar cal = Calendar.getInstance();
      cal.setTimeZone(TimeZone.getTimeZone(UTC));
      cal.setTime(date);
      cal.set(Calendar.HOUR_OF_DAY, 23);
      cal.set(Calendar.MINUTE, 59);
      cal.set(Calendar.SECOND, 59);
      cal.set(Calendar.MILLISECOND, 999);
      
      return cal.getTime();
   }

   /**
    * Given a model date returns a new date that is the end of the same calendar month (e.g. 2010-01-31, 2004-02-29).
    * 
    * @param date the date whose end of month is to be determined.
    * @return a new date adjusted to the last millisecond of the same calendar month of the given date, or null if the date was null.
    */
   public static Date toEndOfDayAtEndOfCalendarMonth(Date date) {
      if (date == null) {
         return null;
      }
      
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      
      int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
      cal.set(Calendar.DAY_OF_MONTH, maxDay);
      cal.set(Calendar.HOUR_OF_DAY, 23);
      cal.set(Calendar.MINUTE, 59);
      cal.set(Calendar.SECOND, 59);
      cal.set(Calendar.MILLISECOND, 999);
      
      return cal.getTime();
   }
   
   /**
    * Returns the latest (most recent) date from a list of dates.
    * 
    * @param dates the list of dates whose maximum is to be determined.
    * 
    * @return the most recent date from the list or null if the list was null or contains all nulls.
    */
   public static Date max(Date ...dates) {
      if (dates == null) {
         return null;
      }
      
      Date maxDate = null;
      for (Date date : dates) {
         if (maxDate == null || date.after(maxDate)) {
            maxDate = date;
         }
      }
      return maxDate;
   }

   /**
    * Returns the earliest (oldest) date from a list of dates.
    * 
    * @param dates the list of dates whose minimum is to be determined.
    * 
    * @return the oldest date from the list or null if the list was null or contains all nulls.
    */
   public static Date min(Date ...dates) {
      if (dates == null) {
         return null;
      }
      
      Date minDate = null;
      for (Date date : dates) {
         if (minDate == null || date.before(minDate)) {
            minDate = date;
         }
      }
      return minDate;
   }
   
   /**
    * Determines if two date ranges overlap. Any of the dates may be null, indicating an open date range. 
    * 
    * @param date1Lower the first range lower date, inclusive. May be null indicating the first date lower bound is open ended.
    * @param date1Upper the first range upper date, inclusive. May be null indicating the first date upper bound is open at this end.
    * @param date2Lower the second range lower date, inclusive. May be null indicating the second date lower bound is open ended.
    * @param date2Upper the second range upper date, inclusive. May be null indicating the second date upper bound is open at this end.
    * @return true if the date ranges overlap, false otherwise.
    */
   public static boolean dateRangesOverlap(Date date1Lower, Date date1Upper, Date date2Lower, Date date2Upper) {
      if (date1Lower == null) {
         date1Lower = MIN_DATE;
      }
      if (date2Lower == null) {
         date2Lower = MIN_DATE;
      }
      if (date1Upper == null) {
         date1Upper = MAX_DATE;
      }
      if (date2Upper == null) {
         date2Upper = MAX_DATE;
      }

      return (date1Lower.compareTo(date2Lower) >= 0 && date1Lower.compareTo(date2Upper) <=0)
            || (date1Upper.compareTo(date2Lower) >= 0 && date1Lower.compareTo(date2Upper) <=0);
   }
   
   /**
    * Returns a new calendar instance in the UTC time zone.
    * 
    * @return a new calendar, configured in the UTC time zone.
    */
   public static Calendar newUtcCalendar() {
      return Calendar.getInstance(TimeZone.getTimeZone(UTC));
   }

   /**
    * Returns a new calendar instance in the UTC time zone, set to the given time.
    * 
    * @param date the date the calendar is to be set to, which may be null.
    * @return a new calendar, configured in the UTC time zone or null if the specified date/time was null.
    */
   public static Calendar newUtcCalendar(Date date) {
      if (date == null) {
         return null;
      }
      Calendar cal = newUtcCalendar();
      cal.setTime(date);
      return cal;
   }
}
