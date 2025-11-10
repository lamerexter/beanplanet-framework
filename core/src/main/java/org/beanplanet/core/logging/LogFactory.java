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
