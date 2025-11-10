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

import org.beanplanet.core.models.Registry;
import org.beanplanet.core.net.http.MediaType;

import java.lang.reflect.Type;
import java.util.stream.Stream;

public interface HttpMessageBodyConverterRegistry extends Registry<MediaType, HttpMessageBodyConverter<?>> {

    /**
     * Find handlers that support read the given media type and class.
     *
     * @param mediaType the media type the handler should support reading from.
     * @param type the type the handler should support reading from the given media type.
     * @return a list of the read handlers, which will never be null but may be empty.
     * @param <T> the type of object the handler will support reading.
     */
    <T> Stream<HttpMessageBodyConverter<T>> findFromConverters(MediaType mediaType, Type type);

    /**
     * Find handlers that support writing the given media type and class.
     *
     * @param mediaType the media type the handler should support writing to.
     * @param type the type the handler should support writing to the given media type.
     * @return a list of the write handlers, which will never be null but may be empty.
     * @param <T> the type of object the handler will support writing.
     */
    <T> Stream<HttpMessageBodyConverter<T>> findToConverters(MediaType mediaType, Type type);
}
