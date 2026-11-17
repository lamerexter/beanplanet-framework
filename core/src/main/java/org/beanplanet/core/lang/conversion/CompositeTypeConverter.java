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

import org.beanplanet.core.lang.Assert;
import org.beanplanet.core.util.CollectionUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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
    * Returns the length of the composite.
    *
    * @return the number of converters that comprise this composite.
    */
    public int size() {
      return (converters == null ? 0 : converters.size());
    }

    /** Widening if any of its converters is, else unspecified if any is, else narrowing. */
    @Override
    public ConversionKind getKind() {
        final java.util.List<ConversionKind> kinds = new ArrayList<>();
        if (converters != null) for (TypeConverter converter : converters) kinds.add(converter.getKind());
        return ConversionKind.combined(kinds);
    }

    /** As {@link #convert(Object, Class)}, using only converters that are not narrowing, unless narrowing is allowed. */
    @Override
    public <T> T convert(Object value, Class<T> targetType, boolean allowNarrowing) throws UnsupportedTypeConversionException {
        if (allowNarrowing) return convert(value, targetType);
        UnsupportedTypeConversionException last = null;
        if (converters != null) {
            for (TypeConverter converter : converters) {
                if (converter.getKind() == ConversionKind.NARROWING) continue;
                try {
                    return converter.convert(value, targetType, false);
                } catch (UnsupportedTypeConversionException e) {
                    last = e;
                }
            }
        }
        if (last != null) throw last;
        throw new UnsupportedTypeConversionException("A narrowing conversion of " + (value == null ? "null" : value.getClass().getName())
                                                     + " to " + targetType.getName() + " is not allowed: convert explicitly");
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
