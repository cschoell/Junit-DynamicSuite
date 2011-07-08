package org.junit.extensions.dynamicsuite;

import org.junit.extensions.dynamicsuite.engine.DirectoryLoader;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Copyright 2011 Christof Schoell
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
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

    private TestDirectoryFilter filter;

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
        DirectoryLoader loader = new DirectoryLoader(getBasePath());
        List<String> classNames = loader.getClassNames();
        for (String name : classNames) {
            TestDirectoryFilter testFilter = getFilter();
            if (testFilter.include(name)) {
                filterByClass(name);
            }
        }
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

    private File getBasePath() throws InitializationError {
        if (basePath == null) {
            basePath = new File(getFilter().getBasePath());
        }
        return basePath;
    }


    private Class<? extends TestDirectoryFilter> getAnnotatedFilterClass() throws InitializationError {
        DirectoryFilter annotation = testedClass.getAnnotation(DirectoryFilter.class);
        if (annotation == null)
            throw new InitializationError(String.format("class '%s' must have a DirectoryFilter annotation", testedClass.getName()));
        return annotation.value();
    }


    private TestDirectoryFilter getFilter() throws InitializationError {
        if (filter == null) {
            try {
                filter = getAnnotatedFilterClass().newInstance();
            } catch (Exception e) {
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
