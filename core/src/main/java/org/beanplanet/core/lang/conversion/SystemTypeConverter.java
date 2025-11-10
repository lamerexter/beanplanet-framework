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

package org.beanplanet.core.lang.conversion;

import org.beanplanet.core.logging.Logger;

import static java.lang.String.format;

public class SystemTypeConverter extends AbstractTypeConverterRegistry implements TypeConverter, Logger {
    private static SystemTypeConverter INSTANCE = new SystemTypeConverter();

    public static final TypeConverter systemTypeConverter() {
        return INSTANCE;
    }

    protected TypeConverterLoader loader = new PackageScanTypeConverterLoader();

    private boolean isLoaded = false;

    private static SystemTypeConverter instance = INSTANCE;

    /**
     * Returns the system default type converter.
     *
     * @return a system-wide type converter for general use.
     */
    public static TypeConverter getInstance() {
        return instance;
    }

    /**
     * Returns the type converter loader that will populate this type converter
     *
     * @return the loader that will be called to populate this type converter with component converters.
     */
    public TypeConverterLoader getLoader() {
        return loader;
    }

    /**
     * Sets the type converter loader that will populate this type converter
     *
     * @param loader the loader that will be called to populate this type converter with component converters.
     */
    public synchronized void setLoader(TypeConverterLoader loader) {
        this.loader = loader;
        isLoaded = false;
    }

    protected void loadTypeConverters() {
        clear();
        loader.load(this);
    }

    protected synchronized void checkLoaded() {
        if (!isLoaded) {
            loadTypeConverters();
            isLoaded = true;
        }
    }

    @Override
    public <T> T convert(Object value, Class<T> targetType) throws UnsupportedTypeConversionException {
        checkLoaded();

        //--------------------------------------------------------------------------------------------------------------
        // Pass through null values
        //--------------------------------------------------------------------------------------------------------------
        if (value == null)
            return null;

        //--------------------------------------------------------------------------------------------------------------
        // If source type is already of the target type then return it immediately
        //--------------------------------------------------------------------------------------------------------------
        if (targetType.isAssignableFrom(value.getClass()))
            return (T) value;

        //--------------------------------------------------------------------------------------------------------------
        // Attempt to find a corresponding converter in the registry.
        //--------------------------------------------------------------------------------------------------------------
//        Class<?> targetClass = targetType.isArray() ? Object[].class : targetType;    // GAW 2023-11-05 Why to Object[] ?!?
        Class<?> targetClass = targetType;
        return lookup(value.getClass(), targetClass)
                   .orElseThrow(() -> new UnsupportedTypeConversionException(format("Type converter from type \"%s\" to type \"%s\" not found",
                                                                                    (value == null ? null : value.getClass().getName()), targetType.getName()))
                   ).convert(value, targetType);
    }

    public static void main(String... args) {
        SystemTypeConverter.getInstance().convert(1L, String.class);
    }
}
