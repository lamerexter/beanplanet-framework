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

package org.beanplanet.core.models.progress;

import org.beanplanet.core.events.*;

public class ProgressIndicator implements BoundedProgress<Integer> {
    private EventSupport eventSupport = new EventSupport();

    private Integer start;
    private Integer end;
    private Integer value;

    public ProgressIndicator() {}

    public ProgressIndicator(Integer start, Integer end) {
        this(start, end, null);
    }

    public ProgressIndicator(Integer start, Integer end, Integer value) {
        this.start = start;
        this.end = end;
        this.value = value;
    }

    @Override
    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        Integer oldValue = this.start;
        this.start = start;
        eventSupport.dispatchEvent(new PropertyChangeEvent<>("start", oldValue, start));
    }

    public ProgressIndicator withStart(Integer start) {
        setStart(start);
        return this;
    }

    @Override
    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        Integer oldValue = this.end;
        this.end = end;
        eventSupport.dispatchEvent(new PropertyChangeEvent<>("end", oldValue, end));
    }

    public ProgressIndicator withEnd(Integer end) {
        setEnd(end);
        return this;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        Integer oldValue = this.value;
        this.value = value;
        eventSupport.dispatchEvent(new PropertyChangeEvent<>("value", oldValue, value));
    }

    public ProgressIndicator withValue(Integer value) {
        setValue(value);
        return this;
    }

    @Override
    public Double getCompletion() {
        if ( isIndeterminate() ) return null;

        boolean isAscendingOrder = getStart().compareTo(getEnd()) <= 0;
        int range = Math.abs(getStart() - getEnd()) + 1;
        double progressRange = isAscendingOrder ? (getValue() - getStart() + 1) : (getStart() - getValue() + 1d);

        return progressRange / range;
    }

    @Override
    public boolean isComplete() {
        if ( isIndeterminate() ) return false;
        return getValue().equals(getEnd());
    }

    @Override
    public boolean isIndeterminate() {
        if (getStart() == null|| getEnd() == null|| getValue() == null) return true;

        boolean isAscendingOrder = getStart().compareTo(getEnd()) <= 0;
        return isAscendingOrder ?
               (getValue().compareTo(getStart()) < 0 || getValue().compareTo(getEnd()) > 0) :
               (getValue().compareTo(getStart()) > 0 || getValue().compareTo(getEnd()) < 0);
    }

    @Override
    public boolean addPropertyChangeListener(PropertyChangeListener listener) {
        return eventSupport.addListener(PropertyChangeEvent.class, listener);
    }

    @Override
    public boolean removePropertyChangeListener(PropertyChangeListener listener) {
        return eventSupport.removeListener(PropertyChangeEvent.class, listener);
    }

    @Override
    public boolean addChangeListener(ChangeListener<Double> listener) {
        return eventSupport.addListener(ChangeEvent.class, listener);
    }

    @Override
    public boolean removeChangeListener(ChangeListener<Double> listener) {
        return eventSupport.removeListener(ChangeEvent.class, listener);
    }
}
