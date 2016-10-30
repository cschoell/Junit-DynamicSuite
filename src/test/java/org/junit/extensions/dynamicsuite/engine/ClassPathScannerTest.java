package org.junit.extensions.dynamicsuite.engine;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
@RunWith(JUnit4.class)
public class ClassPathScannerTest {

    @Test
    public void testLoadFromDirectories() throws Exception {
        List<String> classes = loadClasses(false);
        assertTrue(classes.contains(ClassPathScannerTest.class.getName()));
    }

    @Test
    public void testLoadLibraryClassFromDirectories() throws Exception {
        List<String> classes = loadClasses(false);
        assertFalse(classes.contains(String.class.getName()));

    }

    @Test
    public void testLoadFromLibrary() throws Exception {
        List<String> classes = loadClasses(true);
        assertTrue(classes.contains(StringUtils.class.getName()));
    }
    

    private List<String> loadClasses(boolean includeJars) {
        return loadClasses(includeJars, new String[0]);
    }

    private List<String> loadClasses(boolean includeJars, String... cpJars) {
        ClassPathScanner scanner = new ClassPathScanner(includeJars);
        List<String> classes = scanner.listClassNames();

        assertNotNull(classes);
        return classes;
    }
}
