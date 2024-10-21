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

package org.beanplanet.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.beanplanet.core.Functions.toStringFunction;
import static org.beanplanet.core.Predicates.trueBiPredicate;
import static org.beanplanet.core.Predicates.truePredicate;
import static org.beanplanet.core.util.IterableUtil.asStream;

/**
 * A static utility class containing string-based operations.
 */
public class StringUtil {
    public static final String ALPHA_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public static final String NUMERIC_CHARS = "0123456789";
    public static final String ALPHNUMERIC_CHARS = ALPHA_CHARS+NUMERIC_CHARS;
    public static final String ASCII_PRINTABLE_SPECIAL_CHARS = " !\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
    public static final String ALPHNUMERIC_ASCII_PRINTABLE_SPECIA_CHARS = ALPHNUMERIC_CHARS+ASCII_PRINTABLE_SPECIAL_CHARS;

    /**
     * Converts a given string to lowercase.
     *
     * @param str the string to be converted to lowercase, which may be null.
     * @return the string converted to lowercase, or null if the string was null.
     */
    public static String toLowercase(String str) {
        return str == null ? null : str.toLowerCase();
    }

    /**
     * Converts a given string to lowercase.
     *
     * @param str the string to be converted to lowercase, which may be null.
     * @return the string converted to lowercase, or null if the string was null.
     */
    public static String toUppercase(String str) {
        return str == null ? null : str.toUpperCase();
    }

    /**
     * Returns the specified string with the first letter capitalised.
     *
     * @param str the string whose first character is to be converted to upper case
     * @return the string specified, but with th first character capitalised, or null if the string specified was null.
     */
    public static String initCap(String str) {
        return initCap(str, false);
    }

