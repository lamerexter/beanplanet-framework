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

import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Optional;

import org.beanplanet.core.lang.TypeUtil;
import org.beanplanet.core.util.StringUtil;

/**
 * A static utility class, containing convenient methods that deal will access to <a
 * href="http://java.sun.com/javase/technologies/desktop/javabeans">JavaBeans</a>.
 *
 * @author Gary Watson
 */
public class BeanUtil {
    /**
     * Determines if the specified property name is a "readable" property of the given bean, as determined by the <a
     * href="http://java.sun.com/javase/technologies/desktop/javabeans">JavaBeans Specification</a>.
     *
     * <p>
     * This method simply calls the corresponding <code>isReadable</code> method with the system metadata cache, obtained
     * via a call to <code>JavabeanMetadataCache.getSystemCache()</code>.
     * </p>
     *
     * @param bean the bean whose named property is to be determined as readable.
     * @param name the name to be tested as a readable property of the bean.
     * @return true, if the bean has the specified readable property, false otherwise.
     *         <code><em>returnValue</em> get<em>name</em>()</code>.
     *
     * @see #isReadableProperty(JavabeanMetadataCache, Object, String)
     */
    public static final boolean isReadableProperty(Object bean, String name) {
        return isReadableProperty(JavabeanMetadataCache.getSystemCache(), bean, name);
    }

    /**
     * Determines if the specified property name is a "readable" property of the given bean class, as determined by the
     * <a href="http://java.sun.com/javase/technologies/desktop/javabeans">JavaBeans Specification</a>.
     *
     * <p>
     * This method simply calls the corresponding <code>isReadable</code> method with the system metadata cache, obtained
     * via a call to <code>JavabeanMetadataCache.getSystemCache()</code>.
     * </p>
     *
     * @param beanClass the type of the bean whose named property is to be determined as readable.
     * @param name the name to be tested as a readable property of the bean.
     * @return true, if the bean has the specified readable property, false otherwise.
     *         <code><em>returnValue</em> get<em>name</em>()</code>.
     *
     * @see #isReadableProperty(JavabeanMetadataCache, Object, String)
     */
    public static final boolean isReadableProperty(Class<?> beanClass, String name) {
        return isReadableProperty(JavabeanMetadataCache.getSystemCache(), beanClass, name);
    }

    /**
     * Determines if the specified name is "readable". For a JavaBean, the name is readable if it corresponds to a
     * readable <u>property</u> or public <u>method</u>, with the same name, zero parameters and a return value type, on
     * the bean.
     *
     * @param metaDataCache the cache to search for the bean metadata, or to populate should the cache not already
     *        contain the metadata
     * @param bean the bean whose name readability is to be tested.
     * @param name the name to be assessed for readability on the bean.
     * @return true, if the bean has the specified readable property or method that returns a value.
     */
    public static final boolean isReadableProperty(JavabeanMetadataCache metaDataCache, Object bean, String name) {
        return isReadableProperty(metaDataCache, bean.getClass(), name);
    }

    /**
     * Determines if the specified name is "readable". For a JavaBean, the name is readable if it corresponds to a
     * readable <u>property</u> or public <u>method</u>, with the same name, zero parameters and a return value type, on
     * the bean.
     *
     * @param metaDataCache the cache to search for the bean metadata, or to populate should the cache not already
     *        contain the metadata
     * @param beanClass the type of the bean whose named property is to be determined as readable.
     * @param name the name to be assessed for readability on the bean.
     * @return true, if the bean has the specified readable property or method that returns a value.
     */
    public static final boolean isReadableProperty(JavabeanMetadataCache metaDataCache, Class<?> beanClass, String name) {
        // if (bean instanceof JavaBean) {
        // bean = ((JavaBean) bean).getBeanObject();
        // }
        JavabeanMetadata JavabeanMetadata = metaDataCache.getBeanMetaData(beanClass);
        boolean isReadable = JavabeanMetadata.isReadableProperty(name);

        if (!isReadable) {
            Method publicMethods[] = JavabeanMetadata.getPublicMethods();
            for (int n = 0; n < publicMethods.length && !isReadable; n++) {
                Method method = publicMethods[n];
                isReadable = method.getName().equals(name) && method.getParameterTypes().length == 0
                    && method.getReturnType() != void.class;
            }
        }

        return isReadable;
    }

