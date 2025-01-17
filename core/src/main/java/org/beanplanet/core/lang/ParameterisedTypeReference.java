package org.beanplanet.core.lang;

/**
 * <p>
 * This abstract class allows for subclassing with an 'actual' {@link java.lang.reflect.Type} so that the generic
 * type information is captured at compile time (i.e. burned into this superclass), which may then be referenced at runtime.
 * </p>
 * <pre>
 *
 * ParameterisedTypeReference&lt;List&lt;Person&gt;&gt; listOfPersonType = new ParameterisedTypeReference&lt;List&lt;Person&gt;&gt;(){}
 * </pre>
 * <p>
 * Subclassing in this way is necessary so that the actual type can be captured at compile time. In the example above,
 * the following would confirm the actual type, <code>List&lt;Person&gt;</code>:
 * </p>
 * <pre>
 *
 * assert listOfPersonType.getGenericSuperclass().getTypeArguments()[0] == Person.class
 * </pre>
 */
public abstract class ParameterisedTypeReference<T> {
    public ParameterisedTypeReference() {
    }
}
