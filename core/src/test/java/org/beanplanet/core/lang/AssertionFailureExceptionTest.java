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

package org.beanplanet.core.lang;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;


/**
 * Unit tests for {@link AssertionFailureException}.
 */
public class AssertionFailureExceptionTest {
    @Test
    public void ctorNoArgs() {
        assertThat(new AssertionFailureException().getMessage(), nullValue());
        assertThat(new AssertionFailureException().getCause(), nullValue());
    }

    @Test
    public void ctorMessage() {
        AssertionFailureException exception = new AssertionFailureException("theMessage");
        assertThat(exception.getMessage(), equalTo("theMessage"));
        assertThat(exception.getCause(), nullValue());
    }

    @Test
    public void ctorMessageAndCause() {
        Throwable cause = new Exception();
        AssertionFailureException exception = new AssertionFailureException("theMessage", cause);
        assertThat(exception.getMessage(), equalTo("theMessage"));
        assertThat(exception.getCause(), sameInstance(cause));
    }
}