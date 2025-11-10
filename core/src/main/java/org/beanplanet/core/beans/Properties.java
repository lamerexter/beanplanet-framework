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

import org.beanplanet.core.models.Value;

/**
 * Provides an abstract model of a bean that exhibits some properties (refer to the <i>JavaBeans Specification</i> on
 * Properties, Methods and Events (PMEs)).
 * <p>
 * Further extends the ideas set out in thr <i>JavaBeans Specification</i> to expose advanced property features of a
 * bean object.
 * <p>
 * Many forms of data store exhibit properties (or attributes) including JavaBeans, Java Property Files, XML Documents
 * and JNDI trees. This interface attempts to model, independently, properties on any form of data store. Concrete
 * implementations model each specific data repository.
 * <p>
 * In summary, this model provides the following features independently of the data store wrapped:
 * <ul>
 * <li>A list of the properties the store exhibits. Note that the properties a store exhibits may be store dependent.
 * For example, in the context of a JavaBean the properties will be those exposed by introspection of the design
 * patterns but in the context of a JNDI tree the properties will be the nodes of the tree and the attributes of each
 * node.
 * <li>The type of each property (boolean, char, double, float, int, long, Boolean, Characterm Double, Float or any user
 * defined type).
 * <li>Simplistic access to the data properties of the store, including the type of access (read/write). Note that the
 * names of the properties a store exhibits may be store dependent. For example, in the context of a JavaBean the
 * property names will be the JavaBean properties but in the context of an XML DOM tree, the property names will be the
 * names of elements and attributes. See the point below, <i>Expression Language (EL) Nested Property Evaluation</i>,
 * for further information.
 * <li>Expression Language (EL) Nested Property Evaluation. Nested properties are not supported directly in this model;
 * the Universal Expression Language (UEL) and the <code>UEL</code> facade should be used instead to execute nested
 * property expressions and much much more...
 * <li>Implicit and explicit property value conversion. Implicit conversion takes place during property evaluation when
 * and wherever required.
 * </ul>
 */
public interface Properties {
   /**
    * Returns the number of known properties exhibited by the bean.
    * 
    * @return the number of properties of the bean.
    */
   int getNumberOfProperties();

   /**
    * Returns a list of the names of all properties <u>immediately</i> exposed by this bean. Nested property names are
    * not retuned by this method. See <code>getNestedPropertyNames()</code> for returning a full list of all properties
    * including nested properties.
    * 
    * @return an array of the names of all properties of the bean.
    */
   String[] getPropertyNames();

   /**
    * Determines if a named property is a readable property of the bean.
    * 
    * @param name the name of the property whose readable status is to be determined.
    * @return true if the property can be read, false if the named property does not exist or is not a readable property
    */
   boolean isReadableProperty(String name);

   /**
    * Determines if a named property is a writeable property of the bean.
    * 
    * @param name the name of the property whose writeable status is to be determined.
    * @return true if the property can be written, false if the named property does not exist or is not a writeable
    *         property
    */
   boolean isWritableProperty(String name);

   /**
    * Gets the value of the specified property.
    *
    * @param name the name of the property whose value is to be read
    * @return the value of the property.
    * @throws PropertyNotFoundException if no such property exists on the bean
    */
   Value get(String name) throws PropertyNotFoundException;

   /**
    * Gets the value of the specified property.
    *
    * @param name the name of the property whose value is to be read
    * @param defaultValue a default value to use as the property value, if the property does not exist.
    * @return the resulting value of the property.
    * @throws PropertyNotFoundException if no such property exists on the bean
    */
   Value get(String name, Object defaultValue) throws PropertyNotFoundException;

   /**
    * Sets the value of the specified property.
    *
    * @param name the name of the property whose value is to be set
    * @param value the value of the property to set.
    * @throws PropertyNotFoundException if no such writable property exists on the bean
    */
   void set(String name, Object value) throws PropertyNotFoundException;

   /**
    * Returns the type of a named property of the bean.
    * 
    * @param name the name of the property whose type is to be determined.
    * @return the type of the property.
    * @throws PropertyNotFoundException if no such property exists on the bean
    */
   Class<?> getPropertyType(String name) throws PropertyNotFoundException;
}
