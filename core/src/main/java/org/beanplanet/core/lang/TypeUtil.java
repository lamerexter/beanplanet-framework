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
import org.beanplanet.core.models.tree.TreeNode;
import org.beanplanet.core.util.ExceptionUtil;
import org.beanplanet.core.util.StringUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.stream;

/**
 * A static utility class for the everything <code>type-related</code> related.
 *
 * @author Gary Watson
 */
public final class TypeUtil {

    private static final Set<Class<?>> PRIMITIVE_TYPES = new HashSet<>();
    private static final Set<Class<?>> PRIMITIVE_TYPE_WRAPPERS = new HashSet<>();

    private static final Set<Class<?>> NUMERIC_TYPES = new HashSet<>();

    private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER_TYPE_TO_PRIMITIVE_TYPE = new HashMap<>();

    private static final Map<Class<?>, Class<?>> PRIMITIVE_TYPE_TO_PRIMITIVE_WRAPPER_TYPE = new HashMap<>();

    private static final Map<String, Class<?>> NAME_TO_PRIMITIVE_CLASS = new HashMap<>();

    private static final Object[] EMPTY_PARAMS = new Object[] {};
    private static final Class<?>[] EMPTY_ARG_TYPES = new Class<?>[] {};

    static {
        PRIMITIVE_TYPES.add(boolean.class);
        PRIMITIVE_TYPES.add(byte.class);
        PRIMITIVE_TYPES.add(char.class);
        PRIMITIVE_TYPES.add(double.class);
        PRIMITIVE_TYPES.add(float.class);
        PRIMITIVE_TYPES.add(int.class);
        PRIMITIVE_TYPES.add(long.class);
        PRIMITIVE_TYPES.add(short.class);
        PRIMITIVE_TYPES.add(void.class);

        PRIMITIVE_TYPE_WRAPPERS.add(Boolean.class);
        PRIMITIVE_TYPE_WRAPPERS.add(Byte.class);
        PRIMITIVE_TYPE_WRAPPERS.add(Character.class);
        PRIMITIVE_TYPE_WRAPPERS.add(Double.class);
        PRIMITIVE_TYPE_WRAPPERS.add(Float.class);
        PRIMITIVE_TYPE_WRAPPERS.add(Integer.class);
        PRIMITIVE_TYPE_WRAPPERS.add(Long.class);
        PRIMITIVE_TYPE_WRAPPERS.add(Short.class);
        PRIMITIVE_TYPE_WRAPPERS.add(Void.class);

        PRIMITIVE_WRAPPER_TYPE_TO_PRIMITIVE_TYPE.put(Boolean.class, boolean.class);
        PRIMITIVE_WRAPPER_TYPE_TO_PRIMITIVE_TYPE.put(Byte.class, byte.class);
        PRIMITIVE_WRAPPER_TYPE_TO_PRIMITIVE_TYPE.put(Character.class, char.class);
        PRIMITIVE_WRAPPER_TYPE_TO_PRIMITIVE_TYPE.put(Double.class, double.class);
        PRIMITIVE_WRAPPER_TYPE_TO_PRIMITIVE_TYPE.put(Float.class, float.class);
        PRIMITIVE_WRAPPER_TYPE_TO_PRIMITIVE_TYPE.put(Integer.class, int.class);
        PRIMITIVE_WRAPPER_TYPE_TO_PRIMITIVE_TYPE.put(Long.class, long.class);
        PRIMITIVE_WRAPPER_TYPE_TO_PRIMITIVE_TYPE.put(Short.class, short.class);
        PRIMITIVE_WRAPPER_TYPE_TO_PRIMITIVE_TYPE.put(Void.class, void.class);

        PRIMITIVE_TYPE_TO_PRIMITIVE_WRAPPER_TYPE.put(boolean.class, Boolean.class);
        PRIMITIVE_TYPE_TO_PRIMITIVE_WRAPPER_TYPE.put(byte.class, Byte.class);
        PRIMITIVE_TYPE_TO_PRIMITIVE_WRAPPER_TYPE.put(char.class, Character.class);
        PRIMITIVE_TYPE_TO_PRIMITIVE_WRAPPER_TYPE.put(double.class, Double.class);
        PRIMITIVE_TYPE_TO_PRIMITIVE_WRAPPER_TYPE.put(float.class, Float.class);
        PRIMITIVE_TYPE_TO_PRIMITIVE_WRAPPER_TYPE.put(int.class, Integer.class);
        PRIMITIVE_TYPE_TO_PRIMITIVE_WRAPPER_TYPE.put(long.class, Long.class);
        PRIMITIVE_TYPE_TO_PRIMITIVE_WRAPPER_TYPE.put(short.class, Short.class);
        PRIMITIVE_TYPE_TO_PRIMITIVE_WRAPPER_TYPE.put(void.class, Void.class);

        NAME_TO_PRIMITIVE_CLASS.put("boolean", boolean.class);
        NAME_TO_PRIMITIVE_CLASS.put("byte", byte.class);
        NAME_TO_PRIMITIVE_CLASS.put("char", char.class);
        NAME_TO_PRIMITIVE_CLASS.put("double", double.class);
        NAME_TO_PRIMITIVE_CLASS.put("float", float.class);
        NAME_TO_PRIMITIVE_CLASS.put("int", int.class);
        NAME_TO_PRIMITIVE_CLASS.put("long", long.class);
        NAME_TO_PRIMITIVE_CLASS.put("short", short.class);
        NAME_TO_PRIMITIVE_CLASS.put("void", void.class);

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

    private TypeUtil(){}

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
        Package pkg  = type.getPackage();
        return pkg == null ? null : pkg.getName();
    }

