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

package org.beanplanet.core.util;

import org.beanplanet.core.lang.Assert;
import org.beanplanet.core.lang.TypeUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

/**
 * An exception related utility class.
 *
 * @author Gary Watson
 */
public class ExceptionUtil {
    /**
     * Convenience method which returns a formatted stack trace from the
     * specified exception.
     *
     * @param th
     *           the exception from which the trace is to be taken.
     * @return the formatted, multi-line, stack trace.
     */
    public static String printStackTrace(Throwable th) {
        StringWriter sw = new StringWriter();
        th.printStackTrace(new PrintWriter(sw));

        return sw.toString();
    }

    /**
     * Determines whether the exception hierarchy contains the specified class of
     * exception by inspecting the given exeption and root cause (nested)
     * exceptions.
     *
     * @param th
     * @param fromException
     * @return
     */
    public static <E extends Throwable> boolean exceptionHierarchyContains(Class<E> exceptionClass, Throwable fromException) {
        Assert.notNull(exceptionClass, "The exception class may not be null");
        Assert.notNull(fromException, "The from exception may not be null");
        while (fromException != null) {
            if (exceptionClass.isAssignableFrom(fromException.getClass())) {
                return true;
            }
            if (InvocationTargetException.class == fromException.getClass()) {
                return (fromException.getCause() == null ? false : exceptionHierarchyContains(exceptionClass, fromException.getCause()))
                    || exceptionHierarchyContains(exceptionClass, ((InvocationTargetException) fromException).getTargetException());
            }
            fromException = fromException.getCause();
        }
        return false;
    }

    /**
     * Convenience method to unwind possible target or root cause exceptions from
     * the specified <code>InvocationTargetException</code> and re-throw them if
     * they are runtime exceptions or wrap them in the specified runtime
     * exception otherwise.
     *
     * @param fallbackExceptionClass
     *           the fallback runtime exception to wrap the specified invocation
     *           target exception's target exception or cause exception if they
     *           are not runtime exceptions.
     * @param fallbackMessage
     *           a fallback message to add to the wrapping runtime exception.
     * @param itEx
     *           the invocation target exception whose target or cause exception
     *           are to be re-thrown directly, if runtime exceptions, or are to
     *           be wrapped and re-thrown otherwise.
     * @return a runtime exception which will either be the target or root cause
     *         of the invocation target exception, if they are runtime
     *         exceptions, or the target exception, the root cause exception or
     *         the invocation target exception wrapped in the specified runtime
     *         exception otherwise.
     */
    public static RuntimeException unwindAndRethrowRuntimeException(Class<? extends RuntimeException> fallbackExceptionClass, String fallbackMessage,
                                                                    InvocationTargetException itEx) {
        if (itEx.getTargetException() instanceof RuntimeException) {
            return (RuntimeException) itEx.getTargetException();
        }

        if (itEx.getCause() instanceof RuntimeException) {
            return (RuntimeException) itEx.getCause();
        }

        Throwable cause = (itEx.getTargetException() != null ? itEx.getTargetException() : itEx.getCause());
        if (cause != null && TypeUtil.getCallableConstructor(fallbackExceptionClass, new Object[] { fallbackMessage, cause }) != null) {
            return TypeUtil.instantiateClass(fallbackExceptionClass, new Object[] { fallbackMessage, cause });
        } else {
            return TypeUtil.instantiateClass(fallbackExceptionClass, (fallbackMessage == null ? new Object[] {} : new Object[] { fallbackMessage }));
        }
    }
}

