package org.junit.extensions.dynamicsuite.filter;

import org.junit.extensions.dynamicsuite.TestDirectoryFilter;

/**
 *  @author Christof Schoell
 *
 */
public class DefaultFilter implements TestDirectoryFilter {

    public String getBasePath() {
        return "target/test-classes";
    }

    public boolean include(String className) {
        return className.endsWith("Test");
    }

    public boolean include(Class cls) {
        return true;
    }

}
