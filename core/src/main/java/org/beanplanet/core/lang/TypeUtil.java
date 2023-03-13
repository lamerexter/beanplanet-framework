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

package org.beanplanet.core.lang;

import org.beanplanet.core.UncheckedException;
import org.beanplanet.core.lang.conversion.TypeConverter;
import org.beanplanet.core.models.tree.TreeNode;
import org.beanplanet.core.util.ExceptionUtil;
import org.beanplanet.core.util.StringUtil;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Arrays.*;
import static java.util.Arrays.stream;
import static java.util.Objects.isNull;

/**
 * A static utility class for the everything <code>type-related</code> related.
 *
 * @author Gary Watson
 */
public final class TypeUtil {

    private static final Set<Class<?>> PRIMITIVE_TYPES = new HashSet<>();
    private static final Set<Class<?>> PRIMITIVE_TYPE_WRAPPERS = new HashSet<>();

    private static final Set<Class<?>> INTEGER_NUMERIC_TYPES = new HashSet<>();
    private static final Set<Class<?>> NUMERIC_TYPES = new HashSet<>();

    private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER_TYPE_TO_PRIMITIVE_TYPE = new HashMap<>();

    private static final Map<Class<?>, Class<?>> PRIMITIVE_TYPE_TO_PRIMITIVE_WRAPPER_TYPE = new HashMap<>();

    private static final Map<String, Class<?>> NAME_TO_PRIMITIVE_CLASS = new HashMap<>();

    private static final Object[] EMPTY_PARAMS = new Object[]{};
    private static final Class<?>[] EMPTY_ARG_TYPES = new Class<?>[]{};