    /**
     * Returns the value of a given property of a bean.
     *
     * @param bean the bean whose property value is to be returned.
     * @param name the name of the property whose value is to be determined.
     * @return the value of the named property, which may be null.
     * @throws PropertyNotFoundException if no such property exists on the bean.
     * @throws BeanException if an error occurs reading the property of the bean.
     * @see #getPropertyValue(JavabeanMetadataCache, Object, String)
     */
    public static final Object getPropertyValue(Object bean, String name) throws PropertyNotFoundException,
        BeanException {
        return getPropertyValue(JavabeanMetadataCache.getSystemCache(), bean, name);
    }

    /**
     * Returns the value of a given property of a bean.
     *
     * @param metaDataCache the bean meta-data cache that will be consulted to return the required information
     * @param bean the bean whose property value is to be returned.
     * @param name the name of the property whose value is to be determined.
     * @return the value of the named property, which may be null.
     * @throws PropertyNotFoundException if no such property exists on the bean.
     * @throws BeanException if an error occurs reading the property of the bean.
     * @see #getPropertyValue(JavabeanMetadataCache, Object, String)
     */
    public static final Object getPropertyValue(JavabeanMetadataCache metaDataCache, Object bean, String name) throws PropertyNotFoundException,
        BeanException {
        // if (bean instanceof JavaBean) {
        // bean = ((JavaBean) bean).getBeanObject();
        // }
        if (!isReadableProperty(metaDataCache, bean, name)) {
            throw new PropertyNotFoundException("The name specified ["
                + name
                + "] is not readable on the bean ["
                + bean.getClass().getName()
                + "]. "
                + "In order to read this value, the JavaBean needs to have a readable property with that name or "
                + "method with that name, with zero parameters and a return value type.");
        }

        Object value = null;
        try {
            JavabeanMetadata JavabeanMetadata = metaDataCache.getBeanMetaData(bean.getClass());
            Optional<PropertyDescriptor> property = JavabeanMetadata.getPropertyDescriptor(name);
            boolean readProperty = false;
            if (property.isPresent()) {
                if (property.get() instanceof IndexedPropertyDescriptor) {
                    IndexedPropertyDescriptor idxProperty = (IndexedPropertyDescriptor) property.get();
                    Method method = idxProperty.getIndexedReadMethod();
                    if (method != null) {
                        value = TypeUtil.invokeMethod(bean, method, new Object[] { new Integer(0) });
                        // value = method.invoke(Modifier.isStatic(method.getModifiers()) ? null : bean,
                        // new Object[] { new Integer(0) });
                        readProperty = true;
                    }
                }
                else {
                    Method method = property.get().getReadMethod();
                    if (method != null) {
                        value = TypeUtil.invokeMethod(bean, method, new Object[] {});
                        // value = method.invoke(Modifier.isStatic(method.getModifiers()) ? null : bean, new Object[] {});
                        readProperty = true;
                    }
                }
            }

            // If it wasn't a readable property then it must be a method with the
            // same name that returns a value
            if (!readProperty) {
                Method publicMethods[] = JavabeanMetadata.getPublicMethods();
                for (int n = 0; n < publicMethods.length && !readProperty; n++) {
                    Method method = publicMethods[n];
                    if (method.getName().equals(name) && method.getParameterTypes().length == 0
                        && method.getReturnType() != void.class) {
                        value = TypeUtil.invokeMethod(bean, method, new Object[] {});
                        // value = method.invoke(Modifier.isStatic(method.getModifiers()) ? null : bean, new Object[] {});
                        readProperty = true;
                    }
                }
            }

            return value;

        }
        // catch (InvocationTargetException ex)
        // {
        // // Bean method called threw an exception
        // throw new BeanException("Exception thrown from bean property read method, \"" + name + "\": ", ex
        // .getTargetException());
        // }
        // catch (IllegalAccessException ex)
        // {
        // throw new BeanException(ex.getMessage());
        // }
        catch (Throwable th) {
            // Bean method called threw an exception
            throw new BeanException("Unexpected error reading bean property [" + name + "] on bean [class="
                + bean.getClass().getName() + "]: ", th);
        }
    }

