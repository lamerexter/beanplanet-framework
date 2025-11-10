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

public interface PropertyChangeEventSource extends EventSource {
    /**
     * Adds a listener of property changes to this event source.
     *
     * @param listener the property change listener to be added, which may not be null.
     * @return true if the property listener was added, false otherwise.
     */
    boolean addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Removes a listener of property changes from this event source.
     *
     * @param listener the property change listener to be removed, which may not be null.
     * @return true if the property listener was removed, false otherwise.
     */
    boolean removePropertyChangeListener(PropertyChangeListener listener);
}
