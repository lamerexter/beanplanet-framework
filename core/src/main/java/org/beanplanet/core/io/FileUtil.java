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

package org.beanplanet.core.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

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
     * Returns the suffix name of a filename specification. The suffix name of a file is defined to be the characters
     * from the last dot (".") to the end of the filename specified;
     * <p>
     * For example, given a file "MyDoc.doc" this method will return "doc".
     *
     * @param fileSpec a file path specification, which may be null.
     * @return the file suffix, which may be empty and will not include the leading dot, or null if the provided file
     *         specification was null.
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
            return "";
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
}
