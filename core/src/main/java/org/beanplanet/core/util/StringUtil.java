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

import org.beanplanet.core.Functions;
import org.beanplanet.core.Predicates;
import org.beanplanet.core.lang.Assert;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.beanplanet.core.Functions.toStringFunction;
import static org.beanplanet.core.Predicates.trueBiPredicate;
import static org.beanplanet.core.Predicates.truePredicate;

/**
 * A static utility class containing string-based operations.
 */
public class StringUtil {
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
}
