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

import org.beanplanet.core.events.ChangeEventSource;
import org.beanplanet.core.events.PropertyChangeEventSource;

/**
 * Model of progress of a process from start to finish.
 *
 * <p>The model is also a source or property change events for any changes to its configuration properties, including
 * whether it's indeterminate or not and its completion. It is also a source of change events to indicate
 * when its completion rate changes.</p>
 */
public interface Progress extends PropertyChangeEventSource, ChangeEventSource<Double> {
    /**
     * The progress completion status, if known, between 0 and 1. Set only if the
     * progress completion can be assessed, otherwise null if the progress completion rate cannot be
     * assessed or is indeterminate.
     *
     * @return a value between 0 and 1 which describes the rate of completion, or null if the completion
     * rate is not available at the time or is considered indeterminate.
     */
    Double getCompletion();

    /**
     * The percentage of completion, rounded down to be between 0 and 100.
     *
     * @return 0 if indeterminate or completion status cannot be assessed at the time of call, or the percentage complete between 0 and 100.
     */
    default int getPercentComplete() {
        final Double complete = getCompletion();
        return complete == null ? 0 : (int)(complete * 100d);
    }

    /**
     * Whether the progredss of the process being performed is complete.
     *
     * @return true if the progress is marked as complete, false if still active.
     */
    boolean isComplete();

    /**
     * Whether the progress of the process being performed cannot be determined at this time. If the progress
     * is indeterminate it may still be complete. If it is indeterminate then calls to {@link #getCompletion()}
     * will return null. For a process whose progress is not indetermimate, its completion can be measured through
     * calls to {@link #getCompletion()}.
     *
     * @return true if the progress is indeterminate, false if it is measurable.
     */
    boolean isIndeterminate();
}
