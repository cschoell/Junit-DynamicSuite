package org.junit.extensions.dynamicsuite.engine;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static java.io.File.separator;

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
@RunWith(Parameterized.class)
public class ExtractClassNameTest {

    @Parameterized.Parameters
    public static List<String[]> data() {
        return Arrays.asList(new String[][]{{
                "C:" + separator, "C:" + separator + "somewhere" + separator + "at" + separator + "file.class", "somewhere.at.file"},
                {"", "some" + separator + "file", "some.file"},
                {separator + "somewhere", separator + "somewhere" + separator + "some.class", "some"},
                {"", "", ""}
        });
    }


    private String basePath;
    private String classFileName;
    private String expected;

    public ExtractClassNameTest(String basePath, String classFileName, String expected) {
        this.basePath = basePath;
        this.classFileName = classFileName;
        this.expected = expected;
    }

    private DirectoryScanner directoryScanner;


    @Before
    public void setUp
            () throws Exception {
        directoryScanner = new DirectoryScanner(new File(""));
    }

    @Test
    public void testExtractClassName
            () throws Exception {
        Assert.assertEquals(expected, directoryScanner.extractClassName(basePath, classFileName));
    }

}
