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

package org.beanplanet.core.crypto;

import org.beanplanet.core.io.resource.OutputStreamResource;
import org.beanplanet.core.io.resource.StringResource;

import java.io.FileOutputStream;

public class Hex {
    public static void main(String ... args) throws Exception {
        boolean isEncode = args.length == 0 || "-e".equals(args[0]);
        if (isEncode) new org.beanplanet.core.codec.Hex().encode(System.in, System.out);
        else new org.beanplanet.core.codec.Hex().decode(System.in, System.out);
    }
}
