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

import org.beanplanet.core.lang.TypeUtil;

import java.beans.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Holds meta data for a <code>JavaBean</code> bean instance. Lazily loaded where possible.
 *
 * @author Gary Watson
 */
class JavabeanMetadata {
    /** the specialised class whose property design patterns are being modelled. */
    protected Class<?> designStartClass = Object.class;

    /** the generalised class whose property design patterns are being modelled. */
    protected Class<?> designStopClass = Object.class;

    /** A tree of PropertyDescriptor objects of the properties of the bean */
    // public TreeNodeTreeModel propertiesTree;
    /** Default BeanDescriptor() instance for the bean. */
    protected BeanInfo beanInfo;

    /** A list of the properties of the bean. */
    protected PropertyDescriptor properties[];

    /** A list of the property names, cached for speed of access. */
    protected String propertyNames[];

    /** A mapping of the name-->descriptor for each property of the bean. */
    protected Map<String, PropertyDescriptor> propertyNamesToDescriptorMap = Collections.emptyMap();

    /** A list of the nested property names, cached for speed of access. */
    protected String nestedPropertyNames[];

    /** A list of the public declared methods. */
    protected Method publicDeclaredMethods[];

    /** A list of the public declared method names */
    protected String publicDeclaredMethodNames[];

    /** A list of the protected declared methods. */
    protected Method protectedDeclaredMethods[];

    /** A list of the protected declared method names */
    protected String protectedDeclaredMethodNames[];

    /** A list of the private declared methods. */
    protected Method privateDeclaredMethods[];

    /** A list of the private declared method names */
    protected String privateDeclaredMethodNames[];

    /** A list of the public methods. */
    protected Method publicMethods[];

    /** A list of the public method names */
    protected String publicMethodNames[];

    /** A list of the protected methods. */
    protected Method protectedMethods[];

    /** A list of the protected method names */
    protected String protectedMethodNames[];

    /** A list of the private methods. */
    protected Method privateMethods[];

    /** A list of the private method names */
    protected String privateMethodNames[];

    /**
     * Constructs a new <code>JavabeanMetadata</code> with no initial design patterns.
     */
    public JavabeanMetadata() {
    }

    /**
     * Constructs a new <code>JavabeanMetadata</code>.
     *
     * @param startClass the class whose property design patterns are to be discovered.
     */
    public JavabeanMetadata(Class<?> startClass) {
        this(startClass, null);
    }

    /**
     * Constructs a new <code>JavabeanMetadata</code>.
     *
     * @param startClass the class whose property design patterns are to be discovered.
     * @param stopClass the base class upto which, but not including, the design patterns on the bean will be discovered
     *        and modelled. Can be null to indicate no stop class; in which case all properties upto and including
     *        <code>Object</code> will be inspected.
     */
    public JavabeanMetadata(Class<?> startClass, Class<?> stopClass) {
        this.designStartClass = startClass;
        this.designStopClass = stopClass;
    }

    public Class<?> getDesignStartClass() {
        return designStartClass;
    }

    public Class<?> getDesignStopClass() {
        return designStopClass;
    }

    public BeanInfo getBeanInfo() {
        if (beanInfo == null) {
            try {
                if (designStartClass == Object.class && designStopClass == Object.class) {
                    beanInfo = Introspector.getBeanInfo(Object.class);
                }
                else if (designStartClass.isInterface()) {
                    beanInfo = Introspector.getBeanInfo(designStartClass);
                }
                else {
                    beanInfo = Introspector.getBeanInfo(designStartClass, designStopClass);
                }
            } catch (IntrospectionException ex) {
                throw new BeanException("Error determining design patterns (compile-time) of bean: start-class="
                    + designStartClass.getName() + " stop-class=" + designStopClass.getName());
            }
        }
        return beanInfo;
    }

    public PropertyDescriptor[] getPropertiesDescriptors() {
        if (properties == null) {
            properties = getBeanInfo().getPropertyDescriptors();
        }
        return properties;
    }

