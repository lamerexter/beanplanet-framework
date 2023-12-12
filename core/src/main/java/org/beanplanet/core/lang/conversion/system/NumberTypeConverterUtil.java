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

import org.beanplanet.core.lang.conversion.TypeConversionException;
import org.beanplanet.core.lang.conversion.annotations.TypeConverter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * A static utility class containing numeric type converter methods, discovered at runtime and useful for
 * general use.
 *
 * @author Gary Watson
 */
@TypeConverter
public class NumberTypeConverterUtil {
    @TypeConverter
    public static BigDecimal toBigDecimal(BigInteger value) {
        return (value == null ? null : new BigDecimal(value));
    }

    @TypeConverter
    public static BigDecimal toBigDecimal(boolean value) {
        return BigDecimal.valueOf(value ? 1 : 0);
    }

    @TypeConverter
    public static BigDecimal toBigDecimal(Boolean value) {
        return (value == null ? null : toBigDecimal(value.booleanValue()));
    }

    @TypeConverter
    public static BigDecimal toBigDecimal(byte value) {
        return BigDecimal.valueOf(value);
    }

    @TypeConverter
    public static BigDecimal toBigDecimal(Byte value) {
        return (value == null ? null : toBigDecimal(value.byteValue()));
    }

    @TypeConverter
    public static BigDecimal toBigDecimal(char value) {
        return BigDecimal.valueOf(value);
    }

    @TypeConverter
    public static BigDecimal toBigDecimal(Character value) {
        return (value == null ? null : toBigDecimal(value.charValue()));
    }

    @TypeConverter
    public static BigDecimal toBigDecimal(double value) {
        return BigDecimal.valueOf(value);
    }

    @TypeConverter
    public static BigDecimal toBigDecimal(Double value) {
        return (value == null ? null : toBigDecimal(value.doubleValue()));
    }

    @TypeConverter
    public static BigDecimal toBigDecimal(float value) {
        return BigDecimal.valueOf(value);
    }

    @TypeConverter
    public static BigDecimal toBigDecimal(Float value) {
        return (value == null ? null : toBigDecimal(value.doubleValue()));
    }

    @TypeConverter
    public static BigDecimal toBigDecimal(int value) {
        return BigDecimal.valueOf(value);
    }

    @TypeConverter
    public static BigDecimal toBigDecimal(Integer value) {
        return (value == null ? null : toBigDecimal(value.longValue()));
    }

    @TypeConverter
    public static BigDecimal toBigDecimal(long value) {
        return BigDecimal.valueOf(value);
    }

    @TypeConverter
    public static BigDecimal toBigDecimal(Long value) {
        return (value == null ? null : toBigDecimal(value.longValue()));
    }

    @TypeConverter
    public static BigDecimal toBigDecimal(short value) {
        return BigDecimal.valueOf(value);
    }

    @TypeConverter
    public static BigDecimal toBigDecimal(Short value) {
        return (value == null ? null : toBigDecimal(value.shortValue()));
    }

    @TypeConverter
    public static BigDecimal toBigDecimal(String value) {
        return (value == null ? null : new BigDecimal(value));
    }

    @TypeConverter
    public static BigInteger toBigInteger(BigDecimal value) {
        return (value == null ? null : value.toBigInteger());
    }

    @TypeConverter
    public static BigInteger toBigInteger(boolean value) {
        return BigInteger.valueOf(value ? 1L : 0L);
    }

    @TypeConverter
    public static BigInteger toBigInteger(Boolean value) {
        return (value == null ? null : toBigInteger(value.booleanValue()));
    }

    @TypeConverter
    public static BigInteger toBigInteger(byte value) {
        return BigInteger.valueOf(value);
    }

    @TypeConverter
    public static BigInteger toBigInteger(Byte value) {
        return (value == null ? null : toBigInteger(value.byteValue()));
    }

    @TypeConverter
    public static BigInteger toBigInteger(char value) {
        return BigInteger.valueOf(value);
    }

    @TypeConverter
    public static BigInteger toBigInteger(Character value) {
        return (value == null ? null : toBigInteger(value.charValue()));
    }

    @TypeConverter
    public static BigInteger toBigInteger(double value) {
        return BigInteger.valueOf((long) value);
    }

    @TypeConverter
    public static BigInteger toBigInteger(Double value) {
        return (value == null ? null : toBigInteger(value.doubleValue()));
    }

    @TypeConverter
    public static BigInteger toBigInteger(float value) {
        return BigInteger.valueOf((long) value);
    }

    @TypeConverter
    public static BigInteger toBigInteger(Float value) {
        return (value == null ? null : toBigInteger(value.doubleValue()));
    }

    @TypeConverter
    public static BigInteger toBigInteger(int value) {
        return BigInteger.valueOf(value);
    }

    @TypeConverter
    public static BigInteger toBigInteger(Integer value) {
        return (value == null ? null : toBigInteger(value.longValue()));
    }

    @TypeConverter
    public static BigInteger toBigInteger(long value) {
        return BigInteger.valueOf(value);
    }

    @TypeConverter
    public static BigInteger toBigInteger(Long value) {
        return (value == null ? null : toBigInteger(value.longValue()));
    }

