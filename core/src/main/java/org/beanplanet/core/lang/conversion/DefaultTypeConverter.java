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

public abstract class DefaultTypeConverter extends AbstractTypeConverterRegistry implements TypeConverter, Logger {
//   protected TypeConverterLoader loader = new PackageScanTypeConverterLoader();
//   /**
//    * Whether the type converter should use and apply heuristics in determining implicit type conversion capabilities of
//    * registered converters. Defaults to <code>true</code>.
//    */
//   protected boolean useHeuristics = true;
//
//   private boolean isLoaded = false;
//
//   private static DefaultTypeConverter instance;
//
//   /**
//    * Returns the system default type converter.
//    *
//    * @return a system-wide type converter for general use.
//    */
//   public synchronized static TypeConverter getInstance() {
//      if (instance == null) {
//         instance = new DefaultTypeConverter();
//      }
//
//      return instance;
//   }
//
//   /**
//    * Gets whether the type converter should use and apply heuristics in determining implicit type conversion
//    * capabilities of registered converters. Essentially, if an additional type conversion capability of a registered
//    * converter is discovered (when heuristics are turned on) then the registry will be updated with the new capability
//    * to optimise and improve future performance.
//    *
//    * <p>
//    * A registered type converter may declare generalised type conversion capabilities over <em>interfaces</em> or
//    * superclasses in order to support a wide range of type conversions. This implies it may be capable of converting
//    * many specialised instances. With heuristics turned on, such implicit conversions will be detected and added to the
//    * registry as explicit conversions.
//    * </p>
//    *
//    * @return true if heuristics are turned on in this registry.
//    */
//   public boolean getUseHeuristics() {
//      return useHeuristics;
//   }
//
//   /**
//    * Sets whether the type converter should use and apply heuristics in determining implicit type conversion
//    * capabilities of registered converters. Essentially, if an additional type conversion capability of a registered
//    * converter is discovered (when heuristics are turned on) then the registry will be updated with the new capability
//    * to optimise and improve future performance.
//    *
//    * <p>
//    * A registered type converter may declare generalised type conversion capabilities over <em>interfaces</em> or
//    * <em>superclasses</em> in order to support a wide range of type conversions. This implies it may be capable of
//    * converting many specialised instances. With heuristics turned on, such implicit conversions will be detected and
//    * added to the registry as explicit conversions.
//    * </p>
//    *
//    * @return the useHeuristics
//    * @param useHeuristics the useHeuristics to set
//    */
//   public void setUseHeuristics(boolean useHeuristics) {
//      this.useHeuristics = useHeuristics;
//   }
//
//   /**
//    * Returns the type converter loader that will populate this type converter
//    *
//    * @return the loader that will be called to populate this type converter with component converters.
//    */
//   public TypeConverterLoader getLoader() {
//      return loader;
//   }
//
//   /**
//    * Sets the type converter loader that will populate this type converter
//    *
//    * @param loader the loader that will be called to populate this type converter with component converters.
//    */
//   public synchronized void setLoader(TypeConverterLoader loader) {
//      this.loader = loader;
//      isLoaded = false;
//   }
//
//   protected void loadTypeConverters() {
//      clear();
//      loader.load(this);
//   }
//
//   protected synchronized void checkLoaded() {
//      if (!isLoaded) {
//         loadTypeConverters();
//         isLoaded = true;
//      }
//   }
//
//   @SuppressWarnings("unchecked")
//   public <T> T convert(Object value, Class<T> targetType) throws UnsupportedTypeConversionException {
//      Assert.notNull(targetType, "The target type of the conversion may not be null");
//      if (value == null) {
//         return null;
//      }
//
//      targetType = (Class<T>)TypeUtil.ensureNonPrimitiveType(targetType);
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
//               // ((CompositeTypeConverter)matchingConverter).size() == 1 ?
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
//               // (navigator.size() == 1 ? navigator.iterator().next() : navigator));
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
}
