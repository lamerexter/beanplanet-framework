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

import org.beanplanet.core.lang.TypeUtil;

/**
 * An exception thrown when a type conversion is unsupported.
 * 
 * @author Gary Watson
 */
public class UnsupportedTypeConversionException extends TypeConversionException {
   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   /**
    * Creates a new <code>UnsupportedTypeConversionException</code> with no initial detail message or root cause.
    */
   public UnsupportedTypeConversionException() {
   }

   /**
    * Creates a new <code>UnsupportedTypeConversionException</code> with the specified detail message.
    * 
    * @param message the detail message.
    */
   public UnsupportedTypeConversionException(String message) {
      super(message);
   }

   /**
    * Creates a <code>UnsupportedTypeConversionException</code> with the specified detail message and root cause.
    * 
    * @param message the detail message
    * @param cause the nested root cause of the exception.
    */
   public UnsupportedTypeConversionException(String message, Throwable cause) {
      super(message, cause);
   }

   /**
    * Creates a <code>UnsupportedTypeConversionException</code> with the specified root cause, value object and target
    * conversion types.
    * 
    * @param cause the nested root cause of the exception.
    * @param value the value that was to be converted
    * @param targetType the target type of the conversion
    */
   public UnsupportedTypeConversionException(Throwable cause, Object value, Class<?> targetType) {
      super("Unable to convert the specified value to the required type [valueType="
            + (value == null ? null : value.getClass().getName()) + ", value=" + value + ", targetType="
            + (targetType == null ? null : targetType.getName()) + "]: ", cause);
   }

   /**
    * Creates a <code>UnsupportedTypeConversionException</code> with the specified value object and target conversion
    * types.
    * 
    * @param value the value that was to be converted
    * @param targetType the target type of the conversion
    */
   public UnsupportedTypeConversionException(Object value, Class<?> targetType) {
      super("Unable to convert the specified value to the required type [valueType="
            + (value == null ? null : value.getClass().getName()) + ", value=" + value + ", targetType="
            + (targetType == null ? null : TypeUtil.getDisplayNameForType(targetType)) + "]: ");
   }
}
