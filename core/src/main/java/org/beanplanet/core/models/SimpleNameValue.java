/*
 *  MIT Licence:
 *
 *  Copyright (C) 2020 Beanplanet Ltd
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
