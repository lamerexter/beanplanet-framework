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
