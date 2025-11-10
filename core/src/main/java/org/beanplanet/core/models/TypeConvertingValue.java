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

package org.beanplanet.core.models;

import org.beanplanet.core.dao.DataAccessException;
import org.beanplanet.core.lang.conversion.TypeConverter;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Default implementation of a held value. This implementation is capable of converting held values to the types
 * required by clients, where possible, using a configured <code>{@link TypeConverter}</code>.
 *
 * @author Gary Watson
 */
public class TypeConvertingValue implements Value {
    /** The type converter used by this instance to convert its value to the types required by clients. */
    protected TypeConverter typeConverter;

    protected Supplier<Object> valueProvider;
    protected Supplier<Object> defaultValueProvider;

    public TypeConvertingValue(TypeConverter typeConverter, Object value) {
        this(typeConverter, () -> value, null);
    }

    public TypeConvertingValue(TypeConverter typeConverter, Object value, Object defaultValue) {
        this(typeConverter, () -> value, () -> defaultValue);
    }

    public TypeConvertingValue(TypeConverter typeConverter, Supplier<Object> valueProvider) {
        this(typeConverter, valueProvider, null);
    }

    public TypeConvertingValue(TypeConverter typeConverter, Supplier<Object> valueProvider, Supplier<Object> defaultValueProvider) {
        this.typeConverter = typeConverter;
        this.valueProvider = valueProvider;
        this.defaultValueProvider = defaultValueProvider;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Value)) {
            return false;
        }

        Value that = (Value) o;

        return Objects.equals(getValue(), that.getValue());
    }

    public int hashCode() {
        return Objects.hashCode(getValue());
    }

    public TypeConverter getTypeConverter() {
        return typeConverter;
    }

    /**
     * Returns directly the underlying value.
     *
     * @return the value, which may be null.
     * @see #as(Class)
     */
    public Object getValue() {
        return valueProvider.get();
    }

    public Object getDefaultValue() {
        return defaultValueProvider.get();
    }

    public boolean getHasDefaultValue() {
        return defaultValueProvider != null;
    }

    /**
     * Returns the value of the result, as the type specified.
     *
     * @param targetType the type of the value required; if the value is not an instance of this type an attempt to convert the
     * value to this type will be made, depending on the implementation.
     * @return null if the result was null, or the value of the result, converted to the specified type, if required.
     * @exception DataAccessException thrown if an error occurs during conversion of the result to the required type.
     */
    public <T> T as(Class<T> targetType) throws DataAccessException {
        final Object value = valueProvider.get();
        return value == null ? null : typeConverter.convert(value, targetType);
    }

    /**
     * Returns the value of the result as a new collection of a given type, whose element values are of the type specified.
     *
     * @param supplier a supplier of the collection with which to associate the value's values.
     * @param elementType the type of the elements of the new collection created. How the value's elements are checked
     *                    for conformance with the given element type is implementation specific. For example, one
     *                    implementation might simply assert all elements of the collection are instances of the given
     *                    type, while another implementation might convert elements to the desired type.
     * @return the value of the result, which could be null.
     * @exception DataAccessException thrown if an error occurs accessing or converting the value.
     */
    public <T, C extends Collection<T>> C asCollectionTypeOf(Supplier<C> supplier, Class<T> elementType) throws DataAccessException {
        final Object value = valueProvider.get();
        if (value == null) return null;

        Collection<?> convertedValue = typeConverter.convert(value, Collection.class);
        return convertedValue.stream()
                .map(e -> typeConverter.convert(e, elementType))
                .collect(Collectors.toCollection(supplier));
    }

    /**
     * Returns the value of the result as a map of the given key and value types.
     *
     * @param supplier the supplier of the map to which value entries are to be added.
     * @param keyType the type of the keys of the map.
     * @param valueType the type of the values of the map.
     * @return the value of the result, which could be null.
     */
    public <K, V> Map<K, V> asMapTypeOf(Supplier<Map<K, V>> supplier, Class<K> keyType, Class<V> valueType) {
        final Object value = valueProvider.get();
        if (value == null) return null;

        Map<K, V> newMap = supplier.get();
        Map<?, ?> convertedValue = typeConverter.convert(value, Map.class);
        convertedValue.entrySet()
                .forEach(e -> newMap.put(typeConverter.convert(e.getKey(), keyType),
                                         typeConverter.convert(e.getValue(), valueType)));
        return newMap;
    }
}
