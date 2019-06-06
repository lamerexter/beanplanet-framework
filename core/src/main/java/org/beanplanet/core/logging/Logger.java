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

import static org.beanplanet.core.logging.BeanpanetLoggerFactory.getSystemloggerFor;
import static org.beanplanet.core.logging.Severity.*;

/**
 * A model of a logger that logs messages based on their severity.
 * <p>
 * This type of logger is the most popular logger type in most other logging APIs such as the JDK 1.4 logging API, SLFJ, Log4J
 * and the Jakarta Commons logging API.
 * <p>
 * The following descriptions of log message severities are for informational purposes only and do not reflect the
 * uses of a particular severity within an end-user system. Supported log message severities include:
 * <ul>
 * <li><b>Debug</b> - for application developers to log programmatic events or events that facilitate traceability and the
 * debugging of a program.
 * <li><b>Info</b> - informational messages that allow observers to understand the steps performed in a program's transaction or
 * work-flow.
 * <li><b>Warning</b> - for warning messages fired in the event the program's logic or validation steps performed have detected
 * an anomaly in the input data or instructions being processed. In this eventuality, the program was able to take steps
 * to prevent failure but such occurrences could and should be avoided in the future, if possible.
 * <li><b>Error</b> - fired in the event the program's logic or validation steps performed have detected a serious error in the
 * input data or instructions being processed. Perhaps the program has been instructed to do something it cannot do at
 * that time, the request was not understood or a logic error has been caught by the program. Such errors are reported
 * to the user agent performing the request in the current thread of execution and should not affect other users of the system including other threads, if any, or have little
 * effect on the stability of the system.
 * <li><b>Fatal</b> - fired only in the event a catastrophic error has been detected. Perhaps the program has not been
 * configured correctly, a critical component of the system has failed, the user has made a serious error or requested
 * the system to perform an action that has meant the system can no longer function and act on behalf of the user, other
 * current users or future users. Use of this severity of message is reserved for catastrophic errors that <em>should</em>
 * notify system administrators and appear on notice boards.
 * </ul>
 *
 */
public interface Logger {
    /**
    * Determines if this logger is currently configured to accept {@link Severity#DEBUG} messages.
    *
    * @return true if this logger accepts debugging messages, false otherwise.
    */
    default boolean isDebugEnabled() {
        return isSeverityEnabled(DEBUG);
    }

    /**
    * Determines if this logger is currently configured to accept {@link Severity#INFO} messages.
    *
    * @return true if this logger accepts informational messages, false otherwise.
    */
    default boolean isInfoEnabled() {
        return isSeverityEnabled(INFO);
    }

    /**
    * Determines if this logger is currently configured to accept {@link Severity#INFO} messages.
    *
    * @return true if this logger accepts warning messages, false otherwise.
    */
    default boolean isWarningEnabled() {
        return isSeverityEnabled(WARNING);
    }

    /**
    * Determines if this logger is currently configured to accept {@link Severity#ERROR} messages.
    *
    * @return true if this logger accepts error messages, false otherwise.
    */
    default boolean isErrorEnabled() {
        return isSeverityEnabled(ERROR);
    }

    /**
    * Determines if this logger is currently configured to accept {@link Severity#FATAL} error messages.
    *
    * @return true if this logger accepts fatal error messages, false otherwise.
    */
    default boolean isFatalEnabled() {
        return isSeverityEnabled(FATAL);
    }

    /**
     * Determines if this logger is currently configured to accept a specific {@link Severity} of logged messages.
     *
     * @param severity the severity of logged messages to check.
     * @return true if this logger accepts logged messages of the given severity, false otherwise. Defaults to true.
     */
    default boolean isSeverityEnabled(Severity severity) {
        return true;
    }

    /**
    * Logs a {@link Severity#DEBUG} message without parameters.
    *
    * @param message a message to log.
    * @see #log(Severity, String, Object...)
    */
    default void debug(String message) {
       log(DEBUG, message);
    }

    /**
    * Logs a {@link Severity#DEBUG} message with the given parameters
    *
    * @param message a message to log.
    * @param args arguments to the log message.
    */
    default void debug(String message, Object ... args) {
       log(DEBUG, message, args);
    }

    /**
     * Logs an {@link Severity#INFO} message without parameters.
     *
     * @param message a message to log.
     * @see #log(Severity, String, Object...)
     */
    default void info(String message) {
        log(INFO, message);
    }

    /**
     * Logs a {@link Severity#INFO} message with the given parameters
     *
     * @param message a message to log.
     * @param args arguments to the log message.
     */
    default void info(String message, Object ... args) {
        log(INFO, message, args);
    }

    /**
     * Logs an {@link Severity#WARNING} message without parameters.
     *
     * @param message a message to log.
     * @see #log(Severity, String, Object...)
     */
    default void warning(String message) {
        log(WARNING, message);
    }

    /**
     * Logs a {@link Severity#WARNING} message with the given parameters
     *
     * @param message a message to log.
     * @param args arguments to the log message.
     */
    default void warning(String message, Object ... args) {
        log(WARNING, message, args);
    }

    /**
     * Logs an {@link Severity#ERROR} message without parameters.
     *
     * @param message a message to log.
     * @see #log(Severity, String, Object...)
     */
    default void error(String message) {
        log(ERROR, message);
    }

    /**
     * Logs an {@link Severity#ERROR} message without parameters.
     *
     * @param cause a cause associated with the error message to log, which may be null if there is none.
     * @param message a message to log.
     * @see #log(Severity, String, Object...)
     */
    default void error(Throwable cause, String message) {
        log(ERROR, cause, message);
    }

    /**
     * Logs a {@link Severity#ERROR} message with the given parameters
     *
     * @param message a message to log.
     * @param args arguments to the log message.
     */
    default void error(String message, Object ... args) {
        log(ERROR, message, args);
    }

    /**
     * Logs a {@link Severity#ERROR} message with the given parameters
     *
     * @param cause a cause associated with the error message to log, which may be null if there is none.
     * @param message a message to log.
     * @param args arguments to the log message.
     */
    default void error(Throwable cause, String message, Object ... args) {
        log(ERROR, cause, message, args);
    }

    /**
     * Logs an {@link Severity#FATAL} message without parameters.
     *
     * @param message a message to log.
     * @see #log(Severity, String, Object...)
     */
    default void fatal(String message) {
        log(FATAL, message);
    }

    /**
     * Logs a {@link Severity#FATAL} message with the given parameters
     *
     * @param message a message to log.
     * @param args arguments to the log message.
     */
    default void fatal(String message, Object ... args) {
        log(FATAL, message, args);
    }

    /**
     * Logs a {@link Severity#DEBUG} message with the given parameters
     *
     * @param severity the sevrity of the message to log (see {@link Severity}).
     * @param message a message to log.
     * @param args arguments to the log message.
     */
    default void log(Severity severity, String message, Object ... args) {
        getLogger().log(severity, message, args);
    }

    /**
     * Logs a {@link Severity#DEBUG} message with the given parameters
     *
     * @param severity the sevrity of the message to log (see {@link Severity}).
     * @param cause a cause associated with the message to log, which may be null if there is none.
     * @param message a message to log.
     * @param args arguments to the log message.
     */
    default void log(Severity severity, Throwable cause, String message, Object ... args) {
        getLogger().log(severity, cause, message, args);
    }

    default Logger getLogger() {
        return getSystemloggerFor(this);
    }
}
