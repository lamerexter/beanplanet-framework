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
