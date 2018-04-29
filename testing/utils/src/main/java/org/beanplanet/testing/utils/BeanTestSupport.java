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

package org.beanplanet.testing.utils;

import org.beanplanet.core.beans.Bean;
import org.beanplanet.core.beans.JavaBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static org.beanplanet.core.lang.TypeUtil.isPrimitiveType;
import static org.beanplanet.core.util.ArrayUtil.nullSafe;

/**
 * A useful support class for testing readable and writable properties on beans.
 *
 * @author Gary Watson
 */
public final class BeanTestSupport {
    public static interface PropertyValueGenerator<T> {
        T generateValue(Bean bean, String propertyName);
    }

    public static interface ValueGenerator<T> {
        T generateValue(Class<?> valueClass);
    }

    private static final Map<Class<?>, ValueGenerator<?>> propertyValueGenerators = new HashMap<Class<?>, ValueGenerator<?>>();

    private static final Map<Predicate<Class<?>>, ValueGenerator<?>> DEFAULT_PREDICATED_VALUE_GENERATORS = new HashMap<Predicate<Class<?>>, ValueGenerator<?>>();

    private final Map<Predicate<Class<?>>, ValueGenerator<?>> predicatedPropertyValueGenerators = new HashMap<Predicate<Class<?>>, ValueGenerator<?>>(DEFAULT_PREDICATED_VALUE_GENERATORS);

    private final Set<String> excludedPropertyNames = new LinkedHashSet<String>();

    private final Set<String> excludedToStringPropertyNames = new LinkedHashSet<String>();

    private final Set<String> excludedBuilderMethodNames = new LinkedHashSet<String>();

    private boolean testWithNullValues = true;

    private Object instance;

    static {

    }

    static {
        propertyValueGenerators.put(String.class, valueClass -> "thePropertyValue");
        propertyValueGenerators.put(Boolean.class, valueClass -> Boolean.TRUE);
        propertyValueGenerators.put(boolean.class, valueClass -> Boolean.TRUE);
        propertyValueGenerators.put(Date.class, valueClass -> new Date());
        propertyValueGenerators.put(Double.class, valueClass -> Math.random() * Double.MAX_VALUE);
        propertyValueGenerators.put(Float.class, valueClass -> (float) (Math.random() * Float.MAX_VALUE));
        propertyValueGenerators.put(int.class, valueClass -> (int) (Math.random() * Integer.MAX_VALUE));
        propertyValueGenerators.put(Integer.class, valueClass -> (int) (Math.random() * Integer.MAX_VALUE));
        propertyValueGenerators.put(List.class, valueClass -> Collections.emptyList());
        propertyValueGenerators.put(Long.class, valueClass -> (long) (Math.random() * Long.MAX_VALUE));
        propertyValueGenerators.put(long.class, valueClass -> (long)(Math.random() * Long.MAX_VALUE));
        propertyValueGenerators.put(Set.class, valueClass -> Collections.emptySet());
        propertyValueGenerators.put(LocalDate.class, valueClass -> LocalDate.now(ZoneId.of("UTC")));
        propertyValueGenerators.put(LocalTime.class, valueClass -> LocalTime.now(ZoneId.of("UTC")));
        propertyValueGenerators.put(LocalDateTime.class, valueClass -> LocalDateTime.now(ZoneId.of("UTC")));

        DEFAULT_PREDICATED_VALUE_GENERATORS.put(clazz -> clazz.isEnum() && clazz.getEnumConstants().length > 0, valueClass -> valueClass.getEnumConstants()[0]);
    }

    public BeanTestSupport() {
    }

    public BeanTestSupport(Object instance) {
        this.instance = instance;
    }

    public final BeanTestSupport testProperties() {
        testProperties(instance);
        return this;
    }

    public final BeanTestSupport testProperties(Object instance) {
        testProperties(instance, new JavaBean(instance).getPropertyNames());
        return this;
    }


    public final BeanTestSupport testProperties(Object instance, String... propertyNames) {
        Bean bean = new JavaBean(instance);
        for (String propertyName : nullSafe(String.class, propertyNames)) {
            if (excludedPropertyNames.contains(propertyName)) continue;

            if (bean.isReadableProperty(propertyName) && bean.isWritableProperty(propertyName)) {
                assertPropertyReadableAndWritable(bean, propertyName);
            } else if (bean.isReadableProperty(propertyName)) {
                assertPropertyReadable(bean, propertyName);
            } else if (bean.isWritableProperty(propertyName)) {
                assertPropertyWritable(bean, propertyName);
            }
        }

        return this;
    }

