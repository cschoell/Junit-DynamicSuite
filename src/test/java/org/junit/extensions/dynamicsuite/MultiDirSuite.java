package org.junit.extensions.dynamicsuite;

import org.junit.extensions.dynamicsuite.suite.DynamicSuite;
import org.junit.runner.RunWith;

/**
 * Copyright 2014 Christof Schoell
 * <p></p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p></p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p></p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@RunWith(DynamicSuite.class)
@Filter(MultiDirSuite.class)
@Directories({"target/test-classes", "src/test/java"})
@Sort(SortBy.TESTNAME)
public class MultiDirSuite implements TestClassFilter {


    @Override
    public boolean include(String className) {
        return className.endsWith("ITCase");
    }


    @Override
    public boolean include(Class cls) {
        return cls.getAnnotation(Slow.class) == null;
    }
}
