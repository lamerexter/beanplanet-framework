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

import org.beanplanet.core.io.IoUtil;
import org.beanplanet.core.models.lifecycle.ShutdownLifecycle;
import org.beanplanet.core.util.DateUtil;
import org.beanplanet.core.util.ExceptionUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;


/**
 * A useful logger implementation which simply writes its logged messages to an <code>OutputStream</code>. The
 * <code>toString()</code> method on each logged message will be invoked first and the result is what is written to the
 * output stream. This implementation assumes that the <code>toString()</code> method of each logged message provides
 * sufficient information.
 *
 * <p>
 * The flushing behaviour (after each logged message and during shutdown) is configurable on this logger. Flushing the
 * output stream has the benefit of ensuring logged messages are written-out in a timely and secure manner while
 * resulting in slightly degraded system performance during the flush I/O operation.
 * </p>
 */
public class OutputStreamLogger implements Logger, ShutdownLifecycle {
    /**
     * The <code>OutputStream</code> this logger will write it messages to.
     */
    protected OutputStream outputStream;

    /**
     * Whether to flush the output stream for after each logged message is written-out; defaults to false.
     */
    protected boolean flushAfterWritingEachMessage = false;

    /**
     * Whether to flush the output stream during system shutdown (invocation of the
     * <code>{@link OutputStreamLogger#shutdown()}</code> method); defaults to true.
     */
    protected boolean flushOnShutdown = true;

    /**
     * Creates an <code>OutputStreamLogger</code> initially configured to log its messages to standard out / the system
     * console (<code>System.out</code>).
     */
    public OutputStreamLogger() {
        this(System.out);
    }

    /**
     * Creates an <code>OutputStreamLogger</code> configured with the specified output stream where all logged messages
     * will be written.
     */
    public OutputStreamLogger(OutputStream os) {
        setOutputStream(os);
    }

    /**
     * Returns the output stream where all logged messages through this logger are being written.
     *
     * @return the output stream configured on this logger where all logged messages will be written.
     */
    public OutputStream getOutputStream() {
        return outputStream;
    }

    /**
     * Sets the output stream where all logged messages through this logger are being written.
     *
     * @param outputStream the output stream to configure on this logger where all logged messages will be written.
     */
    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * Returns whether to flush the output stream for after each logged message is written-out.
     *
     * @return true if the output stream is flushed after each logged message is written, false otherwise.
     */
    public boolean getFlushAfterWritingEachMessage() {
        return flushAfterWritingEachMessage;
    }

    /**
     * Sets whether to flush the output stream for after each logged message is written-out. Flushing the output stream
     * has the benefit of ensuring logged messages are written-out in a timely and secure manner while resulting in
     * slightly degraded system performance during the flush I/O operation.
     *
     * @param flushAfterWritingEachMessage true if the output stream is to be flushed after each logged message is
     *                                     written.
     */
    public void setFlushAfterWritingEachMessage(boolean flushAfterWritingEachMessage) {
        this.flushAfterWritingEachMessage = flushAfterWritingEachMessage;
    }

    /**
     * Returns whether to flush the output stream during system shutdown (invocation of the
     * <code>{@link OutputStreamLogger#shutdown()}</code> method).
     *
     * @return true if the output stream is flushed on system shutdown, false otherwise.
     */
    public boolean getFlushOnShutdown() {
        return flushOnShutdown;
    }

    /**
     * Sets whether to flush the output stream during system shutdown (invocation of the
     * <code>{@link OutputStreamLogger#shutdown()}</code> method).
     *
     * @return flushOnShutdown true if the output stream is flushed on system shutdown.
     */
    public void setFlushOnShutdown(boolean flushOnShutdown) {
        this.flushOnShutdown = flushOnShutdown;
    }

    /**
     * Called to notify this logger it is to shut down. If configured, via the <code>flushOnShutdown/code>
     * property, this logger will flush the output stream.
     */
    public void shutdown() {
        if (getFlushOnShutdown()) {
            IoUtil.flushIgnoringErrors(outputStream);
        }
    }

    @Override
    public void log(Severity severity, Throwable cause, String message, Object... args) {
        if (message == null) {
            return;
        }

        if (outputStream == null) {
            throw new IllegalStateException("Unable to log output: no output stream has been configured.");
        }

        try {
            outputStream.write((DateUtil.nowToIso8601WithMillisString() + "--" + severity.name() + "--" + MessageFormat.format(message, args)).getBytes());
            outputStream.write("\n".getBytes());

            if (cause != null) {
                outputStream.write(ExceptionUtil.printStackTrace(cause).getBytes());
                outputStream.write("\n".getBytes());
            }

            if (getFlushAfterWritingEachMessage()) {
                IoUtil.flushIgnoringErrors(outputStream);
            }
        } catch (IOException ignoredEx) {
        }
    }

}