    public static Class<?> loadClassOrNull(String className) {
        Class<?> clazz = NAME_TO_PRIMITIVE_CLASS.get(className);
        if (clazz != null) {
            return clazz;
        }

        try {
            clazz = Class.forName(className);
        } catch (java.lang.ClassNotFoundException cnfEx) {
            if (Thread.currentThread().getContextClassLoader() != null) {
                try {
                    clazz = Class.forName(className, true, Thread.currentThread().getContextClassLoader());
                } catch (java.lang.ClassNotFoundException cnfThreadEx) {
                }
            }
        }

        int lastDotPos = className.lastIndexOf('.');
        if (clazz == null && lastDotPos >= 0) {
            String memberClassName = className.substring(0, lastDotPos)+"$"+className.substring(lastDotPos+1);
            try {
                clazz = Class.forName(memberClassName);
            } catch (java.lang.ClassNotFoundException cnfEx) {
                if (Thread.currentThread().getContextClassLoader() != null) {
                    try {
                        clazz = Class.forName(memberClassName, true, Thread.currentThread().getContextClassLoader());
                    } catch (java.lang.ClassNotFoundException cnfThreadEx) {
                    }
                }
            }
        }

        return clazz;
    }

    public static java.lang.Class<?> loadClass(String className) throws TypeNotFoundException {
        Class<?> clazz = NAME_TO_PRIMITIVE_CLASS.get(className);
        if (clazz != null) {
            return clazz;
        }

        try {
            clazz = Class.forName(className);
        } catch (java.lang.ClassNotFoundException cnfEx) {
            // XClass not found under calling context, try the current threads
            // classloader, if any
            if (Thread.currentThread().getContextClassLoader() != null) {
                try {
                    clazz = Class.forName(className, true, Thread.currentThread().getContextClassLoader());
                } catch (java.lang.ClassNotFoundException cnfThreadEx) {
                    throw new TypeNotFoundException("The given class ["+className+"] was not found: ", cnfThreadEx);
                }
            }
        }

        return clazz;
    }

    public static java.lang.Class<?> loadClass(String className, ClassLoader classLoader) {
        return loadClass(className, true, classLoader);
    }

