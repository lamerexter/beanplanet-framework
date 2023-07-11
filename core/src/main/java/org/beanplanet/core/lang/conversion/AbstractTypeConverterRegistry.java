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
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of a type converter registry.
 * 
 * @author Gary Watson
 * 
 */
public abstract class AbstractTypeConverterRegistry implements TypeConverterRegistry {
   protected ConcurrentHashMap<Class<?>, ConcurrentHashMap<Class<?>, CompositeTypeConverter>> converters = new ConcurrentHashMap<>();

   protected ConcurrentHashMap<Class<?>, CompositeTypeConverter> unboundedConverters = new ConcurrentHashMap<>();

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

   public void addConverter(Class<?> fromType, TypeConverter converter) {
      CompositeTypeConverter unboundedConvertersFromType = unboundedConverters.computeIfAbsent(fromType, t -> new CompositeTypeConverter());
      unboundedConvertersFromType.add(converter);
   }

   public void addConverter(TypeConverter converter) {
      // Dynamic (runtime-based) converters are just a special type of converter whose
      // capabilities cannot be determined until called.
      addConverter(Object.class, Object.class, converter);
   }

   public Optional<TypeConverter> lookup(final Class<?> fromType, final Class<?> toType) {
      TypeConverter matchingConverter = null;

      //----------------------------------------------------------------------------------------------------------------
      // Lookup bounded type converter(s).
      //----------------------------------------------------------------------------------------------------------------
      Map<Class<?>, CompositeTypeConverter> sourceConvertersForTarget = converters.get(toType);
      if (sourceConvertersForTarget != null) {
         // Attempt direct lookup
         matchingConverter = sourceConvertersForTarget.get(fromType);

         if (matchingConverter == null) {
            // Fall back to assignable converter check. This will handle array types, for example (Object[] <-- T[])
            matchingConverter = sourceConvertersForTarget.entrySet().stream()
                                                         .filter(e -> e.getKey().isAssignableFrom(fromType))
                                                         .map(Map.Entry::getValue)
                                                         .findFirst().orElse(null);

         }
      }
      if (matchingConverter != null) return Optional.of(matchingConverter);

      //----------------------------------------------------------------------------------------------------------------
      // Lookup unbounded type converter(s).
      //----------------------------------------------------------------------------------------------------------------
      // Attempt direct lookup
      matchingConverter = unboundedConverters.get(fromType);

      if (matchingConverter == null) {
         // Fall back to assignable converter check. This will handle array types, for example (Object[] <-- T[])
         matchingConverter = unboundedConverters.entrySet().stream()
                 .filter(e -> e.getKey().isAssignableFrom(fromType))
                 .map(Map.Entry::getValue)
                 .findFirst().orElse(null);

      }

      return Optional.ofNullable(matchingConverter);
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
}