    static {
        PRIMITIVE_TYPES.add(boolean.class);
        PRIMITIVE_TYPES.add(byte.class);
        PRIMITIVE_TYPES.add(char.class);
        PRIMITIVE_TYPES.add(double.class);
        PRIMITIVE_TYPES.add(float.class);
        PRIMITIVE_TYPES.add(int.class);
        PRIMITIVE_TYPES.add(long.class);
        PRIMITIVE_TYPES.add(short.class);

        PRIMITIVE_TYPE_WRAPPERS.add(Boolean.class);
        PRIMITIVE_TYPE_WRAPPERS.add(Byte.class);
        PRIMITIVE_TYPE_WRAPPERS.add(Character.class);
        PRIMITIVE_TYPE_WRAPPERS.add(Double.class);
        PRIMITIVE_TYPE_WRAPPERS.add(Float.class);
        PRIMITIVE_TYPE_WRAPPERS.add(Integer.class);
        PRIMITIVE_TYPE_WRAPPERS.add(Long.class);
        PRIMITIVE_TYPE_WRAPPERS.add(Short.class);

        PRIMITIVE_WRAPPER_TYPE_TO_PRIMITIVE_TYPE.put(Boolean.class, boolean.class);
        PRIMITIVE_WRAPPER_TYPE_TO_PRIMITIVE_TYPE.put(Byte.class, byte.class);
        PRIMITIVE_WRAPPER_TYPE_TO_PRIMITIVE_TYPE.put(Character.class, char.class);
        PRIMITIVE_WRAPPER_TYPE_TO_PRIMITIVE_TYPE.put(Double.class, double.class);
        PRIMITIVE_WRAPPER_TYPE_TO_PRIMITIVE_TYPE.put(Float.class, float.class);
        PRIMITIVE_WRAPPER_TYPE_TO_PRIMITIVE_TYPE.put(Integer.class, int.class);
        PRIMITIVE_WRAPPER_TYPE_TO_PRIMITIVE_TYPE.put(Long.class, long.class);
        PRIMITIVE_WRAPPER_TYPE_TO_PRIMITIVE_TYPE.put(Short.class, short.class);

        PRIMITIVE_TYPE_TO_PRIMITIVE_WRAPPER_TYPE.put(boolean.class, Boolean.class);
        PRIMITIVE_TYPE_TO_PRIMITIVE_WRAPPER_TYPE.put(byte.class, Byte.class);
        PRIMITIVE_TYPE_TO_PRIMITIVE_WRAPPER_TYPE.put(char.class, Character.class);
        PRIMITIVE_TYPE_TO_PRIMITIVE_WRAPPER_TYPE.put(double.class, Double.class);
        PRIMITIVE_TYPE_TO_PRIMITIVE_WRAPPER_TYPE.put(float.class, Float.class);
        PRIMITIVE_TYPE_TO_PRIMITIVE_WRAPPER_TYPE.put(int.class, Integer.class);
        PRIMITIVE_TYPE_TO_PRIMITIVE_WRAPPER_TYPE.put(long.class, Long.class);
        PRIMITIVE_TYPE_TO_PRIMITIVE_WRAPPER_TYPE.put(short.class, Short.class);

        NAME_TO_PRIMITIVE_CLASS.put("boolean", boolean.class);
        NAME_TO_PRIMITIVE_CLASS.put("byte", byte.class);
        NAME_TO_PRIMITIVE_CLASS.put("char", char.class);
        NAME_TO_PRIMITIVE_CLASS.put("double", double.class);
        NAME_TO_PRIMITIVE_CLASS.put("float", float.class);
        NAME_TO_PRIMITIVE_CLASS.put("int", int.class);
        NAME_TO_PRIMITIVE_CLASS.put("long", long.class);
        NAME_TO_PRIMITIVE_CLASS.put("short", short.class);

        INTEGER_NUMERIC_TYPES.add(BigInteger.class);
        INTEGER_NUMERIC_TYPES.add(byte.class);
        INTEGER_NUMERIC_TYPES.add(Byte.class);
        INTEGER_NUMERIC_TYPES.add(int.class);
        INTEGER_NUMERIC_TYPES.add(Integer.class);
        INTEGER_NUMERIC_TYPES.add(long.class);
        INTEGER_NUMERIC_TYPES.add(Long.class);
        INTEGER_NUMERIC_TYPES.add(short.class);
        INTEGER_NUMERIC_TYPES.add(Short.class);

        NUMERIC_TYPES.add(BigDecimal.class);
        NUMERIC_TYPES.add(BigInteger.class);
        NUMERIC_TYPES.add(byte.class);
        NUMERIC_TYPES.add(Byte.class);
        NUMERIC_TYPES.add(double.class);
        NUMERIC_TYPES.add(Double.class);
        NUMERIC_TYPES.add(float.class);
        NUMERIC_TYPES.add(Float.class);
        NUMERIC_TYPES.add(int.class);
        NUMERIC_TYPES.add(Integer.class);
        NUMERIC_TYPES.add(long.class);
        NUMERIC_TYPES.add(Long.class);
        NUMERIC_TYPES.add(short.class);
        NUMERIC_TYPES.add(Short.class);
    }

    private TypeUtil() {}

    /**
     * Determines the common superclass (not superinterfaces or any other type hierarchy) of a number of classes. This
     * implementation handles mixed primitive and reference types by autoboxing primitives. In such eventualities, the
     * equivalent primitive wrappers are used.
     *
     * @param classes the classes whose common superclass is to be determined.
     * @return the common superclass of all of the given classes, which may be a primitive type (where they are all the
     * same primitive type), a primitive wrapper type (where there are mixed primitive and non-primitive types), the
     * <code>Object</code> type or <code>null</code> where the common superclass could not be determined.
     */
    public static Class<?> determineCommonSuperclass(Class<?>... classes) {
        return determineCommonSuperclass(true, classes);
    }

