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
import org.beanplanet.core.lang.conversion.TypeConverter;
import org.beanplanet.core.models.TypeConvertingValue;
import org.beanplanet.core.models.Value;

import java.beans.PropertyDescriptor;
import java.util.Optional;

import static org.beanplanet.core.lang.conversion.SystemTypeConverter.systemTypeConverter;

public class JavaBean<T> implements Bean {
    private JavabeanMetadataCache metadataCache;

    private T bean;

    private TypeConverter typeConverter;

    public JavaBean(T bean) {
        this(JavabeanMetadataCache.getSystemCache(), systemTypeConverter(), bean);
    }

    public JavaBean(JavabeanMetadataCache metadataCache, T bean) {
        this(metadataCache, systemTypeConverter(), bean);
    }

    public JavaBean(TypeConverter typeConverter, T bean) {
        this(JavabeanMetadataCache.getSystemCache(), typeConverter, bean);
    }

    public JavaBean(JavabeanMetadataCache metadataCache, TypeConverter typeConverter, T bean) {
        this.metadataCache = metadataCache;
        this.typeConverter = typeConverter;
        this.bean = bean;
    }

    public T getBean() {
        return bean;
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

    public static boolean isWritableProperty(PropertyDescriptor propertyDescriptor) {
        return propertyDescriptor.getWriteMethod() != null
               && propertyDescriptor.getWriteMethod().getParameterCount() == 1
               && propertyDescriptor.getWriteMethod().getParameterTypes()[0].equals(propertyDescriptor.getPropertyType());
    }

    private Optional<PropertyDescriptor> getPropertyDescriptor(String name) {
        return getBeanMetaData().getPropertyDescriptor(name);
    }

    private PropertyDescriptor assertAndGetPropertyDescriptor(String name) {
        return getPropertyDescriptor(name)
                .orElseThrow(() -> new PropertyNotFoundException(String.format("No such property [%s] on bean [%s]",
                                                                               name, TypeUtil.getDisplayNameForType(bean.getClass()))));
    }

    private PropertyDescriptor assertAndGetReadablePropertyDescriptor(String name) throws PropertyNotFoundException {
        return getPropertyDescriptor(name)
                .filter(JavaBean::isReadableProperty)
                .orElseThrow(() -> new PropertyNotFoundException(String.format("The property [%s] on bean [%s] is no readable. Does it have a no-arg accessor/getter?",
                                                                               name, TypeUtil.getDisplayNameForType(bean.getClass()))));
    }

    private PropertyDescriptor assertAndGetWritablePropertyDescriptor(String name) throws PropertyNotFoundException {
        return getPropertyDescriptor(name)
                .filter(JavaBean::isWritableProperty)
                .orElseThrow(() -> new PropertyNotFoundException(String.format("The property [%s] on bean [%s] is no writable. Does it have a single-arg accessor/setter?",
                                                                               name, TypeUtil.getDisplayNameForType(bean.getClass()))));
    }

    @Override
    public Value get(String name) throws PropertyNotFoundException {
        PropertyDescriptor pd = assertAndGetReadablePropertyDescriptor(name);

        return new TypeConvertingValue(typeConverter, TypeUtil.invokeMethod(bean, pd.getReadMethod()));
    }

    @Override
    public Value get(String name, Object defaultValue) {
        try {
            return get(name);
        } catch (PropertyNotFoundException e) {
            return new TypeConvertingValue(typeConverter, defaultValue);
        }
    }

    @Override
    public void set(String name, Object value) throws BeanException {
        PropertyDescriptor pd = assertAndGetWritablePropertyDescriptor(name);

        TypeUtil.invokeMethod(typeConverter, bean, pd.getWriteMethod(), value);
    }

    public JavaBean<T> with(String name, Object value) throws BeanException {
        set(name, value);
        return this;
    }

    @Override
    public Class<?> getPropertyType(String name) throws BeanException {
        return assertAndGetPropertyDescriptor(name).getPropertyType();
    }

    private JavabeanMetadata getBeanMetaData() {
        return metadataCache.getBeanMetaData(bean.getClass(), Object.class);
    }
}
