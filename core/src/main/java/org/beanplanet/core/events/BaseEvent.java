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
