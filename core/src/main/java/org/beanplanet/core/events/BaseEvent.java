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

package org.beanplanet.core.events;

import java.time.Instant;

/**
 * The superclass of all events.
 */
public class BaseEvent implements Event {
    private Object source;
    private String name;
    private Instant startDateTime;
    private Instant endDateTime;

    public BaseEvent() {}

    public BaseEvent(Object source) {
        this(source, null, null);
    }

    public BaseEvent(Object source, Instant startDateTime, Instant endDateTime) {
        this.source = source;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public BaseEvent(Object source, Instant startDateTime) {
        this(source, startDateTime, null);
    }


    @Override
    public Object getSource() {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Instant getEventStartInstant() {
        return startDateTime;
    }

    @Override
    public void setEventStartInstant(Instant startDateTime) {
        this.startDateTime = startDateTime;
    }

    @Override
    public Event withEventStartInstant(Instant startDateTime) {
        setEventStartInstant(startDateTime);
        return this;
    }

    @Override
    public Instant getEventEndInstant() {
        return endDateTime;
    }

    @Override
    public void setEventEndInstant(Instant endDateTime) {
        this.endDateTime = endDateTime;
    }

    @Override
    public Event withEventEndInstant(Instant endDateTime) {
        setEventEndInstant(endDateTime);
        return this;
    }
}
