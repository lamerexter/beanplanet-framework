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

import java.text.DecimalFormat;

/**
 * A handy utility class that contains some well-used sizes and formatting
 * helpers.
 * 
 * @author Gary Watson
 */
public class SizeUtil
{
   /** The number of bytes in 1 kilobyte. */
   public static int KBYTES = 1024;

   /** The number of bytes in 1 megabyte. */
   public static int MEGA_BYTES = KBYTES * KBYTES;

   /** The number of bytes in 1 gigabyte. */
   public static long GIGA_BYTES = MEGA_BYTES * KBYTES;

   /** The number of bytes in 1 terabyte. */
   public static long TERA_BYTES = GIGA_BYTES * KBYTES;

   /** The number of milliseconds in 1 second. */
   public static int SECONDS_IN_MS = 1000;

   /** The number of milliseconds in 1 minute. */
   public static int MINUTES_IN_MS = 60 * SECONDS_IN_MS;

   /** The number of milliseconds in 1 hour. */
   public static int HOURS_IN_MS = 60 * MINUTES_IN_MS;

   /** The number of milliseconds in 1 day. */
   public static int DAYS_IN_MS = 24 * HOURS_IN_MS;

   /** The number of milliseconds in 1 week. */
   public static int WEEKS_IN_MS = 7 * DAYS_IN_MS;

   /** The number of milliseconds in 1 year, ignoring leap years. */
   public static int YEARS_IN_MS = 52 * WEEKS_IN_MS;
   
   /** The standard number of days in a year, ignoring leap years and daylight saving time. */
   public static int DAYS_IN_A_YEAR = 365;

   /** The standard number of days in a week (7). */
   public static int DAYS_IN_A_WEEK = 7;

   /** The maximum number of days in a month (31) */
   public static int MAX_DAYS_IN_MONTH = 31;
   
   /** The number of hours in a day, ignoring daylight saving time. */
   public static int HOURS_IN_A_DAY = 24;

   /**
    * Returns a short string description of the length, in bytes, specified.
    * 
    * @param sizeInBytes
    *           the number of bytes whose length if to be described.
    * @param formatSpec
    *           the <code>DecimalFormal</code> format specification.
    * @return a short-hand string description of the length in bytes.
    * @see java.text.DateFormat
    */
   public static String getBytesSpecificationDescription(double sizeInBytes, String formatSpec)
   {
      DecimalFormat df = new DecimalFormat(formatSpec);
      df.setDecimalSeparatorAlwaysShown(false);
      double teraBytes = sizeInBytes / TERA_BYTES;
      if (teraBytes >= 1.0d)
      {
         return df.format(teraBytes) + " TB";
      }

      double gigaBytes = sizeInBytes / GIGA_BYTES;
      if (gigaBytes >= 1.0d)
      {
         return df.format(gigaBytes) + " GB";
      }

      double megaBytes = sizeInBytes / MEGA_BYTES;
      if (megaBytes >= 1.0d)
      {
         return df.format(megaBytes) + " MB";
      }

      double kBytes = sizeInBytes / KBYTES;
      if (kBytes >= 1.0d)
      {
         return df.format(kBytes) + " KB";
      }

      return ((long) sizeInBytes) + " Bytes";
   }

   /**
    * Returns a short string description of the length, in bytes, specified.
    * 
    * @param sizeInBytes
    *           the number of bytes whose length if to be described.
    * @return a short-hand string description of the length in bytes.
    */
   public static String getBytesSpecificationDescription(double sizeInBytes)
   {
      return getBytesSpecificationDescription(sizeInBytes, "#.##");
   }

   /**
    * Returns a short string description of the elapsed time, in bytes,
    * specified.
    * 
    * @param timeInMS
    *           the number of bytes whose length if to be described.
    * @param formatSpec
    *           the <code>DecimalFormal</code> format specification.
    * @return a short-hand string description of the length in bytes.
    * @see java.text.DateFormat
    */
   public static String getElapsedTimeSpecificationDescription(long timeInMS, String formatSpec)
   {
      StringBuffer sBuf = new StringBuffer();
      long years = timeInMS / YEARS_IN_MS;
      if (years > 0)
      {
         sBuf.append(years).append(years > 1 ? " years" : " year");
      }

      long weeks = timeInMS % YEARS_IN_MS / WEEKS_IN_MS;
      if (weeks > 0)
      {
         sBuf.append(years > 0 ? ", " : " ").append(weeks).append(weeks > 1 ? " weeks" : " week");
      }

      long days = (timeInMS - (years * YEARS_IN_MS) - (weeks * WEEKS_IN_MS)) / DAYS_IN_MS;
      if (days > 0)
      {
         sBuf.append(years > 0 || weeks > 0 ? ", " : " ").append(days).append(days > 1 ? " days" : " day");
      }

      long hours = (timeInMS - (years * YEARS_IN_MS) - (weeks * WEEKS_IN_MS) - (days * DAYS_IN_MS)) / HOURS_IN_MS;
      if (hours > 0)
      {
         sBuf.append(years > 0 || weeks > 0 || days > 0 ? ", " : " ").append(hours).append(
                  hours > 1 ? " hours" : " hour");
      }

      long minutes = (timeInMS - (years * YEARS_IN_MS) - (weeks * WEEKS_IN_MS) - (days * DAYS_IN_MS) - (hours * HOURS_IN_MS))
               / MINUTES_IN_MS;
      if (minutes > 0)
      {
         sBuf.append(years > 0 || weeks > 0 || days > 0 || hours > 0 ? ", " : " ").append(minutes).append(
                  minutes > 1 ? " minutes" : " minute");
      }

      long secs = (timeInMS - (years * YEARS_IN_MS) - (weeks * WEEKS_IN_MS) - (days * DAYS_IN_MS)
               - (hours * HOURS_IN_MS) - (minutes * MINUTES_IN_MS))
               / SECONDS_IN_MS;
      if (secs > 0)
      {
         sBuf.append(years > 0 || weeks > 0 || days > 0 || hours > 0 || minutes > 0 ? ", " : " ").append(secs).append(
                  secs > 1 ? " secs" : " sec");
      }

      long ms = timeInMS - (years * YEARS_IN_MS) - (weeks * WEEKS_IN_MS) - (days * DAYS_IN_MS) - (hours * HOURS_IN_MS)
               - (minutes * MINUTES_IN_MS) - (secs * SECONDS_IN_MS);
      if (ms > 0)
      {
         sBuf.append(years > 0 || weeks > 0 || days > 0 || hours > 0 || minutes > 0 || secs > 0 ? ", " : " ")
                  .append(ms).append(" ms");
      }

      return sBuf.toString().trim();
   }

   /**
    * Returns a short string description of the length, in bytes, specified.
    * 
    * @param sizeInBytes
    *           the number of bytes whose length if to be described.
    * @return a short-hand string description of the length in bytes.
    */
   public static String getElapsedTimeSpecificationDescription(long sizeInBytes)
   {
      return getElapsedTimeSpecificationDescription(sizeInBytes, "#.##");
   }
}
