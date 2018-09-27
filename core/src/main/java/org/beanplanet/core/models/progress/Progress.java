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
     * The progress completion status, if known, betweem 0 and 1. Set only if the
     * progress completion can be assessed, otherwise null if the progress completion rate cannot be
     * assessed or is indeterminate.
     *
     * @return a value between 0 and 1 which describes the rate of completion, or null if the completion
     * rate is not available at the time or is considered indeterminate.
     */
    Double getCompletion();

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
