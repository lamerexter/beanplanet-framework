package org.beanplanet.core.lang;

/**
 * A tagging structural interface, implemented by objects that are able to be shallow copied.
 *
 * <p>An object is capable of being shallow copied if it can produce a new instance of itself, initialised with the values of
 * both it's value members (primitives) and reference type members (objects, classes and arrays). There is an important distinction between shallow and deep copies:
 * shallow copy copies the 'values' of Reference type members (i.e. the pointer, not the whole object); depp copy copies or 'clones' the referenced object.
 * </p>
 */
public interface ShallowCopyable<T> {
    /**
     * Performs shallow copy of this object, initialised with value copies of its members - including the values of pointers to referenced objects (i.e. referred objects are not created).
     *
     * @return a new shallow-copied instance.
     */
    T copyShallow();
}
