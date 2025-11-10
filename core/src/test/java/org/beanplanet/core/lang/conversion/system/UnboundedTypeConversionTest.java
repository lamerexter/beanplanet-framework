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

import org.beanplanet.core.beans.JavaBean;
import org.beanplanet.core.beans.TestBean;
import org.beanplanet.core.lang.conversion.UnsupportedTypeConversionException;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.beanplanet.core.lang.conversion.SystemTypeConverter.systemTypeConverter;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class UnboundedTypeConversionTest {
    @Test
    public void unboundedTypeConversion_successful() {
        assertThat(systemTypeConverter().convert(new JavaBean<>(new TestBean()).with("intProperty", 12345).getBean(), Integer.class), equalTo(12345));
        assertThat(systemTypeConverter().convert(new JavaBean<>(new TestBean()).with("bigDecimalProperty", BigDecimal.TEN).getBean(), BigDecimal.class), equalTo(BigDecimal.TEN));
    }

    @Test(expected = UnsupportedTypeConversionException.class)
    public void unboundedTypeConversion_unsuccessful() {
        systemTypeConverter().convert(new TestBean(), InetAddress.class);
    }
}