    /**
     * Returns the type of a given property of a bean class.
     *
     * @param beanClass the bean whose property value is to be returned.
     * @param name the name of the property whose type is to be determined.
     * @return the type of the named property.
     * @throws PropertyNotFoundException if no such property exists on the bean.
     * @throws BeanException if an error occurs reading the property of the bean.
     * @see #getPropertyValue(JavabeanMetadataCache, Object, String)
     */
    public static final Class<?> getPropertyType(Class<?> beanClass, String name) throws PropertyNotFoundException,
        BeanException {
        return getPropertyType(JavabeanMetadataCache.getSystemCache(), beanClass, name);
    }

    /**
     * Returns the type of a given property of a bean class.
     *
     * @param metaDataCache the bean meta-data cache that will be consulted to return the required information
     * @param beanClass the type of the bean whose named property is to be determined as readable.
     * @param name the name of the property whose type is to be determined.
     * @return the type of the named property.
     * @throws PropertyNotFoundException if no such property exists on the bean.
     * @throws BeanException if an error occurs reading the property of the bean.
     * @see #getPropertyValue(JavabeanMetadataCache, Object, String)
     */
    public static final Class<?> getPropertyType(JavabeanMetadataCache metaDataCache, Class<?> beanClass, String name) throws PropertyNotFoundException,
        BeanException {
        // if (bean instanceof JavaBean) {
        // bean = ((JavaBean) bean).getBeanObject();
        // }
        if (!isReadableProperty(metaDataCache, beanClass, name)) {
            throw new PropertyNotFoundException("The name specified ["
                + name
                + "] is not readable on the bean class ["
                + beanClass.getName()
                + "]. "
                + "In order to read this value, the JavaBean class needs to have a readable property with that name or "
                + "method with that name, with zero parameters and a return value type.");
        }

        Class<?> propertyType = null;
        try {
            JavabeanMetadata JavabeanMetadata = metaDataCache.getBeanMetaData(beanClass);
            Optional<PropertyDescriptor> property = JavabeanMetadata.getPropertyDescriptor(name);
            boolean readProperty = false;
            if (property.isPresent()) {
                if (property.get() instanceof IndexedPropertyDescriptor) {
                    IndexedPropertyDescriptor idxProperty = (IndexedPropertyDescriptor) property.get();
                    Method method = idxProperty.getIndexedReadMethod();
                    if (method != null) {
                        propertyType = method.getReturnType();
                        readProperty = true;
                    }
                }
                else {
                    Method method = property.get().getReadMethod();
                    if (method != null) {
                        propertyType = method.getReturnType();
                        readProperty = true;
                    }
                }
            }

            // If it wasn't a readable property then it must be a method with the
            // same name that returns a value
            if (!readProperty) {
                Method publicMethods[] = JavabeanMetadata.getPublicMethods();
                for (int n = 0; n < publicMethods.length && !readProperty; n++) {
                    Method method = publicMethods[n];
                    if (method.getName().equals(name) && method.getParameterTypes().length == 0
                        && method.getReturnType() != void.class) {
                        propertyType = method.getReturnType();
                        readProperty = true;
                    }
                }
            }

            return propertyType;

        }
        // catch (InvocationTargetException ex)
        // {
        // // Bean method called threw an exception
        // throw new BeanException("Exception thrown from bean property read method, \"" + name + "\": ", ex
        // .getTargetException());
        // }
        // catch (IllegalAccessException ex)
        // {
        // throw new BeanException(ex.getMessage());
        // }
        catch (Throwable th) {
            // Bean method called threw an exception
            throw new BeanException("Unexpected error reading bean property [" + name + "] on bean class ["
                + beanClass.getName() + "]: ", th);
        }
    }

