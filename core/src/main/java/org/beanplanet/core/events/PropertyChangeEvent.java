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

package org.beanplanet.core.events;

import org.beanplanet.core.util.PropertyBasedToStringBuilder;

import java.util.Objects;

public class PropertyChangeEvent<T> extends ChangeEvent<T> {
    private String propertyName;

    public PropertyChangeEvent(String propertyName,
                               T oldValue,
                               T newValue) {
        this(null, propertyName, oldValue, newValue);
    }

    public PropertyChangeEvent(Object source,
                               String propertyName,
                               T oldValue,
                               T newValue) {
        super(oldValue, newValue);
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (getClass() != o.getClass())
            return false;
        PropertyChangeEvent<?> that = (PropertyChangeEvent<?>) o;
        return Objects.equals(propertyName, that.propertyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), propertyName);
    }

    public String toString() {
        return new PropertyBasedToStringBuilder(this).build();
    }
}
