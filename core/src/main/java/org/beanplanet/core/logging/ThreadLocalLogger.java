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
package org.beanplanet.core.logging;

import static org.beanplanet.core.logging.NullLogger.NULL_LOGGER;

/**
 * A logger which can be configured to log its messages by thread.
 * 
 * @author Gary Watson
 * @since 21st January, 2000
 */
public class ThreadLocalLogger implements Logger {
   private static ThreadLocal<Logger> logger = new ThreadLocal<>();

   /**
    * Returns the logger bound to the current thread.
    * 
    * @return the logger for the current thread, which may be null.
    */
   public static Logger getThreadLogger() {
      return logger.get();
   }

   /**
    * Sets the logger for the current thread.
    * 
    * @param wrappedLogger the logger to assign to the current thread.
    */
   public static void setThreadLogger(Logger wrappedLogger) {
      logger.set(wrappedLogger);
   }

   /**
    * Clears any logger associated with the current thread.
    */
   public static void clearThreadLogger() {
      logger.set(NULL_LOGGER);
   }

   /**
    * Returns the logger bound to the current thread.
    * 
    * @return the logger for the current thread, which may be null.
    */
   public Logger getLogger() {
      return getThreadLogger();
   }

   /**
    * Sets the logger for the current thread.
    * 
    * @param wrappedLogger the logger to assign to the current thread.
    */
   public void setLogger(Logger wrappedLogger) {
      setThreadLogger(wrappedLogger);
   }

   /**
    * Clears any logger associated with the current thread.
    */
   public static void clearLogger() {
      clearThreadLogger();
   }
}
