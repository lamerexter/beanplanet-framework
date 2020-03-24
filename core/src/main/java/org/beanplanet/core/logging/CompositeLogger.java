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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Logs its messages to zero or more configured logger end-points.  This implementation implements the
 * <a href="http://en.wikipedia.org/wiki/Composite_pattern">Composite Design Pattern</a> and provides a useful way to direct logged messages to
 * any number of other logger instances.
 * 
 * <p>This logger is thread safe only if the target loggers configured on it are each thread safe and, if required because of
 * structural changes on the list of configured loggers after initialisation of this instance, the list of target loggers is required
 * to change during operation.</p>
 * 
 * @author Gary Watson
 * @since 2001
 */
public class CompositeLogger implements Logger {
   /** The loggers that will be passed messages logged to this instance. */
   protected List<Logger> loggers;

   /**
    * Constructs a <code>CompositeLogger</code> with no initial target loggers.
    */
   public CompositeLogger() {
      this(Collections.synchronizedList(new ArrayList<Logger>()));
   }

   /**
    * Constructs a <code>CompositeLogger</code> with the specified target loggers.
    * 
    * @param targetLoggers the list of loggers this logger will forward logged messages to.
    */
   public CompositeLogger(List<Logger> targetLoggers) {
      setLoggers(targetLoggers);
   }

   /**
    * Returns the target loggers configured for this logger instance.
    * 
    * @return the list of loggers this logger is forwarding logged messages to.
    */
   public List<Logger> getLoggers() {
      return loggers;
   }

   /**
    * Sets the target loggers configured for this logger instance.
    * 
    * @param targetLoggers the list of loggers this logger is to forward logged messages to.
    */
   public void setLoggers(List<Logger> targetLoggers) {
      this.loggers = targetLoggers;
   }

   /**
    * Adds the specified logger to the target loggers already configured for this logger instance.
    * 
    * @param logger the logger to add to the list of loggers this logger is forwarding logged messages to.
    */
   public void addLogger(Logger logger) {
      loggers.add(logger);
   }

   /**
    * Removes the specified logger from the target loggers configured for this logger instance.
    * 
    * @param logger the logger to remove from the list of loggers this logger is forwarding logged messages to.
    */
   public void removeLogger(Logger logger) {
      loggers.remove(logger);
   }

   /**
    * Logs a {@link Severity#DEBUG} message with the given parameters
    *
    * @param severity the sevrity of the message to log (see {@link Severity}).
    * @param cause a cause associated with the message to log, which may be null if there is none.
    * @param message a message to log.
    * @param args arguments to the log message.
    */
   public void log(Severity severity, Throwable cause, String message, Object ... args) {
      if (loggers == null) return;

      synchronized (loggers) {
         for (Logger logger : loggers) {
            logger.log(severity, cause, message, args);
         }
      }
   }
}
