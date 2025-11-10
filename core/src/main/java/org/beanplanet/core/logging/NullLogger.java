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

/**
 * A logger that doesn't log anything - similar to directing output to /dev/null in UNIX.
 */
public class NullLogger implements Logger {
   public static final NullLogger NULL_LOGGER = new NullLogger();

   /**
    * Always answers false as absolutely no logging is performed through this logger.
    *
    * @param severity the severity of the log to check.
    * @return always returns false, indicating all logging is disabled through this logger.
    */
   @Override
   public boolean isSeverityEnabled(Severity severity) {
      return false;
   }

   /**
    * Always ignores all logged messages.
    *
    * @param severity the severty of this logged message to be ignored.
    * @param message a message to log.
    * @param args arguments to the log message.
    */
   @Override
   public void log(Severity severity, String message, Object... args) {
      // Ignore all messages ...
   }
}
