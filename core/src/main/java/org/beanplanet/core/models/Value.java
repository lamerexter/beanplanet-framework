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
import org.beanplanet.core.lang.TypeUtil;
import org.beanplanet.core.util.DateUtil;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Supplier;

/**
 * Holder of a value which does not impose any restriction on where the value has come from, whether the value is
 * immediately available (the value might be lazily retrieved by the implementation on first call to the
 * <code>{@link #getValue()}</code>, <code>{@link #getType()}</code> or <code>as...()</code> methods) and provides
 * type-safe methods to retrieve the value in the type preferred by the caller.
 * 
 * <p>
 * In this model, the value held may be retrieved directly (in its raw form) using the <code>{@link #getValue()}</code>
 * method or via type conversion, if required, via one or more of the <code>as...()</code> methods.
 * </p>
 * 
 * @author Gary Watson
 */
public interface Value {
    /**
     * Determines if the result is <code>null</code>.
     *
     * @return true if the value of the result is <code>null</code>, false otherwise.
     */
    default boolean isNull()  {
       return getValue() == null;
    }

    /**
     * Returns the type of the result.
     *
     * @return the type of the result, which may be an interface or other abstract type or null if the value is null.
     * @exception DataAccessException thrown if an error occurs during conversion of the result to the required type.
     */
    default Class<?> getType() throws DataAccessException {
       return getValue() != null ? getValue().getClass() : null;
    }

    /**
     * Returns directly the underlying value.
     *
     * @return the value, which may be null.
     * @see #as(Class)
     */
    Object getValue();

    /**
     * Returns the value of the result as the type specified.
     *
     * @param targetType the type of the value required; if the value is not an instance of this type an attempt to convert the
     * value to this type will be made, depending on the implementation.
     * @return null if the result was null, or the value of the result, converted to the specified type, if required.
     * @exception DataAccessException thrown if an error occurs during conversion of the result to the required type.
     */
    <T> T as(Class<T> targetType) throws DataAccessException;

    /**
     * Returns the value of the result as the type specified.
     *
     * @param targetType the type of the value required; if the value is not an instance of this type an attempt to convert the
     * value to this type will be made, depending on the implementation.
     * @param defaultValue a default value to use as the return value, if the result could not be converted to the requested type.
     * @return null if the result was null, or the value of the result, converted to the specified type, if required.
     * @exception DataAccessException thrown if an error occurs during conversion of the result to the required type.
     */
    default <T> T as(Class<T> targetType, T defaultValue) throws DataAccessException {
       try {
          return as(targetType);
       }
       catch(DataAccessException ignoreEx) {
          return defaultValue;
       }
    }

    /**
     * Returns the value of the result as a <code>Boolean</code>.
     *
     * @return the value of the result.
     * @exception DataAccessException thrown if an error occurs during conversion of the result to the required type.
     */
    default boolean asBoolean() throws DataAccessException {
       return as(Boolean.class);
    }

    /**
     * Returns the value of the result as a <code>Boolean</code>.
     *
     * @param defaultValue a default value to use as the return value, if the result could not be converted to a boolean.
     * @return the value of the result.
     */
    default boolean asBoolean(boolean defaultValue) {
       try {
          return asBoolean();
       } catch (DataAccessException ignoreEx) {
          return defaultValue;
       }
    }

    /**
     * Returns the value of the result as a <code>java.util.Date</code>.
     *
     * @return the value of the result, as a date. If the result is not of type (or assignable from)
     *         <code>java.util.Date</code> an attempt to convert it to a date, in the default system locale, will be
     *         made.
     * @exception DataAccessException thrown if an error occurs during conversion of the result to the required type.
     */
    default Date asDate() throws DataAccessException {
       return as(Date.class);
    }

    /**
     * Returns the value of the result as a <code>java.util.Date</code>.
     *
     * @param defaultValue a default value to use as the return value, if the result could not be converted to a date.
     * @return the value of the result, as a date. If the result is not of type (or assignable from)
     *         <code>java.util.Date</code> an attempt to convert it to a date, in the default system locale, will be
     *         made.
     */
    default Date asDate(Date defaultValue) {
       try {
          return asDate();
       } catch (DataAccessException ignoreEx) {
          return defaultValue;
       }
    }

