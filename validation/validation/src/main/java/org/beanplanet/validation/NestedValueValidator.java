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

package org.beanplanet.validation;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Use this validator to validate a nested property value of an object. For example, given an entity, <code>person</code>
 * of type <cod>Person</cod>, this validator can be used to validate <code>person.address</code> by specifying the
 * value provider for <code>address</code> (of type <code>Address</code>), as follows:
 * <pre>
 * new NestedPropertyValidator&lt;Person, Address&gt;(Person::getAddress, new AddressValidator())
 * </pre>
 * <p>
 * The first parameter method function which takes a <code>Person</code> and returns the <code>Address</code> property.
 * The second parameter is a validator of <code>Address</code> to which this validator delegates.
 * </p>
 */
public class NestedValueValidator<T, P> extends CompositeValidator<T> {
    /**
     * Conditionally performs validation of a provided value from an enclosing or parent object, by delegating
     * to the given delegates.
     *
     * @param condition          a predicate which determines when validation should be performed on the parent object,
     *                           which may be null to indicate always.
     * @param stopOnFirstError   whether this composite validator will stop on the first validation error produced.
     * @param valueProvider      a supplier of the value to be validated, as a function of the parent object.
     * @param delegateValidators a list of the delegate validators called to validate the computed value.
     */
    public NestedValueValidator(final Predicate<T> condition,
                                final boolean stopOnFirstError,
                                final Function<T, P> valueProvider,
                                final Validator<P>... delegateValidators) {
        super(condition, stopOnFirstError, valueProvider, delegateValidators);
    }

    /**
     * Conditionally performs validation of a provided value from an enclosing or parent object, by delegating
     * to the given delegates.
     *
     * @param condition          a predicate which determines when validation should be performed on the parent object,
     *                           which may be null to indicate always.
     * @param valueProvider      a supplier of the value to be validated, as a function of the parent object.
     * @param delegateValidators a list of the delegate validators called to validate the computed value.
     * @see #NestedValueValidator(Predicate, boolean, Function, Validator[])
     */
    public NestedValueValidator(final Predicate<T> condition,
                                final Function<T, P> valueProvider,
                                final Validator<P>... delegateValidators) {
        this(condition, false, valueProvider, delegateValidators);
    }

    /**
     * Performs validation of a provided value from an enclosing or parent object, by delegating
     * to the given delegates.
     *
     * @param valueProvider      a supplier of the value to be validated, as a function of the parent object.
     * @param delegateValidators a list of the delegate validators called to validate the computed value.
     * @see #NestedValueValidator(Predicate, boolean, Function, Validator[])
     */
    public NestedValueValidator(final Function<T, P> valueProvider, final Validator<P>... delegateValidators) {
        this(null, false, valueProvider, delegateValidators);
    }
}
