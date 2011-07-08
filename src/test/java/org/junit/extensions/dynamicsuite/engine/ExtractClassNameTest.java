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
 * User: Chris
 * Date: 08.07.11
 * Time: 12:38
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

    private DirectoryLoader directoryLoader;


    @Before
    public void setUp
            () throws Exception {
        directoryLoader = new DirectoryLoader(new File(""));
    }

    @Test
    public void testExtractClassName
            () throws Exception {
        Assert.assertEquals(expected, directoryLoader.extractClassName(basePath, classFileName));
    }

}
