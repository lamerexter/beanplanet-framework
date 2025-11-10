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

package org.beanplanet.core.util;

import org.beanplanet.core.models.Factory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MultiValueCollectionMapImpl<K, V, C extends Collection<V>> extends AbstractMultiValueMap<K, V, C> {
    public MultiValueCollectionMapImpl() {
        this(new LinkedHashMap<>());
    }

    public MultiValueCollectionMapImpl(Map<K, C> backingMap) {
        this(backingMap, ArrayList::new);
    }

    @SuppressWarnings("unchecked")
    public MultiValueCollectionMapImpl(Map<K, C> backingMap, Factory<? extends Collection<V>> listFactory) {
        super(backingMap, (Factory<C>)listFactory);
    }
}