    @TypeConverter
    public static BigInteger toBigInteger(short value) {
        return BigInteger.valueOf(value);
    }

    @TypeConverter
    public static BigInteger toBigInteger(Short value) {
        return (value == null ? null : toBigInteger(value.shortValue()));
    }

    @TypeConverter
    public static BigInteger toBigInteger(String value) {
        return (value == null ? null : new BigInteger(value));
    }

    @TypeConverter
    public static Boolean toBoolean(BigDecimal value) {
        return (value == null ? null : value.intValue() != 0);
    }

    @TypeConverter
    public static Boolean toBoolean(BigInteger value) {
        return (value == null ? null : value.intValue() != 0);
    }

    @TypeConverter
    public static Boolean toBoolean(boolean value) {
        return value;
    }

    @TypeConverter
    public static Boolean toBoolean(Boolean value) {
        return value;
    }

    @TypeConverter
    public static Boolean toBoolean(byte value) {
        return value != 0;
    }

    @TypeConverter
    public static Boolean toBoolean(Byte value) {
        return (value == null ? null : toBoolean(value.byteValue()));
    }

    @TypeConverter
    public static Boolean toBoolean(char value) {
        return value != 0;
    }

    @TypeConverter
    public static Boolean toBoolean(Character value) {
        return (value == null ? null : toBoolean(value.charValue()));
    }

    @TypeConverter
    public static Boolean toBoolean(double value) {
        return value != 0;
    }

    @TypeConverter
    public static Boolean toBoolean(Double value) {
        return (value == null ? null : toBoolean(value.doubleValue()));
    }

    @TypeConverter
    public static Boolean toBoolean(float value) {
        return value != 0;
    }

    @TypeConverter
    public static Boolean toBoolean(Float value) {
        return (value == null ? null : toBoolean(value.floatValue()));
    }

    @TypeConverter
    public static Boolean toBoolean(int value) {
        return value != 0;
    }

    @TypeConverter
    public static Boolean toBoolean(Integer value) {
        return (value == null ? null : toBoolean(value.intValue()));
    }

    @TypeConverter
    public static Boolean toBoolean(long value) {
        return value != 0;
    }

    @TypeConverter
    public static Boolean toBoolean(Long value) {
        return (value == null ? null : toBoolean(value.longValue()));
    }

    @TypeConverter
    public static Boolean toBoolean(short value) {
        return value != 0;
    }

    @TypeConverter
    public static Boolean toBoolean(Short value) {
        return (value == null ? null : toBoolean(value.shortValue()));
    }

    @TypeConverter
    public static Boolean toBoolean(String value) {
        return (value == null ? null : Boolean.valueOf(value));
    }

    @TypeConverter
    public static Byte toByte(BigDecimal value) {
        return (value == null ? null : value.byteValue());
    }

    @TypeConverter
    public static Byte toByte(BigInteger value) {
        return (value == null ? null : value.byteValue());
    }

    @TypeConverter
    public static Byte toByte(boolean value) {
        return value ? (byte) 1 : 0;
    }

    @TypeConverter
    public static Byte toByte(Boolean value) {
        return (value == null ? null : toByte(value.booleanValue()));
    }

    @TypeConverter
    public static Byte toByte(byte value) {
        return value;
    }

    @TypeConverter
    public static Byte toByte(char value) {
        return (byte) value;
    }

    @TypeConverter
    public static Byte toByte(Character value) {
        return (value == null ? null : toByte(value.charValue()));
    }

    @TypeConverter
    public static Byte toByte(double value) {
        return (byte) value;
    }

    @TypeConverter
    public static Byte toByte(Double value) {
        return (value == null ? null : toByte(value.doubleValue()));
    }

    @TypeConverter
    public static Byte toByte(float value) {
        return (byte) value;
    }

    @TypeConverter
    public static Byte toByte(Float value) {
        return (value == null ? null : toByte(value.doubleValue()));
    }

    @TypeConverter
    public static Byte toByte(int value) {
        return (byte) value;
    }

    @TypeConverter
    public static Byte toByte(Integer value) {
        return (value == null ? null : toByte(value.longValue()));
    }

    @TypeConverter
    public static Byte toByte(long value) {
        return (byte) value;
    }

    @TypeConverter
    public static Byte toByte(Long value) {
        return (value == null ? null : toByte(value.longValue()));
    }

    @TypeConverter
    public static Byte toByte(short value) {
        return (byte) value;
    }

    @TypeConverter
    public static Byte toByte(Short value) {
        return (value == null ? null : toByte(value.shortValue()));
    }

    @TypeConverter
    public static Byte toByte(String value) {
        return (value == null ? null : Byte.valueOf(value));
    }

    @TypeConverter
    public static Character toChararcter(BigDecimal value) {
        return (value == null ? null : (char) value.intValue());
    }

    @TypeConverter
    public static Character toCharacter(BigDecimal value) {
        return (value == null ? null : (char) value.intValue());
    }

    @TypeConverter
    public static Character toCharacter(BigInteger value) {
        return (value == null ? null : (char) value.intValue());
    }

    @TypeConverter
    public static Character toCharacter(boolean value) {
        return value ? (char) 1 : 0;
    }

