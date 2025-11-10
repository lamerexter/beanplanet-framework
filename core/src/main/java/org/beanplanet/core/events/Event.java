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
 *
 * @author Gary Watson
 */
public interface Event {

    /**
     * Gets the source of the event.
     *
     * @return the source of the event, which may be null if unknown.
     */
    Object getSource();

    /**
     * Gets the name of the event.
     *
     * @return the name of the event.
     */
    String getName();

    /**
     * Gets the date and time the event started.
     *
     * @return the date and time the event started.
     */
    Instant getEventStartInstant();

    /**
     * Sets the date and time the event started.
     *
     * @param eventStartDateTime the date and time the event started.
     */
    void setEventStartInstant(Instant eventStartDateTime);

    /**
     * models-style setter of the date and time the event started.
     *
     * @param eventStartDateTime the date and time the event started.
     * @return this instance for invocation chaining.
     */
    Event withEventStartInstant(Instant eventStartDateTime);

    /**
     * Gets the date and time the event ended.
     *
     * @return the date and time the event ended.
     */
    Instant getEventEndInstant();

    /**
     * Sets the date and time the event occurred.
     *
     * @param eventEndDateTime the date and time the event occurred.
     */
    void setEventEndInstant(Instant eventEndDateTime);

    /**
     * Sets the date and time the event occurred.
     *
     * @param eventEndDateTime the date and time the event occurred.
     * @return this instance for invocation chaining.
     */
    Event withEventEndInstant(Instant eventEndDateTime);
}
