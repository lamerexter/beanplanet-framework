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

import org.beanplanet.core.io.IoException;

/**
 * A tagging and functionality interface for resources that support random access read and/or write operations over or
 * beyond their content.
 *
 * @author Gary Watson
 */
public interface RandomAccessResource {
    /**
     * Always answers <code>true</code> as this resource supports random access operations.
     *
     * @return always <code>true</code>.
     */
    boolean supportsRandomAccess();

    /**
     * Creates a new random accessor which provides functionality to read, at random, content of this resource. The
     * random accessor will be initialised in read-only mode and any attempts to invoke write operations will result in
     * an <code>{@link IoException}</code>.
     *
     * <p>
     * Some resources (such as files and in-memory arrays) support random access. Use this method to expose random access
     * capabilities of those resources.
     * </p>
     *
     * @return a random accessor which exposes the random read access operations. The accessor should be closed after
     *         use.
     * @throws IoException if an I/O error occurs creating the random accessor
     */
    RandomAccessor getRandomReadAccessor() throws IoException;

    /**
     * Creates a new random accessor which provides functionality to read and/or write, at random, over or beyond the
     * content of this resource.
     *
     * <p>
     * Some resources (such as files and in-memory arrays) support random access. Use this method to expose random access
     * capabilities of those resources.
     * </p>
     *
     * @return a random accessor which exposes the random access operations. The accessor should be closed after use.
     * @throws IoException if an I/O error occurs creating the random accessor
     */
    RandomAccessor getRandomReadWriteAccessor() throws IoException;
}
