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

package org.beanplanet.core.util;

import org.beanplanet.core.beans.BeanUtil;
import org.beanplanet.core.beans.JavaBean;
import org.beanplanet.core.lang.Assert;
import org.beanplanet.core.models.Builder;
import org.beanplanet.core.models.Pair;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.beanplanet.core.lang.TypeUtil.getBaseName;
import static org.beanplanet.core.util.StringUtil.asDelimitedString;

/**
 * A convenience class for building the string representation of an object, as
 * returned by the <code>toString()</code> method, based on the reflective
 * properties of an object.
 *
 * @author Gary Watson
 */
public class PropertyBasedToStringBuilder implements Builder<String> {
    /**
     * The source object whose <code>toString()</code> is to built.
     */
    protected Object source;

    protected List<Pair<String, Supplier<Object>>> nameValueSuppliers;

    /**
     * Returns a list of the properties the builder uses to create the string
     * representation of the source bean; defaults to null which signifies all
     * properties.
     */
    protected List<String> propertyNames;

    /**
     * A list of the property names to be excluded.
     */
    private LinkedHashSet<String> excludePropertyNames;

    /**
     * Whether the builder ignores errors when accessing source bean properties;
     * defaults to true;.
     */
    protected boolean ignoresErrors = true;

    /**
     * Whether only one property is to be printed per line; defaults to false.
     */
    protected boolean onePropertyPerLine = false;

    /**
     * Whether properties with null values are to be displayed; defaults to true.
     */
    protected boolean showPropetiesWithNullValues = true;

    /**
     * Constructs a PropertyBasedToStringBuilder based on the given source object and
     * supplied named properties.
     *
     * @param source             the source object.
     * @param nameValueSuppliers the names and supplied values of the properties to build.
     */
    @SafeVarargs
    public PropertyBasedToStringBuilder(final Object source,
                                        final Pair<String, Supplier<Object>>... nameValueSuppliers) {
        this.source = source;
        this.nameValueSuppliers = nameValueSuppliers == null ? Collections.emptyList() : asList(nameValueSuppliers);
    }

    /**
     * Constructs a PropertyBasedToStringBuilder based on the given source object and
     * supplied named properties.
     *
     * @param source         the source object.
     * @param name1          the name of the property #1.
     * @param valueSupplier1 the supplied value of property #1.
     */
    public PropertyBasedToStringBuilder(final Object source,
                                        final String name1,
                                        final Supplier<Object> valueSupplier1) {
        this(source, Pair.of(name1, valueSupplier1));
    }

    /**
     * Constructs a PropertyBasedToStringBuilder based on the given source object and
     * supplied named properties.
     *
     * @param source         the source object.
     * @param name1          the name of the property #1.
     * @param valueSupplier1 the supplied value of property #1.
     * @param name2          the name of the property #2.
     * @param valueSupplier2 the supplied value of property #2.
     */
    public PropertyBasedToStringBuilder(final Object source,
                                        final String name1,
                                        final Supplier<Object> valueSupplier1,
                                        final String name2,
                                        final Supplier<Object> valueSupplier2) {
        this(
                source,
                Pair.of(name1, valueSupplier1),
                Pair.of(name2, valueSupplier2)
        );
    }

    /**
     * Constructs a PropertyBasedToStringBuilder based on the given source object and
     * supplied named properties.
     *
     * @param source         the source object.
     * @param name1          the name of the property #1.
     * @param valueSupplier1 the supplied value of property #1.
     * @param name2          the name of the property #2.
     * @param valueSupplier2 the supplied value of property #2.
     * @param name3          the name of the property #3.
     * @param valueSupplier3 the supplied value of property #3.
     */
    public PropertyBasedToStringBuilder(final Object source,
                                        final String name1,
                                        final Supplier<Object> valueSupplier1,
                                        final String name2,
                                        final Supplier<Object> valueSupplier2,
                                        final String name3,
                                        final Supplier<Object> valueSupplier3) {
        this(
                source,
                Pair.of(name1, valueSupplier1),
                Pair.of(name2, valueSupplier2),
                Pair.of(name3, valueSupplier3)
        );
    }

