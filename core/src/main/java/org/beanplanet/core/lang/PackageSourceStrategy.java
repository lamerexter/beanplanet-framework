package org.beanplanet.core.lang;

/**
 * A strategy for supplying a number of package names.
 *
 * @author Gary Watson
 */
public interface PackageSourceStrategy {
    String [] findPackages();
}
