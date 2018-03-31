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

/**
 * Definition of an <a href="http://en.wikipedia.org/wiki/Abstract_factory_pattern">Abstract Factory</a> and key interface for creating families of loggers
 * suitable for logging messages. Use the returned loggers to log messages within your application; the
 * <a href="http://www.beanplanet.org">Beanplanet Framework</a> itself
 * uses this logging approach in order to remain independent of specific logging implementations.
 * 
 * <p>Analogous to popular logging frameworks such as:
 * <ul>
 * <li><a href="http://commons.apache.org/logging/">Apache Commons Logging</a></li>
 * <li><a href="http://logging.apache.org/log4j/index.html">Apache Log4J</a></li>
 * </ul>
 * The <a href="http://www.beanplanet.org">Beanplanet Framework</a> provides adapters to those
 * popular implementations (see the <code>log4j</code> and <code>commons</code> sub-packages).
 * </p>
 */
public interface LogFactory {
   /**
    * Creates a severity-based logger for the given log context.  Most clients should use their class or
    * their fully qualified class name as the logging context.
    * 
    * @param context the logging context: usually the class or class name of the caller.
    * @return a severity-based logger or log message end-point configured for the caller's context
    */
   Logger loggerFor(Object context);
}