    /**
     * Constructs a PropertyBasedToStringBuilder based on the given source object and
     * supplied named properties.
     *
     * @param source         the source object.
     * @param name1          the name of the property #1.
     * @param valueSupplier1 the supplied value of property #1.
     * @param name2          the name of the property #2.
     * @param valueSupplier2 the supplied value of property #2.
     * @param name3          the name of the property #3.
     * @param valueSupplier3 the supplied value of property #3.
     * @param name4          the name of the property #4.
     * @param valueSupplier4 the supplied value of property #4.
     */
    public PropertyBasedToStringBuilder(final Object source,
                                        final String name1,
                                        final Supplier<Object> valueSupplier1,
                                        final String name2,
                                        final Supplier<Object> valueSupplier2,
                                        final String name3,
                                        final Supplier<Object> valueSupplier3,
                                        final String name4,
                                        final Supplier<Object> valueSupplier4) {
        this(
                source,
                Pair.of(name1, valueSupplier1),
                Pair.of(name2, valueSupplier2),
                Pair.of(name3, valueSupplier3),
                Pair.of(name4, valueSupplier4)
        );
    }

    /**
     * Constructs a PropertyBasedToStringBuilder based on the given source object and
     * supplied named properties.
     *
     * @param source         the source object.
     * @param name1          the name of the property #1.
     * @param valueSupplier1 the supplied value of property #1.
     * @param name2          the name of the property #2.
     * @param valueSupplier2 the supplied value of property #2.
     * @param name3          the name of the property #3.
     * @param valueSupplier3 the supplied value of property #3.
     * @param name4          the name of the property #4.
     * @param valueSupplier4 the supplied value of property #4.
     * @param name5          the name of the property #5.
     * @param valueSupplier5 the supplied value of property #5.
     */
    public PropertyBasedToStringBuilder(final Object source,
                                        final String name1,
                                        final Supplier<Object> valueSupplier1,
                                        final String name2,
                                        final Supplier<Object> valueSupplier2,
                                        final String name3,
                                        final Supplier<Object> valueSupplier3,
                                        final String name4,
                                        final Supplier<Object> valueSupplier4,
                                        final String name5,
                                        final Supplier<Object> valueSupplier5) {
        this(
                source,
                Pair.of(name1, valueSupplier1),
                Pair.of(name2, valueSupplier2),
                Pair.of(name3, valueSupplier3),
                Pair.of(name4, valueSupplier4),
                Pair.of(name5, valueSupplier5)
        );
    }

    /**
     * Constructs a PropertyBasedToStringBuilder based on the given source object and
     * supplied named properties.
     *
     * @param source         the source object.
     * @param name1          the name of the property #1.
     * @param valueSupplier1 the supplied value of property #1.
     * @param name2          the name of the property #2.
     * @param valueSupplier2 the supplied value of property #2.
     * @param name3          the name of the property #3.
     * @param valueSupplier3 the supplied value of property #3.
     * @param name4          the name of the property #4.
     * @param valueSupplier4 the supplied value of property #4.
     * @param name5          the name of the property #5.
     * @param valueSupplier5 the supplied value of property #5.
     * @param name6          the name of the property #6.
     * @param valueSupplier6 the supplied value of property #6.
     */
    public PropertyBasedToStringBuilder(final Object source,
                                        final String name1,
                                        final Supplier<Object> valueSupplier1,
                                        final String name2,
                                        final Supplier<Object> valueSupplier2,
                                        final String name3,
                                        final Supplier<Object> valueSupplier3,
                                        final String name4,
                                        final Supplier<Object> valueSupplier4,
                                        final String name5,
                                        final Supplier<Object> valueSupplier5,
                                        final String name6,
                                        final Supplier<Object> valueSupplier6) {
        this(
                source,
                Pair.of(name1, valueSupplier1),
                Pair.of(name2, valueSupplier2),
                Pair.of(name3, valueSupplier3),
                Pair.of(name4, valueSupplier4),
                Pair.of(name5, valueSupplier5),
                Pair.of(name6, valueSupplier6)
        );
    }

