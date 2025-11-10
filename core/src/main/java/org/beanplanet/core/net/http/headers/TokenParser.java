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

import org.beanplanet.core.util.CharArrayBuffer;

import java.util.BitSet;

public class TokenParser {
    public static final char CR = '\r';
    public static final char LF = '\n';
    public static final char SP = ' ';
    public static final char HT = '\t';
    public static final char DQUOTE = '"';
    public static final char ESCAPE = '\\';
    public static final TokenParser INSTANCE = new TokenParser();

    public TokenParser() {
    }

    public static BitSet INIT_BITSET(int... b) {
        BitSet bitset = new BitSet();
        int[] arr$ = b;
        int len$ = b.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            int aB = arr$[i$];
            bitset.set(aB);
        }

        return bitset;
    }

    public static boolean isWhitespace(char ch) {
        return ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n';
    }

    public String parseToken(CharArrayBuffer buf, ParserCursor cursor, BitSet delimiters) {
        StringBuilder dst = new StringBuilder();
        boolean whitespace = false;

        while(!cursor.atEnd()) {
            char current = buf.charAt(cursor.getPos());
            if (delimiters != null && delimiters.get(current)) {
                break;
            }

            if (isWhitespace(current)) {
                this.skipWhiteSpace(buf, cursor);
                whitespace = true;
            } else {
                if (whitespace && dst.length() > 0) {
                    dst.append(' ');
                }

                this.copyContent(buf, cursor, delimiters, dst);
                whitespace = false;
            }
        }

        return dst.toString();
    }

    public String parseValue(CharArrayBuffer buf, ParserCursor cursor, BitSet delimiters) {
        StringBuilder dst = new StringBuilder();
        boolean whitespace = false;

        while(!cursor.atEnd()) {
            char current = buf.charAt(cursor.getPos());
            if (delimiters != null && delimiters.get(current)) {
                break;
            }

            if (isWhitespace(current)) {
                this.skipWhiteSpace(buf, cursor);
                whitespace = true;
            } else if (current == '"') {
                if (whitespace && dst.length() > 0) {
                    dst.append(' ');
                }

                this.copyQuotedContent(buf, cursor, dst);
                whitespace = false;
            } else {
                if (whitespace && dst.length() > 0) {
                    dst.append(' ');
                }

                this.copyUnquotedContent(buf, cursor, delimiters, dst);
                whitespace = false;
            }
        }

        return dst.toString();
    }

    public void skipWhiteSpace(CharArrayBuffer buf, ParserCursor cursor) {
        int pos = cursor.getPos();
        int indexFrom = cursor.getPos();
        int indexTo = cursor.getUpperBound();

        for(int i = indexFrom; i < indexTo; ++i) {
            char current = buf.charAt(i);
            if (!isWhitespace(current)) {
                break;
            }

            ++pos;
        }

        cursor.updatePos(pos);
    }

    public void copyContent(CharArrayBuffer buf, ParserCursor cursor, BitSet delimiters, StringBuilder dst) {
        int pos = cursor.getPos();
        int indexFrom = cursor.getPos();
        int indexTo = cursor.getUpperBound();

        for(int i = indexFrom; i < indexTo; ++i) {
            char current = buf.charAt(i);
            if (delimiters != null && delimiters.get(current) || isWhitespace(current)) {
                break;
            }

            ++pos;
            dst.append(current);
        }

        cursor.updatePos(pos);
    }

    public void copyUnquotedContent(CharArrayBuffer buf, ParserCursor cursor, BitSet delimiters, StringBuilder dst) {
        int pos = cursor.getPos();
        int indexFrom = cursor.getPos();
        int indexTo = cursor.getUpperBound();

        for(int i = indexFrom; i < indexTo; ++i) {
            char current = buf.charAt(i);
            if (delimiters != null && delimiters.get(current) || isWhitespace(current) || current == '"') {
                break;
            }

            ++pos;
            dst.append(current);
        }

        cursor.updatePos(pos);
    }

    public void copyQuotedContent(CharArrayBuffer buf, ParserCursor cursor, StringBuilder dst) {
        if (!cursor.atEnd()) {
            int pos = cursor.getPos();
            int indexFrom = cursor.getPos();
            int indexTo = cursor.getUpperBound();
            char current = buf.charAt(pos);
            if (current == '"') {
                ++pos;
                ++indexFrom;
                boolean escaped = false;

                for(int i = indexFrom; i < indexTo; ++pos) {
                    current = buf.charAt(i);
                    if (escaped) {
                        if (current != '"' && current != '\\') {
                            dst.append('\\');
                        }

                        dst.append(current);
                        escaped = false;
                    } else {
                        if (current == '"') {
                            ++pos;
                            break;
                        }

                        if (current == '\\') {
                            escaped = true;
                        } else if (current != '\r' && current != '\n') {
                            dst.append(current);
                        }
                    }

                    ++i;
                }

                cursor.updatePos(pos);
            }
        }
    }
}
