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
package org.beanplanet.core.lang.conversion.system;

import org.beanplanet.core.lang.conversion.annotations.TypeConverter;

/**
 * A type converter which simply calls the <code>toString()</code> method on an object to convert its value to a string.
 *
 * @author Gary Watson
 */
@TypeConverter
public final class ObjectToStringTypeConverter {

    /**
     * Converts the specified value to a string, using the objects <code>toString()</code> method.
     *
     * @param value the value to be stringified.
     */
    @TypeConverter
    public static String toString(Object value) {
        return (value == null ? null : value.toString());
    }
}
