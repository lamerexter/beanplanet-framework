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
import static org.junit.Assert.*;

public class HtmlUtilTest {
   @Test
   public void encodeNullReturnsNull() {
      assertNull(HtmlUtil.encode(null));
   }
   
   @Test
   public void encodeCommonAsciiEntities() {
      assertEquals("&#39;", HtmlUtil.encode("\'"));
      assertEquals("&quot;", HtmlUtil.encode("\""));
      assertEquals("&amp;", HtmlUtil.encode("&"));
      assertEquals("&lt;", HtmlUtil.encode("<"));
      assertEquals("&gt;", HtmlUtil.encode(">"));
      
      assertEquals("Marcus &amp; Spencious", HtmlUtil.encode("Marcus & Spencious"));
      assertEquals("&lt;not really an XML tag&gt;", HtmlUtil.encode("<not really an XML tag>"));
   }

   @Test
   public void encodeAdhocEntities() {
      assertEquals("&rsquo;&ldquo;&rdquo;&euro;&trade;&pound;&yen;&copy;&reg;", HtmlUtil.encode("\u2019\u201C\u201D\u20AC\u2122\u00A3\u00A5\u00A9\u00AE"));
   }
}
