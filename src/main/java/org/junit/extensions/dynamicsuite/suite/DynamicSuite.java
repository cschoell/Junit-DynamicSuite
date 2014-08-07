package org.junit.extensions.dynamicsuite.suite;

import org.junit.extensions.dynamicsuite.Filter;
import org.junit.extensions.dynamicsuite.Sort;
import org.junit.extensions.dynamicsuite.TestClassFilter;
import org.junit.extensions.dynamicsuite.engine.ClassScanner;
import org.junit.extensions.dynamicsuite.engine.ScannerFactory;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

/**
 * Copyright 2011 Christof Schoell
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class DynamicSuite extends ParentRunner<Runner> {

    public static final Logger log = Logger.getLogger(DynamicSuite.class.getName());
    private List<Runner> fRunners;

    private Class<?> testedClass;

    private List<Class<?>> filteredClasses = new ArrayList<Class<?>>();

    private TestClassFilter filter;

    private File basePath;

    /**
     * Constructs a new {@code ParentRunner} that will run {@code @TestClass}
     *
     * @throws org.junit.runners.model.InitializationError
     *
     */
    protected DynamicSuite(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    public DynamicSuite(Class<?> klass, RunnerBuilder builder) throws InitializationError {
        this(klass);
        testedClass = klass;

        loadTestClasses();

        fRunners = builder.runners(klass, filteredClasses.toArray(new Class<?>[filteredClasses.size()]));
    }

    private void loadTestClasses() throws InitializationError {
        ClassScanner scanner = null;
        try {
            scanner = ScannerFactory.getInstance().createScanner(testedClass);

            List<String> classNames = scanner.listClassNames();
            for (String name : classNames) {
                TestClassFilter testFilter = getFilter();
                if (testFilter.include(name)) {
                    filterByClass(name);
                }
            }
            Sort sortAnnotation = testedClass.getAnnotation(Sort.class);
            if (sortAnnotation != null) {
                switch (sortAnnotation.value()) {
                    case TESTNAME:
                        doSortByName();
                        break;
                    case RANDOM:
                        doSortByRandom();
                        break;
                }

            }
        } catch (Exception e) {
            throw new InitializationError(e);
        }

    }

    private void doSortByRandom() {
        Collections.shuffle(filteredClasses, new java.util.Random(System.currentTimeMillis()));
    }

    private void doSortByName() {
        Collections.sort(filteredClasses, new Comparator<Class<?>>() {
            @Override
            public int compare(Class<?> o1, Class<?> o2) {
                return o1.getSimpleName().compareTo(o2.getSimpleName());
            }
        });
    }


    private void filterByClass(String className) {
        try {
            final Class<?> newTestClass = ClassLoader.getSystemClassLoader().loadClass(className);
            if (filter.include(newTestClass)) {
                filteredClasses.add(newTestClass);
            }
        } catch (ClassNotFoundException e) {
            log.info(e.getMessage());
        }
    }


    private Class<? extends TestClassFilter> getAnnotatedFilterClass() throws InitializationError {
        Filter annotation = testedClass.getAnnotation(Filter.class);
        if (annotation == null) {
            String string = String.format("class '%s' must have a Filter annotation", testedClass.getName());
            System.err.println(string);
            throw new InitializationError(string);
        }
        return annotation.value();
    }


    private TestClassFilter getFilter() throws InitializationError {
        if (filter == null) {
            try {
                filter = getAnnotatedFilterClass().newInstance();
            } catch (InitializationError e) {
                throw e;
            }catch (Exception e) {
                throw new InitializationError(e);
            }
        }
        return filter;
    }

    @Override
    protected List<Runner> getChildren() {
        return fRunners;
    }

    @Override
    protected Description describeChild(Runner child) {
        return child.getDescription();
    }

    @Override
    protected void runChild(Runner runner, final RunNotifier notifier) {
        runner.run(notifier);
    }
}
