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

import org.beanplanet.core.io.resource.ByteArrayResource;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.beanplanet.core.io.DigestUtil.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class DigestUtilTest {

    @Test
    public void sha256HashByteStreamToHexadecimal_String() {
        assertThat(sha256HashByteStreamToHexadecimal("Hello World"),
                   equalTo(hashByteStreamToHexadecimal(
                       new ByteArrayResource("Hello World".getBytes(StandardCharsets.UTF_8)), "SHA-256"))
        );
    }

}