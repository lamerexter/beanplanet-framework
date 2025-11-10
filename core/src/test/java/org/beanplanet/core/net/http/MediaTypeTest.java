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

package org.beanplanet.core.net.http;

import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class MediaTypeTest {
    @Test
    public void givenAFullSpecMediaType_whenConstructed_theElementsAreParsedCorrectly() {
        assertThat(new MediaType("type/subtype; charset=utf-8").getType(), equalTo("type"));
        assertThat(new MediaType("type/subtype; charset=utf-8").getSubtype(), equalTo("subtype"));
        assertThat(new MediaType("type/subtype; charset=utf-8").getParameters(), equalTo(new Parameters(Map.of("charset", "utf-8"))));
        assertThat(new MediaType("type/subtype; Charset=\"utf-8\"").getParameters(), equalTo(new Parameters(Map.of("Charset", "utf-8"))));
        assertThat(new MediaType("type/subtype; Charset=\"utf-8\"").getParameters().get("charSET").orElse(null), equalTo("utf-8"));
    }
}