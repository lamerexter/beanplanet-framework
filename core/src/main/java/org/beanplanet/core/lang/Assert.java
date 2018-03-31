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

package org.beanplanet.core.lang;

import org.beanplanet.core.models.Builder;

/**
 * Assertion utility class, used to enforce certain conditions throughout the
 * framework. Particularly useful where the value or range of parameters needs
 * to be checked and where illegal values can't really be dealt with at the
 * point they are validated.
 * <p>
 * Assertions should be used with caution. Only assert conditions that cannot be
 * handled at the point of call because an asserted condition will throw an
 * error if the outcome is unexpected. For example, if checking the value or
 * range of a parameter it is preferable to handle an unexpected values via
 * conditional logic in the method call itself rather than throw an error by
 * asserting on the value or range.
 * </p>
 *
 * @author Chris Taylor
 * @author Gary Watson
 */
public class Assert
{
    public static void assertFalse(boolean condition)
    {
        if (condition)
        {
            throw new AssertionFailureException("Assertion failed: the assertion predicate answered false");
        }
    }

    public static void assertFalse(boolean condition, String message)
    {
        if (condition)
        {
            throw new AssertionFailureException(message);
        }
    }

    public static void assertNotNull(Object result)
    {
        if (result == null)
        {
            throw new AssertionFailureException("Assertion failed: the expression should be non-null");
        }
    }

    public static void assertNotNull(Object result, String message)
    {
        if (result == null)
        {
            throw new AssertionFailureException(message);
        }
    }

    public static void assertNull(Object result)
    {
        if (result != null)
        {
            throw new AssertionFailureException("Assertion failed: the expression should be null");
        }
    }

    public static void assertNull(Object result, String message)
    {
        if (result != null)
        {
            throw new AssertionFailureException(message);
        }
    }

    public static void assertTrue(boolean condition) {
        assertTrue(condition, (Builder<String>)null);
    }

    public static void assertTrue(boolean condition, String message)
    {
        assertTrue(condition, () -> message);
    }

    public static void assertTrue(boolean condition, Builder<String> messageBuilder)
    {
        if (!condition)
        {
            String message = messageBuilder != null ? messageBuilder.build() : "Assertion failed: the condition should be true";
            throw new AssertionFailureException(message);
        }
    }

    public static void isFalse(boolean condition)
    {
        assertFalse(condition);
    }

    public static void isFalse(boolean condition, String message)
    {
        assertFalse(condition, message);
    }

    public static void isNull(Object result)
    {
        assertNull(result);
    }

    public static void isNull(Object result, String message)
    {
        assertNull(result, message);
    }

    public static void isTrue(boolean condition)
    {
        assertTrue(condition);
    }

    public static void isTrue(boolean condition, String message)
    {
        assertTrue(condition, message);
    }

    public static void notNull(Object result)
    {
        assertNotNull(result);
    }

    public static void notNull(Object result, String message)
    {
        assertNotNull(result, message);
    }
}