    @TypeConverter
    public static Character toCharacter(Boolean value) {
        return (value == null ? null : toCharacter(value.booleanValue()));
    }

    @TypeConverter
    public static Character toCharacter(byte value) {
        return (char) value;
    }

    @TypeConverter
    public static Character toCharacter(Byte value) {
        return (value == null ? null : (char) value.byteValue());
    }

    @TypeConverter
    public static Character toCharacter(char value) {
        return (char) value;
    }

    @TypeConverter
    public static Character toCharacter(Character value) {
        return (value == null ? null : toCharacter(value.charValue()));
    }

    @TypeConverter
    public static Character toCharacter(double value) {
        return (char) value;
    }

    @TypeConverter
    public static Character toCharacter(Double value) {
        return (value == null ? null : toCharacter(value.doubleValue()));
    }

    @TypeConverter
    public static Character toCharacter(float value) {
        return (char) value;
    }

    @TypeConverter
    public static Character toCharacter(Float value) {
        return (value == null ? null : toCharacter(value.doubleValue()));
    }

    @TypeConverter
    public static Character toCharacter(int value) {
        return (char) value;
    }

    @TypeConverter
    public static Character toCharacter(Integer value) {
        return (value == null ? null : toCharacter(value.longValue()));
    }

    @TypeConverter
    public static Character toCharacter(long value) {
        return (char) value;
    }

    @TypeConverter
    public static Character toCharacter(Long value) {
        return (value == null ? null : toCharacter(value.longValue()));
    }

    @TypeConverter
    public static Character toCharacter(short value) {
        return (char) value;
    }

    @TypeConverter
    public static Character toCharacter(Short value) {
        return (value == null ? null : toCharacter(value.shortValue()));
    }

    @TypeConverter
    public static Character toCharacter(String value) {
        if (value == null)
            return null;

        if (value.isEmpty())
            throw new TypeConversionException("Unable to concert empty string to character");

        return value.charAt(0);
    }

    @TypeConverter
    public static Double toDouble(BigDecimal value) {
        return (value == null ? null : value.doubleValue());
    }

    @TypeConverter
    public static Double toDouble(BigInteger value) {
        return (value == null ? null : value.doubleValue());
    }

    @TypeConverter
    public static Double toDouble(boolean value) {
        return value ? 1d : 0d;
    }

    @TypeConverter
    public static Double toDouble(Boolean value) {
        return (value == null ? null : toDouble(value.booleanValue()));
    }

    @TypeConverter
    public static Double toDouble(byte value) {
        return (double) value;
    }

    @TypeConverter
    public static Double toDouble(Byte value) {
        return (value == null ? null : toDouble(value.byteValue()));
    }

    @TypeConverter
    public static Double toDouble(char value) {
        return (double) value;
    }

    @TypeConverter
    public static Double toDouble(Character value) {
        return (value == null ? null : toDouble(value.charValue()));
    }

    @TypeConverter
    public static Double toDouble(double value) {
        return value;
    }

    @TypeConverter
    public static Double toDouble(float value) {
        return (double) value;
    }

    @TypeConverter
    public static Double toDouble(Float value) {
        return (value == null ? null : toDouble(value.doubleValue()));
    }

    @TypeConverter
    public static Double toDouble(int value) {
        return (double) value;
    }

    @TypeConverter
    public static Double toDouble(Integer value) {
        return (value == null ? null : toDouble(value.longValue()));
    }

    @TypeConverter
    public static Double toDouble(long value) {
        return (double) value;
    }

    @TypeConverter
    public static Double toDouble(Long value) {
        return (value == null ? null : toDouble(value.longValue()));
    }

    @TypeConverter
    public static Double toDouble(short value) {
        return (double) value;
    }

    @TypeConverter
    public static Double toDouble(Short value) {
        return (value == null ? null : toDouble(value.shortValue()));
    }

    @TypeConverter
    public static Double toDouble(String value) {
        return (value == null ? null : Double.valueOf(value));
    }

    @TypeConverter
    public static Float toFloat(BigDecimal value) {
        return (value == null ? null : value.floatValue());
    }

    @TypeConverter
    public static Float toFloat(BigInteger value) {
        return (value == null ? null : value.floatValue());
    }

    @TypeConverter
    public static Float toFloat(boolean value) {
        return value ? 1f : 0f;
    }

    @TypeConverter
    public static Float toFloat(Boolean value) {
        return (value == null ? null : toFloat(value.booleanValue()));
    }

    @TypeConverter
    public static Float toFloat(byte value) {
        return (float) value;
    }

    @TypeConverter
    public static Float toFloat(Byte value) {
        return (value == null ? null : toFloat(value.byteValue()));
    }

    @TypeConverter
    public static Float toFloat(char value) {
        return (float) value;
    }

    @TypeConverter
    public static Float toFloat(Character value) {
        return (value == null ? null : toFloat(value.charValue()));
    }

    @TypeConverter
    public static Float toFloat(double value) {
        return (float) value;
    }

    @TypeConverter
    public static Float toFloat(Double value) {
        return (value == null ? null : toFloat(value.doubleValue()));
    }

    @TypeConverter
    public static Float toFloat(float value) {
        return value;
    }

