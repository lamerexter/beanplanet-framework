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

package org.beanplanet.core.net.http.headers;

import org.beanplanet.core.models.NameValue;
import org.beanplanet.core.models.SimpleNameValue;
import org.beanplanet.core.util.CharArrayBuffer;

import java.util.ArrayList;
import java.util.BitSet;

public class DefaultHeaderValueParser implements HeaderValueParser{
    private static final char PARAM_DELIMITER = ';';
    private static final char ELEM_DELIMITER = ',';
    private static final BitSet TOKEN_DELIMS = TokenParser.INIT_BITSET(new int[]{61, 59, 44});
    private static final BitSet VALUE_DELIMS = TokenParser.INIT_BITSET(new int[]{59, 44});
    private final TokenParser tokenParser;

    private static final DefaultHeaderValueParser instance;

    static {
        instance = new DefaultHeaderValueParser();
    }

    public static DefaultHeaderValueParser getInstance() {
        return instance;
    }

    public DefaultHeaderValueParser() {
        this.tokenParser = TokenParser.INSTANCE;
    }

    public static HeaderValueElement[] parseElements(String value, HeaderValueParser parser) {
        CharArrayBuffer buffer = new CharArrayBuffer(value.length());
        buffer.append(value);
        ParserCursor cursor = new ParserCursor(0, value.length());
        return ((HeaderValueParser)(parser != null ? parser : getInstance())).parseElements(buffer, cursor);
    }

    public HeaderValueElement[] parseElements(CharArrayBuffer buffer, ParserCursor cursor) {
        ArrayList elements = new ArrayList();

        while(true) {
            HeaderValueElement element;
            do {
                if (cursor.atEnd()) {
                    return (HeaderValueElement[])elements.toArray(new HeaderValueElement[elements.size()]);
                }

                element = this.parseHeaderElement(buffer, cursor);
            } while(element.getName().length() == 0 && element.getValue() == null);

            elements.add(element);
        }
    }

    public static HeaderValueElement parseHeaderValueElement(String value, HeaderValueParser parser) {
        CharArrayBuffer buffer = new CharArrayBuffer(value.length());
        buffer.append(value);
        ParserCursor cursor = new ParserCursor(0, value.length());
        return ((HeaderValueParser)(parser != null ? parser : getInstance())).parseHeaderElement(buffer, cursor);
    }

    public HeaderValueElement parseHeaderElement(CharArrayBuffer buffer, ParserCursor cursor) {
        NameValue<String> nvp = this.parseNameValuePair(buffer, cursor);
        NameValue<String>[] params = null;
        if (!cursor.atEnd()) {
            char ch = buffer.charAt(cursor.getPos() - 1);
            if (ch != ',') {
                params = this.parseParameters(buffer, cursor);
            }
        }

        return this.createHeaderValueElement(nvp.getName(), nvp.getValue(), params);
    }

    protected HeaderValueElement createHeaderValueElement(String name, String value, NameValue<String>[] params) {
        return new SimpleHeaderValueElement(name, value, params);
    }

    public static NameValue<String>[] parseParameters(String value, HeaderValueParser parser) {
        CharArrayBuffer buffer = new CharArrayBuffer(value.length());
        buffer.append(value);
        ParserCursor cursor = new ParserCursor(0, value.length());
        return ((HeaderValueParser)(parser != null ? parser : getInstance())).parseParameters(buffer, cursor);
    }

    public NameValue<String>[] parseParameters(CharArrayBuffer buffer, ParserCursor cursor) {
        this.tokenParser.skipWhiteSpace(buffer, cursor);
        ArrayList params = new ArrayList();

        while(!cursor.atEnd()) {
            NameValue<String> param = this.parseNameValuePair(buffer, cursor);
            params.add(param);
            char ch = buffer.charAt(cursor.getPos() - 1);
            if (ch == ',') {
                break;
            }
        }

        return (NameValue<String>[])params.toArray(new NameValue[params.size()]);
    }

    public static NameValue<String> parseNameValuePair(String value, HeaderValueParser parser) {
        CharArrayBuffer buffer = new CharArrayBuffer(value.length());
        buffer.append(value);
        ParserCursor cursor = new ParserCursor(0, value.length());
        return ((HeaderValueParser)(parser != null ? parser : getInstance())).parseNameValuePair(buffer, cursor);
    }

    public NameValue<String> parseNameValuePair(CharArrayBuffer buffer, ParserCursor cursor) {
        String name = this.tokenParser.parseToken(buffer, cursor, TOKEN_DELIMS);
        if (cursor.atEnd()) {
            return createNameValuePair(name, null);
        } else {
            int delim = buffer.charAt(cursor.getPos());
            cursor.updatePos(cursor.getPos() + 1);
            if (delim != '=') {
                return createNameValuePair(name, null);
            } else {
                String value = this.tokenParser.parseValue(buffer, cursor, VALUE_DELIMS);
                if (!cursor.atEnd()) {
                    cursor.updatePos(cursor.getPos() + 1);
                }

                return this.createNameValuePair(name, value);
            }
        }
    }

    /** @deprecated */
    @Deprecated
    public NameValue<String> parseNameValuePair(CharArrayBuffer buffer, ParserCursor cursor, char[] delimiters) {
        BitSet delimSet = new BitSet();
        if (delimiters != null) {
            char[] arr$ = delimiters;
            int len$ = delimiters.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                char delimiter = arr$[i$];
                delimSet.set(delimiter);
            }
        }

        delimSet.set(61);
        String name = this.tokenParser.parseToken(buffer, cursor, delimSet);
        if (cursor.atEnd()) {
            return createNameValuePair(name, null);
        } else {
            int delim = buffer.charAt(cursor.getPos());
            cursor.updatePos(cursor.getPos() + 1);
            if (delim != '=') {
                return this.createNameValuePair(name, (String)null);
            } else {
                delimSet.clear(61);
                String value = this.tokenParser.parseValue(buffer, cursor, delimSet);
                if (!cursor.atEnd()) {
                    cursor.updatePos(cursor.getPos() + 1);
                }

                return this.createNameValuePair(name, value);
            }
        }
    }

    protected NameValue<String> createNameValuePair(String name, String value) {
        return new SimpleNameValue<>(name, value);
    }
}