    /**
     * Returns the value of the result as a <code>java.util.Date</code>.
     *
     * @param dateFormat the format (pattern) the date is expected to take. See <code>java.text.SimpleDateFormat</code>
     *        below.
     * @return the value of the result, as a date. If the property is not of type (or assignable from)
     *         <code>java.util.Date</code> an attempt to convert it to a date, using the specified <i>dateFormat</i>,
     *         will be made.
     * @exception DataAccessException thrown if an error occurs during conversion of the result to the required type.
     * @see java.text.SimpleDateFormat
     */
    default Date asDate(String dateFormat) throws DataAccessException {
       return DateUtil.parse(asString(), dateFormat);
    }

    /**
     * Returns the value of the result as a <code>java.util.Date</code>.
     *
     * @param dateFormat the format (pattern) the date is expected to take. See <code>java.text.SimpleDateFormat</code>
     *        below.
     * @param defaultValue a default value to use as the return value, if the result could not be converted to a boolean.
     * @return the value of the result, as a date. If the property is not of type (or assignable from)
     *         <code>java.util.Date</code> an attempt to convert it to a date, using the specified <i>dateFormat</i>,
     *         will be made.
     * @see java.text.SimpleDateFormat
     */
    default Date asDate(String dateFormat, Date defaultValue) {
       try {
          return asDate(dateFormat);
       } catch (DataAccessException tcEx) {
          return defaultValue;
       }
    }

    /**
     * Returns the value of the result as a <code>Double</code>.
     *
     * @return the value of the result, which could be null.
     * @exception DataAccessException thrown if an error occurs during conversion of the result to the required type.
     */
    default double asDouble() throws DataAccessException {
       return asDouble();
    }

    /**
     * Returns the value of the result as a <code>Double</code>.
     *
     * @param defaultValue a default value to use as the return value, if the result could not be converted to a double.
     * @return the value of the result, which could be null.
     */
    default double asDouble(double defaultValue) {
       try {
          return asDouble();
       } catch (DataAccessException tcEx) {
          return defaultValue;
       }
    }

    /**
     * Returns the value of the result as a <code>Float</code>.
     *
     * @return the value of the result, which could be null.
     * @exception DataAccessException thrown if an error occurs during conversion of the result to the required type.
     */
    default float asFloat() throws DataAccessException {
       return as(Float.class);
    }

    /**
     * Returns the value of the result as a <code>Float</code>.
     *
     * @param defaultValue a default value to use as the return value, if the result could not be converted to a float.
     * @return the value of the result, which could be null.
     */
    default float asFloat(float defaultValue) {
       try {
          return asFloat();
       } catch (DataAccessException tcEx) {
          return defaultValue;
       }
    }

    /**
     * Returns the value of the result as a <code>Integer</code>.
     *
     * @return the value of the result, which could be null.
     * @exception DataAccessException thrown if an error occurs during conversion of the result to the required type.
     */
    default int asInt() throws DataAccessException {
       return as(Integer.class);
    }

    /**
     * Returns the value of the result as a <code>Integer</code>.
     *
     * @param defaultValue a default value to use as the return value, if the result could not be converted to an
     *        integer.
     * @return the value of the result, which could be null.
     */
    default int asInt(int defaultValue) {
       try {
          return asInt();
       } catch (DataAccessException tcEx) {
          return defaultValue;
       }

    }

    /**
     * Returns the value of the result as a <code>Long</code>.
     *
     * @return the value of the result, which could be null.
     * @exception DataAccessException thrown if an error occurs during conversion of the result to the required type.
     */
    default long asLong() throws DataAccessException {
       return as(Long.class);
    }

    /**
     * Returns the value of the result as a <code>Long</code>.
     *
     * @param defaultValue a default value to use as the return value, if the result could not be converted to a number.
     * @return the value of the result, which could be null.
     */
    default long asLong(long defaultValue) {
       try {
          return asLong();
       } catch (DataAccessException tcEx) {
          return defaultValue;
       }
    }

    /**
     * Returns the value of the result as a <code>Number</code>.
     *
     * @return the value of the result, which could be null.
     * @exception DataAccessException thrown if an error occurs during conversion of the result to the required type.
     */
    default Number asNumber() throws DataAccessException {
       return as(Number.class);
    }

