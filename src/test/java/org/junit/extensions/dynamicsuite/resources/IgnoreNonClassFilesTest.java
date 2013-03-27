package org.junit.extensions.dynamicsuite.resources;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.extensions.dynamicsuite.engine.DirectoryScanner;

import java.io.File;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;


public class IgnoreNonClassFilesTest {

    @Test
    public void onlyThisTestClassShouldBeFound() throws Exception {
        final String packagee = getClass().getPackage().getName();
        final String packageAsPath = StringUtils.replace(packagee, ".", "/");
        final String directory = "target/test-classes/" + packageAsPath;

        final DirectoryScanner scanner = new DirectoryScanner(new File(directory));

        assertThat(scanner.listClassNames().size(), is(1));
    }

}
