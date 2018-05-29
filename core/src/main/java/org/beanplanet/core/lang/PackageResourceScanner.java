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

import java.util.Set;



/**
 * Defines a strategy for scanning for and returning resources within packages.
 *  
 * @author Gary Watson
 */
public interface PackageResourceScanner<R> {
   /**
    * Scans the packages, whose names are determined by the given strategy, for resources of the required type according to the
    * filter provided.
    *
    * @param packageSourceStrategy the strategy used to determine the package names
    * @return a set of the resources found in the given packages, or the empty set if no resources were found.
    */
    Set<R> findResourcesInPackages(PackageSourceStrategy packageSourceStrategy);

   /**
    * Scans the specified named packages for resources of the required type according to the
    * filter provided.
    * 
    * @param packageNames the names of the packages to be scanned
    * @return a set of the resources found in the given packages, or the empty set if no resources were found.
    */
   Set<R> findResourcesInPackages(String... packageNames);
}