    /**
     * Returns the value of the result as a <code>Number</code>.
     *
     * @param defaultValue a default value to use as the return value, if the result could not be converted to a number.
     * @return the value of the result, which could be null.
     */
   default Number asNumber(Number defaultValue) {
      try {
         return asNumber();
      } catch (DataAccessException tcEx) {
         return defaultValue;
      }
   }

    /**
     * Returns the value of the result as a <code>String</code> value.
     *
     * @return the value of the result, which could be null.
     * @exception DataAccessException thrown if an error occurs during conversion of the result to the required type.
     */
    default String asString() throws DataAccessException {
       return as(String.class);
    }

    /**
     * Returns the value of the result as a <code>String</code> value.
     *
     * @param defaultValue a default value to use as the return value, if the result could not be converted to a string.
     * @return the value of the result, which could be null.
     */
    default String asString(String defaultValue) {
       try {
          return asString();
       } catch (DataAccessException tcEx) {
          return defaultValue;
       }
    }

    /**
     * Returns the value of the result as a character.
     *
     * @return the value of the result, as a character.
     * @exception DataAccessException thrown if an error occurs during conversion of the result to the required type.
     */
    default char asCharacter() throws DataAccessException {
        return as(Character.class);
    }

    /**
     * Returns the value of the result as a character.
     *
     * @param defaultValue a default value to use as the return value, if the result could not be converted to an
     *        integer.
     * @return the value of the result, as character.
     */
    default char asCharacter(int defaultValue) {
        try {
            return asCharacter();
        } catch (DataAccessException tcEx) {
            return (char)defaultValue;
        }
    }

    /**
     * Returns the value of the result as an array.
     *
     * @return the value of the result, which could be null.
     * @exception DataAccessException thrown if an error occurs during conversion of the result to the required type.
     */
    default Object[] asArray() throws DataAccessException {
       return as(Object[].class);
    }

    /**
     * Returns the value of the result as an array.
     *
     * @param defaultValue a default value to use as the return value, if the result could not be converted to an array.
     * @return the value of the result, which could be null.
     */
    default Object[] asArray(Object[] defaultValue) {
       try {
          return asArray();
       } catch (DataAccessException tcEx) {
          return defaultValue;
       }
    }

    /**
     * Returns the value of the result as an array of the specified component type.
     *
     * @param componentType the component type of the array to be returned as the value
     * @return the value of the result, which could be null.
     * @exception DataAccessException thrown if an error occurs during conversion of the result to the required type.
     */
    @SuppressWarnings("unchecked")
    default <T> T[] asArrayOf(Class<T> componentType) throws DataAccessException {
       return asListOf(componentType).toArray((T[]) Array.newInstance(componentType, 0));
    }

    /**
     * Returns the value of the result as an array.
     *
     * @param defaultValue a default value to use as the return value, if the result could not be converted to an array.
     * @return the value of the result, which could be null.
     */
    default <T> T[] asArrayOf(Class<T> componentType, T[] defaultValue) {
       try {
          return asArrayOf(componentType);
       } catch (DataAccessException tcEx) {
          return defaultValue;
       }
    }

    /**
     * Returns the value of the result as a collection of values.
     *
     * @return the value of the result, which could be null.
     * @exception DataAccessException thrown if an error occurs accessing or converting the value.
     */
    @SuppressWarnings("unchecked")
    default <T, C extends Collection<T>> C asCollection() throws DataAccessException {
        return (C)asCollectionOf(Object.class);
    }

    /**
     * Returns the value of the result as a collection of values.
     *
     * @param defaultValue a default value to use as the return value, if the result could not be converted to a
     *        collection.
     * @return the value of the result, which could be null.
     */
    default <T, C extends Collection<T>> C asCollection(C defaultValue) {
       try {
          return asCollection();
       } catch (DataAccessException tcEx) {
          return defaultValue;
       }
    }

