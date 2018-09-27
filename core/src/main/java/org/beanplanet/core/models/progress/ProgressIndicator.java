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
