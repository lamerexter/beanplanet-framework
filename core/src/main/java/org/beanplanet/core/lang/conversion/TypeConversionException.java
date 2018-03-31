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
