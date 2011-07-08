package org.junit.extensions.dynamicsuite;

import org.junit.extensions.dynamicsuite.filter.DefaultFilter;
import org.junit.runner.RunWith;

@RunWith(DynamicSuite.class)
@DirectoryFilter(DynamicTestSuite.class)
public class DynamicTestSuite extends DefaultFilter {

    @Override
    public String getBasePath() {
        return super.getBasePath();
    }

    @Override
    public boolean include(String className) {
        return super.include(className);
    }

    public boolean include(Class cls) {
        return super.include(cls);
    }
}
