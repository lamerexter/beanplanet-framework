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
