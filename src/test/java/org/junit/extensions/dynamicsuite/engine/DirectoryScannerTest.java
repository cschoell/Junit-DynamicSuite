package org.junit.extensions.dynamicsuite.engine;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
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
    private File basePath2 = new File("target" + separator + " testDirectoryLoader2");
    private File outsideBasePath = new File("target" + separator + "testDirectoryLoaderOutside");

    private String[][] testFileList = new String[][]{
            {"abc" + separator + "def", "filename.class"},
            {"abc" + separator + "def", "anothername.class"},
            {"abc" + separator + "def", "SomeClass.java"},
            {"abc" + separator + "def", "AnotherClass.java"},
            {"empty", ""}
    };
    
    private String[][] testFileList2 = new String[][]{
            {"abc" + separator + "def", "somefile.class"},
            {"abc" + separator + "def", "anothername.class"},
            {"abc" + separator + "def", "AnyClass.java"},
            {"abc" + separator + "def", "AnotherClass.java"},
            {"empty", ""}
    };

    @Before
    public void setUp() throws Exception {
        createFileStructure();

        directoryScanner = new DirectoryScanner(basePath);

    }

    private void createFileStructure() throws IOException {
        outsideBasePath.mkdirs();

        createFiles(basePath, testFileList);
        createFiles(basePath2, testFileList2);
    }

    private void createFiles(File base, String[][] fileList) throws IOException {
        base.mkdirs();

        for (String[] testFile : fileList) {
            String path = testFile[0];
            String name = testFile[1];
            File directory = new File(base, path);
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
        checkClassNamesCorrectlyRead(directoryScanner, testFileList, 0);
    }

    private void checkClassNamesCorrectlyRead(DirectoryScanner scanner, String[][] testFiles, int expectedDuplicates) {
        int expectedFileCount = 0;
        List<String> classNames = scanner.listClassNames();
        for (String[] testFile : testFiles) {
            String path = testFile[0];
            String name = testFile[1];
            if (StringUtils.isNotBlank(name)) {
                String expected  = toClassName(path, name);
                assertTrue("ClassName was not loaded by DirectoryScanner: " + expected, classNames.contains(expected));
                expectedFileCount++;
            }
        }
        assertEquals(expectedFileCount - expectedDuplicates, scanner.listClassNames().size());
    }

    @Test
    public void testMultiDirClassNamesCorrectlyRead() throws Exception {
        DirectoryScanner dirScanner = new DirectoryScanner(new File[] {basePath, basePath2});
        String[][] allTestFiles = ArrayUtils.addAll(testFileList, testFileList2);
        checkClassNamesCorrectlyRead(dirScanner, allTestFiles, 2);
    }

    private String toClassName(String path, String name) {
        return StringUtils.replace(path, separator, ".") + "." + StringUtils.removeEnd(StringUtils.removeEnd(name, ".class"), ".java");
    }

}
