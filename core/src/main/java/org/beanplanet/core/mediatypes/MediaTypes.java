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

package org.beanplanet.core.mediatypes;

/**
 * Industry standard media types, as defined by the <a href="https://www.iana.org/assignments/media-types/media-types.xhtml">IANA media types database</a>,
 */
public interface MediaTypes {
    interface Application {
        MediaType ATOM_XML = new SimpleMediaType("application/atom+xml");
        MediaType JSON = new SimpleMediaType("application/json");
        MediaType OCTET_STREAM = new SimpleMediaType("application/octet-stream");
        MediaType XHTML_XML = new SimpleMediaType("application/xhtml+xml");
        MediaType XML = new SimpleMediaType("application/xml");
    }

    interface Text {
        MediaType HTML = new SimpleMediaType("text/html");
        MediaType PLAIN = new SimpleMediaType("text/plain");
        MediaType XML = new SimpleMediaType("text/xml");
    }

    interface Image {
        MediaType BMP = new SimpleMediaType("image/bmp");
        MediaType GIF = new SimpleMediaType("image/gif");
        MediaType JPEG = new SimpleMediaType("image/jpeg");
        MediaType PNG = new SimpleMediaType("image/png");
        MediaType SVG = new SimpleMediaType("image/svg+xml");
        MediaType TIFF = new SimpleMediaType("image/tiff");
    }

    MediaType[] XML_FORMAT_MEDIA_TYPES = { Application.ATOM_XML, Application.XHTML_XML, Application.XML, Text.XML, Image.SVG };
}
