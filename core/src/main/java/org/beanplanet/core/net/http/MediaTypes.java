/*
 *  MIT Licence:
 *
 *  Copyright (C) 2018 Beanplanet Ltd
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
