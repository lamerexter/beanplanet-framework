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

import java.util.Optional;

public interface TypeConverterRegistry {
   void addConverter(Class<?> fromType, Class<?> toType, TypeConverter converter);
   void addConverter(Class<?> fromType, TypeConverter converter);
   void addConverter(TypeConverter converter);
   Optional<TypeConverter> lookup(Class<?> fromType, Class<?> toType);

   /**
    * The kind of conversion the registry's converters make from one type to another, as far as it can be known before
    * converting a value: empty if there is no converter for the types (a converter that decides only when called,
    * registered for any type, is not counted).
    *
    * @param fromType the type converted from.
    * @param toType the type converted to.
    * @return the kind of the conversion, if there is one.
    */
   default Optional<ConversionKind> kindOf(Class<?> fromType, Class<?> toType) {
      return lookup(fromType, toType).map(TypeConverter::getKind);
   }
   
   int size();
   void clear();
}
