package org.junit.extensions.dynamicsuite.engine;

import org.junit.extensions.dynamicsuite.ClassPath;
import org.junit.extensions.dynamicsuite.Directories;
import org.junit.extensions.dynamicsuite.Directory;

import java.io.File;

/**
 * Copyright 2014 Christof Schoell
 * <p></p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p></p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p></p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class ScannerFactory {

    private final static ScannerFactory instance = new ScannerFactory();

    public static ScannerFactory getInstance() {
        return instance;
    }

    private ScannerFactory() {
    }

    public ClassScanner createScanner(Class filterAnnotatedClass) throws IllegalAccessException, InstantiationException {
        ClassScanner directoryScanner = createDirectoryScanner(filterAnnotatedClass);
        if (directoryScanner == null) {
            return createClassPathScanner(filterAnnotatedClass);
        }
        return directoryScanner;
    }

    private ClassScanner createDirectoryScanner(Class filterAnnotatedClass) throws InstantiationException, IllegalAccessException {
        Directory annotation = (Directory) filterAnnotatedClass.getAnnotation(Directory.class);

        if (annotation != null) {
            return new DirectoryScanner(new File(annotation.value()));
        } else {
            Directories dirs = (Directories) filterAnnotatedClass.getAnnotation(Directories.class);
            if (dirs != null) {
                File[] paths = new File[dirs.value().length];
                for (int i = 0; i < dirs.value().length; i++) {
                    paths[i] = new File(dirs.value()[i]);
                }
                return new DirectoryScanner(paths);
            }
        }

        return null;
    }

    private ClassScanner createClassPathScanner(Class filterAnnotatedClass) throws InstantiationException, IllegalAccessException {
        ClassPath annotation = (ClassPath) filterAnnotatedClass.getAnnotation(ClassPath.class);

        if (annotation != null) {
            return new ClassPathScanner(annotation.includeJars());
        }
        return null;
    }

}
