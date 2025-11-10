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

import org.beanplanet.core.BeanTestSupport;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ProgressIndicatorTest {
    @Test
    public void propertiesEventsAndToString() {
        new BeanTestSupport(new ProgressIndicator())
                .withExcludedProperties("completion")
                .testProperties()
                .testBuilderProperties()
                .testPropertyChangeEvents();
    }

    @Test
    public void completion() {
        // Given
        ProgressIndicator progress = new ProgressIndicator(0, 9);

        // When
        progress.setValue(0);

        // Then
        assertThat(progress.isIndeterminate(), is(false));
        assertThat(progress.isComplete(), is(false));
        assertThat(progress.getCompletion(), equalTo(0.1d));

        // When
        progress.setValue(9);

        // Then
        assertThat(progress.isIndeterminate(), is(false));
        assertThat(progress.isComplete(), is(true));
        assertThat(progress.getCompletion(), equalTo(1d));

        // When
        progress.setValue(4);

        // Then
        assertThat(progress.isIndeterminate(), is(false));
        assertThat(progress.isComplete(), is(false));
        assertThat(progress.getCompletion(), equalTo(0.5d));
    }

    @Test
    public void completionWithDescendingOrder() {
        // Given
        ProgressIndicator progress = new ProgressIndicator(9, 0);

        // When
        progress.setValue(0);

        // Then
        assertThat(progress.isIndeterminate(), is(false));
        assertThat(progress.isComplete(), is(true));
        assertThat(progress.getCompletion(), equalTo(1d));

        // When
        progress.setValue(9);

        // Then
        assertThat(progress.isIndeterminate(), is(false));
        assertThat(progress.isComplete(), is(false));
        assertThat(progress.getCompletion(), equalTo(0.1d));

        // When
        progress.setValue(4);

        // Then
        assertThat(progress.isIndeterminate(), is(false));
        assertThat(progress.isComplete(), is(false));
        assertThat(progress.getCompletion(), equalTo(0.6d));
    }

    @Test
    public void completionWithIndeterminateProgress() {
        // Given
        ProgressIndicator progress = new ProgressIndicator(9, 0, null);

        // Then
        assertThat(progress.isIndeterminate(), is(true));
    }

    @Test
    public void indeterminate() {
        assertThat(new ProgressIndicator(9, 0, null).isIndeterminate(), is(true));
        assertThat(new ProgressIndicator(null, 0, 5).isIndeterminate(), is(true));
        assertThat(new ProgressIndicator(9, null, 5).isIndeterminate(), is(true));
        assertThat(new ProgressIndicator(0, 9, -1).isIndeterminate(), is(true));
        assertThat(new ProgressIndicator(9, 9, 10).isIndeterminate(), is(true));
        assertThat(new ProgressIndicator(0, 0, -1).isIndeterminate(), is(true));
        assertThat(new ProgressIndicator(0, 0, 1).isIndeterminate(), is(true));

        assertThat(new ProgressIndicator(0, 9, 5).isIndeterminate(), is(false));
        assertThat(new ProgressIndicator(0, 9, 0).isIndeterminate(), is(false));
        assertThat(new ProgressIndicator(0, 9, 9).isIndeterminate(), is(false));
        assertThat(new ProgressIndicator(0, 0, 0).isIndeterminate(), is(false));
    }
}