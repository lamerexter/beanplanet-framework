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
import org.beanplanet.core.util.CharArrayBuffer;

public interface HeaderValueParser {
    HeaderValueElement[] parseElements(CharArrayBuffer var1, ParserCursor var2);

    HeaderValueElement parseHeaderElement(CharArrayBuffer var1, ParserCursor var2);

    NameValue<String>[] parseParameters(CharArrayBuffer var1, ParserCursor var2);

    NameValue<String> parseNameValuePair(CharArrayBuffer var1, ParserCursor var2);
}
