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

package org.beanplanet.core.models.tree;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.asList;

public class TreeNode<E> implements Serializable {
    private TreeNode<E> parent;
    private E managedObject;
    private List<TreeNode<E>> children;

    public TreeNode() {}

    public TreeNode(E managedObject, TreeNode<E> ... children) {
        this(null, managedObject, children);
    }

    public TreeNode(TreeNode<E> parent, E managedObject, TreeNode<E> ... children) {
        this(parent, managedObject, asList(children));
    }

    public TreeNode(TreeNode<E> parent, E managedObject, List<TreeNode<E>> children) {
        this.parent = parent;
        this.managedObject = managedObject;
        this.children = children;

        if (children != null) {
            this.children.stream().forEach(c -> c.setParent(this));
        }
    }

    public TreeNode<E> getParent() {
        return parent;
    }

    public void setParent(TreeNode<E> parent) {
        this.parent = parent;
    }

    public TreeNode(E managedObject) {
        this.managedObject = managedObject;
    }
    public E getManagedObject() {
        return managedObject;
    }

    public void setManagedObject(E managedObject) {
        this.managedObject = managedObject;
    }

    List<TreeNode<E>> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode<E>> children) {
        this.children = children;

        if (children != null) {
            this.children.stream().forEach(c -> c.setParent(this));
        }
    }
    public boolean equals(Object other) {
        if (this == other) return true;

        return other instanceof TreeNode
               && Objects.equals(((TreeNode)other).getManagedObject(), getManagedObject())
               && Objects.equals(((TreeNode)other).getChildren(), getChildren());
    }

    public int hashCode() {
        return Objects.hash(managedObject, children);
    }

    public String toString() {
        return managedObject == null ? "<null>" : managedObject.toString();
    }

    public int getIndexOfChild(TreeNode<E> child) {
        if (children == null) return -1;

        return children.indexOf(child);
    }

    public TreeNode<E> getChild(int childIndex) {
        if ( children == null || childIndex > children.size() ) return null;
        return children.get(childIndex);
    }

    public int getNumberOfChildren() {
        return children == null ? 0 : children.size();
    }
}
