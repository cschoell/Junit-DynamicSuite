package org.junit.extensions.dynamicsuite.engine;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Copyright 2014 Christof Schoell
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
public class ClassPathScanner implements ClassScanner {

    private List<String> foundClasses = new ArrayList<String>();

    private final boolean includeJars;

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
        List<String> classPathEntries = getClassPathEntries();
        scanForClasses(classPathEntries);
    }

    private List<String> getClassPathEntries() {
        String separator = getPathSeparator();
        String classpath = getClassPathString();

        StringTokenizer tokenizer = new StringTokenizer(classpath, separator);
        List<String> classPathEntries = new ArrayList<String>();

        while(tokenizer.hasMoreElements()) {
            String entry = tokenizer.nextToken();
            classPathEntries.add(entry);
        }
        return classPathEntries;
    }

    private void scanForClasses(List<String> classPathEntries) {
        for (String entry : classPathEntries) {
            File entryFile = new File(entry);
            if (entryFile.isDirectory()) {
                addDirectory(entryFile);
            } else if (includeJars) {
                addJar(entryFile);
            }


        }
    }

    private void addDirectory(File entryFile) {
        DirectoryScanner directoryScanner = new DirectoryScanner(entryFile);
        foundClasses.addAll(directoryScanner.listClassNames());
    }

    private void addJar(File fromFile) {

        JarFile jar = loadJarFile(fromFile);

        if (jar != null) {
            loadJarEntries(jar);
        }
    }

    private void loadJarEntries(JarFile jar) {
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {

            JarEntry element = entries.nextElement();
            String name = element.getName();
            if (name.toLowerCase().endsWith(".class")) {
                String className = StringUtils.replace(name, "/", ".");
                className = StringUtils.removeEnd(className, ".class");
                foundClasses.add(className);
            }
        }
    }

    private JarFile loadJarFile(File jarFile) {
        try {
            URL jarURL = new URL ("file:" + jarFile.getCanonicalPath ());
            jarURL = new URL("jar:" + jarURL.toExternalForm () + "!/");
			JarURLConnection conn = (JarURLConnection) jarURL.openConnection ();
			return conn.getJarFile();
		}
		catch (Exception e) {
            return null;
        }
    }

    protected String getClassPathString() {
        return System.getProperty("java.class.path");
    }

    protected String getPathSeparator() {
        return System.getProperty("path.separator");
    }
}
