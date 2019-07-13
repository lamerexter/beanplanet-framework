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

package org.beanplanet.core;

import org.beanplanet.core.beans.Bean;
import org.beanplanet.core.beans.JavaBean;
import org.beanplanet.core.events.PropertyChangeEvent;
import org.beanplanet.core.events.PropertyChangeEventSource;
import org.beanplanet.core.events.PropertyChangeListener;
import org.beanplanet.core.lang.TypeUtil;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.beanplanet.core.lang.TypeUtil.isPrimitiveType;
import static org.beanplanet.core.util.ArrayUtil.nullSafe;

/**
 * A useful support class for testing readable and writable properties on beans.
 *
 * @author Gary Watson
 */
@SuppressWarnings("unchecked")
public final class BeanTestSupport {
    public static interface PropertyValueGenerator<T> {
        T generateValue(Bean bean, String propertyName);
    }

    public static interface ValueGenerator<T> {
        T generateValue(Class<?> valueClass);
    }

    private static final Map<Class<?>, ValueGenerator<?>> propertyValueGenerators = new HashMap<Class<?>, ValueGenerator<?>>();

    private static final Map<Predicate<Class<?>>, ValueGenerator<?>> DEFAULT_PREDICATED_VALUE_GENERATORS = new LinkedHashMap<>();

    private final Map<Predicate<Class<?>>, ValueGenerator<?>> predicatedPropertyValueGenerators = new LinkedHashMap<Predicate<Class<?>>, ValueGenerator<?>>(DEFAULT_PREDICATED_VALUE_GENERATORS);

    private final Set<String> excludedPropertyNames = new LinkedHashSet<String>();

    private final Set<String> excludedToStringPropertyNames = new LinkedHashSet<String>();

    private final Set<String> excludedBuilderMethodNames = new LinkedHashSet<String>();

    private boolean testWithNullValues = true;

    private Object instance;

    static {

    }

