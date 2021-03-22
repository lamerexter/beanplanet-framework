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

public interface MediaTypes {
    interface Application {
        MediaType JSON = new DefaultMediaType("application/json");
    }

    interface Text {
        MediaType HTML = new DefaultMediaType("text/html");
        MediaType PLAIN = new DefaultMediaType("text/plain");
    }

    interface Image {
        MediaType BMP = new DefaultMediaType("image/bmp");
        MediaType GIF = new DefaultMediaType("image/gif");
        MediaType JPEG = new DefaultMediaType("image/jpeg");
        MediaType PNG = new DefaultMediaType("image/png");
        MediaType SVG = new DefaultMediaType("image/svg+xml");
        MediaType TIFF = new DefaultMediaType("image/tiff");
    }
}
