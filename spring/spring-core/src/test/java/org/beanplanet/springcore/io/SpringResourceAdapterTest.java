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

package org.beanplanet.springcore.io;

import org.junit.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

public class SpringResourceAdapterTest {
    @Test
    public void givenASpringReadableResource_whenAdaptedToABeanplanetResource_thenTheAdapterResourceSupportsAllFeaturesOfTheAdapted() {
        // Given
        final String resourceContent = "Hello World!";
        Resource adapted = new ByteArrayResource(resourceContent.getBytes());

        // When
        SpringResourceAdapter adapter = new SpringResourceAdapter(adapted);

        // Then
        assertThat(adapter.getAdaptedResource(), sameInstance(adapted));
        assertThat(adapter.readFullyAsString(), equalTo(resourceContent));
    }
}