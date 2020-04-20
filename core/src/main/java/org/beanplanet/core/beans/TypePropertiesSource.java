/*
 *  MIT Licence:
 *
 *  Copyright (C) 2020 Beanplanet Ltd
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

package org.beanplanet.core.beans;

import org.beanplanet.core.lang.TypeUtil;
import org.beanplanet.core.models.Value;

import java.beans.PropertyDescriptor;
import java.util.Optional;

public class TypePropertiesSource<T> implements Properties {
    private JavabeanMetadataCache metadataCache;

    private Class<T> type;

    public TypePropertiesSource(Class<T> type) {
        this(JavabeanMetadataCache.getSystemCache(), type);
    }

    public TypePropertiesSource(JavabeanMetadataCache metadataCache, Class<T> type) {
        this.metadataCache = metadataCache;
        this.type = type;
    }

    public Class<T> getType() {
        return type;
    }

    @Override
    public int getNumberOfProperties() {
        return getPropertyNames().length;
    }

    @Override
    public String[] getPropertyNames() {
        return getBeanMetaData().getPropertyNames();
    }

    @Override
    public boolean isReadableProperty(String name) {
        return getPropertyDescriptor(name).filter(JavaBean::isReadableProperty).isPresent();
    }

    public static boolean isReadableProperty(PropertyDescriptor propertyDescriptor) {
        return propertyDescriptor.getReadMethod() != null && propertyDescriptor.getReadMethod().getParameterCount() == 0;
    }

    @Override
    public boolean isWritableProperty(String name) {
        return getBeanMetaData().getPropertyDescriptor(name).filter(JavaBean::isWritableProperty).isPresent();
    }

    @Override
    public Value get(String name) throws PropertyNotFoundException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Value get(String name, Object defaultValue) throws PropertyNotFoundException {
       throw new UnsupportedOperationException();
    }

    @Override
    public void set(String name, Object value) throws PropertyNotFoundException {
        throw new UnsupportedOperationException();
    }

    public static boolean isWritableProperty(PropertyDescriptor propertyDescriptor) {
        return propertyDescriptor.getWriteMethod() != null
               && propertyDescriptor.getWriteMethod().getParameterCount() == 1
               && propertyDescriptor.getWriteMethod().getParameterTypes()[0].equals(propertyDescriptor.getPropertyType());
    }

    public Optional<PropertyDescriptor> getPropertyDescriptor(String name) {
        return getBeanMetaData().getPropertyDescriptor(name);
    }

    public PropertyDescriptor assertAndGetPropertyDescriptor(String name) {
        return getPropertyDescriptor(name)
                .orElseThrow(() -> new PropertyNotFoundException(String.format("No such property [%s] on type [%s]",
                                                                               name, TypeUtil.getDisplayNameForType(getType()))));
    }

    public PropertyDescriptor assertAndGetReadablePropertyDescriptor(String name) throws PropertyNotFoundException {
        return getPropertyDescriptor(name)
                .filter(JavaBean::isReadableProperty)
                .orElseThrow(() -> new PropertyNotFoundException(String.format("The property [%s] on type [%s] is no readable. Does it have a no-arg accessor/getter?",
                                                                               name, TypeUtil.getDisplayNameForType(getType()))));
    }

    public PropertyDescriptor assertAndGetWritablePropertyDescriptor(String name) throws PropertyNotFoundException {
        return getPropertyDescriptor(name)
                .filter(JavaBean::isWritableProperty)
                .orElseThrow(() -> new PropertyNotFoundException(String.format("The property [%s] on type [%s] is no writable. Does it have a single-arg accessor/setter?",
                                                                               name, TypeUtil.getDisplayNameForType(getType()))));
    }

    @Override
    public Class<?> getPropertyType(String name) throws BeanException {
        return assertAndGetPropertyDescriptor(name).getPropertyType();
    }

    private JavabeanMetadata getBeanMetaData() {
        return metadataCache.getBeanMetaData(getType(), Object.class);
    }
}