    public static ClassLoader getClassLoaderInContext(Object obj) {
        Assert.notNull(obj, "The object whose classloader is to be determined must not be null");
        return getClassLoaderInContext(obj.getClass());
    }

    public static ClassLoader getClassLoaderInContext(Class<?> clazz) {
        if (Thread.currentThread().getContextClassLoader() != null) {
            return Thread.currentThread().getContextClassLoader();
        } else {
            return clazz.getClassLoader();
        }
    }

    public static java.lang.Class<?> loadClass(String className, boolean initialiseClass, ClassLoader classLoader) {
        Class<?> clazz = NAME_TO_PRIMITIVE_CLASS.get(className);
        if (clazz != null) {
            return clazz;
        }

        try {
            clazz = Class.forName(className, initialiseClass, classLoader);
        } catch (java.lang.ClassNotFoundException | NoClassDefFoundError cnfEx) {
            // XClass not found under calling context, try the current threads
            // classloader, if any
            if (Thread.currentThread().getContextClassLoader() != null && Thread.currentThread().getContextClassLoader() != classLoader) {
                try {
                    clazz = Class.forName(className, initialiseClass, Thread.currentThread().getContextClassLoader());
                } catch (java.lang.ClassNotFoundException | NoClassDefFoundError cnfThreadEx) {
                    throw new TypeNotFoundException("The given class ["+className+"] was not found: ", cnfEx);
                }
            }
        }

        return clazz;
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
        return instantiateClass(typeName, new Object[] {});
    }

    public static Object instantiateClass(String typeName, Class<?> ctorArgTypes[], Object ctorArgVals[])  {
        Class<?> clazz = null;
        try {
            clazz = loadClass(typeName);
            Constructor<?> ctor = clazz.getConstructor(ctorArgTypes);
            return ctor.newInstance(ctorArgVals);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new UncheckedException(e);
        }
    }

    public static Object instantiateClass(String typeName, Object ctorArgVals[]) {
        Class<?> clazz = loadClass(typeName);
        return instantiateClass(clazz, ctorArgVals);
    }

    public static <T> T instantiateClass(Class<T> type) throws TypeNotFoundException {
        final Object NO_ARGS[] = new Object[] {};
        return instantiateClass(type, NO_ARGS);
    }

