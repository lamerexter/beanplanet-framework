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

import org.beanplanet.core.models.tree.FilesystemTree;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.UUID;
import java.util.stream.Stream;

import static org.beanplanet.core.util.StringUtil.isNotBlank;

public class FileUtil {
    public static Stream<File> listFiles(File from) {
        try {
            return Files.walk(from.toPath()).map(p -> p.toFile());
        } catch (IOException e) {
            throw new IoException(String.format("Unable to walk file tree [%s]: ", from), e);
        }
    }

    /**   /**
     * Returns the currently configured temporary files directory for the JVM.
     *
     * @return the system temporary directory configured by the JVM.
     * @exception IoException if a problem occurs obtaining the directory
     */
    public static final File getTemporaryFilesDirectory() throws IoException {
        String systemTempDirStr = System.getProperty("java.io.tmpdir");
        if (systemTempDirStr == null) {
            systemTempDirStr = System.getProperty("user.home");
            if (systemTempDirStr == null) {
                throw new IoException("Unable to determine system temporary files directory.");
            }
        }

        return new File(systemTempDirStr);
    }

    /**
     * Returns the directory name of a file.
     * <p>
     * For example, given a file "/usr/local/bin/grep" this method will return "/usr/local/bin".
     *
     * @param file the file whose path or directory name is to be returned.
     * @return the directory or path part of the name of the file specified, with <u>no</u> trailing file path seperator.
     */
    public static final String getDirectoryName(File file) {
        File parentDir = new File(file.getAbsolutePath()).getParentFile();
        String path = (parentDir != null ? parentDir.getAbsolutePath() : file.getAbsolutePath());

        return path;
    }

    /**
     * Returns the basename of a file path specification, using the system default path separator.
     *
     * @param file a file path specification
     * @return the base element of the path specification.
     */
    public static final String getBaseName(File file) {
        return getBaseName(file.getPath());
    }

    /**
     * Returns the basename of a file path specification, using the system default path separator.
     *
     * @param fileSpec a file path specification
     * @return the base element of the path specification, or null if the file path specification was null
     */
    public static final String getBaseName(String fileSpec) {
        return getBaseName(fileSpec, File.separator);
    }

    /**
     * Returns the basename of a file path specification, given the path element separator. On MS Windos
     * the path separator is "/" and on Unix-based systems it is "/".
     *
     * @param fileSpec a file path specification
     * @return the base element of the path specification, or null if the file path specification was null
     */
    public static final String getBaseName(String fileSpec, String separator) {
        if (fileSpec == null) {
            return null;
        }
        int lastPartIndex = fileSpec.lastIndexOf(separator);

        if (lastPartIndex >= 0) {
            fileSpec = fileSpec.substring(lastPartIndex + 1);
        }

        return fileSpec;
    }

    /**
     * Returns the prefix name of a filename specification. The prefix name of a file is defined to be the characters up
     * to, but not including, any ending dot ('.') suffix.
     * <p>
     * For example, given a file "MyDoc.doc" this method will return "MyDoc".
     *
     * @param fileSpec a file path specification
     * @return the file prefix, which may be empty and not including any trailing dot, or null if the provided file
     *         specification was null.
     */
    public static final String getFilenamePrefix(String fileSpec) {
        if (fileSpec == null) {
            return null;
        }

        fileSpec = getBaseName(fileSpec);
        int suffixIndex = fileSpec.lastIndexOf('.');

        if (suffixIndex >= 0) {
            fileSpec = fileSpec.substring(0, suffixIndex);
        }

        return fileSpec;
    }

    /**
     * Returns the prefix name of a file. The prefix name of a file is defined to be the characters up to, but not
     * including, any ending dot ('.') suffix.
     * <p>
     * For example, given a file "MyDoc.doc" this method will return "MyDoc".
     *
     * @param file a file path specification
     * @return the file prefix, which may be empty and not including any trailing dot, or null if the provided file was
     *         null.
     */
    public static final String getFilenamePrefix(File file) {
        if (file == null) {
            return null;
        }
        return getFilenamePrefix(file.getName());
    }

    /**
     * Determines whether a given filename has a filename extension/suffix.

     * @param fileSpec a file path specification, which may be null.
     * @return the file suffix, not including any leading dot, or null if the provided file
     *         specification was null or does not have a file extension.
     */
    public static boolean hasFilenameExtension(String fileSpec) {
        return isNotBlank(getFilenameSuffix(fileSpec));
    }

