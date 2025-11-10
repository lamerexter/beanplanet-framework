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

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.Vector;

/**
 * @author Gary Watson
 * @since 17th December, 2003
 */
public class SwingTreeModelAdapter implements TreeModel {
   private Vector<TreeModelListener> listeners = new Vector<TreeModelListener>();

   private Tree<Object> model;

   public SwingTreeModelAdapter() {
      this(new TreeNodeTree<>(new TreeNodeTree<>("Hello World!")));
   }

   public SwingTreeModelAdapter(Tree<?> model) {
      setModel(model);
   }

   public Tree<?> getModel() {
      return model;
   }

   @SuppressWarnings("unchecked")
   public void setModel(Tree<?> model) {
      this.model = (Tree<Object>) model;
   }

   /**
    * Returns the root of the tree. Returns <code>null</code> only if the tree has no nodes.
    *
    * @return the root of the tree
    */
   public Object getRoot() {
      return model.getRoot();
   }

   /**
    * Returns the child of <code>parent</code> at index <code>index</code> in the parent's child array.
    * <code>parent</code> must be a node previously obtained from this data source. This should not return
    * <code>null</code> if <code>index</code> is a valid index for <code>parent</code> (that is <code>index >= 0 &&
    * index < getChildCount(parent</code>)).
    *
    * @param parent a node in the tree, obtained from this data source
    * @return the child of <code>parent</code> at index <code>index</code>
    */
   public Object getChild(Object parent, int index) {
      // Tree treeNode = (Tree)parent;
      return model.getChild(parent, index);
   }

   /**
    * Returns the number of children of <code>parent</code>. Returns 0 if the node is a leaf or if it has no children.
    * <code>parent</code> must be a node previously obtained from this data source.
    *
    * @param parent a node in the tree, obtained from this data source
    * @return the number of children of the node <code>parent</code>
    */
   public int getChildCount(Object parent) {
      // Tree treeNode = (Tree)parent;
      return model.getNumberOfChildren(parent);
   }

   /**
    * Returns <code>true</code> if <code>node</code> is a leaf. It is possible for this method to return
    * <code>false</code> even if <code>node</code> has no children. A directory in a filesystem, for example, may
    * contain no files; the node representing the directory is not a leaf, but it also has no children.
    *
    * @param node a node in the tree, obtained from this data source
    * @return true if <code>node</code> is a leaf
    */
   public boolean isLeaf(Object node) {
      // Tree treeNode = (Tree)node;
      return model.isLeaf(node);
   }

   /**
    * Messaged when the user has altered the value for the item identified by <code>path</code> to <code>newValue</code>
    * . If <code>newValue</code> signifies a truly new value the model should post a <code>treeNodesChanged</code>
    * event.
    *
    * @param path path to the node that the user has altered
    * @param newValue the new value from the TreeCellEditor
    */
   public void valueForPathChanged(TreePath path, Object newValue) {
      throw new RuntimeException("GAW TODO - not implemented yet!");
   }

   /**
    * Returns the index of child in parent. If <code>parent</code> is <code>null</code> or <code>child</code> is
    * <code>null</code>, returns -1.
    *
    * @param parent a note in the tree, obtained from this data source
    * @param child the node we are interested in
    * @return the index of the child in the parent, or -1 if either <code>child</code> or <code>parent</code> are
    *         <code>null</code>
    */
   public int getIndexOfChild(Object parent, Object child) {
      // Tree treeNode = (Tree)parent;
      return model.getIndexOfChild(parent, child);
   }

   /**
    * Adds a listener for the <code>TreeModelEvent</code> posted after the tree changes.
    *
    * @param l the listener to add
    * @see #removeTreeModelListener
    */
   public void addTreeModelListener(TreeModelListener l) {
      listeners.add(l);
   }

   /**
    * Removes a listener previously added with <code>addTreeModelListener</code>.
    *
    * @see #addTreeModelListener
    * @param l the listener to remove
    */
   public void removeTreeModelListener(TreeModelListener l) {
      listeners.remove(l);
   }
}
