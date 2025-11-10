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

package org.beanplanet.core.net.http;

/**
 * Industry standard media types, as defined by the <a href="https://www.iana.org/assignments/media-types/media-types.xhtml">IANA media types database</a>,
 */
public interface MediaTypes {
    MediaType ALL = new MediaType("*/*");

    interface Application {
        MediaType ALL = new MediaType("application/*");

        MediaType ATOM_XML = new MediaType("application/atom+xml");
        MediaType FORM_URLENCODED = new MediaType("application/x-www-form-urlencoded");
        MediaType JSON = new MediaType("application/json");
        MediaType OCTET_STREAM = new MediaType("application/octet-stream");
        MediaType PDF = new MediaType("application/pdf");
        MediaType XHTML_XML = new MediaType("application/xhtml+xml");
        MediaType XML = new MediaType("application/xml");
        MediaType YAML = new MediaType("application/yaml");  // Defined by RFC 9512
    }

    interface Multipart {
        MediaType FORM_DATA = new MediaType("multipart/form-data");
        MediaType MIXED = new MediaType("multipart/mixed");
        MediaType RELATED = new MediaType("multipart/related");
    }

    interface Text {
        MediaType ALL = new MediaType("text/*");

        MediaType HTML = new MediaType("text/html");
        MediaType PLAIN = new MediaType("text/plain");
        MediaType XML = new MediaType("text/xml");
    }

    interface Image {
        MediaType ALL = new MediaType("image/*");

        MediaType BMP = new MediaType("image/bmp");
        MediaType GIF = new MediaType("image/gif");
        MediaType JPEG = new MediaType("image/jpeg");
        MediaType PNG = new MediaType("image/png");
        MediaType SVG = new MediaType("image/svg+xml");
        MediaType TIFF = new MediaType("image/tiff");
    }

    MediaType[] XML_FORMAT_MEDIA_TYPES = {Application.ATOM_XML, Application.XHTML_XML, Application.XML, Text.XML, Image.SVG};

    static MediaType allForType(final String type) {
        return new MediaType(type, "*");
    }

    static boolean isText(String mediaType) {
        return mediaType != null
                && (mediaType.startsWith("text/")
                || Application.JSON.getName().equals(mediaType)
                || Application.XML.getName().equals(mediaType)
                || mediaType.endsWith("+xml"));
    }


}
