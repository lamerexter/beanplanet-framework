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

import org.junit.Test;

import java.io.OutputStreamWriter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.beanplanet.core.util.StringUtil.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class StringUtilTest {

    @Test
    public void toLowercase_whenInputHasMixedCase_thenReturnsLowercaseValue() {
        assertThat(StringUtil.toLowercase("BeanPLANET 21"), equalTo("beanplanet 21"));
    }

    @Test
    public void toUppercase_whenInputHasMixedCase_thenReturnsUppercaseValue() {
        assertThat(StringUtil.toUppercase("BeanPLANET 21"), equalTo("BEANPLANET 21"));
    }

    @Test
    public void initCap_whenUsingDefaultOverload_thenLeavesRemainingCharactersUntouched() {
        assertThat(initCap("hELLO"), equalTo("HELLO"));
    }

    @Test
    public void initCap_whenStringIsNullEmptyOrSingleCharacter_thenReturnsExpectedValue() {
        assertThat(initCap((String) null, true), nullValue());
        assertThat(initCap("", true), equalTo(""));
        assertThat(initCap("q", false), equalTo("Q"));
    }

    @Test
    public void isNotBlank_whenInputIsWhitespaceOrText_thenReturnsInverseOfIsBlank() {
        assertThat(isNotBlank("\t\n"), is(false));
        assertThat(isNotBlank(" value "), is(true));
    }

    @Test
    public void isEmptyOrNullAndNotEmptyVariants_whenInputIsNullBlankOrText_thenReturnExpectedValues() {
        assertThat(isEmptyOrNull(null), is(true));
        assertThat(isEmptyOrNull("  "), is(true));
        assertThat(isEmptyOrNull(" value "), is(false));

        assertThat(notEmpty(null), is(false));
        assertThat(notEmpty("\n"), is(false));
        assertThat(notEmpty("value"), is(true));

        assertThat(notEmptyAndNotNull(null), is(false));
        assertThat(notEmptyAndNotNull("   "), is(false));
        assertThat(notEmptyAndNotNull("value"), is(true));
    }

    @Test
    public void emptyString_whenCalled_thenReturnsEmptyString() {
        assertThat(StringUtil.emptyString(), equalTo(""));
    }

    @Test
    public void repeat_whenTimesAreNegativeZeroOrPositive_thenReturnsExpectedValue() {
        assertThat(repeat(new StringBuilder("ab"), -3), equalTo(""));
        assertThat(repeat(new StringBuilder("ab"), 0), equalTo(""));
        assertThat(repeat(new StringBuilder("ab"), 3), equalTo("ababab"));
    }

    @Test
    public void replaceAll_whenInputOrPatternsAreNull_thenReturnsExpectedValue() {
        assertThat(replaceAll(null, "a", "b"), nullValue());
        assertThat(replaceAll("abc", null, "b"), equalTo("abc"));
        assertThat(replaceAll("abc", "a", null), equalTo("abc"));
    }

    @Test
    public void replaceAll_whenPatternOccursOrDoesNotOccur_thenReturnsExpectedValue() {
        assertThat(replaceAll("abracadabra", "abra", "AB"), equalTo("ABcadAB"));
        assertThat(replaceAll("beanplanet", "xyz", "AB"), equalTo("beanplanet"));
    }

    @Test
    public void replaceAllRegex_whenRegexMatchesOrDoesNotMatch_thenReturnsExpectedValue() {
        assertThat(replaceAllRegex("a1 b22 c333", "\\d+", "#"), equalTo("a# b# c#"));
        assertThat(replaceAllRegex("plain", "\\d+", "#"), equalTo("plain"));
    }

    @Test
    public void truncate_whenInputIsNullWithinLimitOrExceedsLimit_thenReturnsExpectedValue() {
        assertThat(truncate(null, 3, "..."), equalTo(""));
        assertThat(truncate("cat", 3, "..."), equalTo("cat"));
        assertThat(truncate("catalogue", 3, null), equalTo("cat"));
        assertThat(truncate("catalogue", 3, "..."), equalTo("cat..."));
    }

    @Test
    public void getDefaultCharacterEncoding_whenFileEncodingPropertyIsPresent_thenReturnsThatProperty() {
        final String originalEncoding = System.getProperty("file.encoding");
        try {
            System.setProperty("file.encoding", "UTF-TEST");
            assertThat(getDefaultCharacterEncoding(), equalTo("UTF-TEST"));
        }
        finally {
            restoreSystemProperty("file.encoding", originalEncoding);
        }
    }

    @Test
    public void getDefaultCharacterEncoding_whenFileEncodingPropertyIsMissing_thenUsesWriterEncodingFallback() {
        final String originalEncoding = System.getProperty("file.encoding");
        final String expectedEncoding = new OutputStreamWriter(java.io.OutputStream.nullOutputStream()).getEncoding();
        try {
            System.clearProperty("file.encoding");
            assertThat(getDefaultCharacterEncoding(), equalTo(expectedEncoding));
        }
        finally {
            restoreSystemProperty("file.encoding", originalEncoding);
        }
    }

    @Test
    public void trim_whenOccurrenceIsNullEmptyOrPresentAtBothEnds_thenReturnsExpectedValue() {
        assertThat(trim(null, "ab"), nullValue());
        assertThat(trim("value", null), equalTo("value"));
        assertThat(trim("value", ""), equalTo("value"));
        assertThat(trim("ababvalueabab", "ab"), equalTo("value"));
        assertThat(trim("value", "ab"), equalTo("value"));
    }

    @Test
    public void lTrim_whenCaseSensitiveFlagVaries_thenReturnsExpectedValue() {
        assertThat(lTrim(null, "ab"), nullValue());
        assertThat(lTrim("value", null), equalTo("value"));
        assertThat(lTrim("AbabValue", "ab", true), equalTo("AbabValue"));
        assertThat(lTrim("AbabValue", "ab", false), equalTo("Value"));
        assertThat(lTrim("ababValue", "ab"), equalTo("Value"));
    }

    @Test
    public void rTrim_whenCaseSensitiveFlagVaries_thenReturnsExpectedValue() {
        assertThat(rTrim(null, "ab"), nullValue());
        assertThat(rTrim("value", ""), equalTo("value"));
        assertThat(rTrim("ValueABab", "ab", true), equalTo("ValueAB"));
        assertThat(rTrim("ValueABab", "ab", false), equalTo("Value"));
        assertThat(rTrim("Valueabab", "ab"), equalTo("Value"));
    }

    @Test
    public void asCsvString_whenCollectionOrStreamContainsValues_thenReturnsCommaDelimitedString() {
        assertThat(asCsvString(List.of("alpha", "beta")), equalTo("alpha,beta"));
        assertThat(asCsvString(Stream.of("alpha", "beta")), equalTo("alpha,beta"));
    }

    @Test
    public void asDelimitedString_whenStreamOverloadsReceiveNull_thenReturnNull() {
        assertThat(asDelimitedString((Stream<String>) null, (Predicate<String>) null, ",", null), nullValue());
        assertThat(asDelimitedString((Stream<String>) null, (Predicate<String>) null, ","), nullValue());
        assertThat(asDelimitedString((Stream<String>) null, ",", (Function<String, String>) null), nullValue());
        assertThat(asDelimitedString((Stream<String>) null, ","), nullValue());
    }

    @Test
    public void asDelimitedString_whenStreamOverloadsReceiveValues_thenApplyFiltersAndTransformers() {
        assertThat(asDelimitedString(Stream.of("a", "b", "c"), value -> !"b".equals(value), "|", String::toUpperCase), equalTo("A|C"));
        assertThat(asDelimitedString(Stream.of("a", "b"), value -> true, "|"), equalTo("a|b"));
        assertThat(asDelimitedString(Stream.of("a", "b"), "|", value -> value + value), equalTo("aa|bb"));
        assertThat(asDelimitedString(Stream.of("a", "b"), "|"), equalTo("a|b"));
    }

    @Test
    public void asDelimitedString_whenArrayOverloadsReceiveNull_thenReturnNull() {
        assertThat(asDelimitedString((String[]) null, value -> true, ",", String::valueOf), nullValue());
        assertThat(asDelimitedString((String[]) null, value -> true, ","), nullValue());
        assertThat(asDelimitedString((String[]) null, ",", String::valueOf), nullValue());
        assertThat(asDelimitedString((String[]) null, ","), nullValue());
    }

    @Test
    public void asDelimitedString_whenArrayOverloadsReceiveValues_thenApplyFiltersAndTransformers() {
        final String[] values = {"a", "b", "c"};
        assertThat(asDelimitedString(values, value -> !"b".equals(value), "|", String::toUpperCase), equalTo("A|C"));
        assertThat(asDelimitedString(values, value -> !"b".equals(value), "|"), equalTo("a|c"));
        assertThat(asDelimitedString(values, "|", value -> value + value), equalTo("aa|bb|cc"));
        assertThat(asDelimitedString(values, "|"), equalTo("a|b|c"));
    }

    @Test
    public void asDelimitedString_whenCollectionOverloadsReceiveNull_thenReturnNull() {
        assertThat(asDelimitedString((Collection<String>) null, value -> true, ",", String::valueOf), nullValue());
        assertThat(asDelimitedString((Collection<String>) null, value -> true, ","), nullValue());
        assertThat(asDelimitedString((Collection<String>) null, ",", String::valueOf), nullValue());
        assertThat(asDelimitedString((Collection<String>) null, ","), nullValue());
    }

    @Test
    public void asDelimitedString_whenCollectionOverloadsReceiveValues_thenApplyFiltersAndTransformers() {
        final List<String> values = List.of("a", "b", "c");
        assertThat(asDelimitedString(values, value -> !"b".equals(value), "|", String::toUpperCase), equalTo("A|C"));
        assertThat(asDelimitedString(values, value -> !"b".equals(value), "|"), equalTo("a|c"));
        assertThat(asDelimitedString(values, "|", value -> value + value), equalTo("aa|bb|cc"));
        assertThat(asDelimitedString(values, "|"), equalTo("a|b|c"));
    }

    @Test
    public void asDelimitedString_whenStreamableOverloadsReceiveNull_thenReturnNull() {
        assertThat(asDelimitedString((Streamable<String>) null, ","), nullValue());
        assertThat(asDelimitedString((Streamable<String>) null, value -> true, ",", String::valueOf), nullValue());
    }

    @Test
    public void asDelimitedString_whenStreamableOverloadsReceiveValues_thenApplyFiltersAndTransformers() {
        final Streamable<String> values = streamableOf("a", "b", "c");
        assertThat(asDelimitedString(values, "|"), equalTo("a|b|c"));
        assertThat(asDelimitedString(values, value -> !"b".equals(value), "|", String::toUpperCase), equalTo("A|C"));
    }

    @Test
    public void asDelimitedString_whenIterableOverloadsReceiveNull_thenReturnNull() {
        assertThat(asDelimitedString((Iterable<String>) null, ","), nullValue());
        assertThat(asDelimitedString((Iterable<String>) null, value -> true, ",", String::valueOf), nullValue());
    }

    @Test
    public void asDelimitedString_whenIterableOverloadsReceiveValues_thenApplyFiltersAndTransformers() {
        final Iterable<String> values = List.of("a", "b", "c");
        assertThat(asDelimitedString(values, "|"), equalTo("a|b|c"));
        assertThat(asDelimitedString(values, value -> !"b".equals(value), "|", String::toUpperCase), equalTo("A|C"));
    }

    @Test
    public void asDelimitedString_whenMapOverloadsReceiveNull_thenReturnNull() {
        assertThat(asDelimitedString((Map<String, String>) null, (BiPredicate<String, String>) null, "&", "=", null, null), nullValue());
        assertThat(asDelimitedString((Map<String, String>) null, (BiPredicate<String, String>) null, "&", "="), nullValue());
        assertThat(asDelimitedString((Map<String, String>) null, "&", "=", null, null), nullValue());
        assertThat(asDelimitedString((Map<String, String>) null, "&", "="), nullValue());
    }

    @Test
    public void asDelimitedString_whenMapOverloadsReceiveValues_thenApplyFiltersAndTransformers() {
        final Map<String, Integer> values = new LinkedHashMap<>();
        values.put("a", 1);
        values.put("b", 2);
        values.put("c", 3);

        assertThat(asDelimitedString(values, (key, value) -> value % 2 == 1, "&", "=", String::toUpperCase, value -> "v" + value), equalTo("A=v1&C=v3"));
        assertThat(asDelimitedString(values, (key, value) -> value % 2 == 1, "&", "="), equalTo("a=1&c=3"));
        assertThat(asDelimitedString(values, "&", "=", String::toUpperCase, value -> "v" + value), equalTo("A=v1&B=v2&C=v3"));
        assertThat(asDelimitedString(values, "&", "="), equalTo("a=1&b=2&c=3"));
    }

    @Test
    public void asDelimitedStringOfNotEmpty_whenValuesContainNullOrBlankEntries_thenFiltersThemOut() {
        assertThat(asDelimitedStringOfNotEmpty(",", (Object[]) null), nullValue());
        assertThat(asDelimitedStringOfNotEmpty(",", "alpha", null, " ", 7, "beta"), equalTo("alpha,7,beta"));
    }

    @Test
    public void ensureHasPrefix_whenInputOrPrefixIsNullOrAlreadyPresent_thenReturnsExpectedValue() {
        assertThat(ensureHasPrefix(null, "pre"), nullValue());
        assertThat(ensureHasPrefix("value", null), equalTo("value"));
        assertThat(ensureHasPrefix("prefixValue", "prefix"), equalTo("prefixValue"));
        assertThat(ensureHasPrefix("Value", "pre"), equalTo("preValue"));
    }

    @Test
    public void ensureHasPrefix_whenCaseInsensitiveFlagIsTrue_thenMatchesIgnoringCase() {
        assertThat(ensureHasPrefix("PrefixValue", "prefix", true), equalTo("PrefixValue"));
        assertThat(ensureHasPrefix("Value", "prefix", true), equalTo("prefixValue"));
    }

    @Test
    public void ensureHasSuffix_whenInputOrSuffixIsNullOrAlreadyPresent_thenReturnsExpectedValue() {
        assertThat(ensureHasSuffix(null, "suf"), nullValue());
        assertThat(ensureHasSuffix("value", null), equalTo("value"));
        assertThat(ensureHasSuffix("valueSuffix", "Suffix"), equalTo("valueSuffix"));
        assertThat(ensureHasSuffix("value", "Suffix"), equalTo("valueSuffix"));
    }

    @Test
    public void ensureHasSuffix_whenCaseInsensitiveFlagIsTrue_thenMatchesIgnoringCaseAndHandlesLongSuffix() {
        assertThat(ensureHasSuffix("valueSuffix", "suffix", true), equalTo("valueSuffix"));
        assertThat(ensureHasSuffix("a", "suffix", true), equalTo("asuffix"));
    }

    @Test
    public void asDsvStream_whenInputIsNull_thenReturnsNull() {
        assertThat(asDsvStream(null, ",", true), nullValue());
        assertThat(asDsvStream(null, ","), nullValue());
    }

    @Test
    public void asDsvStream_whenDelimiterIsNullOrEmpty_thenThrowsIllegalArgumentException() {
        expectIllegalArgument(() -> asDsvStream("a,b", null, true));
        expectIllegalArgument(() -> asDsvStream("a,b", "", true));
    }

    @Test
    public void asDsvStream_whenNoDelimiterExists_thenReturnsEmptyForBlankInputOnlyWhenTrimEnabled() {
        assertThat(toList(asDsvStream("   ", ",", true)), equalTo(Collections.emptyList()));
        assertThat(toList(asDsvStream("   ", ",", false)), equalTo(List.of("   ")));
        assertThat(toList(asDsvStream("value", ",", true)), equalTo(List.of("value")));
    }

    @Test
    public void asDsvStream_whenDelimiterExists_thenSplitsPreservesTrailingValuesAndOptionallyTrims() {
        assertThat(toList(asDsvStream(" a | b || ", "|", true)), equalTo(List.of("a", "b", "", "")));
        assertThat(toList(asDsvStream(" a | b || ", "|", false)), equalTo(List.of(" a ", " b ", "", " ")));
        assertThat(toList(asDsvStream("a,b,c", ",")), equalTo(List.of("a", "b", "c")));
    }

    @Test
    public void asDsvList_whenInputIsNullOrContainsValues_thenReturnsExpectedList() {
        assertThat(asDsvList(null, "|"), nullValue());
        assertThat(asDsvList(" a | b | c ", "|"), equalTo(List.of("a", "b", "c")));
    }

    @Test
    public void csvParsingHelpers_whenInputContainsValues_thenReturnExpectedStructures() {
        assertThat(toList(asCsvStream("a,b,c")), equalTo(List.of("a", "b", "c")));
        assertThat(asCsvList("a,b,c"), equalTo(List.of("a", "b", "c")));
        assertThat(asCsvSet("a,b,a,c"), equalTo(new LinkedHashSet<>(List.of("a", "b", "c"))));
    }

    @Test
    public void nvlStr_whenInputIsNullBlankOrText_thenReturnsExpectedValue() {
        assertThat(nvlStr(null), nullValue());
        assertThat(nvlStr(" \t"), nullValue());
        assertThat(nvlStr("value"), equalTo("value"));
    }

    @Test
    public void alphaNumericClassificationMethods_whenCharactersMatchCurrentImplementation_thenReturnExpectedValues() {
        assertThat(isUpperAlpha('A'), is(true));
        assertThat(isUpperAlpha('7'), is(true));
        assertThat(isUpperAlpha('!'), is(false));

        assertThat(isLowerAlpha('z'), is(true));
        assertThat(isLowerAlpha('7'), is(true));
        assertThat(isLowerAlpha('!'), is(false));

        assertThat(isNumeric('3'), is(true));
        assertThat(isNumeric('A'), is(true));
        assertThat(isNumeric('!'), is(false));

        assertThat(StringUtil.isAlphanumeric('A'), is(true));
        assertThat(StringUtil.isAlphanumeric('3'), is(true));
        assertThat(StringUtil.isAlphanumeric('!'), is(false));
    }

    @Test
    public void asciiPrintableClassificationMethods_whenCharactersMatchOrDoNotMatch_thenReturnExpectedValues() {
        assertThat(StringUtil.isAsciiPrintableSpecial('!'), is(true));
        assertThat(StringUtil.isAsciiPrintableSpecial('A'), is(false));
        assertThat(isAlphanumericOrAsciiPrintableSpecial('!'), is(true));
        assertThat(isAlphanumericOrAsciiPrintableSpecial('A'), is(true));
        assertThat(isAlphanumericOrAsciiPrintableSpecial('\n'), is(false));
    }

    @Test
    public void randomChars_whenLengthRangeAndCharacterSourceAreProvided_thenResultUsesOnlyAllowedCharacters() {
        assertThat(randomChars(null, 1, 3), nullValue());
        assertThat(randomChars("", 0, 0), equalTo(""));

        for (int n = 0; n < 20; n++) {
            final String random = randomChars("abc", 2, 4);
            assertThat(random.length(), allOf(greaterThanOrEqualTo(2), lessThanOrEqualTo(4)));
            for (int idx = 0; idx < random.length(); idx++) {
                assertThat("abc".indexOf(random.charAt(idx)) >= 0, is(true));
            }
        }
    }

    @Test
    public void randomAlphaNumericGenerators_whenCalled_thenRespectRequestedLengthsAndCharacterSets() {
        for (int n = 0; n < 20; n++) {
            final String ranged = randomAlphaumericChars(3, 5);
            assertThat(ranged.length(), allOf(greaterThanOrEqualTo(3), lessThanOrEqualTo(5)));
            for (int idx = 0; idx < ranged.length(); idx++) {
                assertThat(StringUtil.isAlphanumeric(ranged.charAt(idx)), is(true));
            }
        }

        final String fixed = randomAlphaumericChars(8);
        assertThat(fixed.length(), equalTo(8));
        for (int idx = 0; idx < fixed.length(); idx++) {
            assertThat(StringUtil.isAlphanumeric(fixed.charAt(idx)), is(true));
        }
    }

    @Test
    public void randomAsciiPrintableGenerators_whenCalled_thenRespectRequestedLengthsAndCharacterSets() {
        for (int n = 0; n < 20; n++) {
            final String ranged = randomAlphanumericAsciiPrintableSpecialChars(3, 5);
            assertThat(ranged.length(), allOf(greaterThanOrEqualTo(3), lessThanOrEqualTo(5)));
            for (int idx = 0; idx < ranged.length(); idx++) {
                assertThat(isAlphanumericOrAsciiPrintableSpecial(ranged.charAt(idx)), is(true));
            }
        }

        final String fixed = randomAlphanumericAsciiPrintableSpecialChars(8);
        assertThat(fixed.length(), equalTo(8));
        for (int idx = 0; idx < fixed.length(); idx++) {
            assertThat(isAlphanumericOrAsciiPrintableSpecial(fixed.charAt(idx)), is(true));
        }
    }

    @Test
    public void lineConversionHelpers_whenInputIsNullEmptyOrMultiLine_thenReturnExpectedRepresentations() {
        assertThat(toLinesStream(null), nullValue());
        assertThat(toLinesList(null), nullValue());
        assertThat(toLinesArray(null), nullValue());

        assertThat(toList(toLinesStream("line1\nline2\r\nline3")), equalTo(List.of("line1", "line2", "line3")));
        assertThat(toLinesList("line1\nline2\r\nline3"), equalTo(List.of("line1", "line2", "line3")));
        assertArrayEquals(new String[]{"line1", "line2", "line3"}, toLinesArray("line1\nline2\r\nline3"));
        assertThat(toList(toLinesStream("")), equalTo(Collections.emptyList()));
    }

    @Test
    public void containsAnyOf_whenInputsAreNullMatchingOrDisjoint_thenReturnsExpectedValue() {
        assertThat(containsAnyOf(null, "abc"), is(false));
        assertThat(containsAnyOf("value", null), is(false));
        assertThat(containsAnyOf("value", "xyz"), is(false));
        assertThat(containsAnyOf("value", "uyz"), is(true));
    }

    @Test
    public void asCsvAppendedTo_whenElementsAreNullOrPresent_thenReturnsExpectedBuilderContent() {
        final StringBuilder nullCollectionBuffer = new StringBuilder("prefix");
        assertThat(asCsvAppendedTo(nullCollectionBuffer, (Collection<String>) null, String::valueOf), sameInstance(nullCollectionBuffer));
        assertThat(nullCollectionBuffer.toString(), equalTo("prefix"));

        assertThat(asCsvAppendedTo(new StringBuilder(), List.of("a", "b"), String::toUpperCase).toString(), equalTo("A,B"));
        assertThat(asCsvAppendedTo(new StringBuilder(), new String[]{"a", "b"}, String::toUpperCase).toString(), equalTo("A,B"));
        assertThat(asCsvAppendedTo(new StringBuilder(), Stream.of("a", "b"), String::toUpperCase).toString(), equalTo("A,B"));
    }

    @Test
    public void asDsvAppendedTo_whenElementsAreNullOrPresent_thenReturnsExpectedBuilderContent() {
        final StringBuilder nullArrayBuffer = new StringBuilder("prefix");
        assertThat(asDsvAppendedTo(nullArrayBuffer, "|", (String[]) null, String::valueOf), sameInstance(nullArrayBuffer));
        assertThat(nullArrayBuffer.toString(), equalTo("prefix"));

        assertThat(asDsvAppendedTo(new StringBuilder(), "|", List.of("a", "b"), String::toUpperCase).toString(), equalTo("A|B"));
        assertThat(asDsvAppendedTo(new StringBuilder(), "|", new String[]{"a", "b"}, String::toUpperCase).toString(), equalTo("A|B"));
        assertThat(asDsvAppendedTo(new StringBuilder("prefix"), "|", Stream.of("a", "b"), String::toUpperCase).toString(), equalTo("prefixA|B"));
        assertThat(asDsvAppendedTo(new StringBuilder("prefix"), "|", (Stream<String>) null, String::toUpperCase).toString(), equalTo("prefix"));
    }

    @Test
    public void firstNonBlank_whenAllValuesAreBlank_thenReturnsNull() {
        assertThat(firstNonBlank(" ", "\t"), nullValue());
        assertThat(firstNonBlank((CharSequence) " ", (CharSequence) "\n"), nullValue());
        assertThat(firstNonBlank(() -> " ", () -> "\t"), nullValue());
    }

    @Test
    public void firstNonBlank_whenCharSequenceValuesContainNonBlank_thenReturnsFirstMatchingSequence() {
        final CharSequence expected = new StringBuilder("value");
        assertThat(firstNonBlank(new StringBuilder(" "), expected), sameInstance(expected));
    }

    @Test
    public void firstNonBlank_whenSuppliersContainNonBlankValue_thenReturnsFirstMatchWithoutCallingLaterSuppliers() {
        final AtomicInteger invocations = new AtomicInteger();
        final Supplier<CharSequence> first = () -> {
            invocations.incrementAndGet();
            return "value";
        };
        final Supplier<CharSequence> second = () -> {
            invocations.incrementAndGet();
            fail("Second supplier should not be invoked after the first non-blank value is found");
            return "other";
        };

        assertThat(firstNonBlank(first, second), equalTo((CharSequence) "value"));
        assertThat(invocations.get(), equalTo(1));
    }

    private static void expectIllegalArgument(Runnable runnable) {
        try {
            runnable.run();
            fail("Expected IllegalArgumentException to be thrown");
        }
        catch (IllegalArgumentException expected) {
            assertThat(expected.getMessage(), containsString("Cannot split a string using a null or empty delimiter"));
        }
    }

    private static void restoreSystemProperty(String propertyName, String originalValue) {
        if (originalValue == null) {
            System.clearProperty(propertyName);
        }
        else {
            System.setProperty(propertyName, originalValue);
        }
    }

    private static <T> List<T> toList(Stream<T> stream) {
        return stream.collect(Collectors.toList());
    }

    @SafeVarargs
    private static <T> Streamable<T> streamableOf(T... values) {
        return () -> Arrays.stream(values);
    }
}