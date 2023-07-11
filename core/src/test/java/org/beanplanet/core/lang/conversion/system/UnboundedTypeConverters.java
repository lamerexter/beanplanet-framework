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
package org.beanplanet.core.lang.conversion.system;

import org.beanplanet.core.beans.TestBean;
import org.beanplanet.core.lang.conversion.UnsupportedTypeConversionException;
import org.beanplanet.core.lang.conversion.annotations.TypeConverter;

import java.math.BigDecimal;

/**
 * Testing utility containing unbounded target type converters for tests.
 */
@TypeConverter
public final class UnboundedTypeConverters {
    @TypeConverter
    public static Object testBeanToTargetType(TestBean value, Class<?> targetType) {
        if (targetType == Integer.class) return value.getIntProperty();
        if (targetType == BigDecimal.class) return value.getBigDecimalProperty();

        throw new UnsupportedTypeConversionException("Conversion from TestBean to "+targetType+" is not supported");
    }
}
