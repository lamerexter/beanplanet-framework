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
package org.beanplanet.core.lang;


/**
 * Defines handler of exceptions which has the capability to selectively choose or filter the
 * handled exception types..
 * 
 * @author Gary Watson
 */
public interface ExceptionHandler {

   /**
    * Called to determine whether, under the specified context, the exception handler
    * can process the given exception.
    * 
    * @param ex the exception to be handled.
    * @return true if the handler can attempt deal with the exception fully via a subsequent call to
    * <code>{@link #handleException(Throwable)}</code>, false otherwise. Despite a true return value
    * from a call to this method, an implementation is free to subsequently re-throw the exception or another
    * exception when any subsequent call to the <code>{@link #handleException(Throwable)}</code> is made.
    */
    boolean canHandleException(Throwable ex);

   /**
    * Called to handle an exception thrown under the specified context.
    * 
    * <p>Before this method is invoked, the
    * <code>{@link #canHandleException(Throwable)}</code> method should first be called to determine
    * whether the handler is able to process the given exception.</p>
    * 
    * @param ex the exception to be handled.
    * @return a value to be returned instead of the given exception being thrown.
    */
   Object handleException(Throwable ex) throws Throwable;
}
