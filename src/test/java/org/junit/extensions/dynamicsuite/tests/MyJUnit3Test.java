package org.junit.extensions.dynamicsuite.tests;

import junit.framework.TestCase;

public class MyJUnit3Test extends TestCase {

    public void testName() throws Exception {
        System.out.println(getName());
        assertTrue(true);
    }
}
