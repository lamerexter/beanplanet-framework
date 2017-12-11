/*
 * Copyright (C) 2017 Beanplanet Ltd
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.beanplanet.core.beans;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A cache of JavaBean related meta data, used by <code>JavaBean</code>.
 *
 * @author Gary Watson
 */
class JavabeanMetadataCache {
    protected Map<String, JavabeanMetadata> metaDataCache = Collections.synchronizedMap(new HashMap<String, JavabeanMetadata>());

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

    public Map<String, JavabeanMetadata> getMetaDataCache() {
        return metaDataCache;
    }

    public void setMetaDataCache(Map<String, JavabeanMetadata> metaDataCache) {
        this.metaDataCache = metaDataCache;
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
        String metaDataKey = getBeanMetaDataKey(startClass, stopClass);
        JavabeanMetadata metaData = metaDataCache.get(metaDataKey);
        if (metaData == null) {
            metaData = new JavabeanMetadata(startClass, stopClass);
            metaDataCache.put(metaDataKey, metaData);
        }

        return metaData;
    }

    /**
     * Computes a hash key for a <code>BeanMetaData</code> object, based on specified design start and stop classes.
     *
     * @param startClass the class whose property design patterns are to be discovered
     * @param stopClass the base class upto which, but not including, the design patterns on the bean will be discovered
     *        and modelled. Can be null to indicate no stop class; in which case all properties upto and including
     *        <code>Object</code> will be inspected.
     * @return a hash key for the specified start/stop classes.
     */
    public String getBeanMetaDataKey(Class<?> startClass, Class<?> stopClass) {
        StringBuilder s = new StringBuilder();
        s.append(startClass.getName());
        if (stopClass != null) {
            s.append("_").append(stopClass.getName());
        }
        return s.toString();
    }
}