    /**
     * Returns the value of the result as a new collection of values.
     *
     * @param collectionType the type of collection (map, list ...) to be returned.
     * @return the value of the result, which could be null.
     * @exception DataAccessException thrown if an error occurs accessing or converting the value.
     */
    @SuppressWarnings("unchecked")
    default <T, C extends Collection<T>> C asCollectionType(Class<C> collectionType) throws DataAccessException {
       return asCollectionTypeOf(collectionType, (Class<T>)Object.class);
    }

    /**
     * Returns the value of the result as a new collection of values.
     *
     * @param defaultValue a default value to use as the return value, if the result could not be converted to a
     *        collection.
     * @return the value of the result, which could be null.
     */
    default <T, C extends Collection<T>> C asCollectionType(Class<C> collectionType, C defaultValue) {
       try {
          return asCollectionType(collectionType);
       } catch (DataAccessException tcEx) {
          return defaultValue;
       }
    }

    /**
     * Returns the value of the result as a new collection of values of the given type.
     *
     * @param elementType the list element type.
     * @return the value of the result, which could be null.
     * @exception DataAccessException thrown if an error occurs accessing or converting the value.
     */
    @SuppressWarnings("unchecked")
    default <T, C extends Collection<T>> C asCollectionOf(Class<T> elementType) throws DataAccessException {
        return (C)asCollectionTypeOf(List.class, elementType);
    }

     /**
      * Returns the value of the result as a new collection of the given type, whose element values are of the type specified.
      *
      * @param collectionType the type of collection created to hold the value elements.
      * @param elementType the type of the elements of the new collection created. How the value's elements are checked
      *                    for conformance with the given element type is implementation specific. For example, one
      *                    implementation might simply assert all elements of the collection are instances of the given
      *                    type, while another implementation might convert elements to the desired type.
      * @return the value of the result, which could be null.
      * @exception DataAccessException thrown if an error occurs accessing or converting the value.
      */
     default <T, C extends Collection<T>> C asCollectionTypeOf(Class<C> collectionType, Class<T> elementType) throws DataAccessException {
         return asCollectionTypeOf(() -> TypeUtil.instantiateClass(collectionType), elementType);
     }

     /**
      * Returns the value of the result, as a new collection of a given type, whose element values are of the type specified.
      *
      * @param collectionType the type of collection created to hold the value elements.
      * @param elementType the type of the elements of the new collection created. How the value's elements are checked
      *                    for conformance with the given element type is implementation specific. For example, one
      *                    implementation might simply assert all elements of the collection are instances of the given
      *                    type, while another implementation might convert elements to the desired type.
      * @param defaultValue a default value to use as the return value, if the result could not be converted to a collection.
      * @return the value of the result, which could be null.
      */
     default <T, C extends Collection<T>> C asCollectionTypeOf(Class<C> collectionType, Class<T> elementType, C defaultValue) {
         try {
             return asCollectionTypeOf(collectionType, elementType);
         } catch (DataAccessException ignoreEx) {
             return defaultValue;
         }
     }

    /**
     * Returns the value of the result as a supplied of a given type, whose element values are of the type specified.
     *
     * @param supplier a supplier of the collection with which to associate the value's values.
     * @param elementType the type of the elements of the new collection created. How the value's elements are checked
     *                    for conformance with the given element type is implementation specific. For example, one
     *                    implementation might simply assert all elements of the collection are instances of the given
     *                    type, while another implementation might convert elements to the desired type.
     * @return the value of the result, which could be null.
     * @exception DataAccessException thrown if an error occurs accessing or converting the value.
     */
    <T, C extends Collection<T>> C asCollectionTypeOf(Supplier<C> supplier, Class<T> elementType) throws DataAccessException;

    /**
     * Returns the value of the result as a new mutable collection of a given type, whose element values are of the type specified.
     *
     * @param supplier a supplier of the collection with which to associate the value's values.
     * @param elementType the type of the elements of the new collection created. How the value's elements are checked
     *                    for conformance with the given element type is implementation specific. For example, one
     *                    implementation might simply assert all elements of the collection are instances of the given
     *                    type, while another implementation might convert elements to the desired type.
     * @param defaultValue a default value to use as the return value, if the result could not be converted to a collection.
     * @return the value of the result, which could be null.
     */
    default <T, C extends Collection<T>> C asCollectionTypeOf(Supplier<C> supplier, Class<T> elementType, C defaultValue) {
        try {
            return asCollectionTypeOf(supplier, elementType);
        } catch (DataAccessException ignoreEx) {
            return defaultValue;
        }
    }