    /**
     * Determines the common superclass (not superinterfaces or any other type hierarchy) of a number of classes,
     * autoboxing primitives where there are mixed primitives ans non-primitives specified. In such eventualities, the
     * equivalent primitive wrappers are used.
     *
     * @param classes      the classes whose common superclass is to be determined.
     * @param autoboxMixed autobox primitives, where mixed primitive and non-primitives were specified.
     * @return the common superclass of all of the given classes, which may be a primitive type (where they are all the
     * same primitive type), a primitive wrapper type (where there are mixed primitive and non-primitive types), the
     * <code>Object</code> type or <code>null</code> where the common superclass could not be determined (only possible
     * in this implementation when all classes specified are mixed primitive types).
     */
    public static Class<?> determineCommonSuperclass(boolean autoboxMixed, Class<?>... classes) {
        if (classes == null || classes.length == 0) return null;

        final Class<?>[] commonSuperclass = {classes[0]};
        boolean allPrimitives = stream(classes).allMatch(Class::isPrimitive);
        if (allPrimitives) {
            return stream(classes).allMatch(c -> commonSuperclass[0] == c) ? commonSuperclass[0] : null;
        }

        boolean somePrimitives = stream(classes).anyMatch(Class::isPrimitive);
        if (somePrimitives && !autoboxMixed) return null;

        commonSuperclass[0] = ensureNonPrimitiveType(commonSuperclass[0]);
        while (!stream(classes).map(TypeUtil::ensureNonPrimitiveType).allMatch(c -> commonSuperclass[0].isAssignableFrom(c))) {
            commonSuperclass[0] = commonSuperclass[0].getSuperclass();
        }

        return commonSuperclass[0];
    }

    /**
     * Given a class, this method return the class name element, or base name, of
     * the fully quialified class name.
     * <p>
     * For example, given <code>com.acme.HelloWorld</code>, this method will
     * return <code>HelloWorld</code>.
     *
     * @param type the type whose base name is to be determied
     * @return the last element, or base name, of the specified class.
     */
    public static String getBaseName(java.lang.Class<?> type) {
        return type.getSimpleName();
    }

    /**
     * Given a class, this method returns the package name of the class, if any.
     * <p>
     * For example, given <code>com.acme.HelloWorld</code>, this method will
     * return <code>com.acme</code>.
     *
     * @param type the type whose package name is to be determined.
     * @return the class package name or null if the class does not have an associated package.
     */
    public static String getPackageName(java.lang.Class<?> type) {
        Package pkg = type.getPackage();
        return pkg == null ? null : pkg.getName();
    }

    public static Class<?> loadClassOrNull(String className) {
        Class<?> type = NAME_TO_PRIMITIVE_CLASS.get(className);
        if (type != null) {
            return type;
        }

        if ( isVoidtype(className) ) return void.class;

        try {
            type = Class.forName(className);
        } catch (java.lang.ClassNotFoundException cnfEx) {
            if (Thread.currentThread().getContextClassLoader() != null) {
                try {
                    type = Class.forName(className, true, Thread.currentThread().getContextClassLoader());
                } catch (java.lang.ClassNotFoundException cnfThreadEx) {
                }
            }
        }

        int lastDotPos = className.lastIndexOf('.');
        if (type == null && lastDotPos >= 0) {
            String memberClassName = className.substring(0, lastDotPos) + "$" + className.substring(lastDotPos + 1);
            try {
                type = Class.forName(memberClassName);
            } catch (java.lang.ClassNotFoundException cnfEx) {
                if (Thread.currentThread().getContextClassLoader() != null) {
                    try {
                        type = Class.forName(memberClassName, true, Thread.currentThread().getContextClassLoader());
                    } catch (java.lang.ClassNotFoundException cnfThreadEx) {
                    }
                }
            }
        }

        return type;
    }

    public static java.lang.Class<?> loadClass(String className) throws TypeNotFoundException {
        Class<?> type = NAME_TO_PRIMITIVE_CLASS.get(className);
        if (type != null) {
            return type;
        }

        if ( isVoidtype(className) ) return void.class;

        try {
            type = Class.forName(className);
        } catch (java.lang.ClassNotFoundException | ClassCastException ex) {
            // XClass not found under calling context, try the current threads
            // classloader, if any
            if (Thread.currentThread().getContextClassLoader() != null) {
                try {
                    type = Class.forName(className, true, Thread.currentThread().getContextClassLoader());
                } catch (java.lang.ClassNotFoundException | ClassCastException threadExx) {
                    throw new TypeNotFoundException("The given class [" + className + "] was not found or could not be loaded: ", threadExx);
                }
            }
        }

        return type;
    }

    public static java.lang.Class<?> loadClass(String className, ClassLoader classLoader) {
        return loadClass(className, true, classLoader);
    }

