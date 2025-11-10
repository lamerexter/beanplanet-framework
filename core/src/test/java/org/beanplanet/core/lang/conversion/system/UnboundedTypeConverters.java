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
