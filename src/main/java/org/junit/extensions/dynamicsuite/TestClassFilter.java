package org.junit.extensions.dynamicsuite;

/**
 * Copyright 2014 Christof Schöll
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * implement to filter test classes found by the DynamicSuite runner
 */
public interface TestClassFilter {

    /**
     * Filters Classes (by Name) found before they get loaded by the ClassLoader
     *
     * @param className name of the class including its package
     * @return true if the class should be included in the TestSuite, false otherwise
     */
    boolean include(String className);

    /**
     *  classes which are found by the ClassLookup algorithm are
     *  passed to this method for further filtering
     *
     * @param cls classes which has been found by the class lookup Mechanism
     * @return true if it is a test class that should be included in the test suite, false otherwise
     */
    boolean include(Class cls);
}
