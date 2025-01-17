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

package org.beanplanet.core.util;

import org.beanplanet.core.beans.JavaBean;
import org.beanplanet.core.models.Value;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Arrays.asList;

public class ObjectUtil {
    public static final <T> T nvl(T value, T valueIfNull) {
        return value != null ? value : valueIfNull;
    }

    public static final <T> T nvl(T value, Supplier<T> supplier) {
        return value != null ? value : supplier.get();
    }

    public static final <S, T> T nvl2(S value, Function<S, T> notNullExpr, Supplier<T> nullExpr) {
        return value != null ? notNullExpr.apply(value) : (nullExpr == null ? null : nullExpr.get());
    }

    public static final boolean equalByProperties(Object obj1, Object obj2, String ... propertyNames) {
        if (obj1 == obj2) return true;
        if (obj1 != null && obj2 == null || obj1 == null) return false;
        if (obj1.getClass() != obj2.getClass()) return false;

        JavaBean obj1Bean = new JavaBean(obj1);
        JavaBean obj2Bean = new JavaBean(obj2);
        Set<String> thisProperties = new HashSet<>(asList(obj1Bean.getPropertyNames()));
        Set<String> thatProperties = new HashSet<>(asList(obj2Bean.getPropertyNames()));
        if ( !thisProperties.equals(thatProperties) ) return false;

        Optional<Boolean> nonMatchingProperty = thisProperties
                .stream()
                .map(p -> !obj1Bean.get(p).equals(obj2Bean.get(p)))
                .findFirst();

        return !nonMatchingProperty.isPresent();
    }

    public static final int hashByProperties(Object obj, String ... propertyNames) {
        JavaBean bean = new JavaBean(obj);
        Set<String> thisProperties = new HashSet<>(asList(bean.getPropertyNames()));

        return Objects.hash(thisProperties
                                    .stream()
                                    .map(bean::get)
                                    .map(Value::getValue)
                                    .toArray(Object[]::new)
        );
    }

    @SuppressWarnings("unchecked")
    public static final int comparableByProperty(Object obj1, Object obj2, String propertyName) {
        if (obj1 == obj2) return 0;
        if (obj1 != null && obj2 == null) return 1;
        if (obj1 == null) return -1;

        if (obj1.getClass() != obj2.getClass()) return 0;

        JavaBean obj1Bean = new JavaBean(obj1);
        JavaBean obj2Bean = new JavaBean(obj2);
        if ( !obj1Bean.isReadableProperty(propertyName)
                || !Comparable.class.isAssignableFrom(obj1Bean.getPropertyType(propertyName)) ) {
            return 0;
        }

        Comparable<Object> obj1ComparableValue = (Comparable)obj1Bean.get(propertyName).getValue();
        Comparable<Object> obj2ComparableValue = (Comparable)obj2Bean.get(propertyName).getValue();
        if ( obj1ComparableValue == obj2ComparableValue ) return 0;
        if (obj1ComparableValue == null && obj2ComparableValue != null) return -1;
        if (obj1ComparableValue != null && obj2ComparableValue == null) return 1;
        return obj1ComparableValue.compareTo(obj2ComparableValue);
    }

    /**
     * Returns the first non-null value supplied from a list.
     *
     * @param suppliers the suppliers of possible non-null values.
     * @return the first non-null value supplied, or null indicating all that were supplied were null.
     */
    @SafeVarargs
    public static <T> T firstNonNull(Supplier<T> ... suppliers) {
        return suppliers == null ? null : Arrays.stream(suppliers)
                                                .map(Supplier::get)
                                                .filter(Objects::nonNull).findFirst().orElse(null);
    }
}
