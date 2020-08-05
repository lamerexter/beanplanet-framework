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

import org.beanplanet.core.lang.Assert;
import org.beanplanet.core.lang.TypeUtil;
import org.beanplanet.core.util.PropertyBasedToStringBuilder;

import java.lang.reflect.Method;

import static org.beanplanet.core.lang.TypeUtil.invokeMethod;

/**
 * Implementation of a type converter that is pinned to a static method of a class.
 * 
 * <p>
 * Static type converter methods must have the signature
 * <code>public static <em>&lt;to-type&gt;</em> <em>&lt;methodName&gt;</em>(<em>&lt;from-type&gt;</em>)</code>. For
 * example, the following are all valid static type converter methods:
 * 
 * <pre>
 * public static int toInteger(String str) { ... }   // Might convert a string to an integer
 * public static Integer toInteger(String str) { ... }   // Might convert a string to an integer wrapper
 * public static Object[] toArray(Collection col) { ... }   // Might convert a collection to an array
 * public static YourType toArray(MyType myTypeValue) { ... }   // Might convert objects of MyType to YourType
 * </pre>
 * 
 * Naturally, interfaces may be specified for the <em>from</em> and <em>to</em> types.
 * </p>
 * 
 * <p>
 * This type converter implementation is not usually used directly by clients but is primarily used during
 * auto-discovery of type converters. See <code>{@link PackageScanTypeConverterLoader}</code> for further information
 * about how to make your type converters available at runtime.
 * </p>
 * 
 * @author Gary Watson
 * 
 */
public class StaticMethodTypeConverter implements TypeConverter {
   private final Method typeConverterMethod;
   private final boolean arrayConversion;

   /**
    * Creates a new static type converter method wrapper with the specified method.
    * 
    * @param typeConverterMethod the type converter method.
    */
   public StaticMethodTypeConverter(final Method typeConverterMethod) {
      this.typeConverterMethod = typeConverterMethod;
      arrayConversion = typeConverterMethod.getParameterCount() == 2 && typeConverterMethod.getParameterTypes()[1] == Class.class;
   }

   /**
    * Returns the type converter method used in this converter.
    * 
    * @return the type converter method used by this converter to convert the source type to the target type.
    */
   public Method getTypeConverterMethod() {
      return typeConverterMethod;
   }

   @SuppressWarnings("unchecked")
   public <T> T convert(final Object value, final Class<T> targetType) throws UnsupportedTypeConversionException {
      Assert.notNull(typeConverterMethod, "The type converter method may not be null");
      return arrayConversion ? (T) invokeMethod(null, typeConverterMethod, value, targetType)
                             : (T) invokeMethod(null, typeConverterMethod, value);
   }

   /**
    * Returns a useful string representation of this converter.
    * 
    * @return a useful report of the state of this converter
    */
   @Override
   public String toString() {
      return new PropertyBasedToStringBuilder(this).build();
   }
}
