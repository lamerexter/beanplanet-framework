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

package org.beanplanet.core.io.resource;

import org.beanplanet.core.io.FileUtil;
import org.beanplanet.core.lang.TypeUtil;
import org.beanplanet.core.models.path.Path;
import org.junit.Test;

import java.io.File;

import static org.beanplanet.core.models.path.PathUtil.emptyPath;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FileResourceTest {
    @Test
    public void resolve_withEmptyPath() throws Exception {
        File tempFilesDirectory = FileUtil.createTemporaryDirectory(TypeUtil.getBaseName(getClass()));
        File tempFile = new File(tempFilesDirectory, "a/b");
        try {
            // Given an absolute resource
            FileResource fileResource = new FileResource(tempFile);
            Path<?> emptyElementsPath = mock(Path.class);
            when(emptyElementsPath.isEmpty()).thenReturn(true);

            // Then
            assertThat(fileResource.resolve(null), equalTo(fileResource));
            assertThat(fileResource.resolve(emptyPath()), equalTo(fileResource));
        } finally {
            FileUtil.deleteIgnoringErrors(tempFilesDirectory);
        }
    }

    @Test
    public void resolve_withAbsolutePath() throws Exception {
        File tempFilesDirectory = FileUtil.createTemporaryDirectory(TypeUtil.getBaseName(getClass()));
        File tempFile = new File(tempFilesDirectory, "a/b");
        File otherTempFile = new File(tempFilesDirectory, "c/d");
        try {
            // Given an absolute resource
            FileResource fileResource = new FileResource(tempFile);
            Path absolutePath = new FileResource(otherTempFile).getPath();

            // When I resolve a resource against an absolute path
            Resource resolved = fileResource.resolve(absolutePath);

            // Then a new resource to the absolute path is returned
            assertThat(resolved, is(not(sameInstance(fileResource))));
        } finally {
            FileUtil.deleteIgnoringErrors(tempFilesDirectory);
            FileUtil.deleteIgnoringErrors(otherTempFile);
        }
    }

    @Test
    public void resolve_withRelativePath() throws Exception {
        File tempFilesDirectory = FileUtil.createTemporaryDirectory(TypeUtil.getBaseName(getClass()));
        File tempFile = new File(tempFilesDirectory, "a"+File.separator+"b"+File.separator);
        File otherTempFile = new File("c"+File.separator+"d");
        try {
            // Given an absolute resource
            FileResource fileResource = new FileResource(tempFile);
            Path relativePath = new FileResource(otherTempFile).getPath();

            // When I resolve a resource against an absolute path
            Resource resolved = fileResource.resolve(relativePath);

            // Then a new resource to the absolute path is returned
            assertThat(resolved, is(instanceOf(FileResource.class)));
            assertThat(((FileResource)resolved).getFile(), equalTo(new File(tempFile, "d")));
        } finally {
            FileUtil.deleteIgnoringErrors(tempFilesDirectory);
            FileUtil.deleteIgnoringErrors(otherTempFile);
        }
    }
}