    public final BeanTestSupport testBuilderProperties() {
        testBuilderProperties(instance);
        return this;
    }

    public final BeanTestSupport testBuilderProperties(Object instance) {
        return testBuilderProperties(
                instance,
                stream(instance.getClass().getDeclaredMethods())
                .filter(m -> m.getName().startsWith("with") && m.getParameterTypes() != null && m.getParameterTypes().length == 1)
        );
    }

    public final BeanTestSupport testBuilderProperties(final Object instance, String... methodNames) {
        Set<String> methodNamesSet = new LinkedHashSet<>(asList(methodNames));
        return testBuilderProperties(
                instance,
                stream(instance.getClass().getDeclaredMethods())
                        .filter(m -> methodNamesSet.contains(m.getName()))
                        .filter(m -> m.getParameterTypes() != null && m.getParameterTypes().length == 1)
        );
    }

    private final BeanTestSupport testBuilderProperties(final Object instance, Stream<Method> builderMethods) {
        builderMethods.forEach( method -> {
            Class<?> valueClass = method.getParameterTypes()[0];
            ValueGenerator<?> propertyValueGenerator = determinePropertyValueGenerator(valueClass);
            if (propertyValueGenerator == null) {
                throw new IllegalStateException(String.format("No property value generator registered for builder property [%s] type [%s] of instance type [%s]", method.getName(), valueClass, instance.getClass().getName()));
            }
            Object value = propertyValueGenerator.generateValue(valueClass);

            Object returnValue;
            try {
                returnValue = method.invoke(instance, new Object[]{value});
            } catch (Exception e) {
                throw new AssertionError(e);
            }

            if (returnValue == null || returnValue.getClass() != instance.getClass()) {
                throw new AssertionError(String.format("The builder method method [%s] on instance [%s] returned incorrect value type [%s] when expecting the instance", method.getName(), instance.getClass(), (returnValue != null ? returnValue.getClass() : null)));
            }
        });
        return this;
    }

    public final BeanTestSupport testToString() {
        testToString(instance);
        return this;
    }

    public final BeanTestSupport testToString(Object instance) {
        String toString = instance.toString();

        if (toString == null || toString.trim().length() == 0) {
            throw new AssertionError(String.format("The toString() of type %s was null or empty", instance.getClass().getName()));
        }
        return this;
    }

