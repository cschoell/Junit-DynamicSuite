package org.junit.extensions.dynamicsuite;

/**
 *  @author Christof Schoell
 *
 *  Implement to DirectoryFilter Classes found in a Directory
 */
public interface TestDirectoryFilter extends TestClassFilter {

    /**
     * @return the Path which should be searched for Classes to DirectoryFilter
     */
    String getBasePath();

    /**
     * Filters Classes (by Name) found in the test Directory before they get loaded by the ClassLoader
     *
     * @param className name of the class including its package
     * @return true if the class should be included in the TestSuite, false otherwise
     */
    boolean include(String className);

}
