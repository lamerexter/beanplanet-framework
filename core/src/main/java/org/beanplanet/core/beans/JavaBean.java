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

package org.beanplanet.core.beans;

import org.beanplanet.core.lang.TypeUtil;
import org.beanplanet.core.lang.conversion.TypeConverter;
import org.beanplanet.core.models.Value;

import java.beans.PropertyDescriptor;

public class JavaBean implements Bean {
    private JavabeanMetadataCache metadataCache;

    private Object bean;

    private TypeConverter typeConverter;

    public JavaBean(Object bean) {
        this(JavabeanMetadataCache.getSystemCache(), null, bean);
    }

    public JavaBean(JavabeanMetadataCache metadataCache, Object bean) {
        this(metadataCache, null, bean);
    }

    public JavaBean(TypeConverter typeConverter, Object bean) {
        this(null, typeConverter, bean);
    }

    public JavaBean(JavabeanMetadataCache metadataCache, TypeConverter typeConverter, Object bean) {
        this.metadataCache = metadataCache;
        this.typeConverter = typeConverter;
        this.bean = bean;
    }

    public Object getWrappedInstance() {
        return bean;
    }

    @Override
    public int getNumberOfProperties() {
        return getPropertyNames().length;
    }

    @Override
    public String[] getPropertyNames() {
        return getBeanMetaData().propertyNames;
    }

    @Override
    public boolean isReadableProperty(String name) {
        return getBeanMetaData().getPropertyDescriptor(name).filter(JavaBean::isReadableProperty).isPresent();
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

    @Override
    public Value get(String name) throws PropertyNotFoundException {
        PropertyDescriptor propertyDescriptor = getBeanMetaData().getPropertyDescriptor(name).filter(JavaBean::isReadableProperty).orElseThrow(
                () -> new PropertyNotFoundException("Property [" + name + "] on the bean [" + TypeUtil.getDisplayNameForType(bean.getClass())+"] not found or not readable. Does it have a public accessor?")
        );

        return null;
//        return TypeUtil.invokeMethod(bean, propertyDescriptor.getReadMethod(), EMPTY_ARRAY);
    }

    @Override
    public Value get(String name, Object defaultValue) {
        return null;
    }

    @Override
    public void set(String name, Object value) throws PropertyNotFoundException, BeanException {

    }

    @Override
    public Class<?> getPropertyType(String name) throws PropertyNotFoundException, BeanException {
        return null;
    }

    private JavabeanMetadata getBeanMetaData() {
        return metadataCache.getBeanMetaData(bean.getClass(), Object.class);
    }
}