    /**
     * Returns the suffix name of a filename specification. The suffix name of a file is defined to be the characters
     * from the last dot (".") to the end of the filename specified;
     * <p>
     * For example, given a file "MyDoc.doc" this method will return "doc".
     *
     * @param fileSpec a file path specification, which may be null.
     * @return the file suffix, not including any leading dot, or null if the provided file
     *         specification was null or does not have a file extension.
     */
    public static final String getFilenameSuffix(String fileSpec) {
        if (fileSpec == null) {
            return null;
        }

        int suffixIndex = fileSpec.lastIndexOf('.');

        if (suffixIndex >= 0) {
            return fileSpec.substring(suffixIndex + 1);
        }
        else {
            return null;
        }
    }

    /**
     * Returns the suffix name of a filename specification. The suffix name of a file is defined to be the characters
     * from the last dot (".") to the end of the filename specified;
     * <p>
     * For example, given a file "MyDoc.doc" this method will return "doc".
     *
     * @param file the file whose suffix (extension) is to be determined, which may be null.
     * @return the file suffix, which may be empty and will not include the leading dot, or null if the provided file
     *         specification was null.
     */
    public static final String getFilenameSuffix(File file) {
        if (file == null) {
            return null;
        }
        return getFilenameSuffix(file.getName());
    }

    /**
     * Deletes the given files or directories quietly, ignoring any errors.
     *
     * @param files the files or directories to be deleted.
     * @return true if all the files or directories (recursively) were removed, false otherwise.
     */
    public static final boolean deleteIgnoringErrors(File ... files) {
        if (files == null) return false;

        boolean result = false, first = true;
        for (File file : files) {
            boolean thisResult = deleteIgnoringErrors(file);
            if (first) {
                result = thisResult;
                first = false;
            } else {
                result = result && thisResult;
            }
        }

        return result;
    }

    /**
     * Deletes the given file or directory quietly, ignoring any errors.
     *
     * @param file the file or directory to be deleted.
     * @return true if the file or directory (recursively) was removed, false otherwise.
     */
    public static final boolean deleteIgnoringErrors(File file) {
        if (!file.exists() ) return false;

        if (file.isFile()) {
            return file.delete();
        } else {
            Iterator<File> filesIterator = new FilesystemTree(file).postorderIterator();
            boolean result = filesIterator.next().delete();
            while (filesIterator.hasNext() ) {
                result = result && filesIterator.next().delete();
            }

            return result;
        }
    }

    /**
     * Creates a temporary file that will be deleted automatically when the VM exists or when the O/S is restarted, relative
     * to the O/S dependent temporary files directory. On *nix systems the temporary files directory is typically {@code /tmp}}.
     *
     * @return the temporary file created, which may be written to and read from.
     * @throws IoException if the temporary file could not be created.
     */
    public static final File createTemporaryFile() {
        try {
            return File.createTempFile(UUID.randomUUID().toString(), ".tmp");
        } catch (IOException e) {
            throw new IoException(e);
        }
    }

    /**
     * Creates a temporary directory, relative to the O/S dependent temporary files directory.
     *
     * <p>
     * For example, on *nix systems where the temporary files directory is typically {@code /tmp}, the temporary directory
     * created will be similar to {@code /tmp/1a2r34-4r5c6-b45a}.
     * </p>
     *
     * @return the directory created.
     * @throws IoException if the directory could not be created.
     */
    public static final File createTemporaryDirectory() {
        return createTemporaryDirectory(UUID.randomUUID().toString());
    }

    /**
     * Creates a temporary directory with the given path, relative to the O/S dependent temporary files directory.
     *
     * <p>
     * For example, on *nix systems where the temporary files directory is typically {@code /tmp}, given the path
     * {@code foo/bar}, then the temporary directory created will be {@code /tmp/foo/bar}.
     * </p>
     *
     * @param path the path beneath the system-dependent temporary files directory where the temporary directory is to
     *             be created.
     * @return the directory created.
     * @throws IoException if the directory could not be created.
     */
    public static final File createTemporaryDirectory(String path) {
        File tmpDir = new File(getTemporaryFilesDirectory(), path);
        if (tmpDir.exists() && tmpDir.isDirectory()) return tmpDir;

        boolean created = tmpDir.mkdirs();
        if ( !created ) {
            throw new IoException(String.format("The temporary directory [%s] could not be created partially or at all", tmpDir));
        }

        return tmpDir;
    }
}
