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

package org.beanplanet.core.logging;

public class BeanpanetLoggerFactory implements LogFactory {
    private static LogFactory systemLoggerFactory = determineSystemLoggerFactory();

    public static LogFactory getSystemLoggerFactory() {
        return systemLoggerFactory;
    }

    public static void setSystemLoggerFactory(LogFactory loggerFactory) {
        systemLoggerFactory = loggerFactory;
    }

    public static Logger getSystemloggerFor(Object context) {
        return getSystemLoggerFactory().loggerFor(context);
    }

    private static LogFactory determineSystemLoggerFactory() {
        return new LogFactory() {
            @Override
            public Logger loggerFor(Object context) {
                return new ConsoleLogger();
            }
        };
    }

    @Override
    public Logger loggerFor(Object context) {
        return getSystemLoggerFactory().loggerFor(context);
    }
}