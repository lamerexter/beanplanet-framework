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