    /**
     * Constructs a PropertyBasedToStringBuilder based on the given source object.
     *
     * @param source the source object.
     */
    public PropertyBasedToStringBuilder(final Object source,
                                        final String... propertyNames) {
        this.source = source;
        this.propertyNames = propertyNames == null ? null : asList(propertyNames);

        final JavaBean<?> bean = new JavaBean<>(source);
        this.nameValueSuppliers = Arrays.stream(propertyNames).map(p -> Pair.of(p, (Supplier<Object>) () -> bean.get(p).getValue())).collect(Collectors.toList());
    }

    /**
     * Constructs a PropertyBasedToStringBuilder.
     */
    public PropertyBasedToStringBuilder() {
        this.nameValueSuppliers = Collections.emptyList();
    }

    /**
     * Constructs a PropertyBasedToStringBuilder based on the given source object.
     *
     * @param source the source object.
     */
    public PropertyBasedToStringBuilder(Object source) {
        this(source, BeanUtil.getPropertyNames(source));
    }

    /**
     * Sets the source object whose string representation is sought.
     *
     * @param source the source object.
     * @return the builder again for chaining reuse.
     */
    public PropertyBasedToStringBuilder withSource(Object source) {
        this.source = source;
        this.nameValueSuppliers = Collections.emptyList();
        return this;
    }

    /**
     * Configures this builder to use all properties of the source bean.
     *
     * @return the builder again for chaining reuse.
     */
    public PropertyBasedToStringBuilder withAllProperties() {
        return this;
    }

    /**
     * Adds a property name to the properties this builder will used to create the
     * string representation of the bean.
     *
     * @param propertyName the property name to add.
     * @return the builder again for chaining reuse.
     */
    public PropertyBasedToStringBuilder withProperty(String propertyName) {
        if (this.propertyNames == null) {
            this.propertyNames = new LinkedList<String>();
        }
        this.propertyNames.add(propertyName);
        return this;
    }

    /**
     * Adds a list of property names to the properties this builder will use to create the
     * string representation of the bean.
     *
     * @param propertyNames the list of property names to add.
     * @return the builder again for chaining reuse.
     */
    public PropertyBasedToStringBuilder withProperties(String... propertyNames) {
        if (this.propertyNames == null) {
            this.propertyNames = new LinkedList<String>();
        }
        this.propertyNames.addAll(asList(propertyNames));
        return this;
    }

    /**
     * Adds a list of property names to exclude from the properties this builder will use to create the
     * string representation of the bean.
     *
     * @param propertyNames the list of property names to exclude.
     * @return the builder again for chaining reuse.
     */
    public PropertyBasedToStringBuilder withoutProperties(String... propertyNames) {
        if (this.excludePropertyNames == null) {
            this.excludePropertyNames = new LinkedHashSet<String>();
        }
        this.excludePropertyNames.addAll(asList(propertyNames));
        return this;
    }

    /**
     * Configures this builder to ignore errors when accessing source bean
     * properties.
     *
     * @return the builder again for chaining reuse.
     */
    public PropertyBasedToStringBuilder withErrorsIgnored() {
        this.ignoresErrors = true;
        return this;
    }

    /**
     * Configures this builder to ignore errors when accessing source bean
     * properties.
     *
     * @param ignored the ignored
     * @return the builder again for chaining reuse.
     * @ignored true if all errors are to be ignored, false if errors are to be
     * propagated up.
     */
    public PropertyBasedToStringBuilder withErrorsIgnored(boolean ignored) {
        this.ignoresErrors = ignored;
        return this;
    }

    /**
     * Configures the builder to print one property per line.
     *
     * @return the builder again for chaining reuse.
     */
    public PropertyBasedToStringBuilder withOnePropertyPerLine() {
        onePropertyPerLine = true;
        return this;
    }

