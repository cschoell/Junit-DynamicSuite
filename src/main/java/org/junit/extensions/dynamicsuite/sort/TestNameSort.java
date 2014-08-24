package org.junit.extensions.dynamicsuite.sort;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * User: Chris
 * Date: 24.08.14
 * Time: 18:00
 */
public class TestNameSort implements TestSort {
    @Override
    public void sort(List<Class<?>> classes) {
        Collections.sort(classes, new Comparator<Class<?>>() {
            @Override
            public int compare(Class<?> o1, Class<?> o2) {
                return o1.getSimpleName().compareTo(o2.getSimpleName());
            }
        });
    }
}
