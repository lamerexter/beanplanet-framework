/*
 * Copyright (C) 2017 Beanplanet Ltd
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.beanplanet.events.core.domain;

import java.time.LocalDateTime;

/**
 * The superclass of all events.
 *
 * @author Gary Watson
 */
public interface Event {
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
    LocalDateTime getEventStartDateTime();

    /**
     * Sets the date and time the event started.
     *
     * @param eventStartDateTime the date and time the event started.
     */
    void setEventStartDateTime(LocalDateTime eventStartDateTime);

    /**
     * Builder-style setter of the date and time the event started.
     *
     * @param eventStartDateTime the date and time the event started.
     * @return this instance for invocation chaining.
     */
    Event withEventStartDateTime(LocalDateTime eventStartDateTime);

    /**
     * Gets the date and time the event ended.
     *
     * @return the date and time the event ended.
     */
    LocalDateTime getEventEndDateTime();

    /**
     * Sets the date and time the event occurred.
     *
     * @param eventEndDateTime the date and time the event occurred.
     */
    void setEventEndDateTime(LocalDateTime eventEndDateTime);

    /**
     * Sets the date and time the event occurred.
     *
     * @param eventEndDateTime the date and time the event occurred.
     * @return this instance for invocation chaining.
     */
    Event withEventEndDateTime(LocalDateTime eventEndDateTime);
}