    /**
     * Returns the value of the result as a list of values.
     *
     * @return the value of the result, which could be null.
     * @exception DataAccessException thrown if an error occurs accessing or converting the value.
     */
    @SuppressWarnings("unchecked")
    default <T, C extends List<T>> C asList() throws DataAccessException {
        return (C)asListOf(Object.class);
    }

    /**
     * Returns the value of the result as a list of values.
     *
     * @param defaultValue a default value to use as the return value, if the result could not be converted to a
     *        collection.
     * @return the value of the result, which could be null.
     */
    default <T, C extends List<T>> C asList(C defaultValue) {
        try {
            return asList();
        } catch (DataAccessException tcEx) {
            return defaultValue;
        }
    }

    /**
     * Returns the value of the result as a new list of values of the given type, whose element values are of the type specified.
     *
     * @param collectionType the type of list to be returned.
     * @return the value of the result, which could be null.
     * @exception DataAccessException thrown if an error occurs accessing or converting the value.
     */
    @SuppressWarnings("unchecked")
    default <T, C extends List<T>> C asListType(Class<C> collectionType) throws DataAccessException {
        return asListTypeOf(collectionType, (Class<T>)Object.class);
    }

    /**
     * Returns the value of the result as a list of values of the given type.
     *
     * @param defaultValue a default value to use as the return value, if the result could not be converted to a
     *        collection.
     * @return the value of the result, which could be null.
     */
    default <T, C extends List<T>> C asListType(Class<C> collectionType, C defaultValue) {
        try {
            return asListType(collectionType);
        } catch (DataAccessException tcEx) {
            return defaultValue;
        }
    }

    /**
     * Returns the value of the result as a list of values, whose element values are of the type specified.
     *
     * @param elementType the list element type.
     * @return the value of the result, which could be null.
     * @exception DataAccessException thrown if an error occurs accessing or converting the value.
     */
    @SuppressWarnings("unchecked")
    default <T, C extends List<T>> C asListOf(Class<T> elementType) throws DataAccessException {
        return (C)asListTypeOf(List.class, elementType);
    }

    /**
     * Returns the value of the result as a list of a given type, whose element values are of the type specified.
     *
     * @param collectionType the type of collection created to hold the value elements.
     * @param elementType the type of the elements of the new collection created. How the value's elements are checked
     *                    for conformance with the given element type is implementation specific. For example, one
     *                    implementation might simply assert all elements of the collection are instances of the given
     *                    type, while another implementation might convert elements to the desired type.
     * @return the value of the result, which could be null.
     * @exception DataAccessException thrown if an error occurs accessing or converting the value.
     */
    default <T, C extends List<T>> C asListTypeOf(Class<C> collectionType, Class<T> elementType) throws DataAccessException {
        return asCollectionTypeOf(collectionType, elementType);
    }

    /**
     * Returns the value of the result as a list of a given type, whose element values are of the type specified.
     *
     * @param collectionType the type of collection created to hold the value elements.
     * @param elementType the type of the elements of the new collection created. How the value's elements are checked
     *                    for conformance with the given element type is implementation specific. For example, one
     *                    implementation might simply assert all elements of the collection are instances of the given
     *                    type, while another implementation might convert elements to the desired type.
     * @param defaultValue a default value to use as the return value, if the result could not be converted to a collection.
     * @return the value of the result, which could be null.
     */
    default <T, C extends Collection<T>> C asListTypeOf(Class<C> collectionType, Class<T> elementType, C defaultValue) {
        try {
            return asCollectionTypeOf(collectionType, elementType);
        } catch (DataAccessException ignoreEx) {
            return defaultValue;
        }
    }

    /**
     * Returns the value of the result as a list of a supplied type, whose element values are of the type specified.
     *
     * @param supplier a supplier of the list with which to associate the value's values.
     * @param elementType the type of the elements of the new list created. How the value's elements are checked
     *                    for conformance with the given element type is implementation specific. For example, one
     *                    implementation might simply assert all elements of the collection are instances of the given
     *                    type, while another implementation might convert elements to the desired type.
     * @return the value of the result, which could be null.
     * @exception DataAccessException thrown if an error occurs accessing or converting the value.
     */
    default <T, C extends List<T>> C asListTypeOf(Supplier<C> supplier, Class<T> elementType) throws DataAccessException {
        return asCollectionTypeOf(supplier, elementType);
    }

