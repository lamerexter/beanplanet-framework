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

import org.beanplanet.core.lang.proxy.DefaultProxyFactory;
import org.beanplanet.core.lang.proxy.MethodCallContext;
import org.beanplanet.core.lang.proxy.MethodCallInterceptor;
import org.beanplanet.core.lang.proxy.Proxy;

import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class FilesystemTree implements Tree<File> {
    private File root;

    public FilesystemTree(File root) {
        this.root = root;
    }

    @Override
    public File getRoot() {
        return root;
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
    public List<File> getChildren(File parent) {
        if (parent == null) return null;

        File[] children = parent.listFiles();
        return children == null ? emptyList() : asList(children);
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

    public static void main(String ... args) {
        final Tree<File> fs = new FilesystemTree(new File("D:\\Apps"));

        Tree<File> cachingTree = DefaultProxyFactory.getInstance().createProxy(Tree.class, new MethodCallInterceptor() {
            Map<List<Object>, Object> cache = new HashMap<>();
            @Override
            public Object interceptMethodCall(MethodCallContext context) throws Throwable {
                if (context.getMethod().getName().equals("postorderIterator")) {
                    return new PostorderIterator<>((Tree<File>)context.getTarget());
                }
                List<Object> cacheKey = new ArrayList();
                cacheKey.add(context.getMethod());
                if (context.getParameters() != null) {
                    cacheKey.addAll(Arrays.asList(context.getParameters()));
                }

                Object cachedResult = cache.get(cacheKey);
                if (cachedResult != null) {
                    return cachedResult;
                }

                Object result = context.invokeOnTarget(fs);
                cache.put(cacheKey, result);
                return result;
            }
        });

        long result = cachingTree.postorderIterator().stream().filter(File::isFile).mapToLong(File::length).sum();
        System.out.println("Result : "+(result));
    }
}
