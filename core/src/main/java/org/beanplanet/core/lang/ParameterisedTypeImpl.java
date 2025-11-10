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

package org.beanplanet.core.lang;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

public final class ParameterisedTypeImpl implements ParameterizedType {
    private final Type ownerType;
    private final Type rawType;
    private final Type[] typeArguments;

    public ParameterisedTypeImpl(Type rawType, Type ... typeArguments) {
        this.ownerType = null;
        this.rawType = rawType;
        this.typeArguments = typeArguments == null ? TypeUtil.EMPTY_TYPES:  typeArguments;
    }

//    public ParameterisedTypeImpl(Class<?> ownerType, Type rawType, Type[] typeArguments) {
//        this.ownerType = ownerType;
//        this.rawType = rawType;
//        this.typeArguments = typeArguments == null ? TypeUtil.EMPTY_TYPES:  typeArguments;
//    }

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
