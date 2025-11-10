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
package org.beanplanet.core.cache;

import org.beanplanet.core.models.Named;


/**
 * Represents a caching policy. By extending the <code>CacheItemListener</cod>
 * interface, caching policies listen for events that relate to items in
 * the cache - newly added or removed items, or if the cache is cleared.
 * 
 * @author Gary Watson
 */
public interface CachePolicy<K, V> extends CacheListener<K, V>, Named {
}
