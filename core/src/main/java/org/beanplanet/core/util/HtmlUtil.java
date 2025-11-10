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

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for everything HTML related.
 * 
 * @author Gary Watson
 */
public class HtmlUtil {
   private static final Map<Character, String> adhocCharactersLookupTable = new HashMap<Character, String>();
   private static final String[] lowerCharacterEntityCodes = new String[256];

   static {
      // Populate well-known character entities lookup table.
      lowerCharacterEntityCodes['\''] = "&#39;";   // single quote
      lowerCharacterEntityCodes['\"'] = "&quot;";  // double quote
      lowerCharacterEntityCodes['&']  = "&amp;";   // ampersand
      lowerCharacterEntityCodes['<']  = "&lt;";    // lower than
      lowerCharacterEntityCodes['>']  = "&gt;";    // greater than

      // Other higher-byte characters
      adhocCharactersLookupTable.put('\u2019', "&rsquo;");
      adhocCharactersLookupTable.put('\u201C', "&ldquo;");
      adhocCharactersLookupTable.put('\u201D', "&rdquo;");
      adhocCharactersLookupTable.put('\u20AC', "&euro;");
      adhocCharactersLookupTable.put('\u2122', "&trade;");
      adhocCharactersLookupTable.put('\u00A3', "&pound;");
      adhocCharactersLookupTable.put('\u00A5', "&yen;");
      adhocCharactersLookupTable.put('\u00A9', "&copy;");
      adhocCharactersLookupTable.put('\u00AE', "&reg;");
   }
   
   /**
    * Character entity encodes a given character sequence.
    * 
    * @param charSeq the character sequence to be encoded, which may be null.
    * @return null if the given character sequence was null or the specified character sequence with key characters escaped using their equivalent HTML
    * character entity codes.
    */
   public static String characterEntityEncode(CharSequence charSeq) {
      if (charSeq == null) {
         return null;
      }
      
      int length = charSeq.length();
      StringBuilder s = new StringBuilder();
      for (int n=0; n < length; n++) {
         char ch = charSeq.charAt(n);
         if (ch < lowerCharacterEntityCodes.length && lowerCharacterEntityCodes[ch] != null) {
            s.append(lowerCharacterEntityCodes[ch]);
         } else {
            String entity = adhocCharactersLookupTable.get(ch);
            if (entity != null) {
               s.append(entity);
            } else {
               s.append((char)ch);
            }
         }
      }
      return s.toString();
   }

   /**
    * Entity encodes a given character sequence.
    * 
    * @param charSeq the character sequence to be encoded, which may be null.
    * @return null if the given character sequence was null or the specified character sequence with key characters escaped using their equivalent HTML
    * character entity codes.
    * @see #characterEntityEncode(CharSequence)
    */
   public static String encode(CharSequence charSeq) {
      return characterEntityEncode(charSeq);
   }
   
   public static void main(String...args) {
      System.out.println(HtmlUtil.characterEntityEncode(args[0]));
   }
}
