package org.junit.extensions.dynamicsuite.engine;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static java.io.File.separator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Copyright 2014 Christof Schoell
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
 */
public class DirectoryScannerTest {

    private DirectoryScanner directoryScanner;
    private File basePath = new File("target" + separator + " testDirectoryLoader");
    private File outsideBasePath = new File("target" + separator + "testDirectoryLoaderOutside");

    private String[][] testFileList = new String[][]{
            {"abc" + separator + "def", "filename.class"},
            {"abc" + separator + "def", "anothername.class"},
            {"abc" + separator + "def", "SomeClass.java"},
            {"abc" + separator + "def", "AnotherClass.java"},
            {"empty", ""}
    };

    @Before
    public void setUp() throws Exception {
        createFileStructure();

        directoryScanner = new DirectoryScanner(basePath);

    }

    private void createFileStructure() throws IOException {
        basePath.mkdirs();
        outsideBasePath.mkdirs();

        for (String[] testFile : testFileList) {
            String path = testFile[0];
            String name = testFile[1];
            File directory = new File(basePath, path);
            directory.mkdirs();
            if (StringUtils.isNotBlank(name)) {
                new File(directory, name).createNewFile();
            }
        }
    }

    @After
    public void tearDown() throws Exception {
        FileUtils.deleteQuietly(basePath);
        FileUtils.deleteQuietly(outsideBasePath);

    }

    @Test
    public void testClassNamesCorrectlyRead() throws Exception {
        List<String> classNames = directoryScanner.listClassNames();
        int expectedFileCount = 0;
        for (String[] testFile : testFileList) {
            String path = testFile[0];
            String name = testFile[1];
            if (StringUtils.isNotBlank(name)) {
                String expected  = toClassName(path, name);
                assertTrue("ClassName was not loaded by DirectoryScanner: " + expected, classNames.contains(expected));
                expectedFileCount++;
            }
        }
        assertEquals(expectedFileCount, directoryScanner.listClassNames().size());
    }

    private String toClassName(String path, String name) {
        return StringUtils.replace(path, separator, ".") + "." + StringUtils.removeEnd(StringUtils.removeEnd(name, ".class"), ".java");
    }

}
