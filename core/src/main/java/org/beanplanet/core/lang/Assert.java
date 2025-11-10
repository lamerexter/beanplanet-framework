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
