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
