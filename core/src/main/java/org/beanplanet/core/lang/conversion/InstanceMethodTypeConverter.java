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
