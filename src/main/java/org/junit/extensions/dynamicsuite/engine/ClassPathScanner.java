package org.junit.extensions.dynamicsuite.engine;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

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
public class ClassPathScanner implements ClassScanner {

    private final boolean includeJars;
    private List<String> foundClasses = new ArrayList<String>();

    public ClassPathScanner(boolean includeJars) {
        this.includeJars = includeJars;
        init();

    }

    public List<String> listClassNames() {
        return foundClasses;
    }

    public boolean isIncludeJars() {
        return includeJars;
    }

    private void init() {
        final List<String> classPathEntries = getClassPathEntries();
        scanForClasses(classPathEntries);
    }

    private List<String> getClassPathEntries() {

        String separator = getPathSeparator();
        String classpath = getClassPathString();
        List<String> classPathEntries = new ArrayList<String>();
        return addFromCPString(separator, classpath, classPathEntries);
    }

    private List<String> addFromCPString(String separator, String classpath, List<String> classPathEntries) {
        StringTokenizer tokenizer = new StringTokenizer(classpath, separator);

        while (tokenizer.hasMoreElements()) {
            String entry = tokenizer.nextToken();
            entry = findAbsoluteOrRelative(entry);
            classPathEntries.add(entry);
        }
        return classPathEntries;
    }

    private String findAbsoluteOrRelative(String entry) {
        String fromUrl = tryAsUrl(entry);
        if (fromUrl != null) return fromUrl;
        return entry;
    }

    private String tryAsUrl(String entry) {
        try {
            URL url = new URL(entry);
            String file = url.getFile();
            if (new File(file).exists()) return file;
        } catch (MalformedURLException ignore) {
        }
        return null;
    }

    private void scanForClasses(List<String> classPathEntries) {


        if (includeJars) {
            FastClasspathScanner fastClasspathScanner = new FastClasspathScanner();
            foundClasses = fastClasspathScanner.scan(4).getNamesOfAllStandardClasses();
        } else {
            for (String entry : classPathEntries) {
                File entryFile = new File(entry);
                if (entryFile.isDirectory()) {
                    addDirectory(entryFile);
                }
            }
        }
    }

    private void addDirectory(File entryFile) {
        DirectoryScanner directoryScanner = new DirectoryScanner(entryFile);
        foundClasses.addAll(directoryScanner.listClassNames());
    }

    protected String getClassPathString() {
        return System.getProperty("java.class.path");
    }

    protected String getPathSeparator() {
        return System.getProperty("path.separator");
    }
}
