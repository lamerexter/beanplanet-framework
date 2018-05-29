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

import org.beanplanet.core.models.tree.SwingTreeModelAdapter;
import org.beanplanet.core.models.tree.TreeNode;
import org.beanplanet.core.models.tree.TreeNodeTree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.function.IntFunction;

import static java.util.Arrays.stream;
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

        while (fromNode.getSuperclass() != null && fromNode != modelRoot){
            fromNode = fromNode.getSuperclass();
            TreeNode<Class<?>> parentTreeNode = createTreeNodeForType(fromNode);
            fromTreeNode.setParent(parentTreeNode);
            parentTreeNode.setChildren(singletonList(fromTreeNode));
            fromTreeNode = parentTreeNode;
        }

        return fromTreeNode;
    }

    private static final TreeNode<Class<?>> createTreeNodeForType(Class<?> type) {
        //--------------------------------------------------------------------------------------------------------------
        // Add interfaces directly implemented by this type
        //--------------------------------------------------------------------------------------------------------------
        TreeNode<Class<?>> interfaceChildrenNodes[] =
                Arrays.stream(type.getInterfaces())
                      .map(TypeTree::createTreeNodeForType)
                      .toArray((IntFunction<TreeNode<Class<?>>[]>) TreeNode[]::new);

        return new TreeNode<>(type, interfaceChildrenNodes);
    }

    public static void main(String args[]) throws Exception
    {
        JFrame frame = new JFrame("Type Tree");
        frame.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });

        JTree tree = new JTree(new SwingTreeModelAdapter(TypeTree.typeTreeFor(TypeUtil.loadClass(args[0]))));
        JScrollPane scroller = new JScrollPane(tree);
        frame.getContentPane().add(scroller, BorderLayout.CENTER);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

}
