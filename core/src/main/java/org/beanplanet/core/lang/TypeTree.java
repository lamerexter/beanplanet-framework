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

package org.beanplanet.core.lang;

import org.beanplanet.core.models.tree.TreeNode;
import org.beanplanet.core.models.tree.TreeNodeTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Collections.singletonList;

public class TypeTree extends TreeNodeTree<Class<?>> {
    public static final TypeTree typeTreeFor(Class<?> specificType) {
        return new TypeTree(specificType);
    }

    public static final TypeTree typeTreeFor(Class<?> generalType, Class<?> specificType) {
        return new TypeTree(generalType, specificType);
    }

    public TypeTree(Class<?> specificType) {
        this(Object.class, specificType);
    }

    public TypeTree(Class<?> generalType, Class<?> specificType) {
        super(createTypeNodeTree(generalType, specificType));
    }

    private static final TreeNode<Class<?>> createTypeNodeTree(Class<?> generalType, Class<?> specificType) {
        Class<?> fromNode = specificType;
        Class<?> modelRoot = generalType;
        TreeNode<Class<?>> fromTreeNode = createTreeNodeForType(fromNode);

        // Cater for specific case where Object is required in tree (generalType == null) and the specific type
        // is an interface.
        if (generalType == null && specificType != null && specificType.isInterface()) {
            fromNode = Object.class;
            TreeNode<Class<?>> parentTreeNode = createTreeNodeForType(fromTreeNode, fromNode);
            fromTreeNode.setParent(parentTreeNode);
            fromTreeNode = parentTreeNode;
        }

        while (fromNode.getSuperclass() != null && fromNode != modelRoot){
            fromNode = fromNode.getSuperclass();
            TreeNode<Class<?>> parentTreeNode = createTreeNodeForType(fromTreeNode, fromNode);
            fromTreeNode.setParent(parentTreeNode);
            fromTreeNode = parentTreeNode;
        }

        return fromTreeNode;
    }

    @SuppressWarnings("unchecked")
    private static final TreeNode<Class<?>> createTreeNodeForType(final TreeNode<Class<?>> concreteChildNode, final Class<?> type) {
        List<TreeNode<Class<?>>> concreteAndAbstractChildren = new ArrayList<>();
        if (concreteChildNode != null) {
            concreteAndAbstractChildren.add(concreteChildNode);
        }

        //--------------------------------------------------------------------------------------------------------------
        // Add interfaces directly implemented by this type
        //--------------------------------------------------------------------------------------------------------------
        Arrays.stream(type.getInterfaces())
              .map(TypeTree::createTreeNodeForType)
              .forEach(concreteAndAbstractChildren::add);

        return new TreeNode<>(type, (TreeNode[])concreteAndAbstractChildren.toArray(new TreeNode[concreteAndAbstractChildren.size()]));
    }

    private static final TreeNode<Class<?>> createTreeNodeForType(Class<?> type) {
        return createTreeNodeForType(null, type);
    }
}
