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
 * An implementation of a logger that logs its messages to the system console (<code>System.out</code>).
 * <p>
 * It should be noted that logging substantial numbers of messages to <i>standard out</i> may degrade system
 * performance. This implementation is designed to be used only when in a buffered or filtered context to avoid
 * degrading system performance.
 * 
 * @author Gary Watson
 * @since 2001
 */
public class ConsoleLogger extends OutputStreamLogger {
   /**
   * Creates the console logger.
   */
   public ConsoleLogger() {
   super(System.out);
   }
}
