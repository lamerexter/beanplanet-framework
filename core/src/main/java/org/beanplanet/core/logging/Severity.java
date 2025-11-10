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
 * Represents the severity of a logged message:
 * <dl>
 * <dt>DEBUG</dt><dd>the finest level of logging, used for outputting the flow of execution to aid debugging.</dd>
 * <dt>INFO</dt><dd>informational messages, indicating progress and data values.</dd>
 * <dt>WARNING</dt><dd>something unexpected has happened but is recoverable and the flow is able to proceed.</dd>
 * <dt>ERROR</dt><dd>something unexpected has happened and the <em>current</em> flow of execution is unable to proceed.</dd>
 * <dt>FATAL</dt><dd>something major and unexpected has happened, the current process may be affected now or in the future, and the flow of execution is not able to proceed
 * from this point on until the issue is corrected.</dd>
 * </dl>
 * 
 * <p>Originally written as a type-safe enum (JSE 1.2+) and now refactored to use JSE 1.5+ enumerated types proper.</p>
 * 
 * @author Gary Watson
 * @since 12th August, 2001
 *
 */
public enum Severity {
   DEBUG, INFO, WARNING, ERROR, FATAL;
}
