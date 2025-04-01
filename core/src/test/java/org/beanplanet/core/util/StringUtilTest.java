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

import org.junit.Test;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.beanplanet.core.Predicates.falsePredicate;
import static org.beanplanet.core.Predicates.truePredicate;
import static org.beanplanet.core.util.StringUtil.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class StringUtilTest {
    @Test
    public void toLowercase_null() {
        assertThat(StringUtil.toLowercase(null), nullValue());
    }

    @Test
    public void toLowercase() {
        assertThat(StringUtil.toLowercase("   "), equalTo("   "));
        assertThat(StringUtil.toLowercase("AbC dEF"), equalTo("abc def"));
    }

    @Test
    public void toUppercase_null() {
        assertThat(StringUtil.toUppercase(null), nullValue());
    }

    @Test
    public void toUppercase() {
        assertThat(StringUtil.toUppercase("   "), equalTo("   "));
        assertThat(StringUtil.toUppercase("AbC dEF"), equalTo("ABC DEF"));
    }

    @Test
    public void asCsvString_null() {
        assertThat(asCsvString(null), nullValue());
    }

    @Test
    public void asCsvString_empty() {
        assertThat(asCsvString(Collections.emptyList()), equalTo(""));
    }

    @Test
    public void asCsvString_single() {
        assertThat(asCsvString(singletonList("a")), equalTo("a"));
    }

    @Test
    public void asCsvString_multiple() {
        assertThat(asCsvString(asList("a", "b", "c")), equalTo("a,b,c"));
    }

    @Test
    public void asCsvStream_null() {
        assertThat(asCsvStream(null), nullValue());
    }

    @Test
    public void asCsvStream_multiple() {
        assertThat(asCsvStream("a,b,c").collect(Collectors.toList()), equalTo(asList("a", "b", "c")));
    }

    @Test
    public void asCsvList_null() {
        assertThat(asCsvList(null), nullValue());
    }

    @Test
    public void asCsvList_empty() {
        assertThat(asCsvList(""), equalTo(Collections.emptyList()));
    }

    @Test
    public void asCsvList_single() {
        assertThat(asCsvList("a"), equalTo(asList("a")));
    }

    @Test
    public void asCsvList_multiple() {
        assertThat(asCsvList("a,b,c"), equalTo(asList("a", "b", "c")));
    }

    @Test
    public void asCsvSet_null() {
        assertThat(asCsvSet(null), nullValue());
    }

    @Test
    public void asCsvSet_empty() {
        assertThat(asCsvSet(""), equalTo(Collections.emptySet()));
    }

    @Test
    public void asCsvSet_single() {
        assertThat(asCsvSet("a"), equalTo(new LinkedHashSet<>(asList("a"))));
    }

    @Test
    public void asCsvSet_multiple() {
        assertThat(asCsvSet("a,b,c,b,a"), equalTo(new LinkedHashSet(asList("a", "b", "c"))));
    }

    @Test
    public void asDelimitedString_Stream_Predicate_Delimiter_Function() {
        assertThat(asDelimitedString(asList("a", "b", "c").stream(), null,",", null), equalTo("a,b,c"));
        assertThat(asDelimitedString(asList("a", "b", "c").stream(), e -> !"b".equals(e), ",", null), equalTo("a,c"));
        assertThat(asDelimitedString(asList("a", "b", "c").stream(), null, ",", e -> e+"_txn"), equalTo("a_txn,b_txn,c_txn"));
        assertThat(asDelimitedString(asList("a", "b", "c").stream(), falsePredicate(), ",", e -> e+"_txn"), equalTo(""));
    }

    @Test
    public void asDelimitedString_Stream_Predicate_Delimiter() {
        assertThat(asDelimitedString(asList("a", "b", "c").stream(), truePredicate(), ","), equalTo("a,b,c"));
    }

    @Test
    public void asDelimitedString_Stream_Delimiter_Function() {
        assertThat(asDelimitedString(asList("a", "b", "c").stream(), ",", e -> "prefix_"+e), equalTo("prefix_a,prefix_b,prefix_c"));
    }

    @Test
    public void asDelimitedString_Stream_Delimiter() {
        assertThat(asDelimitedString(asList("a", "b", "c").stream(), ","), equalTo("a,b,c"));
    }

    @Test
    public void asDelimitedString_Array_Predicate_Delimiter_Function() {
        assertThat(asDelimitedString(new String[] {"a", "b", "c"}, null,",", null), equalTo("a,b,c"));
        assertThat(asDelimitedString(new String[] {"a", "b", "c"}, e -> !"b".equals(e), ",", null), equalTo("a,c"));
        assertThat(asDelimitedString(new String[] {"a", "b", "c"}, null, ",", e -> e+"_txn"), equalTo("a_txn,b_txn,c_txn"));
        assertThat(asDelimitedString(new String[] {"a", "b", "c"}, falsePredicate(), ",", e -> e+"_txn"), equalTo(""));
    }

    @Test
    public void asDelimitedString_Array_Predicate_Delimiter() {
        assertThat(asDelimitedString(new String[] {"a", "b", "c"}, truePredicate(), ","), equalTo("a,b,c"));
    }

    @Test
    public void asDelimitedString_Array_Delimiter_Function() {
        assertThat(asDelimitedString(new String[] {"a", "b", "c"}, ",", e -> "prefix_"+e), equalTo("prefix_a,prefix_b,prefix_c"));
    }

    @Test
    public void asDelimitedString_Array_Delimiter() {
        assertThat(asDelimitedString(new String[] {"a", "b", "c"}, ",", e -> "prefix_"+e), equalTo("prefix_a,prefix_b,prefix_c"));
    }

    @Test
    public void asDelimitedString_Collection_Predicate_Delimiter_Function() {
        assertThat(asDelimitedString(asList("a", "b", "c"), null,",", null), equalTo("a,b,c"));
        assertThat(asDelimitedString(asList("a", "b", "c"), e -> !"b".equals(e), ",", null), equalTo("a,c"));
        assertThat(asDelimitedString(asList("a", "b", "c"), null, ",", e -> e+"_txn"), equalTo("a_txn,b_txn,c_txn"));
        assertThat(asDelimitedString(asList("a", "b", "c"), falsePredicate(), ",", e -> e+"_txn"), equalTo(""));
    }

    @Test
    public void asDelimitedString_Collection_Predicate_Delimiter() {
        assertThat(asDelimitedString(asList("a", "b", "c"), truePredicate(), ","), equalTo("a,b,c"));
    }

    @Test
    public void asDelimitedString_Collection_Delimiter_Function() {
        assertThat(asDelimitedString(asList("a", "b", "c"), ",", e -> "prefix_"+e), equalTo("prefix_a,prefix_b,prefix_c"));
    }

    @Test
    public void asDelimitedString_Collection_Delimiter() {
        assertThat(asDelimitedString(asList("a", "b", "c"), ",", e -> "prefix_"+e), equalTo("prefix_a,prefix_b,prefix_c"));
    }
    @Test
    public void asDelimitedString_Map_Predicate_Delimiter_Delimiter_Function_Function() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("a", "a1");
        map.put("b", "b1");
        map.put("c", "c1");
        map.put("d", "d1");
        map.put("e", "e1");
        assertThat(asDelimitedString(map,
                                     (k, v) -> !("b".equals(k) || "d1".equals(v)),
                                     "&", "=", k -> k+"_k", v -> v+"_v"), equalTo("a_k=a1_v&c_k=c1_v&e_k=e1_v"));
        assertThat(asDelimitedString(map,
                                     null,
                                     "&", "=", k -> k+"_k", v -> v+"_v"), equalTo("a_k=a1_v&b_k=b1_v&c_k=c1_v&d_k=d1_v&e_k=e1_v"));
        assertThat(asDelimitedString(map,
                                     (k, v) -> !("b".equals(k) || "d1".equals(v)),
                                     "&", "=", null, null), equalTo("a=a1&c=c1&e=e1"));
        assertThat(asDelimitedString(map,
                                     null,
                                     "&", "=", null, null), equalTo("a=a1&b=b1&c=c1&d=d1&e=e1"));
    }

    @Test
    public void asDelimitedString_Map_Predicate_Delimiter_Delimiter() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("a", "a1");
        map.put("b", "b1");
        map.put("c", "c1");
        map.put("d", "d1");
        map.put("e", "e1");
        assertThat(asDelimitedString(map,
                                     (k, v) -> !("b".equals(k) || "d1".equals(v)),
                                     "&", "="), equalTo("a=a1&c=c1&e=e1"));
        assertThat(asDelimitedString(map,
                                     null,
                                     "&", "="), equalTo("a=a1&b=b1&c=c1&d=d1&e=e1"));
    }

    @Test
    public void asDelimitedString_Map_Delimiter_Delimiter_Function_Function() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("a", "a1");
        map.put("b", "b1");
        map.put("c", "c1");
        map.put("d", "d1");
        map.put("e", "e1");
        assertThat(asDelimitedString(map,
                                     "&", "=", k -> k+"_k", v -> v+"_v"), equalTo("a_k=a1_v&b_k=b1_v&c_k=c1_v&d_k=d1_v&e_k=e1_v"));
        assertThat(asDelimitedString(map,
                                     "&", "=", null, null), equalTo("a=a1&b=b1&c=c1&d=d1&e=e1"));
    }

    @Test
    public void asDelimitedString_Map_Delimiter_Delimiter() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("a", "a1");
        map.put("b", "b1");
        map.put("c", "c1");
        map.put("d", "d1");
        map.put("e", "e1");
        assertThat(asDelimitedString(map,"&", "="), equalTo("a=a1&b=b1&c=c1&d=d1&e=e1"));
    }

    @Test
    public void initCap_NullString() {
        assertThat(StringUtil.initCap(null), nullValue());
    }

    @Test
    public void initCap_EmptyString() {
        assertThat(StringUtil.initCap(""), equalTo(""));
    }

    @Test
    public void initCap_SingleCharacter() {
        assertThat(StringUtil.initCap("e"), equalTo("E"));
    }

    @Test
    public void initCap_SingleWord() {
        assertThat(StringUtil.initCap("word1"), equalTo("Word1"));
    }

    @Test
    public void initCap_SingleWord_ForceLowercase() {
        assertThat(StringUtil.initCap("woRD1", true), equalTo("Word1"));
    }

    @Test
    public void initCap_TwoWords() {
        assertThat(StringUtil.initCap("word1 word2"), equalTo("Word1 word2"));
    }

    @Test
    public void initCap_TwoWords_ForceLowercase() {
        assertThat(StringUtil.initCap("worD1 wOrd2", true), equalTo("Word1 word2"));
    }

    @Test
    public void repeat_null() {
        assertThat(StringUtil.repeat(null, 1), nullValue());
    }

    @Test
    public void repeat_ZeroTimes() {
        assertThat(StringUtil.repeat("a", 0), equalTo(""));
    }

    @Test
    public void repeat_Once() {
        assertThat(StringUtil.repeat("a", 1), equalTo("a"));
        assertThat(StringUtil.repeat("abc", 1), equalTo("abc"));
    }

    @Test
    public void repeat_MultipleTimes() {
        assertThat(StringUtil.repeat("a", 2), equalTo("aa"));
        assertThat(StringUtil.repeat("a", 5), equalTo("aaaaa"));

        assertThat(StringUtil.repeat("abc", 2), equalTo("abcabc"));
        assertThat(StringUtil.repeat("abc", 5), equalTo("abcabcabcabcabc"));
    }

    @Test
    public void nvlStr_null() {
        assertThat(StringUtil.nvlStr(null), nullValue());
    }

    @Test
    public void nvlStr_empty() {
        assertThat(StringUtil.nvlStr(""), nullValue());
        assertThat(StringUtil.nvlStr(" "), nullValue());
        assertThat(StringUtil.nvlStr("\t\r\n"), nullValue());
    }

    @Test
    public void isAlphanumeric() {
        for (int n=0; n < ALPHNUMERIC_CHARS.length(); n++) {
            assertThat(StringUtil.isAlphanumeric(ALPHNUMERIC_CHARS.charAt(n)), is(true));
        }
    }

    @Test
    public void isAsciiPrintableSpecial() {
        for (int n=0; n < ASCII_PRINTABLE_SPECIAL_CHARS.length(); n++) {
            assertThat(StringUtil.isAsciiPrintableSpecial(ASCII_PRINTABLE_SPECIAL_CHARS.charAt(n)), is(true));
        }
    }

    @Test
    public void randomChars_null() {
        assertThat(StringUtil.randomChars(null, 1, 10), nullValue());
    }

    @Test
    public void randomAlphaNumericChars_fixedLength() {
        final String randomStr = StringUtil.randomAlphaumericChars(10);
        assertThat(randomStr, notNullValue());
        assertThat(randomStr.length(), equalTo(10));
        for (int n=0; n < randomStr.length(); n++) {
            assertThat(StringUtil.isAlphanumeric(randomStr.charAt(n)), is(true));
        }
    }

    @Test
    public void randomAlphaNumericAsciiSpecialPrintableChars_fixedLength() {
        final String randomStr = StringUtil.randomAlphanumericAsciiPrintableSpecialChars(12);
        assertThat(randomStr, notNullValue());
        assertThat(randomStr.length(), equalTo(12));
        for (int n=0; n < randomStr.length(); n++) {
            assertThat(StringUtil.isAlphanumericOrAsciiPrintableSpecial(randomStr.charAt(n)), is(true));
        }
    }

    @Test
    public void isBlank_null() {
        assertThat(StringUtil.isBlank(null), is(true));
    }

    @Test
    public void isBlank_empty() {
        assertThat(StringUtil.isBlank(""), is(true));
    }

    @Test
    public void isBlank_allWhitespaceChars() {
        assertThat(StringUtil.isBlank(" "), is(true));
        assertThat(StringUtil.isBlank(" \t"), is(true));
        assertThat(StringUtil.isBlank(" \r"), is(true));
        assertThat(StringUtil.isBlank(" \n"), is(true));
        assertThat(StringUtil.isBlank(" \t\r\n "), is(true));
    }

    @Test
    public void isBlank_nonWhitespaceChars() {
        assertThat(StringUtil.isBlank(" H"), is(false));
        assertThat(StringUtil.isBlank(" Hello World! "), is(false));
    }

    @Test
    public void isBlank_nullToString() {
        assertThat(StringUtil.isBlank(new Object() {
            public String toString() {
                return null;
            }
        }), is(true));
    }
}