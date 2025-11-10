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

package org.beanplanet.core.net;

import org.beanplanet.core.io.resource.DataUrlResource;
import org.beanplanet.core.net.http.HttpHeaders;
import org.beanplanet.core.net.http.MediaType;
import org.beanplanet.core.net.http.MediaTypes;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class DataUrlConnection extends URLConnection {

    private static final Charset DEFAULT_CONTENT_CHARSET = StandardCharsets.US_ASCII;
    private static final MediaType DEFAULT_MEDIATYPE = MediaTypes.Text.PLAIN;

    private final DataUrlResource dataUrlResource;

    public DataUrlConnection(final URL url) throws MalformedURLException {
        super(url);
        this.dataUrlResource = new DataUrlResource(url);
    }

    private static boolean isText(String mediatype) {
        return mediatype != null && (mediatype.startsWith("text/") || mediatype.endsWith("+xml"));
    }

    @Override
    public void connect() {
        this.connected = true;
    }

    @Override
    public InputStream getInputStream() {
        return dataUrlResource.getInputStream();
    }

    @Override
    public String getContentType() {
        return dataUrlResource.getMediaTypeOrDefault().getCanonicalForm();
    }

    @Override
    public String getHeaderField(String name) {
        if (HttpHeaders.CONTENT_LENGTH.equalsIgnoreCase(name)) {
            return String.valueOf(dataUrlResource.getContentLength());
        }

        return null;
    }
}
