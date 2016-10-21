package org.junit.extensions.dynamicsuite.engine;

import org.junit.Before;
import org.junit.Test;
import org.junit.extensions.dynamicsuite.ClassPath;
import org.junit.extensions.dynamicsuite.Directory;
import org.junit.extensions.dynamicsuite.Filter;
import org.junit.extensions.dynamicsuite.filter.DefaultFilter;

import static org.junit.Assert.assertTrue;

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
public class ScannerFactoryTest {

    private ScannerFactory underTest;

    @Before
    public void setUp() throws Exception {
        underTest = ScannerFactory.getInstance();
    }

    @Test
    public void testCreateDirectoryScanner() throws Exception {
        ClassScanner scanner = underTest.createScanner(DirectoryFilterAnnotated.class);
        assertTrue(scanner instanceof DirectoryScanner);
    }

    @Test
    public void testCreateClassPathScanner() throws Exception {
        ClassScanner scanner = underTest.createScanner(ClassPathFilterAnnotated.class);
        assertTrue(scanner instanceof ClassPathScanner);
        ClassPathScanner cpScanner = (ClassPathScanner) scanner;
        assertTrue(cpScanner.isIncludeJars());
    }

    @Filter(DefaultFilter.class)
    @Directory
    class DirectoryFilterAnnotated {}

    @Filter(DefaultFilter.class)
    @ClassPath(includeJars = true)
    class ClassPathFilterAnnotated {}

}
