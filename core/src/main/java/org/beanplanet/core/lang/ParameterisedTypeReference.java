package org.beanplanet.core.lang;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * <p>
 * This abstract class allows for subclassing with an 'actual' {@link java.lang.reflect.Type} so that the generic
 * type information is captured at compile time (i.e. burned into this superclass), which may then be referenced at runtime.
 * </p>
 * <pre>
 *
 * ParameterisedTypeReference&lt;List&lt;Person&gt;&gt; listOfPersonType = new ParameterisedTypeReference&lt;List&lt;Person&gt;&gt;(){}
 * </pre>
 * <p>
 * Subclassing in this way is necessary so that the actual type can be captured at compile time. In the example above,
 * the following would confirm the actual type, <code>List&lt;Person&gt;</code>:
 * </p>
 * <pre>
 *
 * assert listOfPersonType.getGenericSuperclass().getTypeArguments()[0] == Person.class
 * </pre>
 */
public abstract class ParameterisedTypeReference<T> {
    private final Type type;

    public ParameterisedTypeReference() {
        Class<?> parameterisedTypeReferenceSubclass = findParameterisedTypeReferenceSubclassInHierarchy(getClass());
        Type superclass = parameterisedTypeReferenceSubclass.getGenericSuperclass();
        Assert.isTrue(ParameterizedType.class.isAssignableFrom(parameterisedTypeReferenceSubclass), "Type must be a parameterized type");
        ParameterizedType ptSuperclass = (ParameterizedType) superclass;
        Type[] actualTypeArguments = ptSuperclass.getActualTypeArguments();
        Assert.isTrue(actualTypeArguments.length == 1, "Number of type arguments in parameterised type reference type must be 1");
        this.type = actualTypeArguments[0];
    }

    private static Class<?> findParameterisedTypeReferenceSubclassInHierarchy(final Class<?> subclass) {
        Class<?> superclass = subclass.getSuperclass();
        if (Object.class == superclass) {
            throw new IllegalStateException("Expected a "+ParameterisedTypeReference.class.getSimpleName()+" superclass, but found Object");
        }

        return ParameterisedTypeReference.class == superclass ? subclass : findParameterisedTypeReferenceSubclassInHierarchy(superclass);
    }

    private ParameterisedTypeReference(final Type type) {
        this.type = type;
    }

    public static <T> ParameterisedTypeReference<T> forType(final Type type) {
        return new ParameterisedTypeReference<>(type){};
    }

    public final Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ParameterisedTypeReference)) return false;
        ParameterisedTypeReference<?> that = (ParameterisedTypeReference<?>) object;
        return Objects.equals(getType(), that.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType());
    }
}
