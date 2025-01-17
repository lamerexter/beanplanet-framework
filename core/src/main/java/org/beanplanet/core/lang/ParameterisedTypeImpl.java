package org.beanplanet.core.lang;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

public final class ParameterisedTypeImpl implements ParameterizedType {
    private final Type ownerType;
    private final Type rawType;
    private final Type[] typeArguments;

    private ParameterisedTypeImpl(Type rawType, Type ... typeArguments) {
        this(null, rawType, typeArguments);
    }

    private ParameterisedTypeImpl(Type ownerType, Type rawType, Type ... typeArguments) {
        this.ownerType = ownerType;
        this.rawType = rawType;
        this.typeArguments = typeArguments == null ? TypeUtil.EMPTY_TYPES:  typeArguments;
    }

    @Override
    public Type getOwnerType() {
        return ownerType;
    }

    @Override
    public Type getRawType() {
        return this.rawType;
    }

    public Type[] getActualTypeArguments() {
        return this.typeArguments;
    }

    public String toString() {
        return TypeUtil.toString(this);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ParameterizedType that = (ParameterizedType) object;
        return Objects.equals(ownerType, that.getOwnerType())
                && Objects.equals(rawType, that.getRawType())
                && Arrays.equals(typeArguments, that.getActualTypeArguments());
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerType, rawType, typeArguments);
    }
}
