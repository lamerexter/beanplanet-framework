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

package org.beanplanet.core.net.http.converter;

import org.beanplanet.core.io.resource.Resource;
import org.beanplanet.core.lang.ParameterisedTypeReference;
import org.beanplanet.core.net.http.*;
import org.beanplanet.core.net.http.converter.annotations.HttpMessageBodyConverter;

import java.lang.reflect.Type;
import java.util.Optional;

/**
 * An HTTP message body converter capable of converting generic {@link Resource} types to/from
 * HTTP message bodies.
 */
@HttpMessageBodyConverter
public class ResourceHttpMessageBodyConverter extends AbstractHttpMessageBodyConverter<Resource> {
    public ResourceHttpMessageBodyConverter() {
        super(MediaTypes.ALL);
    }

    /**
     * Whether this handler supports reading from or writing to a given type.
     *
     * @param type the type to be tested for support by this handler.
     * @return true because this handler will attempt to marshall from/to any type, in context of the supported
     * media types.
     */
    @Override
    public boolean supports(Type type) {
        return type instanceof Class<?> && Resource.class.isAssignableFrom((Class<?>)type);
    }

    /**
     * Reads an object from the given input message.
     *
     * @param type    the type of object to be read from the input.
     * @param message the request message from which te object is to be read.
     * @return the object read.
     */
    @Override
    public Resource convertFrom(ParameterisedTypeReference<Resource> type, HttpMessage message) {
        throw new UnsupportedOperationException();
    }

    /**
     * Writes the given object to the output message.
     *
     * @param resource       the resource to be written.
     * @param messageHeaders the headers of the message where the object is to be written.
     * @return the given resource.
     */
    @Override
    public Resource convertTo(Resource resource, HttpMessageHeaders messageHeaders) {
        MediaType resourceContentType = SystemMediaTypesRegistry.getInstance().findMediaType(resource).orElse(MediaTypes.Application.OCTET_STREAM);
        messageHeaders.setContentType(resourceContentType);

        return resource;
    }
}
