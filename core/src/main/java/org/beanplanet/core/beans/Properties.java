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
    * Returns the number of known properties exibited by the bean.
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
