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
