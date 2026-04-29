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

package org.beanplanet.core.util;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
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

        return switch (str.length()) {
            case 0 -> str;
            case 1 -> str.toUpperCase();
            default -> str.substring(0, 1).toUpperCase()
                       + (forceLowercase ? str.substring(1).toLowerCase() : str.substring(1));
        };
    }

    /**
     * Determines whether an object string is null or contains only whitespace.
     *
     * @param obj the object whose string value is to be determined as blank.
     * @return true if the object or object's string value is null or if it contains whitespace characters only, false otherwise.
     */
    public static boolean isBlank(final Object obj) {
        if (obj == null) return true;

        final var s = obj.toString();
        return s == null || s.isBlank();
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
        return Optional.ofNullable(obj)
                       .map(Object::toString)
                       .map(String::isBlank)
                       .orElse(true);
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
    public static String emptyString() {
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
    public static String repeat(CharSequence characterSequence, int times) {
        if (characterSequence == null) {
            return null;
        }

        return characterSequence.toString().repeat(Math.max(0, times));
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

        var sbuf = new StringBuilder(); // Output StringBuilder we'll
        // build up
        int pos = 0; // Our position in the old string
        int index = inString.indexOf(oldPattern); // The index of an occurrence
        // we've found, or -1
        int patLen = oldPattern.length();
        while (index >= 0) {
            sbuf.append(inString, pos, index);
            sbuf.append(newPattern);
            pos = index + patLen;
            index = inString.indexOf(oldPattern, pos);
        }
        sbuf.append(inString, pos, inString.length()); // Remember to append any
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
        return Pattern.compile(regexPattern)
                      .matcher(inString)
                      .replaceAll(newStr);
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
        trailer = Objects.requireNonNullElse(trailer, "");
        if (s.length() <= charLimit) {
            return s;
        }
        return s.substring(0, charLimit) + trailer;
    }

    /**
     * Obtains the platform default character set encoding.
     *
     * @return the character set encoding configured in the JVM.
     */
    public static String getDefaultCharacterEncoding() {
        // Not available on all platforms
        final var defaultEncoding = new java.io.OutputStreamWriter(java.io.OutputStream.nullOutputStream()).getEncoding();
        return System.getProperty("file.encoding", Objects.requireNonNullElse(defaultEncoding, "<unknown charset encoding>"));
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
        if (occurrence == null || occurrence.isEmpty()) {
            return str;
        }

        return rTrim(lTrim(str, occurrence), occurrence);
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
        if (occurrence == null || occurrence.isEmpty()) {
            return str;
        }

        final var occurrenceLength = occurrence.length();
        while (occurrenceLength <= str.length()
               && str.regionMatches(!caseSensitive, 0, occurrence, 0, occurrenceLength)) {
            str = str.substring(occurrenceLength);
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
        if (occurrence == null || occurrence.isEmpty()) {
            return str;
        }

        final var occurrenceLength = occurrence.length();
        for (int lengthDiff = str.length() - occurrenceLength;
             lengthDiff >= 0 && str.regionMatches(!caseSensitive, lengthDiff, occurrence, 0, occurrenceLength);
             lengthDiff = str.length() - occurrenceLength) {
            str = str.substring(0, lengthDiff);
        }

        return str;
    }

    /**
     * Returns a string consisting of the elements of a collection delimited by comma (,).  Before being included,
     * elements are optionally filtered by a given predicate.  Once the decision to include an element has been made, the
     * element may be optionally transformed by a given transformation function.
     *
     * @param collection the collection of objects to be delimited, which may be null.
     * @return null if the given collection is null, otherwise comma-separated-vales (CSV) string of the collection elements.
     */
    public static <T> String asCsvString(Collection<T> collection) {
        return collection == null ? null : asDelimitedString(collection.stream(), ",");
    }

    /**
     * Returns a string consisting of the elements of a stream delimited by comma (,). Elements first have their string
     * representations generated by a call to <code>{@link String#toString()}</code>, prior to being added to the
     * delimited string being built.
     *
     * @param stream the stream of objects to be delimited, which may be null.
     * @return null if the given stream is null, otherwise comma-separated-vales (CSV) string of the stream elements.
     * @see #asDelimitedString(Stream, String)
     */
    public static <T> String asCsvString(Stream<T> stream) {
        return stream == null ? null : asDelimitedString(stream, ",");
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

        final Predicate<T> effectiveFilter = filter != null ? filter : truePredicate();
        final Function<T, String> effectiveTransformer = transformer != null ? transformer : toStringFunction();

        return stream.filter(effectiveFilter)
                     .map(effectiveTransformer)
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
    public static <T> String asDelimitedString(T[] arr, Predicate<T> filter, String delimiter, Function<T, String> transformer) {
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
    public static <T> String asDelimitedString(T[] arr, Predicate<T> filter, String delimiter) {
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
    public static <T> String asDelimitedString(T[] arr, String delimiter, Function<T, String> transformer) {
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
    public static <T> String asDelimitedString(T[] arr, String delimiter) {
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

        final BiPredicate<K, V> effectiveFilter = filter != null ? filter : trueBiPredicate();
        final Function<K, String> effectiveKeyTransformer = keyTransformer != null ? keyTransformer : toStringFunction();
        final Function<V, String> effectiveValueTransformer = valueTransformer != null ? valueTransformer : toStringFunction();

        return asDelimitedString(map.entrySet().stream(),
                                 e -> effectiveFilter.test(e.getKey(), e.getValue()),
                                 entryDelimiter,
                                 e -> effectiveKeyTransformer.apply(e.getKey())
                                      + kvDelimiter
                                      + effectiveValueTransformer.apply(e.getValue()));
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
    public static String asDelimitedStringOfNotEmpty(final String delimiter, Object... collection) {
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

        if (caseSensitive ? str.regionMatches(true, 0, prefix, 0, prefix.length()) : str.startsWith(prefix)) {
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

        if (caseSensitive
            ? str.regionMatches(true, str.length() - suffix.length(), suffix, 0, suffix.length())
            : str.endsWith(suffix)) {
            return str;
        }
        else {
            return str + suffix;
        }
    }

    public static Stream<String> asDsvStream(String str, String delim, boolean trimWhitespace) {
        if (str == null)
            return null;

        if (delim == null || delim.isEmpty()) {
            throw new IllegalArgumentException("Cannot split a string using a null or empty delimiter [str=" + str + ", delimeter=" + delim + "]");
        }

        if (!str.contains(delim)) {
            return trimWhitespace && isBlank(str) ? Stream.empty() : Stream.of(str);
        }

        return Arrays.stream(str.split(Pattern.quote(delim), -1))
                     .map(part -> trimWhitespace ? part.trim() : part);
    }

    public static Stream<String> asDsvStream(String str, String delim) {
        return asDsvStream(str, delim, true);
    }

    public static List<String> asDsvList(String str, String delim) {
        var dsvStream = asDsvStream(str, delim);
        return dsvStream == null ? null : dsvStream.collect(Collectors.toList());
    }

    public static Stream<String> asCsvStream(String str) {
        return asDsvStream(str, ",");
    }

    public static List<String> asCsvList(String str) {
        var dsvStream = asDsvStream(str, ",");
        return dsvStream == null ? null : dsvStream.collect(Collectors.toList());
    }

    public static Set<String> asCsvSet(String str) {
        var dsvStream = asDsvStream(str, ",");
        return dsvStream == null ? null : dsvStream.collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public static String nvlStr(String nullableOrEmpty) {
        return isEmptyOrNull(nullableOrEmpty) ? null : nullableOrEmpty;
    }

    public static boolean isUpperAlpha(final int ch) {
        return ALPHNUMERIC_CHARS.indexOf(ch) >= 0;
    }

    public static boolean isLowerAlpha(final int ch) {
        return ALPHNUMERIC_CHARS.indexOf(ch) >= 0;
    }

    public static boolean isNumeric(final int ch) {
        return ALPHNUMERIC_CHARS.indexOf(ch) >= 0;
    }

    public static boolean isAlphanumeric(final int ch) {
        return ALPHNUMERIC_CHARS.indexOf(ch) >= 0;
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
        final var random = ThreadLocalRandom.current();
        final int chosenLength = minimumLengthInclusive + ((int) Math.round(random.nextDouble() * Math.abs(maximumLengthInclusive - minimumLengthInclusive)));
        var s = new StringBuilder(chosenLength);
        for (int n=0; n < chosenLength; n++) {
            s.append(charSequence.charAt(random.nextInt(inputSequenceLength)));
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
        return str == null ? null : str.lines();
    }

    /**
     * Splits the specified string into a list of lines. Lines can be separated by a line-feed or a carriage-return line-feed
     * terminator.
     *
     * @param str the string to be split into a stream of lines.
     * @return a list of the lines of the string, or null if the string specified was null.
     */
    public static List<String> toLinesList(final String str) {
        return str == null ? null : str.lines().collect(Collectors.toList());
    }

    /**
     * Splits the specified string into lines. Lines can be separated by a line-feed or a carriage-return line-feed
     * terminator.
     *
     * @param str the string to be split into lines
     * @return an array of lines that comprise the string, or null if the string specified was null
     */
    public static String[] toLinesArray(final String str) {
        return str == null ? null : str.lines().toArray(String[]::new);
    }

    /**
     * Determines if a given string contains any pf the given characters.
     *
     * @param str the string to be tested for containing any of the specified characters.
     * @param chars the characters to be determined as contained within the given string.
     * @return true if the given string contains any of the specified characters, false otherwise.
     */
    public static boolean containsAnyOf(String str, String chars) {
        return str != null
               && chars != null
               && str.chars().anyMatch(ch -> chars.indexOf(ch) >= 0);
    }

    /**
     * Appends to a given string builder a comma-separated list (CSV) of the computed string values of the given collection.
     *
     * @param buf the string builder where the delimited values are to be written.
     * @param elements the collection of elements whose string values are to be written; may be null.
     * @param toStringConverter a function capable of converting from the given array value type to string.
     * @return the string builder for chaining.
     * @param <T> the type of elements of the collection.
     */
    public static <T> StringBuilder asCsvAppendedTo(final StringBuilder buf,
                                                    final Collection<T> elements,
                                                    final Function<T, String> toStringConverter) {
        return asDsvAppendedTo(buf, ",", elements, toStringConverter);
    }

    /**
     * Appends to a given string builder a comma-separated list (CSV) of the computed string values of the given array.
     *
     * @param buf the string builder where the delimited values are to be written.
     * @param elements the array of elements whose string values are to be written; may be null.
     * @param toStringConverter a function capable of converting from the given array value type to string.
     * @return the string builder for chaining.
     * @param <T> the type of elements of the array.
     */
    public static <T> StringBuilder asCsvAppendedTo(final StringBuilder buf,
                                                    final T[] elements,
                                                    final Function<T, String> toStringConverter) {
        return asDsvAppendedTo(buf, ",", elements, toStringConverter);
    }

    /**
     * Appends to a given string builder a comma-separated list (CSV) of the computed string values of the given stream.
     *
     * @param buf the string builder where the delimited values are to be written.
     * @param elements the stream of elements whose string values are to be written; which may be null.
     * @param toStringConverter a function capable of converting from the given stream value type to string.
     * @return the string builder for chaining.
     * @param <T> the type of elements of the stream.
     */
    public static <T> StringBuilder asCsvAppendedTo(final StringBuilder buf,
                                                    final Stream<T> elements,
                                                    final Function<T, String> toStringConverter) {
        return asDsvAppendedTo(buf, ",", elements, toStringConverter);
    }

    /**
     * Appends to a given string builder a delimiter-separated list (DSV) of the computed string values of the given collection.
     *
     * @param buf the string builder where the delimited values are to be written.
     * @param delimiter the delimiter with which to separate values.
     * @param elements the collection of elements whose string values are to be written; may be null.
     * @param toStringConverter a function capable of converting from the given array value type to string.
     * @return the string builder for chaining.
     * @param <T> the type of elements of the collection.
     */
    public static <T> StringBuilder asDsvAppendedTo(final StringBuilder buf,
                                                    final CharSequence delimiter,
                                                    final Collection<T> elements,
                                                    final Function<T, String> toStringConverter) {
        return elements == null ? buf : asDsvAppendedTo(buf, delimiter, elements.stream(), toStringConverter);
    }

    /**
     * Appends to a given string builder a delimiter-separated list (CSV) of the computed string values of the given array.
     *
     * @param buf the string builder where the delimited values are to be written.
     * @param delimiter the delimiter with which to separate values.
     * @param elements the array of elements whose string values are to be written; may be null.
     * @param toStringConverter a function capable of converting from the given array value type to string.
     * @return the string builder for chaining.
     * @param <T> the type of elements of the array.
     */
    public static <T> StringBuilder asDsvAppendedTo(final StringBuilder buf,
                                                    final CharSequence delimiter,
                                                    final T[] elements,
                                                    final Function<T, String> toStringConverter) {
        return elements == null ? buf : asDsvAppendedTo(buf, delimiter, Stream.of(elements), toStringConverter);
    }

    /**
     * Appends to a given string builder a delimiter-separated list (CSV) of the computed string values of the given stream.
     *
     * @param buf the string builder where the delimited values are to be written.
     * @param delimiter the delimiter with which to separate values.
     * @param elements the stream of elements whose string values are to be written; which may be null.
     * @param toStringConverter a function capable of converting from the given stream value type to string.
     * @return the string builder for chaining.
     * @param <T> the type of elements of the stream.
     */
    public static <T> StringBuilder asDsvAppendedTo(final StringBuilder buf,
                                                    final CharSequence delimiter,
                                                    final Stream<T> elements,
                                                    final Function<T, String> toStringConverter) {
        if (elements != null) {
            buf.append(elements.map(toStringConverter)
                               .collect(Collectors.joining(delimiter.toString())));
        }

        return buf;
    }

    /**
     * Given a number of supplied character sequences, returns the first one that is not blank (i.e. does not contain only
     * whitespace characters).
     *
     * @param sequenceSuppliers the character sequences whose first non-blank is to be returned, with sequence suppliers
     *                          only called when required; may be null.
     * @return the first non-blank of the given character sequences, or null if sequences was null or if all supplied are blank.
     * @see #isBlank(Object)
     */
    @SafeVarargs
    public static CharSequence firstNonBlank(final Supplier<CharSequence> ... sequenceSuppliers) {
        return sequenceSuppliers == null
               ? null
               : Arrays.stream(sequenceSuppliers)
                       .map(Supplier::get)
                       .filter(StringUtil::isNotBlank)
                       .findFirst()
                       .orElse(null);
    }

    /**
     * Given a number of character sequences, returns the first one that is not blank (i.e. does not contain only
     * whitespace characters).
     *
     * @param sequences the character sequences whose first non-blank is to be returned; may be null.
     * @return the first non-blank of the given character sequences, or null if sequences was null or if all are blank.
     * @see #isBlank(Object)
     */
    public static CharSequence firstNonBlank(final CharSequence ... sequences) {
        return sequences == null
               ? null
               : Arrays.stream(sequences)
                       .filter(StringUtil::isNotBlank)
                       .findFirst()
                       .orElse(null);
    }

    /**
     * Given a number of strings, returns the first one that is not blank (i.e. does not contain only
     * whitespace characters).
     *
     * @param strings the strings whose first non-blank is to be returned; may be null.
     * @return the first non-blank of the given strings, or null if strings was null or if all are blank.
     * @see #isBlank(Object)
     */
    public static String firstNonBlank(final String ... strings) {
        return (String)firstNonBlank((CharSequence[]) strings);
    }

}
