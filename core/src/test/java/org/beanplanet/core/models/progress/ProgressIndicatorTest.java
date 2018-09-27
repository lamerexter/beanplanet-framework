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