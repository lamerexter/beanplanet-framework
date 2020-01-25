/*
 *  MIT Licence:
 *
 *  Copyright (C) 2019 Beanplanet Ltd
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
package org.beanplanet.core.lang.conversion.system;

import java.math.BigDecimal;
import java.math.BigInteger;

import junit.framework.TestCase;
import org.beanplanet.core.lang.TypeUtil;
import org.beanplanet.core.lang.conversion.SystemTypeConverter;
import org.beanplanet.core.lang.conversion.TypeConverter;
import org.beanplanet.core.lang.conversion.UnsupportedTypeConversionException;
import org.beanplanet.core.lang.conversion.system.NumberTypeConverterUtil;
import org.junit.Before;

/**
 * Unit tests for {@code {@link NumberTypeConverterUtil }}.
 */
public class NumberTypeConverterTest extends TestCase {
   protected TypeConverter converter;
   protected Class<?> allTypes[] = new Class<?>[] { BigDecimal.class, BigInteger.class, boolean.class, byte.class,
                                                   char.class, Character.class, double.class, Double.class,
                                                   float.class, Float.class, int.class, Integer.class, long.class,
                                                   Long.class, short.class, Short.class };

   @Before
   public void setUp() {
      converter = SystemTypeConverter.getInstance();
   }

   public void testAllNumberToNumberConversions() {
      String values[] = new String[] { "0", "1" };
      for (String value : values) {
         for (Class<?> sourceType : allTypes) {
            // Convert from test string value to the source type value
            Object sourceTypeValue = converter.convert(value, sourceType); //TypeUtil.instantiateClass(sourceType, char.class.equals(sourceType) || Character.class.equals(sourceType) ? new Object[] {value.charAt(0)} : new Object[] {value });
            for (Class<?> targetType : allTypes) {
               try {
                  Object convertedValue = converter.convert(sourceTypeValue, targetType);
                  assertNotNull(convertedValue);
                  assertEquals(TypeUtil.ensureNonPrimitiveType(targetType), convertedValue.getClass());
               }
               catch (UnsupportedTypeConversionException utcEx) {
                  fail("Unable to test type onversion from "+sourceType+" to "+targetType+": " + utcEx.getMessage());
               }
            }
         }
      }
   }
}