    /**
     * Configures the builder to print one property per line.
     *
     * @param onePropertyPerLine the one property per line
     * @return the builder again for chaining reuse.
     */
    public PropertyBasedToStringBuilder withOnePropertyPerLine(boolean onePropertyPerLine) {
        this.onePropertyPerLine = onePropertyPerLine;
        return this;
    }

    /**
     * Sets the whether properties with null values are to be displayed.
     *
     * @param showPropetiesWithNullValues whether properties with null values are to be displayed.
     */
    public PropertyBasedToStringBuilder withNullValuePropetiesShown(boolean showPropetiesWithNullValues) {
        this.showPropetiesWithNullValues = showPropetiesWithNullValues;
        return this;
    }

    /**
     * Sets the whether properties with null values are to be hidden.
     *
     * @param hidePropertiesWithNullValues whether properties with null values are to be hidden.
     */
    public PropertyBasedToStringBuilder withNullValuePropetiesHidden(boolean hidePropertiesWithNullValues) {
        this.showPropetiesWithNullValues = !hidePropertiesWithNullValues;
        return this;
    }

    /**
     * Builds the string representation of the source object, based on the
     * configured name/value suppliers (properties or all properties if none have been configured).
     *
     * @return the string representation of the source object, based on the supplied names and values.
     */
    public String build() {
        Assert.notNull(source, "The source object may not be null");

        int n = 0;
        StringBuilder s = new StringBuilder();
        s.append(getBaseName(source.getClass())).append("[").append(onePropertyPerLine ? "\n" : "");
        for (Pair<String, Supplier<Object>> nameValueSupplier : nameValueSuppliers) {

            Object propertyValue;
            try {
                propertyValue = nameValueSupplier.getRight().get();
                if (!showPropetiesWithNullValues && propertyValue == null) {
                    continue;
                }
            } catch (RuntimeException runtimeEx) {
                if (!ignoresErrors) {
                    throw runtimeEx;
                }
                propertyValue = "...error accessing property...";
            }

            if (n++ > 0) {
                s.append(onePropertyPerLine ? "\n   " : ", ");
            }
            s.append(nameValueSupplier.getLeft()).append("=");

            if (propertyValue instanceof Object[]) {
                s.append("{").append(asDelimitedString((Object[]) propertyValue, ",")).append("}");
            } else {
                s.append(propertyValue);
            }
        }

        s.append("]");
        return s.toString();
    }

    /**
     * Builds the string representation of the source object, based on the
     * configured properties or all properties if none have been configured.
     *
     * @return the string representation of the source object, based on its
     * properties.
     */
    public String build_DEL_ME() {
        Assert.notNull(source, "The source object may not be null");

        int n = 0;
        StringBuilder s = new StringBuilder();
        s.append(getBaseName(source.getClass())).append("[").append(onePropertyPerLine ? "\n" : "");
        for (String propertyName : (propertyNames != null ? propertyNames : asList(BeanUtil.getPropertyNames(source)))) {
            if (excludePropertyNames != null && excludePropertyNames.contains(propertyName)) continue;

            Object propertyValue = null;
            try {
                propertyValue = BeanUtil.getPropertyValue(source, propertyName);
                if (!showPropetiesWithNullValues && propertyValue == null) {
                    continue;
                }
            } catch (RuntimeException runtimeEx) {
                if (!ignoresErrors) {
                    throw runtimeEx;
                }
                propertyValue = "...error accessing property...";
            }

            if (n++ > 0) {
                s.append(onePropertyPerLine ? "\n   " : ", ");
            }
            s.append(propertyName).append("=");

            if (propertyValue instanceof Object[]) {
                s.append("{").append(asDelimitedString((Object[]) propertyValue, ",")).append("}");
            } else {
                s.append(propertyValue);
            }
        }

        s.append("]");
        return s.toString();
    }

    /**
     * Builds the string representation of the source object, based on the
     * configured properties or all properties if none have been configured.
     *
     * @return the string representation of the source object, based on its
     * properties.
     */
    public String toString() {
        return build();
    }
}