    /**
     * Determines if the specified name is "writable". For a JavaBean, the name is writable if it corresponds to a
     * writable <u>property</u> or public <u>method</u> with the same name that takes 1 parameter on the bean.
     *
     * @param bean the bean whose name readability is to be tested.
     * @param name the name to be assessed for readability on the bean.
     * @return true, if the bean has the specified readable property or method that returns a value.
     */
    public static final boolean isWritableProperty(Object bean, String name) {
        return isWritableProperty(JavabeanMetadataCache.getSystemCache(), bean, name);
    }

    /**
     * Determines if the specified name is "writable". For a JavaBean, the name is writable if it corresponds to a
     * writable <u>property</u> or public <u>method</u> with the same name that takes 1 parameter on the bean.
     *
     * @param bean the bean whose name readability is to be tested.
     * @param name the name to be assessed for readability on the bean.
     * @return true, if the bean has the specified readable property or method that returns a value.
     */
    @SuppressWarnings("serial")
    public static final boolean isWritableProperty(JavabeanMetadataCache metaDataCache, Object bean, final String name) {
        // if (bean instanceof JavaBean) {
        // bean = ((JavaBean) bean).getBeanObject();
        // }
        JavabeanMetadata JavabeanMetadata = metaDataCache.getBeanMetaData(bean.getClass());
        boolean isWritable = JavabeanMetadata.isWriteableProperty(name);

//        if (!isWritable) {
//            isWritable = !TypeUtil.getMethods(bean.getClass(), new Filter<Method>() {
//                private String mutatorMethodName = "set" + StringUtil.initCap(name, false);
//                public boolean accept(Method m) {
//                    return Modifier.isPublic(m.getModifiers()) &&
//                        (m.getName().equals(mutatorMethodName) || m.getName().equals(name)) &&
//                        m.getParameterTypes().length == 1;
//                }
//
//            }).isEmpty();
//        }

        return isWritable;
    }

    /**
     * Returns a list of the names of all properties exposed by the bean class hierarchy, from the specified subclass,
     * <code>subTypeInclusive</code>, to the specified superclass, <code>superTypeExclusive</code>.
     *
     * @param bean the JavaBean whose property names are to be determined.
     * @return an array of the names of all properties within the specified type range.
     * @exception BeanException thrown if access to the properties is forbidden or a problem occurred obtaining the
     *            property names.
     * @see #getPropertyNames(Object, Class);
     */
    public static final String[] getPropertyNames(Object bean) throws BeanException {
        return getPropertyNames(bean, Object.class);
    }

    /**
     * Returns a list of the names of all properties exposed by the bean class hierarchy, from the specified subclass,
     * <code>subTypeInclusive</code>, to the <code>Object.class</code> exclusively.
     *
     * @param specificTypeInclusive the sub type from which, inclusively, all properties are to be discovered.
     * @return an array of the names of all properties within the specified type range.
     * @exception BeanException thrown if access to the properties is forbidden or a problem occurred obtaining the
     *            property names.
     * @see #getPropertyNames(Class, Class);
     */
    public static final String[] getPropertyNames(Class<?> specificTypeInclusive) throws BeanException {
        return getPropertyNames(specificTypeInclusive, Object.class);
    }

    /**
     * Returns a list of the names of all properties exposed by the bean.
     *
     * @return an array of the names of all properties of the bean.
     * @exception BeanException thrown if access to the properties is forbidden or a problem occurred obtaining the
     *            property names.
     */
    public static final String[] getPropertyNames(JavabeanMetadataCache metaDataCache, Object bean) throws BeanException {
        JavabeanMetadata beanMetaData = metaDataCache.getBeanMetaData(bean.getClass());
        return beanMetaData.getPropertyNames();
    }

