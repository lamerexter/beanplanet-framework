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

import org.beanplanet.core.lang.Assert;
import org.beanplanet.core.util.CollectionUtil;

import java.util.*;

/**
 * <a href="http://en.wikipedia.org/wiki/Composite_pattern">Composite</a> design pattern for type converters. This converter encloses or aggregates a
 * number of other converters, providing the combined abilities of them all, transparently.
 * 
 * @author Gary Watson
 */
public class CompositeTypeConverter implements TypeConverter, Iterable<TypeConverter> {
    public CompositeTypeConverter(TypeConverter ... converters) {
        this.converters = Arrays.asList(converters);
    }

    /** The converters this composite is comprised of. */
    List<TypeConverter> converters;

    public CompositeTypeConverter() {
        this(new ArrayList<>());
    }

    public CompositeTypeConverter(List<TypeConverter> converters) {
        this.converters = converters;
    }

    /**
    * Returns the converters this composite is comprised of.
    *
    * @return a list of the converters which combined determined the behaviour of this composite.
    */
    public List<TypeConverter> getConverters() {
      return converters;
    }

    /**
    * Adds a converter to this composite.
    *
    * @param converter the converter to add.
    * @return true if the converter was added or false if the converter is already part of this composite.
    */
    public boolean add(TypeConverter converter) {
        Assert.notNull(converters, "The set of converters may not be null");
        return converters.add(converter);
    }

    /**
    * Removes a converter from this composite.
    *
    * @param converter the converter to remove.
    * @return true if the converter was removed or false if the converter was not part of this composite.
    */
    public boolean remove(TypeConverter converter) {
        Assert.notNull(converters, "The set of converters may not be null");
        return converters.remove(converter);
    }

    /**
    * Returns the size of the composite.
    *
    * @return the number of converters that comprise this composite.
    */
    public int size() {
      return (converters == null ? 0 : converters.size());
    }

    public <T> T convert(Object value, Class<T> targetType) throws UnsupportedTypeConversionException {
        if ( converters != null ) {
         for (Iterator<TypeConverter> tcIterator = converters.iterator(); tcIterator.hasNext(); ) {
            try {
               return tcIterator.next().convert(value, targetType);
            }
            catch (UnsupportedTypeConversionException utcEx) {
               // If it's the only converter we have then re-throw the exception, otherwise
               // ignore it and rethrow (possibly) later
               if ( !tcIterator.hasNext() ) {
                  throw utcEx;
               }
            }
         }
        }

        throw new UnsupportedTypeConversionException(value, targetType);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Iterator<TypeConverter> iterator() {
        return (converters != null ? converters.iterator() : CollectionUtil.<TypeConverter>emptyImmutableIterable().iterator());
    }
}