    public static ClassLoader getClassLoaderInContext(Object obj) {
        Assert.notNull(obj, "The object whose classloader is to be determined must not be null");
        return getClassLoaderInContext(obj.getClass());
    }

    public static ClassLoader getClassLoaderInContext(Class<?> type) {
        if (Thread.currentThread().getContextClassLoader() != null) {
            return Thread.currentThread().getContextClassLoader();
        } else {
            return type.getClassLoader();
        }
    }

    public static java.lang.Class<?> loadClass(String className, boolean initialiseClass, ClassLoader classLoader) {
        Class<?> type = NAME_TO_PRIMITIVE_CLASS.get(className);
        if (type != null) {
            return type;
        }

        if ( isVoidtype(className) ) return void.class;

        try {
            type = Class.forName(className, initialiseClass, classLoader);
        } catch (java.lang.ClassNotFoundException | NoClassDefFoundError cnfEx) {
            // XClass not found under calling context, try the current threads
            // classloader, if any
            if (Thread.currentThread().getContextClassLoader() != null && Thread.currentThread().getContextClassLoader() != classLoader) {
                try {
                    type = Class.forName(className, initialiseClass, Thread.currentThread().getContextClassLoader());
                } catch (java.lang.ClassNotFoundException | NoClassDefFoundError cnfThreadEx) {
                    throw new TypeNotFoundException("The given class [" + className + "] was not found: ", cnfEx);
                }
            }
        }

        return type;
    }

    public static Class<?>[] loadClasses(ClassLoader classLoader, String classNames[]) {
        Class<?> loadedClasses[] = new Class[classNames.length];
        for (int n = 0; n < classNames.length; n++) {
            loadedClasses[n] = loadClass(classNames[n], classLoader);
        }

        return loadedClasses;
    }

    // GAW TODO: Sort out exceptions raised by these methods!
    public static Object instantiateClass(String typeName) {
        return instantiateClass(typeName, new Object[]{});
    }

    public static Object instantiateClass(String typeName, Class<?> ctorArgTypes[], Object ctorArgVals[]) {
        Class<?> type = null;
        try {
            type = loadClass(typeName);
            Constructor<?> ctor = type.getConstructor(ctorArgTypes);
            return ctor.newInstance(ctorArgVals);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new UncheckedException(e);
        }
    }

    public static Object instantiateClass(String typeName, Object ctorArgVals[]) {
        Class<?> type = loadClass(typeName);
        return instantiateClass(type, ctorArgVals);
    }