    @TypeConverter
    public static Float toFloat(int value) {
        return (float) value;
    }

    @TypeConverter
    public static Float toFloat(Integer value) {
        return (value == null ? null : toFloat(value.longValue()));
    }

    @TypeConverter
    public static Float toFloat(long value) {
        return (float) value;
    }

    @TypeConverter
    public static Float toFloat(Long value) {
        return (value == null ? null : toFloat(value.longValue()));
    }

    @TypeConverter
    public static Float toFloat(short value) {
        return (float) value;
    }

    @TypeConverter
    public static Float toFloat(Short value) {
        return (value == null ? null : toFloat(value.shortValue()));
    }

    @TypeConverter
    public static Float toFloat(String value) {
        return (value == null ? null : Float.valueOf(value));
    }

    @TypeConverter
    public static Integer toInteger(BigDecimal value) {
        return (value == null ? null : value.intValue());
    }

    @TypeConverter
    public static Integer toInteger(BigInteger value) {
        return (value == null ? null : value.intValue());
    }

    @TypeConverter
    public static Integer toInteger(boolean value) {
        return value ? 1 : 0;
    }

    @TypeConverter
    public static Integer toInteger(Boolean value) {
        return (value == null ? null : toInteger(value.booleanValue()));
    }

    @TypeConverter
    public static Integer toInteger(byte value) {
        return (int) value;
    }

    @TypeConverter
    public static Integer toInteger(Byte value) {
        return (value == null ? null : toInteger(value.byteValue()));
    }

    @TypeConverter
    public static Integer toInteger(char value) {
        return (int) value;
    }

    @TypeConverter
    public static Integer toInteger(Character value) {
        return (value == null ? null : toInteger(value.charValue()));
    }

    @TypeConverter
    public static Integer toInteger(double value) {
        return (int) value;
    }

    @TypeConverter
    public static Integer toInteger(Double value) {
        return (value == null ? null : toInteger(value.doubleValue()));
    }

    @TypeConverter
    public static Integer toInteger(float value) {
        return (int) value;
    }

    @TypeConverter
    public static Integer toInteger(Float value) {
        return (value == null ? null : toInteger(value.floatValue()));
    }

    @TypeConverter
    public static Integer toInteger(int value) {
        return value;
    }

    @TypeConverter
    public static Integer toInteger(long value) {
        return (int) value;
    }

    @TypeConverter
    public static Integer toInteger(Long value) {
        return (value == null ? null : toInteger(value.longValue()));
    }

    @TypeConverter
    public static Integer toInteger(short value) {
        return (int) value;
    }

    @TypeConverter
    public static Integer toInteger(String value) {
        return (value == null ? null : Integer.valueOf(value));
    }

    @TypeConverter
    public static Integer toInteger(Short value) {
        return (value == null ? null : toInteger(value.shortValue()));
    }

    @TypeConverter
    public static Instant toInstant(long value) {
        return Instant.ofEpochMilli(value);
    }

    @TypeConverter
    public static Instant toInstant(Long value) {
        return (value == null ? null : toInstant(value.longValue()));
    }

