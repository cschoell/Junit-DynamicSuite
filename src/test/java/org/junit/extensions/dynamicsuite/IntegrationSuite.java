package org.junit.extensions.dynamicsuite;

import org.junit.extensions.dynamicsuite.filter.DefaultFilter;
import org.junit.runner.RunWith;

/**
 * User: Chris
 * Date: 01.07.11
 * Time: 09:43
 */
@RunWith(DynamicSuite.class)
@DirectoryFilter(IntegrationSuite.class)
public class IntegrationSuite extends DefaultFilter {

    @Override
    public boolean include(String className) {
        return className.endsWith("Test");
    }
}
