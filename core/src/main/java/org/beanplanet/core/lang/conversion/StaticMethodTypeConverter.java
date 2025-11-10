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
