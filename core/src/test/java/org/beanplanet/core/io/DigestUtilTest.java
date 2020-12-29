/*
 *  MIT Licence:
 *
 *  Copyright (C) 2020 Beanplanet Ltd
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

package org.beanplanet.core.io;

import org.beanplanet.core.io.resource.ByteArrayResource;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.beanplanet.core.io.DigestUtil.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class DigestUtilTest {

    @Test
    public void sha256HashByteStreamToHexadecimal_String() {
        assertThat(sha256HashByteStreamToHexadecimal("Hello World"),
                   equalTo(hashByteStreamToHexadecimal(
                       new ByteArrayResource("Hello World".getBytes(StandardCharsets.UTF_8)), "SHA-256"))
        );
    }

}