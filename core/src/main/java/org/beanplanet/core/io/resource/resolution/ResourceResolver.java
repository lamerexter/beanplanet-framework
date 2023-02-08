/*
 *  MIT Licence:
 *
 *  Copyright (C) 2018 Beanplanet Ltd
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without restriction
 *  including without limitation the rights to use, copy, modify, merge,
 *  publish, distribute, sublicense, and/or sell copies of the Software,
 *  and to permit persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 *  KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 *  PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 *  DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 *  CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *  CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  DEALINGS IN THE SOFTWARE.
 */

package org.beanplanet.core.io.resource.resolution;

import org.beanplanet.core.io.resource.Resource;
import org.beanplanet.core.io.resource.ResourceResolutionContext;

import java.util.Optional;

/**
 * A strategy for resolving resources from resource descriptors.
 *
 * <p>Examples of possible resource descriptors include:
 * <ul>
 *     <li>URIs of the form <code>/</code>, <code>/company/employees?gender=M&country=USA</code> and <code>WEB-INF/services/config.yaml</code>.</li>
 *     <li>URLs of the form <code>https://acme.com</code>, <code>file:///usr/home/pleb/.profile</code>, <code>git@github.com:wizzkid/stocktrader.git</code> and <code>WEB-INF/services/config.yaml</code></li>
 *     <li>Classpath resources of the form <code>classpath:com/acme/config/app.xml</code></li>
 * </ul>
 * </p>
 * <p>
 *     Some resolvers may support wildcard (e.g. glob-like) syntax for resolving multiple similar resources at once, such as <code>classpath:/WEB-INF/config/*.properties</code> and
 *     <code>file://c/Users/pleb/AppData/{@literal *}.dat</code>. This is particularly useful for resolving all similarly names resources in all JARs or modules of the application or filesystem.
 * </p>
 */
public interface ResourceResolver {
    Resource[] EMPTY_RESOURCES = new Resource[0];
    String CLASSPATH_RESOURCE_PREFIX = "classpath:";
    String FILE_RESOURCE_PREFIX = "file:";

    /**
     * Attempts to resolve a single resource from the given descriptor. If the descriptor contains wildcards, resolving to multiple resources, the first of the resolved resources will be returned.
     *
     * @param descriptor a descriptor of the resource being sought.
     * @return optionally a resource found matching the given descriptor, or empty if no resource was found.
     */
    default Optional<Resource> resolveResource(String descriptor) {
        final Resource[] resolvedResources = resolveResources(descriptor);
        return resolvedResources.length > 0 ? Optional.of(resolvedResources[0]) : Optional.empty();
    }

    /**
     * Attempts to resolve one or more resources from the given descriptor. If the descriptor contains wildcards, resolving to multiple resources, all of the resolved resources will be returned.
     *
     * @param descriptor a descriptor of the resource(s) being sought.
     * @return the resources matching the given descriptor, or empty (never <code>null</code>) if none were found.
     */
    Resource[] resolveResources(String descriptor);
}
