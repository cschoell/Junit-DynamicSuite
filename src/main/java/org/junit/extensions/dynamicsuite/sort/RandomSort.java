package org.junit.extensions.dynamicsuite.sort;

import java.util.Collections;
import java.util.List;

/**
 * User: Chris
 * Date: 24.08.14
 * Time: 18:01
 */
public class RandomSort implements TestSort {
    @Override
    public void sort(List<Class<?>> classes) {
        Collections.shuffle(classes, new java.util.Random(System.currentTimeMillis()));
    }
}
