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

package org.beanplanet.core.io;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;


public class IoExceptionTest {
    @Test
    public void ctorNoArgs() {
        IoException exception = new IoException();

        assertThat(exception.getMessage(), nullValue());
        assertThat(exception.getCause(), nullValue());
    }

    @Test
    public void ctorMessage() {
        IoException exception = new IoException("The message");

        assertThat(exception.getMessage(), equalTo("The message"));
        assertThat(exception.getCause(), nullValue());
    }

    @Test
    public void ctorCause() {
        Exception cause = new Exception();
        IoException exception = new IoException(cause);

        assertThat(exception.getMessage(), notNullValue());
        assertThat(exception.getCause(), sameInstance(cause));
    }

    @Test
    public void ctorMessageAndCause() {
        Exception cause = new Exception();
        IoException exception = new IoException("The message", cause);

        assertThat(exception.getMessage(), equalTo("The message"));
        assertThat(exception.getCause(), sameInstance(cause));
    }
}