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
package org.beanplanet.core.lang.proxy;

import java.lang.reflect.InvocationTargetException;
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
        } catch (InvocationTargetException itEx) {
            throw itEx.getTargetException();
        }
    }
}