    /**
     * Returns the value of the result as a list of a specified type, whose element values are of the type specified.
     *
     * @param supplier a supplier of the list with which to associate the value's values.
     * @param elementType the type of the elements of the new list created. How the value's elements are checked
     *                    for conformance with the given element type is implementation specific. For example, one
     *                    implementation might simply assert all elements of the collection are instances of the given
     *                    type, while another implementation might convert elements to the desired type.
     * @param defaultValue a default value to use as the return value, if the result could not be converted to a list.
     * @return the value of the result, which could be null.
     */
    default <T, C extends List<T>> C asListTypeOf(Supplier<C> supplier, Class<T> elementType, C defaultValue) {
        try {
            return asCollectionTypeOf(supplier, elementType);
        } catch (DataAccessException ignoreEx) {
            return defaultValue;
        }
    }

    /**
     * Returns the value of the result as a set of values.
     *
     * @return the value of the result, which could be null.
     * @exception DataAccessException thrown if an error occurs accessing or converting the value.
     */
    @SuppressWarnings("unchecked")
    default <T, C extends Set<T>> C asSet() throws DataAccessException {
        return (C)asSetOf(Object.class);
    }

    /**
     * Returns the value of the result as a set of values.
     *
     * @param defaultValue a default value to use as the return value, if the result could not be converted to a
     *        collection.
     * @return the value of the result, which could be null.
     */
    default <T, C extends Set<T>> C asSet(C defaultValue) {
        try {
            return asSet();
        } catch (DataAccessException tcEx) {
            return defaultValue;
        }
    }

    /**
     * Returns the value of the result as a set of values of a given type.
     *
     * @param collectionType the type of Set to be returned.
     * @return the value of the result, which could be null.
     * @exception DataAccessException thrown if an error occurs accessing or converting the value.
     */
    @SuppressWarnings("unchecked")
    default <T, C extends Set<T>> C asSetType(Class<C> collectionType) throws DataAccessException {
        return asSetTypeOf(collectionType, (Class<T>)Object.class);
    }

    /**
     * Returns the value of the result as a set of values.
     *
     * @param defaultValue a default value to use as the return value, if the result could not be converted to a
     *        collection.
     * @return the value of the result, which could be null.
     */
    default <T, C extends Set<T>> C asSetType(Class<C> collectionType, C defaultValue) {
        try {
            return asSetType(collectionType);
        } catch (DataAccessException tcEx) {
            return defaultValue;
        }
    }

    /**
     * Returns the value of the result, as a set of values of the specified type.
     *
     * @param elementType the Set element type.
     * @return the value of the result, which could be null.
     * @exception DataAccessException thrown if an error occurs accessing or converting the value.
     */
    @SuppressWarnings("unchecked")
    default <T, C extends Set<T>> C asSetOf(Class<T> elementType) throws DataAccessException {
        return (C)asSetTypeOf(Set.class, elementType);
    }

    /**
     * Returns the value of the result as a set of a given type, whose element values are of the type specified.
     *
     * @param collectionType the type of set created to hold the value elements.
     * @param elementType the type of the elements of the new collection created. How the value's elements are checked
     *                    for conformance with the given element type is implementation specific. For example, one
     *                    implementation might simply assert all elements of the collection are instances of the given
     *                    type, while another implementation might convert elements to the desired type.
     * @return the value of the result, which could be null.
     * @exception DataAccessException thrown if an error occurs accessing or converting the value.
     */
    default <T, C extends Set<T>> C asSetTypeOf(Class<C> collectionType, Class<T> elementType) throws DataAccessException {
        return asCollectionTypeOf(collectionType, elementType);
    }

