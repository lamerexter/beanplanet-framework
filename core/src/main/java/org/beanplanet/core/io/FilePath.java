/*
 *  MIT Licence:
 *
 *  Copyright (C) 2020 Beanplanet Ltd
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

package org.beanplanet.core.io;

import org.beanplanet.core.models.path.Path;
import org.beanplanet.core.util.IterableUtil;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

public class FilePath implements Path<File> {
    private File file;

    public FilePath(String filename) {
        this.file = new File(filename);
    }

    public FilePath(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    @Override
    public boolean isAbsolute() {
        return file.isAbsolute();
    }

    @Override
    public FilePath toAbsolutePath() {
        return new FilePath(file.toPath().toAbsolutePath().toFile());
    }

    @Override
    public FilePath normalise() {
        return new FilePath(file.toPath().normalize().toFile());
    }

    @Override
    public File getRoot() {
        java.nio.file.Path root = file.toPath().getRoot();
        return root == null ? null : root.toFile();
    }

    @Override
    public List<File> getElements() {
        if (file == null) return emptyList();

        return IterableUtil.asStream(file.toPath())
                           .map(java.nio.file.Path::toFile)
                           .collect(Collectors.toList());
    }

    @Override
    public org.beanplanet.core.models.path.Path<File> parentPath() {
        return file == null || file.getParentFile() == null ? null : new FilePath(file.getParentFile());
    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object other) {
        if (this == other) return true;
        if ( !(other instanceof FilePath) ) return false;

        FilePath that = (FilePath)other;
        return Objects.equals(this.getFile(), that.getFile());
    }

    public int hashCode() {
        return Objects.hash(getFile());
    }

    @Override
    public String toString() {
        return file == null ? "" : file.toString();
    }

    @Override
    public Path<File> relativeTo(Path<File> other) {
        FilePath otherFilePath = (other instanceof  FilePath) ? (FilePath)other : new FilePath(other.toString());
        return new FilePath(getFile().toPath().relativize(otherFilePath.getFile().toPath()).toFile());
    }

    @Override
    public Path<File> resolve(Path<File> other) {
        FilePath otherFilePath = (other instanceof  FilePath) ? (FilePath)other : new FilePath(other.toString());
        return new FilePath(getFile().toPath().resolve(otherFilePath.getFile().toPath()).toFile());
    }

    @Override
    public Path<File> join(Path<File> other) {
        if (other == null || other.isEmpty()) return this;
        return new FilePath(getFile().toPath().resolve(other.getElement().toPath()).toFile());
    }

    @Override
    public URI toUri() {
        return file == null ? null : file.toURI();
    }
}
