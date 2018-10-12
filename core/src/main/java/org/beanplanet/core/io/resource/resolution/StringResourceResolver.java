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
import org.beanplanet.core.io.resource.StringResource;

/**
 * A resource resolver which assumes the resource descriptor is the resource itself. As a consequence, this resolver will
 * always resolve the resource from a non-null resource descriptor.
 */
public class StringResourceResolver implements ResourceResolver {
    /**
     * Always resolves to a string resource, by assuming the resource descriptor is the string resource itself.
     *
     * @param context the context under which resource resolution is to be carried out.
     * @return the string resource resolved only if the resource descriptor in the given context is non-null, null otherwise.
     */
    @Override
    public Resource resolve(ResourceResolutionContext context) {
        return context.getResourceDescriptor() != null ? new StringResource(context.getResourceDescriptor()) : null;
    }
}
