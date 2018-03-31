/******************************************************************************* 
 * Copyright 2004-2009 BeanPlanet Limited
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
 ******************************************************************************/
package org.beanplanet.core.lang;


/**
 * An implementation of a handler of exceptions which simply re-throws
 * the exceptions.
 * 
 * @author Gary Watson
 *
 */
public class ErrorPropagatingExceptionHandler implements ExceptionHandler {
   /**
    * Called to determine whether, under the specified context, the exception handler
    * can process the given exception.
    * 
    * <p>This implementation can handle any exception simple by re-throwing it.
    * 
    * @param ex the exception to be handled.
    * @return true if the handler can attempt deal with the exception fully via a subsequent call to
    * <code>{@link #handleException(Object, Exception)}</code>, false otherwise. Despite a true return value
    * from a call to this method, an implementation is free to subsequently re-throw the exception or another
    * exception when any subsequent call to the <code>{@link #handleException(Object, Exception)}</code> is made. 
    */
//   * @param context the context under which the exception was thrown; usually related to the
//   * activity which caused the exception.
//   public boolean canHandleException(E context, Exception cause);
    public boolean canHandleException(Throwable cause) {
       return true;
    }

   /**
    * Called to handle an exception thrown under the specified context.
    * 
    * <p>Before this method is invoked, the
    * <code>{@link #canHandleException(Object, Exception)}</code> method should first be called to determine
    * whether the handler is able to process the given exception.</p>
    * 
    * @param ex the exception to be handled.
    * @return a value to be returned instead of the given exception being thrown.
    */
//   * @param context the context under which the exception was thrown; usually related to the
//   * activity which caused the exception.
//   public Object handleException(E context, Exception cause) throws Exception;
   public Object handleException(Throwable cause) throws Throwable {
      throw cause;
   }
}