    /**
     * Simple property name-value pair checker for the toString() method of the object
     * under test that looks for the presence of "name=value" or "name=<value>" in the toString() output.
     *
     * Excluded properties are supported.
     *
     * @param propertyNvp   String array of property names and values, name1,  value1, name2, value3, name3, value3...
     * @return              this BeanTestSupport object, to facilitate method chaining.
     */
    public final BeanTestSupport testToStringProperties(String... propertyNvp) {

        if ((propertyNvp.length % 2) != 0 || propertyNvp.length == 0)
            throw new IllegalArgumentException(
                    "You must supply at least one property to check in the toString(), and each property must be followed by its value");

        String toString = instance.toString();

        if (excludedToStringPropertyNames != null) {
            for (String name : excludedToStringPropertyNames) {
                if (toString.contains(name + "=")) {
                    throw new AssertionError(String.format("The toString() of type %s contained excluded property [%s]", instance.getClass().getName(), name));
                }
            }
        }

        for (int i = 0; i < propertyNvp.length - 1; i = i + 2) {
            String property = String.valueOf(propertyNvp[i]);
            String value = propertyNvp[i + 1] == null ? null : String.valueOf(propertyNvp[i + 1]);

            if (!toString.contains(property + "=" + value) && !toString.contains(property + "=<" + value + ">")) {
                throw new AssertionError(String.format("The toString() of type %s did not contain property [%s] with value [%s]", instance.getClass().getName(), property, value));
            }
        }

        return this;
    }
    public BeanTestSupport withNoArgConstructorValuesGenerator() {
        predicatedPropertyValueGenerators.put(context -> {
            try {
                context.getConstructor(null);
                return true;
            } catch (NoSuchMethodException nsmEx) {
                return false;
            }
        }, valueClass -> {
            try {
                return valueClass.newInstance();
            } catch (InstantiationException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        });

        return this;
    }

    public <T> BeanTestSupport withValueGenerator(Class<T> clazz, ValueGenerator<T> generator) {
        propertyValueGenerators.put(clazz, generator);
        return this;
    }

    public BeanTestSupport withMockValuesGenerator() {
        predicatedPropertyValueGenerators.put(context -> context.isInterface(), new ValueGenerator<Object>() {
            public Object generateValue(Class<?> valueClass) {
                return Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{valueClass}, new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        return null;
                    }
                });
            }
        });

        return this;
    }

    public BeanTestSupport withMockitoValuesGenerator() {
        predicatedPropertyValueGenerators.put(context -> {
            // Dynamically invoke if on classpath ONLY
            try {
                Class.forName("org.mockito.Mockito");
                return true;
            } catch (ClassNotFoundException cnfEx) {
                return false;
            }
        }, valueClass -> {
            try {
                Class<?> mockitoClass = Class.forName("org.mockito.Mockito");

                Method mockMethod = mockitoClass.getMethod("mock", Class.class);

                return mockMethod.invoke(null, valueClass);
            } catch (Exception ex) {
                throw new RuntimeException("Unable to use Mockito to generate mocks: ", ex);
            }
        });

        return this;
    }

    public BeanTestSupport withExcludedProperties(String... propertyNames) {
        if (propertyNames != null) {
            excludedPropertyNames.addAll(asList(propertyNames));
        }
        return this;
    }

    public BeanTestSupport withExcludedToStringProperties(String... propertyNames) {
        if (propertyNames != null) {
            excludedToStringPropertyNames.addAll(asList(propertyNames));
        }
        return this;
    }

    public BeanTestSupport withExcludedBuilderMethodNames(String... methodNames) {
        if (methodNames != null) {
            excludedBuilderMethodNames.addAll(asList(methodNames));
        }
        return this;
    }

    public BeanTestSupport withTestWithNullValues(Boolean testWithNullValues) {
        this.testWithNullValues = testWithNullValues;
        return this;
    }

    private void assertPropertyReadableAndWritable(Bean bean, String propertyName) {
        Object value = generatePropertyValue(bean, propertyName);
        bean.set(propertyName, value);
        Object retrievedPropertyValue = bean.get(propertyName).getValue();
        if (!Objects.equals(value, retrievedPropertyValue)) {
            throw new AssertionError(String.format("The property get/set tests on bean [type=%s] failed on property %s. Set value [%s] did not match retrieved value [%s]", bean.getWrappedInstance().getClass().getName(), propertyName, value, retrievedPropertyValue));
        }

        if (testWithNullValues && !isPrimitiveType(bean.getPropertyType(propertyName))) {
            bean.set(propertyName, null);
            retrievedPropertyValue = bean.get(propertyName).getValue();
            if (retrievedPropertyValue != null) {
                throw new AssertionError(String.format("The property get/set tests on bean [type=%s] failed on property %s. Set null value did not match retrieved value [%s]", bean.getWrappedInstance().getClass().getName(), propertyName, retrievedPropertyValue));
            }
        }
    }

    private void assertPropertyReadable(Bean bean, String propertyName) {
        // Just call it ...
        bean.get(propertyName);
    }

    private void assertPropertyWritable(Bean bean, String propertyName) {
        // Just call it ...
        bean.set(propertyName, generatePropertyValue(bean, propertyName));
    }

    private Object generatePropertyValue(Bean bean, String propertyName) {
        Class<?> propertyType = bean.getPropertyType(propertyName);
        ValueGenerator<?> propertyValueGenerator = determinePropertyValueGenerator(propertyType);
        if (propertyValueGenerator == null) {
            throw new IllegalStateException(String.format("No property value generator registered for property [%s] type [%s] of bean type [%s]", propertyName, propertyType, bean.getWrappedInstance().getClass().getName()));
        }
        return propertyValueGenerator.generateValue(propertyType);
    }

    @SuppressWarnings("unchecked")
    private <T> ValueGenerator<T> determinePropertyValueGenerator(Class<?> propertyType) {
        ValueGenerator<?> propertyValueGenerator = propertyValueGenerators.get(propertyType);
        if (propertyValueGenerator != null) return (ValueGenerator<T>) propertyValueGenerator;

        for (Map.Entry<Predicate<Class<?>>, ValueGenerator<?>> entry : predicatedPropertyValueGenerators.entrySet()) {
            if (entry.getKey().test(propertyType)) {
                return (ValueGenerator<T>) entry.getValue();
            }
        }

        return null;
    }
}
