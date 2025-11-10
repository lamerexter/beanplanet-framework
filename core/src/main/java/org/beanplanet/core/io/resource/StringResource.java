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

package org.beanplanet.core.io.resource;

/**
 * Convenience implementation of a string resource.
 *
 * @author Gary Watson
 */
public class StringResource extends CharSequenceResource {
    /**
     * Constructs a new string resource with no initial backing string.
     *
     */
    public StringResource() {
    }

    /**
     * Consructs a new string resource with the specified backing string.
     *
     * @param string the string backing this resource.
     */
    public StringResource(String string) {
        super(string);
    }
}