    /**
     * Returns a list of the names of all properties exposed by the bean class hierarchy, from the specified subclass,
     * <code>subTypeInclusive</code>, to the specified superclass, <code>superTypeExclusive</code>.
     *
     * @param bean the JavaBean whose property names are to be determined.
     * @param generalTypeInclusive the super type up to which, exclusively, all properties are to be discovered. May be
     *        null to indicate all properties up to and including <code>Object.class</code>.
     * @return an array of the names of all properties within the specified type range.
     * @exception BeanException thrown if access to the properties is forbidden or a problem occurred obtaining the
     *            property names.
     * @see #getPropertyNames(JavabeanMetadataCache, Object, Class)
     */
    public static final String[] getPropertyNames(Object bean, Class<?> generalTypeInclusive) throws BeanException {
        return getPropertyNames(JavabeanMetadataCache.getSystemCache(), bean, generalTypeInclusive);
    }

    /**
     * Returns a list of the names of all properties exposed by the bean class hierarchy, from the specified subclass,
     * <code>subTypeInclusive</code>, to the specified superclass, <code>superTypeExclusive</code>.
     *
     * @param metaDataCache the bean meta-data cache that will be consulted to return the required information
     * @param bean the JavaBean whose property names are to be determined.
     * @param generalTypeInclusive the super type up to which, exclusively, all properties are to be discovered. May be
     *        null to indicate all properties up to and including <code>Object.class</code>.
     * @return an array of the names of all properties within the specified type range.
     * @exception BeanException thrown if access to the properties is forbidden or a problem occurred obtaining the
     *            property names.
     * @see #getPropertyNames(JavabeanMetadataCache, Class, Class)
     */
    public static final String[] getPropertyNames(JavabeanMetadataCache metaDataCache,
                                                  Object bean,
                                                  Class<?> generalTypeInclusive) throws BeanException {
        return getPropertyNames(metaDataCache, bean.getClass(), generalTypeInclusive);
    }

    /**
     * Returns a list of the names of all properties exposed by the bean class hierarchy, from the specified subclass,
     * <code>subTypeInclusive</code>, to the specified superclass, <code>superTypeExclusive</code>.
     *
     * @param specificTypeInclusive the sub type from which, inclusively, all properties are to be discovered.
     * @param generalTypeInclusive the super type up to which, exclusively, all properties are to be discovered. May be
     *        null to indicate all properties up to and including <code>Object.class</code>.
     * @return an array of the names of all properties within the specified type range.
     * @exception BeanException thrown if access to the properties is forbidden or a problem occurred obtaining the
     *            property names.
     * @see #getPropertyNames(JavabeanMetadataCache, Class, Class)
     */
    public static final String[] getPropertyNames(Class<?> specificTypeInclusive, Class<?> generalTypeInclusive) throws BeanException {
        return getPropertyNames(JavabeanMetadataCache.getSystemCache(), specificTypeInclusive, generalTypeInclusive);
    }

    /**
     * Returns a list of the names of all properties exposed by the bean class hierarchy, from the specified subclass,
     * <code>subTypeInclusive</code>, to the specified superclass, <code>superTypeExclusive</code>.
     *
     * @param metaDataCache the bean meta-data cache that will be consulted to return the required information
     * @param specificTypeInclusive the sub type from which, inclusively, all properties are to be discovered.
     * @param generalTypeInclusive the super type up to which, exclusively, all properties are to be discovered. May be
     *        null to indicate all properties up to and including <code>Object.class</code>.
     * @return an array of the names of all properties within the specified type range.
     * @exception BeanException thrown if access to the properties is forbidden or a problem occurred obtaining the
     *            property names.
     */
    public static final String[] getPropertyNames(JavabeanMetadataCache metaDataCache,
                                                  Class<?> specificTypeInclusive,
                                                  Class<?> generalTypeInclusive) throws BeanException {
        JavabeanMetadata beanMetaData = metaDataCache.getBeanMetaData(specificTypeInclusive, generalTypeInclusive);
        return beanMetaData.getPropertyNames();
    }
}

