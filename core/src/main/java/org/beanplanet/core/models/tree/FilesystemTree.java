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

import org.beanplanet.core.cache.Cache;
import org.beanplanet.core.cache.LruCachePolicy;
import org.beanplanet.core.cache.ManagedCache;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;
import static java.util.Comparator.nullsLast;

public class FilesystemTree implements Tree<File> {
    private File root;

    private final Cache<File, List<File>> parentChildren;

    private static final Comparator<File> FILENAME_CASEINSENSITIVE_COMPARATOR = nullsLast(comparing(File::getName, String.CASE_INSENSITIVE_ORDER));
    private final Comparator<File> comparator;

    public FilesystemTree(final Comparator<File> comparator, final File root) {
        this.comparator = comparator;
        this.root = root;
        this.parentChildren = new ManagedCache<>();
        this.parentChildren.addCachePolicy(new LruCachePolicy<>(10));
    }

    public FilesystemTree(File root) {
        this(FILENAME_CASEINSENSITIVE_COMPARATOR, root);
    }

    public FilesystemTree() {
        this(FILENAME_CASEINSENSITIVE_COMPARATOR, null);
    }

    @Override
    public File getRoot() {
        return root;
    }

    /**
     * Returns a subtree of this tree, rooted at the given node.
     * <p>
     * This could be an instance of <code>TreeNode</code> or any other use-defined type. This model does not place any
     * constraints on the type of nodes in the tree model.
     * </p>
     *
     * @param from the new tree model root.
     * @return the substree of this tree, from the given node as root of the subtree.
     */
    @Override
    public FilesystemTree subtree(File from) {
        return new FilesystemTree(from);
    }

    @Override
    public File getParent(File child) {
        return child == null ? null : child.getParentFile();
    }

    @Override
    public File getChild(File parent, int childIndex) {
        return getChildren(parent).get(childIndex);
    }

    @Override
    public int getNumberOfChildren(File parent) {
        return getChildren(parent).size();
    }

    @Override
    public int getIndexOfChild(File parent, File child) {
        return getChildren(parent).indexOf(child);
    }

    @Override
    public List<File> getChildren(final File parent) {
//        if (parent == null) return null;
//
//        File[] children = parent.listFiles();
//        return children == null ? emptyList() : asList(children);
        if (parent == null) return null;
        if (!parent.isDirectory()) return Collections.emptyList();

        return parentChildren.computeIfAbsent(parent, p -> {
            File[] children = parent.listFiles();
            if (children == null) return emptyList();

            final List<File> childList = asList(children);
            if (comparator != null) Collections.sort(asList(children), comparator);
            return childList;
        });
    }

    @Override
    public boolean remove(File parent, File child) {
        return false;
    }

    @Override
    public boolean set(File parent, File old, File replacement) {
        return false;
    }

    @Override
    public boolean add(File parent, File afterNode, File node) {
        return false;
    }
}
