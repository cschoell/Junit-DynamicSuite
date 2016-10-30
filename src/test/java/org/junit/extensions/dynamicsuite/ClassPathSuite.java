package org.junit.extensions.dynamicsuite;

import org.junit.extensions.dynamicsuite.filter.DefaultFilter;
import org.junit.extensions.dynamicsuite.suite.DynamicSuite;
import org.junit.runner.RunWith;

/**
 *
 */
@ClassPath(includeJars = true)
@RunWith(DynamicSuite.class)
@Filter(ClassPathSuite.class)
public class ClassPathSuite extends DefaultFilter{

    @Override
    public boolean include(String className) {
        return super.include(className) &&
                className.startsWith("org.junit.extensions.dynamicsuite")
                || className.startsWith("com.github.cschoell");
    }
}
