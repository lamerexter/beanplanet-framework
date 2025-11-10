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

package org.beanplanet.core.models;

import org.beanplanet.core.util.PropertyBasedToStringBuilder;

import java.util.function.Supplier;

/**
 * An implementation of a name/value pair whose name and value can be supplied directly or indirectly.
 *
 * @param <V> the value type.
 */
public class SimpleNameValue<V> implements NameValue<V> {
    /** The name supplier associated with this name/value pair. */
    private final Supplier<String> nameSupplier;
    /** The value supplier associated with this name/value. */
    private final Supplier<V> valueSupplier;

    /**
     * Creates a new name/value pair whose name and value are directly supplied.
     *
     * @param name the name/value name.
     * @param value the name/value value.
     */
    public SimpleNameValue(final String name, final V value) {
        this(() -> name, () -> value);
    }

    /**
     * Creates a new name/value pair whose name and value are indirectly supplied.
     *
     * @param nameSupplier
     * @param valueSupplier
     */
    public SimpleNameValue(final Supplier<String> nameSupplier, final Supplier<V> valueSupplier) {
        this.nameSupplier = nameSupplier;
        this.valueSupplier = valueSupplier;
    }

    /**
     * Gets the name of this name/value pair.
     *
     * @return the name/value pair name part.
     */
    @Override
    public String getName() {
        return nameSupplier ==  null ? null : nameSupplier.get();
    }

    /**
     * Gets the value of this name/value pair.
     *
     * @return the name/value value part.
     */
    @Override
    public V getValue() {
        return valueSupplier == null ? null : valueSupplier.get();
    }

    @Override
    public String toString() {
        return new PropertyBasedToStringBuilder(this).build();
    }
}
