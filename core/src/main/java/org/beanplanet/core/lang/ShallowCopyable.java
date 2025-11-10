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

/**
 * A tagging structural interface, implemented by objects that are able to be shallow copied.
 *
 * <p>An object is capable of being shallow copied if it can produce a new instance of itself, initialised with the values of
 * both it's value members (primitives) and reference type members (objects, classes and arrays). There is an important distinction between shallow and deep copies:
 * shallow copy copies the 'values' of Reference type members (i.e. the pointer, not the whole object); depp copy copies or 'clones' the referenced object.
 * </p>
 */
public interface ShallowCopyable<T> {
    /**
     * Performs shallow copy of this object, initialised with value copies of its members - including the values of pointers to referenced objects (i.e. referred objects are not created).
     *
     * @return a new shallow-copied instance.
     */
    T copyShallow();
}
