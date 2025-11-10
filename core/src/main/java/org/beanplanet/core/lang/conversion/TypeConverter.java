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

package org.beanplanet.core.lang.conversion;


/**
 *  A type converter strategy.
 *  
 * @author Gary Watson
 * @since 12th December, 2004
 */
public interface TypeConverter {
   /**
    * Convert a given value to the specified target type.
    * 
    * @param value the value to be converted, which may be null.
    * @param targetType the type to convert the value to, which may not be null.
    * 
    * @return an object of the specified target type, converted from the source value. The <code>value</code> will be returned unmodified
    * if it is already an instance of the specified <code>targetType</code>. A <code>null</code> will be returned if the the <code>value</code>
    * was <code>null</code> or if <code>value</code> was converted to <code>null</code>.
    * @exception UnsupportedTypeConversionException if this converter cannot convert the value.
    */
   public <T> T convert(Object value, Class<T> targetType) throws UnsupportedTypeConversionException;
}
