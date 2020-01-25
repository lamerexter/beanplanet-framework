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

//   @SuppressWarnings("unchecked")
//   public <T> T convert(Object value, Class<T> targetType) throws UnsupportedTypeConversionException {
//      Assert.notNull(targetType, "The target type of the conversion may not be null");
//      if (value == null) {
//         return null;
//      }
//
//      targetType = (Class<T>) TypeUtil.ensureNonPrimitiveType(targetType);
//      checkLoaded();
//
//      // -------------------------------------------------------------------------
//      // If source type is already of the target type then return it immediately
//      // -------------------------------------------------------------------------
//      if (targetType.isAssignableFrom(value.getClass())) {
//         return (T) value;
//      }
//
//      // ------------------------------------------------------------------------
//      // Attempt to find a direct conversion from source to target type, through
//      // the source class hierarchy, if required.
//      // ------------------------------------------------------------------------
//      TypeConverter matchingConverter;
//      for (Class<?> concreteType = value.getClass(); concreteType != null && concreteType != Object.class; concreteType = concreteType.getSuperclass()) {
//         matchingConverter = lookup(concreteType, targetType);
//         if (matchingConverter != null) {
//            return matchingConverter.convert(value, targetType);
//         }
//      }
//
//      // ------------------------------------------------------------------------
//      // Attempt to find a conversion from a source interface(s) to the target type.
//      // ------------------------------------------------------------------------
//      for (Class<?> iType : TypeUtil.getInterfacesAndSuperInterfaces(value.getClass())) {
//         matchingConverter = lookup(iType, targetType);
//         if (matchingConverter != null) {
//            return matchingConverter.convert(value, targetType);
//         }
//      }
//
//      // ------------------------------------------------------------------------
//      // Try registered converters whose source and target types are instances of
//      // the required source and target types (excluding the Object type on this run).
//      // ------------------------------------------------------------------------
//      for (Map.Entry<Class<?>, ConcurrentHashMap<Class<?>, CompositeTypeConverter>> targetEntry : converters.entrySet()) {
//         for (Map.Entry<Class<?>, CompositeTypeConverter> sourceTypeConverterEntry : targetEntry.getValue().entrySet()) {
//            if (sourceTypeConverterEntry.getKey() != Object.class
//                && sourceTypeConverterEntry.getKey().isAssignableFrom(value.getClass())
//                && targetEntry.getKey() != Object.class && targetType.isAssignableFrom(targetEntry.getKey())) {
//               CompositeTypeConverter compositeConverter = sourceTypeConverterEntry.getValue();
//               T result = compositeConverter.convert(value, targetType);
//               // if (useHeuristics) {
//               // if (logger.isInfoEnabled()) {
//               // logger.info("Detected explicit type conversion capability [matchingConverter="
//               // + (matchingConverter instanceof CompositeTypeConverter &&
//               // ((CompositeTypeConverter)matchingConverter).length() == 1 ?
//               // ((CompositeTypeConverter)matchingConverter).iterator().next().getClass().getName()
//               // : matchingConverter.getClass().getName()) + ", declaredFromType="
//               // + TypeUtil.getDisplayNameForType(fromTypeToAxisToNodeTestMapEntry.getKey())
//               // + ", actualFromType=" + TypeUtil.getDisplayNameForType(from.getClass())
//               // + ", axisStep=" + axisStep
//               // + "]: adding additional capability to improve future performance ...");
//               //
//               // }
//               // addNavigator(from.getClass(),
//               // canonicalAxisName,
//               // ((NodeTest) axisStep.getNodeTest()).getClass(),
//               // (navigator.length() == 1 ? navigator.iterator().next() : navigator));
//               // }
//               return result;
//            }
//         }
//      }
//
//      // for (Map.Entry<Class<?>, ConcurrentHashMap<Class<?>, CompositeTypeConverter>> targetEntry :
//      // converters.entrySet()) {
//      // if (targetType.isAssignableFrom(targetEntry.getKey())) {
//      // for (Map.Entry<Class<?>, CompositeTypeConverter> sourceTypeConverterEntry : targetEntry.getValue()
//      // .entrySet()) {
//      // if (!sourceTypeConverterEntry.getKey().equals(Object.class)
//      // && sourceTypeConverterEntry.getKey().isAssignableFrom(value.getClass())) {
//      // return sourceTypeConverterEntry.getValue().convert(value, targetType);
//      // }
//      // }
//      // }
//      // }
//
//      // ------------------------------------------------------------------------
//      // Try registered converters whose target types are assignable from the required
//      // target type.
//      // ------------------------------------------------------------------------
//      // for (Map.Entry<Class<?>, ConcurrentHashMap<Class<?>, CompositeTypeConverter>> targetEntry :
//      // converters.entrySet()) {
//      // if (targetEntry.getKey().isAssignableFrom(targetType)) {
//      // for (Map.Entry<Class<?>, CompositeTypeConverter> sourceTypeConverterEntry : targetEntry.getValue()
//      // .entrySet()) {
//      // if (!sourceTypeConverterEntry.getKey().equals(Object.class)
//      // && sourceTypeConverterEntry.getKey().isAssignableFrom(value.getClass())) {
//      // try {
//      // Object result = sourceTypeConverterEntry.getValue().convert(value, targetType);
//      // if (result != null && targetType.isAssignableFrom(result.getClass())) {
//      // return (T) result;
//      // }
//      // } catch (UnsupportedTypeConversionException utcEx) {
//      // // Ignore this conversion attempt
//      // }
//      // }
//      // }
//      // }
//      // }
//
//      // ------------------------------------------------------------------------
//      // Attempt to find a conversion from Object to target type.
//      // ------------------------------------------------------------------------
//      matchingConverter = lookup(Object.class, targetType);
//      if (matchingConverter != null) {
//         return matchingConverter.convert(value, targetType);
//      }
//
//      throw new UnsupportedTypeConversionException("Unable to convert the specified value to the required type [valueType="
//                                                   + (value == null ? null : value.getClass().getName())
//                                                   + ", value="
//                                                   + value
//                                                   + ", targetType="
//                                                   + TypeUtil.getDisplayNameForType(targetType) + "] ");
//   }

    @Override
    public <T> T convert(Object value, Class<T> targetType) throws UnsupportedTypeConversionException {
        checkLoaded();

        //--------------------------------------------------------------------------------------------------------------
        // Pass through null values
        //--------------------------------------------------------------------------------------------------------------
        if (value == null) return null;

        //--------------------------------------------------------------------------------------------------------------
        // If source type is already of the target type then return it immediately
        //--------------------------------------------------------------------------------------------------------------
        if (targetType.isAssignableFrom(value.getClass())) return (T)value;

        //--------------------------------------------------------------------------------------------------------------
        // Attempt to find a corresponding converter in the registry
        //--------------------------------------------------------------------------------------------------------------
        return lookup(value.getClass(), targetType).orElseThrow(() -> new UnsupportedTypeConversionException()).convert(value, targetType);
    }

    public static void main(String ... args) {
       SystemTypeConverter.getInstance().convert(1L, String.class);
    }
}
