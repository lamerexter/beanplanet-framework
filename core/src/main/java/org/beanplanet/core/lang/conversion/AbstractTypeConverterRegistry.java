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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of a type converter registry.
 * 
 * @author Gary Watson
 * 
 */
public abstract class AbstractTypeConverterRegistry implements TypeConverterRegistry {
   protected ConcurrentHashMap<Class<?>, ConcurrentHashMap<Class<?>, CompositeTypeConverter>> converters = new ConcurrentHashMap<Class<?>, ConcurrentHashMap<Class<?>, CompositeTypeConverter>>();

   public void addConverter(Class<?> fromType, Class<?> toType, TypeConverter converter) {
      ConcurrentHashMap<Class<?>, CompositeTypeConverter> sourceTypeConverterMap = converters.get(toType);
      if (sourceTypeConverterMap == null) {
         ConcurrentHashMap<Class<?>, CompositeTypeConverter> newSourceTypeConverterMap = new ConcurrentHashMap<Class<?>, CompositeTypeConverter>();
         sourceTypeConverterMap = converters.putIfAbsent(toType, newSourceTypeConverterMap);
         if (sourceTypeConverterMap == null) {
            sourceTypeConverterMap = newSourceTypeConverterMap; // un-contended put succeeded, so reuse our put value
         }
      }

      CompositeTypeConverter convertersSet = sourceTypeConverterMap.get(fromType);
      if (convertersSet == null) {
         CompositeTypeConverter newConvertersSet = new CompositeTypeConverter();
         convertersSet = sourceTypeConverterMap.putIfAbsent(fromType, newConvertersSet);
         if (convertersSet == null) {
            convertersSet = newConvertersSet; // un-contended put succeeded, so reuse our put value
         }
      }
      convertersSet.add(converter);
   }

   public void addConverter(TypeConverter converter) {
      // Dynamic (runtime-based) converters are just a special type of converter whose
      // capabilities cannot be determined until called.
      addConverter(Object.class, Object.class, converter);
   }

   public TypeConverter lookup(Class<?> fromType, Class<?> toType) {
      TypeConverter matchingConverter = null;

      Map<Class<?>, CompositeTypeConverter> sourceConvertersForTarget = converters.get(toType);
      if (sourceConvertersForTarget != null) {
         matchingConverter = sourceConvertersForTarget.get(fromType);
      }

      return matchingConverter;
   }

   public int size() {
      int size = 0;
      for (Map.Entry<Class<?>, ConcurrentHashMap<Class<?>, CompositeTypeConverter>> targetEntry : converters.entrySet()) {
         for (Map.Entry<Class<?>, CompositeTypeConverter> sourceTypeConverterEntry : targetEntry.getValue().entrySet()) {
            size += sourceTypeConverterEntry.getValue().size();
         }
      }

      return size;
   }

   public void clear() {
      if (converters != null) {
         converters.clear();
      }
   }

   @Override
   public String toString() {
//      StringBuilder s = new StringBuilder();
//      s.append(TypeUtil.getClassBaseName(getClass())).append("[\n");
//      s.append("Type Converter Registry Entries\n");
//      s.append("------------------------------------------------------------\n");
//      for (Map.Entry<Class<?>, ConcurrentHashMap<Class<?>, CompositeTypeConverter>> targetEntry : converters.entrySet()) {
//         for (Map.Entry<Class<?>, CompositeTypeConverter> sourceTypeConverterEntry : targetEntry.getValue().entrySet()) {
//            for (TypeConverter converter : sourceTypeConverterEntry.getValue()) {
//               s.append(ClassUtil.getDisplayNameForType(sourceTypeConverterEntry.getKey()));
//               s.append("=>").append(ClassUtil.getDisplayNameForType(targetEntry.getKey()));
//               s.append(" = ").append(converter).append("\n");
//            }
//         }
//      }
//      s.append("]");
//      return s.toString();
      throw new UnsupportedOperationException();
   }

}