    public String[] getPropertyNames() {
        getPropertiesDescriptors();
        if (propertyNames == null) {
            String propNames[] = new String[properties.length];
            Map<String, PropertyDescriptor> propMap = new HashMap<String, PropertyDescriptor>();
            for (int n = 0; n < properties.length; n++) {
                propNames[n] = properties[n].getName();
                propMap.put(propNames[n], properties[n]);
            }
            propertyNames = propNames;
            propertyNamesToDescriptorMap = propMap;
        }
        return propertyNames;
    }

    public Optional<PropertyDescriptor> getPropertyDescriptor(String propertyName) {
        return Optional.ofNullable(getPropertyNamesToDescriptorMap().get(propertyName));
    }

    private Map<String, PropertyDescriptor> getPropertyNamesToDescriptorMap() {
        if (propertyNames == null) {
            getPropertyNames(); // Cache property name
        }
        // info
        return propertyNamesToDescriptorMap;
    }

    /**
     * Returns a list of the names of all properties exposed by the bean, including all nested properties (where
     * applicable to do so).
     * <p>
     * For example, if there exist immediate properties of the bean, <i>A</i> and <i>B</i> then the list will comprise of
     * <i>A,B</i>. However, if the type of property <i>A</i> also exibits properties <i>C</i> and <i>D</i> then the list
     * will comprise of <i>A,A.C,A.D,B</i>.
     *
     * @return an array of the names of all properties of the bean.
     * @exception BeanException thrown if access to the properties is forbidden or a problem occurred obtaining the
     *            property names.
     */
    public String[] getNestedPropertyNames() throws BeanException {
        if (nestedPropertyNames == null) {
            List<String> hNames = new ArrayList<String>(getPropertyNames().length * 2); // a guess
            // at the
            // length!

            for (int n = 0; n < propertyNames.length; n++) {
                hNames.add(propertyNames[n]);
                Set<Class<?>> pathTypes = new HashSet<Class<?>>();
                // pathTypes.add(beanMetaData.properties[n].getPropertyType());
                addDescendentPropertyNames(propertyNames[n], properties[n], hNames, pathTypes);
            }

            nestedPropertyNames = hNames.toArray(new String[hNames.size()]);
        }

        return nestedPropertyNames;
    }

    private void addDescendentPropertyNames(String parentPropertyName,
                                            PropertyDescriptor property,
                                            List<String> namesList,
                                            Set<Class<?>> pathTypes) throws BeanException {
        // ------------------------------------------------------------------------
        // The following checks terminate the recursive process:
        // - recursive types loop A->B->C--->A
        // - type of property is unknown or null
        // - type of property is Object class
        // - type of property is XClass class
        // ------------------------------------------------------------------------
        Class<?> propertyType = property.getPropertyType();
        if (propertyType == null) {
            // Could be an indexed property
            if (property instanceof IndexedPropertyDescriptor) {
                propertyType = ((IndexedPropertyDescriptor) property).getIndexedPropertyType();
            }
        }

        if (pathTypes.contains(propertyType) || propertyType == null || propertyType == Object.class
            || propertyType == Class.class || TypeUtil.isPrimitiveTypeOrWrapperClass(propertyType)) {
            return;
        }
        pathTypes.add(propertyType);

        JavabeanMetadata beanMetaData = new JavabeanMetadata(propertyType);

        if (beanMetaData.getNestedPropertyNames() != null) {
            String nestedProps[] = beanMetaData.getNestedPropertyNames();
            for (int n = 0; n < nestedProps.length; n++) {
                namesList.add(parentPropertyName + "." + nestedProps[n]);
            }
        }
        else {
            for (int n = 0; n < beanMetaData.getPropertyNames().length; n++) {
                String hPropName = (parentPropertyName + "." + beanMetaData.getPropertyNames()[n]);
                namesList.add(hPropName);
                Set<Class<?>> branchPathTypes = new HashSet<Class<?>>(pathTypes);
                addDescendentPropertyNames(hPropName, beanMetaData.getPropertiesDescriptors()[n], namesList, branchPathTypes);
                branchPathTypes.clear();
                branchPathTypes.addAll(pathTypes);
            }
        }
    }

    public void setNestedPropertyNames(String[] nestedPropertyNames) {
        this.nestedPropertyNames = nestedPropertyNames;
    }

    public Method[] getPublicDeclaredMethods() {
        if (publicDeclaredMethods == null) {
            publicDeclaredMethods = selectDeclaredMethods(Modifier.PUBLIC);
        }
        return publicDeclaredMethods;
    }

