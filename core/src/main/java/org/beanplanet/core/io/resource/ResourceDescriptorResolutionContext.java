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
 * A very basic resource resolution context which only holds the resource descriptor. Subclasses may add more information
 * in order to facilitate more complex forms of resource resolution.
 */
public class ResourceDescriptorResolutionContext implements ResourceResolutionContext {
    private String resourceDescriptor;

    public ResourceDescriptorResolutionContext(String resourceDescriptor) {
        this.resourceDescriptor = resourceDescriptor;
    }

    /**
     * Gets the descriptor which describes how to resolve the resource. This might be a URI/URL, identity of the resource or
     * the resource itself.
     *
     * @return the resource descriptor.
     */
    @Override
    public String getResourceDescriptor() {
        return resourceDescriptor;
    }
}
