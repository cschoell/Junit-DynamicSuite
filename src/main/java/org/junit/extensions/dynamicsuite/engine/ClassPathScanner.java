package org.junit.extensions.dynamicsuite.engine;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

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
    private String[] metaClasspathToInclude;

    public ClassPathScanner(boolean includeJars) {
        this(includeJars, new String[0]);
    }

    public ClassPathScanner(boolean includeJars, String[] metaClasspathToInclude) {
        this.includeJars = includeJars;
        this.metaClasspathToInclude = metaClasspathToInclude;
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
        List<String> classPathEntries = new ArrayList<String>();
        classPathEntries = addCPFromJarMetaInf(classPathEntries);
        return addFromCPString(separator, classpath, classPathEntries, null);
    }

    private List<String> addCPFromJarMetaInf(List<String> classPathEntries) {
        for (String cpMetaJar : metaClasspathToInclude) {
            File found = findCPMetaJarFile(cpMetaJar);
            JarFile jarFile = loadJarFile(found);
            try {
                if (jarFile == null || jarFile.getManifest() == null || jarFile.getManifest().getEntries() == null) {
                    System.err.println("Could not read Jar File " + cpMetaJar + " to get Meta Class-Path from");
                } else {
                    Manifest manifest = jarFile.getManifest();
                    Attributes attributes = manifest.getMainAttributes();
                    String cpToAdd = attributes.getValue(Attributes.Name.CLASS_PATH);
                    addFromCPString(" ", cpToAdd, classPathEntries, new File(cpMetaJar).getParentFile());
                }

            } catch (IOException e) {
                System.err.println("Could not read Jar File " + cpMetaJar + " to get ClassPath from META-INF");
            }
        }
        return classPathEntries;
    }

    private File findCPMetaJarFile(String cpMetaJar) {
        URL resource = getClass().getClassLoader().getResource(cpMetaJar);
        File found = null;
        if (resource != null && resource.getFile() != null) {
            found = new File(resource.getFile());
        } else {
            found = new File(findAbsoluteOrRelative(new File(""), cpMetaJar));
        }
        return found;
    }

    private List<String> addFromCPString(String separator, String classpath, List<String> classPathEntries, File parentFile) {
        StringTokenizer tokenizer = new StringTokenizer(classpath, separator);

        while (tokenizer.hasMoreElements()) {
            String entry = tokenizer.nextToken();
            entry = findAbsoluteOrRelative(parentFile, entry);
            classPathEntries.add(entry);
        }
        return classPathEntries;
    }

    private String findAbsoluteOrRelative(File parentFile, String entry) {
        String fromUrl = tryAsUrl(entry);
        if (fromUrl != null) return fromUrl;

        if (parentFile != null) {
            File file = new File(entry);
            if (!file.exists()) {
                file = new File(parentFile, entry);
                entry = file.getAbsolutePath();
            }
        }
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
            URL jarURL = new URL("file:" + jarFile.getCanonicalPath());
            jarURL = new URL("jar:" + jarURL.toExternalForm() + "!/");
            JarURLConnection conn = (JarURLConnection) jarURL.openConnection();
            return conn.getJarFile();
        } catch (Exception e) {
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