    /**
     * Returns the value of the result as a set of a given type, whose element values are of the type specified.
     *
     * @param collectionType the type of set created to hold the value elements.
     * @param elementType the type of the elements of the new collection created. How the value's elements are checked
     *                    for conformance with the given element type is implementation specific. For example, one
     *                    implementation might simply assert all elements of the collection are instances of the given
     *                    type, while another implementation might convert elements to the desired type.
     * @param defaultValue a default value to use as the return value, if the result could not be converted to a collection.
     * @return the value of the result, which could be null.
     */
    default <T, C extends Set<T>> C asSetTypeOf(Class<C> collectionType, Class<T> elementType, C defaultValue) {
        try {
            return asSetTypeOf(collectionType, elementType);
        } catch (DataAccessException ignoreEx) {
            return defaultValue;
        }
    }

    /**
     * Returns the value of the result as a set of a supplied type, whose element values are of the type specified.
     *
     * @param supplier a supplier of the set with which to associate the value's values.
     * @param elementType the type of the elements of the new set created. How the value's elements are checked
     *                    for conformance with the given element type is implementation specific. For example, one
     *                    implementation might simply assert all elements of the collection are instances of the given
     *                    type, while another implementation might convert elements to the desired type.
     * @return the value of the result, which could be null.
     * @exception DataAccessException thrown if an error occurs accessing or converting the value.
     */
    default <T, C extends Set<T>> C asSetTypeOf(Supplier<C> supplier, Class<T> elementType) throws DataAccessException {
        return asCollectionTypeOf(supplier, elementType);
    }

    /**
     * Returns the value of the result as a set of a specified type, whose element values are of the type specified.
     *
     * @param supplier a supplier of the set with which to associate the value's values.
     * @param elementType the type of the elements of the new set created. How the value's elements are checked
     *                    for conformance with the given element type is implementation specific. For example, one
     *                    implementation might simply assert all elements of the collection are instances of the given
     *                    type, while another implementation might convert elements to the desired type.
     * @param defaultValue a default value to use as the return value, if the result could not be converted to a list.
     * @return the value of the result, which could be null.
     */
    default <T, C extends Set<T>> C asSetTypeOf(Supplier<C> supplier, Class<T> elementType, C defaultValue) {
        try {
            return asCollectionTypeOf(supplier, elementType);
        } catch (DataAccessException ignoreEx) {
            return defaultValue;
        }
    }

    /**
     * Returns the value of the result, as an iterator of result values.
     *
     * @return the value of the result, which could be null.
     * @exception DataAccessException thrown if an error occurs during conversion of the result to the required type.
     */
    @SuppressWarnings("unchecked")
    default <T> Iterator<T> asIterator() throws DataAccessException {
        return ((List<T>)asList()).iterator();
    }

    /**
     * Returns the value of the result, as a map.
     *
     * @return the value of the result, which could be null.
     * @exception DataAccessException thrown if an error occurs during conversion of the result to the required type.
     */
    @SuppressWarnings("unchecked")
    default <K, V> Map<K, V> asMap() throws DataAccessException {
        return as(Map.class);
    }

    /**
     * Returns the value of the result, as a map.
     *
     * @param defaultValue a default value to use as the return value, if the result could not be converted to a map of
     *        values.
     * @return the value of the result, which could be null.
     */
    default <K, V> Map<K, V> asMap(Map<K, V> defaultValue) {
        try {
            return asMap();
        } catch (DataAccessException ignoreEx) {
            return defaultValue;
        }
    }

    /**
     * Returns the value of the result, as a map.
     *
     * @param mapType the type of map to create
     * @return the value of the result, which could be null.
     * @exception DataAccessException thrown if an error occurs during conversion of the result to the required type.
     */
    default <K, V> Map<K, V> asMapType(Class<Map<K, V>> mapType) throws DataAccessException {
        return asMapType(() -> TypeUtil.instantiateClass(mapType));
    }

    /**
     * Returns the value of the result, as a map.
     *
     * @param mapType the type of map to create.
     * @param defaultValue a default value to use as the return value, if the result could not be converted to a map of
     *        values.
     * @return the value of the result, which could be null.
     */
    default <K, V> Map<K, V> asMapType(Class<Map<K, V>> mapType, Map<K, V> defaultValue) {
        try {
            return asMapType(mapType);
        } catch (DataAccessException ignoreEx) {
            return defaultValue;
        }
    }

