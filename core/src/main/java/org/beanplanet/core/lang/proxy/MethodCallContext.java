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
package org.beanplanet.core.lang.proxy;

import org.beanplanet.core.lang.ExceptionHandler;

import java.lang.reflect.Method;

/**
 * The context for a method call. Used during the interception of method calls and subsequent notification of before and
 * after method call advice.
 * 
 * @author Gary Watson
 */
public class MethodCallContext {
    /** The method to be invoked on the target object. */
    protected Method method;

    protected Object target;

    /** The parameters to be supplied to the method invoked on the target object. */
    protected Object parameters[];

    /**
    * An handler of exceptions thrown during method invocation.
    */
    protected ExceptionHandler exceptionHandler;

    /**
    * Constructs a method call context.
    *
    * @param method the method to be called
    * @param parameters an parameters the method expects
    */
    public MethodCallContext(Method method, Object parameters[]) {
      this(null, method, parameters);
    }

    /**
    * Constructs a method call context.
    *
    * @param method the method to be called
    * @param parameters an parameters the method expects
    */
    public MethodCallContext(Object target, Method method, Object parameters[]) {
      this.target = target;
      this.method = method;
      this.parameters = parameters;
    }


    /**
    * Returns the handler of exceptions thrown during method invocation.
    *
    * @return the exceptionHandler the configured handler of exceptions.
    */
    public ExceptionHandler getExceptionHandler() {
      return exceptionHandler;
    }

    /**
    * Sets the handler of exceptions thrown during method invocation.
    *
    * @param exceptionHandler the configured handler of exceptions.
    */
    public void setExceptionHandler(ExceptionHandler exceptionHandler) {
      this.exceptionHandler = exceptionHandler;
    }

    /**
    * Returns the method to be invoked on the target object.
    *
    * @return the method to be called on the target object.
    */
    public Method getMethod() {
      return method;
    }

    /**
    * Sets the method to be invoked on the target object.
    *
    * @param method the method to be called on the target object.
    */
    public void setMethod(Method method) {
      this.method = method;
    }

    public Object getTarget() {
      return target;
    }

    public void setTarget(Object target) {
      this.target = target;
    }

    /**
    * Returns the parameters to be supplied to the method invoked on the target object.
    *
    * @return any parameters to supply to the called method; both the parameters themselves or the array of parameters
    *         itself may be null.
    */
    public Object[] getParameters() {
      return parameters;
    }

    /**
    * Sets the parameters to be supplied to the method invoked on the target object.
    *
    * @param parameters the parameters to supply to the called method; both the parameters themselves or the array of
    *        parameters itself may be null.
    */
    public void setParameters(Object parameters[]) {
      this.parameters = parameters;

    }

    /**
    * Can be used by interceptors to invoke the configured method, with the configured parameters on the configured
    * target object.
    * <p>
    * This implementation simply invokes {@link #invokeOnTarget(Object)} with the configured target passed in the constructor.
    * <p>
    *
    * @return any returned value from the method called, or <code>null</code> if the method returned <code>null</code>
    *         or the return type of the method is <code>void</code>.
    * @throws Throwable if an exception occurs invoking the underlying method
    * @see #invokeOnTarget(Object)
    */
    public Object invokeOnTarget() throws Throwable {
        return invokeOnTarget(getTarget());
    }

    /**
    * Can be used by interceptors to invoke the configured method, with the configured parameters on the configured
    * target object.
    * <p>
    * This implementation declares and throws the corresponding language and reflection exceptions associated with
    * dynamic method invocations.
    * <p>
    *
    * @param target the target of the method call, which must be type compatible with the proxy type.
    * @return any returned value from the method called, or <code>null</code> if the method returned <code>null</code>
    *         or the return type of the method is <code>void</code>.
    * @throws Throwable if an exception occurs invoking the underlying method
    */
    public Object invokeOnTarget(Object target) throws Throwable {
        try {
            return method.invoke(target, parameters);
        } catch (Throwable th) {
            Throwable throwEx = th;
            if (exceptionHandler != null && exceptionHandler.canHandleException(th)) {
                try {
                    Object handlerReturnedValue = exceptionHandler.handleException(th);
                    return handlerReturnedValue;
                } catch (Throwable handlerRethrownEx) {
                    throwEx = handlerRethrownEx;
                }
            }
            throw throwEx;
        }
    }
}
