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

package org.beanplanet.core.beans;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A cache of JavaBean related meta data, used by <code>JavaBean</code>.
 *
 * @author Gary Watson
 */
class JavabeanMetadataCache {
    /** The key of metadata inspected up to and including Object (no stop class). */
    private static final Object NO_STOP_CLASS = new Object();

    /**
     * Each class's metadata, by stop class: kept with the class itself, so two classes of the same name (loaded by
     * different class loaders, as each compiled script's are) never share metadata, and a class's metadata goes when
     * the class is unloaded.
     */
    private final ClassValue<Map<Object, JavabeanMetadata>> metaDataCache = new ClassValue<>() {
        @Override
        protected Map<Object, JavabeanMetadata> computeValue(final Class<?> type) {
            return new ConcurrentHashMap<>();
        }
    };

    private static JavabeanMetadataCache systemCache = new JavabeanMetadataCache();

    /**
     * Constructs a new JavabeanMetadataCache.
     */
    public JavabeanMetadataCache() {
    }

    public static JavabeanMetadataCache getSystemCache() {
        return systemCache;
    }

    public static void setSystemCache(JavabeanMetadataCache systemCache) {
        JavabeanMetadataCache.systemCache = systemCache;
    }

    /**
     * Returns bean property information, including <code>PropertyDescriptor</code> objects, and method information.
     * <p>
     * This method simply calls <code>getBeanMetaData(startClass, null);</code>
     *
     * @param startClass the class whose property design patterns are to be discovered
     * @return meta data detailing the design patterns discovered on the bean class
     * @exception BeanException thrown if an error occurs inspecting the design patterns of the bean.
     * @see #getBeanMetaData(Class, Class)
     */
    public JavabeanMetadata getBeanMetaData(Class<?> startClass) throws BeanException {
        return getBeanMetaData(startClass, null);
    }

    /**
     * Returns bean property information, including <code>PropertyDescriptor</code> objects, and method information.
     * <p>
     * The following details are cached for the inspected design patterns of the bean from the <i>startClass</i> to the
     * <i>stopClass</i> specified:
     * <ol>
     * <li><code>BeanInfo</code>.
     * <li>A list of <code>PropertyDescriptor</code>. objects and property names.
     * <li>The method descriptors and method names.
     * </ol>
     *
     * @param startClass the class whose property design patterns are to be discovered
     * @param stopClass the base class up to which, but not including, the design patterns on the bean will be discovered
     *        and modelled. Can be null to indicate no stop class; in which case all properties up to and including
     *        <code>Object</code> will be inspected.
     * @return meta data detailing the design patterns discovered on the bean class
     * @exception BeanException thrown if an error occurs inspecting the design patterns of the bean.
     */
    public JavabeanMetadata getBeanMetaData(Class<?> startClass, Class<?> stopClass) throws BeanException {
        return metaDataCache.get(startClass).computeIfAbsent(stopClass == null ? NO_STOP_CLASS : stopClass, k -> new JavabeanMetadata(startClass, stopClass));
    }
}