    private Method[] selectDeclaredMethods(int modifiers) {
        Method declaredMethods[] = designStartClass.getDeclaredMethods();
        List<Method> selectedMethods = new ArrayList<Method>(declaredMethods.length);
        for (int n = 0; n < declaredMethods.length; n++) {
            if ((declaredMethods[n].getModifiers() & modifiers) == modifiers) {
                selectedMethods.add(declaredMethods[n]);
            }
        }
        return selectedMethods.toArray(new Method[selectedMethods.size()]);
    }

    public void setPublicDeclaredMethods(Method[] publicDeclaredMethods) {
        this.publicDeclaredMethods = publicDeclaredMethods;
    }

    public String[] getPublicDeclaredMethodNames() {
        if (publicDeclaredMethodNames == null) {
            String methodNames[] = new String[getPublicDeclaredMethods().length];
            for (int n = 0; n < publicDeclaredMethods.length; n++) {
                methodNames[n] = publicDeclaredMethods[n].getName();
            }
            publicDeclaredMethodNames = methodNames;
        }
        return publicDeclaredMethodNames;
    }

    public void setPublicDeclaredMethodNames(String[] publicDeclaredMethodNames) {
        this.publicDeclaredMethodNames = publicDeclaredMethodNames;
    }

    public Method[] getProtectedDeclaredMethods() {
        if (protectedDeclaredMethods == null) {
            protectedDeclaredMethods = selectDeclaredMethods(Modifier.PROTECTED);
        }
        return protectedDeclaredMethods;
    }

    public void setProtectedDeclaredMethods(Method[] protectedDeclaredMethods) {
        this.protectedDeclaredMethods = protectedDeclaredMethods;
    }

    public String[] getProtectedDeclaredMethodNames() {
        if (protectedDeclaredMethodNames == null) {
            String methodNames[] = new String[getProtectedDeclaredMethods().length];
            for (int n = 0; n < protectedDeclaredMethods.length; n++) {
                methodNames[n] = protectedDeclaredMethods[n].getName();
            }
            protectedDeclaredMethodNames = methodNames;
        }
        return protectedDeclaredMethodNames;
    }

    public void setProtectedDeclaredMethodNames(String[] protectedDeclaredMethodNames) {
        this.protectedDeclaredMethodNames = protectedDeclaredMethodNames;
    }

    public Method[] getPrivateDeclaredMethods() {
        if (privateDeclaredMethods == null) {
            privateDeclaredMethods = selectDeclaredMethods(Modifier.PRIVATE);
        }
        return privateDeclaredMethods;
    }

    public void setPrivateDeclaredMethods(Method[] privateDeclaredMethods) {
        this.privateDeclaredMethods = privateDeclaredMethods;
    }

    public String[] getPrivateDeclaredMethodNames() {
        if (privateDeclaredMethodNames == null) {
            String methodNames[] = new String[getPrivateDeclaredMethods().length];
            for (int n = 0; n < privateDeclaredMethods.length; n++) {
                methodNames[n] = privateDeclaredMethods[n].getName();
            }
            privateDeclaredMethodNames = methodNames;
        }
        return privateDeclaredMethodNames;
    }

    public void setPrivateDeclaredMethodNames(String[] privateDeclaredMethodNames) {
        this.privateDeclaredMethodNames = privateDeclaredMethodNames;
    }

    public Method[] getPublicMethods() {
        if (publicMethods == null) {
            publicMethods = designStartClass.getMethods();
        }
        return publicMethods;
    }

    public void setPublicMethods(Method[] publicMethods) {
        this.publicMethods = publicMethods;
    }

    public String[] getPublicMethodNames() {
        if (publicMethodNames == null) {
            String methodNames[] = new String[getPublicMethods().length];
            for (int n = 0; n < publicMethods.length; n++) {
                methodNames[n] = publicMethods[n].getName();
            }
            publicMethodNames = methodNames;
        }
        return publicMethodNames;
    }

    public void setPublicMethodNames(String[] publicMethodNames) {
        this.publicMethodNames = publicMethodNames;
    }