    /**
     * Returns the specified string with the first letter capitalised.
     *
     * @param str the string whose first character is to be converted to upper case
     * @param forceLowercase true if the rest of the string is to be converted to lowercase, or false to leave the rest
     *        as is
     * @return the string specified, but with th first character capitalised, or null if the string specified was null.
     */
    public static String initCap(String str, boolean forceLowercase) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return str;
        }
        if (str.length() == 1) {
            return str.toUpperCase();
        }

        return str.substring(0, 1).toUpperCase()
               + (forceLowercase ? str.substring(1).toLowerCase() : str.substring(1));
    }

    /**
     * Determines whether an object string is null or contains only whitespace.
     *
     * @param obj the object whose string value is to be determined as blank.
     * @return true if the object or object's string value is null or if it contains whitespace characters only, false otherwise.
     */
    public static boolean isBlank(final Object obj) {
        if (obj == null) return true;

        final String s = obj.toString();
        if (s == null) return true;
        for(int n = 0; n < s.length(); ++n) {
            if (!Character.isWhitespace(s.charAt(n))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Determines whether an object string is not blank: not null and does not contain only whitespace.
     *
     * @param obj the object whose string value is to be determined as not blank.
     * @return true if the object or object's string value is not null and does not contain only whitespace characters, false otherwise.
     * @see #isBlank(Object)
     */
    public static boolean isNotBlank(final Object obj) {
        return !isBlank(obj);
    }

    public static boolean isEmptyOrNull(Object obj) {
        return obj == null || obj.toString().trim().isEmpty();
    }

    public static boolean notEmpty(Object obj) {
        return !(isEmptyOrNull(obj));
    }

    public static boolean notEmptyAndNotNull(Object obj) {
        return notEmpty(obj);
    }

    /**
     * Convenience method to return the empty string. Great as a method reference to
     * {@link ObjectUtil#nvl(Object, Supplier)} expressions.
     *
     * @return the zero-length empty string.
     */
    public static final String emptyString() {
        return "";
    }

    /**
     * Repeats a character sequence a specified number of times.
     *
     * @param characterSequence the character sequence to be repeated.
     * @param times the number of times the character sequence is to be repeated or concatenated.
     * @return a string containing the given character sequence repeated the specified number of times or null
     * if the character sequence was null.
     */
    public static final String repeat(CharSequence characterSequence, int times) {
        if (characterSequence == null) {
            return null;
        }

        StringBuilder s = new StringBuilder();
        for (int n=0; n < times; n++) {
            s.append(characterSequence);
        }

        return s.toString();
    }

    /**
     * Replaces all occurrences of an old string with a new string for a given input string.
     *
     * @param inString to string in which the replacements are to occur.
     * @param oldPattern the string to be replaced.
     * @param newPattern the replacement string.
     * @return a string with all occurrences of <code>oldPattern</code> replaced with <code>newPattern</code>.
     */
    public static String replaceAll(String inString, String oldPattern, String newPattern) {
        // Pick up error conditions
        if (inString == null) {
            return null;
        }
        if (oldPattern == null || newPattern == null) {
            return inString;
        }

        StringBuilder sbuf = new StringBuilder(); // Output StringBuilder we'll
        // build up
        int pos = 0; // Our position in the old string
        int index = inString.indexOf(oldPattern); // The index of an occurrence
        // we've found, or -1
        int patLen = oldPattern.length();
        while (index >= 0) {
            sbuf.append(inString.substring(pos, index));
            sbuf.append(newPattern);
            pos = index + patLen;
            index = inString.indexOf(oldPattern, pos);
        }
        sbuf.append(inString.substring(pos)); // Remember to append any
        // characters to the right of a
        // match
        return sbuf.toString();
    }

    /**
     * Replaces all occurrences of an old regular expression string with a new string for a given input string.
     *
     * @param inString to string in which the replacements are to occur.
     * @param regexPattern the string to be replaced.
     * @param newStr the replacement string.
     * @return a string with all occurrences of <code>regexPattern</code> replaced with <code>newPattern</code>.
     */
    public static String replaceAllRegex(String inString, String regexPattern, String newStr) {
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

    /**
     * Removed all leading and trailing occurrences of <code>occurrence</code> from the specified string,
     * <code>str</code>.
     *
     * @param str the string whose leading and trailing occurrences are to be removed.
     * @param occurrence the string to be removed.
     * @return a string with all occurrences of <code>occurrence</code> removed from the start and end of the string. If
     *         <code>str</code> is <code>null</code> then <code>null</code> is returned. If <code>occurrence</code> is
     *         <code>null</code> or zero length then <code>str</code> is returned.
     */
    public static String trim(String str, String occurrence) {
        if (str == null) {
            return null;
        }
        if (occurrence == null || occurrence.length() == 0) {
            return str;
        }

        str = lTrim(str, occurrence);
        str = rTrim(str, occurrence);

        return str;
    }

    /**
     * Removed all leading occurrences of <code>occurrence</code> from the specified string, <code>str</code>. The
     * trimming of characters performed by this implementation is <u>case-sensitive</u>.
     *
     * @param str the string whose leading occurrences are to be removed.
     * @param occurrence the string to be removed.
     * @return a string with all occurrences of <code>occurrence</code> removed from the start of the string. If
     *         <code>str</code> is <code>null</code> then <code>null</code> is returned. If <code>occurrence</code> is
     *         <code>null</code> or zero length then <code>str</code> is returned.
     * @see #lTrim(String, String, boolean)
     */
    public static String lTrim(String str, String occurrence) {
        return lTrim(str, occurrence, true);
    }

    /**
     * Removed all leading occurrences of <code>occurrence</code> from the specified string, <code>str</code>.
     *
     * @param str the string whose leading occurrences are to be removed.
     * @param occurrence the string to be removed.
     * @param caseSensitive whether the character comparison in the trim operation is to be case sensitive,
     * @return a string with all occurrences of <code>occurrence</code> removed from the start of the string. If
     *         <code>str</code> is <code>null</code> then <code>null</code> is returned. If <code>occurrence</code> is
     *         <code>null</code> or zero length then <code>str</code> is returned.
     */
    public static String lTrim(String str, String occurrence, boolean caseSensitive) {
        if (str == null) {
            return null;
        }
        if (occurrence == null || occurrence.length() == 0) {
            return str;
        }

        while (occurrence.length() <= str.length()
               && (caseSensitive ? str.substring(0, occurrence.length()).equals(occurrence) : str.substring(0,
                                                                                                            occurrence.length()).equalsIgnoreCase(occurrence))) {
            str = str.substring(occurrence.length());
        }

        return str;
    }

    /**
     * Removed all trailing occurrences of <code>occurrence</code> from the specified string, <code>str</code>. The
     * trimming of characters performed by this implementation is <u>case-sensitive</u>.
     *
     * @param str the string whose trailing occurrences are to be removed.
     * @param occurrence the string to be removed.
     * @return a string with all occurrences of <code>occurrence</code> removed from the end of the string. If
     *         <code>str</code> is <code>null</code> then <code>null</code> is returned. If <code>occurrence</code> is
     *         <code>null</code> or zero length then <code>str</code> is returned.
     * @see #rTrim(String, String, boolean)
     */
    public static String rTrim(String str, String occurrence) {
        return rTrim(str, occurrence, true);
    }

    /**
     * Removed all trailing occurrences of <code>occurrence</code> from the specified string, <code>str</code>.
     *
     * @param str           the string whose trailing occurrences are to be removed.
     * @param occurrence    the string to be removed.
     * @param caseSensitive whether the character comparison in the trim operation is to be case sensitive,
     * @return a string with all occurrences of <code>occurrence</code> removed from the end of the string. If
     * <code>str</code> is <code>null</code> then <code>null</code> is returned. If <code>occurrence</code> is
     * <code>null</code> or zero length then <code>str</code> is returned.
     */
    public static String rTrim(String str, String occurrence, boolean caseSensitive) {
        if (str == null) {
            return null;
        }
        if (occurrence == null || occurrence.length() == 0) {
            return str;
        }

        for (int lengthDiff = str.length() - occurrence.length(); lengthDiff >= 0
                                                                  && (caseSensitive ? str.substring(lengthDiff,
                                                                                                    lengthDiff + occurrence.length()
        ).equals(
                occurrence) : str.substring(lengthDiff,
                                            lengthDiff + occurrence.length()
        )
                                                                              .equalsIgnoreCase(occurrence)); lengthDiff = str
                                                                                                                                   .length()
                                                                                                                           - occurrence
                                                                                                                                   .length()) {
            str = str.substring(0, lengthDiff);
        }

        return str;
    }

    /**
     * Returns a string consisting of the elements of an collection delimited by comma (,).  Before being included,
     * elements are optionally filtered by a given predicate.  Once the decision to include an element has been made, the
     * element may be optionally transformed by a given transformation function.
     *
     * @param collection the collection of objects to be delimited, which may be null.
     * @return null if the given collection is null, otherwise comma-separated-vales (CSV) string of the collection elements.
     */
    public static <T> String asCsvString(Collection<T> collection) {
        if (collection == null) return null;

        return asDelimitedString(collection.stream(), ",");
    }

    /**
     * Returns a string consisting of the elements of a stream delimited with a given delimiter.  Before being included,
     * elements are optionally filtered by a given predicate.  Once the decision to include an element has been made, the
     * element may be optionally transformed by a given transformation function.
     *
     * @param stream the stream of objects to be delimited, which may be null.
     * @param filter a predicate to determine whether an element should be included, which may be null.
     * @param delimiter the delimiter to apply between adjacent elements from the stream.
     * @param transformer a transformation function to apply to an stream element before its inclusion in the delimited
     *                    string, which may be null.
     * @return null if the given stream is null, otherwise a string of the stream elements, filtered and transformed.
     */
    public static <T> String asDelimitedString(Stream<T> stream, Predicate<T> filter, String delimiter, Function<T, String> transformer) {
        if (stream == null) return null;

        return stream
                .filter(filter != null ? filter : truePredicate())
                .map(transformer != null ? transformer : toStringFunction())
                .collect(Collectors.joining(delimiter));
    }

    /**
     * Returns a string consisting of the elements of a stream delimited with a given delimiter.  Before being included,
     * elements are optionally filtered by a given predicate.  Once the decision to include an element has been made, the
     * element may be optionally transformed by a given transformation function.
     *
     * @param stream the stream of objects to be delimited, which may be null.
     * @param filter a predicate to determine whether an element should be included, which may be null.
     * @param delimiter the delimiter to apply between adjacent elements from the stream.
     * @return null if the given stream is null, otherwise a string of the stream elements, filtered and transformed.
     */
    public static <T> String asDelimitedString(Stream<T> stream, Predicate<T> filter, String delimiter) {
        return asDelimitedString(stream, filter, delimiter, null);
    }

    /**
     * Returns a string consisting of the elements of a stream delimited with a given delimiter.  Before being included,
     * elements are optionally filtered by a given predicate.  Once the decision to include an element has been made, the
     * element may be optionally transformed by a given transformation function.
     *
     * @param stream the stream of objects to be delimited, which may be null.
     * @param delimiter the delimiter to apply between adjacent elements from the stream.
     * @param transformer a transformation function to apply to an stream element before its inclusion in the delimited
     *                    string, which may be null.
     * @return null if the given stream is null, otherwise a string of the stream elements, filtered and transformed.
     */
    public static <T> String asDelimitedString(Stream<T> stream, String delimiter, Function<T, String> transformer) {
        return asDelimitedString(stream, null, delimiter, transformer);
    }

    /**
     * Returns a string consisting of the elements of a stream delimited with a given delimiter.  Before being included,
     * elements are optionally filtered by a given predicate.  Once the decision to include an element has been made, the
     * element may be optionally transformed by a given transformation function.
     *
     * @param stream the stream of objects to be delimited, which may be null.
     * @param delimiter the delimiter to apply between adjacent elements from the stream.
     * @return null if the given stream is null, otherwise a string of the stream elements, filtered and transformed.
     */
    public static <T> String asDelimitedString(Stream<T> stream, String delimiter) {
        return asDelimitedString(stream, null, delimiter, null);
    }

    /**
     * Returns a string consisting of the elements of an array delimited with a given delimiter.  Before being included,
     * elements are optionally filtered by a given predicate.  Once the decision to include an element has been made, the
     * element may be optionally transformed by a given transformation function.
     *
     * @param arr the array of objects to be delimited, which may be null.
     * @param filter a predicate to determine whether an element should be included, which may be null.
     * @param delimiter the delimiter to apply between adjacent elements from the array.
     * @param transformer a transformation function to apply to an array element before its inclusion in the delimited
     *                    string, which may be null.
     * @return null if the given array is null, otherwise a string of the array elements, filtered and transformed.
     */
    public static <T> String asDelimitedString(T arr[], Predicate<T> filter, String delimiter, Function<T, String> transformer) {
        if (arr == null) return null;

        return asDelimitedString(Arrays.stream(arr), filter, delimiter, transformer);
    }

    /**
     * Returns a string consisting of the elements of an array delimited with a given delimiter.  Before being included,
     * elements are optionally filtered by a given predicate.  Once the decision to include an element has been made, the
     * element may be optionally transformed by a given transformation function.
     *
     * @param arr the array of objects to be delimited, which may be null.
     * @param filter a predicate to determine whether an element should be included, which may be null.
     * @param delimiter the delimiter to apply between adjacent elements from the array.
     * @return null if the given array is null, otherwise a string of the array elements, filtered and transformed.
     */
    public static <T> String asDelimitedString(T arr[], Predicate<T> filter, String delimiter) {
        if (arr == null) return null;

        return asDelimitedString(Arrays.stream(arr), filter, delimiter);
    }

    /**
     * Returns a string consisting of the elements of an array delimited with a given delimiter.  Before being included,
     * elements are optionally filtered by a given predicate.  Once the decision to include an element has been made, the
     * element may be optionally transformed by a given transformation function.
     *
     * @param arr the array of objects to be delimited, which may be null.
     * @param delimiter the delimiter to apply between adjacent elements from the array.
     * @param transformer a transformation function to apply to an array element before its inclusion in the delimited
     *                    string, which may be null.
     * @return null if the given array is null, otherwise a string of the array elements, filtered and transformed.
     */
    public static <T> String asDelimitedString(T arr[], String delimiter, Function<T, String> transformer) {
        if (arr == null) return null;

        return asDelimitedString(Arrays.stream(arr), delimiter, transformer);
    }

    /**
     * Returns a string consisting of the elements of an array delimited with a given delimiter.  Before being included,
     * elements are optionally filtered by a given predicate.  Once the decision to include an element has been made, the
     * element may be optionally transformed by a given transformation function.
     *
     * @param arr the array of objects to be delimited, which may be null.
     * @param delimiter the delimiter to apply between adjacent elements from the array.
     * @return null if the given array is null, otherwise a string of the array elements, filtered and transformed.
     */
    public static <T> String asDelimitedString(T arr[], String delimiter) {
        if (arr == null) return null;

        return asDelimitedString(Arrays.stream(arr), delimiter);
    }

    /**
     * Returns a string consisting of the elements of an collection delimited with a given delimiter.  Before being included,
     * elements are optionally filtered by a given predicate.  Once the decision to include an element has been made, the
     * element may be optionally transformed by a given transformation function.
     *
     * @param collection the collection of objects to be delimited, which may be null.
     * @param filter a predicate to determine whether an element should be included, which may be null.
     * @param delimiter the delimiter to apply between adjacent elements from the collection.
     * @param transformer a transformation function to apply to a collection element before its inclusion in the delimited
     *                    string, which may be null.
     * @return null if the given collection is null, otherwise a string of the collection elements, filtered and transformed.
     */
    public static <T> String asDelimitedString(Collection<T> collection, Predicate<T> filter, String delimiter, Function<T, String> transformer) {
        if (collection == null) return null;

        return asDelimitedString(collection.stream(), filter, delimiter, transformer);
    }

    /**
     * Returns a string consisting of the elements of an collection delimited with a given delimiter.  Before being included,
     * elements are optionally filtered by a given predicate.  Once the decision to include an element has been made, the
     * element may be optionally transformed by a given transformation function.
     *
     * @param collection the collection of objects to be delimited, which may be null.
     * @param filter a predicate to determine whether an element should be included, which may be null.
     * @param delimiter the delimiter to apply between adjacent elements from the collection.
     * @return null if the given collection is null, otherwise a string of the collection elements, filtered and transformed.
     */
    public static <T> String asDelimitedString(Collection<T> collection, Predicate<T> filter, String delimiter) {
        if (collection == null) return null;

        return asDelimitedString(collection.stream(), filter, delimiter);
    }

    /**
     * Returns a string consisting of the elements of an collection delimited with a given delimiter.  Before being included,
     * elements are optionally filtered by a given predicate.  Once the decision to include an element has been made, the
     * element may be optionally transformed by a given transformation function.
     *
     * @param collection the collection of objects to be delimited, which may be null.
     * @param delimiter the delimiter to apply between adjacent elements from the collection.
     * @param transformer a transformation function to apply to a collection element before its inclusion in the delimited
     *                    string, which may be null.
     * @return null if the given collection is null, otherwise a string of the collection elements, filtered and transformed.
     */
    public static <T> String asDelimitedString(Collection<T> collection, String delimiter, Function<T, String> transformer) {
        if (collection == null) return null;

        return asDelimitedString(collection.stream(), delimiter, transformer);
    }

    /**
     * Returns a string consisting of the elements of an collection delimited with a given delimiter.  Before being included,
     * elements are optionally filtered by a given predicate.  Once the decision to include an element has been made, the
     * element may be optionally transformed by a given transformation function.
     *
     * @param collection the collection of objects to be delimited, which may be null.
     * @param delimiter the delimiter to apply between adjacent elements from the collection.
     * @return null if the given collection is null, otherwise a string of the collection elements, filtered and transformed.
     */
    public static <T> String asDelimitedString(Collection<T> collection, String delimiter) {
        if (collection == null) return null;

        return asDelimitedString(collection.stream(), delimiter);
    }

    /**
     * Returns a string consisting of the elements of a {@link Streamable} delimited with a given delimiter.
     *
     * @param streamable the streamable of objects to be delimited, which may be null.
     * @param delimiter the delimiter to apply between adjacent elements from the stream.
     * @return null if the given stream is null, otherwise a string of the stream elements, filtered and transformed.
     */
    public static <T> String asDelimitedString(Streamable<T> streamable, String delimiter) {
        if (streamable == null) return null;

        return asDelimitedString(streamable.stream(), delimiter);
    }

    /**
     * Returns a string consisting of the elements of a {@link Streamable} delimited with a given delimiter.  Before being included,
     * elements are optionally filtered by a given predicate.  Once the decision to include an element has been made, the
     * element may be optionally transformed by a given transformation function.
     *
     * @param streamable the iterable of objects to be delimited, which may be null.
     * @param filter a predicate to determine whether an element should be included, which may be null.
     * @param delimiter the delimiter to apply between adjacent elements from the stream.
     * @param transformer a transformation function to apply to an stream element before its inclusion in the delimited
     *                    string, which may be null.
     * @return null if the given stream is null, otherwise a string of the stream elements, filtered and transformed.
     */
    public static <T> String asDelimitedString(Streamable<T> streamable, Predicate<T> filter, String delimiter, Function<T, String> transformer) {
        if (streamable == null) return null;

        return asDelimitedString(streamable.stream(), filter, delimiter, transformer);
    }

    /**
     * Returns a string consisting of the elements of an iterable delimited with a given delimiter.
     *
     * @param iterable the iterable of objects to be delimited, which may be null.
     * @param delimiter the delimiter to apply between adjacent elements from the stream.
     * @return null if the given stream is null, otherwise a string of the stream elements, filtered and transformed.
     */
    public static <T> String asDelimitedString(Iterable<T> iterable, String delimiter) {
        if (iterable == null) return null;

        return asDelimitedString(asStream(iterable), delimiter);
    }

    /**
     * Returns a string consisting of the elements of an iterable delimited with a given delimiter.  Before being included,
     * elements are optionally filtered by a given predicate.  Once the decision to include an element has been made, the
     * element may be optionally transformed by a given transformation function.
     *
     * @param iterable the iterable of objects to be delimited, which may be null.
     * @param filter a predicate to determine whether an element should be included, which may be null.
     * @param delimiter the delimiter to apply between adjacent elements from the stream.
     * @param transformer a transformation function to apply to an stream element before its inclusion in the delimited
     *                    string, which may be null.
     * @return null if the given stream is null, otherwise a string of the stream elements, filtered and transformed.
     */
    public static <T> String asDelimitedString(Iterable<T> iterable, Predicate<T> filter, String delimiter, Function<T, String> transformer) {
        if (iterable == null) return null;

        return asDelimitedString(asStream(iterable), filter, delimiter, transformer);
    }

    /**
     * Returns a string consisting of the entries of a map, with keys and values delimited by a key-value
     * delimiter and with entries delimited by an entry delimiter.  Before being included,
     * entries are optionally filtered by a given key-value predicate.  Once the decision to include an entry has been made, the
     * key and value may be optionally transformed by the given key and value transformation functions.
     *
     * @param map the map of objects to be delimited, which may be null.
     * @param filter a predicate to determine whether an entry (key and value) should be included, which may be null.
     * @param entryDelimiter the delimiter to apply between entries from the map.
     * @param kvDelimiter the delimiter to apply between entry key and values from the map.
     * @param keyTransformer a transformation function to apply to a key before its inclusion in the delimited string, which may be null.
     * @param valueTransformer a transformation function to apply to a value before its inclusion in the delimited string, which may be null.
     * @return null if the given map is null, otherwise a string of the collection elements, filtered and transformed.
     */
    public static <K, V> String asDelimitedString(Map<K, V> map,
                                                  BiPredicate<K, V> filter,
                                                  String entryDelimiter,
                                                  String kvDelimiter,
                                                  Function<K, String> keyTransformer,
                                                  Function<V, String> valueTransformer) {
        if (map == null) return null;

        return asDelimitedString(map.entrySet().stream(),
                                 e -> Optional.ofNullable(filter).orElse(trueBiPredicate()).test(e.getKey(), e.getValue()),
                                 entryDelimiter,
                                 e -> Optional.ofNullable(keyTransformer).orElse(toStringFunction()).apply(e.getKey())+
                                      kvDelimiter+
                                      Optional.ofNullable(valueTransformer).orElse(toStringFunction()).apply(e.getValue())
        );
    }

    /**
     * Returns a string consisting of the entries of a map, with keys and values delimited by a key-value
     * delimiter and with entries delimited by an entry delimiter.  Before being included,
     * entries are optionally filtered by a given key-value predicate.
     *
     * @param map the map of objects to be delimited, which may be null.
     * @param entryDelimiter the delimiter to apply between entries from the map.
     * @param kvDelimiter the delimiter to apply between entry key and values from the map.
     * @return null if the given map is null, otherwise a string of the collection elements, filtered and transformed.
     */
    public static <K, V> String asDelimitedString(Map<K, V> map,
                                                  BiPredicate<K, V> filter,
                                                  String entryDelimiter,
                                                  String kvDelimiter) {
        return asDelimitedString(map, filter, entryDelimiter, kvDelimiter, null, null);
    }

    /**
     * Returns a string consisting of the entries of a map, with keys and values delimited by a key-value
     * delimiter and with entries delimited by an entry delimiter. Keys and values may be optionally transformed by the
     * given key and value transformation functions.
     *
     * @param map the map of objects to be delimited, which may be null.
     * @param entryDelimiter the delimiter to apply between entries from the map.
     * @param kvDelimiter the delimiter to apply between entry key and values from the map.
     * @param keyTransformer a transformation function to apply to a key before its inclusion in the delimited string, which may be null.
     * @param valueTransformer a transformation function to apply to a value before its inclusion in the delimited string, which may be null.
     * @return null if the given map is null, otherwise a string of the collection elements, filtered and transformed.
     */
    public static <K, V> String asDelimitedString(Map<K, V> map,
                                                  String entryDelimiter,
                                                  String kvDelimiter,
                                                  Function<K, String> keyTransformer,
                                                  Function<V, String> valueTransformer) {
        return asDelimitedString(map, null, entryDelimiter, kvDelimiter, keyTransformer, valueTransformer);
    }

    /**
     * Returns a string consisting of the entries of a map, with keys and values delimited by a key-value
     * delimiter and with entries delimited by an entry delimiter.
     *
     * @param map the map of objects to be delimited, which may be null.
     * @param entryDelimiter the delimiter to apply between entries from the map.
     * @param kvDelimiter the delimiter to apply between entry key and values from the map.
     * @return null if the given map is null, otherwise a string of the collection elements, filtered and transformed.
     */
    public static <K, V> String asDelimitedString(Map<K, V> map,
                                                  String entryDelimiter,
                                                  String kvDelimiter) {
        return asDelimitedString(map, null, entryDelimiter, kvDelimiter, null, null);
    }

    /**
     * Returns a string consisting of the elements of an collection, converted to strings using toString(), delimited with a given delimiter. Only
     * strings which are not empty (consisting only of whitespace) are added.
     *
     * @param delimiter the delimiter to apply between adjacent elements from the collection.
     * @param collection an array of objects whose element non-null and non-empty string values to be delimited, which may be null.
     * @return null if the given collection is null, otherwise a string of the collection's non-null and non-empty string element value, delimited by the given delimiter.
     */
    public static <T> String asDelimitedStringOfNotEmpty(final String delimiter, Object ... collection) {
        if (collection == null) return null;

        return asDelimitedString(Arrays.stream(collection), e -> e != null && !isEmptyOrNull(e.toString()), delimiter, null);
    }

    /**
     * Ensures the specified string, <code>str</code>, has the given prefix, <code>prefix</code>. The check for the
     * string prefix is case-sensitive.
     *
     * @param str the string to have the prefix, which may be null.
     * @param prefix the prefix to apply to the string, if not already present, which may be null.
     * @return the specified string, guaranteed to have the prefix if it doesn't already or <code>null</code> if the
     *         string is <code>null</code>.
     * @see #ensureHasPrefix(String, String, boolean)
     */
    public static String ensureHasPrefix(String str, String prefix) {
        return ensureHasPrefix(str, prefix, false);
    }

    /**
     * Ensures the specified string, <code>str</code>, has the given prefix, <code>prefix</code>.
     *
     * @param str the string to have the prefix, which may be null.
     * @param prefix the prefix to apply to the string, if not already present, which may be null.
     * @param caseSensitive <code>false</code> if the check for the string prefix is to be case-sensitive.
     * @return the specified string, guaranteed to have the prefix if it doesn't already or <code>null</code> if the
     *         string is <code>null</code>.
     */
    public static String ensureHasPrefix(String str, String prefix, boolean caseSensitive) {
        if (str == null) {
            return null;
        }
        if (prefix == null) {
            return str;
        }

        if (caseSensitive ? str.toLowerCase().startsWith(prefix.toLowerCase()) : str.startsWith(prefix)) {
            return str;
        }
        else {
            return prefix + str;
        }
    }

    /**
     * Ensures the specified string, <code>str</code>, has the given suffix, <code>suffix</code>. The check for the
     * string suffix is case-sensitive.
     *
     * @param str the string to have the suffix, which may be null.
     * @param suffix the suffix to apply to the string, if not already present, which may be null.
     * @return the specified string, guaranteed to have the suffix if it doesn't already or <code>null</code> if the
     *         string is <code>null</code>.
     * @see #ensureHasSuffix(String, String, boolean)
     */
    public static String ensureHasSuffix(String str, String suffix) {
        return ensureHasSuffix(str, suffix, false);
    }

    /**
     * Ensures the specified string, <code>str</code>, has the given suffix, <code>suffix</code>.
     *
     * @param str the string to have the suffix, which may be null.
     * @param suffix the suffix to apply to the string, if not already present, which may be null.
     * @param caseSensitive <code>false</code> if the check for the string prefix is to be case-sensitive.
     * @return the specified string, guaranteed to have the prefix if it doesn't already or <code>null</code> if the
     *         string is <code>null</code>.
     */
    public static String ensureHasSuffix(String str, String suffix, boolean caseSensitive) {
        if (str == null) {
            return null;
        }
        if (suffix == null) {
            return str;
        }

        if (caseSensitive ? str.toLowerCase().endsWith(suffix.toLowerCase()) : str.endsWith(suffix)) {
            return str;
        }
        else {
            return str + suffix;
        }
    }

    public static Stream asDsvStream(String str, String delim, boolean trimWhitespace) {
        if (str == null)
            return null;

        if (delim == null || delim.length() == 0) {
            throw new IllegalArgumentException("Cannot split a string using a null or empty delimiter [str=" + str + ", delimeter=" + delim + "]");
        }

        int delimPos = str.indexOf(delim);
        if (delimPos < 0) {
            return trimWhitespace && isBlank(str) ? Stream.empty() : Stream.of(str);
        }

        LinkedList<String> splitParts = new LinkedList<>();

        while (delimPos >= 0) {
            if (delimPos == 0) {
                splitParts.add("");
            } else {
                splitParts.add(trimWhitespace ? str.substring(0, delimPos).trim() : str.substring(0, delimPos));
            }
            str = str.substring(delimPos + delim.length());
            delimPos = str.indexOf(delim);
        }

        // Add the trailing part that's left
        splitParts.add(trimWhitespace ? str.trim() : str);

        return splitParts.stream();
    }

    public static Stream<String> asDsvStream(String str, String delim) {
        return asDsvStream(str, delim, true);
    }

    public static List<String> asDsvList(String str, String delim) {
        Stream<String> dsvStream = asDsvStream(str, delim);
        return dsvStream == null ? null : dsvStream.collect(Collectors.toList());
    }

    public static List<String> asCsvList(String str) {
        Stream<String> dsvStream = asDsvStream(str, ",");
        return dsvStream == null ? null : dsvStream.collect(Collectors.toList());
    }

    public static Set<String> asCsvSet(String str) {
        Stream<String> dsvStream = asDsvStream(str, ",");
        return dsvStream == null ? null : dsvStream.collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public static String nvlStr(String nullableOrEmpty) {
        return isEmptyOrNull(nullableOrEmpty) ? null : nullableOrEmpty;
    }

    public static boolean isUpperAlpha(final int ch) {
        return (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9');
    }

    public static boolean isLowerAlpha(final int ch) {
        return (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9');
    }

    public static boolean isNumeric(final int ch) {
        return (ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9');
    }

    public static boolean isAlphanumeric(final int ch) {
        return isUpperAlpha(ch) || isLowerAlpha(ch) || isNumeric(ch);
    }

    public static boolean isAsciiPrintableSpecial(final int ch) {
        return ASCII_PRINTABLE_SPECIAL_CHARS.indexOf(ch) >= 0;
    }

    public static boolean isAlphanumericOrAsciiPrintableSpecial(final int ch) {
        return isAlphanumeric(ch) || isAsciiPrintableSpecial(ch);
    }

    public static String randomChars(final CharSequence charSequence, final int minimumLengthInclusive, final int maximumLengthInclusive) {
        if (charSequence == null) return null;
        final int inputSequenceLength = charSequence.length();
        final int chosenLength = minimumLengthInclusive+((int)Math.round(Math.random() * Math.abs(maximumLengthInclusive-minimumLengthInclusive)));
        StringBuilder s = new StringBuilder(chosenLength);
        for (int n=0; n < chosenLength; n++) {
            s.append(charSequence.charAt((int)Math.floor(Math.random()*inputSequenceLength)));
        }
        return s.toString();
    }

    public static String randomAlphaumericChars(final int minimumLengthInclusive, final int maximumLengthInclusive) {
        return randomChars(ALPHNUMERIC_CHARS, minimumLengthInclusive, maximumLengthInclusive);
    }

    public static String randomAlphaumericChars(final int maximumLengthInclusive) {
        return randomAlphaumericChars(maximumLengthInclusive, maximumLengthInclusive);
    }

    public static String randomAlphanumericAsciiPrintableSpecialChars(final int minimumLengthInclusive, final int maximumLengthInclusive) {
        return randomChars(ALPHNUMERIC_ASCII_PRINTABLE_SPECIA_CHARS, minimumLengthInclusive, maximumLengthInclusive);
    }

    public static String randomAlphanumericAsciiPrintableSpecialChars(final int maximumLengthInclusive) {
        return randomAlphanumericAsciiPrintableSpecialChars(maximumLengthInclusive, maximumLengthInclusive);
    }

    /**
     * Splits the specified string into a stream of lines. Lines can be separated by a line-feed or a carriage-return line-feed
     * terminator.
     *
     * @param str the string to be split into a stream of lines.
     * @return a stream of the lines of the string, or null if the string specified was null.
     */
    public static Stream<String> toLinesStream(final String str) {
        if (str == null) {
            return null;
        }

        return Arrays.stream(toLinesArray(str));
    }

    /**
     * Splits the specified string into lines. Lines can be separated by a line-feed or a carriage-return line-feed
     * terminator.
     *
     * @param str the string to be split into lines
     * @return an array of lines that comprise the string, or null if the string specified was null
     */
    public static String[] toLinesArray(final String str) {
        if (str == null) {
            return null;
        }

        BufferedReader br = new BufferedReader(new StringReader(str));

        ArrayList<String> linesList = new ArrayList<String>();

        try {
            String line = br.readLine();
            while (line != null) {
                linesList.add(line);
                line = br.readLine();
            }
        } catch (IOException notGoingToHappenWithAnInMemoryStringReaderEx) {
        }

        return linesList.toArray(new String[linesList.size()]);
    }

    /**
     * Determines if a given string contains any pf the given characters.
     *
     * @param str the string to be tested for containing any of the specified characters.
     * @param chars the characters to be determined as contained within the given string.
     * @return true if the given string contains any of the specified characters, false otherwise.
     */
    public static boolean containsAnyOf(String str, String chars) {
        if (str == null || chars == null) return false;

        for (int s=0; s < str.length(); s++) {
            for (int c=0; c < chars.length(); c++) {
                if (str.charAt(s) == chars.charAt(c) ) return true;
            }
        }

        return false;
    }
}
