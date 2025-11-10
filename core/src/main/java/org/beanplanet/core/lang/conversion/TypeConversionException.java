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

import org.beanplanet.core.dao.DataAccessException;
import org.beanplanet.core.models.Value;

public class TypeConversionException extends DataAccessException {

   /**
    * 
    */
   private static final long serialVersionUID = 1L;

   /**
    * Creates a new <code>UELTypeConversionException</code> with no initial detail message or root cause.
    */
   public TypeConversionException() {
   }

   /**
    * Creates a new <code>UELTypeConversionException</code> with the specified detail message.
    * 
    * @param message the detail message.
    */
   public TypeConversionException(String message) {
      super(message);
   }

   /**
    * Creates a <code>UELTypeConversionException</code> with the specified detail message and root cause.
    * 
    * @param message the detail message
    * @param cause the nested root cause of the exception.
    */
   public TypeConversionException(String message, Throwable cause) {
      super(message, cause);
   }

   /**
    * Creates a new <code>TypeConversionException</code> based on the specified result and conversion type.
    * 
    * @param valueHolder the value on which the conversion was attempted
    * @param requiredType the result type required in the conversion
    */
   public TypeConversionException(Value value, Class<?> requiredType) {
      this(value, requiredType, null);
   }

   /**
    * Creates a new <code>TypeConversionException</code> based on the specified result and conversion type.
    * 
    * @param value the value on which the conversion was attempted
    * @param requiredType the result type required in the conversion
    * @param additionalMessage additional error message information to be appended onto the final error message; may be
    *        null.
    */
   public TypeConversionException(Value value, Class<?> requiredType, String additionalMessage) {
      this("Unable to convert the specified value [value=" + value.getValue() + ", value class="
           + (value.isNull() ? "null" : value.getType().getName()) + ", value object="
           + value + "] to the required type, " + (requiredType == null ? "null" : requiredType.getName())
           + (additionalMessage != null ? (": " + additionalMessage) : ""));
   }
}