    public Method[] getProtectedMethods() {
        if (protectedMethods == null) {
            protectedMethods = new Method[0];
        }
        return protectedMethods;
    }

    public void setProtectedMethods(Method[] protectedMethods) {
        this.protectedMethods = protectedMethods;
    }

    public String[] getProtectedMethodNames() {
        if (protectedMethodNames == null) {
            String methodNames[] = new String[getProtectedMethods().length];
            for (int n = 0; n < protectedMethods.length; n++) {
                methodNames[n] = protectedMethods[n].getName();
            }
            protectedMethodNames = methodNames;
        }
        return protectedMethodNames;
    }

    public void setProtectedMethodNames(String[] protectedMethodNames) {
        this.protectedMethodNames = protectedMethodNames;
    }

    public Method[] getPrivateMethods() {
        if (privateMethods == null) {
            privateMethods = new Method[0];
        }
        return privateMethods;
    }

    public void setPrivateMethods(Method[] privateMethods) {
        this.privateMethods = privateMethods;
    }

    public String[] getPrivateMethodNames() {
        if (privateMethodNames == null) {
            String methodNames[] = new String[getPrivateMethods().length];
            for (int n = 0; n < privateMethods.length; n++) {
                methodNames[n] = privateMethods[n].getName();
            }
            privateMethodNames = methodNames;
        }
        return privateMethodNames;
    }

    public void setPrivateMethodNames(String[] privateMethodNames) {
        this.privateMethodNames = privateMethodNames;
    }

    /**
     * Determines if a named property is a readable property of the bean; indexed properties are not considered readable
     * (simply by name alone) by this method.
     *
     * @param name a simple, non-nested and non-indexed, name specification of the property whose readability is to be
     *        tested.
     * @return true if the property can be read, false if the named property does not exist or is not a readable
     *         property.
     */
    public boolean isReadableProperty(String name) throws BeanException {
        boolean isReadable = false;
        PropertyDescriptor property = getPropertyNamesToDescriptorMap().get(name);

        if (property != null) {
            Method getterMethod = (property instanceof IndexedPropertyDescriptor ? null : property.getReadMethod());
            isReadable = getterMethod != null;
        }

        return isReadable;
    }

    /**
     * Determines if a named property is a readable indexed property of the bean.
     *
     * @param name an indexed, non-nested, name specification of the property whose readability is to be tested.
     * @return true if the property can be read, false if the named property does not exist or is not a readable
     *         property.
     */
    public boolean isReadableIndexedProperty(String name) throws BeanException {
        boolean isReadable = false;
        PropertyDescriptor property = getPropertyNamesToDescriptorMap().get(name);

        if (property != null) {
            isReadable = (property instanceof IndexedPropertyDescriptor)
                && ((IndexedPropertyDescriptor) property).getIndexedReadMethod() != null;
        }

        return isReadable;
    }

    /**
     * Determines if a named property is a writeable property of the bean; indexed properties are not considered writable
     * (simply by name alone) by this method.
     *
     * @param name a simple, non-nested and non-indexed, name specification of the property whose writability is to be
     *        tested.
     * @return true if the property can be written, false if the named property does not exist or is not a writeable
     *         property.
     */
    public boolean isWriteableProperty(String name) throws BeanException {
        boolean isWritable = false;
        PropertyDescriptor property = getPropertyNamesToDescriptorMap().get(name);

        if (property != null) {
            Method setterMethod = (property instanceof IndexedPropertyDescriptor ? null : property.getWriteMethod());
            isWritable = setterMethod != null;
        }

        return isWritable;
    }

    /**
     * Determines if a named property is a writable indexed property of the bean.
     *
     * @param name a indexed, non-nested, name specification of the property whose writability is to be tested.
     * @return true if the property can be written, false if the named property does not exist or is not a writeable
     *         indexed property.
     */
    public boolean isWriteableIndexedProperty(String name) throws BeanException {
        boolean isWritable = false;
        PropertyDescriptor property = getPropertyNamesToDescriptorMap().get(name);

        if (property != null) {
            isWritable = (property instanceof IndexedPropertyDescriptor)
                && ((IndexedPropertyDescriptor) property).getIndexedWriteMethod() != null;
        }

        return isWritable;
    }
}
