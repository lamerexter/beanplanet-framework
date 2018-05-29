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