    @TypeConverter
    public static LocalDate toLocaldate(long value) {
        return Instant.ofEpochMilli(value).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @TypeConverter
    public static LocalDate toLocaldate(Long value) {
        return (value == null ? null : toLocaldate(value.longValue()));
    }

    @TypeConverter
    public static Long toLong(BigDecimal value) {
        return (value == null ? null : value.longValue());
    }

    @TypeConverter
    public static Long toLong(BigInteger value) {
        return (value == null ? null : value.longValue());
    }

    @TypeConverter
    public static Long toLong(boolean value) {
        return (long) (value ? 1 : 0);
    }

    @TypeConverter
    public static Long toLong(Boolean value) {
        return (value == null ? null : toLong(value.booleanValue()));
    }

    @TypeConverter
    public static Long toLong(byte value) {
        return (long) value;
    }

    @TypeConverter
    public static Long toLong(Byte value) {
        return (value == null ? null : toLong(value.byteValue()));
    }

    @TypeConverter
    public static Long toLong(char value) {
        return (long) value;
    }

    @TypeConverter
    public static Long toLong(Character value) {
        return (value == null ? null : toLong(value.charValue()));
    }

    @TypeConverter
    public static Long toLong(double value) {
        return (long) (int) value;
    }

    @TypeConverter
    public static Long toLong(Double value) {
        return (value == null ? null : toLong(value.doubleValue()));
    }

    @TypeConverter
    public static Long toLong(float value) {
        return (long) (int) value;
    }

    @TypeConverter
    public static Long toLong(Float value) {
        return (value == null ? null : toLong(value.floatValue()));
    }

    @TypeConverter
    public static Long toLong(int value) {
        return (long) value;
    }

    @TypeConverter
    public static Long toLong(Integer value) {
        return (value == null ? null : toLong(value.intValue()));
    }

    @TypeConverter
    public static Long toLong(long value) {
        return (long) (int) value;
    }

    @TypeConverter
    public static Long toLong(Long value) {
        return (value);
    }

    @TypeConverter
    public static Long toLong(short value) {
        return (long) value;
    }

    @TypeConverter
    public static Long toLong(Short value) {
        return (value == null ? null : toLong(value.shortValue()));
    }

    @TypeConverter
    public static Long toLong(String value) {
        return (value == null ? null : Long.valueOf(value));
    }

    @TypeConverter
    public static Long toLong(Date value) {
        return (value == null ? null : value.getTime());
    }

    @TypeConverter
    public static Number toNumber(BigDecimal value) {
        return value;
    }

    @TypeConverter
    public static Number toNumber(BigInteger value) {
        return value;
    }

    @TypeConverter
    public static Number toNumber(boolean value) {
        return (long) (value ? 1 : 0);
    }

    @TypeConverter
    public static Number toNumber(Boolean value) {
        return (value == null ? null : toNumber(value.booleanValue()));
    }

    @TypeConverter
    public static Number toNumber(byte value) {
        return value;
    }

    @TypeConverter
    public static Number toNumber(Byte value) {
        return (value == null ? null : toNumber(value.byteValue()));
    }

    @TypeConverter
    public static Number toNumber(char value) {
        return (int) value;
    }

    @TypeConverter
    public static Number toNumber(Character value) {
        return (value == null ? null : toNumber(value.charValue()));
    }

    @TypeConverter
    public static Number toNumber(double value) {
        return value;
    }

    @TypeConverter
    public static Number toNumber(Double value) {
        return (value == null ? null : toNumber(value.doubleValue()));
    }

    @TypeConverter
    public static Number toNumber(float value) {
        return value;
    }

    @TypeConverter
    public static Number toNumber(Float value) {
        return (value == null ? null : toNumber(value.floatValue()));
    }

    @TypeConverter
    public static Number toNumber(int value) {
        return value;
    }

    @TypeConverter
    public static Number toNumber(Integer value) {
        return (value == null ? null : toNumber(value.intValue()));
    }

    @TypeConverter
    public static Number toNumber(long value) {
        return (long) (int) value;
    }

    @TypeConverter
    public static Number toNumber(short value) {
        return value;
    }

    @TypeConverter
    public static Number toNumber(Short value) {
        return (value == null ? null : toNumber(value.shortValue()));
    }

    @TypeConverter
    public static Number toNumber(String value) {
        return toBigDecimal(value);
    }

    @TypeConverter
    public static Short toShort(BigDecimal value) {
        return (value == null ? null : value.shortValue());
    }

    @TypeConverter
    public static Short toShort(BigInteger value) {
        return (value == null ? null : value.shortValue());
    }

    @TypeConverter
    public static Short toShort(boolean value) {
        return value ? (short) 1 : 0;
    }

    @TypeConverter
    public static Short toShort(Boolean value) {
        return (value == null ? null : toShort(value.booleanValue()));
    }

    @TypeConverter
    public static Short toShort(byte value) {
        return (short) value;
    }

    @TypeConverter
    public static Short toShort(Byte value) {
        return (value == null ? null : toShort(value.byteValue()));
    }

    @TypeConverter
    public static Short toShort(char value) {
        return (short) value;
    }

    @TypeConverter
    public static Short toShort(Character value) {
        return (value == null ? null : toShort(value.charValue()));
    }

    @TypeConverter
    public static Short toShort(double value) {
        return (short) value;
    }

    @TypeConverter
    public static Short toShort(Double value) {
        return (value == null ? null : toShort(value.doubleValue()));
    }

    @TypeConverter
    public static Short toShort(float value) {
        return (short) value;
    }

    @TypeConverter
    public static Short toShort(Float value) {
        return (value == null ? null : toShort(value.floatValue()));
    }

    @TypeConverter
    public static Short toShort(int value) {
        return (short) value;
    }

    @TypeConverter
    public static Short toShort(Integer value) {
        return (value == null ? null : toShort(value.intValue()));
    }

    @TypeConverter
    public static Short toShort(long value) {
        return (short) value;
    }

    @TypeConverter
    public static Short toShort(Long value) {
        return (value == null ? null : toShort(value.longValue()));
    }

    @TypeConverter
    public static Short toShort(short value) {
        return value;
    }

    @TypeConverter
    public static Short toShort(String value) {
        return (value == null ? null : Short.valueOf(value));
    }

    @TypeConverter
    public static boolean toPrimitiveBoolean(BigDecimal value) {
        return toBoolean(value);
    }

    @TypeConverter
    public static boolean toPrimitiveBoolean(BigInteger value) {
        return toBoolean(value);
    }

    @TypeConverter
    public static boolean toPrimitiveBoolean(boolean value) {
        return toBoolean(value);
    }

    @TypeConverter
    public static boolean toPrimitiveBoolean(Boolean value) {
        return toBoolean(value);
    }

    @TypeConverter
    public static boolean toPrimitiveBoolean(byte value) {
        return toBoolean(value);
    }

    @TypeConverter
    public static boolean toPrimitiveBoolean(Byte value) {
        return toBoolean(value);
    }

    @TypeConverter
    public static boolean toPrimitiveBoolean(char value) {
        return toBoolean(value);
    }

    @TypeConverter
    public static boolean toPrimitiveBoolean(Character value) {
        return toBoolean(value);
    }

    @TypeConverter
    public static boolean toPrimitiveBoolean(double value) {
        return toBoolean(value);
    }

    @TypeConverter
    public static boolean toPrimitiveBoolean(Double value) {
        return toBoolean(value);
    }

    @TypeConverter
    public static boolean toPrimitiveBoolean(float value) {
        return toBoolean(value);
    }

    @TypeConverter
    public static boolean toPrimitiveBoolean(Float value) {
        return toBoolean(value);
    }

    @TypeConverter
    public static boolean toPrimitiveBoolean(int value) {
        return toBoolean(value);
    }

    @TypeConverter
    public static boolean toPrimitiveBoolean(Integer value) {
        return toBoolean(value);
    }

    @TypeConverter
    public static boolean toPrimitiveBoolean(long value) {
        return toBoolean(value);
    }

    @TypeConverter
    public static boolean toPrimitiveBoolean(Long value) {
        return toBoolean(value);
    }

    @TypeConverter
    public static boolean toPrimitiveBoolean(short value) {
        return toBoolean(value);
    }

    @TypeConverter
    public static boolean toPrimitiveBoolean(Short value) {
        return toBoolean(value);
    }

    @TypeConverter
    public static boolean toPrimitiveBoolean(String value) {
        return toBoolean(value);
    }

    @TypeConverter
    public static byte toPrimitiveByte(BigDecimal value) {
        return toByte(value);
    }

    @TypeConverter
    public static byte toPrimitiveByte(BigInteger value) {
        return toByte(value);
    }

    @TypeConverter
    public static byte toPrimitiveByte(boolean value) {
        return toByte(value);
    }

    @TypeConverter
    public static byte toPrimitiveByte(Boolean value) {
        return toByte(value);
    }

    @TypeConverter
    public static byte toPrimitiveByte(byte value) {
        return toByte(value);
    }

    @TypeConverter
    public static byte toPrimitiveByte(Byte value) {
        return toByte(value);
    }

    @TypeConverter
    public static byte toPrimitiveByte(char value) {
        return toByte(value);
    }

    @TypeConverter
    public static byte toPrimitiveByte(Character value) {
        return toByte(value);
    }

    @TypeConverter
    public static byte toPrimitiveByte(double value) {
        return toByte(value);
    }

    @TypeConverter
    public static byte toPrimitiveByte(Double value) {
        return toByte(value);
    }

    @TypeConverter
    public static byte toPrimitiveByte(float value) {
        return toByte(value);
    }

    @TypeConverter
    public static byte toPrimitiveByte(Float value) {
        return toByte(value);
    }

    @TypeConverter
    public static byte toPrimitiveByte(int value) {
        return toByte(value);
    }

    @TypeConverter
    public static byte toPrimitiveByte(Integer value) {
        return toByte(value);
    }

    @TypeConverter
    public static byte toPrimitiveByte(long value) {
        return toByte(value);
    }

    @TypeConverter
    public static byte toPrimitiveByte(Long value) {
        return toByte(value);
    }

    @TypeConverter
    public static byte toPrimitiveByte(short value) {
        return toByte(value);
    }

    @TypeConverter
    public static byte toPrimitiveByte(Short value) {
        return toByte(value);
    }

    @TypeConverter
    public static byte toPrimitiveByte(String value) {
        return toByte(value);
    }

    @TypeConverter
    public static char toPrimitiveCharacter(BigDecimal value) {
        return toCharacter(value);
    }

    @TypeConverter
    public static char toPrimitiveCharacter(BigInteger value) {
        return toCharacter(value);
    }

    @TypeConverter
    public static char toPrimitiveCharacter(boolean value) {
        return toCharacter(value);
    }

    @TypeConverter
    public static char toPrimitiveCharacter(Boolean value) {
        return toCharacter(value);
    }

    @TypeConverter
    public static char toPrimitiveCharacter(byte value) {
        return toCharacter(value);
    }

    @TypeConverter
    public static char toPrimitiveCharacter(Byte value) {
        return toCharacter(value);
    }

    @TypeConverter
    public static char toPrimitiveCharacter(char value) {
        return toCharacter(value);
    }

    @TypeConverter
    public static char toPrimitiveCharacter(Character value) {
        return toCharacter(value);
    }

    @TypeConverter
    public static char toPrimitiveCharacter(double value) {
        return toCharacter(value);
    }

    @TypeConverter
    public static char toPrimitiveCharacter(Double value) {
        return toCharacter(value);
    }

    @TypeConverter
    public static char toPrimitiveCharacter(float value) {
        return toCharacter(value);
    }

    @TypeConverter
    public static char toPrimitiveCharacter(Float value) {
        return toCharacter(value);
    }

    @TypeConverter
    public static char toPrimitiveCharacter(int value) {
        return toCharacter(value);
    }

    @TypeConverter
    public static char toPrimitiveCharacter(Integer value) {
        return toCharacter(value);
    }

    @TypeConverter
    public static char toPrimitiveCharacter(long value) {
        return toCharacter(value);
    }

    @TypeConverter
    public static char toPrimitiveCharacter(Long value) {
        return toCharacter(value);
    }

    @TypeConverter
    public static char toPrimitiveCharacter(short value) {
        return toCharacter(value);
    }

    @TypeConverter
    public static char toPrimitiveCharacter(String value) {
        return toCharacter(value);
    }

    @TypeConverter
    public static char toPrimitiveCharacter(Short value) {
        return toCharacter(value);
    }

    @TypeConverter
    public static double toPrimitiveDouble(BigDecimal value) {
        return toDouble(value);
    }

    @TypeConverter
    public static double toPrimitiveDouble(BigInteger value) {
        return toDouble(value);
    }

    @TypeConverter
    public static double toPrimitiveDouble(boolean value) {
        return toDouble(value);
    }

    @TypeConverter
    public static double toPrimitiveDouble(Boolean value) {
        return toDouble(value);
    }

    @TypeConverter
    public static double toPrimitiveDouble(byte value) {
        return toDouble(value);
    }

    @TypeConverter
    public static double toPrimitiveDouble(Byte value) {
        return toDouble(value);
    }

    @TypeConverter
    public static double toPrimitiveDouble(char value) {
        return toDouble(value);
    }

    @TypeConverter
    public static double toPrimitiveDouble(Character value) {
        return toDouble(value);
    }

    @TypeConverter
    public static double toPrimitiveDouble(double value) {
        return toDouble(value);
    }

    @TypeConverter
    public static double toPrimitiveDouble(Double value) {
        return toDouble(value);
    }

    @TypeConverter
    public static double toPrimitiveDouble(float value) {
        return toDouble(value);
    }

    @TypeConverter
    public static double toPrimitiveDouble(Float value) {
        return toDouble(value);
    }

    @TypeConverter
    public static double toPrimitiveDouble(int value) {
        return toDouble(value);
    }

    @TypeConverter
    public static double toPrimitiveDouble(Integer value) {
        return toDouble(value);
    }

    @TypeConverter
    public static double toPrimitiveDouble(long value) {
        return toDouble(value);
    }

    @TypeConverter
    public static double toPrimitiveDouble(Long value) {
        return toDouble(value);
    }

    @TypeConverter
    public static double toPrimitiveDouble(short value) {
        return toDouble(value);
    }

    @TypeConverter
    public static double toPrimitiveDouble(String value) {
        return toDouble(value);
    }

    @TypeConverter
    public static double toPrimitiveDouble(Short value) {
        return toDouble(value);
    }

    @TypeConverter
    public static float toPrimitiveFloat(BigDecimal value) {
        return toFloat(value);
    }

    @TypeConverter
    public static float toPrimitiveFloat(BigInteger value) {
        return toFloat(value);
    }

    @TypeConverter
    public static float toPrimitiveFloat(boolean value) {
        return toFloat(value);
    }

    @TypeConverter
    public static float toPrimitiveFloat(Boolean value) {
        return toFloat(value);
    }

    @TypeConverter
    public static float toPrimitiveFloat(byte value) {
        return toFloat(value);
    }

    @TypeConverter
    public static float toPrimitiveFloat(Byte value) {
        return toFloat(value);
    }

    @TypeConverter
    public static float toPrimitiveFloat(char value) {
        return toFloat(value);
    }

    @TypeConverter
    public static float toPrimitiveFloat(Character value) {
        return toFloat(value);
    }

    @TypeConverter
    public static float toPrimitiveFloat(double value) {
        return toFloat(value);
    }

    @TypeConverter
    public static float toPrimitiveFloat(Double value) {
        return toFloat(value);
    }

    @TypeConverter
    public static float toPrimitiveFloat(float value) {
        return toFloat(value);
    }

    @TypeConverter
    public static float toPrimitiveFloat(Float value) {
        return toFloat(value);
    }

    @TypeConverter
    public static float toPrimitiveFloat(int value) {
        return toFloat(value);
    }

    @TypeConverter
    public static float toPrimitiveFloat(Integer value) {
        return toFloat(value);
    }

    @TypeConverter
    public static float toPrimitiveFloat(long value) {
        return toFloat(value);
    }

    @TypeConverter
    public static float toPrimitiveFloat(Long value) {
        return toFloat(value);
    }

    @TypeConverter
    public static float toPrimitiveFloat(short value) {
        return toFloat(value);
    }

    @TypeConverter
    public static float toPrimitiveFloat(String value) {
        return toFloat(value);
    }

    @TypeConverter
    public static float toPrimitiveFloat(Short value) {
        return toFloat(value);
    }

    @TypeConverter
    public static int toPrimitiveInteger(BigDecimal value) {
        return toInteger(value);
    }

    @TypeConverter
    public static int toPrimitiveInteger(BigInteger value) {
        return toInteger(value);
    }

    @TypeConverter
    public static int toPrimitiveInteger(boolean value) {
        return toInteger(value);
    }

    @TypeConverter
    public static int toPrimitiveInteger(Boolean value) {
        return toInteger(value);
    }

    @TypeConverter
    public static int toPrimitiveInteger(byte value) {
        return toInteger(value);
    }

    @TypeConverter
    public static int toPrimitiveInteger(Byte value) {
        return toInteger(value);
    }

    @TypeConverter
    public static int toPrimitiveInteger(char value) {
        return toInteger(value);
    }

    @TypeConverter
    public static int toPrimitiveInteger(Character value) {
        return toInteger(value);
    }

    @TypeConverter
    public static int toPrimitiveInteger(double value) {
        return toInteger(value);
    }

    @TypeConverter
    public static int toPrimitiveInteger(Double value) {
        return toInteger(value);
    }

    @TypeConverter
    public static int toPrimitiveInteger(float value) {
        return toInteger(value);
    }

    @TypeConverter
    public static int toPrimitiveInteger(Float value) {
        return toInteger(value);
    }

    @TypeConverter
    public static int toPrimitiveInteger(int value) {
        return toInteger(value);
    }

    @TypeConverter
    public static int toPrimitiveInteger(Integer value) {
        return toInteger(value);
    }

    @TypeConverter
    public static int toPrimitiveInteger(long value) {
        return toInteger(value);
    }

    @TypeConverter
    public static int toPrimitiveInteger(Long value) {
        return toInteger(value);
    }

    @TypeConverter
    public static int toPrimitiveInteger(String value) {
        return toInteger(value);
    }

    @TypeConverter
    public static int toPrimitiveInteger(short value) {
        return toInteger(value);
    }

    @TypeConverter
    public static int toPrimitiveInteger(Short value) {
        return toInteger(value);
    }

    @TypeConverter
    public static long toPrimitiveLong(BigDecimal value) {
        return toLong(value);
    }

    @TypeConverter
    public static long toPrimitiveLong(BigInteger value) {
        return toLong(value);
    }

    @TypeConverter
    public static long toPrimitiveLong(boolean value) {
        return toLong(value);
    }

    @TypeConverter
    public static long toPrimitiveLong(Boolean value) {
        return toLong(value);
    }

    @TypeConverter
    public static long toPrimitiveLong(byte value) {
        return toLong(value);
    }

    @TypeConverter
    public static long toPrimitiveLong(Byte value) {
        return toLong(value);
    }

    @TypeConverter
    public static long toPrimitiveLong(char value) {
        return toLong(value);
    }

    @TypeConverter
    public static long toPrimitiveLong(Character value) {
        return toLong(value);
    }

    @TypeConverter
    public static long toPrimitiveLong(double value) {
        return toLong(value);
    }

    @TypeConverter
    public static long toPrimitiveLong(Double value) {
        return toLong(value);
    }

    @TypeConverter
    public static long toPrimitiveLong(float value) {
        return toLong(value);
    }

    @TypeConverter
    public static long toPrimitiveLong(Float value) {
        return toLong(value);
    }

    @TypeConverter
    public static long toPrimitiveLong(int value) {
        return toLong(value);
    }

    @TypeConverter
    public static long toPrimitiveLong(Integer value) {
        return toLong(value);
    }

    @TypeConverter
    public static long toPrimitiveLong(long value) {
        return toLong(value);
    }

    @TypeConverter
    public static long toPrimitiveLong(Long value) {
        return toLong(value);
    }

    @TypeConverter
    public static long toPrimitiveLong(short value) {
        return toLong(value);
    }

    @TypeConverter
    public static long toPrimitiveLong(String value) {
        return toLong(value);
    }

    @TypeConverter
    public static long toPrimitiveLong(Short value) {
        return toLong(value);
    }

    @TypeConverter
    public static short toPrimitiveShort(BigDecimal value) {
        return toShort(value);
    }

    @TypeConverter
    public static short toPrimitiveShort(BigInteger value) {
        return toShort(value);
    }

    @TypeConverter
    public static short toPrimitiveShort(boolean value) {
        return toShort(value);
    }

    @TypeConverter
    public static short toPrimitiveShort(Boolean value) {
        return toShort(value);
    }

    @TypeConverter
    public static short toPrimitiveShort(byte value) {
        return toShort(value);
    }

    @TypeConverter
    public static short toPrimitiveShort(Byte value) {
        return toShort(value);
    }

    @TypeConverter
    public static short toPrimitiveShort(char value) {
        return toShort(value);
    }

    @TypeConverter
    public static short toPrimitiveShort(Character value) {
        return toShort(value);
    }

    @TypeConverter
    public static short toPrimitiveShort(double value) {
        return toShort(value);
    }

    @TypeConverter
    public static short toPrimitiveShort(Double value) {
        return toShort(value);
    }

    @TypeConverter
    public static short toPrimitiveShort(float value) {
        return toShort(value);
    }

    @TypeConverter
    public static short toPrimitiveShort(Float value) {
        return toShort(value);
    }

    @TypeConverter
    public static short toPrimitiveShort(int value) {
        return toShort(value);
    }

    @TypeConverter
    public static short toPrimitiveShort(Integer value) {
        return toShort(value);
    }

    @TypeConverter
    public static short toPrimitiveShort(long value) {
        return toShort(value);
    }

    @TypeConverter
    public static short toPrimitiveShort(Long value) {
        return toShort(value);
    }

    @TypeConverter
    public static short toPrimitiveShort(short value) {
        return toShort(value);
    }

    @TypeConverter
    public static short toPrimitiveShort(Short value) {
        return toShort(value);
    }

    @TypeConverter
    public static short toPrimitiveShort(String value) {
        return toShort(value);
    }
}
