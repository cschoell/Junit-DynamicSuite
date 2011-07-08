package org.junit.extensions.dynamicsuite;

/**
 * Copyright 2011 Christof Schoell
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
 *  Implement to DirectoryFilter Classes found in a Directory
 */
public interface TestDirectoryFilter extends TestClassFilter {

    /**
     * @return the Path which should be searched for Classes to DirectoryFilter
     */
    String getBasePath();

    /**
     * Filters Classes (by Name) found in the test Directory before they get loaded by the ClassLoader
     *
     * @param className name of the class including its package
     * @return true if the class should be included in the TestSuite, false otherwise
     */
    boolean include(String className);

}
