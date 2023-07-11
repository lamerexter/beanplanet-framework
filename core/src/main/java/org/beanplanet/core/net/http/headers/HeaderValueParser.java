package org.beanplanet.core.net.http.headers;

import org.beanplanet.core.models.NameValue;
import org.beanplanet.core.util.CharArrayBuffer;

public interface HeaderValueParser {
    HeaderValueElement[] parseElements(CharArrayBuffer var1, ParserCursor var2);

    HeaderValueElement parseHeaderElement(CharArrayBuffer var1, ParserCursor var2);

    NameValue<String>[] parseParameters(CharArrayBuffer var1, ParserCursor var2);

    NameValue<String> parseNameValuePair(CharArrayBuffer var1, ParserCursor var2);
}