    /**
     * Returns the value of the result as a map of the given key and value types.
     *
     * @param supplier the supplier of the map to which value entries are to be added.
     * @return the value of the result, which could be null.
     */
    default <K, V> Map<K, V> asMapType(Supplier<Map<K, V>> supplier) {
        return asMapTypeOf(supplier, (Class<K>)Object.class, (Class<V>)Object.class);
    }

    /**
     * Returns the value of the result as a map of the given key and value types.
     *
     * @param supplier the supplier of the map to which value entries are to be added.
     * @param defaultValue a default value to use as the return value if the result could not be converted to a map.
     * @return the value of the result, which could be null.
     */
    default <K, V> Map<K, V> asMapType(Supplier<Map<K, V>> supplier, Map<K, V> defaultValue) {
        try {
            return asMapType(supplier);
        } catch (DataAccessException ignoreEx) {
            return defaultValue;
        }
    }

    /**
     * Returns the value of the result as a map of the given key and value types.
     *
     * @param keyType the type of the keys of the map.
     * @param valueType the type of the values of the map.
     * @return the value of the result, which could be null.
     * @exception DataAccessException thrown if an error occurs during conversion of the result to the required type.
     */
    default <K, V> Map<K, V> asMapOf(Class<K> keyType, Class<V> valueType) throws DataAccessException {
        return asMapTypeOf(HashMap::new, keyType, valueType);
    }

    /**
     * Returns the value of the result as a map of the given key and value types.
     *
     * @param keyType the type of the keys of the map.
     * @param valueType the type of the values of the map.
     * @param defaultValue a default value to use as the return value if the result could not be converted to a map.
     * @return the value of the result, which could be null.
     */
    default <K, V> Map<K, V> asMapOf(Class<K> keyType, Class<V> valueType, Map<K, V> defaultValue) {
        try {
            return asMapOf(keyType, valueType);
        } catch (DataAccessException ignoreEx) {
            return defaultValue;
        }
    }

    /**
     * Returns the value of the result as a map of the given key and value types.
     *
     * @param mapType the type of map to create.
     * @param keyType the type of the keys of the map.
     * @param valueType the type of the values of the map.
     * @return the value of the result, which could be null.
     */
    default <K, V> Map<K, V> asMapTypeOf(Class<Map<K, V>> mapType, Class<K> keyType, Class<V> valueType) {
        return asMapTypeOf(() -> TypeUtil.instantiateClass(mapType), keyType, valueType);
    }

    /**
     * Returns the value of the result as a map of the given key and value types.
     *
     * @param mapType the type of map to create.
     * @param keyType the type of the keys of the map.
     * @param valueType the type of the values of the map.
     * @param defaultValue a default value to use as the return value if the result could not be converted to a map.
     * @return the value of the result, which could be null.
     */
    default <K, V> Map<K, V> asMapTypeOf(Class<Map<K, V>> mapType, Class<K> keyType, Class<V> valueType, Map<K, V> defaultValue) {
        try {
            return asMapTypeOf(mapType, keyType, valueType);
        } catch (DataAccessException ignoreEx) {
            return defaultValue;
        }
    }

    /**
     * Returns the value of the result as a map of the given key and value types.
     *
     * @param supplier the supplier of the map to which value entries are to be added.
     * @param keyType the type of the keys of the map.
     * @param valueType the type of the values of the map.
     * @return the value of the result, which could be null.
     */
    <K, V> Map<K, V> asMapTypeOf(Supplier<Map<K, V>> supplier, Class<K> keyType, Class<V> valueType);

    /**
     * Returns the value of the result as a map of the given key and value types.
     *
     * @param supplier the supplier of the map to which value entries are to be added.
     * @param keyType the type of the keys of the map.
     * @param valueType the type of the values of the map.
     * @param defaultValue a default value to use as the return value if the result could not be converted to a map.
     * @return the value of the result, which could be null.
     */
    default <K, V> Map<K, V> asMapTypeOf(Supplier<Map<K, V>> supplier, Class<K> keyType, Class<V> valueType, Map<K, V> defaultValue) {
        try {
            return asMapTypeOf(supplier, keyType, valueType);
        } catch (DataAccessException ignoreEx) {
            return defaultValue;
        }
    }
}
