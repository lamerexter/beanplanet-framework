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
