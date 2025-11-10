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

package org.beanplanet.core.models.tree;

import org.beanplanet.core.lang.ShallowCopyable;
import org.beanplanet.core.models.path.NamePath;
import org.beanplanet.core.models.path.SimpleNamePath;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class TreeNode<E> implements Serializable, ShallowCopyable {
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

    public TreeNode<E> copyShallow() {
        return copyShallow(parent == null ? null : parent.copyShallow());
    }

    public TreeNode<E> copyShallow(final TreeNode<E> parent) {
        return new TreeNode<>(parent, managedObject, (children == null ? null : children.stream().map(n -> n.copyShallow(parent)).collect(Collectors.toList())));
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
        return children == null ? emptyList() : children;
    }

    public void setChildren(List<TreeNode<E>> children) {
        this.children = children;

        if (children != null) {
            this.children.stream().forEach(c -> c.setParent(this));
        }
    }
    public boolean equals(Object other) {
        if (this == other) return true;

        if ( !(other instanceof TreeNode) ) return false;
        final TreeNode<?> that = (TreeNode<?>)other;

        return Objects.equals(this.managedObject, that.managedObject)
               && Objects.equals(this.children, that.children);
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

    public NamePath parentNamePath(final Function<E, String> nameSupplier) {
        LinkedList<String> namePathElements = new LinkedList<>();
        TreeNode<E> fromNode = this;
        while (fromNode != null) {
            namePathElements.add(0, nameSupplier.apply(fromNode.getManagedObject()));
            fromNode = fromNode.getParent();
        }

        return new SimpleNamePath(namePathElements);
    }

}
