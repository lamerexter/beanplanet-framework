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
