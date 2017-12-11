/*
 * Copyright (C) 2017 Beanplanet Ltd
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.beanplanet.core.util;

import org.beanplanet.core.lang.Assert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A static utility class containing string-based operations.
 */
public class StringUtil {
    /**
     * Returns a string representation of the elements of an array, delimited by a given string delimiter.
     *
     * @param arr the array whose elements are to be stringified, separated by the given delimiter. May be null.
     * @param delimiter the delimiter to place between stringified eliements of the given array. May not be null.
     * @return a string consisting of each element <code>toString()</code>, separated by the given delimiter, or the empty string if the array was null.
     */
    public static final String arrayToDelimitedString(Object[] arr, CharSequence delimiter) {
        Assert.notNull(delimiter, "The string delimiter may not be null");
        if (arr == null) return null;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            sb.append(i > 0 ? delimiter : "").append(arr[i]);
        }
        return sb.toString();
    }

    /**
     * Replaces all occurrences of an old regular expression string with a new string for a given input string.
     *
     * @param inString to string in which the replacements are to occur.
     * @param regexPattern the string to be replaced.
     * @param newStr the replacement string.
     * @return a string with all occurrences of <code>regexPattern</code> replaced with <code>newPattern</code>.
     */
    public static String replaceRegex(String inString, String regexPattern, String newStr) {
        Pattern p = Pattern.compile(regexPattern);
        Matcher m = p.matcher(inString);

        return m.replaceAll(newStr);
    }

    /**
     * Truncate this String, with no concern about breaking words. For example, truncating the String
     * "The quick brown fox jumped over the lazy dog" to a maximum length of 10 characters with a trailer of "..." should
     * return "The qui..."
     *
     * @param s String to return a truncated version of
     * @param charLimit maximum number of characters (excluding trailer) in the returned string
     * @param trailer trailer to append to the end of the truncated String. May be empty, but "..." is a common value.
     * @return a truncated version of the String, concluded with the trailer if truncation was required.
     */
    public static String truncate(String s, int charLimit, String trailer) {
        if (s == null) {
            return "";
        }
        if (trailer == null) {
            trailer = "";
        }
        if (s.length() <= charLimit) {
            return s;
        }
        String ret = s.substring(0, charLimit) + trailer;
        return ret;
    }

    /**
     * Obtains the platform default character set encoding.
     *
     * @return the character set encoding configured in the JVM.
     */
    public static String getDefaultCharacterEncoding() {
        // Not available on all platforms
        String charEnc = System.getProperty("file.encoding");
        if (charEnc != null) {
            return charEnc;
        }

        // JDK1.4 onwards
        charEnc = new java.io.OutputStreamWriter(new java.io.ByteArrayOutputStream()).getEncoding();

        // jdk1.5
        // charEnc = Charset.defaultCharset().name();
        return charEnc != null ? charEnc : "<unknown charset encoding>";
    }
}
