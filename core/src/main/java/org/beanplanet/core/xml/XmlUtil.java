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

package org.beanplanet.core.xml;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * A utility class for XML related activities.
 *
 * @author Gary Watson
 */
public class XmlUtil implements XmlConstants
{
    private static final int HIGHEST_ESCAPABLE_CHARACTER = '>';

    private static char charToXMLCharacterEntityTable[][] = new char[HIGHEST_ESCAPABLE_CHARACTER + 1][];

    static
    {
        // initialise character to XML Character Entity String table
        // This lookup table has been taken from the W3C at
        // http://www.w3.org/TR/REC-xml/ section
        // 4.5, Predefined Entities.
        charToXMLCharacterEntityTable['<'] = "&lt;".toCharArray();
        charToXMLCharacterEntityTable['>'] = "&gt;".toCharArray();
        charToXMLCharacterEntityTable['&'] = "&amp;".toCharArray();
        charToXMLCharacterEntityTable['"'] = "&quot;".toCharArray();
        charToXMLCharacterEntityTable['\''] = "&apos;".toCharArray();
    }

    /**
     * Escapes the character entities in an XML String
     *
     * @param s
     *           String possibly containing unescaped entities
     * @return String with all XML character entities escaped
     */
    public static String escapeEntities(String s)
    {
        if (s == null) return null;

        char arrayBuffer[] = s.toCharArray();
        StringBuffer escapedBuffer = null;
        int startIdx = 0;
        int length = s.length();

        for (int n = 0; n < length; n++)
        {
            char ch = arrayBuffer[n];

            // Ensure we only deal with characters which may be escapable
            if (ch <= HIGHEST_ESCAPABLE_CHARACTER)
            {
                char xmlEntity[] = charToXMLCharacterEntityTable[ch];
                if (xmlEntity != null)
                {
                    // Allocate a buffer tyo hold escaped string (original +
                    // entities), if this is the first escape found
                    if (startIdx == 0)
                    {
                        escapedBuffer = new StringBuffer(length + 6);
                    }

                    // Add unescaped portion
                    if (startIdx < n)
                    {
                        escapedBuffer.append(arrayBuffer, startIdx, n - startIdx);
                    }

                    // Set index to include the current character that was escaped
                    // and
                    // add the escaped portion.
                    startIdx = n + 1;
                    escapedBuffer.append(xmlEntity);
                }
            }
        }

        if (startIdx == 0)
        {
            // No escaping was performed so simply return the original string.
            return s;
        }

        if (startIdx < length)
        {
            // Append rest of unescaped portion
            escapedBuffer.append(arrayBuffer, startIdx, length - startIdx);
        }

        return escapedBuffer.toString();
    }

    public static List<Node> toList(NodeList nl)
    {
        ArrayList<Node> result = new ArrayList<Node>(nl.getLength());
        for (int n = 0; n < nl.getLength(); n++)
        {
            result.add(nl.item(n));
        }

        return result;
    }
}
