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