    @SuppressWarnings("unchecked")
    public static <T> Optional<Constructor<T>> getCallableConstructor(Class<T> type, Object[] ctorParameterValues) {
        return stream((Constructor<T>[])type.getDeclaredConstructors())
                .filter(c -> c.getParameterCount() == (ctorParameterValues == null ? 0 : ctorParameterValues.length))
                .filter(c -> {
                    for (int n=0; n < c.getParameterCount(); n++) {
                        if ( !(ctorParameterValues[n] == null || c.getParameterTypes()[n].isAssignableFrom(ctorParameterValues[n].getClass())) ) {
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

    public static boolean isPrimitiveType(Class<?> clazz) {
        return PRIMITIVE_TYPES.contains(clazz);
    }

    public static boolean isPrimitiveTypeWrapperClass(Class<?> clazz) {
        return PRIMITIVE_TYPE_WRAPPERS.contains(clazz);
    }

    public static boolean isPrimitiveTypeOrWrapperClass(Class<?> clazz) {
        return PRIMITIVE_TYPES.contains(clazz) || PRIMITIVE_TYPE_WRAPPERS.contains(clazz);
    }

    public static boolean isNumericType(Class<?> clazz) {
        return NUMERIC_TYPES.contains(clazz);
    }

    /**
     * Returns the primitive type for a given primitive wrapper type.
     *
     * @param clazz the primitive wrapper type or some other type.
     * @return the equivalent primitive type or the original type if not a primnitive wrapper type.
     */
    public static Class<?> primitiveTypeFor(Class<?> clazz) {
        Class<?> result = PRIMITIVE_WRAPPER_TYPE_TO_PRIMITIVE_TYPE.get(clazz);
        return result == null ? clazz : result;
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
     * @param primiveType the primitive type whose wrapper type is to be returned.
     * @return the primitive wrapper type for the primitive type, or null if the type specified
     * was not a primitive type.
     */
    public static Class<?> getPrimitiveWrapperType(Class<?> primiveType) {
        return PRIMITIVE_TYPE_TO_PRIMITIVE_WRAPPER_TYPE.get(primiveType);
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

    public static Object invokeStaticMethod(Class<?> clazz, String methodName) {
        return invokeStaticMethod(clazz, methodName, (Object[])null);
    }

    public static Object invokeStaticMethod(Class<?> clazz, String methodName, Object ... params) {
        Method method = findMethod(Modifier.STATIC, methodName, clazz, Object.class, paramTypes(params))
                .orElseThrow(() -> new UncheckedException("Unable to invoke method, \"" + methodName
                                                          + "\" on target, class=\"" + clazz.getName()
                                                          + "\" - the method was not found: "));
        return invokeMethod(null, method, params);
    }

    private static Class<?>[] paramTypes(Object params[]) {
        return params == null ? null : stream(params).map(p -> p == null ? Object.class : p.getClass()).toArray(Class[]::new);
    }

    public static Stream<Method> streamMethods(int modifiers, String name, Class<?> clazz, Class<?> returnType, Class<?> ... paramTypes) {

        return streamMethods(clazz)
                .filter(m -> m.getName().equals(name))
                .filter(m -> (m.getModifiers() & modifiers) > 0)
                .filter(m -> returnType == null || returnType.isAssignableFrom(m.getReturnType()))
                .filter(m -> m.getParameterCount() == (paramTypes == null ? 0 : paramTypes.length))
                .filter(m -> {
                    for (int n=0; n < paramTypes.length; n++) {
                        if ( paramTypes[n] != null
                             && !(m.getParameterTypes()[n].isAssignableFrom(paramTypes[n])
                                   || (m.getParameterTypes()[n] instanceof Class && paramTypes[n] instanceof Class)) ) {
                            return false;
                        }
                    }
                    return true;
                });
    }

    public static Stream<Constructor> streamConstructors(Class<?> clazz) {
        return stream(clazz.getDeclaredConstructors());
    }

    public static Stream<Method> streamMethods(Class<?> clazz) {

        return new TypeTree(Object.class, clazz)
                .stream()
                .map(TreeNode::getManagedObject)
                .flatMap(c -> stream(c.getDeclaredMethods()));
    }

    public static List<Method> findMethods(int modifiers, String name, Class<?> clazz, Class<?> returnType, Class<?> ... paramTypes) {

        return streamMethods(modifiers, name, clazz, returnType, paramTypes).collect(Collectors.toList());
    }

    public static Optional<Method> findMethod(int modifiers, String name, Class<?> clazz, Class<?> returnType, Class<?> ... paramTypes) {

        return streamMethods(modifiers, name, clazz, returnType, paramTypes).findFirst();
    }

    public static Object invokeMethod(Object target, Method method, Object ... params) {
        try {
            return method.invoke(Modifier.isStatic(method.getModifiers()) ? null : target, (params != null ? params : EMPTY_PARAMS));
        } catch (IllegalAccessException accessEx) {
            if (Modifier.isPublic(method.getModifiers())) {
                // Known issue where method is declared as public but reflection is
                // occasionally denied access. Attempt
                // to allow access in this case.
                try {
                    method.setAccessible(true);
                    return method.invoke(Modifier.isStatic(method.getModifiers()) ? null : target, params);
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

    /**
     * Convenience method to return the display name of a type
     *
     * @param type
     *           the type whose name is to be displayed
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

    public static Stream<Method> getMethodsInClassHierarchy(Class<?> specificType) {
        return new TypeTree(specificType)
                .preorderStream()
                .map(TreeNode::getManagedObject)
                .flatMap(type -> stream(type.getDeclaredMethods()));
    }
}
