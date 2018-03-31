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

    protected Object value;
    protected Object defaultValue;
    protected boolean hasDefaultValue = false;

    public TypeConvertingValue(TypeConverter typeConverter, Object value) {
        this.typeConverter = typeConverter;
        this.value = value;
    }

    public TypeConvertingValue(TypeConverter typeConverter, Object value, Object defaultValue) {
        this.typeConverter = typeConverter;
        this.value = value;
        this.defaultValue = defaultValue;
        this.hasDefaultValue = true;
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
        return value;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public boolean getHasDefaultValue() {
        return hasDefaultValue;
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
        if (value == null) return null;

        Map<K, V> newMap = supplier.get();
        Map<?, ?> convertedValue = typeConverter.convert(value, Map.class);
        convertedValue.entrySet()
                .forEach(e -> newMap.put(typeConverter.convert(e.getKey(), keyType),
                                         typeConverter.convert(e.getValue(), valueType)));
        return newMap;
    }
}
