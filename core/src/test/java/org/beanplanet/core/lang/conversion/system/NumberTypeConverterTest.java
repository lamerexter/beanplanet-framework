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
package org.beanplanet.core.lang.conversion.system;

import org.beanplanet.core.lang.TypeUtil;
import org.beanplanet.core.lang.conversion.SystemTypeConverter;
import org.beanplanet.core.lang.conversion.TypeConverter;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Unit tests for {@code {@link NumberTypeConverterUtil }}.
 */
public class NumberTypeConverterTest {
   protected TypeConverter converter;
   protected Class<?> allTypes[] = new Class<?>[] { BigDecimal.class, BigInteger.class, boolean.class, byte.class,
                                                   char.class, Character.class, double.class, Double.class,
                                                   float.class, Float.class, int.class, Integer.class, long.class,
                                                   Long.class, short.class, Short.class };

   @Before
   public void setUp() {
      converter = SystemTypeConverter.getInstance();
   }

   @Test
   public void testAllNumberToNumberConversions() {
      String values[] = new String[] { "0", "1" };
      for (String value : values) {
         for (Class<?> sourceType : allTypes) {
            // Convert from test string value to the source type value
            Object sourceTypeValue = converter.convert(value, sourceType); //TypeUtil.instantiateClass(sourceType, char.class.equals(sourceType) || Character.class.equals(sourceType) ? new Object[] {value.charAt(0)} : new Object[] {value });
            for (Class<?> targetType : allTypes) {
                  Object convertedValue = converter.convert(sourceTypeValue, targetType);
                  assertThat(convertedValue, notNullValue());
                  assertThat(convertedValue.getClass(), equalTo(TypeUtil.ensureNonPrimitiveType(targetType)));
            }
         }
      }
   }
}
