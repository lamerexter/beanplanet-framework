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

package org.beanplanet.core.lang.conversion;

import org.beanplanet.core.logging.Logger;

public class SystemTypeConverter extends AbstractTypeConverterRegistry implements TypeConverter, Logger {
    private static SystemTypeConverter INSTANCE = new SystemTypeConverter();

    public static final TypeConverter systemTypeConverter() {
        return INSTANCE;
    }

    protected TypeConverterLoader loader = new PackageScanTypeConverterLoader();

    private boolean isLoaded = false;

    private static SystemTypeConverter instance;

    /**
     * Returns the system default type converter.
     *
     * @return a system-wide type converter for general use.
     */
    public synchronized static TypeConverter getInstance() {
        if (instance == null) {
            instance = new SystemTypeConverter();
        }

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
        // Attempt to find a corresponding converter in the registry
        //--------------------------------------------------------------------------------------------------------------
        return lookup(value.getClass(), targetType).orElseThrow(() -> new UnsupportedTypeConversionException()).convert(value, targetType);
    }

    public static void main(String... args) {
        SystemTypeConverter.getInstance().convert(1L, String.class);
    }
}
