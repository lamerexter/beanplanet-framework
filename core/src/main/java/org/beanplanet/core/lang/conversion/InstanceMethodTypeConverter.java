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

/**
 * Implementation of a type converter that is pinned to an instance method of a class.
 * 
 * <p>Type converter instance methods must have the signature
 * <code>public <em>&lt;to-type&gt;</em> <em>&lt;methodName&gt;</em>(<em>&lt;from-type&gt;</em>)</code> and the
 * class itself must have a no-arg constructor. For
 * example, the following are all valid type converter instance methods:
 * <pre>
 * public int toInteger(String str) { ... }   // Might convert a string to an integer
 * public Integer toInteger(String str) { ... }   // Might convert a string to an integer wrapper
 * public Object[] toArray(Collection col) { ... }   // Might convert a collection to an array
 * public YourType toArray(MyType myTypeValue) { ... }   // Might convert objects of MyType to YourType
 * </pre>
 * Naturally, interfaces may be specified for the <em>from</em> and <em>to</em> types.
 * </p>
 * 
 * <p>This type converter implementation is not usually used directly by clients but is primarily used during auto-discovery
 * of type converters. See <code>{@link PackageScanTypeConverterLoader}</code> for further information about how to
 * make your type converters available at runtime.</p>
 * 
 * @author Gary Watson
 *
 */
public class InstanceMethodTypeConverter implements TypeConverter {
   protected Method typeConverterMethod;
   protected Object target;
   
   /**
    * Creates a new type converter instance method wrapper with no initial method. Be sure to set
    * a method using the <code>typeConverterMethod</code> property.
    */
   public InstanceMethodTypeConverter() {
      
   }
   
   /**
    * Creates a new static type converter method wrapper with the specified target instance and method.
    * 
    * @param typeConverterMethod the type converter method.
    */
   public InstanceMethodTypeConverter(Object target, Method typeConverterMethod) {
      setTarget(target);
      setTypeConverterMethod(typeConverterMethod);
   }
   
   
   /**
    * Returns the type converter method used in this converter.
    * 
    * @return the type converter method used by this converter to convert the source type to the target type.
    */
   public Method getTypeConverterMethod() {
      return typeConverterMethod;
   }


   /**
    * Sets the type converter method used in this converter.
    * 
    * @param typeConverterMethod the type converter method used by this converter to convert the source type to the target type.
    */
   public void setTypeConverterMethod(Method typeConverterMethod) {
      this.typeConverterMethod = typeConverterMethod;
   }

   /**
    * @return the target
    */
   public Object getTarget() {
      return target;
   }

   /**
    * @param target the target to set
    */
   public void setTarget(Object target) {
      this.target = target;
   }


   /**
    * Invokes the type converter instance method to perform the type conversion.
    * 
    * @param value the value to be converted
    * @param targetType the type of object the value is to be converted to
    * @return the value, converted to the target type.
    */
   @SuppressWarnings("unchecked")
   public <T> T convert(Object value, Class<T> targetType) throws UnsupportedTypeConversionException {
      Assert.notNull(typeConverterMethod, "The type converter method may not be null");
      return (T) TypeUtil.invokeMethod(null, typeConverterMethod, new Object[] {value});
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
