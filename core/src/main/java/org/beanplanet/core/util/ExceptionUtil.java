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

