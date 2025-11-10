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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public class LambdaCaptureTest {
    public static Stream<String> doIt() {
        CharSequence prefix = "W5";
        List<String> postCodes = asList("W5 4LT", "DL1 2BG");
        return postCodes
                .stream()
                .filter(postCode -> postCode.contains(prefix));
    }

    public static Stream<String> doIt1() {
        List<String> postCodes = asList("W5 4LT", "DL1 2BG");
        return postCodes
                .stream()
                .filter(postCode -> true);
    }
}