    public static <T> T instantiateClass(Class<T> type) throws TypeNotFoundException {
        final Object NO_ARGS[] = new Object[]{};
        return instantiateClass(type, NO_ARGS);
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<Constructor<T>> getCallableConstructor(Class<T> type, Object[] ctorParameterValues) {
        return stream((Constructor<T>[]) type.getDeclaredConstructors())
                   .filter(c -> c.getParameterCount() == (ctorParameterValues == null ? 0 : ctorParameterValues.length))
                   .filter(c -> {
                       for (int n = 0; n < c.getParameterCount(); n++) {
                           if (!(ctorParameterValues[n] == null || c.getParameterTypes()[n].isAssignableFrom(ctorParameterValues[n].getClass()))) {
                               return false;
                           }
                       }
                       return true;
                   })
                   .findFirst();
    }

    @SuppressWarnings("unchecked")
    public static <T> T instantiateClass(final Class<T> type, Object ctorArgVals[]) {
        try {
            return getCallableConstructor(type, ctorArgVals).orElseThrow(() -> new UncheckedException("Unable to instantiate class, type=\"" + type.getName()
                                                                                                      + "\": no suitable constructor found"))
                                                            .newInstance(ctorArgVals);
        } catch (InstantiationException instantiateEx) {
            throw new UncheckedException("Unable to instantiate class, type=\"" + type.getName()
                                         + "\" - it is likely the type does not have a no-arg constructor or invalid arguments have been specified: ", instantiateEx);
        } catch (IllegalAccessException accessEx) {
            throw new UncheckedException("Unable to instantiate class, \"" + type.getName() + "\" - the constructor/method is not accessible: ", accessEx);
        } catch (InvocationTargetException invocationEx) {
            throw new UncheckedException("Unable to instantiate class, \"" + type.getName() + "\" - the constructor threw an exception: ", invocationEx);
        }
    }

    public static boolean isPrimitiveType(String typeName) {
        return NAME_TO_PRIMITIVE_CLASS.containsKey(typeName);
    }

    public static boolean isPrimitiveType(Class<?> type) {
        return PRIMITIVE_TYPES.contains(type);
    }

    public static boolean isPrimitiveTypeWrapperClass(Class<?> type) {
        return PRIMITIVE_TYPE_WRAPPERS.contains(type);
    }

    public static boolean isPrimitiveTypeOrWrapperClass(Class<?> type) {
        return PRIMITIVE_TYPES.contains(type) || PRIMITIVE_TYPE_WRAPPERS.contains(type);
    }

    public static boolean isVoidtype(Class<?> type) {
        return type == void.class;
    }

    public static boolean isVoidtype(String type) {
        return void.class.getSimpleName().equals(type);
    }

    /**
     * Determines whether the given type is a known numeric type, which must be one of:
     * <ul>
     * <li>BigDecimal</li>
     * <li>BigInteger</li>
     * <li>byte</li>
     * <li>Byte</li>
     * <li>double</li>
     * <li>Double</li>
     * <li>float</li>
     * <li>Float</li>
     * <li>int</li>
     * <li>Integer</li>
     * <li>long</li>
     * <li>Long</li>
     * <li>short</li>
     * <li>Short</li>
     * </ul>
     *
     * @param type the type whose numeric status is to be determined.
     * @return true if the given type is numeric, false otherwise.
     */
    public static boolean isNumericType(Class<?> type) {
        return NUMERIC_TYPES.contains(type);
    }

    /**
     * Determines whether the given type is a known integer numeric type, which must be one of:
     * <ul>
     * <li>BigInteger</li>
     * <li>byte</li>
     * <li>Byte</li>
     * <li>int</li>
     * <li>Integer</li>
     * <li>long</li>
     * <li>Long</li>
     * <li>short</li>
     * <li>Short</li>
     * </ul>
     *
     * @param type the type whose numeric status is to be determined.
     * @return true if the given type is an integer type, false otherwise.
     */
    public static boolean isIntegerNumericType(Class<?> type) {
        return INTEGER_NUMERIC_TYPES.contains(type);
    }

    /**
     * Returns the primitive type for a given primitive wrapper type.
     *
     * @param type the primitive wrapper type or some other type.
     * @return the equivalent primitive type or the original type if not a primnitive wrapper type.
     */
    public static Class<?> primitiveTypeFor(Class<?> type) {
        Class<?> result = PRIMITIVE_WRAPPER_TYPE_TO_PRIMITIVE_TYPE.get(type);
        return result == null ? type : result;
    }

    public static Class<?> ensureNonPrimitiveType(Class<?> primitiveOrNonPrimitiveType) {
        if (primitiveOrNonPrimitiveType == null) {
            return null;
        }

        Class<?> primitiveWrapperType = PRIMITIVE_TYPE_TO_PRIMITIVE_WRAPPER_TYPE.get(primitiveOrNonPrimitiveType);
        if (primitiveWrapperType != null) {
            return primitiveWrapperType;
        }

        return primitiveOrNonPrimitiveType;
    }

    /**
     * Returns the primitive wrapper type for a given primitive type.
     *
     * @param primitiveType the primitive type whose wrapper type is to be returned.
     * @return the primitive wrapper type for the primitive type, or null if the type specified
     * was not a primitive type.
     */
    public static Class<?> getPrimitiveWrapperType(Class<?> primitiveType) {
        if (isPrimitiveTypeWrapperClass(primitiveType)) return primitiveType;

        return PRIMITIVE_TYPE_TO_PRIMITIVE_WRAPPER_TYPE.get(primitiveType);
    }

    public static Class<?>[] ensureNonPrimitiveTypes(Class<?> primitiveOrNonPrimitiveTypes[]) {
        if (primitiveOrNonPrimitiveTypes == null) {
            return null;
        }

        Class<?> newTypes[] = new Class<?>[primitiveOrNonPrimitiveTypes.length];

        for (int n = 0; n < primitiveOrNonPrimitiveTypes.length; n++) {
            newTypes[n] = ensureNonPrimitiveType(primitiveOrNonPrimitiveTypes[n]);
        }

        return newTypes;
    }

    public static boolean areEquivalentPrimitiveOrPrimitiveWrappers(Class<?> aClass, Class<?> bClass) {
        return isPrimitiveTypeOrWrapperClass(aClass)
               && isPrimitiveTypeOrWrapperClass(bClass)
               && (aClass == bClass || PRIMITIVE_TYPE_TO_PRIMITIVE_WRAPPER_TYPE.get(aClass) == bClass || PRIMITIVE_WRAPPER_TYPE_TO_PRIMITIVE_TYPE.get(aClass) == bClass);
    }

    public static Object invokeStaticMethod(Class<?> type, String methodName) {
        return invokeStaticMethod(type, methodName, (Object[]) null);
    }

    public static Object invokeStaticMethod(Method method, Object... params) {
        return invokeStaticMethod(method.getDeclaringClass(), method.getName(), params);
    }

    public static Object invokeStaticMethod(Class<?> type, String methodName, Object... params) {
        Method method = findMethod(type, Modifier.STATIC, methodName, null, paramTypes(params))
                            .orElseThrow(() -> new UncheckedException("Unable to invoke method, \"" + methodName
                                                                      + "\" on target, class=\"" + type.getName()
                                                                      + "\" - the method was not found: "));
        return invokeMethod(null, method, params);
    }

    private static Class<?>[] paramTypes(Object params[]) {
        return params == null ? null : stream(params).map(p -> p == null ? Object.class : p.getClass()).toArray(Class[]::new);
    }

    public static Stream<Method> streamMethods(Class<?> type, int modifiers, String name, Class<?> returnType, Class<?>... paramTypes) {

        return streamMethods(type)
                   .filter(m -> m.getName().equals(name))
                   .filter(m -> (m.getModifiers() & modifiers) == modifiers)
                   .filter(m -> returnType == null || returnType.isAssignableFrom(m.getReturnType()))
                   .filter(m -> paramTypes == null || m.getParameterCount() == paramTypes.length)
                   .filter(m -> {
                       if (paramTypes != null) {
                           for (int n = 0; n < paramTypes.length; n++) {
                               if (paramTypes[n] != null
                                   && !(m.getParameterTypes()[n].isAssignableFrom(paramTypes[n])
                                        || (m.getParameterTypes()[n] instanceof Class && paramTypes[n] instanceof Class)
                               )) {
                                   return false;
                               }
                           }
                       }
                       return true;
                   });
    }

    public static Stream<Constructor> streamConstructors(Class<?> type) {
        return stream(type.getDeclaredConstructors());
    }

    public static Stream<Method> streamMethods(Class<?> type) {

        return new TypeTree(Object.class, type)
                   .postorderStream()
                   .map(TreeNode::getManagedObject)
                   .flatMap(c -> stream(c.getDeclaredMethods()));
    }

    public static List<Method> findMethods(Class<?> type, int modifiers, String name, Class<?> returnType, Class<?>... paramTypes) {

        return streamMethods(type, modifiers, name, returnType, paramTypes).collect(Collectors.toList());
    }

    public static Optional<Method> findMethod(Class<?> type, int modifiers, String name, Class<?> returnType, Class<?>... paramTypes) {

        return streamMethods(type, modifiers, name, returnType, paramTypes).findFirst();
    }

    public static Object invokeMethod(Object target, Method method, Object... params) {
       return invokeMethod(null, target, method, params);
    }

    public static Object invokeMethod(TypeConverter typeConverter, Object target, Method method, Object... params) {
        Object[] convertedParams = convertParameters(typeConverter, method.getParameterTypes(), params);
        try {
            return method.invoke(Modifier.isStatic(method.getModifiers()) ? null : target, (convertedParams != null ? convertedParams : EMPTY_PARAMS));
        } catch (IllegalAccessException accessEx) {
            if (Modifier.isPublic(method.getModifiers())) {
                // Known issue where method is declared as public but reflection is
                // occasionally denied access. Attempt
                // to allow access in this case.
                try {
                    method.setAccessible(true);
                    return method.invoke(Modifier.isStatic(method.getModifiers()) ? null : target, convertedParams);
                } catch (IllegalAccessException accessEx1) {
                    throw new UncheckedException("Unable to invoke method, \"" + method.getName() + "\" on target bean, class=\"" + target.getClass().getName()
                                                 + "\" - the method is not accessible: ", accessEx1);
                } catch (InvocationTargetException invocationEx) {
                    throw ExceptionUtil.unwindAndRethrowRuntimeException(UncheckedException.class, "Unable to invoke method, \"" + method.getName()
                                                                                                   + "\" on target bean, class=\"" + target.getClass().getName() + "\" - the method threw an exception: ", invocationEx);
                }
            } else {
                throw new UncheckedException("Unable to invoke method, \"" + method.getName() + "\" on target bean, class=\"" + target.getClass().getName()
                                             + "\" - the method is not accessible: ", accessEx);
            }
        } catch (InvocationTargetException invocationEx) {
            throw ExceptionUtil.unwindAndRethrowRuntimeException(UncheckedException.class, "Unable to invoke method [" + method + "]"
                                                                                           + (target == null ? "" : " on target bean [class=" + target.getClass().getName() + "]: "), invocationEx);
        }
    }

    private static Object[] convertParameters(TypeConverter typeConverter, Class<?>[] parameterTypes, Object[] params) {
        // Does not handle varargs
        if ( typeConverter == null || params == null || parameterTypes.length != params.length) return params;

        final boolean conversionNeeded = IntStream.range(0, params.length)
                .filter(n -> params[n] != null)
                .anyMatch(n -> !parameterTypes[n].isAssignableFrom(params[n].getClass()));
        if ( !conversionNeeded ) return params;

        return IntStream.range(0, params.length)
                .mapToObj(n -> isNull(params[n]) || parameterTypes[n].isAssignableFrom(params[n].getClass()) ? params[n] : typeConverter.convert(params[n], parameterTypes[n]))
                .toArray(Object[]::new);
    }

    public static Stream<Field> streamFields(Class<?> type) {

        return new TypeTree(Object.class, type)
                   .stream()
                   .map(TreeNode::getManagedObject)
                   .flatMap(c -> stream(c.getDeclaredFields()));
    }

    public static Stream<Field> streamFields(Class<?> type, int modifiers, String name, Class<?> fieldType) {
        return streamFields(type)
                   .filter(f -> f.getName().equals(name))
                   .filter(f -> (f.getModifiers() & modifiers) == modifiers)
                   .filter(f -> fieldType == null || fieldType.isAssignableFrom(f.getType()));
    }

    public static List<Field> findFields(Class<?> type, int modifiers, String name, Class<?> fieldType) {
        return streamFields(type, modifiers, name, fieldType).collect(Collectors.toList());
    }


    public static Optional<Field> findField(Class<?> type, int modifiers, String name, Class<?> fieldType) {
        return streamFields(type, modifiers, name, fieldType).findFirst();
    }


    public static List<Field> findFields(Class<?> type) {

        return streamFields(type).collect(Collectors.toList());
    }

    /**
     * Convenience method to return the display name of a type
     *
     * @param type the type whose name is to be displayed
     * @return the string display name for the type, useful for debugging
     */
    public static String getDisplayNameForType(Class<?> type) {
        if (type == null) {
            return "null";
        }

        String name = null;
        if (type.isArray()) {
            name = getDisplayNameForType(type.getComponentType()) + "[]";
        } else {
            name = type.getName();
        }
        name = StringUtil.lTrim(name, "class ");
        name = StringUtil.lTrim(name, "java.lang.");
        name = StringUtil.lTrim(name, "java.util.");
        return name;
    }

    /**
     * Determined whether all of the given types are interfaces.
     *
     * @param classes the types to be determined as interfaces.
     * @return true, if all pf the given types are interfaces, false otherwise.
     */
    public static final boolean areAllInterfaces(Class<?> classes[]) {
        Assert.notNull(classes, "The list of classes may not be null");
        Assert.isTrue(classes.length > 0, "One or more classes must be specified");
        boolean allInterfaces = true;
        for (int n = 0; n < classes.length && allInterfaces; n++) {
            allInterfaces = classes[n].isInterface();
        }

        return allInterfaces;
    }

    public static Stream<Method> getMethodsInClassHierarchy(final Class<?> specificType) {
        return new TypeTree(specificType)
                   .preorderStream()
                   .map(TreeNode::getManagedObject)
                   .flatMap(type -> stream(type.getDeclaredMethods()));
    }

    /**
     * Given a possible array type, determines the number of dimensions of the array.
     *
     * @param fromType the possible array type whose dimensions are to be determined.
     * @return the number of dimensions of the array type or zero if the type is not an array or was null.
     */
    public static int determineArrayDimensions(Class<?> fromType) {
        if (fromType == null) return 0;

        int dimensions = 0;
        while (fromType.isArray()) {
            dimensions++;
            fromType = fromType.getComponentType();
        }

        return dimensions;
    }

    /**
     * Given a possible array type, determined the base component type of the array. That is, the innermost type of the
     * array that is not am array type.
     *
     * @param fromType the possible array type whose base component type is to be determined.
     * @return the base component type of the given array, or the type specified if it is not an array type.
     */
    public static Class<?> determineArrayBaseComponentType(final Class<?> fromType) {
        if (fromType == null) return null;

        Class<?> componentType = fromType;
        while (componentType.isArray()) {
            componentType = componentType.getComponentType();
        }

        return componentType;
    }

    /**
     * Returns the class type descriptor of a given class type. Optionally, if the class type represents
     * an array component type, the number of array dimensions may be specified. The type descriptions
     * returned adhere to the type returned from a call to <a href="http://docs.oracle.com/javase/6/docs/api/java/lang/Class.html#getName%28%29">Class#getName()</a>.
     *
     * @param type            the type or component type, if an array type description is to be returned.
     * @param arrayDimensions in the event the type specified is an array component, the number of dimensions of the
     *                        array type description to be returned (minimum 1) or zero if the type is not an array.
     * @return the class type description for the given type or array of component type.
     */
    public static String getForNameTypeDescription(Class<?> type, int arrayDimensions) {
        StringBuilder buf = new StringBuilder();
        buf.append(StringUtil.repeat("[", arrayDimensions));
        Class<?> currentClass = type;
        while (true) {
            if (isPrimitiveType(currentClass)) {
                char car;
                if (currentClass == int.class) {
                    car = 'I';
                } else if (currentClass == void.class) {
                    car = 'V';
                } else if (currentClass == boolean.class) {
                    car = 'Z';
                } else if (currentClass == byte.class) {
                    car = 'B';
                } else if (currentClass == char.class) {
                    car = 'C';
                } else if (currentClass == short.class) {
                    car = 'S';
                } else if (currentClass == double.class) {
                    car = 'D';
                } else if (currentClass == float.class) {
                    car = 'F';
                } else /* long */ {
                    car = 'J';
                }
                return buf.append(car).toString();
            } else if (currentClass.isArray()) {
                buf.append('[');
                currentClass = currentClass.getComponentType();
            } else {
                return buf.append('L').append(currentClass.getName()).append(";").toString();
            }
        }
    }

    /**
     * Returns the class type of a array class whose component type is specified.
     *
     * @param type            the type or component type, if an array type description is to be returned.
     * @param arrayDimensions in the event the type specified is an array component, the number of dimensions of the
     *                        array type description to be returned (minimum 1) or zero if the type is not an array.
     * @return the class type description for the given type or array of component type.
     * @see #getForNameTypeDescription(Class, int)
     */
    public static Class<?> forName(Class<?> type, int arrayDimensions) {
        if (arrayDimensions <= 0) {
            return type;
        }
        String classDescriptor = getForNameTypeDescription(type, arrayDimensions);
        return loadClass(classDescriptor);
    }
}
