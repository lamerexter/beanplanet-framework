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

   Set<R> findResourcesInPackage(String packageName, ClassLoader classLoader);
}