    static {
        propertyValueGenerators.put(Boolean.class, valueClass -> Boolean.TRUE);
        propertyValueGenerators.put(boolean.class, valueClass -> Boolean.TRUE);
        propertyValueGenerators.put(CharSequence.class, valueClass -> UUID.randomUUID().toString());
        propertyValueGenerators.put(Date.class, valueClass -> new Date());
        propertyValueGenerators.put(Double.class, valueClass -> Math.random() * Double.MAX_VALUE);
        propertyValueGenerators.put(Float.class, valueClass -> (float) (Math.random() * Float.MAX_VALUE));
        propertyValueGenerators.put(int.class, valueClass -> (int) (Math.random() * Integer.MAX_VALUE));
        propertyValueGenerators.put(Integer.class, valueClass -> (int) (Math.random() * Integer.MAX_VALUE));
        propertyValueGenerators.put(List.class, valueClass -> Collections.emptyList());
        propertyValueGenerators.put(LocalDate.class, valueClass -> LocalDate.now(ZoneId.of("UTC")));
        propertyValueGenerators.put(LocalTime.class, valueClass -> LocalTime.now(ZoneId.of("UTC")));
        propertyValueGenerators.put(LocalDateTime.class, valueClass -> LocalDateTime.now(ZoneId.of("UTC")));
        propertyValueGenerators.put(Long.class, valueClass -> (long) (Math.random() * Long.MAX_VALUE));
        propertyValueGenerators.put(long.class, valueClass -> (long)(Math.random() * Long.MAX_VALUE));
        propertyValueGenerators.put(Set.class, valueClass -> Collections.emptySet());
        propertyValueGenerators.put(String.class, valueClass -> UUID.randomUUID().toString());

        // Array of basic types (primitive, character-sequence and class)
        DEFAULT_PREDICATED_VALUE_GENERATORS.put(clazz -> clazz.isArray() && (TypeUtil.isPrimitiveTypeOrWrapperClass(clazz) || CharSequence.class.isAssignableFrom(clazz.getComponentType())),
                                                valueClass -> {
                                                    Object element = propertyValueGenerators.get(valueClass.getComponentType()).generateValue(valueClass);
                                                    Object array = Array.newInstance(valueClass.getComponentType(), 1);
                                                    Array.set(array, 0, element);
                                                    return array;
                                                });

        DEFAULT_PREDICATED_VALUE_GENERATORS.put(clazz -> clazz.isEnum() && clazz.getEnumConstants().length > 0,
                                                valueClass -> {
                                                    Class<? extends Enum> enumClass = (Class<? extends Enum>)valueClass;
                                                    return enumClass.getEnumConstants()[(int)Math.floor(Math.random() * ((Class<? extends Enum>)valueClass).getEnumConstants().length)];
                                                });
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

    public final BeanTestSupport testPropertyChangeEvents() {
        return testPropertyChangeEvents(instance);
    }

    public final BeanTestSupport testPropertyChangeEvents(Object instance) {
        return testPropertyChangeEvents(instance, new JavaBean(instance).getPropertyNames());
    }

    public final BeanTestSupport testPropertyChangeEvents(Object instance, String... propertyNames) {
        if ( !(instance instanceof PropertyChangeEventSource) ) return this;
        PropertyChangeEventSource eventSource = (PropertyChangeEventSource) instance;
        final PropertyChangeEvent callEvent[] = { null };
        PropertyChangeListener changeListener = ev -> {
            callEvent[0] = ev;
        };
        eventSource.addPropertyChangeListener(changeListener);

        try {
            Bean bean = new JavaBean(eventSource);
            for (String propertyName : nullSafe(String.class, propertyNames)) {
                if (bean.isReadableProperty(propertyName) && bean.isWritableProperty(propertyName)) {
                    callEvent[0] = null; // reset
                    Object oldValue = bean.get(propertyName).getValue();

                    Object value = generatePropertyValue(bean, propertyName);
                    bean.set(propertyName, value);

                    assertTrue(callEvent[0] != null,
                               () -> "The property change tests on bean [type=%s] failed on property [%s]. No property change event was received. Check the event source is properly throwing events?",
                               bean.getBean().getClass().getName(), propertyName, value);
                    assertTrue( Objects.equals(propertyName, callEvent[0].getPropertyName()),
                                () -> "The property name [%s] in the property change event from bean [type=%s] did not match the expected name [%s]",
                                callEvent[0].getPropertyName(), bean.getBean().getClass().getName(), propertyName);
                    assertTrue( Objects.equals(oldValue, callEvent[0].getOldValue()),
                                () -> "The old property value [%s=%s] in the property change event from bean [type=%s] did not match the expected value [%s]",
                                propertyName, callEvent[0].getOldValue(), bean.getBean().getClass().getName(), oldValue);
                    assertTrue( Objects.equals(value, callEvent[0].getNewValue()),
                                () -> "The new property value [%s=%s] in the property change event from bean [type=%s] did not match the expected value [%s]",
                                propertyName, callEvent[0].getNewValue(), bean.getBean().getClass().getName(), value);
                }
            }
        } finally {
            eventSource.removePropertyChangeListener(changeListener);
        }

        return this;
    }

    private void assertTrue(boolean result, Supplier<String> messageSupplier, Object ... messageArgs) {
        if ( !(result) ) {
            String message = messageSupplier.get();
            throw new AssertionError(String.format(message, messageArgs));
        }
    }

    private void assertFalse(boolean result, Supplier<String> messageSupplier, Object ... messageArgs) {
        if ( result ) {
            String message = messageSupplier.get();
            throw new AssertionError(String.format(message, messageArgs));
        }
    }

    public final BeanTestSupport testBuilderProperties() {
        testBuilderProperties(instance);
        return this;
    }

    public final BeanTestSupport testBuilderProperties(Object instance) {
        return testBuilderProperties(
                instance,
                TypeUtil.getMethodsInClassHierarchy(instance.getClass())
                        .filter(m -> m.getName().startsWith("with") && m.getParameterTypes() != null && m.getParameterTypes().length == 1)
        );
    }

    public final BeanTestSupport testBuilderProperties(final Object instance, String... methodNames) {
        Set<String> methodNamesSet = new LinkedHashSet<>(asList(methodNames));
        return testBuilderProperties(
                instance,
                TypeUtil.getMethodsInClassHierarchy(instance.getClass())
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

    public final BeanTestSupport testEqualsWithProperties(Supplier<?> instanceSupplier) {
        return testEqualsWithProperties(instanceSupplier, new JavaBean(instanceSupplier.get()).getPropertyNames());
    }

    public final <T> BeanTestSupport testEqualsWithProperties(Supplier<T> instanceSupplier, String ... propertyNames) {
        return testEqualsWithProperties(instanceSupplier, null, propertyNames);
    }

    public final <T> BeanTestSupport testEqualsWithProperties(Supplier<T> instanceSupplier, Consumer<T> mutator, String ... propertyNames) {
        //--------------------------------------------------------------------------------------------------------------
        // Affirmative case: same instance equals itself
        //--------------------------------------------------------------------------------------------------------------
        T instance1 = instanceSupplier.get();
        assertTrue(instance1.equals(instance1),
                   () -> "The equals() method of bean [type=%s] failed for the same instance",
                   instance1.getClass());

        //--------------------------------------------------------------------------------------------------------------
        // Affirmative case: two instances with the same property values are equal
        //--------------------------------------------------------------------------------------------------------------
        T instance2 = instanceSupplier.get();
        Bean bean1 = new JavaBean(instance1);
        Bean bean2 = new JavaBean(instance2);

        Arrays.stream(propertyNames)
              .forEach(p -> {
                  ValueGenerator<?> propertyValueGenerator = determinePropertyValueGenerator(bean1.getPropertyType(p));
                  Object propertyValue = propertyValueGenerator.generateValue(bean1.getPropertyType(p));
                  bean1.set(p, propertyValue);
                  bean2.set(p, propertyValue);
              });
        assertTrue(instance1.equals(instance2),
                   () -> "The equals() method of bean [type=%s] failed for the given properties [%s]",
                   instance1.getClass(), Arrays.asList(propertyNames));

        //--------------------------------------------------------------------------------------------------------------
        // Negative case: not equal to another type
        //--------------------------------------------------------------------------------------------------------------
        assertFalse(instance1.equals(new Object()),
                    () -> "The equals() method of type [type=%s] passed unexpectedly when compared to another distinct type [Object]",
                    instance1.getClass());


        //--------------------------------------------------------------------------------------------------------------
        // Negative case: two instances are not equal
        //--------------------------------------------------------------------------------------------------------------
        if (mutator != null) {
            mutator.accept(instance2);
            assertFalse(instance1.equals(instance2),
                        () -> "The equals() method of type [type=%s] passed unexpectedly when second instance was mutated [instance1=\n%s\n, instance2=\n%s\n]",
                        instance1.getClass(), instance1, instance2);

        }
        // GAW: 2019-01-14: Can't test the negative case until we have a way to 'toggle' a value to be different!
        // e.g. instance1.isMale=true  so instance2.isMale=false  MUST be the case!
//        if ( propertyNames != null && propertyNames.length > 0 ) {
//            int randonProperty = (int)Math.floor(Math.random() * propertyNames.length);
//            String randomPropertyName = propertyNames[randonProperty];
//            ValueGenerator<?> propertyValueGenerator = determinePropertyValueGenerator(bean1.getPropertyType(randomPropertyName));
//            Object propertyValue = propertyValueGenerator.generateValue(bean1.getPropertyType(randomPropertyName));
//            bean2.set(randomPropertyName, propertyValue);
//
//            assertFalse(instance1.equals(instance2),
//                    () -> "The equals() methjod of bean [type=%s] passed unexpectedly for differing property [%s]",
//                    instance1.getClass(), randomPropertyName);
//        }
        return this;
    }

    /**
     * Test that the hashcode  of new instance of an object is equivalent to the hash of a number of properties,
     * equivalent to a call to <i>Objects.hash(propertyValues[]</i>, where <i>propertyValues[]</i> are the values of
     * the properties set in order they were specified. That is, the hashcode of an object is equivalent to the hash of
     * the properties specified, in order - which is the common was <i>hashCode</i> methods are written.
     *
     * @param instanceSupplier a supplier of new instances of the object to be hashed.
     * @param propertyNames the names of the properties to set and hash, in order.
     * @return this BeanTestSupport object, to facilitate method chaining.
     */
    public final BeanTestSupport testObjectsHashcodeWithProperties(Supplier<?> instanceSupplier, String ... propertyNames) {
        //--------------------------------------------------------------------------------------------------------------
        // Affirmative case: two instances are equal
        //--------------------------------------------------------------------------------------------------------------
        Object instance = instanceSupplier.get();
        Bean bean = new JavaBean(instance);
        List<Object> hashValues = new ArrayList<>(propertyNames == null ? 0 : propertyNames.length);

        Arrays.stream(propertyNames)
              .forEach(p -> {
                  ValueGenerator<?> propertyValueGenerator = determinePropertyValueGenerator(bean.getPropertyType(p));
                  Object propertyValue = propertyValueGenerator.generateValue(bean.getPropertyType(p));
                  bean.set(p, propertyValue);
                  hashValues.add(propertyValue);
              });
        assertTrue(instance.hashCode() == Arrays.hashCode(hashValues.toArray()),
                   () -> "The hashCode() method of bean [type=%s] failed for the given properties [%s] and values [%s]",
                   instance.getClass(), Arrays.asList(propertyNames), hashValues);
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
                context.getConstructor((Class[])null);
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
            throw new AssertionError(String.format("The property get/set tests on bean [type=%s] failed on property [%s]. Set value [%s] did not match retrieved value [%s]", bean.getBean().getClass().getName(), propertyName, value, retrievedPropertyValue));
        }

        if (testWithNullValues && !isPrimitiveType(bean.getPropertyType(propertyName))) {
            bean.set(propertyName, null);
            retrievedPropertyValue = bean.get(propertyName).getValue();
            if (retrievedPropertyValue != null) {
                throw new AssertionError(String.format("The property get/set tests on bean [type=%s] failed on property [%s]. Set null value did not match retrieved value [%s]", bean.getBean().getClass().getName(), propertyName, retrievedPropertyValue));
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
            throw new IllegalStateException(String.format("No property value generator registered for property [%s] type [%s] of bean type [%s]", propertyName, propertyType, bean.getBean().getClass().getName()));
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

    public static void testEnum(Class<? extends Enum> enumClass) {
        for (int n=0; n < enumClass.getEnumConstants().length; n++) {
            enumClass.getEnumConstants()[n].name();
        }
    }
}
